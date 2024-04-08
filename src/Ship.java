import java.util.ArrayList;

/**
 * Instances of this class represent ships on a 2D grid.
 */
public class Ship {
    private ArrayList<Coordinates> tiles;
    private int rotation;
    private int size;
    private int sunkenTileCount;
    private Coordinates tmpLocation;

    /**
     * Constructs a new ship.
     * This constructor is meant for ships that are used for ship creating.
     * The rotation of this ship is set to 1 (vertical)
     */
    public Ship() {
        tiles = new ArrayList<>();
        rotation = 1;
    }

    /**
     * Constructs a new ship and assigns the passed in coordinates to it.
     * This constructor is meant for ships with an already decided on location.
     * @param tiles the new coordinates
     */
    public Ship(ArrayList<Coordinates> tiles){
        this.tiles = new ArrayList<>();
        sunkenTileCount = 0;
        this.tiles.addAll(tiles);
        size = this.tiles.size();
    }

    /**
     * Gets the <code>ArrayList</code> of coordinates that this ship occupies.
     * @return the <code>ArrayList</code> of coordinates that this ship occupies
     */
    public ArrayList<Coordinates> getTiles() {
        return tiles;
    }

    /**
     * Gets this ship's rotation.
     * 1 = Vertical
     * 2 = Horizontal
     * @return this ship's rotation
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Sets this ship's rotation.
     * Only values between 1 and 2 are intended.
     * 1 = Vertical
     * 2 = Horizontal
     * @param rotation the new rotation
     */
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    /**
     * Gets this ship's size.
     * @return this ship's size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size of this ship.
     * @param size the new size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets how many tiles of this ship have been sunk.
     * @return sunken tile count
     */
    public int getSunkenTileCount() {
        return sunkenTileCount;
    }

    /**
     * Sets how many of this ship's tiles have been sunk.
     * @param sunkenTileCount sunken tile count
     */
    public void setSunkenTileCount(int sunkenTileCount) {
        this.sunkenTileCount = sunkenTileCount;
    }

    /**
     * Toggles the rotation of this ship.
     * Only intended for ships that are in the process of being placed.
     * This method toggles the rotation value (from 1 to 2 and vice versa), refreshes the tiles this ship used to occupy and adds the new coordinates with the new rotation value, then it displays the ship on the ship creation grid.
     */
    public void toggleRotation(){
        if(rotation == 1){
            rotation = 2;
        } else {
            rotation = 1;
        }
        tmpLocation = tiles.get(0);
        GameState.refreshNewShipTiles();
        tiles.clear();
        GameState.addShipTiles(tmpLocation);
        GameState.showShip();
    }
}
