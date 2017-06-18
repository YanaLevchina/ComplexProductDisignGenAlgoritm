package input;

import model.Bracket;
import model.Formula;
import model.Product;

import java.io.*;
import java.util.Scanner;

/**
 *
 * Created by alex on 15.06.17.
 */
public class ExampleInput implements Input{

    @Override
    public Product getProduct(File file) throws FileNotFoundException {
        Scanner scan = new Scanner(new FileInputStream(file));
        String line;
        String[] fileLine;
        String[] brackets;
        String[] variablesInFile;
        int[] variables;
        int size = scan.nextInt();
        int numFormulas = scan.nextInt();

        Formula[] hardFormulas = new Formula[scan.nextInt()];
        Formula[] softFormulas = new Formula[numFormulas - hardFormulas.length];
        int[] weights = new int[numFormulas];
        Product product = new Product(numFormulas);
        int hard;
        int hardNum = 0;
        int softNum = 0;
        scan.nextLine();
        while(scan.hasNext()) {
            //System.out.println(line);
            line=scan.nextLine();
            fileLine = line.split(" ");
            hard = Integer.valueOf(fileLine[0]);
            weights[hardNum + softNum] = hard;
            brackets = fileLine[1].split(";");
            Bracket[] formula = new Bracket[brackets.length];
            for(int i = 0; i < brackets.length; i++) {

                variablesInFile = brackets[i].split(",");
                variables = new int[variablesInFile.length];
                for(int j = 0; j < variablesInFile.length; j++)
                    variables[j] = Integer.valueOf(variablesInFile[j]);
                formula[i] = new Bracket(variables);
            }
            if(hard > 0) {
                softFormulas[softNum] = new Formula(formula, hard);
                softNum++;
            }
            else {
                hardFormulas[hardNum] = new Formula(formula, hard);
                hardNum++;
            }
        }
        product = new Product(size, softFormulas, hardFormulas, weights);
        return product;
    }
}
