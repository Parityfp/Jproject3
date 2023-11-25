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
        setBounds(200, 200, 400, 400);
        setPreferredSize(new Dimension(700, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout( new BorderLayout(25, 25) );

        startButton = new StartButton(null);
        contentPane.add(startButton, BorderLayout.CENTER);

        JPanel cregion = new JPanel();
        String[] difficulties = {"Very easy", "Easy", "Medium", "Hard", "Impossible"};
        JComboBox<String> difficultyComboBox = new JComboBox<>(difficulties);
	    cregion.add(difficultyComboBox);

        StartMenu startMenu = this;
        contentPane.add(cregion, BorderLayout.NORTH);
        
        pack();
        setVisible(true);
        this.validate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartMenu();
        });
    }
}

class StartButton extends JButton implements MouseListener {

    private int curX = 250, curY = 20;
    private int width = 200, height = 150;

    private ImageIcon startImage;
    private game gameInstance;

    public StartButton(game gameInstance) {
        String path = "src/main/java/Project3/";
        startImage = new ImageIcon(getClass().getResource("startButton.png"));

        Image img = startImage.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        startImage = new ImageIcon(resizedImg);

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

        //startmenu disappear
        SwingUtilities.getWindowAncestor(this).setVisible(false);
    }
}
