package library.networking;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

import library.models.network.MessageType;
import library.models.network.NetworkMessage;
import library.util.MessagingLogger;

public abstract class CommonCommunication {

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
	}

	protected void startCommunicationThreads(CommunicationInterface communicationListener) {
		inputThread.setCommunicationListener(communicationListener);
		outputThread.setCommunicationListener(communicationListener);
		heartbeatThread.setCommunicationListener(communicationListener);

		inputThread.start();
		outputThread.start();
		heartbeatThread.start();
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

	protected void handleIncomingFile(CommunicationInterface communicationListener, String path) {
		NetworkMessage statusMessage = new NetworkMessage();
		statusMessage.setType(MessageType.STATUS_RESPONSE);

		File newFile = new File(path);

		if (newFile.exists()) {
			statusMessage.setStatus(NetworkMessage.STATUS_ERROR_CREATING_FILE);
		} else {
			statusMessage.setStatus(NetworkMessage.STATUS_OK);
		}

		fileThread = new FileThread(communicationListener, path, FileThread.MODE_DOWNLOAD);

		communicationListener.sendMessage(statusMessage);
	}

	protected void handleFileChunk(CommunicationInterface communicationListener, String fileChunk) {
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
