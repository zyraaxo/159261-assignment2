public enum Direction {

    /* Simple Enum class. This is use for setting the direction the snake is facing as well as getting the natural opposite
    *  direction. This is used so the snake can't turn back on itself. */

    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    private final int x, y;
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // X and Y is used for the actual direction. i.e. when Snake#move() is called it will change the snakes location by 'X' and 'Y'
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOppositeTo(Direction dir) {
        return (this == Direction.NORTH && dir == Direction.SOUTH) || (this == Direction.SOUTH && dir == Direction.NORTH)
                || (this == Direction.EAST && dir == Direction.WEST) || (this == Direction.WEST && dir == Direction.EAST);
    }
}
