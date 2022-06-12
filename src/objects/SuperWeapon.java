package objects;

import Others.HitBox;

import java.awt.*;
import java.util.ArrayList;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description :  high level weapon
 */

public class SuperWeapon extends Skill{
    public Image[] images=new Image[6];
    public double bullet_x;
    public double bullet_y;
    public int width=140;
    public int height=140;
    public double weaponTimer=0;
    public double weaponRound=0.5;
    public int imageInd;
    public int hurt=1000;
    public ArrayList<HitBox> hitBoxes=new ArrayList<>();

    public SuperWeapon (double x,double y){
        super();
        this.bullet_x=x;
        this.bullet_y=y;
        this.skillDuration=2;
        hitBoxes.add(new HitBox((int)bullet_x,(int)bullet_y,width,height-70));
        hitBoxes.add(new HitBox((int)bullet_x,(int)bullet_y,width-40,height-10));
    }
    public void updateHitBox(){
        hitBoxes.get(0).x=(int)bullet_x;
        hitBoxes.get(0).y=(int)bullet_y+35;
        hitBoxes.get(1).x=(int)bullet_x+10;
        hitBoxes.get(1).y=(int)bullet_y;
    }
}
