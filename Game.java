/* REFERENCE:
    https://codereview.stackexchange.com/questions/129719/java-snake-game  
Code is copied from the above link and done some changes

Package Declearation...  */
package javaapplication33;

//IMPORT JAVA Built-in Functions for GUI, GRAPHICS & EVENT HANDLING 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.EventQueue;
import javax.swing.JFrame;


class Snake {   //Snake Class for MAking the Body of Snake 

    // Stores the joints / body part locations for our snake
    // Here we have used ARRAY Data Structure...
    private final int[] x = new int[Board.getAllDots()];
    private final int[] y = new int[Board.getAllDots()];

    // To stores direction of our snake
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean movingUp = false;
    private boolean movingDown = false;

    private int joints = 0; // Stores # of dots / joints the snake has (starts
                            // with 3)

    public int getSnakeX(int index) {
        return x[index];
    }

    public int getSnakeY(int index) {
        return y[index];
    }

    public void setSnakeX(int i) {
        x[0] = i;
    }

    public void setSnakeY(int i) {
        y[0] = i;
    }

    //Making and Setting Functions for Moving Left, Right, Up & Down
    public boolean isMovingLeft() {
        return movingLeft;
    }
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }
    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public boolean isMovingUp() {
        return movingUp;
    }
    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public boolean isMovingDown() {
        return movingDown;
    }
    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public int getJoints() {
        return joints;
    }
    public void setJoints(int j) {
        joints = j;
    }

    public void move() {
        for (int i = joints; i > 0; i--) {

        // Moves the joints of the snake 'up the chain'
        // Meaning, the joint of the snake all move up one
        x[i] = x[(i - 1)];
        y[i] = y[(i - 1)];
    }

    // Moves snake to the left
    if (movingLeft) {
        x[0] -= Board.getDotSize();
    }
    // To the right
    if (movingRight) {
        x[0] += Board.getDotSize();
    }
    // Down
    if (movingDown) {
        y[0] += Board.getDotSize();
    }
    // And finally up
    if (movingUp) {
        y[0] -= Board.getDotSize();
    }

    // Dotsize represents the size of the joint, so a pixel of DOTSIZE
    // gets added on to the snake in that direction
     }
}


class Food {  //Food Class for creating food on board

    private Snake snake = new Snake();  //Instances of our Snake for its methods
    private int foodX; // Stores X pos of our food
    private int foodY; // Stores Y pos of our food

    // Used to determine random position of food
    private final int RANDOMPOSITION = 10;

    public void createFood() {
        // Set our food's x & y position to a random position

        int location = (int) (Math.random() * RANDOMPOSITION);
        foodX = ((location * Board.getDotSize()));

        location = (int) (Math.random() * RANDOMPOSITION);
        foodY = ((location * Board.getDotSize()));

        if ((foodX == snake.getSnakeX(0)) && (foodY == snake.getSnakeY(0))) {
            createFood();
        }
    }

    public int getFoodX() {
        return foodX;
    }

    public int getFoodY() {
        return foodY;
    }
}




@SuppressWarnings("serial")
class Board extends JPanel implements ActionListener {

    // TODO: Implement a way for the player to win
    // Holds height and width of the window
    private final static int BOARDWIDTH = 800;
    private final static int BOARDHEIGHT = 700;

    // Used to represent pixel size of food & our snake's joints
    private final static int PIXELSIZE = 20;

    /* The total amount of pixels the game could possibly have.
     We don't want less, because the game would end prematurely.
     We don't more because there would be no way to let the player win. */

    private final static int TOTALPIXELS = (BOARDWIDTH * BOARDHEIGHT)/(PIXELSIZE * PIXELSIZE);

    // Check to see if the game is running
    private boolean inGame = true;

    // Timer used to record tick times
    private Timer timer;

    /* Used to set game speed, the lower the #, the faster the snake travels which in turn
    makes the game harder.  */
    private static int speed = 45;

    // Instances of our snake & food so we can use their methods
    private Snake snake = new Snake();
    private Food food = new Food();

    public Board() {

        addKeyListener(new Keys());
        setBackground(Color.BLACK);
        setFocusable(true);

        setPreferredSize(new Dimension(BOARDWIDTH, BOARDHEIGHT));

        initializeGame();
    }

    // Used to paint our components to the screen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Draw our Snake & Food (Called on repaint()).
    void draw(Graphics g) {
    // Only draw if the game is running / the snake is alive
    if (inGame == true) {
        g.setColor(Color.MAGENTA);
        g.fillRect(food.getFoodX(), food.getFoodY(), PIXELSIZE, PIXELSIZE); // food

        // Draw our snake.
        for (int i = 0; i < snake.getJoints(); i++) {
            // Snake's head
            if (i == 0) {
                g.setColor(Color.CYAN);
                g.fillRect(snake.getSnakeX(i), snake.getSnakeY(i),
                        PIXELSIZE, PIXELSIZE);
                // Body of snake
            } else {
                g.fillRect(snake.getSnakeX(i), snake.getSnakeY(i),
                        PIXELSIZE, PIXELSIZE);
            }
        }

        // Sync our graphics together
        Toolkit.getDefaultToolkit().sync();
        } else {
            // If we're not alive, then we end our game
            endGame(g);
        }
    }

