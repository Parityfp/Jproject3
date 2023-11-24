package Project3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class StartMenu extends JFrame {

    private JPanel contentPane;
    private StartButton startButton;

    public StartMenu() {
        setTitle("Start Menu");
        setBounds(200, 200, 700, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        startButton = new StartButton(null);
        contentPane.add(startButton);

        setPreferredSize(new Dimension(700, 300));
        
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartMenu();
        });
    }
}

class StartButton extends JButton implements MouseListener {

    private int curX = 100, curY = 20;
    private int width = 100, height = 100;

    private ImageIcon startImage;
    private game gameInstance;

    public StartButton(game gameInstance) {
        // Load image from the resources
        String path = "src/main/java/Project3/";
        startImage = new ImageIcon(getClass().getResource("startButton.png"));

        setBounds(curX, curY, width, height);
        setIcon(startImage);

        addMouseListener(this);
        this.gameInstance = gameInstance;

    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {
        System.out.println("Game Started");
        //start game
        startGame(); 
    }

    public void startGame(){
        game gameInstance = new game();
        
        JFrame gameFrame = new JFrame(game.TITLE);
        gameFrame.add(gameInstance);
        gameFrame.setPreferredSize(new Dimension(game.WIDTH * game.SCALE, game.HEIGHT * game.SCALE));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.pack();
        gameFrame.setVisible(true);
        gameFrame.setLocationRelativeTo(null);
        gameInstance.start();

    }
}
