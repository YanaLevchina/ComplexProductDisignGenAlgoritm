package model;

/**
 * Created by alex on 13.06.17.
 */
public class Bracket {
    private int[] variables;

    public Bracket(int[] variables) {
        this.variables = new int[variables.length];
        for(int i = 0 ; i < variables.length; i++)
            this.variables[i] = variables[i];

    }

    public boolean isExecuted(boolean[] partsInProduct) {
        for(int i = 0; i < variables.length; i++) {
            if((partsInProduct[Math.abs(variables[i]) - 1]&&(variables[i] > 0))||(!partsInProduct[Math.abs(variables[i]) - 1]&&(variables[i] < 0)))
                return true;
        }
        return false;
    }

    public Bracket getCopy() {
        Bracket bracket = new Bracket(this.variables);
        return bracket;
    }
}
