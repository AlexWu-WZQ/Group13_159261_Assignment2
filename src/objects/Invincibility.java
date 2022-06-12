package objects;

import java.awt.*;
import java.util.Random;
/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description :Invincible skill - Resist any damage
 */

public class Invincibility extends Skill{
    public int relativeX;
    public int y;
    public int width;
    public int height;
    public Image image;
    public Image[] images=new Image[10];
    public Invincibility(int begin, int length, int positionY) {
//        Random randx = new Random();
        relativeX = length;
        width = 30;
        height = 30;
        y = positionY - 30;
    }
}
