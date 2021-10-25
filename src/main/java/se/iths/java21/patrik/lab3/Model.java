package se.iths.java21.patrik.lab3;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import se.iths.java21.patrik.lab3.shapes.Shape;


public class Model {
    ObservableList<Shape> shapes;
    ObservableList<Shape> selectedShapes;

    private final BooleanProperty circleSelected;
    private final BooleanProperty rectangleSelected;
    private final BooleanProperty lineSelected;
    private final BooleanProperty pointSelected;

    private final ObjectProperty<Color> color;
    private final ObjectProperty<Color> borderColor;
    private final StringProperty shapeSize;

    private final BooleanProperty selectMode;

    public Model() {
        this.shapes = FXCollections.observableArrayList();
        this.selectedShapes = FXCollections.observableArrayList();

        this.circleSelected = new SimpleBooleanProperty();
        this.rectangleSelected = new SimpleBooleanProperty();
        this.lineSelected = new SimpleBooleanProperty();
        this.pointSelected = new SimpleBooleanProperty();

        this.color = new SimpleObjectProperty<>(Color.BLACK);
        this.borderColor = new SimpleObjectProperty<>();
        this.borderColor.set(Color.TRANSPARENT);
        this.shapeSize = new SimpleStringProperty("18");

        this.selectMode = new SimpleBooleanProperty();
    }



    public boolean isCircleSelected() {
        return circleSelected.get();
    }

    public BooleanProperty circleSelectedProperty() {
        return circleSelected;
    }

    public void setCircleSelected(boolean circleSelected) {
        this.circleSelected.set(circleSelected);
    }

    public boolean isRectangleSelected() {
        return rectangleSelected.get();
    }

    public BooleanProperty rectangleSelectedProperty() {
        return rectangleSelected;
    }

    public void setRectangleSelected(boolean rectangleSelected) {
        this.rectangleSelected.set(rectangleSelected);
    }

    public boolean isLineSelected() {
        return lineSelected.get();
    }

    public BooleanProperty lineSelectedProperty() {
        return lineSelected;
    }

    public void setLineSelected(boolean lineSelected) {
        this.lineSelected.set(lineSelected);
    }

    public boolean isPointSelected() {
        return pointSelected.get();
    }

    public BooleanProperty pointSelectedProperty() {
        return pointSelected;
    }

    public void setPointSelected(boolean pointSelected) {
        this.pointSelected.set(pointSelected);
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

    public String getShapeSize() {
        return shapeSize.get();
    }

    public Double getShapeSizeAsDouble() {
        return Double.parseDouble(getShapeSize());
    }

    public StringProperty shapeSizeProperty() {
        return shapeSize;
    }

    public void setShapeSize(String shapeSize) {
        this.shapeSize.set(shapeSize);
    }


    public boolean isSelectMode() {
        return selectMode.get();
    }

    public BooleanProperty selectModeProperty() {
        return selectMode;
    }

    public void setSelectMode(boolean selectMode) {
        this.selectMode.set(selectMode);
    }


    public void deleteSelectedShapes() {
        for (var shape : selectedShapes){
            shapes.remove(shape);
        }
    }

    public void changeSizeOnSelectedShapes() {
        for (var shape : selectedShapes) {
            shape.setSize(getShapeSizeAsDouble());
        }
    }

    public void changeColorOnSelectedShapes() {
        for (var shape : selectedShapes) {
            shape.setColor(getColor());
        }
    }
}
