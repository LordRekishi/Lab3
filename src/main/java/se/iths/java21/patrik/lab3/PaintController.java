package se.iths.java21.patrik.lab3;

import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import se.iths.java21.patrik.lab3.shapes.Shape;
import se.iths.java21.patrik.lab3.shapes.ShapesFactory;
import se.iths.java21.patrik.lab3.tools.SVGWriter;

import javax.imageio.ImageIO;
import java.io.File;

public class PaintController {
    public Model model;

    public MenuItem connectToServer;
    public Canvas canvas;
    public CheckBox selector;
    public ColorPicker colorPicker;
    public TextField shapeSize;
    public ListView<Shape> listView;

    public PaintController(){}

    public PaintController(Model model) {
        this.model = model;
    }

    public void initialize() {
        model = new Model();

        canvas.widthProperty().addListener(observable -> executeDraw());
        canvas.heightProperty().addListener(observable -> executeDraw());
        model.getShapes().addListener((ListChangeListener<Shape>) change -> executeDraw());

        colorPicker.valueProperty().bindBidirectional(model.colorProperty());
        shapeSize.textProperty().bindBidirectional(model.shapeSizeProperty());
        connectToServer.textProperty().bindBidirectional(model.getServer().connectToServerMenuItemTextProperty());

        listView.setItems(model.getShapes());

        selector.selectedProperty().bindBidirectional(model.selectModeProperty());
    }

    public void onCanvasClicked(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        if (model.isSelectMode()) {
            model.checkIfInsideShape(x, y);
        } else {
            checkShapeTypeAndAdd(x, y);
        }
    }

    private void checkShapeTypeAndAdd(double x, double y) {
        if (model.isRectangleSelected()) {
            Shape shape = ShapesFactory.rectangleOf(model.getColor(), x, y, model.getShapeSizeAsDouble());
            model.checkServerOrLocalThenAddShape(shape);
        }
        if (model.isCircleSelected()) {
            Shape shape = ShapesFactory.circleOf(model.getColor(), x, y, model.getShapeSizeAsDouble());
            model.checkServerOrLocalThenAddShape(shape);
        }
    }

    private void executeDraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (var shape : model.getShapes()) {
            shape.draw(gc);
        }
    }


    public void circleClick() {
        model.toggleCircle();
    }

    public void rectangleClick() {
        model.toggleRectangle();
    }


    public void onDelete() {
        model.deleteSelectedShapes();
    }

    public void onChangeSize() {
        model.changeSizeOnSelectedShapes();
    }

    public void onChangeColor() {
        model.changeColorOnSelectedShapes();
    }


    public void listViewClicked() {
        var selectedShape = listView.getSelectionModel().getSelectedItem();

        model.selectedShapesContains(selectedShape);
    }

    public void clearSelected() {
        model.clearSelectedShapes();
    }


    public void connectServer() {
        model.connectToServer();
    }


    public void onUndo() {
        if (model.getUndoDeque().isEmpty())
            return;

        model.addToRedoDeque();
        model.updateShapesListWithUndo();
    }

    public void onRedo() {
        if (model.getRedoDeque().isEmpty())
            return;

        model.addToUndoDeque();
        model.updateShapesListWithRedo();
    }


    public void onSave() {
        SVGWriter.saveSVGFile(model);

        try {
            Image snapshot = canvas.snapshot(null, null);

            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("paint.png"));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }

    public void onExit() {
        System.exit(0);
    }
}