package se.iths.java21.patrik.lab3.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Shape {
    private Color color;
    private Color borderColor;
    private double x;
    private double y;
    private double size;

    public Shape(Color color, double x, double y, double size) {
        this.color = color;
        this.borderColor = Color.TRANSPARENT;
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public Shape setColor(Color color) {
        this.color = color;
        return this;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public double getX() {
        return x;
    }

    public Shape setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public Shape setY(double y) {
        this.y = y;
        return this;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public abstract void draw(GraphicsContext gc);

    public abstract boolean isInsideShape(double mouseX, double mouseY);
}
