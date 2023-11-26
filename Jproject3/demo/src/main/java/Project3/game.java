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
    public static final int HEIGHT = 766;
    public static String TITLE = "game";

    private boolean running = false;
    private Thread thread;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private ImageIcon bg;

    private JLabel pointsLabel;

    private player p;

    //amount of bullets currently on screen
    private List<Bullet> bullets = new ArrayList<>();
    private boolean shooting = false;
    private int bulletCounter = 0;
    private int gameTickCounter = 0;

    private final int bulletThreshold = 5; // rate of the player's bullet
    private int totalBulletsShot = 0;

    //same thing for items
    private List<item> items = new ArrayList<>();


    //same thing for enemies
    private List<Enemy> enemies = new ArrayList<>();
    private int enemySpawnCounter = 0;
    private final int enemySpawnThreshold = 120; 
    private boolean shootingEnemyActive = false;
    private void addEnemy(double x, double y, String enemyType) {
        Enemy newEnemy;
        switch (enemyType) {
            case "shootingEnemy":
                newEnemy = new shootingEnemy(x, y, bullets);
                break;
            default:
                newEnemy = new DefaultEnemy(x, y);
                break;
        }
        enemies.add(newEnemy);
    }
    

    public void init(){
        //focuses on window instantly, no need to click on window to register key
        requestFocus();

        //should make a method for this
        pointsLabel = new JLabel("");
        pointsLabel.setForeground(Color.WHITE); 
        pointsLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        pointsLabel.setBounds(WIDTH - 340, 45, 180, 30);
        
        this.setLayout(null); // null layout for absolute positioning
        this.add(pointsLabel);

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
        System.out.println("Points: " + p.getPoints());

        stop();
        
    }

    private void tick() {
        p.tick();
        gameTickCounter++;

        if (shooting) {
            // adjust the rate as needed
            if (bulletCounter % bulletThreshold == 0) {
                bullets.add(new playerBullet(p.getX() + 9, p.getY()));
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
        for (Enemy enemy : enemies) {
            enemy.tick();
        }
        enemySpawnCounter++;
        if (enemySpawnCounter >= enemySpawnThreshold) {
            addEnemy(new Random().nextDouble() * (WIDTH - 50), 0, "DefaultEnemy"); // Example: spawn at a random x position at the top
            if (!shootingEnemyActive) {
                addEnemy(new Random().nextDouble() * (WIDTH - 50), 0, "shootingEnemy");
                shootingEnemyActive = true;
            }
    
            enemySpawnCounter = 0;
        }

        // Update and remove enemies
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.tick();
            if (enemy.isOffScreen()) {
                iterator.remove();
            }
        }

        //item updating
        for (int i = 0; i < items.size(); i++) {
            item it = items.get(i);
            it.tick();
            if(it.isOffScreen()){
                items.remove(i);
                i--; 
            }
            // Optionally, remove the item if it goes off-screen
        }
    


        //collision handling
        for (Enemy e : enemies) {
            if (p.getBounds().intersects(e.getBounds())) {
                // Handle collision between player and enemy
                System.out.println("player hit, GAME OVER");
                running = false;
                return;
            }
    
            for (Bullet b : bullets) {
                if (b.getBounds().intersects(e.getBounds()) && !b.isEnemyBullet) {
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
                Enemy e = enemies.get(j);
    
                if (b.getBounds().intersects(e.getBounds())) {
                    if(!b.isEnemyBullet){
                        e.hit(); // Enemy has been hit
                        bullets.remove(i); // Remove the bullet
                    }
                    if (e.isDestroyed()) {
                        int enemyType = 0;
                        if(e instanceof shootingEnemy){
                            shootingEnemyActive = false;
                            enemyType = 1;
                        } 
                        items.add(new point(e.getX(), e.getY(), enemyType));
                        enemies.remove(j); // Remove the enemy if it's destroyed
                    }
    
                    
                    System.out.println("enemy hit");
    
                    // Break out of the enemies loop since the bullet is removed
                    break;
                }
            }
        }

        //item collision with player
        for (int i = 0; i < items.size(); i++) {
            item it = items.get(i);
            it.tick();
            if (p.getBounds().intersects(it.getBounds())) {
                // Increase player's points, can add if or case statement for how much to increase depending on the enmemy type
                switch (it.getEnemyType()) {
                    case 0:
                        p.addPoints(1000);
                        pointsLabel.setText("" + p.getPoints());
                        break;
                    case 1:
                        p.addPoints(10000);
                        pointsLabel.setText("" + p.getPoints());
                        break;
                    default:
                        p.addPoints(100);
                        pointsLabel.setText("" + p.getPoints());
                        break;
                }
        
                items.remove(i);
                i--;
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

        // Draw the GIF TODO: THIS LINE CAUSES ERROR IN TERMINAL BUT WORKS
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

        //render items
        for (item it : items) {
            it.render(g);
        }
    
        //render enemies
        for (Enemy enemy : enemies) {
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

    private int points = 0;
    public void addPoints(int amount) {
        points += amount;
    }
    public int getPoints(){
        return points;
    }


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
        return new Rectangle((int)x, (int)y, player.getWidth() - 10, player.getHeight() - 10);
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


abstract class Enemy {
    protected double x, y;
    protected BufferedImage enemyImage;
    protected Rectangle bounds;
    protected int hitCount = 1;
    protected boolean destroyed = false;
    protected List<Bullet> bulletList;


    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
        // Common initialization
    }

    public abstract void tick(); // Each enemy type can have its own implementation

    public void render(Graphics g) {
        if (enemyImage != null) {
            g.drawImage(enemyImage, (int)x, (int)y, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, enemyImage.getWidth()-10, enemyImage.getHeight()-10);
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    

    public boolean isDestroyed() {
        return destroyed;
    }

    public boolean isOffScreen() {
        return y > game.HEIGHT;
    }

    public void hit() {
        hitCount++;
    }
}

class DefaultEnemy extends Enemy {
    private double initialX;
    private double speedY = 2; 
    private double amplitude = 20;
    private double frequency = 0.05;
    private final int hitThreshold = 5; 

    public DefaultEnemy(double x, double y) {
        super(x, y);
        initialX = x;
        // Load specific image for this enemy type
        try {
            enemyImage = ImageIO.read(getClass().getResource(MyConstants.FILE_ALIEN1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
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

    @Override
    public void hit() {
        hitCount++;
        if (hitCount >= hitThreshold) {
            destroyed = true;
        }
    }
}

class shootingEnemy extends Enemy{
    private int shootCooldown = 100;
    private int currentCooldown = 0;
    private final int hitThreshold = 35; 
    private double velX = 1.5;

    public shootingEnemy(double x, double y, List<Bullet> bulletList) {
        super(x, y);
        this.bulletList = bulletList;
        try {
            enemyImage = ImageIO.read(getClass().getResource(MyConstants.FILE_ALIEN2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
        // Enemy movement logic, probably a linear function
        y+=1;
        x+=velX;
        if (currentCooldown <= 0) {
            shoot();
            currentCooldown = shootCooldown;
        } else {
            currentCooldown--;
        }
        if (x <= 0 + 350) {
            x = 0 + 350;
            velX = -velX;
        }
        if (x >= (1366 - 350) - 64) {
            x = (1366 - 350) - 64;
            velX = -velX;
        }
        if (y <= 0 + 50) y = 0 + 50;
        if (y >= 766 - 50){
            y = 0;
            x = 350 + new Random().nextInt(game.WIDTH - 700);
        }
    }

    private void shoot() {
    int numberOfBullets = 20;
    double bulletSpeed = 5; // Speed of bullets
    double spreadAngle = Math.PI * 2; // Total angle of spread
    //direction
    double startAngle = Math.PI / 2 - spreadAngle / 2; 

    System.out.println("Shooting Bullets:");
    for (int i = 0; i < numberOfBullets; i++) {
        double angle = startAngle + i * (spreadAngle / (numberOfBullets - 1));
        double dx = Math.cos(angle);
        double dy = Math.sin(angle);
        Bullet newBullet = new enemyBullet(x, y, bulletSpeed, dx, dy);
        bulletList.add(newBullet);

        // Debugging print statements
        System.out.println("Bullet " + (i + 1) + ": Angle = " + angle + ", dx = " + dx + ", dy = " + dy);
    }
}

    @Override
    public void hit() {
        hitCount++;
        if (hitCount >= hitThreshold) {
            destroyed = true;
        }
    }
}

//////////////////////////////////// BULLET CLASS ////////////////////////////////////
abstract class Bullet {
    protected double x, y, dx; // X direction
    protected double dy; // Y direction
    protected double speed = 15.0;
    protected BufferedImage bullet;
    protected boolean isEnemyBullet;

    public Bullet(double x, double y, double speed, double dx, double dy, boolean isEnemyBullet) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.dx = dx;
        this.dy = dy;
        this.isEnemyBullet = isEnemyBullet;
    }
    public Bullet(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, bullet.getWidth(), bullet.getHeight());
    }

    public void tick() {
        y -= speed; // Moves the bullet upwards
    }

    public void render(Graphics g) {
        g.drawImage(bullet, (int)x, (int)y, null);
    }

    public boolean isOffScreen() {
        return y < 0;
    }

    protected void setBulletImage(String imagePath) {
        try {
            bullet = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class playerBullet extends Bullet{

    public playerBullet(double x, double y) {
        super(x, y);
        setBulletImage(MyConstants.FILE_BULLET); 
        }
}


class enemyBullet extends Bullet{

    public enemyBullet(double x, double y, double speed, double dx, double dy) {
        super(x, y, speed, dx, dy, true);
        setBulletImage(MyConstants.FILE_ENEMYBULLET2); 
    }

    @Override
    public void tick() {
        x += dx * speed;
        y += dy * speed;
        System.out.println("Bullet moving to x: " + x + ", y: " + y);
    }

    @Override
    public boolean isOffScreen() {
        return y > game.HEIGHT - 50 || y < 0 + 50 || x < 0 + 350 || x > game.WIDTH - 350;
    }
}

//////////////////////////////////// ITEM CLASS ////////////////////////////////////

abstract class item{
    protected BufferedImage item;
    protected int enemyType;
    protected double x;
    protected double y;
    public item(double x, double y, int enemyType) {
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;

    }

    public void tick() {
        y++;
    }

    public void render(Graphics g) {
        g.drawImage(item, (int)x, (int)y, null);
    }

    protected void setItemImage(String imagePath) {
        try {
            item = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, item.getWidth(), item.getHeight());
    }
    public int getEnemyType(){
        return enemyType;
    }

    public boolean isOffScreen() {
        return y > game.HEIGHT - 50 || y < 0 + 50 || x < 0 + 350 || x > game.WIDTH - 350;
    }
}

class point extends item{

    public point(double x, double y, int enemyType) {
        super(x, y, enemyType);
        setItemImage(MyConstants.FILE_POINT); 
    }
    @Override
    public void tick(){
        y++;
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
