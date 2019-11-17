import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class UI {
	public static URI getURIToParse(String invitation) {
		while (true) {
			URI uri = URI.create(userInput(invitation));
			if (isURIValid(uri))
				return uri;
			System.err.print("Wrong input. Address should be written with protocol. Example \"https://www.example" +
					".com\": ");
		}
	}

	public static String userInput() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String userInput = "";
		try {
			userInput = reader.readLine();
		} catch (IOException e) {
		}
		return userInput;
	}

	public static String userInput(String invitation) {
		print(invitation);
		return userInput();
	}

	public static boolean isURIValid(URI uri) {
		return !uri.isOpaque() && uri.isAbsolute();
	}

	public static void print(String string) {
		System.out.print(string + ": ");
	}

	public static void println(String string) {
		System.out.println(string);
	}
}
