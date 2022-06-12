package views;

import controller.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Objects;

import static controller.GameController.mainGame;
import static views.GameStartView.mFrame;
import static views.GameStartView.viewName;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : Choose a character for you to control
 */

public class SelectCharacterView extends GameEngine {

    public static int selectedCharacterNumber;
    private Image[] playerImages;
    private Character[] playerCharacter;
    private int runningCounter;
    private Boolean character1Selected;
    private Boolean character2Selected;
    private Boolean character3Selected;
    private Boolean character4Selected;
    private Boolean character1Focused;
    private Boolean character2Focused;
    private Boolean character3Focused;
    private Boolean character4Focused;
    private Boolean changeView;
    private AudioClip selectSound;
    private AudioClip selectBGM;
    private int mutex;

    public void init() {
        //init basic variables
        runningCounter = 0;
        selectedCharacterNumber = 0;
        playerImages = new Image[9];
        playerCharacter = new Character[4];

        //focus and select player
        character1Selected = false;
        character2Selected = false;
        character3Selected = false;
        character4Selected = false;
        character1Focused = false;
        character2Focused = false;
        character3Focused = false;
        character4Focused = false;
        changeView = false;
        mutex = 0;

        //load images
        loadImage();

        //create player
        createPlayer();

        //create audio
        selectSound = loadAudio("src/images/select.wav");
        selectBGM = loadAudio("src/images/selectBGM.wav");
        startAudioLoop(selectBGM);
    }

    @Override
    public void update(double dt) {
        if(!changeView) {
            //if the character is focused, then change the image
            if (character1Focused) {
                updateSelectedEffect(playerCharacter[0]);
                updateImage(runningCounter, playerCharacter[0]);
            } else if (character2Focused) {
                updateSelectedEffect(playerCharacter[1]);
                updateImage(runningCounter, playerCharacter[1]);
            } else if (character3Focused) {
                updateSelectedEffect(playerCharacter[2]);
                updateImage(runningCounter, playerCharacter[2]);
            } else if (character4Focused) {
                updateSelectedEffect(playerCharacter[3]);
                updateImage(runningCounter, playerCharacter[3]);
            }
            runningCounter++;
            //obtain the selected character
            if (character1Selected || character2Selected || character3Selected || character4Selected) {
                getSelectedCharacter();
                changeView();
            }
        }
    }

    @Override
    public void paintComponent() {
        clearBackground(width(), height());
        Image selectViewBackground = loadImage("src//images//selectCharacterView.png");
        drawImage(selectViewBackground, 0, 0, width(), height());
        if (character1Focused) {
            updateSelectedEffect(playerCharacter[0]);
        } else if (character2Focused) {
            updateSelectedEffect(playerCharacter[1]);
        } else if (character3Focused) {
            updateSelectedEffect(playerCharacter[2]);
        } else if (character4Focused) {
            updateSelectedEffect(playerCharacter[3]);
        }
        for (int i = 0; i < 4; i++) {
            drawPlayer(playerCharacter[i]);
        }
    }

