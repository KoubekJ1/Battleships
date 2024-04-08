import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * A subclass of <code>JLabel</code> meant for displaying a tile in a <code>GridPanel</code>.
 */
public class Tile extends JLabel implements MouseListener {
    private Coordinates coordinates;
    private int type;
    private boolean hovering;
    private boolean isOpponentGrid;

    /**
     * Constructs a new tile, assigns a type of either undiscovered or empty based on whether the user sets this tile as revealed or not, and sets up the look of this tile.
     * @param coordinates the coordinates of this tile
     * @param revealed whether the tile is revealed
     * @param opponentGrid whether the tile is part of an opponent <code>GridPanel</code> (whether it is shootable)
     */
    Tile(Coordinates coordinates, boolean revealed, boolean opponentGrid){
        this.coordinates = coordinates;
        if(revealed){
            type = 1;
        } else {
            type = 0;
        }
        refreshColor();
        this.isOpponentGrid = opponentGrid;
        hovering = false;
        this.setBorder(new LineBorder(Color.BLACK, 2));
        this.setOpaque(true);
        this.addMouseListener(this);
    }

    /**
     * Gets the coordinates of this tile.
     * @return the coordinates of this tile
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Gets the type of this tile.
     * 0 = Undiscovered
     * 1 = Empty
     * 2 = Ship
     * 3 = Sunk ship
     * 4 = Shot empty
     * 5 = Highlighted
     * @return the type of this tile
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the type of this tile.
     * Only values between 0 and 5 are intended.
     * 0 = Undiscovered
     * 1 = Empty
     * 2 = Ship
     * 3 = Sunk ship
     * 4 = Shot empty
     * 5 = Highlighted
     * @param type the type of tile
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets the color based on the type of tile that was passed in.
     * @param color the type of tile
     * @return the corresponding color
     */
    private Color getColor(int color){
        return switch (color) {
            case 0 -> Color.DARK_GRAY;
            case 1 -> Color.GRAY;
            case 2 -> Color.WHITE;
            case 3 -> Color.RED;
            case 4 -> Color.PINK;
            case 5 -> Color.ORANGE;
            default -> null;
        };
    }

    /**
     * Changes this tile's color back to its assigned type.
     */
    public void refreshColor(){
        this.setBackground(this.getColor(type));
    }

    /**
     * Changes this tile's color to blue.
     * Used for displaying a ship that's currently being placed and showing that its location is valid.
     */
    public void highlightValid(){
        this.setBackground(Color.BLUE.brighter());
    }

    /**
     * Changes this tile's color to red.
     * Used for displaying a ship that's currently being placed and showing that its location is invalid.
     */
    public void highlightInvalid(){
        this.setBackground(Color.RED.brighter());
    }

    /**
     * Changes this tile's color to a brighter version of its assigned color.
     * Used when the player hovers over the tile with the cursor.
     */
    private void highlight(){
        this.setBackground(this.getColor(type).brighter());
    }

    /**
     * Changes this tile's color to a darker version of its assigned color.
     * Used when the player presses the tile down.
     */
    private void pressTile(){
        this.setBackground(this.getColor(type).darker());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Darkens this tile's color to show that it's pressed down (unless the game is in the ship creating phase).
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(!GameState.isShipCreating()){
            pressTile();
        }
    }

    /**
     * Handles the mouse button presses.
     * The left mouse button either places down a ship on the grid if the game is in the ship creating phase, or it shoots at the opponent's grid.
     * The right mouse button either rotates the ship if the game is in the ship creating phase, or it highlights this tile.
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(hovering){
            if(e.getButton() == MouseEvent.BUTTON1){
                if(GameState.isShipCreating()){
                    GameState.addShip();
                } else {
                    if(isOpponentGrid){
                        if(GameState.getCurrentPlayer() == 1){
                            GameState.shootPlayer1(this);
                        } else {
                            GameState.shootPlayer2(this);
                        }
                    }
                    highlight();
                }
            } else if(e.getButton() == MouseEvent.BUTTON3) {
                if (GameState.isShipCreating()) {
                    GameState.getNewShip().toggleRotation();
                } else {
                    if(type == 0){
                        type = 5;
                    } else if(type == 5){
                        type = 0;
                    }
                    highlight();
                }
            }
        }
    }

    /**
     * Clears the tiles of the new ship and sets the new location of the new ship if the game is in the ship creating phase, or it highlights this tile.
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        hovering = true;
        if(GameState.isShipCreating()){
            GameState.getNewShip().getTiles().clear();
            GameState.addShipTiles(coordinates);
            GameState.showShip();
        } else {
            highlight();
        }
    }

    /**
     * Changes this tile's color back to its set type and clears the old ship tiles if the game is in the ship creating phase.
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
        refreshColor();
        hovering = false;
        if(GameState.isShipCreating()){
            GameState.refreshNewShipTiles();
        }
    }
}
