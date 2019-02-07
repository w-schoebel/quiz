import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainQuiz {

	private static List<Question> _questions = null;

	//this function could be removed
	private void opening() {
		Supportfunctions.seperatorLine();
		System.out.println("Willkommen bei [Name].");// TODO: Name einf�gen
	}

	/**
	 * ask players for how many players there are
	 * @return int playerCount
	 */
	private int initPlayerCount() {
		Supportfunctions.seperatorLine();
		System.out.println("Bitte geben Sie die Anzahl der Spieler ein, die an diesem Quiz teilnehmen -");

		int playerCount = 0;
		while (playerCount < 2 || playerCount > 6) {// damit nur Spieleranzahl zwischen 2 und 6 m�glich
			System.out.println("Spieleranzahl [2-6]: \n");
			playerCount = Supportfunctions.getIntFromConsole();
		}

		return playerCount;
	}

	/**
	 * ask players to enter their name in order to have a list of players
	 * @param playerCount
	 * @return list of player objects
	 */
	private Player[] initPlayers(int playerCount) {
		Supportfunctions.seperatorLine();

		// players is a list/ array of the Player object so name it in the plural
		Player[] players = new Player[playerCount];

		for (int i = 0; i < playerCount; i++) {
			System.out.println(String.format("Bitte geben Sie den Namen von Spieler %d ein: ", i + 1));

			players[i] = new Player(Supportfunctions.getStringFromConsole(), 0);
		}

		return players;
	}
	/**
	 * display the current score rankings
	 * @param players
	 */
	private static void showScoreBoard(Player[] players){
		for (int i = 0; i < players.length; i++) {
			System.out.println(String.format("Spieler %s: %s | Punktzahl: %s", i + 1, players[i].name, players[i].score));
		}
	}
	/**
	 * ask players how many round they each want to play
	 * @param players
	 * @return
	 */
	private static int askRoundNumber(Player[] players){
		// initialize Questionlist 
		_questions = QuestionManagement.initQuestionList();
		// set round limit
		int roundCount = 1;
		int roundLimit = (int) _questions.size() / players.length;

		System.out.println(String.format("Wie viele Runden sollen gespielt werden: 1 - %d Runden", roundLimit));

		Boolean isPossibleInput = false;

		while (!isPossibleInput) {
			roundCount = Supportfunctions.getIntFromConsole();

			if (roundCount < 1 || roundCount > roundLimit) {
				System.out.println(String.format( "Ihre Eingabe entspricht nicht der Vorgabe! " + "Geben Sie eine Zahl zwischen 1 - %d ein", roundLimit));
			} else {
				isPossibleInput = true;
			}
		}
		return roundCount;

	}


	/**
	 * increase score
	 * @param players
	 * @param currentPlayer
	 */
	private void increaseScore(Player[] players, int currentPlayer) {
		int currentScore = players[currentPlayer].score;
		currentScore += 100;
		players[currentPlayer].setScore(currentScore);
	}
	/**
	 * check if there is a draw
	 * @param players
	 * @return boolean draw 
	 */
	private boolean isDraw(Player[] players) {
		boolean draw = false;
		for (int i = 0; i < players.length; i++) {
			if (i == 1 && players[i].score == players[0].score) draw = true;
		}
		return draw;
		
	}


	/**
	 * sorts and shows highest player
	 * @param players
	 */
	private void showHighscore(Player[] players) {
		Supportfunctions.seperatorLine();
		System.out.println("Highscore: ");

		Arrays.sort(players, Collections.reverseOrder());
		// maybe put showScoreBoard one level higher
		showScoreBoard(players);
		Supportfunctions.seperatorLine();

		boolean draw = isDraw(players);
		if (draw) System.out.println("Unentschieden!");
		else System.out.println(String.format("Der Gewinner ist: %s mit einer Punktzahl von %d ", players[0].name, players[0].score));
		
	}

	/**
	 * deep nesting here, => player.initJokerList => JokerLibrary.getJoker => JokerLibrary.iniJokerList 
	 * initJokerList changes Joker class variable jokerList which then is returned by getJoker in Player class, 
	 * so jokerlist from initJokerList is transported that way
	 * @param players
	 * @param questionCount
	 */
	private static void initJokerForEachPlayer(Player[] players, int questionCount) {
		for (Player player : players) player.initJokerList(questionCount);
	}
	/**
	 * this is the main function of the programm.
	 * The for loop loops over the roundCount and the playerCount to ask each player a question.
	 * The question is then checked and the players score adjusted
	 * 
	 * @param players
	 * @param roundCount
	 */
	private void runQuiz(Player[] players, int roundCount) {
		Supportfunctions.seperatorLine();



		Supportfunctions.seperatorLine();


		System.out.println("In jeder Runde können Sie über den Buchstaben J einen Joker verwenden. Solange bis keine Joker mehr zur Verfügung stehen.");
		System.out.println("Für jeden Fragentyp stehen unterschiedliche Joker zur Auswahl.");

		// vital part of programm here
		for (int roundIndex = 0; roundIndex < roundCount; roundIndex++) {
			for (int currentPlayerIndex = 0; currentPlayerIndex < players.length; currentPlayerIndex++) {

				// +1 because we start with question 1 and so that modulo works
				List<Question> questionTypeList = QuestionManagement.getQuestionTypeList(roundIndex + 1); 
				Question question = QuestionManagement.getQuestion(questionTypeList);
				int questionNumber = roundIndex * players.length + currentPlayerIndex;
				

				QuestionManagement.showQuestion(question, questionNumber, players[currentPlayerIndex]);

				String input = QuestionManagement.forcePossibleInput(question, false);
				if(input.equalsIgnoreCase("J")){
					JokerLibrary.askForJoker(question, players[currentPlayerIndex], questionNumber);
					input = QuestionManagement.forcePossibleInput(question, false);
				}
				Boolean isCorrectSolved = QuestionManagement.checkAnswer(question, input);

				if (isCorrectSolved) {
					increaseScore(players, currentPlayerIndex);
					System.out.println("Die Antwort ist richtig!");
				} else {
					System.out.println(String.format("Die Antwort ist falsch! Die Richtige Antwort wäre: %s", question.correctAnswer));
				}
				System.out.println(String.format("Die aktuelle Punktzahl ist: %d", players[currentPlayerIndex].score));
			}
		}
	}

	/**
	 * main function
	 * init all vars, run quiz, end
	 * @param args
	 */
	public static void main(String[] args) {
		MainQuiz quiz = new MainQuiz();
		quiz.opening();
		int playerCount = quiz.initPlayerCount();
		Player[] players = quiz.initPlayers(playerCount);
		int roundCount = askRoundNumber(players);

		System.out.println("Das Quiz kann nun beginnen! Die folgenden Spieler haben sich bei dem Quiz angemeldet: \n");
		showScoreBoard(players);
		initJokerForEachPlayer(players, roundCount);

		// runQuiz is the main function 
		quiz.runQuiz(players, roundCount);

		quiz.showHighscore(players);
	}
}
