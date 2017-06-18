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

    public GenAlgorythm(double mutLevel, int populationSize, Input input, File file) throws FileNotFoundException {
        this.input = input;
        this.product = input.getProduct(file);
        this.populationSize = populationSize;
        this.mutLevel = mutLevel;
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
        System.out.println("first for crossover is " + result);
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
        System.out.println("second for crossover is " + result);
        return result;
    }

    private boolean[] createNextGenIndividual(double[] prob) {
        boolean[] vect = new boolean[population[0].length];
        do {
            int first = getFirstForCrossOver(prob);
            int second = getSecondForCrossOver(prob, first);
            vect = mutation(crossOver(population[first], population[second]));
        } while (!product.isFeasible(vect));
        System.out.println("Vector created");
        for(int i = 0; i < vect.length; i++)
            System.out.print(vect[i] + " ");
        System.out.println();
        return vect;
    }

    private void copyArr(boolean[] vect1, boolean[] vect2) {
        for(int i = 0; i < vect2.length; i++)
            vect1[i] = vect2[i];
    }

    int getMax(int[] vect, boolean[][] population) {
        int max = -1;
        for(int i = 0; i < vect.length; i++) {
            if(vect[i] > max) {
                max = vect[i];
                this.max = new boolean[population[i].length];
                copyArr(this.max, population[i]);
                System.out.println("New maximum is:");
                for(int j = 0; j < this.max.length; j++)
                    System.out.print(this.max[j] + " ");
                System.out.println();
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
                maxValue = getMax(fitness, population);
        }
        System.out.println("current max is " + maxValue);
        for(int i = 0; i < populationSize; i++)
            prob[i] = (double) fitness[i]/summ;


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
            else
                newVect[i] = vect[i];
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
        printMaxValue();
        return maxValue;
    }

    private void printMaxValue() {
        for(int i = 0; i < max.length; i++) {
            System.out.print(max[i] + " ");
        }
    }

}
