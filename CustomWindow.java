import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.JOptionPane;

public class CustomWindow extends JDialog {
	private static final long serialVersionUID = -2459845486545646721L;
	private ScoreManager scoreManager;

	public CustomWindow(Frame owner, boolean modal, Main m){
		super(owner, modal);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);
        JTextField minesField = new JTextField(5);

        JPanel optionsPanel = new JPanel();
        optionsPanel.add(new JLabel("Cells (X):"));
        optionsPanel.add(xField);
        optionsPanel.add(Box.createHorizontalStrut(15)); //A spacer
        optionsPanel.add(new JLabel("Cells (Y):"));
        optionsPanel.add(yField);
        optionsPanel.add(Box.createHorizontalStrut(15)); //A spacer
        optionsPanel.add(new JLabel("Mines:"));
        optionsPanel.add(minesField);

        int result = JOptionPane.showConfirmDialog(null, optionsPanel, "Please Enter X, Y and Mines Values", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int currentX, currentY, currentMines;
            currentX = m.xCells;
            currentY = m.yCells;
            currentMines = m.mines;

            try {
                m.xCells = Integer.parseInt(xField.getText());
                m.yCells = Integer.parseInt(yField.getText());
                m.mines = Integer.parseInt(minesField.getText());

                Board.difficulty = Difficulty.CUSTOM;

                m.restartGame();
            } catch (Exception e) {
                m.xCells = currentX;
                m.yCells = currentY;
                m.mines = currentMines;
            }
        }

        //pack();
        //setVisible(true);
	}
}
