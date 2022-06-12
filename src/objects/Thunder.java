package objects;

import Others.HitBox;
import Others.ImageOperations;

import java.awt.*;
import java.util.ArrayList;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description :  normal time thunder
 */

public class Thunder {
    public double positionX;
    public double positionY;
    public HitBox hitBox;
    public int width=100;
    public int height;
    public Image[] images=new Image[8];
    public int imageInd;
    public double peaceRound=0.2;
    public double peaceDuratioin=1.3;
    public double attackRound=0.4;
    public double wholeRound=1.6;
    public double wholeTimer=0;
    public int generateInd=2;
    public boolean hitMonster=false;
    public Thunder(double positionX,double positionY, int height){
        this.positionX=positionX;
        this.positionY=positionY;
        this.height=height;
        this.hitBox=new HitBox((int)positionX,(int)positionY,40,height);
    }
    public void updateHitBox(){
        if(imageInd<generateInd){
            hitBox.x=(int)positionX+40;
            hitBox.h=height/5*2;
        }
        else{
            hitBox.x=(int)positionX+40;
            hitBox.h=height;
        }
    }
}
