package views;

import java.awt.*;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : characters in different views
 */
public class Character {
    //UI
    Image action1;
    Image action2;
    int x;
    int y;
    int width;
    int height;
    Boolean actionSwapped = false;

    public Character(int x, int y, int width, int height, Image action1, Image action2) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.action1 = action1;
        this.action2 = action2;

    }

    public Character(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    //Rank
    public int characterNum;
    public int score;

    public Character(int characterNum, int score) {
        this.characterNum = characterNum;
        this.score = score;
    }

    @Override
    public String toString() {
        return characterNum + " " + score;
    }
}
