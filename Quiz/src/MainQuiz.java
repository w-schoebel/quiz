import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainQuiz {

	private List<Question> _questions = null;

	private void opening() {
	Supportfunctions.seperatorLine();
		System.out.println("Willkommen bei [Name].");// TODO: Name einfügen
	}

	private int initPlayerCount() {
	Supportfunctions.seperatorLine();
		System.out.println("Bitte geben Sie die Anzahl der Spieler ein, die an diesem Quiz teilnehmen -");

		int playerCount = 0;
		while (playerCount < 2 || playerCount > 6) {// damit nur Spieleranzahl zwischen 2 und 6 möglich
			System.out.println("Spieleranzahl [2-6]: \n");
			playerCount = Supportfunctions.getIntFromConsole();
		}

		return playerCount;
	}

	private Player[] initPlayers(int playerCount) {
	Supportfunctions.seperatorLine();

		// players is a list/ array of the Player object so name it in the plural
		Player[] players = new Player[playerCount];


		for (int i = 0; i < playerCount; i++) {
			System.out.println(String.format("Bitte geben Sie den Namen für Spieler %d ein: ", i + 1));

			players[i] = new Player(Supportfunctions.getStringFromConsole(), 0);
			//this is just a check for devWork
			//System.out.println(String.format("Name und Punktzahl: %s | %d", players[i].name, players[i].score));
		}

		return players;
	}

	private void startQuiz(Player[] players) {
	Supportfunctions.seperatorLine();
		
		System.out.println("Das Quiz kann nun beginnen! Die folgenden Spieler haben sich für das Quiz angemeldet: \n");
		
		for (int i = 0; i < players.length; i++) {
			System.out.println(String.format("Spieler %s: %s | Punktzahl: %s", i + 1, players[i].name, players[i].score));
		}
	Supportfunctions.seperatorLine();
		
		//FragenListe initialisieren um mit der Fragenanzahl arbeiten zu können
		initQuestionList();
		
		int roundCount = 1;
		int roundLimit = (int)_questions.size()/players.length;
		System.out.println(String.format("Wie viele Runden sollen gespielt werden: 1 - %d Runden", roundLimit));
		
		Boolean isPossibleInput = false;
		
		while(!(isPossibleInput))
		{
			roundCount = Supportfunctions.getIntFromConsole();
			
			if(roundCount < 1  || roundCount > roundLimit)
			{
				System.out.println(String.format("Ihre Eingabe entspricht nicht der Vorgabe! "
						+ "Geben Sie eine Zahl zwischen 1 - %d ein", roundLimit));
			}
			else
			{
				isPossibleInput = true;
			}
		}

		for (int round = 0; round < roundCount; round++) {
			for (int currentPlayer = 0; currentPlayer < players.length; currentPlayer++) {
				Question question = getQuestion();
				if(showQuestion(question,  round * players.length + currentPlayer, players[currentPlayer].name)) {
					increaseScore(players, currentPlayer);
					System.out.println("Die Antwort ist richtig!");
					
				}
				else {
					System.out.println("Die Antwort ist falsch!");
				}
			System.out.println(String.format( "Die aktuelle Punktzahl ist: %d", players[currentPlayer].score));
			}
		}
	}
	
	/**
	 * eine zufällige Frage zurückgeben aus Fragenliste und Fragenliste befüllen
	 * @return
	 */
	private Question getQuestion() {
		// init questions and getting single question should be in diff functions
		// TODO init _question at start and make it global (or this._questions)
		Question question = null;
		
		if (_questions == null) {
			initQuestionList();
		}
		
		Random randomGenerator = new Random();
		
		if (_questions != null) {
			question = _questions.get(randomGenerator.nextInt(_questions.size()));
			_questions.remove(question);
		}
		
		return question;
	}
	
	private void initQuestionList() {
			QuestionLibrary questionLibrary = new QuestionLibrary();
			_questions = questionLibrary.getQuestions(System.getProperty("user.dir") + "\\questions.accdb");
	}
	
	private Boolean showQuestion(Question question, int questionNumber, String playerName) {
	Supportfunctions.seperatorLine();
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
				Supportfunctions.spaces() + String.format("B: %s", question.answer2));
		System.out.println(String.format("C: %s", question.answer3) + 
				Supportfunctions.spaces() + String.format("D: %s", question.answer4));
		
		String input = "";
		
		Boolean isPossibleAnswer = false;
		while (!isPossibleAnswer) {
			input = Supportfunctions.getStringFromConsole();
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
			input = Supportfunctions.getStringFromConsole();
			if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("f")) {
				isPossibleAnswer = true;
			}
			else {
				System.out.println("Ihre Eingabe entspricht nicht der Vorgabe! Geben Sie w oder f!");
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
		
		String input = Supportfunctions.getStringFromConsole();
		//trim -> Leerzeichen entfernen, damit dadurch keine Fehler entstehen können (z.B. zu viele Leerzeichen zwischen Wörtern)
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
	private void showHighscore(Player[] players){
	Supportfunctions.seperatorLine();
		System.out.println("Highscore: ");
		
		Arrays.sort(players, Collections.reverseOrder());
		
		boolean draw = false;
		
		for(int i = 0; i < players.length; i++)
		{
			System.out.println(String.format("%s hat %s Punkte", players[i].name, players[i].score));
			if (i == 1 && players[i].score == players[0].score){
				draw = true; 
				}
		}
	Supportfunctions.seperatorLine(); 
		
		if (draw) {
			System.out.println("Unentschieden");
		}
		else {
			System.out.println(String.format("Der Gewinner ist: %s mit einer Punktzahl von %d ", players[0].name, players[0].score));
		}
				
	}
	
	
	public static void main(String[] args) {
		MainQuiz quiz = new MainQuiz();
		quiz.opening();
		int playerCount = quiz.initPlayerCount();
		Player[] players = quiz.initPlayers(playerCount); // Spieleranzahl übergeben an initPlayer
		quiz.startQuiz(players);
		quiz.showHighscore(players);
	}

	// Hilfsfunktionen

	
}
