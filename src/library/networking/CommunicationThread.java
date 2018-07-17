package library.networking;

import java.net.Socket;
import java.util.logging.Logger;

import library.util.MessagingLogger;

public class CommunicationThread extends Thread {

	protected Logger logger = MessagingLogger.getLogger();

	protected CommunicationInterface communicationListener;
	protected Socket socket;

	public CommunicationThread(Socket socket) {
		this.socket = socket;
	}

	public void setCommunicationListener(CommunicationInterface communicationListener) {
		this.communicationListener = communicationListener;
	}
}
