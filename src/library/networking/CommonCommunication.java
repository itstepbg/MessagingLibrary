package library.networking;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

import library.models.network.NetworkMessage;
import library.util.MessagingLogger;

public abstract class CommonCommunication implements CommunicationInterface {

	protected long messageCounter = 0;
	protected HashMap<Long, NetworkMessage> pendingRequests = new HashMap<>();

	protected Socket communicationSocket;
	protected InputThread inputThread;
	protected OutputThread outputThread;
	protected static Logger logger = MessagingLogger.getLogger();

	public CommonCommunication(Socket communicationSocket) {
		this.communicationSocket = communicationSocket;

		inputThread = CommunicationThreadFactory.createInputThread(communicationSocket);
		outputThread = CommunicationThreadFactory.createOutputThread(communicationSocket);

		inputThread.setCommunicationListener(this);
		outputThread.setCommunicationListener(this);

		inputThread.start();
		outputThread.start();
	}

	protected void updateMessageCounter(NetworkMessage networkMessage) {
		networkMessage.setMessageId(messageCounter);
		messageCounter++;
	}

	protected void addPendingRequest(NetworkMessage networkMessage) {
		pendingRequests.put(networkMessage.getMessageId(), networkMessage);
	}

	protected void clearPendingRequests(long messageId) {
		pendingRequests.remove(messageId);
	}

	public void closeSocket() {
		try {
			communicationSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public abstract void handleMessage(NetworkMessage networkMessage);

	@Override
	public abstract void sendMessage(NetworkMessage networkMessage);

	@Override
	public abstract void closeCommunication();
}
