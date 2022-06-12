package views;

//import tools.MusicPlayer;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;

import static controller.GameEngine.createGame;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : the view show the beginning of the game
 */

public class GameStartView {

    public static RankingUpdate ranking;
    public static GameFrame mFrame;
    public static String viewName;
    public static Font font;
    JPanel startView;

    ImageIcon gameMenuViewImage;
    private JButton helpButton;
    private JButton startButton;
    private JButton rankButton;
    AudioClip startMusic;

    public static void main(String[] args) {
        createGameStartView();
    }
    //create the game start view
    public static void createGameStartView() {
        EventQueue.invokeLater(() -> {
            try {
                new GameStartView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GameStartView() throws MalformedURLException {
        //frame initialization
        mFrame = new GameFrame();
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/images/font.ttf"))).deriveFont(28f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        //panel initialization
        gameMenuViewImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/gameMenuView.png")));
        startView = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(gameMenuViewImage.getImage(), 0, 0, mFrame.getWidth(), mFrame.getHeight(), this);
            }
        };
        startView.setLayout(null);

        //start button initialization
        ImageIcon startButtonImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/notPushed.png")));
        startButton = new JButton(startButtonImage);
        startButton.setBounds(mFrame.getWidth()/2 - startButtonImage.getIconWidth()/2, mFrame.getHeight()/2 - startButtonImage.getIconHeight()/2, startButtonImage.getIconWidth(), startButtonImage.getIconHeight());
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);
        startButton.setRolloverEnabled(true);
        startButton.setRolloverIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/pushed.png"))));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //help button initialization
        helpButton = new JButton("Help");
        helpButton.setForeground(Color.WHITE);
        helpButton.setBounds(mFrame.getWidth()/2 - 120, mFrame.getHeight()/2 + 150, 200, 80);
        helpButton.setBorder(null);
        helpButton.setFont(font);
        helpButton.setBorderPainted(false);
        helpButton.setContentAreaFilled(false);
        helpButton.setFocusPainted(false);
        helpButton.setVisible(true);

        //rank button initialization
        ImageIcon rankButtonImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/rank1.png")));
        rankButton = new JButton(rankButtonImage);
        rankButton.setBounds(mFrame.getWidth()/2 - startButtonImage.getIconWidth()/2 - 20, mFrame.getHeight()/2 - startButtonImage.getIconHeight()/2 + 240, startButtonImage.getIconWidth(), startButtonImage.getIconHeight());
        rankButton.setContentAreaFilled(false);
        rankButton.setBorderPainted(false);
        rankButton.setFocusPainted(false);
        rankButton.setOpaque(false);
        rankButton.setRolloverEnabled(true);
        rankButton.setRolloverIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/rank2.png"))));
        rankButton.setCursor(new Cursor(Cursor.HAND_CURSOR));


        // add buttons to panel
        startView.add(helpButton);
        startView.add(startButton);
        startView.add(rankButton);

        // add action listener to buttons
        ButtonHandler buttonHandler = new ButtonHandler();
        startButton.addActionListener(buttonHandler);
        helpButton.addActionListener(buttonHandler);
        rankButton.addActionListener(buttonHandler);

        mFrame.contentPanel.add(startView,"startView");
        mFrame.showCard("startView");

        //music initialization
        startMusic = Applet.newAudioClip(new File("src/images/begin.wav").toURL());
        startMusic.play();

        //init rank
        ranking = new RankingUpdate();
        ranking.initRank();
        ranking.getRankData();
    }


    class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == helpButton) {
                JOptionPane.showMessageDialog(null, "Hello adventurer,welcome to Running Game!\n" +
                        "You are a runner, you need to collect the coins and avoid the obstacles.\n" +
                        "You can use 'WASD' keys to control character actions.\n" +
                        "You can use the 'K' key to fire bullets.\n" +
                        "You can use the 'J' key to fire super bullets.\n" +
                        "You can use the 'E' key to pause the game.\n" +
                        "You can use the 'R' key to restart the game.\n" +
                        "Good luck!", "Help", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/icon.png"))));
            } else if (e.getSource() == startButton) {
                startGame();
                startMusic.stop();
            } else if (e.getSource() == rankButton) {
                showRankView();
                startMusic.stop();
            }


        }
    }

    public void startGame() {
        viewName = "selectCharacterView";
        createGame(new SelectCharacterView(),120);
    }

    private void showRankView() {
        viewName = "rankView";
        createGame(new GameRankView(),6);
    }
}


