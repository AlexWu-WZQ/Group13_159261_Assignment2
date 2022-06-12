package controller;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.awt.geom.Line2D;

import Others.HitBox;
import objects.*;
import views.Character;

import static views.GameStartView.*;
import static views.RankingUpdate.characters;
import static views.SelectCharacterView.selectedCharacterNumber;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : the main game controller to run this game.
 * However, if you want to start the game. please run the class GameStartView to run this game.
 */


public class GameController extends GameEngine {
    public static GameController mainGame=new GameController();
    //AUDIOS
    AudioClip startSound;
    AudioClip jumpSound1;
    AudioClip jumpSound2;
    AudioClip endSound;
    AudioClip loseLifePointSound;
    AudioClip getLifePointSound;
    AudioClip getCoinSound;
    AudioClip getSkillSound;
    AudioClip bulletSound;
    AudioClip enemySufferAttackSound;
    AudioClip gameBGM;
    //Sound initiation
    public void initSound() {
        startSound = loadAudio("src//images//startSound.wav");
        endSound = loadAudio("src//images//endSound.wav");
        jumpSound1 = loadAudio("src//images//jumpSound1.wav");
        jumpSound2 = loadAudio("src//images//jumpSound2.wav");
        loseLifePointSound = loadAudio("src//images//loseLifePointSound.wav");
        getLifePointSound = loadAudio("src//images//getLifePointSound.wav");
        getCoinSound = loadAudio("src//images//getCoinSound.wav");
        getSkillSound = loadAudio("src//images//getSkillSound.wav");
        bulletSound = loadAudio("src//images//bulletSound.wav");
        enemySufferAttackSound = loadAudio("src//images//enemySufferAttackSound.wav");
        gameBGM = loadAudio("src//images//gameBGM.wav");
        startAudioLoop(gameBGM);
        playAudio(startSound);
    }


    /**
     * Monster
     */
    double monsterX,monsterY,lastMonsterX,lastMonsterY;
    double monsterHeight=60,monsterWidth=50;
    // some basic attributes of the monster

    Image[] monsterAction=new Image[3];
    double walkRound,walkTimer;
    double velocityY;
    double velocityX=200;
    double gravity;
    boolean inTheSky=true;
    //a very important variable to demonstrate whether the monster is in the sky.
    // if it's "true", the monster will be influenced by the gravity; Otherwise, it's velocity of Y-direction won't be influenced

    boolean failing=false;
    // when the monster fell from the boards in the sky, he can also jump for two more times. "failing" attribute is also
    // a very important variable to enable things can run as expected.

    boolean secondJump=true;
    // control the monster can jump for one more time in the sky


    boolean moveForward=false;
    boolean moveBack=false;
    int curInd;
    // the index of the sprites of the monster

    // initSome basic i
    public void initMonster(){
        monsterX=100;
        monsterY=boards.get(0).positionY-monsterHeight;
        inTheSky=false;
        gravity=0;
        velocityY=0;
        beHit=false;
        walkRound=0.5;
        monsterAction[0]=loadImage("src//images//player"+ selectedCharacterNumber +"1.png");
        monsterAction[1]=loadImage("src//images//player"+ selectedCharacterNumber +"2.png");
    }

    /* a very import method to update the status of monster in the sky by analysing the relationship between two lines:
    Line 1:  the line between the last position in the last Update() method and cur position (positionX,position Y) of the monster
    Line 2:  different edges of the board in the sky.
     */
    public void updateInTheSky(double dt){
        if(inTheSky){
            if(monsterY<0&&velocityY<0) {
                velocityY=0;
            }
            for(Board b:boards){
                // if the monster's feet touch the upper edge of the board, it will stay on it.
                if(velocityY>0&&(Line2D.linesIntersect(b.positionX,b.positionY,b.positionX+b.width,b.positionY,
                        lastMonsterX+monsterWidth,lastMonsterY+monsterHeight,monsterX+monsterWidth,monsterY+monsterHeight)
                        ||Line2D.linesIntersect(b.positionX,b.positionY,b.positionX+b.width,b.positionY,
                        lastMonsterX,lastMonsterY+monsterHeight,monsterX,monsterY+monsterHeight))
                ){
                    inTheSky=false;
                    velocityY=0;
                    monsterY=b.positionY-monsterHeight;
                }

                // if the monster's head touch the lower edge of the board, it will be rebounced back.
                else if(velocityY<0&&(Line2D.linesIntersect(b.positionX,b.positionY+boardThick,b.positionX+b.width,b.positionY+boardThick,
                        lastMonsterX+monsterWidth,lastMonsterY,monsterX+monsterWidth,monsterY)||
                        Line2D.linesIntersect(b.positionX,b.positionY+boardThick,b.positionX+b.width,b.positionY+boardThick,
                                lastMonsterX,lastMonsterY,monsterX,monsterY)
                )
                ){
                    monsterY=b.positionY+boardThick+1;
                    velocityY*=-0.7;
                }

                // if the monster's right side touch the left edge of the board, it will be carried by the board, especially for the x-direction.
                else if(
                        Line2D.linesIntersect(monsterX+monsterWidth,monsterY,monsterX+monsterWidth,monsterY+monsterHeight, b.positionX,b.positionY,b.positionX+b.width,b.positionY)
                                ||
                                Line2D.linesIntersect(monsterX+monsterWidth,monsterY,monsterX+monsterWidth,monsterY+monsterHeight, b.positionX,b.positionY+boardThick,b.positionX+b.width,b.positionY+boardThick)
                ){
                    moveForward=false;
                    monsterX-=groundMoveSpeed*dt;

                    gravity=1000;
                    // when the monster touch the left edge of the board, because of the friction, the gravity will decrease to a certain extent.
                    secondJump=false;
                    // the monster can't jump for one more time because of the influence of the board
                }
            }
        }
        else{
            int flag=1;
            secondJump=true;

            // check the collisions between the monster every boards
            for(Board cur:boards){
                if (Line2D.linesIntersect(monsterX+15,monsterY+monsterHeight,monsterX+15+monsterWidth,monsterY+monsterHeight,
                        cur.positionX,cur.positionY,cur.positionX+cur.width,cur.positionY)) {
                    flag=0;
                    break;
                }
            }

            //if flag==1, it means that the monster will fall down from the board
            if(flag==1){
                inTheSky=true;
                failing=true;
                velocityY=100;
            }
        }

        // limit that the monster can't exceed the edge of the screen
        if(monsterX+monsterWidth>width()){
            monsterX=lastMonsterX;
        }
        // if the monster miss from the left edge of the screen, it will definitely lose some health points.
        // and its position will also be reset
        else if(monsterX+monsterWidth<0){
            monsterX=100;
            health-=3;
            hitTimer=0;
            beHit=true;
            playAudio(loseLifePointSound);
            invincibilities.restart();
        }

        // update the gravity according to current status of the monster (whether in the sky or not)
        if(inTheSky) gravity=2000;
        else gravity=0;
    }

    public void updateMonster(double dt){
        // ** when the monster be hit by something in this game, its "beHit" status will be changed
        // ** at this time, the round of different actions (hit or normal walk) will also be different.
        walkTimer+=dt;
        if(!beHit) walkTimer%=walkRound; else walkTimer%=hit_round;


        // update the position X and position Y.
        velocityY+=dt*gravity;
        monsterY+=velocityY*dt;
        if(moveForward) monsterX+=dt*velocityX;
        if(moveBack) monsterX-=dt*velocityX;

        updateInTheSky(dt);

        // through record the last position of the monster, some collisions between monster and board can be deal with better.
        lastMonsterX=monsterX;
        lastMonsterY=monsterY;
    }

