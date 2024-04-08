import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * A subclass of <code>JFrame</code> that serves as the window where the whole game plays out.
 */
public class GameWindow extends JFrame implements ActionListener {
    private JLabel readyScreenLabel;
    private JButton readyScreenButton;

    private GridPanel gridPlayer1;
    private GridPanel gridOpponent1;
    private GridPanel gridPlayer2;
    private GridPanel gridOpponent2;

    private JPanel gamePanel;
    private JPanel readyScreenPanel;

    private GridPanel shipCreationGridPanel;
    private JPanel shipCreationPanel;
    private ShipLabel submarine;
    private ShipLabel destroyer;
    private ShipLabel cruiser;
    private ShipLabel battleship;
    private ShipLabel carrier;
    private JLabel shipCreationCurrentPlayerLabel;
    private JButton confirmButton;
    private JButton resetButton;

    private JButton endTurnButton;

    private int tileSize;
    private int gridSize;
    private int gridOffsetX;
    private int gridOffsetY;

    /**
     * Constructs a new game window and sets up its look and its components, which are then added to this window.
     * Note that it also sets this window as visible.
     */
    public GameWindow() {
        int windowX = 1280;
        int windowY = 720;

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(new Dimension(windowX, windowY));
        this.setResizable(false);
        this.addWindowListener(new GameWindowAdapter(this));

        tileSize = 20;
        gridSize = tileSize * 10;
        gridOffsetX = 100;
        gridOffsetY = 100;

        gridPlayer1 = new GridPanel( true, false);
        gridPlayer1.setBounds(gridOffsetX, gridOffsetY, gridSize, gridSize);

        gridOpponent1 = new GridPanel( false, true);
        gridOpponent1.setBounds(windowX - gridOffsetX - gridSize, gridOffsetY, gridSize, gridSize);

        gridPlayer2 = new GridPanel( true, false);
        gridPlayer2.setBounds(gridOffsetX, gridOffsetY, gridSize, gridSize);

        gridOpponent2 = new GridPanel( false, true);
        gridOpponent2.setBounds(windowX - gridOffsetX - gridSize, gridOffsetY, gridSize, gridSize);

        endTurnButton = new JButton("End turn");
        endTurnButton.setBounds(gridOffsetX, gridOffsetY + gridSize + 20, 200, 50);
        endTurnButton.addActionListener(this);

        initializeShipCreationGUIComponents();

        readyScreenLabel = new JLabel();
        readyScreenButton = new JButton("Ready");

        int startTurnLabelX = 200;
        int startTurnLabelY = 50;
        int readyButtonSizeX = 100;
        int readyButtonSizeY = 20;

        readyScreenLabel.setBounds(windowX / 2 - startTurnLabelX / 2, 100, startTurnLabelX, startTurnLabelY);
        readyScreenButton.setBounds(windowX / 2 - readyButtonSizeX / 2, 600, readyButtonSizeX, readyButtonSizeY);

        readyScreenLabel.setBorder(new LineBorder(Color.BLACK, 5));
        readyScreenLabel.setOpaque(true);
        readyScreenLabel.setHorizontalAlignment(JLabel.CENTER);
        readyScreenLabel.setFont(new Font("Helvetica", Font.BOLD, 20));

        readyScreenButton.addActionListener(this);

        readyScreenPanel = new JPanel();
        readyScreenPanel.setLayout(null);
        readyScreenPanel.add(readyScreenLabel);
        readyScreenPanel.add(readyScreenButton);

        gamePanel = new JPanel();
        gamePanel.setLayout(null);
        gamePanel.add(endTurnButton);

        GameState.initialize(this);

        shipCreationScreen();
        this.setVisible(true);
    }

    /**
     * Gets the "End turn" button that ends your turn and lets either another player or the AI play.
     * @return the "End turn" button
     */
    public JButton getEndTurnButton() {
        return endTurnButton;
    }

    /**
     * Gets Player 1's <code>GridPanel</code> which keeps track of player 1's ships and lets player 1 see his ships.
     * @return player 1's <code>GridPanel</code>
     */
    public GridPanel getGridPlayer1() {
        return gridPlayer1;
    }

