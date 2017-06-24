import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenu {
	private static final long serialVersionUID = -1812290916516866724L;

	public JMenuBar menuBar;
    public JMenu menu;
    public JMenuItem menuItemEasy, menuItemMedium, menuItemHard, menuItemCustom, menuItemHighscore;


    private Main m;

	public Menu(Main _m){
		//Reference to main object
		m = _m;

		//Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Options");
        menu.setMnemonic(KeyEvent.VK_O);
        menuBar.add(menu);

		ButtonAction actionEasy = new ButtonAction("EASY", KeyEvent.VK_E, Difficulty.EASY, m);
		ButtonAction actionMedium = new ButtonAction("MEDIUM", KeyEvent.VK_M, Difficulty.MEDIUM, m);
		ButtonAction actionHard = new ButtonAction("HARD", KeyEvent.VK_H, Difficulty.HARD, m);

		CustomAction actionCustom = new CustomAction("CUSTOM", KeyEvent.VK_C, m);

		HighscoreAction actionHighscore = new HighscoreAction("HIGHSCORES", KeyEvent.VK_I, m);


        //a group of JMenuItems
        menuItemEasy = new JMenuItem(actionEasy);
		menuItemMedium = new JMenuItem(actionMedium);
		menuItemHard = new JMenuItem(actionHard);
		menuItemCustom = new JMenuItem(actionCustom);
		menuItemHighscore = new JMenuItem(actionHighscore);

        menu.add(menuItemEasy);
		menu.add(menuItemMedium);
		menu.add(menuItemHard);
		menu.add(menuItemCustom);
		menu.add(menuItemHighscore);
	}
}
