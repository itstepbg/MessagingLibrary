package library.networking;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

import library.models.network.MessageType;
import library.models.network.NetworkMessage;
import library.util.MessagingLogger;

public class CommonCommunication implements CommunicationInterface {

	public final static int HEARTBEAT_INTERVAL = 2;
	public final static int TIMEOUT_BUFFER_SIZE = 3;

	protected static Logger logger = MessagingLogger.getLogger();

	protected Socket communicationSocket;

	protected InputThread inputThread;
	protected OutputThread outputThread;
	protected HeartbeatThread heartbeatThread;
	protected FileThread fileThread;

	protected long messageCounter = 0;
	protected HashMap<Long, NetworkMessage> pendingRequests = new HashMap<>();

	public CommonCommunication(Socket communicationSocket) {
		this.communicationSocket = communicationSocket;

		inputThread = CommunicationThreadFactory.createInputThread(communicationSocket);
		outputThread = CommunicationThreadFactory.createOutputThread(communicationSocket);
		heartbeatThread = CommunicationThreadFactory.createHeartbeatThread(HEARTBEAT_INTERVAL);

		startCommunicationThreads();
	}

	protected void startCommunicationThreads() {
		inputThread.setCommunicationListener(this);
		outputThread.setCommunicationListener(this);
		heartbeatThread.setCommunicationListener(this);

		inputThread.start();
		outputThread.start();
		heartbeatThread.start();
	}

	@Override
	public void sendMessage(NetworkMessage networkMessage) {
		updateMessageCounter(networkMessage);
		outputThread.addMessage(networkMessage);
	}

	@Override
	public void handleMessage(NetworkMessage networkMessage) {
		// TODO This implementation will be empty for now. All common message handling
		// code should be moved here in the future.
	}

	@Override
	public void closeCommunication() {
		logger.info("Closing communication for " + communicationSocket.getInetAddress().getHostAddress());

		if (!communicationSocket.isClosed()) {
			try {
				communicationSocket.shutdownInput();
				communicationSocket.shutdownOutput();
				communicationSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		interruptThread(heartbeatThread);

		// TODO Maybe remove the code below? It is used as a failsafe now.
		interruptThread(inputThread);
		interruptThread(outputThread);
		// TODO Maybe the file thread reference should be removed when it dies?
		interruptThread(fileThread);
	}

	private void interruptThread(Thread thread) {
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
		}
	}

	@Override
	public void unregisterCommunication() {
		closeCommunication();
	}

	protected void updateMessageCounter(NetworkMessage networkMessage) {
		if (networkMessage.getType() != MessageType.HEARTBEAT) {
			networkMessage.setMessageId(messageCounter);
			messageCounter++;
		}
	}

	protected void addPendingRequest(NetworkMessage networkMessage) {
		pendingRequests.put(networkMessage.getMessageId(), networkMessage);
	}

	protected void clearPendingRequests(long messageId) {
		pendingRequests.remove(messageId);
	}

	protected void handleIncomingFile(String path) {
		NetworkMessage statusMessage = new NetworkMessage();
		statusMessage.setType(MessageType.STATUS_RESPONSE);

		File newFile = new File(path);

		if (newFile.exists()) {
			statusMessage.setStatus(NetworkMessage.STATUS_ERROR_CREATING_FILE);
		} else {
			statusMessage.setStatus(NetworkMessage.STATUS_OK);
			fileThread = new FileThread(this, path, FileThread.MODE_DOWNLOAD);
		}

		sendMessage(statusMessage);
	}

	protected void handleFileChunk(String fileChunk) {
		if (!fileThread.isInterrupted()) {
			fileThread.addFileChunk(fileChunk);
		} else {
			// TODO Maybe something went wrong? Notify the other side that the file transfer
			// could not be completed.
		}
	}

	public void closeSocket() {
		try {
			communicationSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
