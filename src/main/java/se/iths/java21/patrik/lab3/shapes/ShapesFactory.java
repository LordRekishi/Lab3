package se.iths.java21.patrik.lab3.shapes;

import javafx.scene.paint.Color;

public class ShapesFactory {

    public static Rectangle rectangleOf(Color color, double x, double y, double size) {
        return new Rectangle(color,x,y,size);
    }

    public static Circle circleOf(Color color, double x, double y, double size) {
        return new Circle(color,x,y,size);
    }

    public static Rectangle rectangleOf(Shape shape) {
        return new Rectangle(shape);
    }

    public static Circle circleOf(Shape shape) {
        return new Circle(shape);
    }

}
