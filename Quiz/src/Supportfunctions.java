import java.util.Scanner;

public class Supportfunctions {

	public static int getIntFromConsole() {
		boolean isInt = false;
		int value = 0;
		Scanner sc01 = new Scanner(System.in);

		while (!isInt) {
			String input = sc01.next();
			if (!tryParseInt(input)) {
				System.out.println("Ihre Eingabe entspricht nicht den Vorgaben!");
			} else {
				isInt = true;
				value = Integer.parseInt(input);
			}
		}

		return value;
	}

	public static String getStringFromConsole() {
		Scanner sc01 = new Scanner(System.in);
		String value = sc01.next();

		return value;
	}

	public static boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value); // versucht den String in einen Int zu überführen
			return true;
		} catch (NumberFormatException e) { // wenn kein Int geparst werden kann-> Exception e
			return false;
		}
	}

	public static void seperatorLine() {
		System.out.println(
				"--------------------------------------------------------------------------------------------------------");
	}
	
	public static String spaces() {
		return "        ";
	}
}
