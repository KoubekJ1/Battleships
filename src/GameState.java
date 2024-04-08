import java.util.ArrayList;
import java.util.Random;

/**
 * This class contains static variables and methods that keep track of and manage the state of the whole game.
 */
public class GameState {

    private static ArrayList<Ship> player1ships;
    private static ArrayList<Ship> player2ships;

    private static int player1shipsRemaining;
    private static int player2shipsRemaining;

    private static int currentPlayer;
    private static int playerCount;
    private static boolean canShoot;

    private static GameWindow gameWindow;

    private static Ship newShip;
    private static ArrayList<Ship> createdShips;
    private static boolean shipCreating;
    private static boolean isLocationValid;
    private static ShipLabel currentShipTypeLabelReference;

    private static final int SUBMARINE_COUNT = 2;
    private static final int DESTROYER_COUNT = 2;
    private static final int CRUISER_COUNT = 1;
    private static final int BATTLESHIP_COUNT = 1;
    private static final int CARRIER_COUNT = 1;

    private static int submarineRemainder;
    private static int destroyerRemainder;
    private static int cruiserRemainder;
    private static int battleshipRemainder;
    private static int carrierRemainder;

    private static boolean aiHit;
    private static boolean aiValidShot;
    private static boolean aiSunk;
    private static ArrayList<Coordinates> aiSunkTiles;
    private static ArrayList<Coordinates> aiPotentialTiles;
    private static int aiDiscoveredShipRotation;

    /**
     * Initializes all of this class's variables to their starting values.
     * Note that this method also sets the <code>shipCreating</code> value to <code>true</code>.
     * @param gameWindowReference reference to the <code>GameWindow</code> where the game plays out
     */
    public static void initialize(GameWindow gameWindowReference) {
        shipCreating = true;
        initializeShipCreation();

        player1ships = new ArrayList<>();
        player2ships = new ArrayList<>();

        player1shipsRemaining = 7;
        player2shipsRemaining = 7;

        currentPlayer = 1;
        canShoot = true;
        gameWindow = gameWindowReference;

        aiSunkTiles = new ArrayList<>();
        aiPotentialTiles = new ArrayList<>();
    }

    /**
     * Gets the ships that the player has created so far.
     * @return created ships
     */
    public static ArrayList<Ship> getCreatedShips() {
        return createdShips;
    }

    /**
     * Returns whether the game is in the ship creating phase or not.
     * @return if the game is in the ship creating phase or not
     */
    public static boolean isShipCreating() {
        return shipCreating;
    }

    /**
     * Gets the ship that is currently being placed.
     * @return the ship that is currently being placed
     */
    public static Ship getNewShip() {
        return newShip;
    }

    /**
     * Gets the reference of the ship type label that is currently selected.
     * @return the current ship type label reference
     */
    public static ShipLabel getCurrentShipTypeLabelReference() {
        return currentShipTypeLabelReference;
    }

    /**
     * Sets the reference of the current ship type label.
     * @param currentShipTypeLabelReference ship type label reference
     */
    public static void setCurrentShipTypeLabelReference(ShipLabel currentShipTypeLabelReference) {
        GameState.currentShipTypeLabelReference = currentShipTypeLabelReference;
    }

    /**
     * Gets the amount of players playing the game.
     * @return the player count
     */
    public static int getPlayerCount() {
        return playerCount;
    }

    /**
     * Sets the amount of players playing the game.
     * It should be a value between 1 and 2.
     * @param playerCount amount of players
     */
    public static void setPlayerCount(int playerCount) {
        GameState.playerCount = playerCount;
    }

    /**
     * Gets which player's turn it currently is.
     * @return current player
     */
    public static int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Initializes all variables related to ship creating.
     * This method can also be used as a way to reset the ship creating process (for example when it's the other player's turn to create his ships).
     */
    public static void initializeShipCreation() {
        isLocationValid = true;
        newShip = new Ship();
        createdShips = new ArrayList<>();
        currentShipTypeLabelReference = null;

        submarineRemainder = SUBMARINE_COUNT;
        destroyerRemainder = DESTROYER_COUNT;
        cruiserRemainder = CRUISER_COUNT;
        battleshipRemainder = BATTLESHIP_COUNT;
        carrierRemainder = CARRIER_COUNT;
    }

