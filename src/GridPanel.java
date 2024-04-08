import javax.swing.*;
import java.awt.*;

/**
 * A subclass of <code>JPanel</code> made for displaying and keeping track of a set of ships.
 */
public class GridPanel extends JPanel {
    private Tile[][] tiles;

    /**
     * Constructs a new grid panel.
     * It also defines the <code>tiles</code> array and each tile in the array.
     * @param canSee whether the tiles in this panel are revealed
     * @param isOpponentGrid whether this grid displays tiles belonging to the opponent, therefore whether the user is able to shoot them
     */
    GridPanel(boolean canSee, boolean isOpponentGrid){
        this.setLayout(new GridLayout(10, 10));
        tiles = new Tile[10][10];
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                tiles[j][i] = new Tile(new Coordinates(j, i), canSee, isOpponentGrid);
                this.add(tiles[j][i]);
            }
        }
    }

    /**
     * Gets the array of tiles of this grid panel
     * @return this panel's array of tiles
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Assigns the passed in value as the type of each tile in this grid panel.
     * @param type the type assigned to each tile
     */
    public void assignValue(int type){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                tiles[i][j].setType(type);
                tiles[i][j].refreshColor();
            }
        }
    }
}
