package se.iths.java21.patrik.lab3.shapes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Shape {
    private final ObjectProperty<Color> color = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> borderColor = new SimpleObjectProperty<>();
    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final DoubleProperty size = new SimpleDoubleProperty();

    protected Shape(Color color, double x, double y, double size) {
        setColor(color);
        setBorderColor(Color.TRANSPARENT);
        setX(x);
        setY(y);
        setSize(size);
    }

    protected Shape(Shape shape) {
        setColor(shape.getColor());
        setBorderColor(Color.TRANSPARENT);
        setX(shape.getX());
        setY(shape.getY());
        setSize(shape.getSize());
    }

    public Color getColor() {
        return color.get();
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public Color getBorderColor() {
        return borderColor.get();
    }

    public ObjectProperty<Color> borderColorProperty() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor.set(borderColor);
    }

    public double getX() {
        return x.get();
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public double getSize() {
        return size.get();
    }

    public DoubleProperty sizeProperty() {
        return size;
    }

    public void setSize(double size) {
        this.size.set(size);
    }


    public abstract Shape copyOf();

    public abstract String drawSVG();

    public abstract void draw(GraphicsContext gc);

    public abstract boolean isInsideShape(double mouseX, double mouseY);
}
