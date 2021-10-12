import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

class CustomAction extends AbstractAction {
  private static final long serialVersionUID = 7432187654316840186L;
  Main m;

  public CustomAction(String text, Integer mnemonic, Main _m) {
    super(text);
    putValue(MNEMONIC_KEY, mnemonic);
    m = _m;
  }

  public void actionPerformed(ActionEvent e) {
    m.showCustomWindow();
  }
}