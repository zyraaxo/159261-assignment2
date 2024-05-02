/*
* Corey Smith (20006140)
* Massey University Albany
* 159.261 Games Programming - Assignment 1
*
* Extra images were created by me.
* Audio files were sourced from https://mixkit.co/free-sound-effects/game/
* */


import java.awt.*;
import java.awt.event.KeyEvent;

public class Main extends GameEngine {

    private final int SCREEN_SIZE = 600;
    private final int GRID_SIZE = 50;

    public Snake snake;
    public Snake snakeP2;
    public Image body, bodyP2, head, headP2, apple, wall, drawGuide, drawGuideP2;
    public AudioClip collectApple, endOfGame, menuSelect, hit, win;
    public TileGrid grid;

    public static boolean isPaused, hasStarted, isOver;
    public int GAME_MODE = -1;
    public int tileSize;
    public double counter, keyCounter, keyCounterP2;
    public double speed;

    public static void main(String[] args) {
        createGame(new Main(), 60);
    }

    /* Initialises the grid frame, the snakes, image and audio files.  */
    @Override
    public void init() {
        setWindowSize(SCREEN_SIZE, SCREEN_SIZE);
        grid = new TileGrid(this, GRID_SIZE, GRID_SIZE);
        tileSize = SCREEN_SIZE / GRID_SIZE;

        snake = new Snake(grid, 0);
        snakeP2 = new Snake(grid, 1, -10, 0);

        body = loadImage(System.getProperty("user.dir") + "/resources/body.png");
        head = loadImage(System.getProperty("user.dir") + "/resources/head.png");
        bodyP2 = loadImage(System.getProperty("user.dir") + "/resources/body_2.png");
        headP2 = loadImage(System.getProperty("user.dir") + "/resources/head_2.png");
        apple = loadImage(System.getProperty("user.dir") + "/resources/apple.png");
        wall = loadImage(System.getProperty("user.dir") + "/resources/wall.png");
        drawGuide = loadImage(System.getProperty("user.dir") + "/resources/move_guide.png");
        drawGuideP2 = loadImage(System.getProperty("user.dir") + "/resources/move_guide_2.png");

        collectApple = loadAudio(System.getProperty("user.dir") + "/resources/sounds/collect_apple.wav");
        endOfGame = loadAudio(System.getProperty("user.dir") + "/resources/sounds/end_of_game.wav");
        menuSelect = loadAudio(System.getProperty("user.dir") + "/resources/sounds/menu_select.wav");
        hit = loadAudio(System.getProperty("user.dir") + "/resources/sounds/hit.wav");
        win = loadAudio(System.getProperty("user.dir") + "/resources/sounds/win.wav");
    }

    /* This function checks if the game can be running or not based on if it is paused, active or at the start menu.
       It also keeps track of the key cooldown timers.

        Also calls the Snake#move function and attempts to spawn in the apple. */
    @Override
    public void update(double dt) {
        if (isPaused || !hasStarted || isOver) {
            return;
        }

        if (keyCounter > 0) {
            keyCounter -= dt;
        }

        if (keyCounterP2 > 0) {
            keyCounterP2 -= dt;
        }

        counter += dt;
        if (counter > speed) {
            counter = 0;

            snake.move();
            if (GAME_MODE == 2) {
                //snakeP2.move();
            }

            grid.spawnApple();
        }
    }

    /* This is where the title screens, snake and tiles are rendered. It uses gamemodes to determine what to render.
    * GAMEMODE:-1 means it hasnt started yet, 0 is the classic snake game, 1 is hardmode and 2 is multiplayer.*/
    @Override
    public void paintComponent() {
        changeBackgroundColor(Color.black);
        clearBackground(width(), height());
        changeColor(Color.white);

        if (GAME_MODE == -1) {
            displayTitleScreen();
            return;
        }

        if (isOver) {
            displayEndScreen();
            return;
        }

        if (!hasStarted) {
            displayInstructions();
            return;
        }

        if (GAME_MODE == 2) {
            drawText(SCREEN_SIZE - 150, 20, "Score P2: " + snakeP2.getScore(), "Monospaced", 20);
            drawText(50, 20, "Score P1: " + snake.getScore(), "Monospaced", 20);
        } else {
            drawText(SCREEN_SIZE - 120, 20, "Score: " + snake.getScore(), "Monospaced", 20);
        }

        if (isPaused) {
            drawRectangle(165, 10, 275, 50);
            drawText(170, 50, "Game Paused!", "Monospaced", 37);
        }

        displayTiles();
    }

    // Function used to end the game. It clears all the tiles and initialises the snake method.
    public void endGame() {
        grid.clearTiles();
        snake.init();
        if (GAME_MODE == 2) {
            snakeP2.init();
        }
    }

