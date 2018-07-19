package library.networking;

import library.models.network.NetworkMessage;

public interface CommunicationInterface {

	public void handleMessage(NetworkMessage networkMessage);

	public void sendMessage(NetworkMessage networkMessage);

	public void closeCommunication();
}
