package se.iths.java21.patrik.lab3.shapes;

import javafx.scene.paint.Color;

import java.util.regex.Pattern;

public class ShapesFactory {

    public static Rectangle rectangleOf(Color color, double x, double y, double size) {
        return new Rectangle(color, x, y, size);
    }

    public static Circle circleOf(Color color, double x, double y, double size) {
        return new Circle(color, x, y, size);
    }

    public static Rectangle rectangleOf(Shape shape) {
        return new Rectangle(shape);
    }

    public static Circle circleOf(Shape shape) {
        return new Circle(shape);
    }

    public static Shape convertSVGToShape(String line) {
        try {
            Pattern pattern = Pattern.compile("=");
            String[] arr = pattern.split(line);

            if (line.contains("rect")) {
                double size = Double.parseDouble(arr[3].substring(1, 5)) / 2;
                double x = Double.parseDouble(arr[1].substring(1, 5)) + size;
                double y = Double.parseDouble(arr[2].substring(1, 5)) + size;
                Color rectColor = Color.valueOf(arr[5].substring(1, 10));
                return rectangleOf(rectColor, x, y, size);
            } else {
                double size = Double.parseDouble(arr[3].substring(1, 5));
                double x = Double.parseDouble(arr[1].substring(1, 5));
                double y = Double.parseDouble(arr[2].substring(1, 5));
                Color circleColor = Color.valueOf(arr[4].substring(1, 10));
                return circleOf(circleColor, x, y, size);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException();
        }
    }
}
