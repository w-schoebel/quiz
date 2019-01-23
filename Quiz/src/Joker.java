import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Joker {
	String Name;
	int Count;
	JokerType type;
	QuestionType questionType;

	private static List<Joker> jokerList = null;

	public static List<Joker> getJoker(int questionCount) {
		if (jokerList == null) {
			initJokerList((int) questionCount / 10);
		}
		return jokerList;

	}

	public JokerType askForJoker(QuestionType questionType) {
		JokerType type = null;
		String input = "";

		switch (questionType) {
		case multipleChoice:
			System.out.println("M�chten Sie einen 50/50 - Joker verwenden? - Geben Sie ein J ein!");
			input = Supportfunctions.getStringFromConsole();
			if (input.equalsIgnoreCase("j")) {
				type = JokerType.fiftyFifty;
			}
			break;
		case userInput:
			System.out.println("M�chten Sie einen der 3 folgenden Joker verwenden - Wortl�ngen - Joker "
					+ "/ Erster Buchstaben - Joker /Tipp - Joker? - Geben Sie ein J ein!");
			input = Supportfunctions.getStringFromConsole();
			if (input.equalsIgnoreCase("j")) {
				System.out.println("Welchen Joker m�chten Sie verwenden? \n "
						+ "W�hlen Sie 1, um den ersten Buchstaben zu erhalten \n"
						+ "W�hlen Sie 2, um einen Tipp zu erhalten \n"
						+ "W�hlen Sie 3, um die Anzahl der Buchstaben zu erhalten \n"
						+ "Wenn Sie doch keinen Joker verwenden m�chten, geben Sie Z (zur�ck) ein!");
				input = Supportfunctions.getStringFromConsole();
				if (input.equalsIgnoreCase("1")) {
					type = JokerType.firstLetter;
				}
				else if (input.equalsIgnoreCase("2")) {
					type = JokerType.tipp;
				}

				else if (input.equalsIgnoreCase("3")) {
					type = JokerType.letterNumber;
				}
				else if (input.equalsIgnoreCase("z")) {
					;
				}
				break;
			}
		case trueFalseQuestion:
			System.out.println("M�chten Sie einen Tipp - Joker verwenden? - Geben Sie ein J ein!");
			input = Supportfunctions.getStringFromConsole();
			if (input.equalsIgnoreCase("j")) {
				type = JokerType.tipp;
			}
			break;
		}

		return type;
	}

	/*
	 String chars = "ABCD";
	 Random randomChar = new Random();
	 char c = chars.charAt(randomChar.nextInt(chars.length()));
	 System.out.println(c);
	 */

	private static void initJokerList(int jokerCount)
	{
		jokerList = new ArrayList<Joker>();
		Joker joker = new Joker();
		
		//50/50 - Joker und Wortl�ngen-Joker gibt es bereits bei unter 10 Fragen	
		
		//50/50 - Joker
		joker.Name = "50/50 - Joker";
		joker.Count = jokerCount == 0 ? 1 : jokerCount; //if(jokerCount == 0) joker.Count = 1; else joker.Count = jokerCount;
		joker.type = JokerType.fiftyFifty;
		joker.questionType = QuestionType.multipleChoice;
		jokerList.add(joker);
		
		
		//gibt die Wortl�nge f�r die richtige Antwort zur�ck
		joker = new Joker();
		joker.Name = "Wortl�ngen - Joker";
		joker.Count = jokerCount == 0 ? 1 : jokerCount; 
		joker.type = JokerType.letterNumber;
		joker.questionType = QuestionType.userInput;
		jokerList.add(joker);

		
		//gibt den ersten Buchstaben der richtigen Antwort zur�ck
		joker = new Joker();
		joker.Name = "Erster - Buchstaben - Joker";
		joker.Count = jokerCount; 
		joker.type = JokerType.firstLetter;
		joker.questionType = QuestionType.userInput;
		jokerList.add(joker);
		
		
		//gibt einen Tipp f�r die richtige Antwort zur�ck
		joker = new Joker();
		joker.Name = "Tipp - Joker";
		joker.Count = jokerCount; 
		joker.type = JokerType.tipp;
		joker.questionType = QuestionType.trueFalseQuestion;
		jokerList.add(joker);
	
		//gibt einen Tipp f�r die richtige Antwort zur�ck
		joker = new Joker();
		joker.Name = "Tipp - Joker";
		joker.Count = jokerCount; 
		joker.type = JokerType.tipp;
		joker.questionType = QuestionType.userInput;
		jokerList.add(joker);
	}
}
