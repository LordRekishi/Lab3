package se.iths.java21.patrik.lab3;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import se.iths.java21.patrik.lab3.shapes.Circle;
import se.iths.java21.patrik.lab3.shapes.Rectangle;
import se.iths.java21.patrik.lab3.shapes.Shape;
import se.iths.java21.patrik.lab3.shapes.ShapesFactory;

import java.util.ArrayDeque;
import java.util.Deque;

public class Model {
    public ObservableList<Shape> shapes;
    public ObservableList<Shape> selectedShapes;

    public Deque<ObservableList<Shape>> undoDeque;
    public Deque<ObservableList<Shape>> redoDeque;

    private final BooleanProperty circleSelected;
    private final BooleanProperty rectangleSelected;
    private final BooleanProperty lineSelected;
    private final BooleanProperty pointSelected;

    private final ObjectProperty<Color> color;
    private final ObjectProperty<Color> borderColor;
    private final StringProperty shapeSize;

    private final BooleanProperty selectMode;
    private final ObjectProperty<Shape> selectedShape;

    public Model() {
        this.shapes = FXCollections.observableArrayList(
                shape -> new Observable[]{
                        shape.xProperty(),
                        shape.yProperty(),
                        shape.sizeProperty(),
                        shape.colorProperty(),
                        shape.borderColorProperty()
                }
        );

        this.selectedShapes = FXCollections.observableArrayList();

        this.undoDeque = new ArrayDeque<>();
        this.redoDeque = new ArrayDeque<>();

        this.circleSelected = new SimpleBooleanProperty();
        this.rectangleSelected = new SimpleBooleanProperty();
        this.lineSelected = new SimpleBooleanProperty();
        this.pointSelected = new SimpleBooleanProperty();

        this.color = new SimpleObjectProperty<>(Color.BLACK);
        this.borderColor = new SimpleObjectProperty<>();
        this.borderColor.set(Color.TRANSPARENT);
        this.shapeSize = new SimpleStringProperty("18");

        this.selectMode = new SimpleBooleanProperty();
        this.selectedShape = new SimpleObjectProperty<>();
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

    public Shape getSelectedShape() {
        return selectedShape.get();
    }

    public ObjectProperty<Shape> selectedShapeProperty() {
        return selectedShape;
    }

    public void setSelectedShape(Shape selectedShape) {
        this.selectedShape.set(selectedShape);
    }

    public void deleteSelectedShapes() {

        ObservableList<Shape> tempList = getTempList();

        undoDeque.addLast(tempList);

        for (var shape : selectedShapes) {
            shapes.remove(shape);
        }
    }

    public void changeSizeOnSelectedShapes() {
        ObservableList<Shape> tempList = getTempList();

        undoDeque.addLast(tempList);

        for (var shape : selectedShapes) {
            shape.setSize(getShapeSizeAsDouble());
        }
    }

    public void changeColorOnSelectedShapes() {
        ObservableList<Shape> tempList = getTempList();

        undoDeque.addLast(tempList);

        for (var shape : selectedShapes) {
            shape.setColor(getColor());
        }
    }

    public ObservableList<Shape> getTempList() {
        ObservableList<Shape> tempList = FXCollections.observableArrayList();

        for (Shape shape : shapes) {
            if (shape.getClass() == Rectangle.class)
                tempList.add(ShapesFactory.rectangleOf(shape));
            if (shape.getClass() == Circle.class)
                tempList.add(ShapesFactory.circleOf(shape));
        }
        return tempList;
    }

    public void updateShapesListWithUndo() {
        shapes.clear();
        shapes.addAll(undoDeque.removeLast());
    }

    public void updateShapesListWithRedo() {
        shapes.clear();
        shapes.addAll(redoDeque.removeLast());
    }

    public void selectedShapesContains(Shape selectedShape) {
        if (selectedShapes.contains(selectedShape)) {
            selectedShape.setBorderColor(Color.TRANSPARENT);
            selectedShapes.remove(selectedShape);
        } else {
            selectedShape.setBorderColor(Color.RED);
            selectedShapes.add(selectedShape);
        }
    }

}