    /**
     * Gets how many ships of a selected type can still be placed.
     * @param shipSize the ship type's ship size
     * @return the amount of ships left
     */
    public static int getShipTypeRemainder(int shipSize) {
        return switch (shipSize) {
            case 1 -> submarineRemainder;
            case 2 -> destroyerRemainder;
            case 3 -> cruiserRemainder;
            case 4 -> battleshipRemainder;
            case 5 -> carrierRemainder;
            default -> 0;
        };
    }

    /**
     * Assigns the placed ships to the player that placed the ships.
     * Note that all the variables associated with the ship creating process will be reset back to their initial values to let the other player create his ships.
     * If the player whose ship layout was just confirmed is player 2, the <code>ShipCreating</code> value will be set to <code>false</code> and the <code>currentPlayer</code> value will be set to 1.
     */
    public static void confirmShipPlacement() {
        if (currentPlayer == 1) {
            for (Ship ship : createdShips) {
                player1ships.add(new Ship(ship.getTiles()));
                for (Coordinates coordinates : ship.getTiles()) {
                    gameWindow.getGridPlayer1().getTiles()[coordinates.getX()][coordinates.getY()].setType(2);
                    gameWindow.getGridPlayer1().getTiles()[coordinates.getX()][coordinates.getY()].refreshColor();
                }
            }
            currentPlayer = 2;
        } else {
            for (Ship ship : createdShips) {
                player2ships.add(new Ship(ship.getTiles()));
                for (Coordinates coordinates : ship.getTiles()) {
                    gameWindow.getGridPlayer2().getTiles()[coordinates.getX()][coordinates.getY()].setType(2);
                    gameWindow.getGridPlayer2().getTiles()[coordinates.getX()][coordinates.getY()].refreshColor();
                }
            }
            currentPlayer = 1;
            shipCreating = false;
        }
        initializeShipCreation();
        gameWindow.resetShipCreationLabels();
    }

    /**
     * Confirms the placement of the ship to the ship creation grid.
     * The ship will then be displayed on the ship creation grid.
     * Note that this method also adjusts the text of the corresponding <code>ShipLabel</code>.
     * If the player runs out of the type of ship that he was just placing, the corresponding <code>ShipLabel</code> will be disabled.
     */
    public static void addShip() {
        if (isLocationValid) {
            createdShips.add(new Ship(newShip.getTiles()));
            for (Coordinates coordinates : newShip.getTiles()) {
                gameWindow.getShipCreationGridPanel().getTiles()[coordinates.getX()][coordinates.getY()].setType(2);
            }
            refreshNewShipTiles();
            switch (newShip.getSize()) {
                case 1 -> {
                    submarineRemainder--;
                    currentShipTypeLabelReference.setText("Submarine (" + submarineRemainder + " left)");
                }
                case 2 -> {
                    destroyerRemainder--;
                    currentShipTypeLabelReference.setText("Destroyer (" + destroyerRemainder + " left)");
                }
                case 3 -> {
                    cruiserRemainder--;
                    currentShipTypeLabelReference.setText("Cruiser (" + cruiserRemainder + " left)");
                }
                case 4 -> {
                    battleshipRemainder--;
                    currentShipTypeLabelReference.setText("Battleship (" + battleshipRemainder + " left)");
                }
                case 5 -> {
                    carrierRemainder--;
                    currentShipTypeLabelReference.setText("Carrier (" + carrierRemainder + " left)");
                }
            }
            if (getShipTypeRemainder(newShip.getSize()) <= 0) {
                currentShipTypeLabelReference.disableSelect();
                newShip.setSize(0);
            }
        }
    }

    /**
     * Adds the tiles that the player is hovering over to the <code>ArrayList</code> of currently selected tiles.
     * This method also checks whether this ship location is valid or not. The value <code>isLocationValid</code> is then set accordingly.
     * @param coordinates the tile that the player's cursor is currently hovering over
     */
    public static void addShipTiles(Coordinates coordinates) {
        isLocationValid = true;
        if (newShip.getRotation() == 1) {
            for (int i = 0; i < GameState.newShip.getSize(); i++) {
                if (coordinates.getY() - i < 0) {
                    isLocationValid = false;
                } else {
                    newShip.getTiles().add(new Coordinates(coordinates.getX(), coordinates.getY() - i));
                }
            }
        } else {
            for (int i = 0; i < GameState.newShip.getSize(); i++) {
                if (coordinates.getX() + i > 9) {
                    isLocationValid = false;
                } else {
                    newShip.getTiles().add(new Coordinates(coordinates.getX() + i, coordinates.getY()));
                }
            }
        }
        if (isAdjacent()) {
            isLocationValid = false;
        }
    }

