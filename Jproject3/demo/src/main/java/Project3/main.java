package Project3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class StartMenu extends JFrame {

    private JPanel contentPane;
    private StartButton startButton;
    private JToggleButton []tb;
    private JPanel contentpane;
    private JLabel drawpane;
    private ImageIcon backgroundImg;

    public StartMenu() {
        setTitle("Start Menu");
        setBounds(200, 200, 700, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        AddComponents();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        contentpane = (JPanel) getContentPane();
        contentpane.setLayout(new BorderLayout());
        
        setContentPane(drawpane);

        startButton = new StartButton(null);
        drawpane.add(startButton);

         // Create mute and unmute toggle buttons
        tb = new JToggleButton[2];
        tb[0] = new JRadioButton("Mute");
        tb[0].setName("Mute");
        tb[1] = new JRadioButton("Unmute");
        tb[1].setName("Unmute");
        tb[0].setSelected(true);

        

        // Set positions and add buttons to the content pane
        tb[0].setBounds(40, 100, 100, 30);
        tb[1].setBounds(140, 100, 100, 30);
        drawpane.add(tb[0]);
        drawpane.add(tb[1]);

        pack();
        setVisible(true);

        tb[0].addItemListener(new ItemListener() {//Mute button
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    tb[1].setSelected(false); // Deselect the other button
                }
            }
        });

        tb[1].addItemListener(new ItemListener() {//Unmute button
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    tb[0].setSelected(false); // Deselect the other button
                }
            }
        });

        setPreferredSize(new Dimension(700, 300));

        pack();
        setVisible(true);
    }
    ///////Add BG////////////////////
    private void AddComponents() {
        final String FILE_BG = "C:/Users/Admin/OneDrive/Desktop/MUIC/Paradigms/Jproject3/Jproject3/demo/src/main/java/Project3/spaceBG.png";//file path

        backgroundImg = new ImageIcon(FILE_BG);
        drawpane = new JLabel();
        drawpane.setIcon(backgroundImg);
        drawpane.setLayout(null); // You can set the layout manager if required
        // Add other components to drawpane if needed
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartMenu();
        });
    }
    
}

class StartButton extends JButton implements MouseListener {

    private int curX = 50, curY = 20;
    private int width = 150, height = 60;

    private ImageIcon startImage;
    private game gameInstance;
    

    public StartButton(game gameInstance) {
        // Load image from the resources
        String path = "src/main/java/Project3/";
        startImage = new ImageIcon(getClass().getResource("startButton.png"));
        
        // Resize the image to fit the button
        Image scaledImage = startImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        startImage = new ImageIcon(scaledImage);

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
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        

        gameInstance.start();

    }

    
}

    
    
