import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

class ButtonAction extends AbstractAction {
	Main m;
	private Difficulty d;

	private static int[][] difficulty_values = new int[][]{
    	{10, 10, 10},
    	{16, 16, 40},
    	{30, 16, 99}
    };

    public ButtonAction(String text, Integer mnemonic, Difficulty index, Main _m) {
		super(text);
        putValue(MNEMONIC_KEY, mnemonic);	
		m = _m;
		d = index;
    }

    public void actionPerformed(ActionEvent e) {
		m.xCells = difficulty_values[d.ordinal()][0];
		m.yCells = difficulty_values[d.ordinal()][1];
		m.mines = difficulty_values[d.ordinal()][2];
		
		m.width = (int) (m.xCells * m.cellWidth);
		m.height = (int) (m.yCells * m.cellHeight);
		
		Board.difficulty = d;
		m.restartGame();
    }
}
