package se.iths.java21.patrik.lab3;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import se.iths.java21.patrik.lab3.shapes.Shape;
import se.iths.java21.patrik.lab3.shapes.ShapesFactory;
import se.iths.java21.patrik.lab3.tools.Server;

import java.util.ArrayDeque;
import java.util.Deque;

public class Model {
    private Server server;
    private ObservableList<Shape> shapes;
    private ObservableList<Shape> selectedShapes;

    private Deque<ObservableList<Shape>> undoDeque;
    private Deque<ObservableList<Shape>> redoDeque;

    private final BooleanProperty circleSelected;
    private final BooleanProperty rectangleSelected;
    private final BooleanProperty lineSelected;
    private final BooleanProperty pointSelected;

    private final ObjectProperty<Color> color;
    private final ObjectProperty<Color> borderColor;
    private final StringProperty shapeSize;

    private final BooleanProperty selectMode;

    public Model() {
        server = new Server();
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
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }


    public ObservableList<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(ObservableList<Shape> shapes) {
        this.shapes = shapes;
    }

    public void addToShapes(Shape shape) {
        shapes.add(shape);
    }


    public ObservableList<Shape> getSelectedShapes() {
        return selectedShapes;
    }

    public void setSelectedShapes(ObservableList<Shape> selectedShapes) {
        this.selectedShapes = selectedShapes;
    }

    public void addToSelectedShapes(Shape shape) {
        selectedShapes.add(shape);
    }

    public void clearSelectedShapes() {
        for (var shape : shapes) {
            shape.setBorderColor(Color.TRANSPARENT);
        }
        selectedShapes.clear();
    }


    public Deque<ObservableList<Shape>> getUndoDeque() {
        return undoDeque;
    }

    public void setUndoDeque(Deque<ObservableList<Shape>> undoDeque) {
        this.undoDeque = undoDeque;
    }

    public void addToUndoDeque() {
        ObservableList<Shape> tempList = getTempList();
        undoDeque.addLast(tempList);
    }

    public ObservableList<Shape> getFromUndoDeque() {
        return undoDeque.removeLast();
    }

    public Deque<ObservableList<Shape>> getRedoDeque() {
        return redoDeque;
    }

    public void setRedoDeque(Deque<ObservableList<Shape>> redoDeque) {
        this.redoDeque = redoDeque;
    }

    public void addToRedoDeque() {
        ObservableList<Shape> tempList = getTempList();
        redoDeque.addLast(tempList);
    }

    public ObservableList<Shape> getFromRedoDeque() {
        return redoDeque.removeLast();
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
        addToUndoDeque();

        for (var shape : selectedShapes) {
            shapes.remove(shape);
        }
    }

    public void changeSizeOnSelectedShapes() {
        addToUndoDeque();

        for (var shape : selectedShapes) {
            shape.setSize(getShapeSizeAsDouble());
        }
    }

    public void changeColorOnSelectedShapes() {
        addToUndoDeque();

        for (var shape : selectedShapes) {
            shape.setColor(getColor());
        }
    }


    public ObservableList<Shape> getTempList() {
        ObservableList<Shape> tempList = FXCollections.observableArrayList();

        for (Shape shape : shapes) {
            tempList.add(shape.copyOf());
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


    public void checkIfInsideShape(double x, double y) {
        for (var shape : shapes) {
            if (shape.isInsideShape(x, y))
                selectedShapesContains(shape);
        }
    }


    public void addShapeToShapesList(Shape shape) {
        if (shape == null)
            return;

        addToUndoDeque();
        addToShapes(shape);
    }

    public void addShapeToShapesList(String line) {
        if (line == null || line.contains("joined") || line.contains("left"))
            return;

        addToUndoDeque();
        addToShapes(ShapesFactory.convertSVGToShape(line));
    }


    public void checkServerOrLocalThenAddShape(Shape shape) {
        if (server.isConnected()) {
            server.sendToServer(shape);
        } else {
            addShapeToShapesList(shape);
        }
    }

    public void connectToServer() {
        if (server.isConnected())
            server.setConnectToServerMenuItemText("Connect to Server");
        else
            server.setConnectToServerMenuItemText("Disconnect from Server");

        server.connect(this);
    }


    public void toggleCircle() {
        setCircleSelected(true);
        setRectangleSelected(false);
    }

    public void toggleRectangle() {
        setCircleSelected(false);
        setRectangleSelected(true);
    }


    public void undo() {
        if (getUndoDeque().isEmpty())
            return;

        addToRedoDeque();
        updateShapesListWithUndo();
    }

    public void redo() {
        if (getRedoDeque().isEmpty())
            return;

        addToUndoDeque();
        updateShapesListWithRedo();
    }
}
