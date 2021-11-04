package se.iths.java21.patrik.lab3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaintControllerTest {
    PaintController paintController = new PaintController(new Model());
    Model model = paintController.model;

    @Test
    void circleClick() {
        paintController.circleClick();

        assertTrue(model.isCircleSelected(), "Should return true");
    }

    @Test
    void onUndo() {
    }
}