package library.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import library.models.network.MessageType;
import library.models.network.NetworkMessage;
import library.util.FileUtils;
import library.util.MessagingLogger;

public class FileThread extends Thread {

	public static final int MODE_UPLOAD = 0;
	public static final int MODE_DOWNLOAD = 1;

	private CommunicationInterface communicationListener;

	private BlockingQueue<String> fileChunks = new ArrayBlockingQueue<>(1024);

	private String path;
	private InputStream inputStream;
	private OutputStream outputStream;

	private int mode;

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
				outputStream.close();
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
		while (!Thread.interrupted()) {
			String base64 = FileUtils.readFromFile(inputStream);

			NetworkMessage networkMessage = new NetworkMessage();
			networkMessage.setType(MessageType.UPLOAD_CHUNK);
			networkMessage.setText(base64);

			communicationListener.sendMessage(networkMessage);

			if (base64.equals(FileUtils.EOF_TAG)) {
				try {
					inputStream.close();
					MessagingLogger.getLogger().info("Closed file input.");
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
				String base64 = fileChunks.take();
				if (base64 != null) {
					if (base64.equals(FileUtils.EOF_TAG)) {
						try {
							outputStream.close();
							MessagingLogger.getLogger().info("Closed file output.");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							// TODO Maybe delete the output file here as well?
							interrupt();
						}
						break;
					} else {
						FileUtils.writeToFile(outputStream, base64);
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