    // paint the monster according to its current status.
    public void paintMonster(){
        if(!beHit){
            if(!inTheSky)curInd=getCurImage(walkTimer,walkRound,2);
            drawImage(monsterAction[curInd],monsterX,monsterY,monsterWidth,monsterHeight);
        }
        else{
            curInd=getCurImage(walkTimer,hit_round,2);
            drawImage(beingHit[curInd],monsterX,monsterY,monsterWidth,monsterHeight);
        }
    }

    boolean beHit=false;
    Image[] beingHit =new Image[3];
    double hitTimer=0;

    double hit_round=0.1;
    double hit_duration=0.5;
    // when the monster is hit, all images about hitting will be displayed once in a hit_round, but this will last "hit_duration".

    public void initIsHit(){
        beingHit[0]=loadImage("src//images//dino1powerup.png");
        beingHit[1]=loadImage("src//images//dino2powerup.png");
    }

    // if the monster is hit, update the hit_timer to display right images of the hit.
    public void updateIsHit(double dt){
        if(!beHit)return;
        hitTimer+=dt;
        if(hitTimer>=hit_duration) {
            hitTimer=0;
            beHit=false;
        }
    }




    /**
     * some convenient tools of the game design
     */
    // get the correct index of the sprites according to the timer and round.
    public int getCurImage(double time, double round, int num) {
        time%=round;
        double result=num*time/round;
        return (int) (result%num);
    }


    // when the monster is hitting by something of the game (like enemies, thunder, etc), this method will be used.
    public void isHitting(int damage){
        if(invincibilities.skillActivation) return;
        health-=damage;
        hitTimer=0;
        beHit=true;
        playAudio(loseLifePointSound);
    }

    // check the collisions between two rectangles
    public boolean checkCoilRects(int x1,int y1,int w1,int h1,int x2,int y2,int w2,int h2){
        Rectangle a=new Rectangle(x1,y1,w1,h1);
        Rectangle b=new Rectangle(x2,y2,w2,h2);
        return a.intersects(b);
    }


    /**
     * Background
     */
    double groundMoveSpeed;
    /*a very important variable that decide the speed of the game. Most of the things in this game (like thunder, cloud, boards) are moved
    according to this variable. it let the things of this game move in great order
     */

    // different game modes (normal time, coin time, monster time) have different backgrounds.
    Image ground,coinTimeGround,monsterTimeGround;
    double groundMiddle=width();
    // a very import variable that let the background move

    Image groundPiece1,groundPiece2;
    public void initGround(){
        ground=loadImage("src//images//background.png");
        coinTimeGround=loadImage("src//images//coinSceneBack.jpg");
        monsterTimeGround=loadImage("src//images//monsterSceneBack.jpg");
    }
    public void updateGround(double dt){
        groundMiddle=(groundMiddle-groundMoveSpeed*dt+width())%width();
    }

    //paint the background according to current game mode
    public void paintGround(){
        clearBackground(width(),height());
        if(coinTime){
            if(groundMiddle>1)groundPiece1=subImage(coinTimeGround,width()-(int)groundMiddle,0,(int)groundMiddle,height());
            if(groundMiddle<width()&&groundMiddle>0)groundPiece2=subImage(coinTimeGround,0,0,width()-(int)groundMiddle,height());
        }
        else if(monsterTime){
            if(groundMiddle>1)groundPiece1=subImage(monsterTimeGround,width()-(int)groundMiddle,0,(int)groundMiddle,height());
            if(groundMiddle<width()&&groundMiddle>0)groundPiece2=subImage(monsterTimeGround,0,0,width()-(int)groundMiddle,height());
        }
        else{
            if(groundMiddle>1)groundPiece1=subImage(ground,width()-(int)groundMiddle,0,(int)groundMiddle,height());
            if(groundMiddle<width()&&groundMiddle>0)groundPiece2=subImage(ground,0,0,width()-(int)groundMiddle,height());
        }
        if(groundPiece1!=null)drawImage(groundPiece1,0,0,groundMiddle,height());
        if(groundPiece2!=null)drawImage(groundPiece2,groundMiddle,0,width()-(int)groundMiddle,height());
    }




    /**
     * Here are all the lists in this game.
     */
    public ArrayList<Board> boards=new ArrayList<>();
    public ArrayList<NormalTimeEnemy> normalTimeEnemies=new ArrayList<>();
    public ArrayList<Cloud> clouds=new ArrayList<>();
    public ArrayList<SuperWeapon> superWeapons=new ArrayList<>();
    public ArrayList<Bullet> bullets = new ArrayList<>();


    /**
     * Board
     */
    int boardThick=30;
    /*
     * initialise the boards of the game;
     * firstly, a "fake" board with "shuxing" 0 on the ground will be added to the Arraylist boards, which won't be drawn.
     * That is mainly because, as you can see in the method updateInTheSky(), you can see all the basic collisions are based on the boards.
     * As to this reason, set a fixed and fake board on the ground will be easy to deal will basic collision of this game
     */
    public void initBoard(){
        boards.clear();
        boards.add(new Board(width(),false,390));
        if(coinTime||monsterTime) return;
        boards.add(new Board(width(),true,240));
    }
    public int curChoose=0;
    public int lastChoose=0;
    // two variables that make the board which is generated is different from the last one.

    public void updateBoard(double dt){

        // randomly generate a new board
        if(boards.size()!=0){
            int length=boards.size();
            // when the right edge of a new board entirely enter the screen, as this time, a new board can be generated.
            if(boards.get(length-1).positionX+boards.get(length-1).width<width()){
                Board newBoard;
                while(curChoose==lastChoose){
                    curChoose=rand(3);
                }
                // randomly generate a board with different height.
                if(curChoose==0){
                    newBoard=new Board(width(),true,390);
                    /*this a board on the ground, however, its attribute "moving" is true, which means that it can move as the
                    background move, however, it won't be drawn on the screen
                     */

                    boards.add(newBoard);
                }
                else if(curChoose==1){
                    newBoard=new Board(width(),true,240);
                    //generate a board in the sky will positionY-240;
                    boards.add(newBoard);
                }
                else if(curChoose==2){
                    newBoard=new Board(width(),true,170);
                    //generate a board in the sky will positionY-170;
                    boards.add(newBoard);
                }
                lastChoose=curChoose;
            }
        }


        //let the boards move, the attributes "move" of which is true;
        for (Board board : boards) {
            if (board.move) board.positionX -= groundMoveSpeed * dt;
        }

        // remove the boards which are out-of-bounds.
        if(boards.size()>1&&boards.get(1).positionX+boards.get(1).width+400<=0) boards.remove(1);

        updateThunder(dt);
        updateWater(dt);
        updateInvincibility(dt);
        updateStrongerDamage(dt);
        updateEnergy(dt);
        updateCoin(dt);

    }

    //only in normal time, the moving boards can be drawn
    public void paintBoard(){
        Image ha=subImage(ground,0,450,width(),boardThick);
        for (Board board : boards) {
            if(board.draw||coinTime||monsterTime) drawImage(ha, board.positionX, board.positionY, board.width, boardThick);
        }

        // draw the elements on the board.
        drawThunder();
        paintWater();
        paintInvincibility();
        paintStrongerDamage();
        paintEnergy();
        paintCoin();
    }


