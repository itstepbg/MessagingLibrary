package library.networking;

import java.net.Socket;

public class CommunicationThreadFactory {

	public static InputThread createInputThread(Socket socket) {
		return new InputThread(socket);
	}
	
	public static OutputThread createOutputThread(Socket socket) {
		return new OutputThread(socket);
	}
}
