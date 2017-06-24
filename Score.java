import java.io.*;
import java.util.Date;

public class Score implements Serializable, Comparable<Score> {
	public String name;
	public int score;
	public Difficulty difficulty;
	public Date date;
	
	public Score (String name, int score, Difficulty difficulty, Date date){
		this.name = name;
		this.score = score;
		this.difficulty = difficulty;
		this.date = date;
	}
	
	@Override
	public int compareTo(Score s){
		if (s.score > this.score){
			return -1;
		} else if (s.score < this.score){
			return 1;
		} else {
			return 0;
		}
		
	}
}