    void initializeGame() {
        snake.setJoints(3); // set our snake's initial size

        // Create our snake's body
        for (int i = 0; i < snake.getJoints(); i++) {
           snake.setSnakeX(BOARDWIDTH / 2);
           snake.setSnakeY(BOARDHEIGHT / 2);
        }
        // Start off our snake moving right
        snake.setMovingRight(true);

        // Generate our first 'food'
        food.createFood();

        // set the timer to record our game's speed / make the game move
        timer = new Timer(speed, this);
        timer.start();
    }

    // if our snake is in the close proximity of the food..
    void checkFoodCollisions() {

    if ((proximity(snake.getSnakeX(0), food.getFoodX(), 20))
            && (proximity(snake.getSnakeY(0), food.getFoodY(), 20))) {

        System.out.println("intersection");
        // Add a 'joint' to our snake
        snake.setJoints(snake.getJoints() + 1);
        // Create new food
        food.createFood();
    }
}

    // Used to check collisions with snake's self and board edges
    void checkCollisions() {

        // If the snake hits its' own joints..
        for (int i = snake.getJoints(); i > 0; i--) {

            // Snake cant intersect with itself if it's not larger than 5
            if ((i > 5) && (snake.getSnakeX(0) == snake.getSnakeX(i) && (snake
                        .getSnakeY(0) == snake.getSnakeY(i)))) {
                inGame = false; // then the game ends
            }
        }

        // If the snake intersects with the board edges..
        if (snake.getSnakeY(0) >= BOARDHEIGHT) {
            inGame = false;
        }

        if (snake.getSnakeY(0) < 0) {
            inGame = false;
        }

        if (snake.getSnakeX(0) >= BOARDWIDTH) {
            inGame = false;
        }

        if (snake.getSnakeX(0) < 0) {
            inGame = false;
        }

        // If the game has ended, then we can stop our timer
        if (!inGame) {
            timer.stop();
        }
    }

    void endGame(Graphics g) {

        // Create a message telling the player the game is over
        String message = "!!!..GAME OVER..!!!";

        // Create a new font instance
        Font font = new Font("Times New Roman", Font.BOLD, 30);
        FontMetrics metrics = getFontMetrics(font);

        // Set the color of the text to red, and set the font
        g.setColor(Color.ORANGE);
        g.setFont(font);

        // Draw the message to the board
        g.drawString(message, (BOARDWIDTH - metrics.stringWidth(message)) / 2,
            BOARDHEIGHT / 2);

        System.out.println("Game Ended");
    }

    // Run constantly as long as we're in game.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame == true) {

            checkFoodCollisions();
            checkCollisions();
            snake.move();

            System.out.println(snake.getSnakeX(0) + " " + snake.getSnakeY(0)
                + " " + food.getFoodX() + ", " + food.getFoodY());
        }
        // Repaint or 'render' our screen
        repaint();
    }

    private class Keys extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!snake.isMovingRight())) {
                snake.setMovingLeft(true);
                snake.setMovingUp(false);
                snake.setMovingDown(false);
            }

            if ((key == KeyEvent.VK_RIGHT) && (!snake.isMovingLeft())) {
                snake.setMovingRight(true);
                snake.setMovingUp(false);
                snake.setMovingDown(false);
            }

            if ((key == KeyEvent.VK_UP) && (!snake.isMovingDown())) {
                snake.setMovingUp(true);
                snake.setMovingRight(false);
                snake.setMovingLeft(false);
            }

            if ((key == KeyEvent.VK_DOWN) && (!snake.isMovingUp())) {
                snake.setMovingDown(true);
                snake.setMovingRight(false);
                snake.setMovingLeft(false);
            }

            if ((key == KeyEvent.VK_ENTER) && (inGame == false)) {

                inGame = true;
                snake.setMovingDown(false);
                snake.setMovingRight(false);
                snake.setMovingLeft(false);
                snake.setMovingUp(false);

                initializeGame();
            }
        }
    }

    private boolean proximity(int a, int b, int closeness) {
        return Math.abs((long) a - b) <= closeness;
    }

    public static int getAllDots() {
        return TOTALPIXELS;
    }

    public static int getDotSize() {
        return PIXELSIZE;
    }
}

public class Game extends JFrame {

    Game() {
        add(new Board());
        setResizable(false);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        // Creates a new thread so our GUI can process itself
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new Game();
                frame.setVisible(true);
            }
        });
    }
}
