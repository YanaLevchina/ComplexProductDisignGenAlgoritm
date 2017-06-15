package input;


import model.Product;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by alex on 15.06.17.
 */
public interface Input {
    Product getProduct(File file) throws FileNotFoundException;
}
