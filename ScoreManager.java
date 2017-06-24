import java.util.*;
import java.io.*;

public class ScoreManager {
    private ArrayList<Score> scores;

    private static final String HIGHSCORE_FILE = "highscores.dat";

    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;

    public ScoreManager() {
        scores = new ArrayList<Score>();
    }
    
    public ArrayList<Score> getScores(Difficulty difficulty) {
        loadScoreFile();
        sort();
        
        ArrayList<Score> newList = new ArrayList<Score>();
        for (Score s : scores){
        	if (s.difficulty == difficulty){
        		newList.add(s);
        	}
        }
        
        return newList;
    }
    
    private void sort() {
        Collections.sort(scores);
    }
    
    public void addScore(String name, int score, Difficulty difficulty, Date date) {
        loadScoreFile();
        scores.add(new Score(name, score, difficulty, date));
        updateScoreFile();
    }
    
    @SuppressWarnings("unchecked")
	public void loadScoreFile() {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
            scores = (ArrayList<Score>) inputStream.readObject();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }
    
    public void updateScoreFile() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
            Collections.sort(scores);
            
            ArrayList<Score> newList = new ArrayList<Score>();
            for(Difficulty d : Difficulty.values()){
            	int i = 0;
            	for (Score s : scores){
            		if (s.difficulty == d){
            			newList.add(s);
            			i++;
            			if (i >= 10){
            				break;
            			}
            		}
            	}
            }
            
            scores = newList;
            outputStream.writeObject(scores);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
    }
    
    public String[][] getTableData(Difficulty d){
    	String[][] tableData = new String[getScores(d).size()][4];
    	int i = 0;
    	for(Score s : getScores(d)){
    		if (s.difficulty == d){
	    		String minutes = String.valueOf((int)Math.floor(s.score/60));
	    		String seconds = String.valueOf(s.score % 60);
	    		if (minutes.length() == 1){
	    			minutes = "0" + minutes;
	    		}
	    		if (seconds.length() == 1){
	    			seconds = "0" + seconds;
	    		}
	    		
	    		tableData[i][0] = ""+(i+1);
	    		tableData[i][1] = s.name;
	    		tableData[i][2] = ""+minutes+":"+seconds;
	    		tableData[i][3] = s.date.toString();
	    		i++;
    		}
    	}
    	return tableData;
    }
    
    public int GetLowestScore(Difficulty difficulty){
    	loadScoreFile();
    	sort();
    	int lowestScore = Integer.MIN_VALUE;
    	for (Score s : scores){
    		if (s.difficulty == difficulty && s.score > lowestScore){
    			lowestScore = s.score;
    		}
    	}
    	
    	return lowestScore;
    }
}
