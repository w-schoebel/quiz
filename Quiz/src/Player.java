import java.util.List;

public class Player implements Comparable <Player>{
	
	String name;
	int score;
	private List<Joker> jokerList = null;

	public Player(String name, int score) { 
		this.name = name;
		this.score = score;		
	}
	
	public Player setScore(int score) {
		this.score = score;
		return this;
	}
	
	public void initJokerList(int questionCount)
	{
		jokerList = Joker.getJoker(questionCount);
	}
	
	@Override
	public int compareTo(Player player) {
		return (int)(this.score - player.score); // Vergleicht den Score des einen Elements mit dem übergebenen Score; (für Sorting)
// definiert das Sort über die variable Score durchgeführt wird
	}
}