    /**
     * Gets Player 1's opponent <code>GridPanel</code> which keeps track of what tiles player 1 has revealed and lets player 1 see where he can shoot.
     * @return player 1's opponent <code>GridPanel</code>
     */
    public GridPanel getGridOpponent1() {
        return gridOpponent1;
    }

    /**
     * Gets Player 2's <code>GridPanel</code> which keeps track of player 2's ships and lets player 2 see his ships.
     * @return player 2's <code>GridPanel</code>
     */
    public GridPanel getGridPlayer2() {
        return gridPlayer2;
    }

    /**
     * Gets Player 2's opponent <code>GridPanel</code> which keeps track of what tiles player 2 has revealed and lets player 2 see where he can shoot.
     * @return player 2's opponent <code>GridPanel</code>
     */
    public GridPanel getGridOpponent2() {
        return gridOpponent2;
    }

    /**
     * Gets the <code>GridPanel</code> used for creating the ships. It keeps track of where the player placed his ships, and it lets the player place ships.
     * @return the <code>GridPanel</code> that serves for creating the ships
     */
    public GridPanel getShipCreationGridPanel() {
        return shipCreationGridPanel;
    }

    /**
     * Shows the screen where the player can create his ship layout.
     */
    private void shipCreationScreen(){
        this.remove(readyScreenPanel);
        this.remove(gamePanel);
        this.add(shipCreationPanel);
        this.setTitle("Battleships (Creating ships) - Player " + GameState.getCurrentPlayer());
        this.revalidate();
        this.repaint();
    }

    /**
     * Shows the ready screen where the game informs the player(s) whose turn it is.
     */
    private void readyScreen(){
        readyScreenLabel.setText("Player " + GameState.getCurrentPlayer() + "'s turn");
        this.remove(shipCreationPanel);
        this.remove(gamePanel);
        this.add(readyScreenPanel);
        this.setTitle("Battleships (Ready) - Player " + GameState.getCurrentPlayer());
        this.revalidate();
        this.repaint();
    }

    /**
     * Starts the player's turn.
     * This method adds the current player's grids (the grid showing his ships and the grid showing his opponent's ships), and it then shows the main screen where the turn plays out.
     */
    private void startTurn(){
        endTurnButton.setEnabled(false);
        if (GameState.getCurrentPlayer() == 1){
            gamePanel.remove(gridPlayer2);
            gamePanel.remove(gridOpponent2);
            gamePanel.add(gridPlayer1);
            gamePanel.add(gridOpponent1);
        } else {
            gamePanel.remove(gridPlayer1);
            gamePanel.remove(gridOpponent1);
            gamePanel.add(gridPlayer2);
            gamePanel.add(gridOpponent2);
        }
        this.remove(shipCreationPanel);
        this.remove(readyScreenPanel);
        this.add(gamePanel);
        this.setTitle("Battleships (Shooting) - Player " + GameState.getCurrentPlayer());
        this.revalidate();
        this.repaint();
    }

    /**
     * Displays a <code>JOptionPane</code> letting the player(s) know who won.
     * It also disposes of this window and creates a new instance of the <code>MenuWindow</code> class.
     */
    public void victory(){
        JOptionPane.showMessageDialog(null, "Player " + GameState.getCurrentPlayer() + " won!", "Results", JOptionPane.PLAIN_MESSAGE);
        this.dispose();
        new MenuWindow();
    }

    /**
     * Sets up all the components used when creating the ships.
     */
    private void initializeShipCreationGUIComponents(){
        shipCreationPanel = new JPanel();
        shipCreationPanel.setLayout(null);

        initializeShipCreationLabels();

        confirmButton = new JButton("Confirm ship layout");
        confirmButton.setBounds(gridOffsetX, gridOffsetY + gridSize + 20, 200, 50);
        confirmButton.addActionListener(this);

        resetButton = new JButton("Reset ship layout");
        resetButton.setBounds(gridOffsetX, gridOffsetY + gridSize + 80, 200, 50);
        resetButton.addActionListener(this);

        shipCreationCurrentPlayerLabel = new JLabel("Player 1");
        shipCreationCurrentPlayerLabel.setBounds(gridOffsetX + gridSize + 50, gridOffsetY, gridSize, gridSize);
        shipCreationCurrentPlayerLabel.setFont(new Font("Helvetica", Font.BOLD, 20));

        shipCreationGridPanel = new GridPanel(true, false);
        shipCreationGridPanel.setBounds(gridOffsetX, gridOffsetY, gridSize, gridSize);

        shipCreationPanel.add(submarine);
        shipCreationPanel.add(destroyer);
        shipCreationPanel.add(cruiser);
        shipCreationPanel.add(battleship);
        shipCreationPanel.add(carrier);

        shipCreationPanel.add(confirmButton);
        shipCreationPanel.add(resetButton);
        shipCreationPanel.add(shipCreationCurrentPlayerLabel);
        shipCreationPanel.add(shipCreationGridPanel);
    }

