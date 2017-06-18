package model;

import input.*;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by alex on 13.06.17.
 */
public class GenAlgorythm {
    private Product product;//Изделие, к которому применяем алгоритм
    private double mutLevel;//Уровень мутации
    private int populationSize;//Численность популяции
    private boolean[][] population;//Текущая популяция
    private boolean[] max;//Вектор, на котором достигается максимальное значение
    private Input input;//Отвечает за создание продукта из файла
    int maxValue;//Максимальное значение выполненных мягких ограничений

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
    }//Создаем стартовую популяцию. Создаем случайные вектора до тех пор, пока одном из них
    // не будут выполнены все жесткие ограничения, добавляем его в популяцию и продолжаем процесс,
    // пока популяция не полная

    private int getFirstForCrossOver(double[] prob) {
        int result = -1;
        while(result < 0) {
            for(int i = 0; i < prob.length; i++) {
                if(Math.random() < prob[i])
                    result = i;
            }
        }
        return result;
    }//Отбираем первого родителя для скрещивания.

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
    }//Отбираем второго родителя для скрещивания

    private boolean[] createNextGenIndividual(double[] prob) {
        boolean[] vect = new boolean[population[0].length];
        do {
            int first = getFirstForCrossOver(prob);
            int second = getSecondForCrossOver(prob, first);
            vect = mutation(crossOver(population[first], population[second]));
        } while (!product.isFeasible(vect));
        return vect;
    }//Функция, создающая отдельную особь новой популяции. Выбираем из предыдущей популяции две
    // особи способом, описанным в методе createNextGen() и применяем к ним операторы мутации и скрещивания до тех пор,
    // пока полученный вектор не станет допустимым, т.е. пока на нем не будут выполнены все жесткие ограничения.

    private void copyArr(boolean[] vect1, boolean[] vect2) {
        for(int i = 0; i < vect2.length; i++)
            vect1[i] = vect2[i];
    } //Копия массива. Все очевидно.

    int getMax(int[] vect, boolean[][] population) {
        int max = -1;
        for(int i = 0; i < vect.length; i++) {
            if(vect[i] > max) {
                max = vect[i];
                this.max = new boolean[population[i].length];
                copyArr(this.max, population[i]);
            }
        }

        return max;
    }//Найти индекс максимального элемента в массиве и сохранить массив в поле max.
    // Используется при улучшении текущего максимального значения в популяции

    int getMax(int[] vect) {
        int max = vect[0];
        for(int i = 0; i < vect.length; i++) {
            if(vect[i] > max)
                max = vect[i];
        }

        return max;
    } //Найти индекс максимального элемента в массиве. Используется для поиска максимального значения
    // функции приспособленности в популяции.

    private boolean[][] createNextGen() {
        boolean[][] newPopulation = new boolean[populationSize][];

        int[] fitness = new int[populationSize];
        double[] prob = new double[populationSize];
        int summ = 0;
        for(int i = 0 ; i < populationSize; i ++) {
            fitness[i] = product.getProductWeight(population[i]);
            summ+=fitness[i];
            newPopulation[i] = new boolean[product.getSize()];
            if(maxValue < getMax(fitness))
                maxValue = getMax(fitness, population);
        }
        for(int i = 0; i < populationSize; i++)
            prob[i] = (double) fitness[i]/summ;


        for(int i = 0; i < populationSize; i++) {
            newPopulation[i] = createNextGenIndividual(prob);
        }
        population = newPopulation;
        return newPopulation;

    }//Основная функция алгоритма. Создает новую популяцию на основе старой. Реализован рулеточный принцип.
    //Вероятность каждой особи участвовать в создании следующего поколения определяется отношением веса
    // выполненных мягких формул на данном векторе к сумме весов выполненных мягких ограничений всей популяции.

    private boolean[] mutation(boolean[] vect) {
        boolean[] newVect = new boolean[vect.length];
        for(int i =0; i < vect.length; i++) {
            if(Math.random() < mutLevel)
                newVect[i] = !vect[i];
            else
                newVect[i] = vect[i];
        }
        return newVect;
    }//Оператор мутации. С заданной вероятностью изменяет биты вектора.

    private boolean[] crossOver(boolean[] vect1, boolean[] vect2) {
        boolean[] child = new boolean[vect1.length];
        for(int i = 0; i < vect1.length; i++){
            if(Math.random() < 0.5)
                child[i] = vect1[i];
            else
                child[i] = vect2[i];
        }
        return child;
    }//Функция кроссинговера(Оператор скрещивания). Получаем потомка по двум родителям.

    public int euristic(int iterCount) {
        createStartPopulation();
        for(int i = 0; i < iterCount; i++)
            createNextGen();
        printMaxValue();
        return maxValue;
    }//Эвристика. Проводит основное действие алгоритма заданное количество раз

    private void printMaxValue() {
        for(int i = 0; i < max.length; i++) {
            if(max[i])
                System.out.print(1 + " ");
            else
                System.out.print(0 + " ");
        }//Вывод на экран вектора, на котором достигается максимальное значение
    }

}
