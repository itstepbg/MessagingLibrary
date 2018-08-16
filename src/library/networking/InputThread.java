package library.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import library.models.network.NetworkMessage;

public class InputThread extends CommunicationThread {

	public InputThread(Socket socket) {
		super(socket);
	}

	@Override
	public void run() {
		BufferedReader inputReader = null;

		try {
			inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			communicationListener.unregisterCommunication();
		}

		if (inputReader != null) {
			while (!Thread.interrupted()) {
				String messageXml = null;
				try {
					messageXml = inputReader.readLine();
					if (messageXml == null) {
						communicationListener.unregisterCommunication();
						break;
					}
					logger.info("<- " + messageXml);
				} catch (IOException e) {
					// The input has been shut down, finish the thread execution.
					break;
				}

				NetworkMessage networkMessage = deserializeMessage(messageXml);

				if (networkMessage != null) {
					communicationListener.handleMessage(networkMessage);
				}
			}
		}
	}

	private NetworkMessage deserializeMessage(String messageXml) {
		// Serializer serializer = new Persister();
		// NetworkMessage networkMessage = null;
		// try {
		// networkMessage = serializer.read(NetworkMessage.class, messageXml);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		NetworkMessage networkMessage = null;

		try {
			JAXBContext contextB = JAXBContext.newInstance(NetworkMessage.class);
			StringReader reader = new StringReader(messageXml);
			Unmarshaller unmarshallerB = contextB.createUnmarshaller();
			networkMessage = (NetworkMessage) unmarshallerB.unmarshal(reader);
			reader.close();

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return networkMessage;
	}
}
