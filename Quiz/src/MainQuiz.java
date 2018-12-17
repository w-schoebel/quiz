import java.util.Scanner;

public class QuizAllgemein {

	private void begruessung() {
		seperatorLine();
		System.out.println("Willkommen bei [Name].");// TODO: Name einfügen
	}

	public void seperatorLine() {
		System.out.println(
				"--------------------------------------------------------------------------------------------------------");
	}

	private int initPlayerCount() {
		seperatorLine();
		System.out.println("Bitte geben Sie die Anzahl der Spieler ein, die an diesem Quiz teilnehmen -");

		int playerCount = 0;
		while (playerCount < 2 || playerCount > 6) {// damit nur Spieleranzahl zwischen 2 und 6 möglich
			System.out.println("Spieleranzahl [2-6]: \n");
			playerCount = GetIntFromConsole();
		}

		return playerCount;
	}

	private int GetIntFromConsole() {
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
		
		sc01.close();

		return value;
	}

	private String[][] initPlayer(int playerCount) {
		seperatorLine();
		
		String[][] player = new String[playerCount][playerCount]; // beinhaltet Name und Punktzahl

		for (int i = 0; i < playerCount; i++) {
			System.out.println(String.format("Bitte geben Sie den Namen für Spieler %d ein: ", i + 1));
			player[i][0] = GetStringFromConsole();
			player[i][1] = "0";
		}

		return player;
	}

	private String GetStringFromConsole() {
		Scanner sc01 = new Scanner(System.in);
		String value = sc01.next();
		
		sc01.close();

		return value;
	}

	private void StartQuiz(String[][] player) {
		seperatorLine();
		System.out.println("Das Quiz kann nun beginnen! Die folgenden Spieler haben sich für das Quiz angemeldet: \n");
		for (int i = 0; i < player.length; i++) {
			System.out.println(String.format("Spieler %d: %s | Punktzahl: %s", i + 1, player[i][0], player[i][1]));
		}
	}

	public static void main(String[] args) {
		QuizAllgemein quiz = new QuizAllgemein();
		quiz.begruessung();
		int playerCount = quiz.initPlayerCount();
		String[][] player = quiz.initPlayer(playerCount); // Spieleranzahl übergeben an initPlayer
		quiz.StartQuiz(player);
	}

	private boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value); // versucht den String in einen Int zu überführen
			return true;
		} catch (NumberFormatException e) { // wenn kein Int geparst werden kann-> Exception e
			return false;
		}
	}

}
