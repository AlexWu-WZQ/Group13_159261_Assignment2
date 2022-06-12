package objects;

import Others.ImageOperations;

import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : A class that play a role in controlling the dynamic changes of the buff/weapon on the bottom of the screen.
 * e.g. tell the player when you can use this weapon or when the buffs end.
 */

public class Skill {
    public double skillDuration = 8.0;
    public double timer = 0;
    public boolean skillActivation = false;
    public Image activationImage;
    public int activationHeight=50;
    public int activationWidth=50;
    public int positionX;
    public int positionY;
    public int alpha=360;
    public boolean abailable=true;
    public double coolTime=5;


    public Skill(int positionX,int positionY){
        this.positionX=positionX;
        this.positionY=positionY;
    }

    public Skill() {}

    //Skill is available for 8 seconds
    public void updateBuff(double dt) {
        if (!skillActivation) return;
        timer += dt;
        double ha=timer/skillDuration;
        alpha=(int)(ha*360);
        if (timer >= skillDuration) {
            skillActivation = false;
            timer = 0;
        }
    }


    public void updateSkill(double dt){
        if (abailable) {
            timer=0;
            return;
        }
        timer += dt;
        double ha=timer/coolTime;
        alpha=(int)(ha*360);
        if (timer >= coolTime) {
            abailable = true;
            timer=0;
            alpha= 360;
        }
    }


    public void restart(){
        skillActivation=true;
        timer=0;
    }
}

