package se.iths.java21.patrik.lab3;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import se.iths.java21.patrik.lab3.shapes.Shape;
import se.iths.java21.patrik.lab3.shapes.ShapesFactory;

import javax.imageio.ImageIO;
import java.io.File;


public class PaintController {

    Model model;

    public Button circleButton;
    public Button rectangleButton;
    public Button lineButton;
    public Button pointButton;
    public Button deleteButton;
    public Button changeSizeButton;
    public Button changeColorButton;

    public Canvas canvas;

    public CheckBox selector;
    public ColorPicker colorPicker;
    public TextField shapeSize;
    public ListView<Shape> listView;

    public void initialize() {
        model = new Model();

        // Observable list kan kopplas bidirectional
        // listView.getSelectionModel().getSelectedItems()

        // Kopplingar till Model
        canvas.widthProperty().addListener(observable -> executeDraw());
        canvas.heightProperty().addListener(observable -> executeDraw());

        colorPicker.valueProperty().bindBidirectional(model.colorProperty());
        shapeSize.textProperty().bindBidirectional(model.shapeSizeProperty());

        listView.setItems(model.shapes);

        selector.selectedProperty().bindBidirectional(model.selectModeProperty());

    }

    public void onCanvasClicked(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        if (model.isSelectMode()) {
            for (var shape : model.shapes) {
                if (shape.isInsideShape(x, y)) {
                    if (model.selectedShapes.contains(shape)) {
                        shape.setBorderColor(Color.TRANSPARENT);
                        model.selectedShapes.remove(shape);
                    } else {
                        shape.setBorderColor(Color.RED);
                        model.selectedShapes.add(shape);
                    }
                }
                executeDraw();
            }

        } else {
            for (var shape : model.shapes) {
                shape.setBorderColor(Color.TRANSPARENT);
                model.selectedShapes.clear();
            }

            ObservableList<Shape> tempList = model.getTempList();

            if (model.isRectangleSelected()) {
                model.undoDeque.addLast(FXCollections.observableArrayList(tempList));
                model.shapes.add(ShapesFactory.rectangleOf(model.getColor(), x, y, model.getShapeSizeAsDouble()));
            }
            if (model.isCircleSelected()) {
                model.undoDeque.addLast(FXCollections.observableArrayList(tempList));
                model.shapes.add(ShapesFactory.circleOf(model.getColor(), x, y, model.getShapeSizeAsDouble()));
            }
            executeDraw();
        }
    }

    private void executeDraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (var shape : model.shapes) {
//            shape.draw(gc);
            shape.drawSVG(new SVGPath());
        }


    }

    public void circleClick() {
        model.circleSelectedProperty().setValue(true);
        model.rectangleSelectedProperty().setValue(false);
        model.lineSelectedProperty().setValue(false);
        model.pointSelectedProperty().setValue(false);
    }

    public void rectangleClick() {
        model.circleSelectedProperty().setValue(false);
        model.rectangleSelectedProperty().setValue(true);
        model.lineSelectedProperty().setValue(false);
        model.pointSelectedProperty().setValue(false);
    }

    public void lineClick() {
        model.circleSelectedProperty().setValue(false);
        model.rectangleSelectedProperty().setValue(false);
        model.lineSelectedProperty().setValue(true);
        model.pointSelectedProperty().setValue(false);
    }

    public void pointClick() {
        model.circleSelectedProperty().setValue(false);
        model.rectangleSelectedProperty().setValue(false);
        model.lineSelectedProperty().setValue(false);
        model.pointSelectedProperty().setValue(true);
    }


    public void onDelete() {
        model.deleteSelectedShapes();
        executeDraw();
    }

    public void onChangeSize() {
        model.changeSizeOnSelectedShapes();
        executeDraw();
    }

    public void onChangeColor() {
        model.changeColorOnSelectedShapes();
        executeDraw();
        listView.refresh();
    }

    public void listViewClicked() {
        var selectedShape = listView.getSelectionModel().getSelectedItem();

        if (model.selectedShapes.contains(selectedShape)) {
            selectedShape.setBorderColor(Color.TRANSPARENT);
            model.selectedShapes.remove(selectedShape);
        } else {
            selectedShape.setBorderColor(Color.RED);
            model.selectedShapes.add(selectedShape);
        }
        executeDraw();
    }


    public void onUndo() {
        if (model.undoDeque.isEmpty())
            return;

        ObservableList<Shape> tempList = model.getTempList();

        model.redoDeque.addLast(FXCollections.observableArrayList(tempList));
        model.shapes = FXCollections.observableArrayList(model.undoDeque.removeLast());
        listView.setItems(model.shapes);
        executeDraw();
    }

    public void onRedo() {
        if (model.redoDeque.isEmpty())
            return;

        ObservableList<Shape> tempList = model.getTempList();

        model.undoDeque.addLast(FXCollections.observableArrayList(tempList));
        model.shapes = FXCollections.observableArrayList(model.redoDeque.removeLast());
        listView.setItems(model.shapes);
        executeDraw();
    }


    public void onSave() {
        try {
            Image snapshot = canvas.snapshot(null, null);

            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("paint.png"));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }

    public void onExit() {
        Platform.exit();
    }
}