import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PP extends JPanel implements ActionListener, KeyListener {
    private int h, w;
    private Player leftPlayer;
    private Player rightPlayer;
    private Ball ball;
    private Timer gameLoop;

    public PP(int x, int y) {
        this.w = x;
        this.h = y;

        leftPlayer = new Player(15, h / 2 - 50, 10, 100, Color.RED);
        rightPlayer = new Player(w - 25, h / 2 - 50, 10, 100, Color.BLUE);
        ball = new Ball(w / 2, h / 2, 16);

        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(w, h));
        this.addKeyListener(this);
        this.setFocusable(true);

        // 60 FPS (1000ms / 60 = ~16ms)
        gameLoop = new Timer(16, this);
        gameLoop.start();
    }

    private class Player {
        int x, y, width, height, score, moveY;
        Color color;

        Player(int x, int y, int w, int h, Color c) {
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
            this.color = c;
            this.score = 0;
            this.moveY = 0;
        }

        void move() {
            y += moveY;
            if (y < 0)
                y = 0;
            if (y + height > h)
                y = h - height;
        }

        Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }

    private class Ball {
        int x, y, size;
        double speedX, speedY;

        Ball(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speedX = 5.0;
            this.speedY = 5.0;
        }

        void move() {
            x += (int) speedX;
            y += (int) speedY;
        }

        void accelerate() {

            speedX *= 1.1;
            speedY *= 1.1;

            // Opciono: Limitiraj maksimalnu brzinu da igra ne postane nemoguća
            if (Math.abs(speedX) > 20)
                speedX = (speedX > 0) ? 20 : -20;
        }

        Rectangle getBounds() {
            return new Rectangle(x, y, size, size);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Srednja linija
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0));
        g2.drawLine(w / 2, 0, w / 2, h);

        // Rezultat
        g2.setFont(new Font("Monospaced", Font.BOLD, 50));
        g2.setColor(Color.WHITE);
        g2.drawString(String.valueOf(leftPlayer.score), w / 2 - 70, 50);
        g2.drawString(String.valueOf(rightPlayer.score), w / 2 + 40, 50);

        // Igrači
        g2.setColor(leftPlayer.color);
        g2.fillRect(leftPlayer.x, leftPlayer.y, leftPlayer.width, leftPlayer.height);

        g2.setColor(rightPlayer.color);
        g2.fillRect(rightPlayer.x, rightPlayer.y, rightPlayer.width, rightPlayer.height);

        // Loptica
        g2.setColor(Color.WHITE);
        g2.fillOval(ball.x, ball.y, ball.size, ball.size);
    }

    private void resetBall() {
    ball.x = w / 2 - ball.size / 2;
    ball.y = h / 2 - ball.size / 2;
    ball.speedX = (ball.speedX > 0) ? -5.0 : 5.0; // Vraća na 5 i menja smer
    ball.speedY = 5.0;
}
    public void update() {
        leftPlayer.move();
        rightPlayer.move();
        ball.move();

        // Odbijanje od zidova
        if (ball.y <= 0 || ball.y + ball.size >= h) {
            ball.speedY *= -1;
        }

        // Sudar sa levim reketom
        if (ball.getBounds().intersects(leftPlayer.getBounds())) {
            ball.speedX = Math.abs(ball.speedX);
            ball.accelerate(); // DODATO: Loptica ubrzava
        }

        // Sudar sa desnim reketom
        if (ball.getBounds().intersects(rightPlayer.getBounds())) {
            ball.speedX = -Math.abs(ball.speedX);
            ball.accelerate(); // DODATO: Loptica ubrzava
        }

        // Golovi (brzina se resetuje u resetBall)
        if (ball.x < 0) {
            rightPlayer.score++;
            resetBall();
        } else if (ball.x > w) {
            leftPlayer.score++;
            resetBall();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W)
            leftPlayer.moveY = -8;
        if (key == KeyEvent.VK_S)
            leftPlayer.moveY = 8;
        if (key == KeyEvent.VK_UP)
            rightPlayer.moveY = -8;
        if (key == KeyEvent.VK_DOWN)
            rightPlayer.moveY = 8;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_S)
            leftPlayer.moveY = 0;
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
            rightPlayer.moveY = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}