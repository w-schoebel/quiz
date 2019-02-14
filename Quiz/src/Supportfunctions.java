import java.util.Random;
import java.util.Scanner;

public class Supportfunctions {
	
	/**
	 * get random char from given alphabet
	 * @param alphabet
	 * @return char char
	 */
	public static char getRandomChar(String alphabet) {
		Random randomChar = new Random(); 
		
		return alphabet.charAt(randomChar.nextInt(alphabet.length()));	
	}
	
	
	/**
	 *  get random int
	 * @param range
	 * @return int int
	 */
	public static int getRandomInt(int range) {
		Random randomGenerator = new Random();

		return randomGenerator.nextInt(range);
	}
	
	/**
	 * get int from user
	 * @return int value
	 */
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

	/**
	 * get string from user
	 * @return string value
	 */
	public static String getStringFromConsole() {
		Scanner sc01 = new Scanner(System.in);
		String value = sc01.next();

		return value;
	}

	/**
	 * 
	 * changes string into integer, returns if successfull or not
	 * @param value
	 * @return boolean 
	 */
	public static boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value); 
			return true;
		} catch (NumberFormatException e) { 
			return false;
		}
	}

	/**
	 * display line in console for seperation
	 * @return void
	 */
	public static void seperatorLine() {
		System.out.println(
				"--------------------------------------------------------------------------------------------------------");
	}
	
	/**
	 * adds spaces
	 * @param maxTextLength
	 * @return String spaces
	 */
	public static String spaces(int maxTextLength) {
		String spaces = "";
		
		for(int i = 0; i < maxTextLength; i++)
		{
				//insert empty space for every letter
				spaces += " "; 
		}
		
		//additional empty spaces for better overview
		return spaces + "        "; 
	}
}
