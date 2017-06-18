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
            GenAlgorythm alg = new GenAlgorythm(10, new ExampleInput(), new File("/home/alex/IdeaProjects/ComplexProductDesignGenAlgorythm/src/data/pract_example"));
            alg.euristic(100);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
