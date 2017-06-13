/**
 * Created by alex on 13.06.17.
 */
public class Formula {
    private int weight;
    private Bracket[] brackets;

    public boolean isExecuted(boolean[] partsInProduct) {
        boolean result = true;
        for (Bracket bracket : brackets)
            result = result && bracket.isExecuted(partsInProduct);
        return result;
    }

    public int getWeight() {
        return weight;
    }
}
