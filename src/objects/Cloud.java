package objects;

import java.util.Random;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : the cloud in normal time
 */

public class Cloud{
    public double positionX,positionY;
    public String link;
    public int width,height;
    public Cloud(double maxLen){
        positionX=maxLen;
        Random randY=new Random();
        positionY=randY.nextInt(150);
        Random randTu=new Random();
        link="src//images//cloud"+(1+randTu.nextInt(4))+".png";
        width=300;
        height=150;
    }
}
