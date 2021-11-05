package se.iths.java21.patrik.lab3;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import se.iths.java21.patrik.lab3.shapes.Shape;
import se.iths.java21.patrik.lab3.shapes.ShapesFactory;

import static org.junit.jupiter.api.Assertions.*;

class PaintControllerTest {
    PaintController paintController = new PaintController(new Model());
    Model model = paintController.model;
    ObservableList<Shape> shapes = model.shapes;

    Shape circle1 = ShapesFactory.circleOf(Color.BLACK, 100, 200, 50);
    Shape circle2 = ShapesFactory.circleOf(Color.RED, 40, 100, 10);
    Shape rectangle1 = ShapesFactory.rectangleOf(Color.BLUE, 40, 100, 100);
    Shape rectangle2 = ShapesFactory.rectangleOf(Color.GREEN, 200, 10, 30);

    @Test
    void whenActivatedCircleClickShouldReturnTrueRectangleReturnFalse() {
        paintController.circleClick();

        assertTrue(model.isCircleSelected(), "Should return true");
        assertFalse(model.isRectangleSelected(), "Should return false");
    }

    @Test
    void whenActivatedRectangleClickShouldReturnTrueCircleReturnFalse() {
        paintController.rectangleClick();

        assertTrue(model.isRectangleSelected(), "Should return true");
        assertFalse(model.isCircleSelected(), "Should return false");
    }

    @Test
    void addShapeToShapeListAndUndoDequeThenExecuteUndoShouldReturnPreviousListOfShapes() {
        shapes.add(circle1);

        model.undoDeque.addLast(model.getTempList());
        shapes.add(rectangle1);

        model.undoDeque.addLast(model.getTempList());
        shapes.add(circle2);

        // Shapes: 3 shapes
        // Undo: list(1), list(2)

        paintController.onUndo();
        var result = shapes.size();

        assertEquals(2, result, "Should have a size of 2");
    }

    @Test
    void addShapeToShapesAndUndoDequeThenOnUntoShouldReturnEmptyListAndReturn() {
        shapes.add(rectangle1);
        model.undoDeque.addLast(model.getTempList());

        paintController.onUndo();
        paintController.onUndo();

        var result = model.undoDeque.size();

        assertEquals(0, result, "Should return empty");
    }

    @Test
    void addShapesToShapesListAndToSelectedShapesThenRunOnDeleteShouldReturnEmptyList() {
        shapes.add(rectangle2);
        model.selectedShapes.add(rectangle2);

        paintController.onDelete();

        var result = shapes.size();

        assertEquals(0, result, "Should return zero");
    }

    @Test
    void addShapesToShapesListAndToSelectedShapesThenRunOnChangeSizeShouldReturnUpdatedSize() {
        shapes.add(rectangle2);
        model.selectedShapes.add(rectangle2);

        model.setShapeSize("10");

        paintController.onChangeSize();

        var result = rectangle2.getSize();

        assertEquals(10, result, "Should return 10");
    }

    @Test
    void addShapesToShapesListAndToSelectedShapesThenRunOnChangeColorShouldReturnUpdatedColor() {
        shapes.add(rectangle1);
        model.selectedShapes.add(rectangle1);

        model.setColor(Color.RED);

        paintController.onChangeColor();

        var result = rectangle1.getColor();

        assertEquals(Color.RED, result, "Should return Color.RED");
    }

    @Test
    void addShapeToSelectedShapesAndShapesThenClearSelectedShouldReturnZeroBorderShouldBeTransparent() {
        model.selectedShapes.add(circle2);

        circle2.setBorderColor(Color.RED);
        shapes.add(circle2);

        paintController.clearSelected();

        var result = model.selectedShapes.size();
        var borderColor = circle2.getBorderColor();

        assertEquals(0, result, "Should return zero size");
        assertEquals(Color.TRANSPARENT, borderColor, "Should be set to Transparent");
    }
}