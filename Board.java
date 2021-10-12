import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.Color;

public class Board extends JPanel implements MouseListener, MouseMotionListener {
  static final long serialVersionUID = 1L;

  private GUI gui;
  private Main main;
  private Cell previous = null;
  public Cell[][] cells;

  public BufferedImage img;
  private BufferedImageOp blur;
  private float[] blurKernel = { 1f / 273, 4f / 273, 7f / 273, 4f / 273, 1f / 273, 4f / 273, 16f / 273, 26f / 273,
      16f / 273, 4f / 273, 7f / 273, 26f / 273, 41f / 273, 26f / 273, 7f / 273, 4f / 273, 16f / 273, 26f / 273,
      16f / 273, 4f / 273, 1f / 273, 4f / 273, 7f / 273, 4f / 273, 1f / 273 };

  public static int remainingMines;
  public int remainingCells;

  public static boolean firstClick = true;
  public static boolean dead = false;
  public static boolean won = false;

  // Difficulty variable
  public static Difficulty difficulty = Difficulty.HARD;

  public Board(Main _main, GUI gui) {
    main = _main;

    this.gui = gui;

    remainingMines = main.mines;
    remainingCells = main.xCells * main.yCells - main.mines;

    cells = new Cell[main.xCells][main.yCells];

    previous = null;

    // The cells are initialized
    for (int i = 0; i < main.xCells; i++) {
      for (int j = 0; j < main.yCells; j++) {
        cells[i][j] = new Cell(i, j);
      }
    }

    addMouseListener(this);
    addMouseMotionListener(this);
  }

  public void resetCounter() {
    Counter.count = 0;
    gui.StopCounter();
  }

  public void initNeighbors() {
    for (int i = 0; i < main.xCells; i++) {
      for (int j = 0; j < main.yCells; j++) {
        // Top neighbor
        if (j > 0) {
          cells[i][j].neighbors[0] = cells[i][j - 1];
          if (cells[i][j - 1].mine)
            cells[i][j].degree++;
        }

        // Top-right neighbor
        if (j > 0 && i < main.xCells - 1) {
          cells[i][j].neighbors[1] = cells[i + 1][j - 1];
          if (cells[i + 1][j - 1].mine)
            cells[i][j].degree++;
        }

        // Right neighbor
        if (i < main.xCells - 1) {
          cells[i][j].neighbors[2] = cells[i + 1][j];
          if (cells[i + 1][j].mine)
            cells[i][j].degree++;
        }

        // Bottom-right neighbor
        if (j < main.yCells - 1 && i < main.xCells - 1) {
          cells[i][j].neighbors[3] = cells[i + 1][j + 1];
          if (cells[i + 1][j + 1].mine)
            cells[i][j].degree++;
        }

        // Bottom neighbor
        if (j < main.yCells - 1) {
          cells[i][j].neighbors[4] = cells[i][j + 1];
          if (cells[i][j + 1].mine)
            cells[i][j].degree++;
        }

        // Bottom-left neighbor
        if (j < main.yCells - 1 && i > 0) {
          cells[i][j].neighbors[5] = cells[i - 1][j + 1];
          if (cells[i - 1][j + 1].mine)
            cells[i][j].degree++;
        }

        // Left neighbor
        if (i > 0) {
          cells[i][j].neighbors[6] = cells[i - 1][j];
          if (cells[i - 1][j].mine)
            cells[i][j].degree++;
        }

        // Top-left neighbor
        if (j > 0 && i > 0) {
          cells[i][j].neighbors[7] = cells[i - 1][j - 1];
          if (cells[i - 1][j - 1].mine)
            cells[i][j].degree++;
        }
      }
    }
  }

  public void placeMines() {
    Random random = new Random();

    // Create a one-dimensional array with pointers to board cells
    ArrayList<Cell> list = new ArrayList<Cell>();
    for (int i = 0; i < main.xCells; i++) {
      for (int j = 0; j < main.yCells; j++) {
        list.add(cells[i][j]);
      }
    }

    // Shuffle the list
    for (int i = list.size() - 1; i > 0; i--) {
      int j = random.nextInt(i + 1);
      Cell tmp = list.get(i);
      list.set(i, list.get(j));
      list.set(j, tmp);
    }

    // Place mines in the first x cells
    for (int i = 0; i < main.mines; i++) {
      list.get(i).mine = true;
    }
  }

  public void traverse(Cell cell) {
    ArrayDeque<Cell> cells = new ArrayDeque<Cell>(main.xCells * main.yCells);
    cells.add(cell);
    cell.enqueued = true;

    Cell c = null;
    while (!cells.isEmpty()) {
      c = cells.poll();

      // Don't process if marked
      if (c.marked) {
        continue;
      }

      if (!c.processed && !c.mine) {
        remainingCells--;
        if (remainingCells == 0) {
          gui.StopCounter();
          won = true;
          img = null;
          img = main.getScreenShot(this);
          blur = new ConvolveOp(new Kernel(5, 5, blurKernel));
          main.ShowHighscoreDialog();
        }
        c.processed = true;
      }

      if (!c.mine) {
        // Also process neighbors if degree is 0
        if (c.degree == 0) {
          // Operate on all neighbors
          for (int i = 0; i < 8; i += 1) {
            if (c.neighbors[i] == null) {
              continue;
            }

            if (!c.neighbors[i].processed && !c.neighbors[i].enqueued) {
              cells.add(c.neighbors[i]);
              c.neighbors[i].enqueued = true;
            }
          }
        }

        // try {
        // //Sleep and repaint to make animated effect
        // Thread.sleep(1);
        // c.draw((Graphics2D)this.getGraphics());
        // main.repaint(c.posX, c.posY, c.displayWidth, c.displayHeight);
        // } catch (InterruptedException e) {}
      } else {
        // Player has hit a mine
        gui.StopCounter();
        dead = true;
        img = null;
        img = main.getScreenShot(this);
        blur = new ConvolveOp(new Kernel(5, 5, blurKernel));
      }
    }

    main.repaint();
  }

  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Paint each of the cells
    for (int i = 0; i < main.xCells; i++) {
      for (int j = 0; j < main.yCells; j++) {
        cells[i][j].draw(g2d);
      }
    }

