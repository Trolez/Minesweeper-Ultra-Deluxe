import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Counter extends ScheduledThreadPoolExecutor {
  private Task task;
  private ScheduledFuture<?> t;

  private boolean isRunning;

  public static int count;

  public Counter(GUI gui) {
    super(1);
    count = 0;
    task = new Task(gui);
    isRunning = false;
  }

  public void start() {
    t = scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS);
    isRunning = true;
  }

  public void stop() {
    try {
      t.cancel(false);
    } catch (NullPointerException e) {
    }
    isRunning = false;
  }

  public boolean isRunning() {
    return isRunning;
  }
}

class Task implements Runnable {
  private GUI gui;

  public Task(GUI gui) {
    this.gui = gui;
  }

  public void run() {
    Counter.count++;
    gui.repaint();
  }
}
