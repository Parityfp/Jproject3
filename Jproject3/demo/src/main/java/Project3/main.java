package Project3;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

class StartMenu extends JFrame {

    private JPanel contentPane;
    private StartButton startButton;
    private JToggleButton []tb;

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

         // Create mute and unmute toggle buttons
        tb = new JToggleButton[2];
        tb[0] = new JRadioButton("Mute");
        tb[0].setName("Mute");
        tb[1] = new JRadioButton("Unmute");
        tb[1].setName("Unmute");
        tb[0].setSelected(true);


        //ensure cant deselect
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(tb[0]);
        buttonGroup.add(tb[1]);

        // Set positions and add buttons to the content pane
        tb[0].setBounds(40, 100, 100, 30);
        tb[1].setBounds(150, 100, 100, 30);
        contentPane.add(tb[0]);
        contentPane.add(tb[1]);

        tb[0].addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    tb[1].setSelected(false); // Deselect the other button
                }
            }
        });

        tb[1].addItemListener(new ItemListener() {
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
        gameInstance.addKeyListener(new KeyInput(gameInstance));
        
        JFrame gameFrame = new JFrame(game.TITLE);
        gameFrame.add(gameInstance);
        gameFrame.setPreferredSize(new Dimension(game.WIDTH, game.HEIGHT));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        

        gameInstance.start();
        //startmenu disappear
        SwingUtilities.getWindowAncestor(this).setVisible(false);
    }

    
}

// class BufferedimageLoader{
//     private BufferedImage image;

//     public BufferedImage loadImage(String path) throws IOException{
//         image = ImageIO.read(getClass().getResource(path));
//         return image;

//     }

// }

    
    