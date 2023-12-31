package Project3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.*;

class MainApplication extends JFrame {

    private JPanel contentPane;
    private StartButton startButton;
    private JToggleButton[] tb;
    private JLabel drawpane;
    private ImageIcon backgroundImg;MySoundEffect
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton creditsButton, guideButton;
    private static String selectedDifficulty = "Lunatic";
    private static MySoundEffect title;
    private boolean mute = false;
    private JSlider volumeSlider;

    public MainApplication() {
        requestFocus();
        setTitle("Start Menu");
        setBounds(200, 200, 620, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        startButton = new StartButton(null);
        contentPane.add(startButton);

        // Combo box for diffculties
        String[] difficulties = { "Baby", "Easy", "Normal", "Hard", "Lunatic" };
        JComboBox<String> difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setBounds(250, 20, 150, 30);
        contentPane.add(difficultyComboBox);
        difficultyComboBox.setSelectedItem("Lunatic");
        difficultyComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                System.out.println(selectedDifficulty);
            }
        });

        // Audio
        this.title = new MySoundEffect();

        // Volume slider
        volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        volumeSlider.setBounds(350, 100, 200, 30);
        contentPane.add(volumeSlider);

        JLabel volumeLabel = new JLabel("Volume");
        volumeLabel.setForeground(Color.WHITE); 
        volumeLabel.setBounds(350, 80, 200, 20);
        contentPane.add(volumeLabel);

        // Username and Password
        JPanel authPanel = new JPanel(new GridLayout(3, 2));

        authPanel.add(new JLabel("                      Username:")).setForeground(Color.WHITE);
        usernameField = new JTextField();
        authPanel.add(usernameField);

        authPanel.add(new JLabel("                      Password:")).setForeground(Color.WHITE);
        passwordField = new JPasswordField();
        authPanel.add(passwordField);

        authPanel.setBounds(-30, 150, 300, 100);
        contentPane.add(authPanel);
        authPanel.setOpaque(false);

        // Credits button
        creditsButton = new JButton("Credits");
        creditsButton.setBounds(490, 320, 100, 30);
        contentPane.add(creditsButton);

        creditsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCredits();
            }
        });
        // Guide button
        guideButton = new JButton("Guide");
        guideButton.setBounds(490, 280, 100, 30);
        contentPane.add(guideButton);

        guideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGuide();
            }
        });

        // Mute and Unmute toggle buttons
        tb = new JToggleButton[2];
        tb[0] = new JRadioButton("Mute");
        tb[0].setName("Mute");
        tb[1] = new JRadioButton("Unmute");
        tb[1].setName("Unmute");
        tb[1].setSelected(true);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(tb[0]);
        buttonGroup.add(tb[1]);

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
                mute = true;
                title.pauseSound();
            }
        });

        tb[1].addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    tb[0].setSelected(false); // Deselect the other button
                }
                mute = false;
                title.resume();
            }
        });

        setPreferredSize(new Dimension(getWidth(), getHeight()));

        AddBackground();

        pack();
        setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                title.SFX(MyConstants.FILE_TITLE, true, 0.5f);
            }
        });
    }

    private void AddBackground() {
        ImageIcon originalGif = new ImageIcon(getClass().getResource("backGround5.gif"));
        Image scaledImage = originalGif.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
        ImageIcon scaledGif = new ImageIcon(scaledImage);
        drawpane = new JLabel(scaledGif);
        drawpane.setLayout(null);

        drawpane.setBounds(0, 0, getWidth(), getHeight());
        contentPane.add(drawpane);
        contentPane.setComponentZOrder(drawpane, contentPane.getComponentCount() - 1);
    }

    private void showCredits() {
        JFrame creditsFrame = new JFrame("Credits");
        creditsFrame.setBounds(300, 300, 400, 200);
        creditsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        creditsFrame.setLayout(new GridLayout(7, 1));

        int fontSize = 18;

        JLabel nameLabel1 = new JLabel(" Frank Piyawat Davies 6480255 ");
        Font customFont1 = new Font("Times New Roman", Font.BOLD, fontSize); 
        nameLabel1.setFont(customFont1);

        JLabel nameLabel2 = new JLabel(" Jitsopin Kanthaulis 6480376 ");
        Font customFont2 = new Font("Times New Roman", Font.BOLD, fontSize); 
        nameLabel2.setFont(customFont2);

        JLabel nameLabel3 = new JLabel(" Chanakan Boonchoo 6580128 ");
        Font customFont3 = new Font("Times New Roman", Font.BOLD, fontSize); 
        nameLabel3.setFont(customFont3);

        JLabel nameLabel4 = new JLabel(" Anaphat Sueakhamron 6480228 ");
        Font customFont4 = new Font("Times New Roman", Font.BOLD, fontSize); 
        nameLabel4.setFont(customFont4);

        JLabel nameLabel5 = new JLabel(" ~~ ASSETS ~~ ");
        JLabel nameLabel6 = new JLabel(" Touhou 6, 8, 15 ; HoloCure ");
        JLabel nameLabel7 = new JLabel(" Honkai: Star Rail ; Umineko ");
        nameLabel6.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel5.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel7.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel5.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
        nameLabel6.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
        nameLabel7.setFont(new Font("Times New Roman", Font.BOLD, fontSize));

        creditsFrame.add(nameLabel1);
        creditsFrame.add(nameLabel2);
        creditsFrame.add(nameLabel3);
        creditsFrame.add(nameLabel4);
        creditsFrame.add(nameLabel5);
        creditsFrame.add(nameLabel6);
        creditsFrame.add(nameLabel7);

        creditsFrame.pack();
        creditsFrame.setLocationRelativeTo(null);
        creditsFrame.setVisible(true);
    }

    private void showGuide() {
        JFrame guideFrame = new JFrame("Guide");
        guideFrame.setBounds(300, 300, 400, 200);
        guideFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        addImagePanel(MyConstants.FILE_SLIDE1, cardPanel);
        addImagePanel(MyConstants.FILE_SLIDE2, cardPanel);
        addImagePanel(MyConstants.FILE_SLIDE3, cardPanel);
        addImagePanel(MyConstants.FILE_SLIDE4, cardPanel);
        addImagePanel(MyConstants.FILE_SLIDE5, cardPanel);
        addImagePanel(MyConstants.FILE_SLIDE6, cardPanel);

        JButton prevButton = new JButton("<<");
        prevButton.addActionListener(e -> cardLayout.previous(cardPanel));

        JButton nextButton = new JButton(">>");
        nextButton.addActionListener(e -> cardLayout.next(cardPanel));

        prevButton.setPreferredSize(new Dimension(500, 40));
        nextButton.setPreferredSize(new Dimension(500, 40));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        guideFrame.add(buttonPanel);

        guideFrame.setLayout(new BorderLayout());
        guideFrame.add(cardPanel, BorderLayout.CENTER);
        guideFrame.add(buttonPanel, BorderLayout.SOUTH);

        guideFrame.pack();
        guideFrame.setLocationRelativeTo(null);
        guideFrame.setVisible(true);
    }

    public void addImagePanel(String imagePath, JPanel cardPanel) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(new ImageIcon(getClass().getResource(imagePath)));
        panel.add(label);
        cardPanel.add(panel);
    }

    public static String getDifficulty() {
        return selectedDifficulty;
    }

    public static void stoptitleSound() {
        title.stopSound();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainApplication();
        });
    }
}

