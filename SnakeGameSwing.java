import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGameSwing extends JPanel {
    private static final int TILE_SIZE = 25; // Size of each tile
    private static final int GRID_WIDTH = 20; // Number of columns
    private static final int GRID_HEIGHT = 20; // Number of rows
    private static final int GAME_SPEED = 200; // Speed in milliseconds

    private LinkedList<Point> snake = new LinkedList<>(); // Snake body
    private Point food; // Food position
    private int direction = KeyEvent.VK_RIGHT; // Initial direction
    private boolean gameOver = false; // Game over flag

    public SnakeGameSwing() {
        setPreferredSize(new Dimension(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);

        // Initialize game
        initGame();

        // Add key listener for controls
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) return;

                int newDirection = e.getKeyCode();
                if ((newDirection == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) ||
                    (newDirection == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) ||
                    (newDirection == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) ||
                    (newDirection == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP)) {
                    direction = newDirection;
                }
            }
        });

        // Timer for game loop
        Timer timer = new Timer(GAME_SPEED, e -> gameTick());
        timer.start();
    }

    private void initGame() {
        // Initialize snake at the center
        snake.clear();
        snake.add(new Point(GRID_WIDTH / 2, GRID_HEIGHT / 2));

        // Place the first food
        placeFood();
    }

    private void placeFood() {
        Random random = new Random();
        do {
            food = new Point(random.nextInt(GRID_WIDTH), random.nextInt(GRID_HEIGHT));
        } while (snake.contains(food)); // Ensure food doesn't overlap with the snake
    }

    private void gameTick() {
        if (gameOver) return;

        // Move the snake
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case KeyEvent.VK_LEFT: newHead.x--; break;
            case KeyEvent.VK_RIGHT: newHead.x++; break;
            case KeyEvent.VK_UP: newHead.y--; break;
            case KeyEvent.VK_DOWN: newHead.y++; break;
        }

        // Check collisions
        if (newHead.x < 0 || newHead.x >= GRID_WIDTH || newHead.y < 0 || newHead.y >= GRID_HEIGHT || snake.contains(newHead)) {
            gameOver = true;
            repaint();
            return;
        }

        // Add new head
        snake.addFirst(newHead);

        // Check if the snake eats food
        if (newHead.equals(food)) {
            placeFood(); // Place new food
        } else {
            snake.removeLast(); // Remove tail
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the snake
        for (Point p : snake) {
            g.setColor(Color.GREEN);
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw the food
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw "Game Over" message
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String message = "Game Over!";
            FontMetrics metrics = g.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g.drawString(message, x, y);
            
        }
    }
    

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGameSwing game = new SnakeGameSwing();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
