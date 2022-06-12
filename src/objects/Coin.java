package objects;

import java.awt.*;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : the coin in normal/coin time
 */

public class Coin {
    public int relativeX;
    public int y;
    public int width;
    public int height;
    public Image[] image=new Image[3];
    public Coin(int begin, int length, int positionY){
        relativeX=length;
        width=30;
        height=30;
        y=positionY-30;

    }

}
