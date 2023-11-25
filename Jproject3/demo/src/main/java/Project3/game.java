package Project3;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.util.List;
import java.util.Random;
import java.util.Iterator;





class game extends JPanel implements Runnable // implements KeyListener
{

    //if change dimensions do not forget to change other code that relies on it
    public static final int WIDTH = 1366;
    //game is at 766 instead of 768, im sorry.
    public static final int HEIGHT = 768;
    public static String TITLE = "game";

    private boolean running = false;
    private Thread thread;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private ImageIcon bg;


    private player p;

    //amount of bullets currently on screen
    private List<Bullet> bullets = new ArrayList<>();
    private boolean shooting = false;
    private int bulletCounter = 0;
    private int gameTickCounter = 0;

    private final int bulletThreshold = 5; // rate of the player's bullet
    private int totalBulletsShot = 0;

    //same thing for enemies
    private List<enemy> enemies = new ArrayList<>();
    private int enemySpawnCounter = 0;
    private final int enemySpawnThreshold = 120; 
    private void addEnemy(double x, double y) {
        enemies.add(new enemy(x, y));
    }
    

    public void init(){
        //focuses on window instantly, no need to click on window to register key

        requestFocus();
        p = new player(WIDTH / 2, HEIGHT - 32);
        bg = new ImageIcon(getClass().getResource(MyConstants.FILE_BG));
        enemies = new ArrayList<>();
        
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
        };

        System.exit(0);
    }

    public void setRunning(boolean running) {
        this.running = running;
        try {
            if (!running && thread != null) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }


    private long lastSoundTime = 0;
    private final long SOUND_COOLDOWN = 160; 
    private synchronized void SFX(String soundFileName) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSoundTime > SOUND_COOLDOWN) {
            lastSoundTime = currentTime;
            try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(soundFileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.setMicrosecondPosition(0);
            clip.start();
        } catch (Exception e) {e.printStackTrace(); }
        }
    }

    //main method is only for testing here since we are launching the game through main.java
    public static void main(String args[]) {
        game game = new game();
        game.addKeyListener(new KeyInput(game));


        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        JFrame frame = new JFrame(game.TITLE);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.setRunning(false);
            }
        });
        

        game.start();
    }

    @Override
    public void run() {
        System.out.println("Run method started"); 
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
            
            repaint(); //render
        }
        System.out.println("Game Tick Counter: " + gameTickCounter);
        System.out.println("Bullet Counter: " + bulletCounter);
        System.out.println("Total Bullets Shot: " + totalBulletsShot);

        stop();
        
    }

    private void tick() {
        p.tick();
        gameTickCounter++;

        if (shooting) {
            // adjust the rate as needed
            if (bulletCounter % bulletThreshold == 0) {
                bullets.add(new Bullet(p.getX() + 9, p.getY()));
                totalBulletsShot++;
                SFX(MyConstants.FILE_SHOOT);
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

        //handles on-screen enemies
        for (enemy enemy : enemies) {
            enemy.tick();
        }

        enemySpawnCounter++;
        if (enemySpawnCounter >= enemySpawnThreshold) {
            addEnemy(new Random().nextDouble() * (WIDTH - 50), 0); // Example: spawn at a random x position at the top
            enemySpawnCounter = 0;
        }

        // Update and remove enemies
        Iterator<enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            enemy enemy = iterator.next();
            enemy.tick();
            if (enemy.isOffScreen()) {
                iterator.remove();
            }
        }


        //collision handling
        for (enemy e : enemies) {
            if (p.getBounds().intersects(e.getBounds())) {
                // Handle collision between player and enemy
                System.out.println("player hit, GAME OVER");
                running = false;
                return;
            }
    
            for (Bullet b : bullets) {
                if (b.getBounds().intersects(e.getBounds())) {
                    // Handle collision between bullet and enemy
                    // Remove the enemy and the bullet, or mark them for removal
                    System.out.println("enemy hit");
                    SFX(MyConstants.FILE_HIT);
                }
            }
        } 

        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
    
            // Reverse loop for enemies
            for (int j = enemies.size() - 1; j >= 0; j--) {
                enemy e = enemies.get(j);
    
                if (b.getBounds().intersects(e.getBounds())) {
                    e.hit(); // Enemy has been hit
    
                    if (e.isDestroyed()) {
                        enemies.remove(j); // Remove the enemy if it's destroyed
                    }
    
                    bullets.remove(i); // Remove the bullet
                    System.out.println("enemy hit");
    
                    // Break out of the enemies loop since the bullet is removed
                    break;
                }
            }
        }
    


    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        // Draw the background image
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

        int startX = (WIDTH - 666) / 2;
        int startY = (HEIGHT - 666) / 2;

        // Draw the GIF
        g.drawImage(bg.getImage(), startX, startY, 666, 666, this);

    
        // Render the player
        if (p != null) {
            p.render(g);
        }
    
        // Render bullets
        for (Bullet bullet : bullets) {
            if (bullet != null) {
                bullet.render(g);
            }
        }

        //render enemies
        for (enemy enemy : enemies) {
            if (enemy != null) {
                enemy.render(g);
            }
        }
    
    
        Toolkit.getDefaultToolkit().sync(); // Uncomment this if there are any rendering issues
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

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, player.getWidth(), player.getHeight());
    }

    public void tick(){
        x += velX;
        y += velY;

        //collision bound with border
        if (x <= 0 + 350) x = 0 + 350;
        if (x >= (1366 - 350) - 32) x = (1366 - 350) - 32;
        if (y <= 0 + 50) y = 0 + 50;
        if (y >= 766 - 50 - 32) y = 766 - 50 - 32;

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
//////////////////////////////////// ENEMY CLASS ////////////////////////////////////

class enemy{
    private double x, y;
    private double initialX;
    private double speedY = 2; // Speed of enemy moving down
    private double amplitude = 20; // Amplitude of the sine wave
    private double frequency = 0.05; // Frequency of the sine wave
    private BufferedImage enemyImage;
    private final int hitThreshold = 5; 
    private int hitCount = 0;
    private boolean destroyed = false;

    public enemy(double x, double y){
        this.x = x;
        this.y = y;
        this.initialX = x;


        try {
            enemyImage = ImageIO.read(getClass().getResource(MyConstants.FILE_ALIEN1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, enemyImage.getWidth(), enemyImage.getHeight());
    }

    public void tick(){

        //if we want crazy teleporting enemies (for Lunatic difficulty), just add X = 350 + new Random().nextInt(game.WIDTH - 700); to some conditions below
        //enemies do not get removed once off screen, instead they respawn at a random spot at the top or you could say "their friend replaced them"
        y += speedY;
        x = initialX + amplitude * Math.sin(frequency * y);
        if (x <= 0 + 350) x = 0 + 350;
        if (x >= (1366 - 350) - 64) x = (1366 - 350) - 64;
        if (y <= 0 + 50) y = 0 + 50;
        if (y >= 766 - 50){
            y = 0;
            initialX = 350 + new Random().nextInt(game.WIDTH - 700);
        } 
    }

    public void render(Graphics g){
        if (enemyImage != null) {
            g.drawImage(enemyImage, (int)x, (int)y, null);
        }
    }

    //handles enemy getting hit, marking it to be destroyed
    public void hit() {
        hitCount++;
        if (hitCount >= hitThreshold) {
            destroyed = true;
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean isOffScreen() {
        return y > game.HEIGHT;
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

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, bullet.getWidth(), bullet.getHeight());
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
