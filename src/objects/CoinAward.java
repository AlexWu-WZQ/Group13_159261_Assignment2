package objects;

import java.util.ArrayList;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : the coin award in coin time
 */

public class CoinAward {
    public ArrayList<Coin> coins1=new ArrayList<>();

    public int width;
    public int positionY;
    public double positionX;


    public CoinAward(int maxLen){
        positionX=maxLen;
        positionY=390;
        width=maxLen/2;
        int count1 = (width/50);
        int count2 = (positionY/50)+1;
        //Generates half of the screen coins
        for(int i=0;i<count1;i++) {
            for(int j=0; j<count2; j++){
                coins1.add(new Coin((int)positionX,50*i,positionY-50*j));
            }
        }

    }
}
