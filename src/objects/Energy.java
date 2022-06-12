package objects;

import java.awt.*;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description :Energy skill - Monster restores 5 health
 */

public class Energy extends Skill{
    public int relativeX;
    public int y;
    public int width;
    public int height;
    public Image image;
    public Energy(int begin, int length, int positionY) {
//        Random randx = new Random();
        relativeX = length;
        width = 30;
        height = 30;
        y = positionY - 30;
    }
}
