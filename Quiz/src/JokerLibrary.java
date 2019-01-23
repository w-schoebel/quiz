import java.util.ArrayList;
import java.util.List;

public class JokerLibrary {

	private static List<Joker> jokerList = null;

	public static List<Joker> getJoker(int questionCount) {
		if (jokerList == null) {
			initJokerList((int) questionCount / 10);
		}
		return jokerList;

	}

	public static void askForJoker(Question question, Player player, int questionNumber) {
		Supportfunctions.seperatorLine();
		
		JokerType type = null;

		int i = 1;
		List<Joker> possibleJokers = new ArrayList<Joker>();

		for (Joker joker : player.jokerList) {
			if (joker.questionType.equals(question.type) && joker.Count > 0) {
				System.out.println(String.format(
						"Möchten Sie einen %s verwenden? - Geben Sie ein %d ein! Ihnen stehen davon noch %d Stück zur Verfügung!",
						joker.Name, i, joker.Count));
				possibleJokers.add(joker);
				i++;
			}
		}
		
		if (i == 1) { // wenn i weiterhin 1 ist, wurde kein Joker gefunden -> sonst wäre der Wert auf
						// 2 gestiegen (siehe i++)
			System.out.println("Leider stehen Ihnen keine weiteren Joker für diesen Fragentyp zur Verfügung!");

			QuestionManagement.showQuestion(question, questionNumber, player);
		} else {
			int input = -1;
			System.out.println(String.format("Möchten Sie doch keinen Joker verwenden, so drücken Sie bitte die %d!", i));

			System.out.println(String.format("Bitte treffen Sie eine Wahl zwischen 1 und %d!", i));

			while (input <= 0 || input > i) {
				input = Supportfunctions.getIntFromConsole();

				if (input > i || input <= 0) {
					System.out.println(String.format(
							"Ihre Eingabe entspricht nicht den Vorgaben! Bitte geben Sie eine Zahl zwischen 1 und %d ein!",
							i));
				}
			}
			if (input == i) {
				QuestionManagement.showQuestion(question, questionNumber, player);
			} else {
				useJoker(possibleJokers.get(input - 1));
			}
		}
	}

	/*
	 * 50-50 String chars = "ABCD"; Random randomChar = new Random(); int i = 0;
	 * 
	 * while(i<) char c = chars.charAt(randomChar.nextInt(chars.length()));
	 * System.out.println(c);
	 */

	/*
	 * erster Buchstabe public static char firstChar (question){
	 * 
	 * String s = question.answer1; char c = s.charAt(0); System.out.println(c);
	 * return c; }
	 */

	/*
	 * Tipp private void showJoker (Question question) {
	 * System.out.println(String.format("Tipp: %s", question.joker)); //return
	 * question.joker; }
	 */

	private static void initJokerList(int jokerCount) {
		jokerList = new ArrayList<Joker>();
		Joker joker = new Joker();

		// 50/50 - Joker und Wortlängen-Joker gibt es bereits bei unter 10 Fragen

		// 50/50 - Joker
		joker.Name = "50/50 - Joker";
		joker.Count = jokerCount == 0 ? 1 : jokerCount; // if(jokerCount == 0) joker.Count = 1; else joker.Count =
														// jokerCount;
		joker.type = JokerType.fiftyFifty;
		joker.questionType = QuestionType.multipleChoice;
		jokerList.add(joker);

		// gibt die Wortlänge für die richtige Antwort zurück
		joker = new Joker();
		joker.Name = "Wortlängen - Joker";
		joker.Count = jokerCount == 0 ? 1 : jokerCount;
		joker.type = JokerType.letterNumber;
		joker.questionType = QuestionType.userInput;
		jokerList.add(joker);

		// gibt den ersten Buchstaben der richtigen Antwort zurück
		joker = new Joker();
		joker.Name = "Erster - Buchstaben - Joker";
		joker.Count = jokerCount;
		joker.type = JokerType.firstLetter;
		joker.questionType = QuestionType.userInput;
		jokerList.add(joker);

		// gibt einen Tipp für die richtige Antwort zurück
		joker = new Joker();
		joker.Name = "Tipp - Joker";
		joker.Count = jokerCount;
		joker.type = JokerType.tipp;
		joker.questionType = QuestionType.trueFalseQuestion;
		jokerList.add(joker);

		// gibt einen Tipp für die richtige Antwort zurück
		joker = new Joker();
		joker.Name = "Tipp - Joker";
		joker.Count = jokerCount;
		joker.type = JokerType.tipp;
		joker.questionType = QuestionType.userInput;
		jokerList.add(joker);
	}

	private static void useJoker(Joker joker) {
	}
}
