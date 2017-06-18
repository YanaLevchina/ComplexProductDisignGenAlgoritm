import input.ExampleInput;
import model.GenAlgorythm;
import model.Product;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by alex on 13.06.17.
 */
public class Launcher {
    public static void main(String[] args) {
        Product product = new Product();
        try {
            GenAlgorythm alg = new GenAlgorythm(0.1, 3, new ExampleInput(), new File("/home/alex/IdeaProjects/ComplexProductDesignGenAlgorythm/src/data/pract_example"));//Создаем алгоритм.
            // Первые два параметра задают уровень мутации и размер популяции
            int result = alg.euristic(100);//запускаем работу эвристики.
            // Параметр определяет, сколько раз будут произведены действия
            System.out.println("Final max is: " + result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
