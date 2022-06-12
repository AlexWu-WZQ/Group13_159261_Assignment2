package objects;

import Others.GenerateThingsRange;

import java.util.ArrayList;
import java.util.Random;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : an important class, which will carry many other elements in this game, like coins, thunder, etc.
 * It is noticeable that as to the attribute "move":  if "move" is true, it refers to the board which generate randomly from the right
 * side of the screen and gradually move to the left side of the screen.
 * If "move" is false, it only refers to the board on the ground, it's a fake and fixed board which won't move on the ground.
 * There are two main advantages: 1. it's convenient for collisions detection.
 * 2. it's easy to randomly generate something on this board through its position-X, position-Y and width.
 */
public class Board {
    public ArrayList<Coin> coins=new ArrayList<>();
    public ArrayList<Invincibility> invincibilities=new ArrayList<>();
    public ArrayList<Energy> energies=new ArrayList<>();
    public ArrayList<StrongerDamage> strongerDamages=new ArrayList<>();
    public ArrayList<WaterObstacle> waterObstacles=new ArrayList<>();
    public ArrayList<Thunder> thunders=new ArrayList<>();
    public int width;
    public int positionY;
    public double positionX;
    public int choose;
    public boolean move;
    public boolean draw;
    public int thick;
    public int begin,end;
    public int boardGap=150;
    public boolean havaCoin=true;
    public int imageWidth=50;

    public void generateCoin() {
        Random r = new Random();
        begin = r.nextInt(width/55);
        end = begin + r.nextInt((width/55)-begin)+2;
        int count = end-begin;
        //Creates a hollow pyramid coin area on a board of length greater than or equal to 5
        if(count>=5&&count%2!=0&&positionY!=390) {
            for(int i=0;i<count;i++) {
                if(i<(count+1)/2) {
                    coins.add(new Coin((int)positionX,(begin+i)*50,positionY-50*i));
                }else {
                    coins.add(new Coin((int)positionX,(begin+i)*50,positionY-50*(count-1-i)));
                }
            }
        }
        //Generates an even number of horizontal coins
        else if(count%2==0){
            for(int i=0;i<count;i++) {
                coins.add(new Coin((int)positionX,(begin+i)*50,positionY));
            }
        }
        else havaCoin=false;
    }
    public void generateStrongerDamage(){
        Random r = new Random();
        //The starting position of the object on the board, and the frequency of its occurrence
        int begin = r.nextInt(width/55);
        int count = r.nextInt(4);
        boolean add = true;
        if(count % 3 ==0){
            //Avoid overlapping
            for(Coin c :coins) {
                if ((begin + 1) * 50 >= c.relativeX && (begin + 1) * 50 <= c.relativeX + 50) {
                    add = false;
                    break;
                }
            }
            if(add) {
                strongerDamages.add(new StrongerDamage((int)positionX,begin*50,positionY-110));
            }
        }
    }

    public void generateEnergy(){
        Random r = new Random();
        //The starting position of the object on the board, and the frequency of its occurrence
        int begin = r.nextInt(width/55);
        int count = r.nextInt(4);
        boolean add = true;
        int beginLen=begin*50;
        int endLen=beginLen+imageWidth;
        if(count % 3==0){
            //Avoid overlapping
            for(Coin c :coins) {
                if ((begin + 1) * 50 >= c.relativeX && (begin + 1) * 50 <= c.relativeX + 50) {
                    add = false;
                    break;
                }
            }
            for(StrongerDamage b :strongerDamages) {
                if((beginLen >= b.relativeX && beginLen <= b.relativeX+imageWidth)||
                        (endLen>=b.relativeX&&endLen<=b.relativeX+imageWidth)) {
                    add = false;
                    break;
                }
            }if(add) {
                energies.add(new Energy((int)positionX,beginLen,positionY-110));
            }
        }
    }
    public void generateInvincibility(){
        Random r = new Random();
        //The starting position of the object on the board, and the frequency of its occurrence
        int begin = r.nextInt(width/55);
        int count = r.nextInt(4);
        boolean add = true;
        int beginLen=begin*50;
        int endLen=beginLen+imageWidth;
        if(count % 3 == 0){
            //Avoid overlapping
            for(Coin c :coins) {
                if ((begin + 1) * 50 >= c.relativeX && (begin + 1) * 50 <= c.relativeX + 50) {
                    add = false;
                    break;
                }
            }
            for(StrongerDamage b :strongerDamages) {
                if((beginLen >= b.relativeX && beginLen <= b.relativeX+imageWidth)||
                        (endLen>=b.relativeX&&endLen<=b.relativeX+imageWidth)) {
                    add = false;
                    break;
                }
            }
            for(Energy e :energies) {
                if((beginLen >= e.relativeX && beginLen <= e.relativeX+imageWidth)||
                        (endLen>=e.relativeX&&endLen<=e.relativeX+imageWidth)) {
                    add = false;
                    break;
                }
            }
            if(add) {
                invincibilities.add(new Invincibility((int)positionX,beginLen,positionY-110));
            }
        }
    }
    public void generateThunder(){
        Random r=new Random();
        //The frequency of its occurrence
        int t=r.nextInt(6);
        if(t!=0||positionY==170) return;
        thunders.add(new Thunder(positionX+r.nextInt(width-80),0,positionY));
    }
    public void generateWater(){
        ArrayList<GenerateThingsRange> generateThingsRanges=new ArrayList<>();
        //Avoid overlapping
        if(havaCoin){
            generateThingsRanges.add(new GenerateThingsRange(positionX,positionX+begin*55));
            generateThingsRanges.add(new GenerateThingsRange(positionX+end*55,positionX+width));
        }
        else{
            generateThingsRanges.add(new GenerateThingsRange(positionX,positionX+width));
        }
        for(GenerateThingsRange g:generateThingsRanges){
            Random r=new Random();
            //The frequency of its occurrence
            int choose=r.nextInt(5);
            if(choose==0) return;
            if(positionY==390&&g.range>150){
                waterObstacles.add(new WaterObstacle((int)g.left+r.nextInt((int)g.range-150),positionY));
            }
        }
    }
    public Board(int maxLen,boolean move, int positionY){
        this.move=move;
        thick=30;
        //Boards generated in the sky
        if(move){
            positionX=maxLen+boardGap;
            Random rand1 = new Random();
            width=150+rand1.nextInt(350);
            Random rand2= new Random();
            choose=rand2.nextInt(3);
            draw=true;
            this.positionY=positionY;
            if(positionY==390){
                draw=false;
                width+=50;
            }
            generateCoin();
            generateStrongerDamage();
            generateEnergy();
            generateInvincibility();
            generateThunder();
            generateWater();
        }

        //Boards generated on the ground, the "draw" attribute is false
        // it's a fixed and fake board which won't be paint or move in this game
        // this will play an important role in the basic collisions between the monster and boards.
        else{
            draw=false;
            positionX=0;
            width=maxLen;
            this.positionY=positionY;
        }
    }
}
