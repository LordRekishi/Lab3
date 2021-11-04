package se.iths.java21.patrik.lab3;

import javafx.collections.FXCollections;
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
    Model model;
    Server server;

    public MenuItem connectToServer;

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

    public PaintController() {}

    public PaintController(Model model) {
        this.model = model;
    }

    public void initialize() {
        model = new Model();
        server = new Server();

        canvas.widthProperty().addListener(observable -> executeDraw());
        canvas.heightProperty().addListener(observable -> executeDraw());

        colorPicker.valueProperty().bindBidirectional(model.colorProperty());
        shapeSize.textProperty().bindBidirectional(model.shapeSizeProperty());

        listView.setItems(model.shapes);

        selector.selectedProperty().bindBidirectional(model.selectModeProperty());

        model.shapes.addListener((ListChangeListener<Shape>) change -> executeDraw());
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
            }
        } else {
            ObservableList<Shape> tempList = model.getTempList();

            if (model.isRectangleSelected()) {
                Shape shape = ShapesFactory.rectangleOf(model.getColor(), x, y, model.getShapeSizeAsDouble());
                model.undoDeque.addLast(tempList);
                if (server.isConnected()) {
                    server.sendToServer(shape);
                } else {
                    model.shapes.add(shape);
                }

            }
            if (model.isCircleSelected()) {
                Shape shape = ShapesFactory.circleOf(model.getColor(), x, y, model.getShapeSizeAsDouble());
                model.undoDeque.addLast(tempList);
                if (server.isConnected()) {
                    server.sendToServer(shape);
                } else {
                    model.shapes.add(shape);
                }
            }
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
    }

    public void onChangeSize() {
        model.changeSizeOnSelectedShapes();
    }

    public void onChangeColor() {
        model.changeColorOnSelectedShapes();
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
    }

    public void clearSelected() {
        for (var shape : model.shapes) {
            shape.setBorderColor(Color.TRANSPARENT);
        }
        model.selectedShapes.clear();
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


    public void connectServer() {
        if (server.isConnected())
            connectToServer.setText("Connect to Server");
        else
            connectToServer.setText("Disconnect from Server");

        server.connect(model);
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