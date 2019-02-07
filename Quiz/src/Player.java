import java.util.List;

public class Player implements Comparable <Player>{
	
	String name;
	int score;
	List<Joker> jokerList = null;

	/**
	 * constructor
	 * @param name
	 * @param score
	 */
	public Player(String name, int score) { 
		this.name = name;
		this.score = score;		
	}
	
	/**
	 * setter for score
	 * @param score
	 * @return
	 */
	public Player setScore(int score) {
		this.score = score;
		return this;
	}
	
	/**
	 * transport jokerlist from jokerclass
	 * @param questionCount
	 */
	public void initJokerList(int questionCount) {
		jokerList = JokerLibrary.getJoker(questionCount);
	}
	
	@Override
	public int compareTo(Player player) {
		return (int)(this.score - player.score); // Vergleicht den Score des einen Elements mit dem �bergebenen Score (f�r Sorting)
		// definiert das Sort �ber die variable Score durchgef�hrt wird
	}
}
