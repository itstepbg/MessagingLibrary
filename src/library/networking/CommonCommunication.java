package library.networking;

import java.util.HashMap;

import library.models.network.NetworkMessage;

public class CommonCommunication {

	protected long messageCounter = 0;
	protected HashMap<Long, NetworkMessage> pendingRequests = new HashMap<>();

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
}
