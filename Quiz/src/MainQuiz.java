import java.util.Scanner;
import java.util.List;
import java.util.Random;

public class MainQuiz {

	private List<Question> _questions = null;

	private void opening() {
		seperatorLine();
		System.out.println("Willkommen bei [Name].");// TODO: Name einf�gen
	}

	private int initPlayerCount() {
		seperatorLine();
		System.out.println("Bitte geben Sie die Anzahl der Spieler ein, die an diesem Quiz teilnehmen -");

		int playerCount = 0;
		while (playerCount < 2 || playerCount > 6) {// damit nur Spieleranzahl zwischen 2 und 6 m�glich
			System.out.println("Spieleranzahl [2-6]: \n");
			playerCount = getIntFromConsole();
		}

		return playerCount;
	}

	private Player[] initPlayers(int playerCount) {
		seperatorLine();

		// players is a list/ array of the Player object so name it in the plural
		Player[] players = new Player[playerCount];


		for (int i = 0; i < playerCount; i++) {
			System.out.println(String.format("Bitte geben Sie den Namen f�r Spieler %d ein: ", i + 1));

			players[i] = new Player(getStringFromConsole(), 0);
			//this is just a check for devWork
			System.out.println(String.format("Name and score are:", players[i].name + ' ' + players[i].score));
		}

		return players;
	}

	private void startQuiz(Player[] players) {
		seperatorLine();
		
		System.out.println("Das Quiz kann nun beginnen! Die folgenden Spieler haben sich f�r das Quiz angemeldet: \n");
		
		for (int i = 0; i < players.length; i++) {
			System.out.println(String.format("Spieler %s: %s | Punktzahl: %s", i + 1, players[i].name, players[i].score));
		}
		seperatorLine();

		//System.out.println(_questions.size());
		//_question not in scope, need rework 
		for (int round = 0; round <  4; round++) {
			for (int currentPlayer = 0; currentPlayer < players.length; currentPlayer++) {
				Question question = getQuestion();
				if(showQuestion(question,  players.length + currentPlayer, players[currentPlayer].name)) {
					increaseScore(players, currentPlayer);
				}
			}
		}
	}
	
	/**
	 * eine zuf�llige Frage zur�ckgeben aus Fragenliste und Fragenliste bef�llen
	 * @return
	 */
	private Question getQuestion() {
		// init questions and getting single question should be in diff functions
		// TODO init _question at start and make it global (or this._questions)
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
		System.out.println(String.format("Frage Nr. %d f�r %s: ", questionNumber + 1, playerName));
		
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
				"Antworten Sie mit w (f�r wahr) oder f (f�r falsch)!");
		
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
				"Anworten Sie �ber die Eingabe mit der korrekten Antwort!");
		
		String input = getStringFromConsole();
		//trim -> Leerzeichen entfernen, damit dadurch keine Fehler entstehen k�nnen (z.B. zu viele Leerzeichen zwischen W�rtern)
		if (input.trim().equalsIgnoreCase(question.answer1.trim())) {
			isCorrectSolved = true;
		}
		
		return isCorrectSolved;
	}
	
	private void increaseScore (Player[] players, int currentPlayer) {
		int currentScore = players[currentPlayer].score;
		currentScore += 100;
		Player player = players[currentPlayer].setScore(currentScore);
	}
	
	public static void main(String[] args) {
		MainQuiz quiz = new MainQuiz();
		quiz.opening();
		int playerCount = quiz.initPlayerCount();
		Player[] players = quiz.initPlayers(playerCount); // Spieleranzahl �bergeben an initPlayer
		quiz.startQuiz(players);
		System.out.println("Highscore: ");
		for(int i = 0; i < players.length; i++)
		{
			System.out.println(String.format("%s hat %s Punkte", players[i].name, players[i].score));
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
			Integer.parseInt(value); // versucht den String in einen Int zu �berf�hren
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
