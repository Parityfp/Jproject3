package Project3;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.List;


class game extends Canvas implements Runnable // implements KeyListener
{

    //if change dimensions do not forget to change other code that relies on it
    public static final int WIDTH = 300;
    public static final int HEIGHT = 450;
    public static final int SCALE = 2;
    public static String TITLE = "game";

    private boolean running = false;
    private Thread thread;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    private player p;

    //amount of bullets currently on screen
    private List<Bullet> bullets = new ArrayList<>();
    private boolean shooting = false;
    private int bulletCounter = 0;
    private final int bulletThreshold = 5; // rate of the player's bullet
    private int totalBulletsShot = 0;


    //player ship, temporary
    //private BufferedImage player;

    public void init(){
        //focuses on window instantly, no need to click on window to register key
        requestFocus();
        // try {
        //     player = ImageIO.read(getClass().getResource(MyConstants.FILE_SHIP));
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        p = new player((300 * 2) / 2, (450 * 2) - 32);
    }

    synchronized public void start() {
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

    //main method is not important here since we are launching the game through main.java
    public static void main(String args[]) {
        game game = new game();
        game.addKeyListener(new KeyInput(game));


        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        JFrame frame = new JFrame(game.TITLE);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        // frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        

        game.start();
    }

    @Override
    public void run() {
        init();
        final int TICKS_PER_SECOND = 60;
        final long TIME_PER_TICK = 1000000000 / TICKS_PER_SECOND;
        long lastTime = System.nanoTime();
        long now;
        long delta = 0;
    

        //game loop
        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime);
            lastTime = now;
    
            while (delta >= TIME_PER_TICK) {
                tick(); //tick
                delta -= TIME_PER_TICK;
            }
            
            render(); //render
        }
    
        stop();
    }

    private void tick() {
        p.tick();

        if (shooting) {
            // adjust the rate as needed
            if (bulletCounter % bulletThreshold == 0) {
                bullets.add(new Bullet(p.getX() + 9, p.getY()));
                totalBulletsShot++;
            }
            bulletCounter++;
        }
    
        //handles on-screen bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).tick();
            if (bullets.get(i).isOffScreen()) {
                bullets.remove(i);
                i--;
            }
        }
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

        p.render(g);

        for (Bullet bullet : bullets) {
            bullet.render(g);
        }


        ///////////////////////
        g.dispose();
        bs.show();
    }

    private boolean rightPressed = false;
    private boolean leftPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;


    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_W){
            p.setVelY(-5);
            upPressed = true;
        }else if(key == KeyEvent.VK_A){
            p.setVelX(-5);
            leftPressed = true;
        }else if(key == KeyEvent.VK_S){
            p.setVelY(5);
            downPressed = true;
        }else if(key == KeyEvent.VK_D){
            p.setVelX(5);
            rightPressed = true;
        }
        
        if (key == KeyEvent.VK_SPACE) {
            shooting = true;
        }
    
        updateVelocity();

    }
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
    
        if(key == KeyEvent.VK_A){
            leftPressed = false;
        }else if(key == KeyEvent.VK_D){
            rightPressed = false;
        }else if(key == KeyEvent.VK_W){
            upPressed = false;
        }else if(key == KeyEvent.VK_S){
            downPressed = false;
        }

        if (key == KeyEvent.VK_SPACE) {
            shooting = false;
        }
    

        updateVelocity();
    }
    private void updateVelocity() {
        if (leftPressed && !rightPressed) {
            p.setVelX(-5);
        } else if (rightPressed && !leftPressed) {
            p.setVelX(5);
        } else {
            p.setVelX(0);
        }

        if (upPressed && !downPressed) {
            p.setVelY(-5);
        } else if (downPressed && !upPressed) {
            p.setVelY(5);
        } else {
            p.setVelY(0);
        }
    }
}



//////////////////////////////////// PLAYER CLASS ////////////////////////////////////

class player {

    private double x;
    private double y;

    private double velX = 0;
    private double velY = 0;

    private BufferedImage player;



    public player(double x, double y){
        this.x = x;
        this.y = y;

        try {
            player = ImageIO.read(getClass().getResource(MyConstants.FILE_SHIP));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tick(){
        x += velX;
        y += velY;

        //collision bound with border
        if (x <= 0) x = 0;
        if (x >= (300 * 2) - 32) x = (300 * 2) - 32;
        if (y <= 0) y = 0;
        if (y >= (450 * 2) - 32) y = (450 * 2) - 32;

    }

    public void render(Graphics g){
        g.drawImage(player, (int)x, (int)y, null);
    }

    public void setVelX(double velX){
        this.velX = velX;
    }
    public void setVelY(double velY){
        this.velY = velY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}

//////////////////////////////////// BULLET CLASS ////////////////////////////////////
class Bullet {
    private double x;
    private double y;
    private double speed = 15.0;
    private BufferedImage bullet;

    public Bullet(double x, double y) {
        this.x = x;
        this.y = y;

        try {
            bullet = ImageIO.read(getClass().getResource(MyConstants.FILE_BULLET));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void tick() {
        y -= speed; // Move the bullet upwards
    }

    public void render(Graphics g) {
        g.drawImage(bullet, (int)x, (int)y, null);
    }

    public boolean isOffScreen() {
        return y < 0;
    }

}


//////////////////////////////////// KEYINPUT CLASS ////////////////////////////////////

class KeyInput extends KeyAdapter {

    game game;

    public KeyInput(game game){
        this.game = game;
    }

    public void keyPressed(KeyEvent e){
        game.keyPressed(e);
    }
    public void keyReleased(KeyEvent e){
        game.keyReleased(e);
    }
}
