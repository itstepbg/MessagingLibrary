package library.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import library.models.network.MessageType;
import library.models.network.NetworkMessage;
import library.util.FileUtils;

public class FileThread extends Thread {

	public static final int MODE_UPLOAD = 0;
	public static final int MODE_DOWNLOAD = 1;

	private CommunicationInterface communicationListener;

	private BlockingQueue<String> fileChunks = new ArrayBlockingQueue<>(16);

	private String path;
	private InputStream inputStream;
	private OutputStream outputStream;

	private int mode;
	private int offset = 0;

	public FileThread(CommunicationInterface communicationListener, String path, int mode) {
		this.communicationListener = communicationListener;
		this.path = path;
		this.mode = mode;
	}

	@Override
	public void run() {
		if (mode == MODE_UPLOAD) {
			inputStream = FileUtils.getFileInputStrem(path);
			uploadFile();
		} else {
			outputStream = FileUtils.getFileOutputStream(path);
			downloadFile();
		}
	}

	public void addFileChunk(String fileChunk) {
		try {
			fileChunks.put(fileChunk);
		} catch (InterruptedException e) {
			// TODO Maybe something else is needed as well?
			try {
				inputStream.close();
			} catch (IOException ioe) {
				// TODO Auto-generated catch block
				ioe.printStackTrace();
			} finally {
				this.interrupt();
			}
		}
	}

	private void uploadFile() {
		// READING
		while (true) {
			String base64 = FileUtils.readFromFile(inputStream, offset);

			if (base64 == null) {
				base64 = FileUtils.EOF_TAG;
			} else if (FileUtils.calculateOriginalNumberOfBytes(base64) < FileUtils.FILE_READ_WRITE_BUFFER_SIZE) {
				base64 += FileUtils.EOF_TAG;
			} else {
				offset += FileUtils.FILE_READ_WRITE_BUFFER_SIZE;
			}

			NetworkMessage networkMessage = new NetworkMessage();
			networkMessage.setType(MessageType.UPLOAD_CHUNK);
			networkMessage.setText(base64);

			communicationListener.sendMessage(networkMessage);

			if (base64.endsWith(FileUtils.EOF_TAG)) {
				try {
					inputStream.close();
					// TODO Notify the other side that the file transfer was successfully completed.
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					interrupt();
				}
			}
		}
	}

	private void downloadFile() {
		// WRITING
		while (!Thread.interrupted()) {
			try {
				String chunk = fileChunks.take();
				if (chunk != null) {
					boolean eof = false;
					String base64 = chunk;
					if (chunk.endsWith(FileUtils.EOF_TAG)) {
						eof = true;
						base64 = chunk.replace(FileUtils.EOF_TAG, "");
					}

					FileUtils.writeToFile(outputStream, base64);

					if (eof) {
						try {
							outputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							// TODO Maybe delete the output file here as well?
							interrupt();
						}
						break;
					}
				}
			} catch (InterruptedException e) {
				// TODO Remove code repetition.
				try {
					outputStream.close();
				} catch (IOException ioe) {
					// TODO Auto-generated catch block
					ioe.printStackTrace();
				} finally {
					FileUtils.deleteFile(path);
					interrupt();
				}
				break;
			}
		}
	}
}
