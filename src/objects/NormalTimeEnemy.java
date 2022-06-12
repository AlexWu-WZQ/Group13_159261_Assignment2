package objects;


import Others.ImageOperations;

import java.awt.*;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : normal time enemies design
 */

public class NormalTimeEnemy extends ImageOperations {
    public double positionX;
    public double positionY;
    public String kind;
    public Image[] idleImages=new Image[50];
    public Image[] hitImages=new Image[50];
    public Image[] walktImages=new Image[50];
    public Image[] dieImages=new Image[50];
    public double boardPositionY;

    public double timer=0;
    public boolean isHitting;
    public int height=55;
    public int width=70;
    public int health=7;
    public int hurtDamage=3;
    public boolean beHit=false;
    public double speedbeishu=1.3;
    public boolean walk;
    public int cur_ind;
    public boolean exist=true;
    public boolean die=false;
    public double dieround=0.5;
    public double hitround=0.5;
    public boolean touchplayer=false;

    public NormalTimeEnemy(String kind,double x,double y) {
        this.kind = kind;
        this.positionX = x;
        this.boardPositionY=y;
        this.positionY = y - height+5;
        this.timer = 0;
        isHitting = false;
        walk= y == 390;
        if(kind.equals("monster2")){
            width=80;
            height=70;
            this.positionY=y-height+10;
        }
        else if(kind.equals("flying")){
            walk=true;
            height=70;
            width=100;
        }
    }
    public int numHurt,numWalk,numIdle,numSmoke;
    public void quickCopy(NormalTimeEnemy n){
        this.hitImages=n.hitImages;
        this.walktImages=n.walktImages;
        this.idleImages=n.idleImages;
        this.dieImages=n.dieImages;
        this.numHurt=n.numHurt;
        this.numWalk=n.numWalk;
        this.numIdle=n.numIdle;
        this.numSmoke=n.numSmoke;
    }



    public void initSelf(int numHurt,int numWalk, int numIdle, int numSmoke){
        this.numHurt=numHurt;
        this.numWalk=numWalk;
        this.numIdle=numIdle;
        this.numSmoke=numSmoke;
        for(int i=0;i<numHurt;i++){
            this.hitImages[i]=loadImage("src//images//"+kind+"//Hurt_"+i+".png");
        }
        for(int i=0;i<numWalk;i++){
            this.walktImages[i]=loadImage("src//images//"+kind+"//Walking_"+i+".png");
        }
        for(int i=0;i<numIdle;i++){
            this.idleImages[i]=loadImage("src//images//"+kind+"//Idle_"+i+".png");
        }
        for(int i=0;i<numSmoke;i++){
            this.dieImages[i]=loadImage("src//images//"+kind+"//Dying_"+i+".png");
        }
    }

    public void update(double dt,double groundMoveSpeed){
        if(die){
            cur_ind=getCurImage(timer,dieround,numSmoke);
            if(timer>dieround) exist=false;
        }
        else if(isHitting){
            positionX-=groundMoveSpeed*dt;
            cur_ind=getCurImage(timer,hitround,numHurt);
            if(timer>hitround) isHitting=false;
        }
        else if(walk){
            positionX-=groundMoveSpeed*dt*speedbeishu;
            cur_ind=getCurImage(timer,2.5,numWalk);
        }
        else{
            positionX-=groundMoveSpeed*dt;
            cur_ind=getCurImage(timer,2.5,numIdle);
        }
        timer+=dt;
    }

    public int getCurImage(double time, double round, int num) {
        int i = (int)Math.floor(((timer % round) / round) * num);
        // Check range
        if(i >= num) {
            i = num-1;
        }
        return i;
    }

    public void sufferDamage(int a){
        if(die) return;
        //if this enemy dies, it means that more operations of checking collisions is meaningless and will let the enemy play more useless action
        if(health-a>0){
            isHitting=true;
        }
        else{
            die=true;
        }
        health-=a;
        timer=0.0;
    }
}
