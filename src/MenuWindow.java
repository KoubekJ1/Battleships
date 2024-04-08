import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A subclass of <code>JFrame</code> that serves as the first window that the player sees.
 * It allows the player to start the game or to quit.
 */
public class MenuWindow extends JFrame implements ActionListener {
    private JButton playButton;
    private JButton quitButton;
    private int answer;
    private String options[];

    /**
     * Constructs a new menu window.
     * The constructor sets up this whole window and its components, which are then added.
     * Note that it also sets this window as visible.
     */
    MenuWindow() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(2, 1));
        this.setResizable(false);
        this.setTitle("Battleships");
        options = new String[]{"1 player", "2 players"};

        LineBorder buttonBorder = new LineBorder(Color.BLACK, 1);
        Dimension buttonDimension = new Dimension(250, 80);

        playButton = new JButton("Start match");
        playButton.setBorder(buttonBorder);
        playButton.setPreferredSize(buttonDimension);
        playButton.addActionListener(this);
        this.add(playButton);

        quitButton = new JButton("Quit");
        quitButton.setBorder(buttonBorder);
        quitButton.setPreferredSize(buttonDimension);
        quitButton.addActionListener(this);
        this.add(quitButton);

        this.pack();

        this.setLocationRelativeTo(null);

        this.setVisible(true);
    }

    /**
     * Handles the button presses.
     * The play button shows a <code>JOptionPane</code> window, asking the player whether only 1 player's going to be playing or 2.
     * The quit button shuts down the program.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            if((answer = JOptionPane.showOptionDialog(null, "Select game mode", "Start match", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null)) != -1){
                this.dispose();
                GameState.setPlayerCount(answer + 1);
                new GameWindow();
            }
        }
        else if (e.getSource() == quitButton) {
            System.exit(0);
        }
    }
}
