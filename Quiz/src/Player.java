
public class Player implements Comparable <Player>{
	String name;
	int score;
	// Joker joker = new Joker; 

	public Player(String name, int score) { 
		this.name = name;
		this.score = score;

	}
	public static void main(String[] args) {
	}

	public Player setScore(int score) {
		this.score = score;
		return this;
	}
	@Override
	public int compareTo(Player player) {
		return (int)(this.score - player.score); // Vergleicht den Score des einen Elements mit dem �bergebenen Score; (f�r Sorting)
// definiert das Sort �ber die variable Score durchgef�hrt wird
	}
}
