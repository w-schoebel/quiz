import java.util.ArrayList;
import java.util.List;

public class JokerLibrary {

	private static List<Joker> jokerList = null;

	/**
	 * init and return jokerlist
	 * @param questionCount
	 * @return jokerList
	 */
	public static List<Joker> getJoker(int questionCount) {
		
		initJokerList((int) questionCount / 10);
		
		return jokerList;

	}

	/**
	 * ask user for / until number between 0 and param maxInputNumber
	 * @param maxInputNumber
	 * @return
	 */
	private static int getJokerChoiceNumber(int maxInputNumber){
		int input = -1;
		System.out.println(String.format("Vorgang abbrechen: %d!", maxInputNumber));
		if(maxInputNumber != 1) System.out.println(String.format("Bitte treffen Sie eine Wahl zwischen 1 und %d!", maxInputNumber ));

		Boolean possibleRange = false;
		while (!possibleRange) {
			input = Supportfunctions.getIntFromConsole();
			possibleRange = (input >= 0 && input <= maxInputNumber );

			if (!possibleRange) {
				System.out.println(String.format( "Ihre Eingabe %d entspricht nicht den Vorgaben! Bitte geben Sie eine Zahl zwischen 1 und %d ein!", input, maxInputNumber));
			}
		}

		return input;
	}
	/**
	 * shows user availble jokers and returns list of them 
	 * @param question
	 * @param player
	 * @return possibleJokers
	 */
	private static List<Joker> getPossiblJokers(Question question,Player player){
		List<Joker> possibleJokers = new ArrayList<Joker>();

		int i = 0;
		for (Joker joker : player.jokerList) {
			if (joker.questionType.equals(question.type) && joker.Count > 0) {
				System.out.println(String
					.format( "Wollen Sie einen %s verwenden? - Geben Sie eine %d ein! Sie haben davon noch %d Joker.", joker.Name, i + 1, joker.Count));
				possibleJokers.add(joker);
				i++;
			}
		}

		return possibleJokers;
	}
	
	// possible name useJoker / checkForJoker
	/**
	 * high level function for joker usage
	 * @param question
	 * @param player
	 * @param questionNumber
	 */
	public static void askForJoker(Question question, Player player, int questionNumber) {
		Boolean isCorrectSolved = false;
		
		Supportfunctions.seperatorLine();

		List<Joker> possibleJokers = getPossiblJokers(question, player);
		// + 1 because + 1 is the exit
		int maxJokerChoiceNumber = possibleJokers.size() + 1;
		int jokerChoiceNumber = getJokerChoiceNumber(maxJokerChoiceNumber);

		if (possibleJokers.size() == 0 || jokerChoiceNumber == maxJokerChoiceNumber) { 
			// refactor?
			if(possibleJokers.size() == 0) System.out.println("Bei diesem Fragetypen haben Sie leider keine weiteren Joker.");
			System.out.println("Wiederholung der Frage:\n");

			QuestionManagement.showQuestion(question, questionNumber, player);
		} else {
			// -1 because joker Display shows them with +1 (so they dont start with 0)
			Joker choosenJoker = possibleJokers.get(jokerChoiceNumber - 1);
			showJoker(question, choosenJoker); 
		}
	}


	/**
	 * decide which joker type to show
	 * @param question
	 * @param joker
	 */
	private static void showJoker(Question question, Joker joker) {
		switch (question.type) {
		case multipleChoice:
			QuestionManagement.showMultipleChoiceQuestionWithJoker(question, joker);
			joker.Count -= 1;
			break;
		case trueFalseQuestion:
			QuestionManagement.showTrueFalseQuestionWithJoker(question, joker);
			joker.Count -= 1;
			break;
		case userInput:
			QuestionManagement.showUserInputQuestionWithJoker(question, joker);
			joker.Count -= 1;
			break;
		}
		
	}

	/**
	 * initializes jokers
	 * @param jokerCount
	 */
	private static void initJokerList(int jokerCount) {
		jokerList = new ArrayList<Joker>();
		Joker joker = new Joker();

		// 50/50 - Joker und Wortl�ngen-Joker gibt es bereits bei unter 10 Fragen

		// 50/50 - Joker
		joker.Name = "50/50 - Joker";
		joker.Count = jokerCount == 0 ? 1 : jokerCount; // if(jokerCount == 0) joker.Count = 1; else joker.Count =
														// jokerCount;
		joker.type = JokerType.fiftyFifty;
		joker.questionType = QuestionType.multipleChoice;
		jokerList.add(joker);

		// gibt die Wortl�nge f�r die richtige Antwort zur�ck
		joker = new Joker();
		joker.Name = "Wortlängen - Joker";
		joker.Count = jokerCount == 0 ? 1 : jokerCount;
		joker.type = JokerType.letterNumber;
		joker.questionType = QuestionType.userInput;
		jokerList.add(joker);

		// gibt den ersten Buchstaben der richtigen Antwort zur�ck
		joker = new Joker();
		joker.Name = "Erster - Buchstaben - Joker";
		joker.Count = jokerCount;
		joker.type = JokerType.firstLetter;
		joker.questionType = QuestionType.userInput;
		jokerList.add(joker);

		// gibt einen Tipp f�r die richtige Antwort zur�ck
		joker = new Joker();
		joker.Name = "Tipp - Joker";
		joker.Count = jokerCount;
		joker.type = JokerType.tipp;
		joker.questionType = QuestionType.trueFalseQuestion;
		jokerList.add(joker);

		// gibt einen Tipp f�r die richtige Antwort zur�ck
		joker = new Joker();
		joker.Name = "Tipp - Joker";
		joker.Count = jokerCount;
		joker.type = JokerType.tipp;
		joker.questionType = QuestionType.userInput;
		jokerList.add(joker);
	}
}