    /**
     * Normal Time enemies
     */
    public double generateEnemyTimer=0;
    public double grnerateEnemyRound=2;
    NormalTimeEnemy monster1=new NormalTimeEnemy("monster1",0,0);
    NormalTimeEnemy monster2=new NormalTimeEnemy("monster2",0,0);
    NormalTimeEnemy monster3=new NormalTimeEnemy("flying",0,0);
    public void initEnemiesImage(){
        monster1.initSelf(12,18,24,10);
        monster2.initSelf(12,42,35,15);
        monster3.initSelf(12,36,0,5);
    }
    public void updateNormalTimeEnemy(double dt){
        for(int j=0;j<normalTimeEnemies.size();j++){
            NormalTimeEnemy n=normalTimeEnemies.get(j);
            //check the collisions between bullets and enemies
            for(int i=0;i<bullets.size();i++){
                Bullet b=bullets.get(i);
                if(checkCoilRects(b.hitBox.x,b.hitBox.y,b.hitBox.w,b.hitBox.h,(int)n.positionX-10,(int)n.positionY,n.width,n.height)&&!n.die){
                    playAudio(enemySufferAttackSound);
                    n.sufferDamage(b.hurt);
                    bullets.remove(b);
                    if(n.die) score+=10;
                }
            }
            //check the collisions between super weapon and enemies
            for(SuperWeapon s:superWeapons){
                for(HitBox h:s.hitBoxes){
                    if(checkCoilRects(h.x,h.y,h.w,h.h,(int)n.positionX-10,(int)n.positionY,n.width,n.height)&&!n.die){
                        n.sufferDamage(s.hurt);
                        score+=10;
                        playAudio(enemySufferAttackSound);
                    }
                }
            }
            //check the collisions between monster and enemies
            if(checkCoilRects((int)monsterX,(int)monsterY,(int)monsterWidth,(int)monsterHeight,(int)n.positionX-10,(int)n.positionY,n.width,n.height)){
                if(!n.isHitting&&!n.touchplayer){
                    isHitting(n.hurtDamage);
                    n.touchplayer=true;
                }
                //if the enemy is hit by the bullet, the monster can avoid this damage when collide the enemy

                //if the monster has a invincibility, it can destory the enemy directly when collide the enemy.
                if(invincibilities.skillActivation&&!n.die){
                    n.sufferDamage(1000);
                    score+=10;
                    playAudio(enemySufferAttackSound);
                }
            }
            n.update(dt,groundMoveSpeed);
            if(!n.exist||n.positionX+n.width<0) normalTimeEnemies.remove(n);
        }
        generateEnemyTimer+=dt;
        if(generateEnemyTimer<=grnerateEnemyRound) return;
        generateEnemyTimer=0;
        grnerateEnemyRound=1+rand(3);
        int positionY=390;
        for(Board board:boards){
            if(board.positionX+board.width-100>width()&&board.positionX<width()){
                positionY=Math.min(positionY,board.positionY);
            }
        }
        int choose=rand(5);
        if(choose==0){
            NormalTimeEnemy n=new NormalTimeEnemy("flying",width(),50+rand(75));
            n.quickCopy(monster3);
            normalTimeEnemies.add(n);
        }
        else if(choose<=2){
            choose=rand(2);
            NormalTimeEnemy n;
            if(choose==1){
                n = new NormalTimeEnemy("monster1", width(), positionY);
                n.quickCopy(monster1);
            }
            else {
                n = new NormalTimeEnemy("monster2", width(), positionY);
                n.quickCopy(monster2);
            }
            normalTimeEnemies.add(n);
        }
    }
    public void paintNorTimeEnemy(){
        for(NormalTimeEnemy n:normalTimeEnemies){
            if(n.die){
                drawImage(n.dieImages[n.cur_ind],n.positionX,n.positionY,n.width,n.height);
            }
            else if(n.isHitting){
                drawImage(n.hitImages[n.cur_ind],n.positionX,n.positionY,n.width,n.height);
            }
            else if(n.walk){
                drawImage(n.walktImages[n.cur_ind],n.positionX,n.positionY,n.width,n.height);
            }
            else{
                drawImage(n.idleImages[n.cur_ind],n.positionX,n.positionY,n.width,n.height);
            }
        }
    }


    /**
     * clouds
     */
    int cloudTimer;
    public void initCloud(){
        clouds.clear();
        cloudTimer=50+rand(100);
        clouds.add(new Cloud(width()));
    }
    public void updateCloud(double dt){
        cloudTimer-=dt;
        if(cloudTimer<=0){
            clouds.add(new Cloud(width()));
            cloudTimer=50+rand(100);
        }
        //update the position of the cloud
        for(Cloud a:clouds){
            a.positionX-=groundMoveSpeed*dt;
        }
        // remove the cloud if it's out of bounds.
        if(clouds.size()>0&&clouds.get(0).positionX+clouds.get(0).width<0) clouds.remove(0);
    }
    public void paintCloud(){
        for(Cloud a:clouds){
            drawImage(loadImage(a.link),a.positionX,a.positionY,a.width,a.height);
        }
    }



    /**
     * when a new element of the game generate (many new elements will generate in this game),
     * if it always loads images by accessing the memory of computer, it will waster a lot of time.
     * so we just set some elements which doesn't actually exist in the real game, when we need to paint something, we can use these elements' images.
     */
    Invincibility invincibilities;
    Energy energies;
    StrongerDamage strongerDamage;
    WaterObstacle waterObstacle;
    Thunder thunder;
    Coin coins;
    public void initElementsOnTheBoard(){

        // initialise the skill-- invincibilities: it means that the monster are well protected
        invincibilities=new Invincibility(0,width(),boards.get(0).positionY);
        invincibilities.image=loadImage("src//images//invincibility.png");
        invincibilities.relativeX=width();
        for(int a = 0; a<8; a+=1){
            String path="src//images//"+ a+".png";
            invincibilities.images[a]=loadImage(path);
        }

        // initialise the skill-- energy: you can recover some healthy points
        energies=new Energy(0,width(),boards.get(0).positionY);
        energies.image=loadImage("src//images//poison2.png");
        energies.relativeX=width();


        // initialise the skill-- stronger Damage: the normal bullets have more damages
        strongerDamage=new StrongerDamage(0,width(),boards.get(0).positionY);
        strongerDamage.image=loadImage("src//images//strongerdamage.png");
        strongerDamage.relativeX=width();



        //// initialise the coins on the board
        coins=new Coin(0,width(),boards.get(0).positionY);
        coins.image[0]=loadImage("src//images//coin1.png");
        coins.image[1]=loadImage("src//images//coin2.png");
        coins.relativeX=width();


        //initialise the thunder
        thunder=new Thunder(0,0,0);
        Image wholeImage=loadImage("src//images//thunderh.png");
        for(int i=0;i<8;i++) thunder.images[i]=subImage(wholeImage,i*64,0,64,240);


        //initialise the water Obstacle
        waterObstacle=new WaterObstacle(width(),boards.get(0).positionY);
        waterObstacle.image=loadImage("src//images//water1.png");
    }


    SuperWeapon superWeapon;
    public void initOtherElements(){
        initSuperWeapon();
        initRecover();
        initFonts();
        initInfo();
        initBomb();
        initSuperWeapon();
        initSkillBar();
        initEnemiesImage();
        initGround();
        initIsHit();
        initCoinOfCoinTime();
    }




