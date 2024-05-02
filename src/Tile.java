public class Tile {

    private int type, x, y;
    private final TileGrid grid;

    /* Tile class used for the Tile Grid system. Stores basic information about its location on the grid
    *  and the type of tile it is. The type is then used later for rendering the tile. */

    public Tile(TileGrid grid, int type, int x, int y) {
        this.grid = grid;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public TileGrid getTileGrid() {
        return grid;
    }

    public int getType() {
        return type;
    }

    public void setType(int newType) {
        this.type = newType;

        if (type == 3) {
            getTileGrid().setAppleTile(this);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOccupied() {
        return type > 0;
    }
}