    // Displays the first title screen where the player can select a gamemode based on key input (1, 2 or 3).
    public void displayTitleScreen() {
        drawText(230, 50, "Snake!", "Monospaced", 35);
        drawText(220, 200, "Select a gamemode:", "Monospaced", 15);
        changeColor(Color.green);
        drawText(70, 250, "[1] - CLASSIC", "Monospaced", 15);
        changeColor(Color.red);
        drawText(230, 250, "[2] - HARDMODE", "Monospaced", 15);
        changeColor(Color.cyan);
        drawText(400, 250, "[3] - TWO PLAYER", "Monospaced", 15);
    }


    /* Displays the instructions based on the gamemode. This makes sure the player is prepared for what the gamemode
       offers and how to play it.

        Different gamemodes will display different instructions/images. */
    public void displayInstructions() {
        drawText(170, 50, "How to Play?", "Monospaced", 35);

        if (GAME_MODE == 0 || GAME_MODE == 1) {
            drawText(120, 130, "Use WASD to move up, left, down and right.", "Monospaced", 15);
            drawText(120, 150, "Collect green apples to grow your snake and", "Monospaced", 15);

            if (GAME_MODE == 0) {
                drawText(120, 180, "increase your score.", "Monospaced", 15);
            } else if (GAME_MODE == 1) {
                drawText(120, 180, "increase your score. However in this mode,", "Monospaced", 15);
                drawText(120, 200, "you'll move faster and need to watch out", "Monospaced", 15);
                drawText(120, 220, "for additional walls!", "Monospaced", 15);
                drawImage(wall, 320, 205, 20, 20);
                drawImage(wall, 350, 205, 20, 20);
            }

            drawImage(apple, 285, 250, 30, 30);
            drawImage(drawGuide, 200, 325, 200, 200);
        } else {
            drawText(120, 120, "Two players will compete against each other.", "Monospaced", 15);
            drawText(120, 140, "The first player to reach 20 points wins.", "Monospaced", 15);
            changeColor(Color.green);
            drawText(80, 180, "[Player 1]", "Monospaced", 20);
            changeColor(Color.white);
            drawText(40, 205, "Use WASD to move up, left, down,", "Monospaced", 12);
            drawText(95, 220, "and right.", "Monospaced", 12);
            drawImage(drawGuide, 40, 280, 200, 200);

            changeColor(Color.cyan);
            drawText(400, 180, "[Player 2]", "Monospaced", 20);
            changeColor(Color.white);
            drawText(350, 205, "Use Arrow Keys to move up, left,", "Monospaced", 12);
            drawText(400, 220, "down and right.", "Monospaced", 12);
            drawImage(drawGuideP2, 360, 280, 200, 200);
        }
        drawText(200, 565, "Press [SPACE] to begin!", "Monospaced", 15);
        drawText(200, 585, "Press [ESC] to go back!", "Monospaced", 15);
    }

    // Displays the end screen, this occurs when the game is over. It also displays the players final score.
    public void displayEndScreen() {
        if (snake.getScore() >= 20 || snakeP2.getScore() >= 20) {
            changeColor(Color.green);
            if (GAME_MODE == 1) {
                drawText(215, 50, "You Won!", "Monospaced", 37);
            } else if (snake.getScore() > snakeP2.getScore()) {
                drawText(220, 50, "P1 Won!", "Monospaced", 37);
            } else if (snakeP2.getScore() > snake.getScore()) {
                drawText(220, 50, "P2 Won!", "Monospaced", 37);
            }
        } else {
            changeColor(Color.red);
            drawText(190, 50, "Game Over!", "Monospaced", 37);
        }

        drawRectangle(185, 10, 225, 50);
        changeColor(Color.yellow);
        if (GAME_MODE == 2) {
            drawText(200, 150, "Final score (P1): ", "Monospaced", 20);
            drawText(430, 150, snake.getScore() + "", "Monospaced", 20);

            drawText(200, 180, "Final score (P2): ", "Monospaced", 20);
            drawText(430, 180, snakeP2.getScore() + "", "Monospaced", 20);
        } else {
            drawText(200, 150, "Final score: ", "Monospaced", 20);
            drawText(360, 150, snake.getScore() + "", "Monospaced", 20);
        }

        changeColor(Color.white);
        drawText(150, 230, "Press [SPACE] to continue", "Monospaced", 20);
    }

