import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class HighscoreTable extends JDialog {
	private static final long serialVersionUID = -3888326792973021372L;
	private ScoreManager scoreManager;
	
	public HighscoreTable(Frame owner, boolean modal){
		super(owner, modal);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        scoreManager = new ScoreManager();
		
		String[] columnNamesEasy = {
			"# EASY",
			"Name",
			"Time",
			"Date"
        };
		
		String[] columnNamesMedium = {
			"# MEDIUM",
			"Name",
			"Time",
			"Date"
        };
		
		String[] columnNamesHard = {
			"# HARD",
			"Name",
			"Time",
			"Date"
        };
		
		Object[][] dataEasy = scoreManager.getTableData(Difficulty.EASY);
		Object[][] dataMedium = scoreManager.getTableData(Difficulty.MEDIUM);
		Object[][] dataHard = scoreManager.getTableData(Difficulty.HARD);
		
		final JTable tableEasy = new JTable(dataEasy, columnNamesEasy);
        tableEasy.setFillsViewportHeight(true);
        
        final JTable tableMedium = new JTable(dataMedium, columnNamesMedium);
        tableMedium.setFillsViewportHeight(true);
        
        final JTable tableHard = new JTable(dataHard, columnNamesHard);
        tableMedium.setFillsViewportHeight(true);

        JScrollPane tableContainerEasy = new JScrollPane(tableEasy);
        JScrollPane tableContainerMedium = new JScrollPane(tableMedium);
        JScrollPane tableContainerHard = new JScrollPane(tableHard);
        
        tableContainerEasy.setPreferredSize(new Dimension(600, 188));
        tableContainerMedium.setPreferredSize(new Dimension(600, 188));
        tableContainerHard.setPreferredSize(new Dimension(600, 188));

        add(tableContainerEasy, BorderLayout.NORTH);
        add(tableContainerMedium, BorderLayout.CENTER);
        add(tableContainerHard, BorderLayout.SOUTH);

        pack();
        setVisible(true);
	}
}
