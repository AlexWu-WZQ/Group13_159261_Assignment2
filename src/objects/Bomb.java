package objects;

import Others.ImageOperations;

import javax.swing.*;
import java.awt.*;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : this class will play a role in the monster time game mode.
 */
public class Bomb extends ImageOperations {

    public double bomb_x;
    public double bomb_y;
    public double width;
    public double height;
    public Image[] images=new Image[7];
    public double timer;
    public double round=0.7;
    public int ind;
    public boolean exist=true;
    public int count=0;//If killing an enemy increases the score, delete the relevant part if not necessary
//    public int getCount() {
//        return count;
//    }
    public Bomb(double x, double y) {
        this.bomb_x = x;
        this.bomb_y = y;
        this.width = 70;
        this.height = 70;
    }

//    public void move() {
//        count++;
//    }
}