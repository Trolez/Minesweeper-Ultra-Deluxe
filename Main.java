import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Date;

public class Main extends JFrame implements ComponentListener, ItemListener {
  static final long serialVersionUID = 1L;

  // Default board values
  public int xCells = 30;
  public int yCells = 16;
  public int mines = 99;

  // The dimensions of the board
  public double width, height;

  // Default values for cell sizes
  public static double cellWidth = 30, cellHeight = 30;

  // Highscore manager
  private ScoreManager scoreManager = new ScoreManager();

  private Board board;

  private boolean enterFlag = false;
  KeyAdapter keyAdapter = new KeyAdapter() {
    public void keyPressed(KeyEvent arg0) {
      // Not needed

    }

    public void keyReleased(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_SPACE:
          restartGame();
          break;
        case KeyEvent.VK_ENTER:
          if (!enterFlag)
            resetGame();
          else
            enterFlag = false;
          break;
        case KeyEvent.VK_ESCAPE:
          System.exit(0);
        case KeyEvent.VK_F:
          setExtendedState(JFrame.MAXIMIZED_BOTH);
          break;
      }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
      // TODO Auto-generated method stub

    }
  };

  public Main(String[] args) {
    // Change the board values if args are set
    try {
      if (args.length > 0) {
        xCells = Integer.parseInt(args[0]);
      }
      if (args.length > 1) {
        yCells = Integer.parseInt(args[1]);
      }
      if (args.length > 2) {
        mines = Integer.parseInt(args[2]);
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid number format");
    }

    // Make sure the amount of mines doesn't mess things up
    verifyCells();

    System.setProperty("awt.useSystemAAFontSettings", "on");
    System.setProperty("swing.aatext", "true");
    setTitle("Minesweeper Ultra Deluxe (v1.1)");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // Set custom window title
    if (args.length > 3) {
      String newTitle = "";
      for (int i = 3; i < args.length; i++) {
        if (i > 3) {
          newTitle += " ";
        }
        newTitle += args[i];
      }
      setTitle(newTitle);
    }

    setVisible(true);
    setLayout(new BorderLayout());

    Menu menu = new Menu(this);

    setJMenuBar(menu.menuBar);

    try {
      // Set cross-platform Java L&F (also called "Metal")
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (UnsupportedLookAndFeelException e) {
      // handle exception
    } catch (ClassNotFoundException e) {
      // handle exception
    } catch (InstantiationException e) {
      // handle exception
    } catch (IllegalAccessException e) {
      // handle exception
    }

    board = new Board(this);
    board.setPreferredSize(new Dimension((int) width, (int) height + 75)); // +75 for the gui
    setMinimumSize(new Dimension((int) width, (int) height + 75));
    board.placeMines();

    this.addKeyListener(keyAdapter);
    this.add(board, BorderLayout.CENTER);
    board.addComponentListener(this);
    pack();
    this.repaint();
  }

  public static void main(String[] args) {
    new Main(args);
  }

  public void restartGame() {
    verifyCells();
    board.resetCounter();
    this.remove(board);
    board = new Board(this);
    Board.dead = false;
    Board.won = false;
    Board.firstClick = true;
    board.setPreferredSize(new Dimension((int) width, (int) height + 75));
    setMinimumSize(new Dimension((int) width, (int) height + 75));
    board.placeMines();
    this.add(board, BorderLayout.CENTER);
    board.addComponentListener(this);
    pack();
    this.repaint();
    board.paint(getGraphics());
  }

  public void resetGame() {
    // Resets the game with the mines in the same location
    if (Board.firstClick) {
      return;
    }

    Board.dead = false;
    Board.won = false;
    board.remainingMines = mines;
    board.remainingCells = xCells * yCells - mines;
    board.resetCounter();

    // Reset the state of the cells
    for (int i = 0; i < xCells; i++) {
      for (int j = 0; j < yCells; j++) {
        board.cells[i][j].processed = false;
        board.cells[i][j].marked = false;
        board.cells[i][j].enqueued = false;
      }
    }
    this.repaint();
  }

  // Used to save the current screen in a buffered image to be blurred
  public BufferedImage getScreenShot(Component component) {
    BufferedImage image = new BufferedImage((int) width, (int) height + 75, BufferedImage.TYPE_INT_RGB);
    component.paint(image.getGraphics());
    return image;
  }

  public void ShowHighscoreDialog() {
    enterFlag = true;

    // Disable highscore for custom difficulties
    if (Board.difficulty == Difficulty.CUSTOM) {
      return;
    }

    // Only show highscore prompt if the score qualifies for the list
    if (Counter.count < scoreManager.GetLowestScore(Board.difficulty)
        || scoreManager.getScores(Board.difficulty).size() < 10) {
      Date date = new Date();
      String scoreName = (String) JOptionPane.showInputDialog(this, "Please enter your name for the highscore",
          "You made the highscore!", JOptionPane.PLAIN_MESSAGE, null, null, System.getProperty("user.name"));
      if (scoreName != null && scoreName != "") {
        scoreManager.addScore(scoreName, Counter.count, Board.difficulty, date);
      }
    }
  }

  public void showCustomWindow() {
    new CustomWindow(this, true, this);
  }

  public void showHighscore() {
    new HighscoreTable(this, true);
  }

  @Override
  public void componentHidden(ComponentEvent arg0) {
    // TODO Auto-generated method stub
  }

  @Override
  public void componentMoved(ComponentEvent arg0) {
    // TODO Auto-generated method stub
  }

  public void componentResized(ComponentEvent e) {
    width = e.getComponent().getWidth();
    height = e.getComponent().getHeight() - 75;
    cellWidth = width / xCells;
    cellHeight = height / yCells;

    // Redo the gradient for all cells
    for (int i = 0; i < xCells; i++) {
      for (int j = 0; j < yCells; j++) {
        board.cells[i][j].resize();
      }
    }

    // Generate a new game over screen
    if (Board.dead || Board.won) {
      board.img = null;
      board.img = getScreenShot(board);
      repaint();
    }

    board.setPreferredSize(new Dimension((int) width, (int) height + 75));
    pack();
  }

  private void verifyCells() {
    if (xCells < 9) {
      xCells = 9;
    }

    if (yCells < 9) {
      yCells = 9;
    }

    if (mines > xCells * yCells - 20) {
      mines = xCells * yCells - 20;
    }
    if (mines < 0) {
      mines = 0;
    }

    width = (int) (xCells * cellWidth);
    height = (int) (yCells * cellHeight);
  }

  @Override
  public void componentShown(ComponentEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void itemStateChanged(ItemEvent arg0) {
    // TODO Auto-generated method stub

  }
}
