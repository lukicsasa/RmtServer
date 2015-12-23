import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.LinkedList;

public class DataThread extends Thread {

	BufferedReader dataInputStream = null;
	PrintStream dataOutputStream = null;
	Socket clientSocketData = null;
	String operation = "";
	BigDecimal result;
	String regex = "[0-9 ]+";
	String[] numbers;

	LinkedList<BigDecimal> nums = new LinkedList<>();

	public DataThread(Socket clientSocketData, String operation) {
		this.operation = operation;
		this.clientSocketData = clientSocketData;
	}

	public void run() {
			try {
				dataInputStream = new BufferedReader(new InputStreamReader(
						clientSocketData.getInputStream()));
				dataOutputStream = new PrintStream(
						clientSocketData.getOutputStream());
				while (true) {
				try {
					while (true) {
						
						nums.clear();

						String ans = dataInputStream.readLine();
						if (ans.trim().equals("exit")) {
							dataOutputStream.println("Goodbye!");
							return;
						}
						numbers = ans.trim().split(" ");

						if (numbers[0].matches(regex))
							break;
						else {
							dataOutputStream
									.println("Please type in ONLY numbers:");
						}
					}
					for (int i = 0; i < numbers.length; i++) {
						if (numbers[i].matches(regex)) {
							BigDecimal n = new BigDecimal(numbers[i]);
							nums.add(n);
						}
					}
					switch (operation) {
					case "+":
						result = addition();
						break;
					case "-":
						result = subtraction();
						break;
					case "*":
						result = multiplication();
						break;
					case "/":
						result = division();
						break;
					}

					dataOutputStream.println("Your result is: " + result);
					clientSocketData.close();
					break;
				} catch(ArithmeticException e) {
					dataOutputStream
					.println("Calculator can't coprehand division by zero. Please type in your numbers again:");
				}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	

	private BigDecimal division() {
		BigDecimal res = nums.getFirst();
		for (int i = 0; i < nums.size(); i++) {
			if (nums.get(i) == nums.getFirst())
				continue;
			res = res.divide(nums.get(i),100,BigDecimal.ROUND_HALF_EVEN);
		}
		return res.stripTrailingZeros();
	}

	private BigDecimal multiplication() {
		BigDecimal mul = BigDecimal.valueOf(1);
		for (int i = 0; i < nums.size(); i++) {
			mul = mul.multiply(nums.get(i));
		}
		return mul;
	}

	private BigDecimal subtraction() {
		BigDecimal res = nums.getFirst();
		for (int i = 0; i < nums.size(); i++) {
			if (nums.get(i) == nums.getFirst())
				continue;
			res = res.subtract(nums.get(i));
		}
		return res;
	}

	private BigDecimal addition() {
		BigDecimal sum = BigDecimal.valueOf(0);
		for (int i = 0; i < nums.size(); i++) {
			sum = sum.add(nums.get(i));
		}
		return sum;
	}
}
