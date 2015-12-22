import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerThread extends Thread{

	BufferedReader controlInputStream = null;
	PrintStream controlOutputStream = null;
	Socket clientSocketControl = null;
	ServerSocket dataSocket = null;
	
	boolean start = true;
	String operationFromClient = "";
	String operationToDataThread = "";
	
	public ServerThread(Socket clientSocketControl, ServerSocket dataSocket) {
		this.clientSocketControl = clientSocketControl;
		this.dataSocket = dataSocket;
	}
	
	public void run() {
		try {
			controlInputStream = new BufferedReader(new InputStreamReader(clientSocketControl.getInputStream()));
			controlOutputStream = new PrintStream(clientSocketControl.getOutputStream());
			
			while(true){
				if(start) {
					controlOutputStream.println("Welcome to WEB calculator.");
					controlOutputStream.println("Valid operations are: addition(+), substriction(-), division(/) and multiplication(*).");
					start = false;
				}
				controlOutputStream.println("Please choose the arithmetic operation:");
				operationFromClient = controlInputStream.readLine().trim();
				
				if(operationFromClient.equals("exit")) {
					controlOutputStream.println("Goodbye!");
					break;
				}
				if(operationFromClient.equals("+") || operationFromClient.equalsIgnoreCase("addition")) {
					controlOutputStream.println("Choosen operation is: ADDITION.");
					operationToDataThread = "+";
				}
				else if(operationFromClient.equals("-") || operationFromClient.equalsIgnoreCase("subtraction")) {
					controlOutputStream.println("Choosen operation is: SUBTRACTION.");
					operationToDataThread = "-";
				}
				else if(operationFromClient.equals("*") || operationFromClient.equalsIgnoreCase("multiplication")) {
					controlOutputStream.println("Choosen operation is: MULTIPLICATION.");
					operationToDataThread = "*";
				}
				else if(operationFromClient.equals("/") || operationFromClient.equalsIgnoreCase("division")) {
					controlOutputStream.println("Choosen operation is: DIVISION.");
					operationToDataThread = "/";
				}
				else {
					controlOutputStream.println("Invalid operation.");
					continue;
				}
				
				controlOutputStream.println("Enter numbers with space between them:");
				//podaci
				Socket clientSocketData = dataSocket.accept();
				DataThread dataThread = new DataThread(clientSocketData, operationToDataThread);
				dataThread.start();
				
			}
			clientSocketControl.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	
}
