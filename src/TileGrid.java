import java.util.ArrayList;
import java.util.List;

/* This class sets up the framework for the game. The game is completely based off a tile system and their locations
*  and is limited to the grid size.
*
*  It uses an array that is x wide and y long. Each square in the grid is split up into a tile which it will store
*  information about the type of tile it is e.g. void, snake head, body ect.  */

public class TileGrid {

    private final Main engine;
    private Tile[][] tiles;
    private final int width, height;

    private Tile appleTile;

    public TileGrid(Main engine, int width, int height) {
        this.engine = engine;
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];

        // Initialising the tile array.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(this, 0, x, y);
            }
        }
    }

    public Main getEngine() {
        return engine;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    // Never used.
    public void setTile(int x, int y, int type) {
        if (x < 0 || x > width || y < 0 || y > height) {
            System.out.println("Error: attempting to set tile outside of grid bounds.");
            return;
        }

        tiles[x][y].setType(type);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile getAppleTile() {
        return appleTile;
    }

    public void setAppleTile(Tile tile) {
        this.appleTile = tile;
    }


    /* Function to clear all the tiles on the screen. It has two possible outcomes, it either sets all of them to
    *  blank tiles OR if the gamemode is hardmode (has walls) then it sets the tiles to either blanks or a 2% chance
    *  of being a wall tile. */
    public void clearTiles() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y].setType(0);
            }
        }

        if (engine.GAME_MODE == 1) {
            /* These confinements are here to that the walls dont spawn right around the player spawn point. This is to
               avoid the player starting and immediately dying. */
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if ((x > 0 && x < 22) || x > 26) {
                        if ((y > 0 && y < 22) || y > 26) {
                            int rand = engine.rand(100);
                            if (rand < 2) {
                                tiles[x][y].setType(6); //BLOCK
                            } else {
                                tiles[x][y].setType(0);
                            }
                        }
                    } else {
                        tiles[x][y].setType(0);
                    }
                }
            }
        }
    }

    /* This function is used for attempting to spawn in an apple providing there isn't already an apple present.
    *  It will get all the available tiles (empty ones) and select a random tile from that list to be the new
    *  apple. */
    public void spawnApple() {
        if (getAppleTile() != null) {
            return;
        }

        List<Tile> emptyTiles = getEmptyTiles();
        Tile selectedAppleTile = emptyTiles.get(engine.rand(emptyTiles.size()));
        this.tiles[selectedAppleTile.getX()][selectedAppleTile.getY()].setType(3);
    }

    /* This function loops through each tile and checks if it is occupied and if it's not then add it
       to the empty tiles list and return it */
    public List<Tile> getEmptyTiles() {
        List<Tile> emptyTiles = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!tiles[x][y].isOccupied()) {
                    emptyTiles.add(tiles[x][y]);
                }
            }
        }

        return emptyTiles;
    }
}
