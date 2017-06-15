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
        Product product = new Product();
        String line;
        String[] fileLine;
        String[] brackets;
        String[] variablesInFile;
        int[] variables;
        Bracket[] formula = new Bracket[scan.nextInt()];
        Formula[] softFormulas = new Formula[scan.nextInt()];
        Formula[] hardFormulas = new Formula[formula.length - softFormulas.length];
        int hard;
        int hardNum = 0;
        int softNum = 0;
        scan.nextLine();
        while(scan.hasNext()) {
            //System.out.println(line);
            line=scan.nextLine();
            fileLine = line.split(" ");
            hard = Integer.valueOf(fileLine[0]);

            brackets = fileLine[1].split(";");
            for(int i = 0; i < brackets.length; i++) {
                variablesInFile = brackets[i].split(",");
                variables = new int[variablesInFile.length];
                for(int j = 0; j < variablesInFile.length; j++)
                    variables[j] = Integer.valueOf(variablesInFile[j]);
                formula[i] = new Bracket(variables);
            }
            if(hard > 0) {
                hardFormulas[hardNum] = new Formula(formula, hard);
                hardNum++;
            }
            else {
                softFormulas[softNum] = new Formula(formula, hard);
                softNum++;
            }
        }
        product = new Product(softFormulas, hardFormulas);
        return product;
    }
}