class StartButton extends JButton implements MouseListener {

    private int curX = 50, curY = 0;
    private int width = 100, height = 100;

    private ImageIcon startImage;
    private game gameInstance;
    private MySoundEffect title;

    public StartButton(game gameInstance) {
        startImage = new ImageIcon(getClass().getResource("newStartButton.png"));

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

        this.title = new MySoundEffect();

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
        MainApplication.stoptitleSound();
        title.SFX(MyConstants.FILE_OK, false, 0.7f);
        System.out.println("Game Started");
        // start game
        startGame();
    }

    public void startGame() {
        game gameInstance = new game(MainApplication.getDifficulty());
        gameInstance.addKeyListener(new KeyInput(gameInstance));
        MouseInput mouseInput = new MouseInput(gameInstance);
        gameInstance.addMouseListener(mouseInput);
        gameInstance.addMouseMotionListener(mouseInput);

        JFrame gameFrame = new JFrame(game.TITLE);
        gameFrame.add(gameInstance);
        gameFrame.setPreferredSize(new Dimension(game.WIDTH, game.HEIGHT));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        gameFrame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                System.out.println("Window gained focus");
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println("Window lost focus");
                gameInstance.togglePauseOnWindowLostFocus();
            }
        });

        gameInstance.start();
        // startmenu disappear
        SwingUtilities.getWindowAncestor(this).setVisible(false);
    }
}
