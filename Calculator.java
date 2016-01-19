/**
 * Created by Nathan on 18/01/2016.
 */
public class Calculator {
    public long getEdges(int n) {
        return (2*n-1)*(long)Math.pow(2,n-1);
    }

    public long getVertices(int n) {
        return (long)Math.pow(2,n);
    }

}
