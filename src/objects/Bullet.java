package objects;

import Others.HitBox;
import Others.ImageOperations;

import javax.swing.*;
import java.awt.*;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : the normal bullet
 */
public class Bullet extends ImageOperations {
    public int hurt=3;
    public double bullet_x;
    public double bullet_y;
    public double width;
    public double height;
    public ImageIcon bulletImageIcon = new ImageIcon("src//images//bullet.png");
    public Image[] bulletImages=new Image[7];
    public Image all;
    public double round=0.2;
    public double timer=0;
    public int ind;
    public int zhang;
    public HitBox hitBox;
    public Bullet(double x, double y) {
        this.bullet_x = x;
        this.bullet_y = y;
        this.width = 50;
        this.height = 30;
        all=loadImage("src//images//sstrongerdamage.png");
        hitBox=new HitBox((int)bullet_x,(int)bullet_y-10,(int)width-20,(int)height);
    }
    public void updateOwnImage(){
        if(hurt==3){
            width=50;
            height=30;
            bulletImages[0]=loadImage("src//images//normalbullet0.png");
            bulletImages[1]=loadImage("src//images//normalbullet1.png");
            bulletImages[2]=loadImage("src//images//normalbullet2.png");
            zhang=3;
        }
        else{
            round=0.2;
            width=50;
            height=30;
            for(int i=0;i<4;i++)bulletImages[i]=subImage(all,0,i*32,128,32);
            zhang=4;
        }
    }
    public void updateHitBox(){
        hitBox.x=(int)bullet_x;
        hitBox.y=(int)bullet_y-20;
    }
}
