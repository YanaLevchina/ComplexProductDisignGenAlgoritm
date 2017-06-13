/**
 * Created by alex on 13.06.17.
 */
public class Bracket {
    private int[] variables;

    public boolean isExecuted(boolean[] partsInProduct) {
        for(int i = 0; i < partsInProduct.length; i++) {
            if((partsInProduct[i]&&(variables[i] > 0))||(!partsInProduct[i]&&(variables[i] < 0)))
                return true;
        }
        return false;
    }
}
