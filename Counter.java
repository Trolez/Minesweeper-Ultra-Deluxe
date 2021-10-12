import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Counter extends ScheduledThreadPoolExecutor {

  private Main m;
  private Task task;
  private ScheduledFuture<?> t;

  private boolean isRunning;

  public static int count;

  public Counter(Main main) {
    super(1);
    m = main;
    count = 0;
    task = new Task(m);
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
  private Main m;

  public Task(Main main) {
    m = main;
  }

  public void run() {
    Counter.count++;
    m.repaint();
  }
}
