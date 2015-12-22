import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


public class DataThread extends Thread {

	BufferedReader dataInputStream = null;
	PrintStream dataOutputStream = null;
	Socket clientSocketData = null;
	String operation = "";
	double result;
	String regex = "[0-9 ]+";
	String[] numbers;
	
	LinkedList<Integer> nums = new LinkedList<>();
	
	public DataThread(Socket clientSocketData,String operation) {
		this.operation = operation;
		this.clientSocketData = clientSocketData;
	}
	
	public void run() {
		try {
			dataInputStream = new BufferedReader(new InputStreamReader(clientSocketData.getInputStream()));
			dataOutputStream = new PrintStream(clientSocketData.getOutputStream());
			while(true){
			while(true){
			String ans = dataInputStream.readLine();
			if(ans.trim().equals("exit")) {
				dataOutputStream.println("Goodbye!");
				return;
			}
			numbers = ans.trim().split(" ");

			if(numbers[0].matches(regex))
				break;
			else {
				dataOutputStream.println("Please type in ONLY numbers:");
			}
			}
			for(int i = 0; i < numbers.length;i++) {
				if(numbers[i].matches(regex))
					nums.add(Integer.parseInt(numbers[i]));
			}
			switch (operation) {
			case "+":
				result = addition();
				nums.clear();
				break;
			case "-":
				result = subtraction();
				nums.clear();
				break;
			case "*":
				result = multiplication();
				nums.clear();
				break;
			case "/":
				result = division();
				nums.clear();
				break;
			}
			
			if(result == Integer.MAX_VALUE) {
				dataOutputStream.println("You can't divide by zero. Please type in your numbers again:");
				continue;
			}
			else{
				dataOutputStream.println("Your result is: "+result);
				break;
			}
			}
			clientSocketData.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private double division() {
		double res = nums.getFirst();
		for (int i = 0; i < nums.size(); i++) {
			if(nums.get(i) == nums.getFirst())
				continue;
			if(nums.get(i) == 0)
				return Integer.MAX_VALUE;
			res = res / nums.get(i);
		}
		return res;
	}

	private double multiplication() {
		int mul = 1;
		for (int i = 0; i < nums.size(); i++) {
			mul *= nums.get(i);
		}
		return mul;
	}

	private double subtraction() {
		int res = nums.getFirst();
		for (int i = 0; i < nums.size(); i++) {
			if(nums.get(i) == nums.getFirst())
				continue;
			res -= nums.get(i);
		}
		return res;
	}

	private double addition() {
		int sum = 0;
		for (int i = 0; i < nums.size(); i++) {
			sum += nums.get(i);
		}
		return sum;
	}
}
