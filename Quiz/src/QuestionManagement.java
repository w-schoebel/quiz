import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class QuestionManagement {

	private static List<Question> _questions = null;
	//diff categories 
	private static List<Question> _questionsMultipleChoice = null;
	private static List<Question> _questionsUserInput = null;
	private static List<Question> _questionsTrueFalse = null;

	private static int _questionNumber = 0;
	private static Player _player = null;

	/**
	 * getsQuestion from Db and puts them into Class List by type
	 * questionType is determined by number % x == 0
	 * @return _questions
	 */
	public static List<Question> initQuestionList() {
		QuestionLibrary questionLibrary = new QuestionLibrary();
		_questions = questionLibrary.getQuestions(System.getProperty("user.dir") + "\\questions.accdb");

		_questionsMultipleChoice = new ArrayList<Question>();
		_questionsTrueFalse = new ArrayList<Question>();
		_questionsUserInput = new ArrayList<Question>();

		for (Question question : _questions) {
			switch (question.type) {
			case multipleChoice:
				_questionsMultipleChoice.add(question);
				break;
			case trueFalseQuestion:
				_questionsTrueFalse.add(question);
				break;
			case userInput:
				_questionsUserInput.add(question);
				break;
			}
		}

		return _questions;
	}
	/**
	 * takes number and returns Llist of certain questiontype
	 * questionType is determined by number % x == 0
	 * @param questionNumber
	 * @return questionTpeList
	 */
	public static List<Question> getQuestionTypeList(int questionNumber) {

		if (_questions == null) { // wird hier nur ausgef�hrt falls im Vorfeld noch keine Initialisierung der
									// Fragen vorgenommen wurde -> sollte eigentlich nicht auftreten
			initQuestionList();
		}

		if (questionNumber % 3 == 0) return _questionsUserInput;  
		else if (questionNumber % 2 == 0) return _questionsTrueFalse;  
		else return _questionsMultipleChoice;
		

	}

	/**
	 * takes array of questions and then returns random question out of that array
	 * @param List<Question> 
	 * @return question
	 */
	public static Question getQuestion(List<Question> arr){

		Question question = null;
		if(arr != null || arr.size() == 0){

			// gets random question
			question = arr.get(Supportfunctions.getRandomInt(arr.size()));

			// remove from both 
			arr.remove(question);
			_questions.remove(question);
			return question;
		} else {
			// else dosent make much sense 
			// i dont understand this at all, why is this here
			if (_questions != null) {

				question = _questions.get(Supportfunctions.getRandomInt(_questions.size()));

				_questions.remove(question);
			}
			return question;
		}



	}

	//this still needs refactoring

	/**
	 * 
	 * checks questiontypes and then checks if user answer is correct
	 * @param question
	 * @param jokerAlreadyUsed
	 * @return boolean isCorrectSolved
	 */
	public static Boolean checkAnswer(Question question, Boolean jokerAlreadyUsed) {

		Boolean isCorrectSolved = false;
		Boolean isPossibleAnswer = false;
		String input = "";



		/**
		 * 
		 * Ideal structure of function like this:
		 * //this function just be broken up in a validAnswer and correct Answer function
		 * 
		 * 
		 * //name vars either answer or input for consistency
		 * 
		 * Boolean validAnswer = question.possibleAnswers.contains(userAnswer);
		 * Boolean correctAnswer = question.correctAnswer == userAnswer;
		 * 
		 * while(!validAnswer) {
		 *  print('please enter a valid answer')
		 *  validAnswer = question.possibleAnswers.contains(userAnswer);
		 * 
		 * 
		 * } 
		 * if(correctAnswer){
		 *   return true
		 * } else {
		 *   return true 
		 * }
		 * //Joker logic should also not be in here:
		 * jokerEvent(){
		 * 	if(userAnswer =='j'){
		 *    useJoker();
		 *  }
		 * 
		 * }
		 * 
		 * The irony is that the actual line of code which checkAnswer needs is 
		 * } else if (input.equalsIgnoreCase(question.correctAnswer)) {
		 * 	isCorrectSolved = true;
		 * }
		 * and its repeated for every case except userInput type.
		 * I think we just need to trim userInput and we could just trim that regardless
		 * The question is where to put the joker 
		 * 
		 */

		
		// it checks both if user input is valid and if input is correct. needs to be split up into functions
		switch (question.type) {
		case multipleChoice:

			while (!isPossibleAnswer) {
				input = Supportfunctions.getStringFromConsole();
				//String[] values = {"a","b","c","d"};
				//input = input.toLowerCase();
				//boolean contains = Arrays.stream(values).anyMatch(input::equals);
				//System.out.println(contains);

				// would be much better if this logic was in the db
				if (	input.equalsIgnoreCase("A") 
						|| input.equalsIgnoreCase("B") 
						|| input.equalsIgnoreCase("C")
						|| input.equalsIgnoreCase("D") 
						|| (!jokerAlreadyUsed && input.equalsIgnoreCase("J"))) {
					isPossibleAnswer = true;
				} else {
					System.out.println("Ihre Eingabe entspricht nicht der Vorgabe! Geben Sie A,B,C oder D ein!");
				}
			}

			if (input.equalsIgnoreCase("j")) isCorrectSolved = JokerLibrary.askForJoker(question, _player, _questionNumber);
			else if (input.equalsIgnoreCase(question.correctAnswer)) isCorrectSolved = true;

			break;

		case trueFalseQuestion:

			while (!isPossibleAnswer) {
				input = Supportfunctions.getStringFromConsole();
				if (input.equalsIgnoreCase("w") 
						|| input.equalsIgnoreCase("f")
						|| (!jokerAlreadyUsed && input.equalsIgnoreCase("J"))) {
					isPossibleAnswer = true;
				} else {
					System.out.println("Ihre Eingabe entspricht nicht der Vorgabe! Geben Sie w oder f!");
				}
			}
			if (input.equalsIgnoreCase("j")) {
				isCorrectSolved = JokerLibrary.askForJoker(question, _player, _questionNumber);
			} else if (input.equalsIgnoreCase(question.correctAnswer)) {
				isCorrectSolved = true;
			}

			break;
		case userInput:

			input = Supportfunctions.getStringFromConsole();
			// trim -> Leerzeichen entfernen, damit dadurch keine Fehler entstehen k�nnen
			// (z.B. zu viele Leerzeichen zwischen W�rtern)

			if (!jokerAlreadyUsed && input.equalsIgnoreCase("J")) {
				isCorrectSolved = JokerLibrary.askForJoker(question, _player, _questionNumber);
			} else if (input.trim().equalsIgnoreCase(question.correctAnswer.trim())) {
				isCorrectSolved = true;
			}

			break;
		}

		return isCorrectSolved;

	}
	//////////////////////////////////////////////////////////////////////////////////////////
	// From now on its all about showQuestion. Ideally this would be refactored.
	// The current structure makes it really hard to add another question type, 
	// and then for that questiontype you have to add the jokers, so this is really suboptimal.
	// 

	public static void showQuestion(Question question, int questionNumber, Player player) {
		Supportfunctions.seperatorLine();

		_questionNumber = questionNumber;
		_player = player;

		System.out.println(String.format("Frage Nr. %d an %s: ", questionNumber + 1, player.name));

		switch (question.type) {
		case multipleChoice:
			showMultipleChoiceQuestion(question);
			break;
		case trueFalseQuestion:
			showTrueFalseQuestion(question);
			break;
		case userInput:
			showUserInputQuestion(question);
			break;
		default:
			System.out.println("Leider ist ein Problem aufgetreten. Versuchen Sie das Quiz erneut zu starten!");
		}
	}

	private static void showMultipleChoiceQuestion(Question question) {
		int maxTextLength = 0;
		maxTextLength = question.answer1.length() > maxTextLength ? question.answer1.length() : maxTextLength;
		maxTextLength = question.answer3.length() > maxTextLength ? question.answer3.length() : maxTextLength;

		System.out.println(String.format("Frage: %s ", question.question) + "Antworten Sie mit A, B, C oder D!");
		System.out.println(String.format("A: %s", question.answer1) + Supportfunctions.spaces(maxTextLength - question.answer1.length()) + String.format("B: %s", question.answer2));
		System.out.println(String.format("C: %s", question.answer3) + Supportfunctions.spaces(maxTextLength - question.answer3.length()) + String.format("D: %s", question.answer4));
	}

	private static void showTrueFalseQuestion(Question question) {
		System.out.println( String.format("Frage: %s ", question.question) + "Antworten Sie mit w (wahr) oder f (falsch)!");

	}

	private static void showUserInputQuestion(Question question) {

		System.out.println(String.format("Frage: %s ", question.question) + "Anworten Sie die korrekten Antwort mit der Eingabe");

	}

	public static void showMultipleChoiceQuestionWithJoker(Question question, Joker joker) {

		Supportfunctions.seperatorLine();

		switch (joker.type) { // switch falls die Joker erweitert werden sollen
		case fiftyFifty:

			char wrongAnswer = '\0';

			while (true) { // gibt einen Buchstaben f�r eine falsche Antwort zur�ck

				char rndChar = Supportfunctions.getRandomChar("ABCD");

				if (!String.valueOf(rndChar).equalsIgnoreCase(question.correctAnswer)) {
					wrongAnswer = rndChar;
					break;
				}
			}

			char rightAnswer = question.correctAnswer.charAt(0); // gibt den ersten Buchstaben der korrekten Antwort
																	// zur�ck -> Absicherung falls es mehrere Buchstaben
																	// gibt

			// Antwortm�glichkeiten leer setzen aufgrund des Jokers
			String a = String.valueOf(wrongAnswer)
				.equalsIgnoreCase("A") || String.valueOf(rightAnswer).equalsIgnoreCase("A") ? question.answer1 : "";

			String b = String.valueOf(wrongAnswer)
				.equalsIgnoreCase("B") || String.valueOf(rightAnswer).equalsIgnoreCase("B") ? question.answer2 : "";

			String c = String.valueOf(wrongAnswer)
				.equalsIgnoreCase("C") || String.valueOf(rightAnswer).equalsIgnoreCase("C") ? question.answer3 : "";

			String d = String.valueOf(wrongAnswer)
				.equalsIgnoreCase("D") || String.valueOf(rightAnswer).equalsIgnoreCase("D") ? question.answer4 : "";

			// Textl�nge berechnen f�r die richtigen Abst�nde bei dem Anzeigen der Fragen
			int maxTextLength = 0;
			maxTextLength = a.length() > maxTextLength ? a.length() : maxTextLength;
			maxTextLength = c.length() > maxTextLength ? c.length() : maxTextLength;

			System.out.println(String.format("Frage: %s ", question.question) + "Geben Sie A,B,C oder D ein!");

			System.out.println(String.format("A: %s", a) + Supportfunctions.spaces(maxTextLength - a.length()) + String.format("B: %s", b));

			System.out.println(String.format("C: %s", c) + Supportfunctions.spaces(maxTextLength - c.length()) + String.format("D: %s", d));
			break;
		default:
			break;
		}

	}

	public static void showTrueFalseQuestionWithJoker(Question question, Joker joker) {

		Supportfunctions.seperatorLine();

		switch (joker.type) { // switch falls die Joker erweitert werden sollen
		case tipp:
			System.out.println(String.format("Frage: %s ", question.question) + "Antworten Sie mit w (wahr) oder f (falsch)!");
			System.out.println(String.format("Tipp: %s", question.joker));
			break;
		default:
			break;
		}

	}

	public static void showUserInputQuestionWithJoker(Question question, Joker joker) {

		Supportfunctions.seperatorLine();

		System.out.println(String.format("Frage: %s ", question.question) + "Anworten Sie die korrekten Antwort mit der Eingabe");

		switch (joker.type) {
		case tipp:
			System.out.println(String.format("Tipp: %s", question.joker));
			break;
		case letterNumber:
			System.out.println(String.format("Die antwort ist %d Schriftzeichen lang ", question.correctAnswer.length()));
			break;
		case firstLetter:
			System.out.println(String.format("Das Wort beginnt mit: %c", question.correctAnswer.charAt(0)));
			break;
		default:
			break;
		}

	}
}
