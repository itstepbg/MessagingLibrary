package library.networking;

import java.util.HashMap;

import library.models.network.MessageType;
import library.models.network.NetworkMessage;

public class CommonCommunication {

	public final static int HEARTBEAT_INTERVAL = 2;
	public final static int TIMEOUT_INTERVAL = 3 * HEARTBEAT_INTERVAL;

	protected long messageCounter = 0;
	protected HashMap<Long, NetworkMessage> pendingRequests = new HashMap<>();

	// TODO There should be a common constructor for initializing and starting the
	// communication threads.

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
}