    //mouse click event
    public void mouseClicked(MouseEvent event) {
        if (event.getX() >= playerCharacter[0].x && event.getX() <= playerCharacter[0].x + playerCharacter[0].width && event.getY() >= playerCharacter[0].y && event.getY() <= playerCharacter[0].y + playerCharacter[0].height) {
            int opt = JOptionPane.showConfirmDialog(null, "Are you sure to choose this little green dinosaur as your character?", "GREEN", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/icon.png"))));
            if (opt == JOptionPane.OK_OPTION) {
                character1Selected = true;
            }
        } else if (event.getX() >= playerCharacter[1].x && event.getX() <= playerCharacter[1].x + playerCharacter[1].width && event.getY() >= playerCharacter[1].y && event.getY() <= playerCharacter[1].y + playerCharacter[1].height) {
            int opt = JOptionPane.showConfirmDialog(null, "Are you sure to choose this little blue dinosaur as your character?", "BLUE", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/icon.png"))));
            if (opt == JOptionPane.OK_OPTION) {
                character2Selected = true;
            }
        } else if (event.getX() >= playerCharacter[2].x && event.getX() <= playerCharacter[2].x + playerCharacter[2].width && event.getY() >= playerCharacter[2].y && event.getY() <= playerCharacter[2].y + playerCharacter[2].height) {
            int opt = JOptionPane.showConfirmDialog(null, "Are you sure to choose this little yellow dinosaur as your character?", "YELLOW", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/icon.png"))));
            if (opt == JOptionPane.OK_OPTION) {
                character3Selected = true;
            }
        } else if (event.getX() >= playerCharacter[3].x && event.getX() <= playerCharacter[3].x + playerCharacter[3].width && event.getY() >= playerCharacter[3].y && event.getY() <= playerCharacter[3].y + playerCharacter[3].height) {
            int opt = JOptionPane.showConfirmDialog(null, "Are you sure to choose this little red dinosaur as your character?", "RED", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/icon.png"))));
            if (opt == JOptionPane.OK_OPTION) {
                character4Selected = true;
            }
        }
    }

    //mouse pass through the image: change the image
    public void mouseMoved(MouseEvent event) {
        if (event.getX() >= playerCharacter[0].x && event.getX() <= playerCharacter[0].x + playerCharacter[0].width && event.getY() >= playerCharacter[0].y && event.getY() <= playerCharacter[0].y + playerCharacter[0].height) {
            resetAll();
            character1Focused = true;
            zoomCharacter(0,event);
        } else if (event.getX() >= playerCharacter[1].x && event.getX() <= playerCharacter[1].x + playerCharacter[1].width && event.getY() >= playerCharacter[1].y && event.getY() <= playerCharacter[1].y + playerCharacter[1].height) {
            resetAll();
            character2Focused = true;
            zoomCharacter(1,event);
        } else if (event.getX() >= playerCharacter[2].x && event.getX() <= playerCharacter[2].x + playerCharacter[2].width && event.getY() >= playerCharacter[2].y && event.getY() <= playerCharacter[2].y + playerCharacter[2].height) {
            resetAll();
            character3Focused = true;
            zoomCharacter(2,event);
        } else if (event.getX() >= playerCharacter[3].x && event.getX() <= playerCharacter[3].x + playerCharacter[3].width && event.getY() >= playerCharacter[3].y && event.getY() <= playerCharacter[3].y + playerCharacter[3].height) {
            resetAll();
            character4Focused = true;
            zoomCharacter(3,event);
        } else {
            mutex = 0;
            resetAll();
        }
    }

    //create and draw player
    private void loadImage() {
        playerImages[0] = loadImage("src//images//player11.png");
        playerImages[1] = loadImage("src//images//player12.png");
        playerImages[2] = loadImage("src//images//player21.png");
        playerImages[3] = loadImage("src//images//player22.png");
        playerImages[4] = loadImage("src//images//player31.png");
        playerImages[5] = loadImage("src//images//player32.png");
        playerImages[6] = loadImage("src//images//player41.png");
        playerImages[7] = loadImage("src//images//player42.png");
    }

    private void createPlayer() {
        playerCharacter[0] = new Character(140, 300, 50, 50, playerImages[0], playerImages[1]);
        playerCharacter[1] = new Character(300, 300, 50, 50, playerImages[2], playerImages[3]);
        playerCharacter[2] = new Character(460, 300, 50, 50, playerImages[4], playerImages[5]);
        playerCharacter[3] = new Character(620, 300, 50, 50, playerImages[6], playerImages[7]);
    }

    private void drawPlayer(Character player) {
        if (player.actionSwapped) {
            drawImage(player.action2, player.x, player.y, player.width, player.height);
        } else {
            drawImage(player.action1, player.x, player.y, player.width, player.height);
        }
    }

    //switch actions
    private void updateImage(int time, Character player) {
        if (time % 2 == 0) {
            player.actionSwapped = !player.actionSwapped;
        }
    }

    //bigger the character
    private void zoomCharacter(int characterNumber, MouseEvent event) {
        playerCharacter[characterNumber].width = 100;
        playerCharacter[characterNumber].height = 100;
        playerCharacter[characterNumber].x = playerCharacter[characterNumber].x - 25;
        playerCharacter[characterNumber].y = playerCharacter[characterNumber].y - 25;
        //only one music playing at the same time
        if(mutex == 0) {
            playAudio(selectSound);
            mutex = 1;
        }
    }

    //reset all the character
    private void resetAll() {
        character1Focused = false;
        character2Focused = false;
        character3Focused = false;
        character4Focused = false;
        for (int i = 0; i < 4; i++) {
            playerCharacter[i].width = 50;
            playerCharacter[i].height = 50;
            playerCharacter[i].x = 140 + i * 160;
            playerCharacter[i].y = 300;
        }
    }

    //draw selected effect
    private void updateSelectedEffect(Character player) {
        Image select = loadImage("src//images//select.png");
        if (player.actionSwapped) {
            drawImage(select,player.x + 25, player.y - 60, 50, 50);
        } else {
            drawImage(select,player.x + 25, player.y - 70, 50, 50);
        }
        Image back = loadImage("src//images//obstacle (1).png");
        drawImage(back, player.x - 50 ,player.y - 50,200, 200);
    }

    //returen the selected character result
    public void getSelectedCharacter() {
        if (character1Selected) {
            selectedCharacterNumber = 1;
        } else if (character2Selected) {
            selectedCharacterNumber = 2;
        } else if (character3Selected) {
            selectedCharacterNumber = 3;
        } else if (character4Selected) {
            selectedCharacterNumber = 4;
        }
    }

    private void changeView() {
        viewName = "mainGameView";
        createGame(mainGame,30);
        mFrame.showCard("mainGameView");
        changeView = true;
        stopAudioLoop(selectBGM);
    }

}

