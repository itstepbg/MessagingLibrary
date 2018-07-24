package library.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import library.models.network.NetworkMessage;

public class InputThread extends CommunicationThread {

	public InputThread(Socket socket) {
		super(socket);
	}

	@Override
	public void run() {
		BufferedReader inFromClient = null;

		try {
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			communicationListener.unregisterCommunication();
		}

		if (inFromClient != null) {
			while (!socket.isClosed()) {
				String messageXml = null;
				try {
					messageXml = inFromClient.readLine();
					if (messageXml == null) {
						communicationListener.unregisterCommunication();
						break;
					}
					logger.info("<- " + messageXml);
				} catch (IOException e) {
					// The read has timed-out, so we do a blocking wait for input again...
					continue;
				}

				NetworkMessage networkMessage = deserializeMessage(messageXml);

				if (networkMessage != null) {
					communicationListener.handleMessage(networkMessage);
				}
			}
		}
	}

	private NetworkMessage deserializeMessage(String messageXml) {
		Serializer serializer = new Persister();
		NetworkMessage networkMessage = null;
		try {
			networkMessage = serializer.read(NetworkMessage.class, messageXml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return networkMessage;
	}
}
