import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A subclass of <code>JLabel</code> made for the purpose of letting the player select the ship type that he currently wants to place.
 */
public class ShipLabel extends JLabel implements MouseListener {
    private boolean hovering;
    private int shipSize;
    private boolean disabled;
    private boolean selected;

    /**
     * Constructs a new ship label and sets up the look of this label.
     * @param shipSize the size of the ship this label represents
     */
    ShipLabel(int shipSize){
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(new LineBorder(Color.BLACK, 5));
        this.setOpaque(true);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
        this.setHorizontalTextPosition(JLabel.RIGHT);
        this.setVerticalTextPosition(JLabel.CENTER);
        this.shipSize = shipSize;
        hovering = false;
        disabled = false;
        selected = false;

        this.addMouseListener(this);
    }

    /**
     * Sets this <code>ShipLabel</code> as the active <code>ShipLabel</code> currently used for creating the ships.
     * Note that this method handles the look of the label as well as everything related to the ship type in <code>GameState</code>.
     */
    private void setShipCreatingShipType(){
        if(GameState.getCurrentShipTypeLabelReference() != null && GameState.getCurrentShipTypeLabelReference().disabled != true){
            GameState.getCurrentShipTypeLabelReference().setBackground(Color.LIGHT_GRAY);
            GameState.getCurrentShipTypeLabelReference().selected = false;
        }
        this.setBackground(Color.DARK_GRAY);
        selected = true;
        GameState.getNewShip().setSize(shipSize);
        GameState.setCurrentShipTypeLabelReference(this);
    }

    /**
     * Makes this label not selectable by turning it red and preventing the user from being able to select it.
     */
    public void disableSelect(){
        disabled = true;
        selected = false;
        this.setBackground(Color.RED);
    }

    /**
     * Resets this label by changing the background back to light gray and letting the user select it again.
     */
    public void reset(){
        disabled = false;
        selected = false;
        this.setBackground(Color.LIGHT_GRAY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    /**
     * Sets the background as a darker light gray to show that this label is pressed down.
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (!disabled){
            this.setBackground(Color.LIGHT_GRAY.darker());
        }
    }

    /**
     * Sets this label as the selected ship label when the player clicks it.
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(!disabled && !selected){
            if(hovering){
                setShipCreatingShipType();
            } else {
                this.setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    /**
     * Highlights this label to give the player feedback when they hover over this label.
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        hovering = true;
        if(!disabled && !selected){
            this.setBackground(Color.LIGHT_GRAY.brighter());
        }
    }

    /**
     * Resets the background back to light gray.
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
        hovering = false;
        if(!disabled && !selected){
            this.setBackground(Color.LIGHT_GRAY);
        }
    }
}
