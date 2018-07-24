package library.networking;

import library.models.network.MessageType;
import library.models.network.NetworkMessage;

public class HeartbeatThread extends Thread {

	private int waitTimeInSeconds;
	protected CommunicationInterface communicationListener;

	private int pendingRequests = 0;

	public HeartbeatThread(int waitTimeInSeconds) {
		this.waitTimeInSeconds = waitTimeInSeconds;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				if (pendingRequests > 3) {
					communicationListener.closeCommunication();
				} else {
					sleep(waitTimeInSeconds * 1000);
					NetworkMessage heartbeat = new NetworkMessage();
					heartbeat.setType(MessageType.HEARTBEAT);
					communicationListener.sendMessage(heartbeat);
					pendingRequests++;
				}
			} catch (InterruptedException e) {
				interrupt();
			}
		}
	}

	public void setCommunicationListener(CommunicationInterface communicationListener) {
		this.communicationListener = communicationListener;
	}

	public void decrementPendingRequests() {
		pendingRequests--;
	}
}
