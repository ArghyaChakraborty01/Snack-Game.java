import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private static final int TILE_SIZE = 30;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    private static final int BOARD_SIZE = WIDTH * HEIGHT;
    private static final int INITIAL_LENGTH = 4;
    private static final int DELAY = 120; // Milliseconds

    private enum Direction {UP, DOWN, LEFT, RIGHT}

    private boolean running;
    private Direction direction;
    private ArrayList<Point> snake;
    private Point food;
    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBackground(Color.BLACK);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != Direction.DOWN) direction = Direction.UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != Direction.UP) direction = Direction.DOWN;
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != Direction.RIGHT) direction = Direction.LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != Direction.LEFT) direction = Direction.RIGHT;
                        break;
                }
            }
        });

        snake = new ArrayList<>();
        direction = Direction.RIGHT;
        initGame();

        timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    moveSnake();
                    checkCollision();
                    checkFood();
                    repaint();
                }
            }
        });
        timer.start();
    }

    private void initGame() {
        snake.clear();
        for (int i = 0; i < INITIAL_LENGTH; i++) {
            snake.add(new Point(WIDTH / 2 - i, HEIGHT / 2));
        }
        spawnFood();
        running = true;
    }

    private void moveSnake() {
        Point head = (Point) snake.get(0).clone();
        switch (direction) {
            case UP:
                head.translate(0, -1);
                break;
            case DOWN:
                head.translate(0, 1);
                break;
            case LEFT:
                head.translate(-1, 0);
                break;
            case RIGHT:
                head.translate(1, 0);
                break;
        }

        if (head.equals(food)) {
            snake.add(0, food);
            spawnFood();
        } else {
            snake.add(0, head);
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT || snake.subList(1, snake.size()).contains(head)) {
            running = false;
            timer.stop();
        }
    }

    private void checkFood() {
        if (snake.get(0).equals(food)) {
            snake.add(food);
            spawnFood();
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        while (snake.contains(food)) {
            food = new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (running) {
            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        } else {
            g.setColor(Color.black);
            g.drawString("Game Over", WIDTH * TILE_SIZE / 2 - 30, HEIGHT * TILE_SIZE / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SnakeGame().setVisible(true);
            }
        });
    }
}