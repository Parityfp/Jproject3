package Project3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class StartMenu extends JFrame {

    private JPanel contentPane;
    private StartButton startButton;
    private JToggleButton[] tb;
    private JLabel drawpane;
    private ImageIcon backgroundImg;
    private JTextField usernameField;
    private JPasswordField passwordField;
     private JButton creditsButton;

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

        // Combo box for diffculties
        String[] difficulties = { "Very easy", "Easy", "Medium", "Hard", "Lunatic" };
        JComboBox<String> difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setBounds(250, 20, 150, 30);
        contentPane.add(difficultyComboBox);

        // Username and Password
        JPanel authPanel = new JPanel(new GridLayout(3, 2));

        authPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        authPanel.add(usernameField);

        authPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        authPanel.add(passwordField);

        authPanel.setBounds(50, 150, 300, 100);
        contentPane.add(authPanel);

        //Credits button
        creditsButton = new JButton("Credits");
        creditsButton.setBounds(570, 220, 100, 30);
        contentPane.add(creditsButton);

        // Add action listener to Credits button
        creditsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCredits();  // Call the method to display credits
            }
        });

        // Create mute and unmute toggle buttons
        tb = new JToggleButton[2];
        tb[0] = new JRadioButton("Mute");
        tb[0].setName("Mute");
        tb[1] = new JRadioButton("Unmute");
        tb[1].setName("Unmute");
        tb[0].setSelected(true);

        // ensure cant deselect
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(tb[0]);
        buttonGroup.add(tb[1]);

        // Set positions and add buttons to the content pane
        tb[0].setBounds(50, 100, 100, 30);
        tb[1].setBounds(200, 100, 100, 30);
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

        AddBackground();

        pack();
        setVisible(true);
    }

    private void AddBackground() {
        backgroundImg = new ImageIcon(getClass().getResource("spaceBG.png"));
        drawpane = new JLabel();
        drawpane.setIcon(backgroundImg);
        drawpane.setLayout(null);

        drawpane.setBounds(0, 0, getWidth(), getHeight());
        contentPane.add(drawpane);
    }

    private void showCredits() {
        JFrame creditsFrame = new JFrame("Credits");
        creditsFrame.setBounds(300, 300, 400, 200);
        creditsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        creditsFrame.setLayout(new GridLayout(3, 1));

        JLabel nameLabel1 = new JLabel("Member 1:");
        JLabel nameLabel2 = new JLabel("Member 2:");

        creditsFrame.add(nameLabel1);
        creditsFrame.add(nameLabel2);

        creditsFrame.pack();
        creditsFrame.setLocationRelativeTo(null);
        creditsFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartMenu();
        });
    }
}

class StartButton extends JButton implements MouseListener {

    private int curX = 50, curY = 0;
    private int width = 150, height = 100;

    private ImageIcon startImage;
    private game gameInstance;

    public StartButton(game gameInstance) {
        // Load image from the resources
        startImage = new ImageIcon(getClass().getResource("startButton.png"));

        // Resize the image to fit the button
        Image scaledImage = startImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        startImage = new ImageIcon(scaledImage);

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

        setBounds(curX, curY, width, height);
        setIcon(startImage);

        addMouseListener(this);
        this.gameInstance = gameInstance;

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
        // start game
        startGame();
    }

    public void startGame() {
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
        // startmenu disappear
        SwingUtilities.getWindowAncestor(this).setVisible(false);
    }
}

// class BufferedimageLoader{
// private BufferedImage image;

// public BufferedImage loadImage(String path) throws IOException{
// image = ImageIO.read(getClass().getResource(path));
// return image;

// }

// }