    /**
     * Some actions of the monster.
     */
    double shootGap=0.5;
    // it is also the cool time of the normal weapon, it means that you can only fire once for every 0.5 seconds.

    public void jump(){
        // if the monster failing from one board or currently stay on one board, it can jump for two more times.
        // it the monster is in the sky because of falling from the boards, it can also jump for two more times.
        if(failing||!inTheSky){
            velocityY=-700;
            inTheSky=true;
            failing=false;
            playAudio(jumpSound1);
        }
        // the monster is in the sky now, so it can jump for only one more time.
        else if(secondJump){
            velocityY=-700;
            secondJump=false;
            playAudio(jumpSound2);

        }
    }
    public void shoot(){
        // determine the maxbullets and shootgap through checking whether the invincibilities is active or not.
        if(invincibilities.skillActivation||monsterTime){
            maxBullets=10;
            shootGap=0.3;
        }
        else{
            maxBullets=5;
            shootGap=0.5;
        }

        Bullet bullet = new Bullet(monsterX + monsterWidth/2, monsterY + monsterHeight / 2);
        if(strongerDamage.skillActivation||monsterTime) bullet.hurt=5;
        // update its damage through checking whether the stronger damage is active or not.


        bullet.updateOwnImage();


        if(remainingBullets.abailable&&bullets.size()<maxBullets){
            // remainingBullets is the images on the right bottom corner of the game.
            // when it's available, you can shoot, then it will go to cool time, during which you can't shoot bullet.
            // after a shoot gap, you can shoot another bullet again.
            bullets.add(bullet);
            remainingBullets.abailable=false;
            playAudio(bulletSound);
        }
    }
    public void shootSuperWeapon(){
        if(superWeapon.abailable){
            superWeapons.add(new SuperWeapon(monsterX,monsterY-40));
            superWeapon.abailable=false;
            playAudio(bulletSound);
        }
    }



    /**
     * display the some information about the monster of this game
     */
    Image heart;
    Image bulletImage;
    public void initInfo(){
        heart=loadImage("src//images//life.png");
        bulletImage=loadImage("src//images//bullet (1).png");
    }
    public void paintInfo(){
        drawImage(heart,0,10,130,60);
        changeColor(black);
        mGraphics.drawRoundRect(55,15,170,30,40,40);
        changeColor(200,0,0);
        mGraphics.fillRoundRect(55,15,(int)(health/maxHealth*170),30,40,40);
        changeColor(black);
        changeColor(white);
        mGraphics.setFont(f1);
        changeColor(50,50,50);
        mGraphics.fillRoundRect(330,15,250,30,30,30);
        changeColor(white);
        mGraphics.drawString("Score: "+score.toString(),355,40);
    }
    public void paintDeathInfo(){
        //protect game over still play the music
        //timer.stop();
        stopAudioLoop(gameBGM);
        Image result = loadImage("src/images/gameOver1.png");
        drawImage(result, 0, 0, mFrame.getWidth(), mFrame.getHeight());
        Image scoreLabel = loadImage("src/images/s"+ selectedCharacterNumber +".png");
        drawImage(scoreLabel, 230,250, 350, 175);
        drawBoldText(430, 335, String.valueOf(score), font, 50, Color.white);
        //get the data
        ranking.getRankData();
        //if score is more than score in the file, record it, else just show it at the end.
        for (Character character : characters) {
            if (character.characterNum == selectedCharacterNumber) {
                if (character.score >= score) {
                    drawBoldText(430, 400, String.valueOf(character.score), font, 50, Color.white);
                } else {
                    drawBoldText(430, 400, String.valueOf(score), font, 50, Color.white);
                    ranking.updateRank(selectedCharacterNumber,score);
                }
            }
        }
    }


    /**
     * draw the visual effects about "buff" (at the left-bottom corner) and "skill" (at the right-bottom corner)
     */
    //An important method to draw the dynamic changes of the skills and buffs.
    public void loadActivationImage(Image image,double x, double y,double w, double h,double alpha){
        drawImage(image,x,y,w,h);
        changeColor(white);
        drawRectangle(x,y,w,h,2);
        changeColor(new Color(0,0,0,0.5F));
        mGraphics.clipRect((int)x,(int)y,(int)w,(int)h);
        int r=(int)(Math.sqrt(Math.pow(w,2)+Math.pow(h,2))/2);
        int midX=(int)(x+w/2);
        int midY=(int)(y+h/2);
        mGraphics.fillArc(midX-r,midY-r,r*2,r*2,90,360-(int)alpha);
        mGraphics.setClip(0,0,800,600);
    }

    public void updateSkills(double dt){
        updateRecover(dt);
        updateInvincibility(dt);
        updateEnergy(dt);
        updateStrongerDamage(dt);
        updateSuperWeapon(dt);
    }
    public Skill remainingBullets=new Skill(670,500);
    public void initSkillBar(){
        remainingBullets.coolTime=shootGap;
        remainingBullets.activationImage=loadImage("src//images//remainingBullet.png");
        strongerDamage.positionX=50;
        strongerDamage.positionY=500;
        strongerDamage.activationImage=loadImage("src//images//strongerdamage.png");
        invincibilities.positionX=110;
        invincibilities.positionY=500;
        invincibilities.activationImage=loadImage("src//images//invincibility.png");
        superWeapon.positionX=730;
        superWeapon.positionY=500;
        superWeapon.activationImage=loadImage("src//images//superWeaponImage.png");
    }
    public void updateSkillBar(double dt){
        //Set the cooldown time of the super weapon in different scenarios
        if(monsterTime) superWeapon.coolTime=0.2;
        else superWeapon.coolTime=10;
        strongerDamage.updateBuff(dt);
        invincibilities.updateBuff(dt);
        superWeapon.updateSkill(dt);
        remainingBullets.updateSkill(dt);
    }
    public void drawSkillBar(){
        loadActivationImage(superWeapon.activationImage,superWeapon.positionX,superWeapon.positionY,superWeapon.activationWidth,superWeapon.activationHeight, superWeapon.alpha);
        loadActivationImage(remainingBullets.activationImage,remainingBullets.positionX,remainingBullets.positionY,remainingBullets.activationWidth,remainingBullets.activationHeight, remainingBullets.alpha);
        if(strongerDamage.skillActivation)
            loadActivationImage(strongerDamage.activationImage,strongerDamage.positionX,strongerDamage.positionY,strongerDamage.activationWidth,strongerDamage.activationHeight, strongerDamage.alpha);
        if(invincibilities.skillActivation)
            loadActivationImage(invincibilities.activationImage,invincibilities.positionX,invincibilities.positionY,invincibilities.activationWidth,invincibilities.activationHeight, invincibilities.alpha);
        mGraphics.setFont(f1);
        changeColor(white);
        mGraphics.drawString("Buff",50,490);
        mGraphics.drawString("Weapons",670,490);
        int a=maxBullets-bullets.size();
        if(a<0) a=0;
        changeColor(black);
        mGraphics.drawString(Integer.toString(a),695,550);
    }



