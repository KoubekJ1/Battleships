/**
 * Instances of this class serve as points in a 2D space, with an X and a Y value.
 */
public class Coordinates {
    private int x;
    private int y;

    /**
     * Constructs a new set of coordinates.
     * @param x the location of this point on the X axis
     * @param y the location of this point on the Y axis
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the horizontal location of this point.
     * @return the location of this point on the X axis
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the vertical location of this point.
     * @return the location of this point on the Y axis
     */
    public int getY() {
        return y;
    }
}
