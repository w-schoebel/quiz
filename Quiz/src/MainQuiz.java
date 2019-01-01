import java.util.Scanner;
import java.util.List;
import java.util.Random;

public class MainQuiz {

	private List<Question> _questions = null;

	private void opening() {
		seperatorLine();
		System.out.println("Willkommen bei [Name].");// TODO: Name einfügen
	}

	private int initPlayerCount() {
		seperatorLine();
		System.out.println("Bitte geben Sie die Anzahl der Spieler ein, die an diesem Quiz teilnehmen -");

		int playerCount = 0;
		while (playerCount < 2 || playerCount > 6) {// damit nur Spieleranzahl zwischen 2 und 6 möglich
			System.out.println("Spieleranzahl [2-6]: \n");
			playerCount = getIntFromConsole();
		}

		return playerCount;
	}

	private String[][] initPlayer(int playerCount) {
		seperatorLine();

		String[][] player = new String[playerCount][playerCount]; // beinhaltet Name und Punktzahl

		for (int i = 0; i < playerCount; i++) {
			System.out.println(String.format("Bitte geben Sie den Namen für Spieler %d ein: ", i + 1));
			player[i][0] = getStringFromConsole();
			player[i][1] = "0";
		}

		return player;
	}

	private void startQuiz(String[][] player) {
		seperatorLine();
		
		System.out.println("Das Quiz kann nun beginnen! Die folgenden Spieler haben sich für das Quiz angemeldet: \n");
		
		for (int i = 0; i < player.length; i++) {
			System.out.println(String.format("Spieler %d: %s | Punktzahl: %s", i + 1, player[i][0], player[i][1]));
		}
		seperatorLine();
		
		System.out.println("Wie viele Runden sollen gespielt werden: ");
		int roundCount = getIntFromConsole();
		
		for (int round = 0; round < roundCount; round++) {
			for (int currentPlayer = 0; currentPlayer < player.length; currentPlayer++) {
				Question question = getQuestion();
				if(showQuestion(question, round * player.length + currentPlayer, player[currentPlayer][0])) {
					increaseScore(player, currentPlayer);
				}
			}
		}	
	}
	
	/**
	 * eine zufällige Frage zurückgeben aus Fragenliste und Fragenliste befüllen
	 * @return
	 */
	private Question getQuestion() {
		
		Question question = null;
		
		if (_questions == null) {
			QuestionLibrary questionLibrary = new QuestionLibrary();
			_questions = questionLibrary.getQuestions(System.getProperty("user.dir") + "\\questions.accdb");
		}
		
		Random randomGenerator = new Random();
		
		if (_questions != null) {
			question = _questions.get(randomGenerator.nextInt(_questions.size()));
			_questions.remove(question);
		}
		
		return question;
	}
	
	private Boolean showQuestion(Question question, int questionNumber, String playerName) {
		seperatorLine();
		System.out.println(String.format("Frage Nr. %d für %s: ", questionNumber + 1, playerName));
		
		Boolean isCorrectSolved = false;
		
		switch(question.type) {
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

	private Boolean showMultipleChoiceQuestion(Question question) {
		Boolean isCorrectSolved = false;
		
		System.out.println(String.format("Frage: %s ", question.question) + 
				"Antworten Sie mit A, B, C oder D!");
		System.out.println(String.format("A: %s", question.answer1) + 
				spaces() + String.format("B: %s", question.answer2));
		System.out.println(String.format("C: %s", question.answer3) + 
				spaces() + String.format("D: %s", question.answer4));
		
		String input = "";
		
		Boolean isPossibleAnswer = false;
		while (!isPossibleAnswer) {
			input = getStringFromConsole();
			if (input.equalsIgnoreCase("A") || input.equalsIgnoreCase("B") ||
					input.equalsIgnoreCase("C") || input.equalsIgnoreCase("D")) {
				isPossibleAnswer = true;
			}
			else {
				System.out.println("Ihre Eingabe entspricht nicht der Vorgabe! Geben Sie A,B,C oder D ein!");
			}
		}
		
		if (input.equalsIgnoreCase("A")) {
			if (question.correctAnswer == 1) {
				isCorrectSolved = true;
			}
		}
		else if (input.equalsIgnoreCase("B")) {
			if (question.correctAnswer == 2) {
				isCorrectSolved = true;
			}
		}
		else if (input.equalsIgnoreCase("C")) {
			if (question.correctAnswer == 3) {
				isCorrectSolved = true;
			}
		}
		else if (input.equalsIgnoreCase("D")) {
			if (question.correctAnswer == 4) {
				isCorrectSolved = true;
			}
		}
		
		return isCorrectSolved;
	}
	
	private Boolean showTrueFalseQuestion (Question question) {
		Boolean isCorrectSolved = false;
		
		System.out.println(String.format("Frage: %s ", question.question) + 
				"Antworten Sie mit w (für wahr) oder f (für falsch)!");
		
		String input = "";
		
		Boolean isPossibleAnswer = false;
		while (!isPossibleAnswer) {
			input = getStringFromConsole();
			if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("f")) {
				isPossibleAnswer = true;
			}
			else {
				System.out.println("Ihre Eingabe entspricht nicht der Vorgabe! Geben Sie w oder f ein!");
			}
		}
		
		if (input.equalsIgnoreCase("w")) {
			if (question.correctAnswer == 1) {
				isCorrectSolved = true;
			}
		}
		else if (input.equalsIgnoreCase("f")) {
			if (question.correctAnswer == 2) {
				isCorrectSolved = true;
			}
		}
		
		return isCorrectSolved;
	}
	
	private Boolean showUserInputQuestion(Question question) {
		Boolean isCorrectSolved = false;
		
		System.out.println(String.format("Frage: %s ", question.question) + 
				"Anworten Sie über die Eingabe mit der korrekten Antwort!");
		
		String input = getStringFromConsole();
		//trim -> Leerzeichen entfernen, damit dadurch keine Fehler entstehen können (z.B. zu viele Leerzeichen zwischen Wörtern)
		if (input.trim().equalsIgnoreCase(question.answer1.trim())) {
			isCorrectSolved = true;
		}
		
		return isCorrectSolved;
	}
	
	private void increaseScore (String[][] player, int currentPlayer) {
		String currentScore = player [currentPlayer][1];
		if(tryParseInt(currentScore)) {
			Integer newScore = Integer.parseInt(currentScore) + 100;
			player[currentPlayer][1] = newScore.toString();
		}
	}
	
	public static void main(String[] args) {
		MainQuiz quiz = new MainQuiz();
		quiz.opening();
		int playerCount = quiz.initPlayerCount();
		String[][] player = quiz.initPlayer(playerCount); // Spieleranzahl übergeben an initPlayer
		quiz.startQuiz(player);
		System.out.println("Highscore: ");
		for(int i = 0; i < player.length; i++)
		{
			System.out.println(String.format("%s hat %s Punkte", player[i][0], player[i][1]));
		}
	}

	// Hilfsfunktionen

	private int getIntFromConsole() {
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

	private String getStringFromConsole() {
		Scanner sc01 = new Scanner(System.in);
		String value = sc01.next();

		return value;
	}

	private boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value); // versucht den String in einen Int zu überführen
			return true;
		} catch (NumberFormatException e) { // wenn kein Int geparst werden kann-> Exception e
			return false;
		}
	}

	public void seperatorLine() {
		System.out.println(
				"--------------------------------------------------------------------------------------------------------");
	}
	
	private String spaces() {
		return "        ";
	}
}
