package Others;
/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description : Sometimes the element should be set their own hit Box to detect collisions. Because their original size may not be suitable to detect the collision.
 */

public class HitBox {
    public int x;
    public int y;
    public int w;
    public int h;
    public HitBox(int x,int y,int w,int h){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
    }
}
