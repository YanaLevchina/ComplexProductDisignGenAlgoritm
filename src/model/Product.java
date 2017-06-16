package model;

/**
 * Created by alex on 13.06.17.
 */
public class Product {
    private int size;
    //private boolean[] partInProduct;
    private int[] partWeights;
    private int bottomBorderWeight;
    private Formula[] softFormulas;
    private Formula[] hardFormulas;

    public Product(){};

    public Product(int length){
        partWeights = new int[length];
    };

    public Product(Formula[] softFormulas, Formula[] hardFormulas, int[] weights) {
        this.softFormulas = softFormulas;
        this.hardFormulas = hardFormulas;
        this.partWeights = weights;
        this.size = softFormulas.length + hardFormulas.length;
    }

    public void setPart(int i, int value) {
        partWeights[i] = value;
    }
    public int getSize() {
        return size;
    }

    public boolean isFeasible(boolean[] partInProduct) {
        for(Formula formula: hardFormulas) {
            if(!formula.isExecuted(partInProduct))
                return false;
        }
        return true;
    }


    private int getFormulaWeight(Formula formula, boolean[] partInProduct) {
        if(formula.isExecuted(partInProduct))
            return formula.getWeight();
        else
            return 0;

    }

    public int getProductWeight(boolean[] partInProduct) {
        int summ = 0;
        for(Formula formula: softFormulas)
            summ+=getFormulaWeight(formula, partInProduct);
        return summ;
    }


}
