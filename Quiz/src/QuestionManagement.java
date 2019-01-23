import java.util.List;
import java.util.Random;

public class QuestionManagement {

	private static List<Question> _questions = null;
	private static int _questionNumber = 0;
	private static Player _player = null;

	/**
	 * eine zufällige Frage zurückgeben aus Fragenliste und Fragenliste befüllen
	 * 
	 * @return
	 */
	public static Question getQuestion() {
		Question question = null;

		if (_questions == null) { // wird hier nur ausgeführt falls im Vorfeld noch keine Initialisierung der
									// Fragen vorgenommen wurde -> sollte eigentlich nicht auftreten
			initQuestionList();
		}

		Random randomGenerator = new Random();

		if (_questions != null) {
			question = _questions.get(randomGenerator.nextInt(_questions.size()));
			_questions.remove(question);
		}

		return question;
	}

	public static List<Question> initQuestionList() {
		QuestionLibrary questionLibrary = new QuestionLibrary();
		_questions = questionLibrary.getQuestions(System.getProperty("user.dir") + "\\questions.accdb");

		return _questions;
	}

	public static Boolean showQuestion(Question question, int questionNumber, Player player) {
		Supportfunctions.seperatorLine();
		
		_questionNumber = questionNumber;
		_player = player;

		System.out.println(String.format("Frage Nr. %d für %s: ", questionNumber + 1, player.name));

		Boolean isCorrectSolved = false;

		switch (question.type) {
		case multipleChoice:
			isCorrectSolved = showMultipleChoiceQuestion(question);
			break;
		case trueFalseQuestion:
			isCorrectSolved = showTrueFalseQuestion(question);
			break;
		case userInput:
			isCorrectSolved = showUserInputQuestion(question);
			break;
		default:
			System.out.println("Leider ist ein Problem aufgetreten. Versuchen Sie das Quiz erneut zu starten!");
		}

		return isCorrectSolved;
	}

	private static Boolean showMultipleChoiceQuestion(Question question) {
		Boolean isCorrectSolved = false;

		System.out.println(String.format("Frage: %s ", question.question) + "Antworten Sie mit A, B, C oder D!");
		System.out.println(String.format("A: %s", question.answer1) + Supportfunctions.spaces()
				+ String.format("B: %s", question.answer2));
		System.out.println(String.format("C: %s", question.answer3) + Supportfunctions.spaces()
				+ String.format("D: %s", question.answer4));

		String input = "";

		Boolean isPossibleAnswer = false;
		while (!isPossibleAnswer) {
			input = Supportfunctions.getStringFromConsole();
			if (input.equalsIgnoreCase("A") || input.equalsIgnoreCase("B") || input.equalsIgnoreCase("C")
					|| input.equalsIgnoreCase("D") || input.equalsIgnoreCase("J")) {
				isPossibleAnswer = true;
			} else {
				System.out.println("Ihre Eingabe entspricht nicht der Vorgabe! Geben Sie A,B,C oder D ein!");
			}
		}

		if (input.equalsIgnoreCase("j")) {
			JokerLibrary.askForJoker(question, _player, _questionNumber);
		}
		else if (input.equalsIgnoreCase(question.correctAnswer)) {
			isCorrectSolved = true;
		}

		return isCorrectSolved;
	}

	private static Boolean showTrueFalseQuestion(Question question) {
		Boolean isCorrectSolved = false;

		System.out.println(
				String.format("Frage: %s ", question.question) + "Antworten Sie mit w (für wahr) oder f (für falsch)!");

		String input = "";

		Boolean isPossibleAnswer = false;
		while (!isPossibleAnswer) {
			input = Supportfunctions.getStringFromConsole();
			if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("f") || input.equalsIgnoreCase("J")) {
				isPossibleAnswer = true;
			} else {
				System.out.println("Ihre Eingabe entspricht nicht der Vorgabe! Geben Sie w oder f!");
			}
		}
		if (input.equalsIgnoreCase("j")) {
			JokerLibrary.askForJoker(question, _player, _questionNumber);
		}
		else if (input.equalsIgnoreCase(question.correctAnswer)) {
			isCorrectSolved = true;
		}

		return isCorrectSolved;
	}

	private static Boolean showUserInputQuestion(Question question) {
		Boolean isCorrectSolved = false;

		System.out.println(String.format("Frage: %s ", question.question)
				+ "Anworten Sie über die Eingabe mit der korrekten Antwort!");

		String input = Supportfunctions.getStringFromConsole();
		// trim -> Leerzeichen entfernen, damit dadurch keine Fehler entstehen können
		// (z.B. zu viele Leerzeichen zwischen Wörtern)
		
		if (input.equalsIgnoreCase("j")) {
			JokerLibrary.askForJoker(question, _player, _questionNumber);
		}
		else if (input.trim().equalsIgnoreCase(question.correctAnswer.trim())) {
			isCorrectSolved = true;
		}

		return isCorrectSolved;
	}

}
