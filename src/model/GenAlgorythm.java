package model;

import input.*;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by alex on 13.06.17.
 */
public class GenAlgorythm {
    private Product product;
    private double mutLevel;
    private int populationSize;
    private boolean[][] population;
    private boolean[] max;
    private Input input;
    int maxValue;

    public GenAlgorythm(Input input, File file) throws FileNotFoundException {
        this.input = input;
        this.product = input.getProduct(file);

    }

    private boolean[][] createStartPopulation() {
        boolean[][] population = new boolean[populationSize][];
        for(int i = 0; i < populationSize; i++){
            boolean[] vect = new boolean[product.getSize()];
            do {
                for (int j = 0; j < product.getSize(); j++) {
                    if (Math.random() < 0.5)
                        vect[j] = false;
                    else
                        vect[j] = true;
                }
            }while (!product.isFeasible(vect));
            population[i] = vect;
        }
        this.population = population;
        return population;
    }

    private int getFirstForCrossOver(double[] prob) {
        int result = -1;
        while(result < 0) {
            for(int i = 0; i < prob.length; i++) {
                if(Math.random() < prob[i])
                    result = i;
            }
        }
        return result;
    }

    private int getSecondForCrossOver(double[] prob, int num) {
        int result = -1;
        while(result < 0) {
            for(int i = 0; i < prob.length; i++) {
                if(i == num)
                    continue;
                else {
                    if (Math.random() < prob[i])
                        result = i;
                }
            }
        }
        return result;
    }

    private boolean[] createNextGenIndividual(double[] prob) {
        boolean[] vect = new boolean[prob.length];
        do {
            vect = mutation(crossOver(population[getFirstForCrossOver(prob)], population[getSecondForCrossOver(prob, getFirstForCrossOver(prob))]));
        } while (!product.isFeasible(vect));
        return vect;
    }

    int getMax(int[] vect, boolean[][] population) {
        int max = vect[0];
        for(int i = 0; i < vect.length; i++) {
            if(vect[i] > max) {
                max = vect[i];
                this.max = population[i];
            }
        }

        return max;
    }

    int getMax(int[] vect) {
        int max = vect[0];
        for(int i = 0; i < vect.length; i++) {
            if(vect[i] > max)
                max = vect[i];
        }

        return max;
    }

    private boolean[][] createNextGen() {
        boolean[][] newPopulation = new boolean[populationSize][];

        int[] fitness = new int[populationSize];
        double[] prob = new double[populationSize];
        int summ = 0;
        for(int i = 0 ; i < populationSize; i ++) {
            fitness[i] = product.getProductWeight(population[i]);
            summ+=fitness[i];
            newPopulation[i] = new boolean[product.getSize()];
            System.out.println(getMax(fitness));
            if(maxValue < getMax(fitness))
                maxValue = getMax(fitness, newPopulation);
        }
        for(int i = 0; i < populationSize; i++)
            prob[i] = fitness[i]/summ;


        for(int i = 0; i < populationSize; i++) {
            newPopulation[i] = createNextGenIndividual(prob);
        }
        population = newPopulation;
        return newPopulation;

    }

    private boolean[] mutation(boolean[] vect) {
        boolean[] newVect = new boolean[vect.length];
        for(int i =0; i < vect.length; i++) {
            if(Math.random() < mutLevel)
                newVect[i] = !vect[i];
        }
        return newVect;
    }

    private boolean[] crossOver(boolean[] vect1, boolean[] vect2) {
        boolean[] child = new boolean[vect1.length];
        for(int i = 0; i < vect1.length; i++){
            if(Math.random() < 0.5)
                child[i] = vect1[i];
            else
                child[i] = vect2[i];
        }
        return child;
    }

    public int euristic(int iterCount) {
        createStartPopulation();
        for(int i = 0; i < iterCount; i++)
            createNextGen();
        //printMaxValue();
        return maxValue;
    }

    private void printMaxValue() {
        for(int i = 0; i < max.length; i++) {
            System.out.println(max[i] + " ");
        }
    }

}
