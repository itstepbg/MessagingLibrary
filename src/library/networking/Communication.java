package library.networking;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

import library.models.network.MessageType;
import library.models.network.NetworkMessage;
import library.util.MessagingLogger;

public class Communication implements CommunicationInterface {

	public final static int HEARTBEAT_INTERVAL = 60;
	public final static int TIMEOUT_BUFFER_SIZE = 3;

	protected static Logger logger = MessagingLogger.getLogger();

	protected Socket communicationSocket;
	protected String salt;
	protected String registerPassword;
	protected String sessionID;
	protected InputThread inputThread;
	protected OutputThread outputThread;
	protected HeartbeatThread heartbeatThread;
	protected FileThread fileThread;

	protected long messageCounter = 0;
	protected HashMap<Long, NetworkMessage> pendingRequests = new HashMap<>();

	public Communication(Socket communicationSocket) {
		this.communicationSocket = communicationSocket;

		inputThread = CommunicationThreadFactory.createInputThread(communicationSocket);
		outputThread = CommunicationThreadFactory.createOutputThread(communicationSocket);
		//heartbeatThread = CommunicationThreadFactory.createHeartbeatThread(HEARTBEAT_INTERVAL);

		startCommunicationThreads();
	}

	protected void startCommunicationThreads() {
		inputThread.setCommunicationListener(this);
		outputThread.setCommunicationListener(this);
		//heartbeatThread.setCommunicationListener(this);

		inputThread.start();
		outputThread.start();
		//heartbeatThread.start();
	}

	@Override
	public void sendMessage(NetworkMessage networkMessage) {
		// TODO All sent messages should have an actor if the user is logged in.
		updateMessageCounter(networkMessage);
		outputThread.addMessage(networkMessage);
	}

	@Override
	public void handleMessage(NetworkMessage networkMessage) {
	//	heartbeatThread.resetTimeoutBuffer();
	}

	@Override
	public void closeCommunication() {
		logger.info("Closing communication for " + communicationSocket.getInetAddress().getHostAddress());

		closeSocket();

		interruptThread(heartbeatThread);

		// TODO Maybe remove the code below? It is used as a failsafe now.
		interruptThread(inputThread);
		interruptThread(outputThread);
		// TODO Maybe the file thread reference should be removed when it dies?
		interruptThread(fileThread);
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getRegisterPassword() {
		return registerPassword;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
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

	private void closeSocket() {
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
	}

	protected void updateMessageCounter(NetworkMessage networkMessage) {
		MessageType messageType = networkMessage.getType();
		if (messageType != MessageType.HEARTBEAT && messageType != MessageType.STATUS_RESPONSE
				&& messageType != MessageType.UPLOAD_CHUNK) {
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

	// File stuff.

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

		fileThread.start();
		sendMessage(statusMessage);
	}

	protected void handleFileChunk(String fileChunk) {
		fileThread.addFileChunk(fileChunk);
	}

	public void createFileUploadThread(String filePath) {
		fileThread = new FileThread(this, filePath, FileThread.MODE_UPLOAD);
	}

	protected void startFileUpload() {
		fileThread.start();
	}

	protected void clearFileUploadThread() {
		fileThread = null;
	}
}
