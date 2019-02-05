import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class JokerLibrary {

	private static List<Joker> jokerList = null;

	public static List<Joker> getJoker(int questionCount) {
		
		initJokerList((int) questionCount / 10);
		
		return jokerList;

	}

	// get joker number from user
	private static int getInput(int maxInputNumber){
		int input = -1;
		System.out
				.println(String.format("Möchten Sie doch keinen Joker verwenden, so drücken Sie bitte die %d!", maxInputNumber));

		System.out.println(String.format("Bitte treffen Sie eine Wahl zwischen 1 und %d!", maxInputNumber ));

		Boolean possibleRange = false;
		while (!possibleRange) {
			input = Supportfunctions.getIntFromConsole();
			possibleRange = (input >= 0 && input <= maxInputNumber );

			if (!possibleRange) {
				System.out.println(String
					.format( "Ihre Eingabe %d entspricht nicht den Vorgaben! Bitte geben Sie eine Zahl zwischen 1 und %d ein!", input, maxInputNumber));
			}
		}
		return input;

	}
	// shows user availble jokers and returns list of them 
	private static List<Joker> getPossiblJokers(Question question,Player player){
		List<Joker> possibleJokers = new ArrayList<Joker>();

		int i = 0;
		for (Joker joker : player.jokerList) {
			if (joker.questionType.equals(question.type) && joker.Count > 0) {
				System.out.println(String
					.format( "Möchten Sie einen %s verwenden? - Geben Sie ein %d ein! Ihnen stehen davon noch %d Stück zur Verfügung!", joker.Name, i + 1, joker.Count - 1));
				possibleJokers.add(joker);
				i++;
			}
		}

		return possibleJokers;
	}
	
	public static Boolean askForJoker(Question question, Player player, int questionNumber) {
		Boolean isCorrectSolved = false;
		
		Supportfunctions.seperatorLine();

		List<Joker> possibleJokers = getPossiblJokers(question, player);
		// + 1 because + 1 is the exit
		int maxInputNumber = possibleJokers.size() + 1;
		int input = getInput(maxInputNumber);

		if (possibleJokers.size() == 0 || input == maxInputNumber) { 
			if(possibleJokers.size() == 0) System.out.println("Leider stehen Ihnen keine weiteren Joker für diesen Fragentyp zur Verfügung!");
			System.out.println("Wiederholung der Frage:\n");

			QuestionManagement.showQuestion(question, questionNumber, player);
		} else {
			//input = eingegebener Wert des Spieler -> zwischen 1 und ... da Liste aber bei 0 losgeht -> -1
			Joker choosenJoker = possibleJokers.get(input - 1);
			showJoker(question, choosenJoker); 
		}
		//this should be removed soon
		isCorrectSolved = QuestionManagement.checkAnswer(question, false);
		return isCorrectSolved;
	}


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