    /* This function is used to render the tiles based on their type.
    *  0 = Blank/ignore rendering any images
    *  1 = Player 1 head
    *  2 = Player 2 body
    *  3 = Green Apple
    *  4 = Player 2 head
    *  5 = Player 2 body
    *  6 = Wall  */
    public void displayTiles() {
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Tile tile = grid.getTiles()[x][y];

                if (tile.isOccupied()) {
                    switch(tile.getType()) {
                        case 1:
                            drawImage(head, x * tileSize, y * tileSize, tileSize, tileSize);
                            break;
                        case 2:
                            drawImage(body, x * tileSize, y * tileSize, tileSize, tileSize);
                            break;
                        case 3:
                            drawImage(apple, x * tileSize, y * tileSize, tileSize, tileSize);
                            break;
                        case 4:
                            drawImage(headP2, x * tileSize, y * tileSize, tileSize, tileSize);
                            break;
                        case 5:
                            drawImage(bodyP2, x * tileSize, y * tileSize, tileSize, tileSize);
                            break;
                        case 6:
                            drawImage(wall, x * tileSize, y * tileSize, tileSize, tileSize);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /* This function is used for key input. It is used to get menu selections, pausing the game and moving
    * Player 1 and 2. */
    @Override
    public void keyPressed(KeyEvent e) {
        /* ESC key used to go back to the previous menu. However if the game has started then it is used
           to toggle the isPaused boolean which pauses the game when true.*/
        if (e.getKeyCode() == 27) { //ESC key
            if (isOver) {
                return;
            }

            if (hasStarted) {
                playAudio(menuSelect);
                isPaused = !isPaused;
            } else {
                if (GAME_MODE > -1) {
                    playAudio(menuSelect);
                    GAME_MODE = -1;
                }
            }
        }

        // Stops any key input (besides ESC above) when the game is paused.
        if (isPaused) {
            return;
        }

        /* Takes input from the arrow keys when it is in two player mode. It uses the directionFromKey function
           to determine which direction to set the snake based on which arrow key they pressed. Also restarts the
            key counter.

            A counter is used to that the can't press the keys too quickly. In testing if there was no timer the
            player could attempt to turn around by would end up crashing in on them selves before the body parts
            have had a chance to move. */
        if (GAME_MODE == 2) {
            if (isDirectionalKey(e.getKeyCode(), 1)) {
                if (keyCounterP2 <= 0) {
                    Direction dir = getDirectionFromKey(e.getKeyCode(), snakeP2);
                    if (dir != null) {
                        snakeP2.setDirection(dir);
                        keyCounterP2 = 0.065;
                    }
                }
            }
        }

        /* Same as for the player 2 inputs by uses WASD instead. No need to check for gamemode as the player 1 is
           always present in each mode. */
        if (isDirectionalKey(e.getKeyCode(), 0)) {
            if (keyCounter <= 0) {
                Direction dir = getDirectionFromKey(e.getKeyCode(), snake);
                if (dir != null) {
                    snake.setDirection(dir);
                    keyCounter = 0.065;
                }
            }
        }

        // Input used for the start menu. Takes in number key inputs.
        if (GAME_MODE == -1) {
            if (e.getKeyChar() == '1') { //CLASSIC
                playAudio(menuSelect);
                speed = 0.07;
                GAME_MODE = 0;
            } else if (e.getKeyChar() == '2') { //HARDMODE
                playAudio(menuSelect);
                speed = 0.05;
                GAME_MODE = 1;
            } else if (e.getKeyChar() == '3') { //TWO PLAYER
                playAudio(menuSelect);
                speed = 0.07;
                GAME_MODE = 2;
            }
        }

        // Takes in the SPACE input. Used for continuing onto the next screen e.g. when the game ends.
        if (e.getKeyChar() == ' ') {
            if (GAME_MODE != -1 && !hasStarted) {
                playAudio(menuSelect);
                hasStarted = true;
                grid.clearTiles();
            } else if (isOver) {
                playAudio(menuSelect);
                endGame();
                isOver = false;
                isPaused = false;
                hasStarted = false;
                GAME_MODE = -1;
            }
        }
    }

    /* This function returns a boolean based on if the key is an action key based on the player
       e.g. player=0 key=W, A, S or D would return true, player=1 key=UP, DOWN, LEFT or RIGHT*/
    private boolean isDirectionalKey(int key, int player) {
        if (player == 0) {
            return key == 87 || key == 65 || key == 83 || key == 68;
        }

        return (key >= 37 && key <= 40);
    }

    //LEFT:37, UP:38, RIGHT:39, DOWN:40, W:87, A:65, S:83, D:68
    private Direction getDirectionFromKey(int key, Snake player) {
        Direction dir = null;

        if (key == 37 || key == 65) {
            dir = Direction.WEST;
        } else if (key == 38 || key == 87) {
            dir = Direction.NORTH;
        } else if (key == 39 || key == 68) {
            dir = Direction.EAST;
        } else if (key == 40 || key == 83) {
            dir = Direction.SOUTH;
        }

        if (!player.getDirection().isOppositeTo(dir)) {
            return dir;
        }

        return null;
    }
}