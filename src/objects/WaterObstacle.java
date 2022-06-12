package objects;

import java.awt.*;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description :  normal time water on the ground
 */

public class WaterObstacle {
    public double relativeX;
    public int y;
    public int width;
    public int height;
    public Image image;
    public boolean hitMonster=false;
    public WaterObstacle(int length, int positionY) {
        relativeX = length;
        width = 150;
        height = 50;
        y = positionY-24;
    }
}