    /**
     * control the visual effects between different scenes
     * It is noticeable that all the things will be reset when the screen is black(expect for some attributes like score, health, etc)
     * I also use "normal distribution" function to let the player get better game experience.
     * Because when I try adjusting the transparency at an average speed,some obvious changes can be seen(like the position of monster, change of the background)
     * this will give the player a bad game experience. So at this time, I think the normal distribution maybe a good choice.
     * It means that for most of the time, the screen are black. And it will also change to transparent gradually.
     */
    boolean normalTime=true;
    boolean coinTime=false;
    boolean monsterTime=false;
    // Three game modes

    double wholeGameTimer=0;
    double sceneRound=25;
    // the round of the normal time game round
    double specialSceneDuration=10;
    // the duration of the coinTime and MonsterTime

    String nextScene="normalTime";
    double sceneTranslateTimer=0;
    double sceneTranslateRound=4;
    boolean translateScene=false;
    // variables that display the translating scenes


    // when enter a differetn game mode, something need to be reset, which is different from the "Restart" funciont.
    public void diffScenesInit(){
        clearElements();
        initBoard();
        initMonster();
        invincibilities.restart();
        strongerDamage.skillActivation=false;
        remainingBullets.abailable=true;
        superWeapon.abailable=true;
    }

    // clear some dynamic elements of the screen, it's very important for changes among different scenes.
    public void clearElements(){
        enemies.clear();
        bombs.clear();
        bullets.clear();
        superWeapons.clear();
        normalTimeEnemies.clear();
    }


    /**
     * Methods that controls the translation between different game modes.
     * You will be more clear after watching the part 3 of our presentation.
     */
    public void updateScene(double dt){
        wholeGameTimer+=dt;
        if(normalTime){
            if(wholeGameTimer>=sceneRound-sceneTranslateRound/2){
                translateScene=true;
                if(nextScene.equals("normalTime")){
                    int a=rand(2);
                    if(a==0) nextScene="coinTime";
                    else nextScene="monsterTime";
                }
            }
            if(wholeGameTimer>=sceneRound){
                if(nextScene.equals("coinTime")){
                    coinTime=true;
                }
                else{
                    monsterTime=true;
                }
                normalTime=false;
                wholeGameTimer=0;
                diffScenesInit();
            }
        }
        else if(coinTime||monsterTime){
            if(wholeGameTimer>=specialSceneDuration-sceneTranslateRound/2){
                translateScene=true;
                nextScene="normalTime";
            }
            if(wholeGameTimer>=specialSceneDuration){
                normalTime=true;
                coinTime=false;
                monsterTime=false;
                wholeGameTimer=0;
                diffScenesInit();
            }
        }
    }
    public void updateTranslate(double dt){
        if(translateScene){
            sceneTranslateTimer+=dt;
            if(sceneTranslateTimer>=sceneTranslateRound){
                sceneTranslateTimer=0;
                translateScene=false;
            }
        }

    }
    public double zhengtaifenbu(double x,double u,double f){
        return Math.exp((-0.5)*Math.pow((x-u)/f,2))/(f*Math.sqrt(2*Math.PI));
    }
    public void paintTranslate(String ss){
        if(!translateScene) return;
        float zuida=(float)zhengtaifenbu(sceneTranslateRound/2,sceneTranslateRound/2,0.7);
        float cur=(float)zhengtaifenbu(sceneTranslateTimer,sceneTranslateRound/2,0.7);
        changeColor(new Color(0,0,0,cur/zuida));
        drawSolidRectangle(0,0,width(),height());
        changeColor(200,0,0);
        mGraphics.setFont(f2);
        mGraphics.drawString(ss+" is coming ... "+(int)(sceneTranslateRound-sceneTranslateTimer),100,200);
    }


    /**
     * the score will increase as the game go by.
     */
    double timePerScore=0.5;
    double scoreTimer=0;
    public void updateScore(double dt){
        scoreTimer+=dt;
        if(scoreTimer>timePerScore) {
            score+=1;
            scoreTimer=0;
        }
    }


    Integer score=0;
    boolean restart=false;
    boolean pause=false;
    double health=10;
    double maxHealth=10;
    // if you die in a game mode(e.g monsterTime), you can go back to the normal time mode to restart the game
    public void initForRestart(){
        score=0;
        groundMoveSpeed=250;
        normalTime=true;
        coinTime=false;
        monsterTime=false;
        health=maxHealth;
        wholeGameTimer=0;
        translateScene=false;
        sceneTranslateTimer=0;
        superWeapon.timer=superWeapon.coolTime+0.5;
        invincibilities.timer=invincibilities.skillDuration;
        strongerDamage.timer=strongerDamage.skillDuration;
    }


    public void updateForNormal(double dt){
        updateIsHit(dt);
        updateGround(dt);
        updateCloud(dt);
        updateBullet(dt);
        updateBoard(dt);
        updateScene(dt);
        updateTranslate(dt);
        updateSkills(dt);
        updateMonster(dt);
        updateSkillBar(dt);
        updateNormalTimeEnemy(dt);
    }


