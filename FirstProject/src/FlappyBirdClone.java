import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlappyBirdClone extends JFrame {

    private int birdY;
    private int velocity;
    private boolean isJumping;

    private List<Pipe> pipes;
    private Timer timer;

    private BufferedImage buffer;

    public FlappyBirdClone() {
        setTitle("Flappy Bird Clone");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        birdY = 200;
        velocity = 0;
        isJumping = false;
        pipes = new ArrayList<>();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    jump();
                }
            }
        });

        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        timer.start();

        generatePipes();
    }

    private void jump() {
        if (!isJumping) {
            velocity = -10;
            isJumping = true;
        }
    }

    private void update() {
        birdY += velocity;
        velocity += 1;

        if (birdY > getHeight()) {
            // Game over condition
            birdY = getHeight();
            velocity = 0;
            isJumping = false;
        }

        if (birdY < 0) {
            birdY = 0;
            velocity = 0;
        }

        isJumping = false;

        movePipes();
        checkCollision();
    }

    private void generatePipes() {
        Random random = new Random();
        int pipeGap = 150;
        int pipeWidth = 50;

        for (int i = 0; i < 5; i++) {
            int pipeHeight = random.nextInt(getHeight() - 2 * pipeGap) + pipeGap;

            Pipe upperPipe = new Pipe(getWidth() + i * 200, 0, pipeWidth, pipeHeight);
            Pipe lowerPipe = new Pipe(getWidth() + i * 200, pipeHeight + pipeGap, pipeWidth, getHeight() - pipeHeight - pipeGap);

            pipes.add(upperPipe);
            pipes.add(lowerPipe);
        }
    }

    private void movePipes() {
        for (Pipe pipe : pipes) {
            pipe.move();
        }

        if (pipes.get(0).getX() < -pipes.get(0).getWidth()) {
            pipes.remove(0);
            pipes.remove(0);

            generatePipes();
        }
    }

    private void checkCollision() {
        for (Pipe pipe : pipes) {
            if (pipe.intersects(50, birdY, 30, 30)) {
                gameOver();
                return;
            }
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        resetGame();
    }

    private void resetGame() {
        birdY = 200;
        velocity = 0;
        pipes.clear();
        generatePipes();
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        Graphics gBuffer = buffer.getGraphics();

        // Draw background
        gBuffer.setColor(Color.CYAN);
        gBuffer.fillRect(0, 0, getWidth(), getHeight());

        // Draw bird
        gBuffer.setColor(Color.YELLOW);
        gBuffer.fillRect(50, birdY, 30, 30);

        // Draw pipes
        gBuffer.setColor(Color.GREEN);
        for (Pipe pipe : pipes) {
            gBuffer.fillRect(pipe.getX(), pipe.getY(), pipe.getWidth(), pipe.getHeight());
        }

        // Draw offscreen buffer to the screen
        g.drawImage(buffer, 0, 0, this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlappyBirdClone().setVisible(true);
            }
        });
    }

    private class Pipe {
        private int x, y, width, height;
        private int speed = 5;

        public Pipe(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void move() {
            x -= speed;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public boolean intersects(int otherX, int otherY, int otherWidth, int otherHeight) {
            return x < otherX + otherWidth && x + width > otherX && y < otherY + otherHeight && y + height > otherY;
        }
    }
}
