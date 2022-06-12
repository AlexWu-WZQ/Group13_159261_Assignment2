package Others;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description :   generate things in some range to avoid covering other elements on the boards
 */

public class GenerateThingsRange {
    public double left;
    public double right;
    public double range;
    public GenerateThingsRange(double left,double right){
        this.left=left;
        this.right=right;
        this.range=right-left;
    }
}
