import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	static int controlPort = 1908;
	static int dataPort = 20513;
	static ServerSocket dataSocket = null;
	static ServerSocket controlSocket = null;
	static Socket clientSocket = null;

	public static void main(String[] args) {

		try {
			dataSocket = new ServerSocket(dataPort);
			controlSocket = new ServerSocket(controlPort);
			while (true) {
				clientSocket = controlSocket.accept();
				ServerThread serverThread = new ServerThread(clientSocket,
						dataSocket);
				serverThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
