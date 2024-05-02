public class SnakeNode {

    /* Used by the snake class for the body parts. Each node has a grid location and stores its previous location
    *  as well. It also keeps note of the node in front of it. */

    private final Snake snake;
    private SnakeNode nextNode;
    int type, locX, locY, locLastX, locLastY;

    public SnakeNode(Snake snake, SnakeNode nextNode, int type) {
        this.snake = snake;
        this.nextNode = nextNode;
        this.type = type;
    }

    public Snake getSnake() {
        return snake;
    }

    public int getType() {
        return type;
    }

    public int getLocationX() {
        return locX;
    }

    public int getLocationY() {
        return locY;
    }

    // Sets the location of the snake, as long as its within the bounds of the grid.
    public void setLocation(int x, int y) {
        if (x < 0 || x > snake.getGrid().getWidth() || y < 0 || y > snake.getGrid().getHeight()) {
            System.out.println("Error: attempting to set snake location outside of grid bounds.");
            return;
        }

        this.locX = x;
        this.locY = y;
    }

    public int getLastLocationX() {
        return locLastX;
    }

    public int getLastLocationY() {
        return locLastY;
    }

    // Sets the last location which is used by the latter node in the list.
    public void setLastLocation(int lastX, int lastY) {
        if (lastX < 0 || lastX > snake.getGrid().getWidth() || lastY < 0 || lastY > snake.getGrid().getHeight()) {
            System.out.println("Error: attempting to set snake location outside of grid bounds.");
            return;
        }

        this.locLastX = lastX;
        this.locLastY = lastY;
    }

    public SnakeNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(SnakeNode node) {
        this.nextNode = node;
    }

    public boolean hasNext() {
        return nextNode != null;
    }
}
