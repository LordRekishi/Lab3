package se.iths.java21.patrik.lab3;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import se.iths.java21.patrik.lab3.shapes.Shape;
import se.iths.java21.patrik.lab3.shapes.ShapesFactory;
import se.iths.java21.patrik.lab3.tools.SVGWriter;
import se.iths.java21.patrik.lab3.tools.Server;

import javax.imageio.ImageIO;
import java.io.File;

public class PaintController {
    public Model model;
    public Server server;

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
        server = new Server();

        canvas.widthProperty().addListener(observable -> executeDraw());
        canvas.heightProperty().addListener(observable -> executeDraw());
        model.shapes.addListener((ListChangeListener<Shape>) change -> executeDraw());

        colorPicker.valueProperty().bindBidirectional(model.colorProperty());
        shapeSize.textProperty().bindBidirectional(model.shapeSizeProperty());
        connectToServer.textProperty().bindBidirectional(server.connectToServerMenuItemTextProperty());

        listView.setItems(model.shapes);

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
        ObservableList<Shape> tempList = model.getTempList();

        if (model.isRectangleSelected()) {
            Shape shape = ShapesFactory.rectangleOf(model.getColor(), x, y, model.getShapeSizeAsDouble());
            model.undoDeque.addLast(tempList);

            checkServerOrLocalThenAddShape(shape);
        }
        if (model.isCircleSelected()) {
            Shape shape = ShapesFactory.circleOf(model.getColor(), x, y, model.getShapeSizeAsDouble());
            model.undoDeque.addLast(tempList);

            checkServerOrLocalThenAddShape(shape);
        }
    }

    private void checkServerOrLocalThenAddShape(Shape shape) {
        if (server.isConnected()) {
            server.sendToServer(shape);
        } else {
            model.shapes.add(shape);
        }
    }


    private void executeDraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (var shape : model.shapes) {
            shape.draw(gc);
        }
    }


    public void circleClick() {
        model.setCircleSelected(true);
        model.setRectangleSelected(false);
    }

    public void rectangleClick() {
        model.setCircleSelected(false);
        model.setRectangleSelected(true);
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
        for (var shape : model.shapes) {
            shape.setBorderColor(Color.TRANSPARENT);
        }
        model.selectedShapes.clear();
    }


    public void connectServer() {
        if (server.isConnected())
            server.setConnectToServerMenuItemText("Connect to Server");
        else
            server.setConnectToServerMenuItemText("Disconnect from Server");

        server.connect(model);
    }


    public void onUndo() {
        if (model.undoDeque.isEmpty())
            return;

        ObservableList<Shape> tempList = model.getTempList();

        model.redoDeque.addLast(tempList);
        model.updateShapesListWithUndo();
    }

    public void onRedo() {
        if (model.redoDeque.isEmpty())
            return;

        ObservableList<Shape> tempList = model.getTempList();

        model.undoDeque.addLast(tempList);
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