package se.iths.java21.patrik.lab3.tools;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import se.iths.java21.patrik.lab3.Model;
import se.iths.java21.patrik.lab3.PaintApplication;
import se.iths.java21.patrik.lab3.shapes.Shape;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static se.iths.java21.patrik.lab3.tools.CheckedSupplier.wrap;

public class SVGWriter {
    private static FileChooser fileChooser = new FileChooser();

    public static void saveSVGFile(Model model) {
        fileChooser.setTitle("Save SVG File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SVG format","*.svg"));

        Path path = Path.of(wrap(() -> fileChooser.showSaveDialog(new Stage()).toURI()));
        List<String> strings = new ArrayList<>();

        buildSVGString(model, strings);

        try {
            Files.write(path, strings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void buildSVGString(Model model, List<String> strings) {
        strings.add(startOfSVGString());
        model.shapes.forEach(shape -> shapeSVGInfoToString(shape, strings));
        strings.add(endOfSVGString());
    }

    private static String startOfSVGString() {
        return String.join(" ",
                "<svg",
                "xmlns=\"http://www.w3.org/2000/svg\"",
                "version=\"1.1\"",
                "width=\"800.0\"",
                "height=\"800.0\">");
    }

    private static void shapeSVGInfoToString(Shape shape, List<String> strings) {
        strings.add(String.join(" ",shape.drawSVG()));
    }

    private static String endOfSVGString() {
        return "</svg>";
    }
}