    /**
     * initialise the fonts of this game
     */
    Font f1;
    Font f2;
    public void initFonts(){
        try {
            f1 = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/images/font.ttf"))).deriveFont(17f);
            f2 = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/images/font.ttf"))).deriveFont(25f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Visual effects when the monster touch the buff "energy"
     */
    // when the monster touch the "energy" buff, it can recover some health.
    Image[] recoverImages=new Image[14];
    double recoverTimer=0;
    double recoverRound=1.5;
    int recoverInd;
    boolean isRecovering=false;
    public void initRecover(){
        for(int i=0;i<13;i++) recoverImages[i]=loadImage("src//images//xue"+i+".png");
    }
    public void updateRecover(double dt){
        if(!isRecovering) return;
        recoverTimer+=dt;
        // the recovering visual effect will only be displayed in just one round
        if(recoverTimer>recoverRound) {
            isRecovering=false;
            recoverTimer=0;
            return;
        }
        recoverInd=getCurImage(recoverTimer,recoverRound,13);
    }
    public void paintRecover(){
        if(!isRecovering) return;
        drawImage(recoverImages[recoverInd],monsterX-20,monsterY-55,100,150);
    }




    /**
     * Skills: normal bullet and super weapon
     */
    int maxBullets=3;
    public void updateBullet(double dt){
        for (Bullet bullet : bullets) {
            bullet.bullet_x+=4;
            bullet.timer=(bullet.timer+dt)%bullet.round;
            bullet.ind=getCurImage(bullet.timer,bullet.round,bullet.zhang);
            //update the hitbox of the bullet
            bullet.updateHitBox();
        }

        if(bullets.size()>0&&bullets.get(0).bullet_x>width()) bullets.remove(0);
    }

    public void paintBullet(){
        for (Bullet bullet : bullets) {
            drawImage(bullet.bulletImages[bullet.ind], bullet.bullet_x, bullet.bullet_y-20,bullet.width,bullet.height);
        }
    }
    public void initSuperWeapon(){
        superWeapons.clear();
        superWeapon=new SuperWeapon(0,0);
        for(int i=0;i<6;i++){
            superWeapon.images[i]=loadImage("src//images//superWeapon//"+i+".png");
        }
    }
    public void updateSuperWeapon(double dt){
        for(SuperWeapon s:superWeapons){
            s.bullet_x+=groundMoveSpeed*dt;
            s.weaponTimer=(s.weaponTimer+dt)%s.weaponRound;
            s.imageInd=getCurImage(s.weaponTimer,s.weaponRound,6);
            s.updateHitBox();
        }
    }
    public void drawSuperWeapon(){
        for(SuperWeapon s:superWeapons){
            drawImage(superWeapon.images[s.imageInd],s.bullet_x,s.bullet_y,s.width,s.height);
        }
    }








    /**
     * MonsterTime game mode
     */
    ArrayList<Enemy>enemies  = new ArrayList<>();
    Double enemyTimer=0.0;
    public Bomb bomb=new Bomb(0,0);
    ArrayList<Bomb> bombs = new ArrayList<>();
    public void initBomb() {
        for (int i = 0; i < 6; i++) bomb.images[i] = loadImage("src//images//zha" + i + ".png");
    }
    public void updateBomb(double dt){
        for(Bomb b:bombs){
            b.timer+=dt;
            if(b.timer>=b.round) b.exist=false;
            b.ind=getCurImage(b.timer,b.round,6);
        }

    }
    public void paintBomb(){
        for(Bomb b:bombs){
            if(b.exist)drawImage(bomb.images[b.ind],b.bomb_x,b.bomb_y);
        }
    }
    Double enemyTimer1=0.0;
    Double enemyTimer2=0.0;

    //Function to update the enemy&#039;s position and status
    public void updatemonstertimeEnemy(double dt){
        enemyTimer+=dt;//Single enemy refresh time
        enemyTimer2+=dt;//Multiple enemies refresh time
        String kind = "";
        double time =rand(2)+1;
        double time1 =rand(2)+2;
        if (enemyTimer>=time){//Single enemy refresh method
            int a=rand(5);
//                  if (a==0){kind="flower";}
            if (a==0){kind="thief";}if (a==1){kind="plane";}
            if (a==2){kind="star";}if (a==3){kind="jet_pack";}
            if (a==4){kind="kite";}
            //Add enemies to the collection
            Enemy enemy=new Enemy(kind);
            enemies.add(enemy);
            if (kind.equals("jet_pack")){Enemy enemy1=new Enemy("rushing");
                enemy1.enemy_y=enemy.enemy_y-40;enemy1.enemy_x=enemy.enemy_x-10;
                enemies.add(enemy1);}
            else if (kind.equals("kite")){Enemy enemy1=new Enemy("rushingred");
                enemy1.enemy_y=enemy.enemy_y-10;enemy1.enemy_x=enemy.enemy_x-40;
                enemies.add(enemy1);}
            enemyTimer=0.0;
        }
        if (enemyTimer2>=time1){//Multiple  enemies refresh  method
            int a=rand(3);
            if (a==0){kind="kite";}if (a==1){kind="star";}if (a==2){kind="jet_pack";}
            //Add enemies to the collection
            int number=rand(4)+2;//Bonus area gold coins
            if (kind.equals("kite")){
                Random random=new Random();
                double firsty=0.0+random.nextInt(50);
                for (int i=0;i<=number;i++){
                    Enemy enemy=new Enemy(kind);
                    enemy.enemy_y=50+firsty;
                    enemies.add(enemy);
                    firsty+=70;
                    Enemy enemy1=new Enemy("rushingred");
                    enemy1.enemy_y=enemy.enemy_y-10;enemy1.enemy_x=enemy.enemy_x-40;
                    enemies.add(enemy1);
                }
            }
            if (kind.equals("jet_pack")){
                Random random=new Random();
                double firstx=0.0+random.nextInt(500);
                for (int i=0;i<=number;i++){
                    Enemy enemy=new Enemy(kind);
                    enemy.enemy_y=600;
                    enemy.enemy_x=50+firstx;
                    enemies.add(enemy);
                    firstx+=70;
                    Enemy enemy1=new Enemy("rushing");
                    enemy1.enemy_y=enemy.enemy_y-40;enemy1.enemy_x=enemy.enemy_x-10;
                    enemies.add(enemy1);
                }
            }
            if (kind.equals("star")){
                number+=3;
                Random random=new Random();
                double firstx=0.0+random.nextInt(300)+400;
                double firsty=(firstx-800)/2;
                for (int i=0;i<=number;i++){
                    Enemy enemy=new Enemy(kind);
                    enemy.enemy_x=firstx;
                    enemy.enemy_y=firsty;
                    enemies.add(enemy);
                    firstx+=70;
                    firsty+=60;

                }

            }
            enemyTimer2=0.0;
        }

        //The method by which an aircraft generates a missile
        if (enemyTimer-enemyTimer1>2 || enemyTimer==0.0){
            enemyTimer1=enemyTimer;
            if (enemies.size()!=0){
                for (int i=0;i<enemies.size();i++){
                    Enemy enemy1=enemies.get(i);
                    if (enemy1.kind.equals("plane") &&enemy1.Missilesnum>0){
                        enemy1.Missilesnum-=1;
                        Enemy enemy=new Enemy("Missiles");
                        enemy.enemy_y=enemy1.enemy_y+enemy1.height ;
                        enemy.enemy_x=enemy1.enemy_x;
                        enemies.add(enemy);
                    }
                }
            }
        }

        //Player collides with enemy
        for(int i = 0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (checkCoilRects((int)monsterX,(int)monsterY,(int)monsterWidth,(int)monsterHeight,
                    (int)enemy.enemy_x,(int)enemy.enemy_y,(int)enemy.width,(int)enemy.height
            )){
                isHitting(enemy.hurt);
                bombs.add(new Bomb(enemy.enemy_x,enemy.enemy_y));
                enemies.remove(enemy);
                playAudio(enemySufferAttackSound);
            }
            switch (enemy.kind){
                case "thief" ->enemy.move(dt,groundMoveSpeed,1.2,0);
                case "star" ->enemy.move(dt,groundMoveSpeed,1,-0.5);
                case "kite", "rushingred" ->enemy.move(dt,groundMoveSpeed,2,0);
                case "plane" ->enemy.move(dt,groundMoveSpeed,0.5,0);
                case "jet_pack", "rushing" ->enemy.move(dt,groundMoveSpeed,0,0.5);
                case "Missiles" ->enemy.speedupmove(dt,groundMoveSpeed,0.5,0,+50,-100);
            }
        }
        for(Enemy enemy:enemies){//Dynamic picture time
            switch (enemy.kind){
                case "rushing", "rushingred" ->enemy.changeimage(dt,0.3);
                case "Missiles", "plane", "thief" ->enemy.changeimage(dt,0.2);
            }
        }
        for (int i = 0; i < enemies.size(); i++){//Out of range removal
            Enemy enemy=enemies.get(i);
            if (enemy.kind.equals("jet_kack") &&enemy.enemy_y<0){enemies.remove(enemy);}
            else if (enemy.kind.equals("plane") &&enemy.enemy_x<-80){enemies.remove(enemy);}
            else if (enemy.kind.equals("star") &&(enemy.enemy_x<-80||enemy.enemy_y>350)){enemies.remove(enemy);}
            else if (enemy.kind.equals("thief") &&enemy.enemy_x<-80){enemies.remove(enemy);}
            else if (enemy.kind.equals("kite") &&enemy.enemy_x<-80){enemies.remove(enemy);}
            else if (enemy.kind.equals("Missiles") &&(enemy.enemy_x<-80||enemy.enemy_y>350)){ bombs.add(new Bomb(enemy.enemy_x,enemy.enemy_y));enemies.remove(enemy);}
            else if (enemy.kind.equals("rushing") &&enemy.enemy_y<-20){enemies.remove(enemy);}
            else if (enemy.kind.equals("rushingred") &&enemy.enemy_x<-80){enemies.remove(enemy);}
        }

        for(int i=0; i<enemies.size(); i++) {//The bullet collided with the enemy
            Enemy enemy = enemies.get(i);
            for(int j=0; j<bullets.size(); j++) {
                Bullet bullet = bullets.get(j);
                if(checkCoilRects(bullet.hitBox.x, bullet.hitBox.y, bullet.hitBox.w, bullet.hitBox.h,(int)enemy.enemy_x,(int)enemy.enemy_y,
                        (int)enemy.width,(int)enemy.height)){
                    boolean live=enemy.setHealthy_bar(bullet.hurt);
                    playAudio(enemySufferAttackSound);
                    if (!live&&!enemy.beHit){
                        enemies.remove(enemy);
                        Bomb bomb = new Bomb(enemy.enemy_x, enemy.enemy_y);
                        bombs.add(bomb);//Add to the collection
                    }
                    enemy.beHit=true;
                    bullets.remove(bullet);
                   // Create an object that explodes the image
                }
            }


            //A collision with a special enemy (a special enemy will stop attacking under certain circumstances)

            for (SuperWeapon b : superWeapons) {
                for (HitBox h2 : b.hitBoxes) {
                    if (checkCoilRects(h2.x, h2.y, h2.w, h2.h, (int)enemy.enemy_x, (int)enemy.enemy_y, (int)enemy.width, (int)enemy.height)) {
                        boolean live=enemy.setHealthy_bar(b.hurt);
                        if (!live&&!enemy.beHit) {
                            Bomb bomb = new Bomb(h2.x - 20, h2.y - 20);
                            bombs.add(bomb);//Add to the collection
                            score += 10;
                        }
                        enemy.beHit=true;
                        enemies.remove(enemy);
                        playAudio(enemySufferAttackSound);
                    }
                }
            }
        }
    }

    //Draw the enemy
    public void paintmonstertimeEnemy(){
        for (Enemy enemy : enemies) {
            switch (enemy.kind) {
                //Different sizes of different enemies
                case "kite", "star", "plane" -> drawImage(enemy.enemyImage, enemy.enemy_x, enemy.enemy_y, 80, 60);
                case "thief" -> drawImage(enemy.enemyImage, enemy.enemy_x, enemy.enemy_y, 54, 60);
                case "jet_pack" -> drawImage(enemy.enemyImage, enemy.enemy_x, enemy.enemy_y, 60, 80);
                case "Missiles" -> drawImage(enemy.enemyImage, enemy.enemy_x, enemy.enemy_y, 130, 30);
                case "rushing" -> drawImage(enemy.enemyImage, enemy.enemy_x, enemy.enemy_y, 80, 40);
                case "rushingred" -> drawImage(enemy.enemyImage, enemy.enemy_x, enemy.enemy_y, 40, 80);
            }
        }
    }
    public void updateMonsterTime(double dt){
        updateIsHit(dt);
        updateGround(dt);
        updateCloud(dt);
        updateBullet(dt);
        updateBoard(dt);
        updateScene(dt);
        updateTranslate(dt);
        updateSkills(dt);
        updateMonster(dt);
        updatemonstertimeEnemy(dt);
        updateBomb(dt);
        updateSuperWeapon(dt);
        updateSkillBar(dt);
    }
    public void drawMonsterTime(){
        clearBackground(800,600);
        paintGround();
        paintBullet();
        paintMonster();
        paintmonstertimeEnemy();
        paintInvincibility();
        paintInfo();
        paintStrongerDamage();
        paintBomb();
        drawSuperWeapon();
        drawSkillBar();
    }




    /**
     * Coin Time Game Mode
     */
    Coin coins1;
    public void initCoinOfCoinTime(){
        coins1=new Coin(0,50,0);
        coins1.image[0]=loadImage("src//images//coin1.png");
        coins1.image[1]=loadImage("src//images//coin2.png");
        coins1.relativeX=width();

    }
    public void updateCoinOfCoinTime(double dt){
        coins1.relativeX-=groundMoveSpeed*dt;
        coinTimer=(coinTimer+dt)%coinRound;
        coinInd=getCurImage(coinTimer,coinRound,2);
        for(CoinAward a:coinAwards){
            for(int i=0;i<a.coins1.size();i++){
                Coin e=a.coins1.get(i);
                if(checkCoilRects(e.relativeX+(int)a.positionX,e.y,e.width,e.height,(int)monsterX,(int)monsterY,(int)monsterWidth,(int)monsterHeight)){
                    a.coins1.remove(e);
                    score+=10;
                    playAudio(getCoinSound);
                    break;
                }
            }
        }
    }
    ArrayList<CoinAward> coinAwards=new ArrayList<>();
    public void initAward(){
        //Initialize the gold reward area
        coinAwards.clear();
        coinAwards.add(new CoinAward(width()));

    }
    public void updateAward(double dt){
        //Generate the next reward area
        if(coinAwards.size()!=0){
            int chang=coinAwards.size();
            if(coinAwards.get(chang-1).positionX+coinAwards.get(chang-1).width<width()){
                coinAwards.add(new CoinAward(width()));
            }
        }
        for (CoinAward coinAward : coinAwards) {
            coinAward.positionX -= groundMoveSpeed * dt;
        }
    }
    public void paintCoin(){
        for(Board a:boards){
            if(a.move) {
                for (Coin e : a.coins) {
                    drawImage(coins.image[coinInd], e.relativeX + a.positionX, e.y, e.width, e.height);
                    changeColor(red);
                }
            }
        }
    }
    public void updateCoinTime(double dt){
        updateIsHit(dt);
        updateGround(dt);
        updateCloud(dt);
        updateBullet(dt);
        updateBoard(dt);
        updateScene(dt);
        updateTranslate(dt);
        updateSkills(dt);
        updateMonster(dt);
    }
    public void drawCoinTime(){
        clearBackground(800,600);
        paintGround();
        paintBoard();
        paintMonster();
    }
    public void paintCoinOfCoinTime(){
        for (CoinAward a:coinAwards) {
            for (Coin e : a.coins1) {
                drawImage(coins1.image[coinInd], e.relativeX + a.positionX, e.y, e.width, e.height);
            }
        }
    }


    /**
     * Things (Buff, coin, etc.) which relates to the board.
     */
    public void updateWater(double dt){
        for(Board b:boards){
            for(WaterObstacle w:b.waterObstacles){
                w.relativeX-=groundMoveSpeed*dt;
            }
            for(WaterObstacle e:b.waterObstacles){
                if(e.hitMonster) continue;
                if(checkCoilRects((int)e.relativeX,e.y,e.width,e.height,(int)monsterX,(int)monsterY,(int)monsterWidth,(int)monsterHeight)){
                    isHitting(1);
                    e.hitMonster=true;
                    break;
                }
            }
        }
    }
    public void paintWater(){
        for(Board b:boards)
            for(WaterObstacle w:b.waterObstacles) drawImage(waterObstacle.image, w.relativeX, w.y,w.width,w.height);
    }
    public void updateThunder(double dt){
        for(Board b:boards){
            for(Thunder t:b.thunders){
                t.positionX-=groundMoveSpeed*dt;
                t.wholeTimer=(t.wholeTimer+dt)%t.wholeRound;
                if(t.wholeTimer<=t.peaceDuratioin){
                    t.imageInd=getCurImage(t.wholeTimer,t.peaceRound,2);
                }
                else{
                    t.imageInd=2+getCurImage(t.wholeTimer-t.peaceDuratioin,t.attackRound,6);
                }
                t.updateHitBox();
                HitBox h=t.hitBox;
                if(checkCoilRects((int)monsterX,(int)monsterY,(int)monsterWidth,(int)monsterHeight,h.x,h.y,h.w,h.h)){
                    if(!t.hitMonster) isHitting(3);
                    t.hitMonster=true;
                }
            }
        }
    }
    public void drawThunder(){
        for(Board b:boards){
            for(Thunder t:b.thunders){
                drawImage(thunder.images[t.imageInd],t.positionX,t.positionY,t.width,t.height);
            }
        }
    }
    double invincibilitiesTimer=0;
    double invincibiliteseRound=1;
    int invincibilitiesInd;
    public void updateInvincibility(double dt){
        //update the timer of the visual effects to display correct image.
        invincibilitiesTimer=(invincibilitiesTimer+dt)%invincibiliteseRound;
        invincibilitiesInd=getCurImage(invincibilitiesTimer,invincibiliteseRound,8);

        for(Board a:boards){
            for(int i=0;i<a.invincibilities.size();i++){
                Invincibility e=a.invincibilities.get(i);
                if(checkCoilRects(e.relativeX+(int)a.positionX,e.y,e.width,e.height,(int)monsterX,(int)monsterY,(int)monsterWidth,(int)monsterHeight)){
                    a.invincibilities.remove(e);
                    playAudio(getSkillSound);
                    invincibilities.restart();
                    break;
                }
            }
        }
    }
    public void paintInvincibility(){
        for(Board a:boards){
            for(Invincibility e:a.invincibilities){
                drawImage(invincibilities.image, e.relativeX + a.positionX, e.y, e.width, e.height);
            }
        }

        // draw the visual effects of the invincibilities.
        if(invincibilities.skillActivation)
            drawImage(invincibilities.images[invincibilitiesInd],monsterX-35,monsterY-35,120,120 );
    }
    public void updateEnergy(double dt){
        for(Board a:boards){
            for(int i=0;i<a.energies.size();i++){
                Energy e=a.energies.get(i);
                e.positionX-=dt*groundMoveSpeed;
                if(checkCoilRects(e.relativeX+(int)a.positionX,e.y,e.width,e.height,(int)monsterX,(int)monsterY,(int)monsterWidth,(int)monsterHeight)){
                    a.energies.remove(e);
                    health+=5;
                    playAudio(getLifePointSound);
                    if(health>maxHealth) health=maxHealth;
                    isRecovering=true;
                    recoverTimer=0;
                    break;
                }
            }
        }
    }
    public void paintEnergy(){
        for(Board a:boards){
            for(Energy e:a.energies){
                drawImage(energies.image, e.relativeX + a.positionX, e.y, e.width, e.height);
            }
        }
    }
    public void updateStrongerDamage(double dt){
        for(Board a:boards){
            for(int i=0;i<a.strongerDamages.size();i++){
                StrongerDamage e=a.strongerDamages.get(i);
                if(checkCoilRects(e.relativeX+(int)a.positionX,e.y,e.width,e.height,(int)monsterX,(int)monsterY,(int)monsterWidth,(int)monsterHeight)){
                    a.strongerDamages.remove(e);
                    strongerDamage.restart();
                    playAudio(getSkillSound);
                    break;
                }
            }
        }
    }
    public void paintStrongerDamage(){
        for(Board a:boards){
            for(StrongerDamage e:a.strongerDamages){
                drawImage(strongerDamage.image, e.relativeX + a.positionX, e.y, e.width, e.height);
            }
        }
    }
    int coinInd;
    double coinTimer=0;
    double coinRound=0.6;
    public void updateCoin(double dt){
        coins.relativeX-=groundMoveSpeed*dt;
        //update the timer of the visual effects to display correct image.
        coinTimer=(coinTimer+dt)%coinRound;
        coinInd=getCurImage(coinTimer,coinRound,2);
        for(Board a:boards){
            for(int i=0;i<a.coins.size();i++){
                Coin e=a.coins.get(i);
                if(checkCoilRects(e.relativeX+(int)a.positionX,e.y,e.width,e.height,(int)monsterX,(int)monsterY,(int)monsterWidth,(int)monsterHeight)){
                    a.coins.remove(e);
                    score+=10;
                    playAudio(getCoinSound);
                    break;
                }
            }
        }
    }




    public void drawPause(){
        mGraphics.setColor(new Color(0,0,0,0.5F));
        drawSolidRectangle(0,0,width(),height());
        mGraphics.setFont(f2);
        changeColor(200,0,0);
        mGraphics.drawString("Pausing",310,280);
    }





    @Override
    public void init() {
        initSound();
        initBoard();
        setWindowSize(800,600);
        initMonster();
        initCloud();
        initAward();
        initSuperWeapon();
        initElementsOnTheBoard();
        initOtherElements();
        initForRestart();
    }
    @Override
    public void update(double dt) {
        if(restart){
            clearElements();
            initForRestart();
            initSound();
            initBoard();
            setWindowSize(800,600);
            initMonster();
            initCloud();
            initAward();
            restart=false;
        }
        if(!pause&&health>0){
            updateScore(dt);
            if(normalTime){
                updateForNormal(dt);
            }
            else if(coinTime){
                updateCoinTime(dt);
                updateCoinOfCoinTime(dt);
                updateAward(dt);
            }
            else if(monsterTime){
                updateMonsterTime(dt);
            }
        }
    }
    @Override
    public void paintComponent() {
        if(normalTime){
            clearBackground(800,600);
            paintGround();
            paintCloud();
            paintMonster();
            paintBoard();
            paintWater();
            paintInvincibility();
            paintEnergy();
            paintCoin();
            paintStrongerDamage();
            paintBullet();
            drawThunder();
            drawSuperWeapon();
            paintNorTimeEnemy();
            paintInfo();
            paintBomb();
            paintRecover();
            drawSkillBar();
            paintTranslate(nextScene);

        }
        else if(coinTime){
            drawCoinTime();
            paintCoinOfCoinTime();
            paintInfo();
            paintTranslate(nextScene);
        }
        else if(monsterTime){
            drawMonsterTime();
            paintTranslate(nextScene);
        }
        if(pause)drawPause();
        if(health<=0) paintDeathInfo();
    }
    @Override
    public void keyPressed(KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.VK_W){
            jump();
        }
        if(event.getKeyCode()==KeyEvent.VK_D){
            moveForward=true;
        }
        if(event.getKeyCode()==KeyEvent.VK_A){
            moveBack=true;
        }

        if(event.getKeyCode()==KeyEvent.VK_E){
            if(health>0){
                pause=!pause;
            }
        }
        if(event.getKeyCode()==KeyEvent.VK_T){
            strongerDamage.restart();
        }
        if(event.getKeyCode()==KeyEvent.VK_R){
            restart=true;
            stopAudioLoop(gameBGM);
            //timer.start();
        }
        if(event.getKeyCode()==KeyEvent.VK_K){
            if(!coinTime) shoot();
        }
    }
    @Override
    public void keyReleased(KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.VK_D){
            moveForward=false;
        }
        if(event.getKeyCode()==KeyEvent.VK_A){
            moveBack=false;
        }
        if(event.getKeyCode()==KeyEvent.VK_J){
            if(coinTime) return;
            shootSuperWeapon();
        }
    }
}