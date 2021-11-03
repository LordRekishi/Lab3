package se.iths.java21.patrik.lab3.tools;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import se.iths.java21.patrik.lab3.Model;
import se.iths.java21.patrik.lab3.shapes.Shape;
import se.iths.java21.patrik.lab3.shapes.ShapesFactory;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private Model model;
    private Socket socket;
    private PrintWriter writer; // Skickar meddelanden till servern
    private BufferedReader reader; // Läser meddelanden från servern
    public BooleanProperty connected = new SimpleBooleanProperty();
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void connect(Model model) {
        this.model = model;
        if (connected.get()) {
            System.out.println("Disconnected from server...");
            connected.set(false);
            return;
        }

        try {
            socket = new Socket("localhost", 8000);
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            connected.set(true);
            System.out.println("Connected to Server...");

            executorService.submit(this::networkHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToServer(Shape shape) {
        if (connected.get()) {
            writer.println("Created a new Shape with coords, x:" + shape.getX() + " y:" + shape.getY());
        }
    }

    private void networkHandler() {
        try {
            while (true) {
                String line = reader.readLine();
                System.out.println(line);
                Platform.runLater(() ->
                        model.shapes.add(ShapesFactory.rectangleOf(Color.PINK, 100, 100, 50)));
            }
        } catch (IOException e) {
            System.out.println("I/O error. Disconnected.");
            Platform.runLater(() -> connected.set(false));
        }
    }
}