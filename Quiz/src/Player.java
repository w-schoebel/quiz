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
	 * @return this
	 */
	public Player setScore(int score) {
		this.score = score;
		return this;
	}
	
	/**
	 * transport jokerlist from jokerclass
	 * @param questionCount
	 * @return void
	 */
	public void initJokerList(int questionCount) {
		jokerList = JokerLibrary.getJoker(questionCount);
	}
	
	/**
	 * to compare Player object to other player
	 * @param Player player
	 * @return int scoreDifference
	 */
	@Override
	public int compareTo(Player player) {
		return (int)(this.score - player.score); 
	}
}
