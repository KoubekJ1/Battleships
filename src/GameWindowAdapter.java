import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A subclass of the <code>WindowAdapter</code> class, made for the purpose of making sure that the player truly wants to leave the match, in case he clicked the exit button on accident.
 */
public class GameWindowAdapter extends WindowAdapter {

    private JFrame frame;

    /**
     * Constructs a new game window adapter and assigns the passed in <code>JFrame</code> as the reference to the frame the window is working with.
     * @param frame a reference to the <code>JFrame</code> object that this instance works with
     */
    GameWindowAdapter(JFrame frame){
        this.frame = frame;
    }

    /**
     * Shows the player a <code>JOptionPane</code> letting him choose if he truly wants to leave the match.
     * @param e the event to be processed
     */
    @Override
    public void windowClosing(WindowEvent e) {
        if(JOptionPane.showOptionDialog(null, "Are you sure you want to leave the match?", "Leave match", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null) == 0) {
            super.windowClosing(e);
            frame.dispose();
            new MenuWindow();
        }
    }
}
