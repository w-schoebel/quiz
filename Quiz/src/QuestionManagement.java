import java.util.ArrayList;
import java.util.List;

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
	 * @return List<Question> _questions
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
	 * takes number and returns List of certain questiontype
	 * questionType is determined by number % x == 0
	 * @param questionNumber int 
	 * @return List<Question> questionTpeList
	 */
	public static List<Question> getQuestionTypeList(int questionNumber) {

		if (_questions == null) initQuestionList();
	

		if (questionNumber % 3 == 0) return _questionsUserInput;  
		else if (questionNumber % 2 == 0) return _questionsTrueFalse;  
		else return _questionsMultipleChoice;
		

	}

	/**
	 * takes List of questions and then returns random question out of that array
	 * @param List<Question> List of question class instances
	 * @return Question question
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
			if (_questions != null) {

				question = _questions.get(Supportfunctions.getRandomInt(_questions.size()));

				_questions.remove(question);
			}
			return question;
		}



	}



	/**
	 * forces user to enter a valid input
	 * @param question question class instance
	 * @return String input
	 */
	public static String forcePossibleInput(Question question) {

		Boolean isPossibleAnswer = false;
		String input = "";

		switch (question.type) {
		case multipleChoice:

			while (!isPossibleAnswer) {
				input = Supportfunctions.getStringFromConsole();

				if (	input.equalsIgnoreCase("A") 
						|| input.equalsIgnoreCase("B") 
						|| input.equalsIgnoreCase("C")
						|| input.equalsIgnoreCase("D") 
						|| input.equalsIgnoreCase("J")) {
					isPossibleAnswer = true;
				} else {
					System.out.println("Ihre Eingabe entspricht nicht der Vorgabe! Geben Sie A, B, C oder D ein!");
				}
			}


			break;

		case trueFalseQuestion:

			while (!isPossibleAnswer) {
				input = Supportfunctions.getStringFromConsole();
				if (	input.equalsIgnoreCase("w") 
						|| input.equalsIgnoreCase("f")
						|| input.equalsIgnoreCase("J")) {
					isPossibleAnswer = true;
				} else {
					System.out.println("Ihre Eingabe entspricht nicht der Vorgabe! Geben Sie W oder F ein!");
				}
			}

			break;
		case userInput:

			input = Supportfunctions.getStringFromConsole();

			break;
		}

		return input;

	}
	/**
	 * 
	 * checks questiontypes and then checks if user answer is correct
	 * @param question question class instance
	 * @param input input from user
	 * @return boolean isCorrectSolved
	 */
	public static Boolean checkAnswer(Question question, String input) {
		if(question.type == QuestionType.multipleChoice || question.type == QuestionType.trueFalseQuestion ){
			if (input.equalsIgnoreCase(question.correctAnswer)) return true;
		}
		if(question.type == QuestionType.userInput){
			 if (input.trim().equalsIgnoreCase(question.correctAnswer.trim())) {
				return true;
			}

		}
		return false;
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	// From now on its all about showQuestion. Ideally this would be refactored.
	// The current structure makes it really hard to add another question type, 
	// and then for that questiontype you have to add the jokers, so this is really suboptimal.
	// 

	/**
	 * sorts current question for type then displays it
	 * @param question question class instance 
	 * @param questionNumber  int number of current question 
	 * @param player player class instance
	 * @return void
	 */
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

	/**
	 * shows multiple choice question
	 * @param question
	 * @return void
	 */
	private static void showMultipleChoiceQuestion(Question question) {
		int maxTextLength = 0;
		maxTextLength = question.answer1.length() > maxTextLength ? question.answer1.length() : maxTextLength;
		maxTextLength = question.answer3.length() > maxTextLength ? question.answer3.length() : maxTextLength;

		System.out.println(String.format("Frage: %s ", question.question) + "Antworten Sie mit A, B, C oder D!");
		System.out.println(String.format("A: %s", question.answer1) + Supportfunctions.spaces(maxTextLength - question.answer1.length()) + String.format("B: %s", question.answer2));
		System.out.println(String.format("C: %s", question.answer3) + Supportfunctions.spaces(maxTextLength - question.answer3.length()) + String.format("D: %s", question.answer4));
	}

	/**
	 * shows true false question
	 * @param question
	 * @return void
	 */
	private static void showTrueFalseQuestion(Question question) {
		System.out.println( String.format("Frage: %s ", question.question) + "Antworten Sie mit W (wahr) oder F (falsch)!");

	}

	/**
	 * show userinput question
	 * @param question
	 * @return void
	 */
	private static void showUserInputQuestion(Question question) {

		System.out.println(String.format("Frage: %s ", question.question) + "Geben Sie die korrekte Antwort ein!");

	}

	/**
	 * show multiple choice but with joker
	 * @param question
	 * @param joker
	 * @return void
	 */
	public static void showMultipleChoiceQuestionWithJoker(Question question, Joker joker) {

		Supportfunctions.seperatorLine();

		// switch in case further types will be added
		switch (joker.type) { 
		case fiftyFifty:

			char wrongAnswer = '\0';

			//return letter for wrong answer
			while (true) { 

				char rndChar = Supportfunctions.getRandomChar("ABCD");

				if (!String.valueOf(rndChar).equalsIgnoreCase(question.correctAnswer)) {
					wrongAnswer = rndChar;
					break;
				}
			}

			//returns first letter of correct answer
			//safty in case multiple letters
			char rightAnswer = question.correctAnswer.charAt(0);

			// convert letter into imptey spaces 
			String a = String.valueOf(wrongAnswer)
				.equalsIgnoreCase("A") || String.valueOf(rightAnswer).equalsIgnoreCase("A") ? question.answer1 : "";

			String b = String.valueOf(wrongAnswer)
				.equalsIgnoreCase("B") || String.valueOf(rightAnswer).equalsIgnoreCase("B") ? question.answer2 : "";

			String c = String.valueOf(wrongAnswer)
				.equalsIgnoreCase("C") || String.valueOf(rightAnswer).equalsIgnoreCase("C") ? question.answer3 : "";

			String d = String.valueOf(wrongAnswer)
				.equalsIgnoreCase("D") || String.valueOf(rightAnswer).equalsIgnoreCase("D") ? question.answer4 : "";

		
			// text length for correct spaces when displaying the questions
			int maxTextLength = 0;
			maxTextLength = a.length() > maxTextLength ? a.length() : maxTextLength;
			maxTextLength = c.length() > maxTextLength ? c.length() : maxTextLength;

			System.out.println(String.format("Frage: %s ", question.question) + "Geben Sie A, B, C oder D ein!");

			System.out.println(String.format("A: %s", a) + Supportfunctions.spaces(maxTextLength - a.length()) + String.format("B: %s", b));

			System.out.println(String.format("C: %s", c) + Supportfunctions.spaces(maxTextLength - c.length()) + String.format("D: %s", d));
			break;
		default:
			break;
		}

	}

	/**
	 * show true false question but with joker
	 * @param question
	 * @param joker
	 * @return void
	 */
	public static void showTrueFalseQuestionWithJoker(Question question, Joker joker) {

		Supportfunctions.seperatorLine();

		// switch in case for more types
		switch (joker.type) { 
		case tipp:
			System.out.println(String.format("Frage: %s ", question.question) + "Antworten Sie mit W (wahr) oder F (falsch)!");
			System.out.println(String.format("Tipp: %s", question.joker));
			break;
		default:
			break;
		}

	}

	/**
	 * show userinput but with joker
	 * @param question
	 * @param joker
	 * @return void
	 */
	public static void showUserInputQuestionWithJoker(Question question, Joker joker) {

		Supportfunctions.seperatorLine();

		System.out.println(String.format("Frage: %s ", question.question) + "Geben Sie die korrekte Antwort ein!");

		switch (joker.type) {
		case tipp:
			System.out.println(String.format("Tipp: %s", question.joker));
			break;
		case letterNumber:
			System.out.println(String.format("Die Antwort ist %d Schriftzeichen lang ", question.correctAnswer.length()));
			break;
		case firstLetter:
			System.out.println(String.format("Das Wort beginnt mit: %c", question.correctAnswer.charAt(0)));
			break;
		default:
			break;
		}

	}
}
