import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenu {
	private static final long serialVersionUID = -1812290916516866724L;
	
	public JMenuBar menuBar;
    public JMenu menu;
    public JMenuItem menuItem, menuItem2, menuItem3, menuItem4;

    
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
 
		ButtonAction action1 = new ButtonAction("EASY", KeyEvent.VK_E, Difficulty.EASY, m);
		ButtonAction action2 = new ButtonAction("MEDIUM", KeyEvent.VK_M, Difficulty.MEDIUM, m);
		ButtonAction action3 = new ButtonAction("HARD", KeyEvent.VK_H, Difficulty.HARD, m);
		
		HighscoreAction action4 = new HighscoreAction("HIGHSCORES", KeyEvent.VK_I, m);


        //a group of JMenuItems
        menuItem = new JMenuItem(action1);
		menuItem2 = new JMenuItem(action2);
		menuItem3 = new JMenuItem(action3);
		menuItem4 = new JMenuItem(action4);
		
        menu.add(menuItem);
		menu.add(menuItem2);
		menu.add(menuItem3);
		menu.add(menuItem4);
	}
}