    // Draw a win screen if game won
    if (won || dead) {
      g2d.drawImage(img, blur, 0, 0);

      g2d.setColor(new Color(0, 0, 0, 80));
      g2d.fillRect(0, 0, (int) main.width, (int) main.height + 75);
      g2d.setColor(new Color(255, 255, 255));
      int baseline = (((int) main.height) / 2);
      g2d.setFont(new Font("default", Font.BOLD, 30));
      if (won)
        g2d.drawString("YOU WON!", 20, baseline);
      if (dead)
        g2d.drawString("YOU LOST!", 20, baseline);
      g2d.setFont(new Font("default", Font.BOLD, 15));
      g2d.drawString("Press 'space' to start a new game", 20, baseline + 25);
      g2d.drawString("Press 'enter' to replay this board", 20, baseline + 45);
    }
  }

  public void mouseDragged(MouseEvent arg0) {
    // Not needed
  }

  public void mouseMoved(MouseEvent e) {
    if (dead || won) {
      return;
    }

    int x = (int) (e.getX() / Main.cellWidth);
    int y = (int) (e.getY() / Main.cellHeight);

    if (x > main.xCells - 1) {
      x = main.xCells - 1;
    }

    if (y > main.yCells - 1) {
      y = main.yCells - 1;
    }

    Cell c = cells[x][y];
    c.highlighted = true;
    if (previous != null && previous != c) {
      previous.highlighted = false;
      previous.draw((Graphics2D) this.getGraphics());
      main.repaint(previous.posX, previous.posY, previous.displayWidth, previous.displayHeight);
    }

    if (previous == null) {
      // Fix for weird rendering issue
      main.repaint();
    }

    previous = c;

    c.draw((Graphics2D) this.getGraphics());
    main.repaint(c.posX, c.posY, c.displayWidth, c.displayHeight);
  }

  public void mouseClicked(MouseEvent e) {
    // Not needed
  }

  public void mouseEntered(MouseEvent arg0) {
    // Not needed
  }

  public void mouseExited(MouseEvent arg0) {
    // Not needed
  }

  public void mousePressed(MouseEvent e) {
    if (dead) {
      return;
    }

    int x = (int) (e.getX() / Main.cellWidth);
    int y = (int) (e.getY() / Main.cellHeight);

    boolean clickedOutsideBoard = x > main.xCells - 1 || y > main.yCells - 1;

    if (clickedOutsideBoard) {
      return;
    }

    Cell c = cells[x][y];

    if (SwingUtilities.isRightMouseButton(e)) {
      if (c.processed) {
        return;
      }

      if (c.marked) {
        c.marked = false;
        remainingMines++;
      } else {
        c.marked = true;
        remainingMines--;
      }

      gui.repaint();

      c.draw((Graphics2D) this.getGraphics());
      main.repaint(c.posX, c.posY, c.displayWidth, c.displayHeight);
    } else {
      if (c.processed) {
        if (c.degree > 0) {
          // Count neighbors that has been marked
          int count = 0;
          for (Cell neighbor : c.neighbors) {
            if (neighbor != null && neighbor.marked) {
              count++;
            }
          }

          // Process the neighbors if the correct amount has been marked
          if (count == c.degree) {
            for (Cell neighbor : c.neighbors) {
              if (neighbor != null) {
                traverse(neighbor);
              }
            }
          }
        }
      } else {
        if (firstClick) {
          Random random = new Random();
          // First click. Move nearby mines
          int startX = x - 2;
          int startY = y - 2;
          int endX = x + 2;
          int endY = y + 2;
          if (startX < 0) {
            startX = 0;
          }
          if (startY < 0) {
            startY = 0;
          }
          if (endX > main.xCells) {
            endX = main.xCells;
          }
          if (endY > main.yCells) {
            endY = main.yCells;
          }

          int newX, newY;
          for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
              if (cells[i][j].mine) {
                // There is a mine within the safe zone. Move it
                newX = random.nextInt(main.xCells);
                newY = random.nextInt(main.yCells);
                while (cells[newX][newY].mine || (Math.abs(newX - x) < 3 && Math.abs(newY - y) < 3)) {
                  newX = random.nextInt(main.xCells);
                  newY = random.nextInt(main.yCells);
                }

                // Do the moving
                cells[i][j].mine = false;
                cells[newX][newY].mine = true;
              }
            }
          }
          initNeighbors();
          firstClick = false;

          gui.StartCounterIfNotRunning();
        }

        // If the thread is not started, start it
        gui.StartCounterIfNotRunning();

        traverse(c);
      }
    }
  }

  public void mouseReleased(MouseEvent arg0) {
    // Not needed
  }
}
