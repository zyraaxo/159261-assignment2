import java.util.ArrayList;

/* Main class for controlling the snake.
*
*  A snake is made up of a bunch of SnakeNode objects each representing a body part of the snake. They are stored
*  in a ArrayList so that they can be added within order. It also makes it easier to access the head/tail part
*  of the snake.*/

public class Snake {
    TileGrid grid;
    ArrayList<SnakeNode> nodes;
    SnakeNode head;
    Direction direction;
    int length, score;
    int pendingNodes;
    int nextX, nextY;
    int offsetX, offsetY;
    int typeHead, typeBody;
    int playerNum;

    public Snake(TileGrid grid, int playerNum) {
        initAttributes(grid, playerNum);

        init();
    }

    public Snake(TileGrid grid, int playerNum, int offsetX, int offsetY) {
        initAttributes(grid, playerNum);
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        init();
    }


    /* Sets up basic data about the snake like location offset (used for two players), initialise the node list
       and then also the colour of the snake based on there being one or two players. */
    private void initAttributes(TileGrid grid, int playerNum) {
        this.grid = grid;
        this.playerNum = playerNum;
        this.offsetX = 0;
        this.offsetY = 0;
        nodes = new ArrayList<>();

        if (playerNum == 0) {
            typeHead = 1;
            typeBody = 2;
        } else {
            typeHead = 4;
            typeBody = 5;
        }
    }

    /* Initialises the snake. Sets the default location, length and the body parts. Also clears the nodes for when
    *  the game resets and sets the score to 0.  */
    public void init() {
        nodes.clear();
        direction = Direction.NORTH;
        this.length = 3;
        grid.setAppleTile(null);
        score = 0;

        head = new SnakeNode(this, null, typeHead);
        head.setLocation((grid.getWidth() / 2) + offsetX, (grid.getHeight() / 2) + offsetY);
        nodes.add(head);

        SnakeNode body = new SnakeNode(this, head, typeBody);
        body.setLocation((grid.getWidth() / 2) + offsetX, (grid.getHeight() / 2) + 1 + offsetY);
        nodes.add(body);

        SnakeNode tail = new SnakeNode(this, body, typeBody);
        tail.setLocation((grid.getWidth() / 2) + offsetX, (grid.getHeight() / 2) + 2 + offsetY);
        nodes.add(tail);
    }

    public TileGrid getGrid() {
        return grid;
    }

    public int getPlayerNumber() {
        return playerNum;
    }

    public int getLength() {
        return length;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore(int amount) {
        score += amount;
    }

    public ArrayList<SnakeNode> getNodes() {
        return nodes;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /* Primary function for moving the snake. It first calculates the next location based on
       the head coordinates and the direction.  */
    public void move() {
        nextX = head.getLocationX() + getDirection().getX();
        nextY = head.getLocationY() + getDirection().getY();

        /* This part checks if the current location is within the bounds of the grid, if it is not then
        *  it can end the game and play the appropriate sounds. */
        if (!isInBounds()) {
            grid.getEngine().playAudio(grid.getEngine().hit);
            grid.getEngine().playAudio(grid.getEngine().endOfGame);
            Main.isOver = true;
            return;
        }

        /* Checks if it has collided with another snake or the apple by checking if the tile it is on is
           occupied or not. */
        if (grid.getTiles()[nextX][nextY].isOccupied()) {
            if (grid.getTiles()[nextX][nextY].getType() == 3) {
                collectApple();
            } else {
                grid.getEngine().playAudio(grid.getEngine().hit);
                grid.getEngine().playAudio(grid.getEngine().endOfGame);
                Main.isOver = true;
                return;
            }
        }

        // Updates the last location so the latter body part knows when to move to.
        head.setLastLocation(head.getLocationX(), head.getLocationY());
        head.setLocation(nextX, nextY);

        boolean canMove = true;

        /* This part checks if there are any pending nodes (if the snake length needs to be extended from picking
        *  up an apple). It does this by adding it to the front just behind the head to achieve or more clean
        *  look and avoid weird scenarios like when the tail in at the corner of the screen.
        *
        *  It does this by moving only the player head and then adding a new body part to when the head was and then
        *  updates the head and body part before the head to adjust for this */
        if (getPendingNodes() > 0) {
            canMove = false;

            SnakeNode newBody = new SnakeNode(this, head, typeBody);
            newBody.setLocation(head.getLastLocationX(), head.getLastLocationY());
            newBody.setLastLocation(nodes.get(1).getLocationX(), nodes.get(1).getLocationY());
            nodes.get(1).setNextNode(newBody);
            nodes.add(1, newBody);

            removeQueuedNode();
            length++;
        }

        /* This is where the actual movement happens. It loops through each snake node and moves it to the
        *  last location of the node in front of it. This way they are essentially all moving in a domino effect
        *  of where the head location was. */
        for (int i = 0; i < getLength(); i++) {
            SnakeNode node = getNodes().get(i);
            if (canMove && node.hasNext()) {
                if (i == getLength() - 1) {
                    grid.getTiles()[node.getLocationX()][node.getLocationY()].setType(0);
                }

                node.setLastLocation(node.getLocationX(), node.getLocationY());
                node.setLocation(node.getNextNode().getLastLocationX(), node.getNextNode().getLastLocationY());
            }

            grid.getTiles()[node.getLocationX()][node.getLocationY()].setType(node.getType());
        }
    }

    public void queueNewNode() {
        this.pendingNodes++;
    }

    public int getPendingNodes() {
        return pendingNodes;
    }

    public void removeQueuedNode() {
        this.pendingNodes--;
    }

    public boolean isInBounds() {
        return nextX >= 0 && nextX < grid.getWidth() && nextY >= 0 && nextY < grid.getHeight();
    }

    /* Function that is called for when the apple is collided with. It clears the apple and adds a new body part
    *  to the snake, within the limit of 20.*/
    private void collectApple() {
        grid.getEngine().playAudio(grid.getEngine().collectApple);
        incrementScore(1);

        if (getScore() < 20) {
            queueNewNode();
        } else {
            grid.getEngine().playAudio(grid.getEngine().win);
            Main.isOver = true;
        }


        grid.setAppleTile(null);
    }
}
