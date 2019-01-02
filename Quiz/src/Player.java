
public class Player {
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

}
