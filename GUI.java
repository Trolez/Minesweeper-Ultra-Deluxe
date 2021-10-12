import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GUI extends JPanel {
  private Main main;
  private Counter counter;

  private Color bottomBackgroundColor1 = new Color(90, 90, 90);
  private Color bottomBackgroundColor2 = new Color(110, 110, 110);
  private Color numberBackgroundColor = new Color(220, 220, 220);

  public GUI(Main _main) {
    main = _main;
    counter = new Counter(this);
  }

  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    DrawBottomBox(g2d);
    DrawNumberBoxes(g2d);
    DrawNumberBoxBorders(g2d);
    DrawNumbers(g2d);
  }

  private void DrawBottomBox(Graphics2D g2d) {
    GradientPaint gradient = new GradientPaint(0, (int) main.height, bottomBackgroundColor1, 0, (int) main.height + 50,
        bottomBackgroundColor2);
    g2d.setPaint(gradient);
    g2d.fillRect(0, 0, (int) main.width, 75);
  }

  private void DrawNumberBoxes(Graphics2D g2d) {
    g2d.setColor(numberBackgroundColor);
    g2d.fillRect((int) main.width - 112, 12, 100, 51);
    g2d.fillRect(12, (int) 12, 100, 51);
  }

  private void DrawNumberBoxBorders(Graphics2D g2d) {
    g2d.setStroke(new BasicStroke(3));
    g2d.setColor(new Color(50, 50, 50));
    g2d.drawRect((int) main.width - 112, (int) 12, 100, 51);
    g2d.drawRect(12, (int) 12, 100, 51);
  }

  private void DrawNumbers(Graphics2D g2d) {
    g2d.setColor(new Color(20, 20, 20));
    g2d.setFont(new Font("default", Font.BOLD, 20));
    g2d.drawString("" + Board.remainingMines, (int) main.width - 102, (int) 45);
    g2d.drawString("" + Counter.count, 22, (int) 45);
  }

  public void StartCounterIfNotRunning() {
    if (!counter.isRunning()) {
      counter.start();
    }
  }

  public void StopCounter() {
    counter.stop();
  }
}
