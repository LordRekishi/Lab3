package se.iths.java21.patrik.lab3.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class Rectangle extends Shape {

    protected Rectangle(Color color, double x, double y, double size) {
        super(color, x, y, size);
    }

    protected Rectangle(Shape shape) {
        super(shape);
    }

    @Override
    public void drawSVG(SVGPath path) {
//        path.setContent(" M " + getX() + " " + getY() +
//                " h " + getSize() +
//                " v " + getSize() +
//                " h " + (- getSize()) +
//                " Z ");
//        path.setContent("M100 100 h 50 v 50 h -50 Z");
        path.setContent("M100,100 L300,100 L200,300 z");
        path.setFill(getColor());
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getBorderColor());
        gc.fillRect(getX() - getSize() - 2.5, getY() - getSize() - 2.5, 2 * getSize() + 5, 2 * getSize() + 5);
        gc.setFill(getColor());
        gc.fillRect(getX() - getSize(), getY() - getSize(), 2 * getSize(), 2 * getSize());

    }

    @Override
    public boolean isInsideShape(double mouseX, double mouseY) {
        double leftX = getX() - getSize();
        double topY = getY() - getSize();

        return mouseX >= leftX &&
                mouseX <= leftX + 2 * getSize() &&
                mouseY >= topY &&
                mouseY <= topY + 2 * getSize();
    }

    @Override
    public String toString() {
        return "Rectangle " + super.getColor();
    }
}
