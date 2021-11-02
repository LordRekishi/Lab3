package se.iths.java21.patrik.lab3.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SVGWriter {

    public static void saveSVGFile(ProductList productList) {
        Path productPath = Path.of(wrap(() -> ClassLoader.getSystemResource("products.csv").toURI()));
        List<String> strings = new ArrayList<>();
        productList.forEach(product -> convertToStrings(product, strings));

        try {
            Files.write(productPath, strings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
