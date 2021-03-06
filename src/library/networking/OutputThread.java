package library.networking;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import library.models.network.NetworkMessage;

public class OutputThread extends CommunicationThread {

	private BlockingQueue<NetworkMessage> messages = new ArrayBlockingQueue<NetworkMessage>(512);

	public OutputThread(Socket socket) {
		super(socket);
	}

	public void addMessage(NetworkMessage message) {
		try {
			messages.put(message);
		} catch (InterruptedException e) {
			// TODO ?
		}
	}

	@Override
	public void run() {
		DataOutputStream outToClient = null;

		try {
			outToClient = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (outToClient != null) {
			while (!Thread.interrupted()) {
				NetworkMessage networkMessage = null;
				try {
					networkMessage = messages.take();
				} catch (InterruptedException e) {
					interrupt();
				}

				if (networkMessage != null) {
					try {
						String messageXml = serializeMessage(networkMessage);
						outToClient.writeBytes(messageXml + "\n");
						logger.info("-> " + messageXml);
					} catch (IOException e) {
						communicationListener.unregisterCommunication();
					}
				}
			}
		}
	}

	private String serializeMessage(NetworkMessage networkMessage) {

		String serializedMessage = null;

		try {
			JAXBContext ctx = JAXBContext.newInstance(NetworkMessage.class);

			Marshaller m = ctx.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			StringWriter sw = new StringWriter();
			m.marshal(networkMessage, sw);
			sw.close();

			serializedMessage = sw.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return serializedMessage;
	}
}