    /**
     * Checks whether the tiles the player is currently hovering over are adjacent to an existing ship or not.
     * @return whether the tiles the player is hovering over are adjacent to another ship or not
     */
    private static boolean isAdjacent() {
        for (Coordinates shipCoordinates : newShip.getTiles()) {
            for (Coordinates surroundingCoordinates : getSurroundingTilesCoordinates(shipCoordinates)) {
                if (gameWindow.getShipCreationGridPanel().getTiles()[surroundingCoordinates.getX()][surroundingCoordinates.getY()].getType() == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Displays the tiles that the player is hovering over on the ship creation grid.
     * If the location is valid the tiles are going to be blue, otherwise they're going to be red.
     * Note that the tiles' type does not change.
     */
    public static void showShip() {
        for (Coordinates coordinates : newShip.getTiles()) {
            if (isLocationValid) {
                gameWindow.getShipCreationGridPanel().getTiles()[coordinates.getX()][coordinates.getY()].highlightValid();
            } else {
                gameWindow.getShipCreationGridPanel().getTiles()[coordinates.getX()][coordinates.getY()].highlightInvalid();
            }
        }
    }

    /**
     * Changes the tiles the player is hovering over's color to their set type.
     */
    public static void refreshNewShipTiles() {
        for (Coordinates coordinates : newShip.getTiles()) {
            gameWindow.getShipCreationGridPanel().getTiles()[coordinates.getX()][coordinates.getY()].refreshColor();
        }
    }

    /**
     * Generates the AI's ships.
     * This method adds the generated ships to player 2's <code>GridPanel</code> and to player 1's opponent <code>GridPanel</code>.
     */
    public static void AIGenerateShips() {
        Random random = new Random();
        initializeShipCreation();
        currentShipTypeLabelReference = new ShipLabel(0);
        for (int i = 0; i < 7; i++) {
            switch (i) {
                case 0 -> newShip.setSize(5);
                case 1 -> newShip.setSize(4);
                case 2 -> newShip.setSize(3);
                default -> {
                    if (i >= 5) {
                        newShip.setSize(1);
                    } else {
                        newShip.setSize(2);
                    }
                }
            }
            addShipTiles(new Coordinates(random.nextInt(10), random.nextInt(10)));
            newShip.setRotation(random.nextInt(2) + 1);
            if (isLocationValid) {
                addShip();
            } else {
                i--;
            }
            newShip.getTiles().clear();
        }
        confirmShipPlacement();
    }

    /**
     * "Shoots" at the tile that player 1 clicked on.
     * If player 1 shot a valid tile (one that wasn't already revealed), the value <code>canShoot</code> gets set to <code>false</code>, so the player can't shoot twice in 1 turn.
     * The tiles in both <code>gridPlayer2</code> and <code>gridOpponent1</code>'s type gets changed to their new corresponding type, then their color is refreshed.
     * If player 1 sinks player 2's last ship, the game ends.
     * @param tile the tile that was clicked on
     */
    public static void shootPlayer1(Tile tile) {
        if (canShoot) {
            if (gameWindow.getGridOpponent1().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].getType() == 0 || gameWindow.getGridOpponent1().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].getType() == 5) {
                if (gameWindow.getGridPlayer2().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].getType() == 2) {
                    gameWindow.getGridPlayer2().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].setType(3);
                    for (Ship ship : player2ships) {
                        for (Coordinates coordinates : ship.getTiles()) {
                            if (tile.getCoordinates().getX() == coordinates.getX() && tile.getCoordinates().getY() == coordinates.getY()) {
                                ship.setSunkenTileCount(ship.getSunkenTileCount() + 1);
                                gameWindow.getGridPlayer2().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].setType(3);
                                if (ship.getSunkenTileCount() >= ship.getSize()) {
                                    revealSurroundingTiles(ship);
                                    player2shipsRemaining--;
                                }
                            }
                        }
                    }
                } else {
                    gameWindow.getGridPlayer2().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].setType(4);
                }
                gameWindow.getGridOpponent1().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].setType(gameWindow.getGridPlayer2().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].getType());
                tile.setType(gameWindow.getGridPlayer2().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].getType());
                tile.refreshColor();
                gameWindow.getGridPlayer2().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].refreshColor();
                canShoot = false;
                gameWindow.getEndTurnButton().setEnabled(true);
                if (player2shipsRemaining <= 0) {
                    gameWindow.victory();
                }
            }
        }
    }

    /**
     * "Shoots" at the tile that player 2 clicked on.
     * If player 2 shot a valid tile (one that wasn't already revealed), the value <code>canShoot</code> gets set to <code>false</code>, so the player can't shoot twice in 1 turn.
     * The tiles in both <code>gridPlayer1</code> and <code>gridOpponent2</code>'s type gets changed to their new corresponding type, then their color is refreshed.
     * If player 2 sinks player 1's last ship, the game ends.
     * The AI values regarding the current shot get reset at the beginning of the method.
     * If the AI shoots at a valid tile, the value <code>aiValidShot</code> gets set to <code>true</code>.
     * If the AI manages to hit the ship, the value <code>aiHit</code> gets set to <code>true</code>.
     * If the AI manages to sink the ship, the value <code>aiSunk</code> gets set to <code>true</code>.
     * @param tile the tile that was clicked on
     */
    public static void shootPlayer2(Tile tile) {
        aiHit = false;
        aiValidShot = false;
        aiSunk = false;
        if (canShoot) {
            if (gameWindow.getGridOpponent2().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].getType() == 0 || gameWindow.getGridOpponent2().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].getType() == 5) {
                aiValidShot = true;
                if (gameWindow.getGridPlayer1().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].getType() == 2) {
                    gameWindow.getGridPlayer1().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].setType(3);
                    aiHit = true;
                    for (Ship ship : player1ships) {
                        for (Coordinates coordinates : ship.getTiles()) {
                            if (tile.getCoordinates().getX() == coordinates.getX() && tile.getCoordinates().getY() == coordinates.getY()) {
                                ship.setSunkenTileCount(ship.getSunkenTileCount() + 1);
                                gameWindow.getGridPlayer1().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].setType(3);
                                if (ship.getSunkenTileCount() >= ship.getSize()) {
                                    revealSurroundingTiles(ship);
                                    player1shipsRemaining--;
                                    aiSunk = true;
                                }
                            }
                        }
                    }
                } else {
                    gameWindow.getGridPlayer1().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].setType(4);
                }
                gameWindow.getGridOpponent2().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].setType(gameWindow.getGridPlayer1().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].getType());
                tile.setType(gameWindow.getGridPlayer1().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].getType());
                tile.refreshColor();
                gameWindow.getGridPlayer1().getTiles()[tile.getCoordinates().getX()][tile.getCoordinates().getY()].refreshColor();
                canShoot = false;
                gameWindow.getEndTurnButton().setEnabled(true);
                if (player1shipsRemaining <= 0) {
                    gameWindow.victory();
                }
            }
        }
    }

    /**
     * Manages the AI's entire turn.
     * If the AI shot an invalid tile, it shoots again.
     * If it doesn't have a current discovered ship, it shoots a random tile.
     * If it manages to hit a ship, it writes down it's surrounding tiles as potential targets for the next turn.
     * If it then manages to hit another one of the ship's tiles, it writes down its rotation, and it only shoots adjacent tiles on that row/column the next time.
     * If it sinks the ship, it'll look for another ship again on its next turn.
     */
    public static void AITurn() {
        Random random = new Random();
        Coordinates aiShot;
        if (aiSunkTiles.size() == 0) {
            do {
                aiShot = new Coordinates(random.nextInt(10), random.nextInt(10));
                shootPlayer2(gameWindow.getGridOpponent2().getTiles()[aiShot.getX()][aiShot.getY()]);
            } while (!aiValidShot);
            if (aiHit) {
                aiSunkTiles.add(new Coordinates(aiShot.getX(), aiShot.getY()));
                aiPotentialTiles.addAll(getCloseSurroundingTilesCoordinates(aiShot));
            }
        } else {
            do {
                aiShot = aiPotentialTiles.get(random.nextInt(aiPotentialTiles.size()));
                shootPlayer2(gameWindow.getGridOpponent2().getTiles()[aiShot.getX()][aiShot.getY()]);
            } while (!aiValidShot);
            if (aiHit) {
                aiSunkTiles.add(new Coordinates(aiShot.getX(), aiShot.getY()));
                aiPotentialTiles.clear();
                if (aiSunkTiles.size() == 2) {
                    if (aiSunkTiles.get(0).getX() < aiSunkTiles.get(1).getX() || aiSunkTiles.get(0).getX() > aiSunkTiles.get(1).getX()) {
                        aiDiscoveredShipRotation = 2;
                    } else {
                        aiDiscoveredShipRotation = 1;
                    }
                }
                if (aiDiscoveredShipRotation == 1) {
                    int lowestCoordinates = getLowestCoordinatesIndex(aiSunkTiles);
                    int highestCoordinates = getHighestCoordinatesIndex(aiSunkTiles);
                    if (aiSunkTiles.get(lowestCoordinates).getY() + 1 <= 9) {
                        aiPotentialTiles.add(new Coordinates(aiSunkTiles.get(lowestCoordinates).getX(), aiSunkTiles.get(lowestCoordinates).getY() + 1));
                    }
                    if (aiSunkTiles.get(highestCoordinates).getY() - 1 >= 0) {
                        aiPotentialTiles.add(new Coordinates(aiSunkTiles.get(highestCoordinates).getX(), aiSunkTiles.get(highestCoordinates).getY() - 1));
                    }
                } else {
                    int leftMostCoordinates = getLeftmostCoordinatesIndex(aiSunkTiles);
                    int rightMostCoordinates = getRightmostCoordinatesIndex(aiSunkTiles);
                    if (aiSunkTiles.get(leftMostCoordinates).getX() - 1 >= 0) {
                        aiPotentialTiles.add(new Coordinates(aiSunkTiles.get(leftMostCoordinates).getX() - 1, aiSunkTiles.get(leftMostCoordinates).getY()));
                    }
                    if (aiSunkTiles.get(rightMostCoordinates).getX() + 1 <= 9) {
                        aiPotentialTiles.add(new Coordinates(aiSunkTiles.get(rightMostCoordinates).getX() + 1, aiSunkTiles.get(rightMostCoordinates).getY()));
                    }
                }
            }
        }

        if (aiSunk) {
            aiPotentialTiles.clear();
            aiSunkTiles.clear();
        }
        newTurn();
    }

    /**
     * Gets the index of the lowest coordinates in an <code>ArrayList</code> of coordinates.
     * @param coordinatesArray the array list of coordinates
     * @return the index of the lowest coordinates in the <code>ArrayList</code>
     */
    private static int getLowestCoordinatesIndex(ArrayList<Coordinates> coordinatesArray) {
        int lowestIndex = 0;
        for (int i = 1; i < coordinatesArray.size(); i++) {
            if (coordinatesArray.get(i).getY() > coordinatesArray.get(lowestIndex).getY()) {
                lowestIndex = i;
            }
        }
        return lowestIndex;
    }

    /**
     * Gets the index of the highest coordinates in an <code>ArrayList</code> of coordinates.
     * @param coordinatesArray the array list of coordinates
     * @return the index of the highest coordinates in the <code>ArrayList</code>
     */
    private static int getHighestCoordinatesIndex(ArrayList<Coordinates> coordinatesArray) {
        int highestIndex = 0;
        for (int i = 1; i < coordinatesArray.size(); i++) {
            if (coordinatesArray.get(i).getY() < coordinatesArray.get(highestIndex).getY()) {
                highestIndex = i;
            }
        }
        return highestIndex;
    }

    /**
     * Gets the index of the leftmost coordinates in an <code>ArrayList</code> of coordinates.
     * @param coordinatesArray the array list of coordinates
     * @return the index of the leftmost coordinates in the <code>ArrayList</code>
     */
    private static int getLeftmostCoordinatesIndex(ArrayList<Coordinates> coordinatesArray) {
        int leftMostIndex = 0;
        for (int i = 1; i < coordinatesArray.size(); i++) {
            if (coordinatesArray.get(i).getX() < coordinatesArray.get(leftMostIndex).getX()) {
                leftMostIndex = i;
            }
        }
        return leftMostIndex;
    }

    /**
     * Gets the index of the rightmost coordinates in an <code>ArrayList</code> of coordinates.
     * @param coordinatesArray the array list of coordinates
     * @return the index of the rightmost coordinates in the <code>ArrayList</code>
     */
    private static int getRightmostCoordinatesIndex(ArrayList<Coordinates> coordinatesArray) {
        int rightMostIndex = 0;
        for (int i = 1; i < coordinatesArray.size(); i++) {
            if (coordinatesArray.get(i).getX() > coordinatesArray.get(rightMostIndex).getX()) {
                rightMostIndex = i;
            }
        }
        return rightMostIndex;
    }

    /**
     * Gets the coordinates of all the tiles adjacent to the tile at the passed in coordinates, including corner tiles.
     * @param coordinates the coordinates to get the adjacent tiles of
     * @return an <code>ArrayList</code> of the adjacent tiles' coordinates
     */
    private static ArrayList<Coordinates> getSurroundingTilesCoordinates(Coordinates coordinates) {
        ArrayList<Coordinates> surroundingTiles = new ArrayList<>();
        if (coordinates.getX() + 1 <= 9) {
            surroundingTiles.add(new Coordinates(coordinates.getX() + 1, coordinates.getY()));
        }
        if (coordinates.getY() + 1 <= 9) {
            surroundingTiles.add(new Coordinates(coordinates.getX(), coordinates.getY() + 1));
        }
        if (coordinates.getX() + 1 <= 9 && coordinates.getY() + 1 <= 9) {
            surroundingTiles.add(new Coordinates(coordinates.getX() + 1, coordinates.getY() + 1));
        }
        if (coordinates.getX() - 1 >= 0) {
            surroundingTiles.add(new Coordinates(coordinates.getX() - 1, coordinates.getY()));
        }
        if (coordinates.getY() - 1 >= 0) {
            surroundingTiles.add(new Coordinates(coordinates.getX(), coordinates.getY() - 1));
        }
        if (coordinates.getX() - 1 >= 0 && coordinates.getY() - 1 >= 0) {
            surroundingTiles.add(new Coordinates(coordinates.getX() - 1, coordinates.getY() - 1));
        }
        if (coordinates.getX() + 1 <= 9 && coordinates.getY() - 1 >= 0) {
            surroundingTiles.add(new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1));
        }
        if (coordinates.getX() - 1 >= 0 && coordinates.getY() + 1 <= 9) {
            surroundingTiles.add(new Coordinates(coordinates.getX() - 1, coordinates.getY() + 1));
        }
        return surroundingTiles;
    }

    /**
     * Gets the coordinates of all the tiles adjacent to the tile at the passed in coordinates, excluding corner tiles.
     * @param coordinates the coordinates to get the adjacent tiles of
     * @return an <code>ArrayList</code> of the adjacent tiles' coordinates
     */
    private static ArrayList<Coordinates> getCloseSurroundingTilesCoordinates(Coordinates coordinates) {
        ArrayList<Coordinates> surroundingTiles = new ArrayList<>();
        if (coordinates.getX() + 1 <= 9) {
            surroundingTiles.add(new Coordinates(coordinates.getX() + 1, coordinates.getY()));
        }
        if (coordinates.getY() + 1 <= 9) {
            surroundingTiles.add(new Coordinates(coordinates.getX(), coordinates.getY() + 1));
        }
        if (coordinates.getX() - 1 >= 0) {
            surroundingTiles.add(new Coordinates(coordinates.getX() - 1, coordinates.getY()));
        }
        if (coordinates.getY() - 1 >= 0) {
            surroundingTiles.add(new Coordinates(coordinates.getX(), coordinates.getY() - 1));
        }
        return surroundingTiles;
    }

    /**
     * Reveals all the tiles around a ship.
     * The tile will only be revealed if its type is undiscovered (0).
     * @param ship the ship to reveal the surrounding tiles of
     */
    private static void revealSurroundingTiles(Ship ship) {
        for (Coordinates shipTileCoordinates : ship.getTiles()) {
            for (Coordinates surroundingTileCoordinates : getSurroundingTilesCoordinates(shipTileCoordinates)) {
                if (currentPlayer == 1) {
                    if (gameWindow.getGridOpponent1().getTiles()[surroundingTileCoordinates.getX()][surroundingTileCoordinates.getY()].getType() == 0) {
                        gameWindow.getGridOpponent1().getTiles()[surroundingTileCoordinates.getX()][surroundingTileCoordinates.getY()].setType(1);
                        gameWindow.getGridOpponent1().getTiles()[surroundingTileCoordinates.getX()][surroundingTileCoordinates.getY()].refreshColor();
                    }
                } else {
                    if (gameWindow.getGridOpponent2().getTiles()[surroundingTileCoordinates.getX()][surroundingTileCoordinates.getY()].getType() == 0) {
                        gameWindow.getGridOpponent2().getTiles()[surroundingTileCoordinates.getX()][surroundingTileCoordinates.getY()].setType(1);
                        gameWindow.getGridOpponent2().getTiles()[surroundingTileCoordinates.getX()][surroundingTileCoordinates.getY()].refreshColor();
                    }
                }
            }
        }
    }

    /**
     * Resets the <code>canShoot</code> value back to <code>true</code> and sets the <code>currentPlayer</code> value to the other player.
     */
    public static void newTurn() {
        canShoot = true;
        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
    }
}
