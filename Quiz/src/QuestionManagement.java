import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class QuestionManagement {

	private static List<Question> _questions = null;
	private static List<Question> _questionsMultipleChoice = null;
	private static List<Question> _questionsUserInput = null;
	private static List<Question> _questionsTrueFalse = null;

	private static int _questionNumber = 0;
	private static Player _player = null;

	/**
	 * eine zufällige Frage zurückgeben aus Fragenliste und Fragenliste befüllen
	 * 
	 * @return
	 */
	public static Question getQuestion(int questionNumber) {
		Question question = null;

		if (_questions == null) { // wird hier nur ausgeführt falls im Vorfeld noch keine Initialisierung der
									// Fragen vorgenommen wurde -> sollte eigentlich nicht auftreten
			initQuestionList();
		}

		Boolean noPreferedQuestion = false;

		if (questionNumber % 3 == 0) {
			if (_questionsUserInput != null || _questionsUserInput.size() == 0) {

				question = _questionsUserInput.get(Supportfunctions.getRandomInt(_questionsUserInput.size()));

				_questionsUserInput.remove(question);
				_questions.remove(question);
			} else {
				noPreferedQuestion = true;
			}

		} else if (questionNumber % 2 == 0) {
			if (_questionsTrueFalse != null || _questionsTrueFalse.size() == 0) {

				question = _questionsTrueFalse.get(Supportfunctions.getRandomInt(_questionsTrueFalse.size()));

				_questionsTrueFalse.remove(question);
				_questions.remove(question);
			} else {
				noPreferedQuestion = true;
			}

		} else {
			if (_questionsMultipleChoice != null || _questionsMultipleChoice.size() == 0) {

				question = _questionsMultipleChoice.get(Supportfunctions.getRandomInt(_questionsMultipleChoice.size()));

				_questionsMultipleChoice.remove(question);
				_questions.remove(question);
			} else {
				noPreferedQuestion = true;
			}
		}

		if (noPreferedQuestion) {
			if (_questions != null) {

				question = _questions.get(Supportfunctions.getRandomInt(_questions.size()));

				_questions.remove(question);
			}
		}

		return question;
	}

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
		int maxTextLength = 0;
		maxTextLength = question.answer1.length() > maxTextLength ? question.answer1.length() : maxTextLength;
		maxTextLength = question.answer3.length() > maxTextLength ? question.answer3.length() : maxTextLength;

		System.out.println(String.format("Frage: %s ", question.question) + "Antworten Sie mit A, B, C oder D!");
		System.out.println(String.format("A: %s", question.answer1)
				+ Supportfunctions.spaces(maxTextLength - question.answer1.length())
				+ String.format("B: %s", question.answer2));
		System.out.println(String.format("C: %s", question.answer3)
				+ Supportfunctions.spaces(maxTextLength - question.answer3.length())
				+ String.format("D: %s", question.answer4));

		return proofAnswer(question, false);
	}

	private static Boolean showTrueFalseQuestion(Question question) {
		System.out.println(
				String.format("Frage: %s ", question.question) + "Antworten Sie mit w (für wahr) oder f (für falsch)!");

		return proofAnswer(question, false);
	}

	private static Boolean showUserInputQuestion(Question question) {

		System.out.println(String.format("Frage: %s ", question.question)
				+ "Anworten Sie über die Eingabe mit der korrekten Antwort!");

		return proofAnswer(question, false);
	}

	public static Boolean showMultipleChoiceQuestionWithJoker(Question question, Joker joker) {

		Supportfunctions.seperatorLine();

		switch (joker.type) { // switch falls die Joker erweitert werden sollen
		case fiftyFifty:

			char wrongAnswer = '\0';

			while (true) { // gibt einen Buchstaben für eine falsche Antwort zurück

				char rndChar = Supportfunctions.getRandomChar("ABCD");

				if (!String.valueOf(rndChar).equalsIgnoreCase(question.correctAnswer)) {
					wrongAnswer = rndChar;
					break;
				}
			}

			char rightAnswer = question.correctAnswer.charAt(0); // gibt den ersten Buchstaben der korrekten Antwort
																	// zurück -> Absicherung falls es mehrere Buchstaben
																	// gibt

			// Antwortmöglichkeiten leer setzen aufgrund des Jokers
			String a = String.valueOf(wrongAnswer).equalsIgnoreCase("A")
					|| String.valueOf(rightAnswer).equalsIgnoreCase("A") ? question.answer1 : "";
			String b = String.valueOf(wrongAnswer).equalsIgnoreCase("B")
					|| String.valueOf(rightAnswer).equalsIgnoreCase("B") ? question.answer2 : "";
			String c = String.valueOf(wrongAnswer).equalsIgnoreCase("C")
					|| String.valueOf(rightAnswer).equalsIgnoreCase("C") ? question.answer3 : "";
			String d = String.valueOf(wrongAnswer).equalsIgnoreCase("D")
					|| String.valueOf(rightAnswer).equalsIgnoreCase("D") ? question.answer4 : "";

			// Textlänge berechnen für die richtigen Abstände bei dem Anzeigen der Fragen
			int maxTextLength = 0;
			maxTextLength = a.length() > maxTextLength ? a.length() : maxTextLength;
			maxTextLength = c.length() > maxTextLength ? c.length() : maxTextLength;

			System.out.println(String.format("Frage: %s ", question.question) + "Geben Sie A,B,C oder D ein!");

			System.out.println(String.format("A: %s", a) + Supportfunctions.spaces(maxTextLength - a.length())
					+ String.format("B: %s", b));

			System.out.println(String.format("C: %s", c) + Supportfunctions.spaces(maxTextLength - c.length())
					+ String.format("D: %s", d));
			break;
		default:
			break;
		}

		return proofAnswer(question, true);
	}

	public static Boolean showTrueFalseQuestionWithJoker(Question question, Joker joker) {

		Supportfunctions.seperatorLine();

		switch (joker.type) { // switch falls die Joker erweitert werden sollen
		case tipp:
			System.out.println(String.format("Frage: %s ", question.question)
					+ "Antworten Sie mit w (für wahr) oder f (für falsch)!");
			System.out.println(String.format("Tipp: %s", question.joker));
			break;
		default:
			break;
		}

		return proofAnswer(question, true);
	}

	public static Boolean showUserInputQuestionWithJoker(Question question, Joker joker) {

		Supportfunctions.seperatorLine();

		System.out.println(String.format("Frage: %s ", question.question)
				+ "Anworten Sie über die Eingabe mit der korrekten Antwort!");

		switch (joker.type) {
		case tipp:
			System.out.println(String.format("Tipp: %s", question.joker));
			break;
		case letterNumber:
			System.out.println(String.format("Die Länge der Antwort beträgt: %d", question.correctAnswer.length()));
			break;
		case firstLetter:
			System.out.println(String.format("Das Wort beginnt mit: %c", question.correctAnswer.charAt(0)));
			break;
		default:
			break;
		}

		return proofAnswer(question, true);
	}

	private static Boolean proofAnswer(Question question, Boolean jokerAlreadyUsed) {

		Boolean isCorrectSolved = false;
		Boolean isPossibleAnswer = false;
		String input = "";

		switch (question.type) {
		case multipleChoice:

			while (!isPossibleAnswer) {
				input = Supportfunctions.getStringFromConsole();
				if (input.equalsIgnoreCase("A") || input.equalsIgnoreCase("B") || input.equalsIgnoreCase("C")
						|| input.equalsIgnoreCase("D") || (!jokerAlreadyUsed && input.equalsIgnoreCase("J"))) {
					isPossibleAnswer = true;
				} else {
					System.out.println("Ihre Eingabe entspricht nicht der Vorgabe! Geben Sie A,B,C oder D ein!");
				}
			}

			if (input.equalsIgnoreCase("j")) {
				isCorrectSolved = JokerLibrary.askForJoker(question, _player, _questionNumber);
			} else if (input.equalsIgnoreCase(question.correctAnswer)) {
				isCorrectSolved = true;
			}

			break;

		case trueFalseQuestion:

			while (!isPossibleAnswer) {
				input = Supportfunctions.getStringFromConsole();
				if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("f")
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
			// trim -> Leerzeichen entfernen, damit dadurch keine Fehler entstehen können
			// (z.B. zu viele Leerzeichen zwischen Wörtern)

			if (!jokerAlreadyUsed && input.equalsIgnoreCase("J")) {
				isCorrectSolved = JokerLibrary.askForJoker(question, _player, _questionNumber);
			} else if (input.trim().equalsIgnoreCase(question.correctAnswer.trim())) {
				isCorrectSolved = true;
			}

			break;
		}

		return isCorrectSolved;

	}
}
