package se.iths.java21.patrik.lab3.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle extends Shape {

    public Circle(Color color, double x, double y, double size) {
        super(color, x, y, size);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getBorderColor());
        gc.fillOval(getX() - getSize() - 2.5, getY() - getSize() - 2.5, 2 * getSize() + 5, 2 * getSize() + 5);
        gc.setFill(getColor());
        gc.fillOval(getX() - getSize(), getY() - getSize(), 2 * getSize(), 2 * getSize());
    }

    @Override
    public boolean isInsideShape(double mouseX, double mouseY) {
        double distX = mouseX - getX();
        double distY = mouseY - getY();
        double distance = Math.sqrt((distX * distX) + (distY * distY));

        return distance <= getSize();
    }

    @Override
    public String toString() {
        return "Circle " + super.getColor();
    }
}