    /**
     * Sets up all of the <code>ShipLabel</code> labels used for creating the ships.
     */
    private void initializeShipCreationLabels(){
        submarine = new ShipLabel(1);
        submarine.setText("Submarine (2 left)");
        submarine.setIcon(new ImageIcon("shipImages/submarine.png"));
        submarine.setBounds(1030, 25, 200, 124);
        destroyer = new ShipLabel(2);
        destroyer.setText("Destroyer (2 left)");
        destroyer.setIcon(new ImageIcon("shipImages/destroyer.png"));
        destroyer.setBounds(1030, 149, 200, 124);
        cruiser = new ShipLabel(3);
        cruiser.setText("Cruiser (1 left)");
        cruiser.setIcon(new ImageIcon("shipImages/cruiser.png"));
        cruiser.setBounds(1030, 273, 200, 124);
        battleship = new ShipLabel(4);
        battleship.setText("Battleship (1 left)");
        battleship.setIcon(new ImageIcon("shipImages/battleship.png"));
        battleship.setBounds(1030, 397, 200, 124);
        carrier = new ShipLabel(5);
        carrier.setText("Carrier (1 left)");
        carrier.setIcon(new ImageIcon("shipImages/carrier.png"));
        carrier.setBounds(1030, 521, 200, 124);
    }

    /**
     * Resets the labels used for creating the ships, restoring their text and making them enabled again if they've been disabled.
     */
    public void resetShipCreationLabels(){
        submarine.reset();
        submarine.setText("Submarine (2 left)");
        destroyer.reset();
        destroyer.setText("Destroyer (2 left)");
        cruiser.reset();
        cruiser.setText("Cruiser (1 left)");
        battleship.reset();
        battleship.setText("Battleship (1 left)");
        carrier.reset();
        carrier.setText("Carrier (1 left)");
    }

    /**
     * Handles the button presses.
     * The "Ready" button on the ready screen starts the player's turn.
     * The "Confirm ship placement" button on the ship creation screen saves the placement of the player's ships and either lets the other player/AI set up their ships, or it starts the game if the other player has already set up his ships.
     * The "Reset ship placement" button resets the labels showing the ships the player can place and the <code>GridPanel</code> where the player sets up his ship layout.
     * The "End turn" button ends the player's turn and shows the ready screen, letting the other player/the AI play.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == readyScreenButton) {
            startTurn();
        } else if (e.getSource() == confirmButton) {
            if(GameState.getCreatedShips().size() >= 7){
                GameState.confirmShipPlacement();
                shipCreationGridPanel.assignValue(1);
                if(GameState.getPlayerCount() == 1){
                    GameState.AIGenerateShips();
                }
                shipCreationCurrentPlayerLabel.setText("Player " + GameState.getCurrentPlayer());
                if(!GameState.isShipCreating()){
                    readyScreen();
                }
            } else {
                JOptionPane.showMessageDialog(null, "You haven't placed all of your ships yet!", "Confirm ship layout", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == resetButton) {
            if(JOptionPane.showConfirmDialog(null, "Are you sure you want to reset your ship layout?", "Reset ship layout", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0){
                GameState.initializeShipCreation();
                resetShipCreationLabels();
                shipCreationGridPanel.assignValue(1);
            }
        } else if (e.getSource() == endTurnButton) {
            GameState.newTurn();
            if(GameState.getPlayerCount() == 1){
                GameState.AITurn();
            }
            readyScreen();
        }
    }
}