import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

class HighscoreAction extends AbstractAction {
  private static final long serialVersionUID = 5514054285773002639L;
  Main m;

  public HighscoreAction(String text, Integer mnemonic, Main _m) {
    super(text);
    putValue(MNEMONIC_KEY, mnemonic);
    m = _m;
  }

  public void actionPerformed(ActionEvent e) {
    m.showHighscore();
  }
}
