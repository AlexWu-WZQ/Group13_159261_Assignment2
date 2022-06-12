package objects;

import Others.ImageOperations;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : the enemy
 */

public class Enemy extends ImageOperations {
    //Enemy attributes, amount of blood picture size will move, etc.
    public Double live=0.0;
    public double enemy_x=800;
    public double enemy_y;
    public int healthy_bar;
    boolean moving;
    boolean shoting;
    boolean flying;
    public double width;
    public double height;
    public ImageIcon enemyImageIcon ;
    public Image enemyImage ;
    public String kind;
    public double speed=5;
    public int hurt=2;
    Random random=new Random();
    double speedx=1.0;
    public int Missilesnum;
    boolean changeable=false;
    public Image[] changeableimage;
    public double changetime=0.0;
    int remember=0;
    public boolean beHit=false;
    //The main type of enemy
    public Enemy(String kind){
        this.kind=kind;
        if (kind =="flower"){
            moving=false;  shoting=false; flying=false;speed=8;healthy_bar=4;enemy_y=310;
            width= 75;height=80;
            enemyImageIcon=new ImageIcon("src//images//flower.png");
            enemyImage=loadImage("src//images//flower.png");
        }
        else if (kind =="rush"){
            moving=true;  shoting=false; flying=false;speed=15;healthy_bar=2;enemy_y=330;
            width= 80;height=60;
            enemyImageIcon=new ImageIcon("src//images//rush.png");
            enemyImage=loadImage("src//images//rush.png");
        }
        else if (kind =="bee"){
            Random random=new Random();
            enemy_y=10+random.nextInt(6)*25;
            moving=true;  shoting=false; flying=false;speed=4;healthy_bar=1;
            width= 50;height=80;
            enemyImageIcon=new ImageIcon("src//images//bee.png");
            enemyImage=loadImage("src//images//bee.png");
        }
        else if (kind=="plane"){
            Missilesnum=3;
            width= 80;height=60;
            enemy_y=10+random.nextInt(6)*25;
            moving=true;  shoting=true; flying=true;speedx=2;healthy_bar=1;
            enemyImageIcon=new ImageIcon("src//images//plane1.png");
            enemyImage=loadImage("src//images//plane1.png");
            changeable=true;
            changeableimage=new Image[2];
            changeableimage[0]=loadImage("src//images//plane1.png");
            changeableimage[1]=loadImage("src//images//plane2.png");
        }
        else if (kind=="thief"){
            width= 54;height=60;
            enemy_y=350;
            moving=true;  shoting=false; flying=false;speedx=0.5;healthy_bar=3;
            enemyImageIcon=new ImageIcon("src//images//thief1.png");
            enemyImage=loadImage("src//images//thief1.png");
            changeable=true;
            changeableimage=new Image[2];
            changeableimage[0]=loadImage("src//images//thief1.png");
            changeableimage[1]=loadImage("src//images//thief2.png");
        }
        else if (kind=="star"){
            width= 60;height=70;
            enemy_y=random.nextInt(100)*50;
            moving=true;  shoting=false; flying=true;speedx=4;healthy_bar=100;
            enemyImageIcon=new ImageIcon("src//images//star.png");
            enemyImage=loadImage("src//images//star.png");
        }
        else if (kind =="jet_pack"){
            width= 60;height=80;
            enemy_x= random.nextInt(600)+50;
            enemy_y=600;
            moving=true;  shoting=false; flying=true;speedx=0;healthy_bar=1;
            enemyImageIcon=new ImageIcon("src//images//jetpack.png");
            enemyImage=loadImage("src//images//jetpack.png");
        }
        else if (kind=="kite"){
            width= 80;height=60;
            enemy_y=10+random.nextInt(6)*25;
            moving=true;  shoting=false; flying=true;speedx=2;healthy_bar=1;
            enemyImageIcon=new ImageIcon("src//images//kite.png");
            enemyImage=loadImage("src//images//kite.png");
        }
        else if (kind=="Missiles"){
            width= 130;height=30;
            moving=true;  shoting=false; flying=true;speedx=2;healthy_bar=1;
            enemyImageIcon=new ImageIcon("src//images//Missiles.png");
            enemyImage=loadImage("src//images//Missiles.png");
            changeable=true;
            changeableimage=new Image[2];
            changeableimage[0]=loadImage("src//images//导弹1.png");
            changeableimage[1]=loadImage("src//images//导弹2.png");
        }
        else if (kind=="rushing"){
            width= 80;height=30;
            moving=true;  shoting=false; flying=true;speedx=2;healthy_bar=1;
            enemyImageIcon=new ImageIcon("src//images//冲击1.png");
            enemyImage=loadImage("src//images//冲击1.png");
            changeable=true;
            changeableimage=new Image[4];
            changeableimage[0]=loadImage("src//images//冲击1.png");
            changeableimage[1]=loadImage("src//images//冲击2.png");
            changeableimage[2]=loadImage("src//images//冲击3.png");
            changeableimage[3]=loadImage("src//images//冲击4.png");
        }
        else if (kind=="rushingred"){
            width= 40;height=80;
            moving=true;  shoting=false; flying=true;speedx=2;healthy_bar=1;
            enemyImageIcon=new ImageIcon("src//images//冲击红1.png");
            enemyImage=loadImage("src//images//冲击红1.png");
            changeable=true;
            changeableimage=new Image[4];
            changeableimage[0]=loadImage("src//images//冲击红1.png");
            changeableimage[1]=loadImage("src//images//冲击红2.png");
            changeableimage[2]=loadImage("src//images//冲击红3.png");
            changeableimage[3]=loadImage("src//images//冲击红4.png");
        }
    }
    //Function of uniform motion
    public void move(double dt,double groundMoveSpend,double speedx,double speedy){
        enemy_x-=dt*groundMoveSpend*speedx;
        enemy_y-=dt*groundMoveSpend*speedy;
    }
    //Acceleration function
    public void speedupmove(double dt,double groundMoveSpend,double speedx,double speedy,double speedxup,double speedyup){
        live +=dt;
        enemy_x-=dt*groundMoveSpend*speedx+dt*speedxup*live;
        enemy_y-=dt*speedyup*live;
    }
    //Change the image function
    public void changeimage(double dt,double time){
        if (changeable){
            changetime+=dt;
            if (changetime>time){
                if(changeableimage.length-1>remember){
                    remember+=1;}
                else if(changeableimage.length-1==remember){
                    remember=0;
                }
                enemyImage=changeableimage[remember];
                changetime=0.0;
            }
        }
    }
    //Subtract blood volume function
    public boolean setHealthy_bar(int a){
        healthy_bar-=a;
        return healthy_bar > 0;
    }
}
