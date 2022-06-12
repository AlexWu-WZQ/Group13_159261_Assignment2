package views;

import controller.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static views.GameStartView.font;
import static views.GameStartView.mFrame;
import static views.RankingUpdate.characters;


/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : the view for ranking characters and show their scores
 */
public class GameRankView extends GameEngine {

    private Image background;
    private Image light1;
    private Image light2;
    private Image light3;
    private Image stairs;
    private Image king;

    private Character[] actors;
    private int counter;
    private Boolean changeView;
    private AudioClip rankBGM;
    JButton exit;

    public void init() {
        //init basic variables
        counter = 0;
        actors = new Character[4];
        changeView = false;

        //load images
        initGameImage();

        //create player
        createPlayer();

        //BGM
        rankBGM = loadAudio("src/images/rankBGM.wav");
        startAudioLoop(rankBGM);

        //get data
        RankingUpdate ranking = new RankingUpdate();
        ranking.getRankData();
        setActorValues();
    }

    @Override
    public void update(double dt) {
        if(!changeView) {
            //if the character is focused, then change the image
            for (int i = 0; i < 3; i++) {
                updateImage(counter, actors[i]);
            }
            counter++;
        }
    }

    @Override
    public void paintComponent() {
        clearBackground(width(), height());
        // draw static items
        drawImage(background, 0, 0, 820, 610);
        drawImage(stairs, width()/2 - stairs.getWidth(null)/2 - 100, height()/2 - stairs.getHeight(null)/2 + 80, stairs.getWidth(null) - 80, stairs.getHeight(null) - 80);

        // draw selected effect
        updateSelectedEffect(actors[0], light1);
        updateSelectedEffect(actors[1], light2);
        updateSelectedEffect(actors[2], light3);

        // draw characters
        for (int i = 0; i < 4; i++) {
            drawPlayer(actors[i]);
        }
        //init text
        drawText();
        drawExitButton();
    }

    //initialize the game image
    public void initGameImage() {
        //images
        stairs = loadImage("src//images//stairs.png");
        background = loadImage("src//images//rankViewBackground.png");
        king = loadImage("src//images//king.png");

        //special effect
        light1 = loadImage("src//images//light1.png");
        light2 = loadImage("src//images//light2.png");
        light3 = loadImage("src//images//light3.png");
    }

    private void createPlayer() {
        actors[0] = new Character(220, 160, 100, 100);
        actors[1] = new Character(60, 270, 100, 100);
        actors[2] = new Character(320, 280, 100, 100);
        actors[3] = new Character(60, 470, 100, 100);
    }

    private void setActorValues() {
        for (int i = 0; i < actors.length; i++) {
            actors[i].score = characters[i].score;
            actors[i].characterNum = characters[i].characterNum;
            actors[i].action1 = loadImage("src//images//player"+ actors[i].characterNum +"1.png");
            actors[i].action2 = loadImage("src//images//player"+ actors[i].characterNum +"2.png");
        }
    }

    private void drawPlayer(Character player) {
        if (player.actionSwapped) {
            drawImage(player.action2, player.x, player.y, player.width, player.height);
        } else {
            drawImage(player.action1, player.x, player.y, player.width, player.height);
        }
    }

    private void drawText() {
        drawBoldText(width()/2 - 180, height()/2 - 200, "RANKING LIST", font, 300, new Color(151, 70, 19));
        drawBoldText(width()/2 + 80, height()/2 - 150, "1st", font, 50, new Color(255, 236, 108));
        drawBoldText(width()/2 + 80, height()/2 - 100, colorToText(actors[0]) + actors[0].score, font, 50, new Color(255, 236, 108));
        drawBoldText(width()/2 + 80, height()/2 - 30, "2nd", font, 50, new Color(255, 193, 139));
        drawBoldText(width()/2 + 80, height()/2 + 20, colorToText(actors[1]) + actors[1].score, font, 50, new Color(255, 193, 139));
        drawBoldText(width()/2 + 80, height()/2 + 90, "3rd", font, 50, new Color(255, 182, 199));
        drawBoldText(width()/2 + 80, height()/2 + 140, colorToText(actors[2]) + actors[2].score, font, 50, new Color(255, 182, 199));
        drawBoldText(actors[3].x + 120, actors[3].y + 50, "Mr.Nobody", font, 50, new Color(255, 255, 255));
        drawBoldText(actors[3].x + 160, actors[3].y + 100, colorToText(actors[3]) + actors[3].score, font, 50, new Color(255, 255, 255));
    }

    //identify the character color by the number
    private String colorToText(Character player) {
        if (player.characterNum == 1) {
            return "GREEN:";
        } else if (player.characterNum == 2) {
            return "BLUE:";
        } else if (player.characterNum == 3) {
            return "YELLOW:";
        } else if (player.characterNum == 4) {
            return "RED:";
        } else {
            return "";
        }
    }

    //draw exit button
    private void drawExitButton() {
        exit = new JButton("EXIT");
        exit.setForeground(Color.WHITE);
        exit.setBounds(width()/2 + 200, height()/2 + 220, 200, 80);
        exit.setFont(font);
        exit.setBorderPainted(false);
        exit.setContentAreaFilled(false);
        exit.setFocusPainted(false);
        exit.setVisible(true);
        ButtonHandler buttonHandler = new ButtonHandler();
        exit.addActionListener(buttonHandler);
        mPanel.add(exit);
    }

    //switch actions
    private void updateImage(int time, Character player) {
        if (time % 2 == 0) {
            player.actionSwapped = !player.actionSwapped;
        }
    }

    //draw selected effect
    private void updateSelectedEffect(Character player, Image light) {
        drawImage(light, player.x - 50 ,player.y - 50,200, 200);
        if(player == actors[0]) {
            if (actors[0].actionSwapped) {
                drawImage(king,player.x + 25, player.y - 30, 50, 50);
            } else {
                drawImage(king,player.x + 25, player.y - 40, 50, 50);
            }
        }
    }

    private void changeView() {
        stopAudioLoop(rankBGM);
        mFrame.showCard("startView");
    }

    class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() != exit) {
                changeView();
            }
        }
    }
}
