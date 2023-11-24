package Project3;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.*;

class game extends Canvas implements Runnable // implements KeyListener
{
    public static final int WIDTH = 320;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 2;
    public static String TITLE = "game";

    private boolean running = false;
    private Thread thread;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    private synchronized void start() {
        if (running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        if (!running)
            return;

        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ;
        System.exit(1);
    }

    public static void main(String args[]) {
        game game = new game();

        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        JFrame frame = new JFrame(game.TITLE);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        // frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        new StartMenu();
        game.start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double fps = 60.0;
        double ns = 1000000000 / fps;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                updates++;
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(updates + " Ticks, Fps: " + frames);
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {

    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        ////////////////////////

        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

        ///////////////////////
        g.dispose();
        bs.show();
    }
}

class StartMenu extends JFrame {

    private JPanel contentpane;
    private StartButton startButton;

    public StartMenu() {
        setTitle("Start Menu");
        setBounds(200, 200, 700, 300);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        contentpane = (JPanel) getContentPane();
        contentpane.setLayout(null);

        startButton = new StartButton();
        contentpane.add(startButton);
        repaint();
    }
}

class StartButton extends JButton implements MouseListener {
    private int curX = 100, curY = 20;
    private int width = 100, height = 100;

    private ImageIcon startImage;

    public StartButton(ImageIcon icon) {
        String path = "src/main/Java/Project3/";
        startImage = new ImageIcon(path + "startButton.png");

        setBounds(curX, curY, width, height);
        setIcon(startImage);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("Game Started");
    }
}
