import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class PongGame extends JFrame implements KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private int paddle1Y = HEIGHT / 2 - 50;
    private int paddle2Y = HEIGHT / 2 - 50;
    private int ballX = WIDTH / 2 - 15;
    private int ballY = HEIGHT / 2 - 15;
    private int ballXSpeed = -5;
    private int ballYSpeed = 5;

    private BufferedImage buffer;

    public PongGame() {
        setTitle("Pong Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addKeyListener(this);

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        timer.start();
    }

    private void update() {
        // Update ball position
        ballX += ballXSpeed;
        ballY += ballYSpeed;

        // Ball collisions with walls
        if (ballY <= 0 || ballY >= HEIGHT - 30) {
            ballYSpeed = -ballYSpeed;
        }

        // Ball collisions with paddles
        if (ballX <= 50 && ballY + 30 >= paddle1Y && ballY <= paddle1Y + 100) {
            ballXSpeed = -ballXSpeed;
        }
        if (ballX >= WIDTH - 80 && ballY + 30 >= paddle2Y && ballY <= paddle2Y + 100) {
            ballXSpeed = -ballXSpeed;
        }

        // Check for scoring
        if (ballX < 0 || ballX > WIDTH) {
            ballX = WIDTH / 2 - 15;
            ballY = HEIGHT / 2 - 15;
            ballXSpeed = -ballXSpeed;
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics gBuffer = buffer.getGraphics();

        // Draw to offscreen buffer
        gBuffer.setColor(Color.CYAN);
        gBuffer.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw paddles
        gBuffer.setColor(Color.WHITE);
        gBuffer.fillRect(50, paddle1Y, 20, 100);
        gBuffer.fillRect(WIDTH - 70, paddle2Y, 20, 100);

        // Draw ball
        gBuffer.setColor(Color.WHITE);
        gBuffer.fillOval(ballX, ballY, 30, 30);

        // Draw center line
        gBuffer.setColor(Color.WHITE);
        for (int i = 0; i < HEIGHT; i += 50) {
            gBuffer.fillRect(WIDTH / 2 - 5, i, 10, 30);
        }

        // Draw offscreen buffer to the screen
        g.drawImage(buffer, 0, 0, this);

        Toolkit.getDefaultToolkit().sync();  // Sync the display on some systems to avoid tearing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && paddle2Y > 0) {
            paddle2Y -= 10;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && paddle2Y < HEIGHT - 100) {
            paddle2Y += 10;
        }
        if (e.getKeyCode() == KeyEvent.VK_W && paddle1Y > 0) {
            paddle1Y -= 10;
        }
        if (e.getKeyCode() == KeyEvent.VK_S && paddle1Y < HEIGHT - 100) {
            paddle1Y += 10;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PongGame().setVisible(true));
    }
}