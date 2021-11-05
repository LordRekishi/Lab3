package se.iths.java21.patrik.lab3.tools;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    private PrintWriter writer;
    private BufferedReader reader;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final BooleanProperty connected = new SimpleBooleanProperty();
    private final StringProperty connectToServerMenuItemText = new SimpleStringProperty("Connect to Server");

    public void connect(Model model) {
        this.model = model;
        if (connected.get()) {
            System.out.println("Disconnected from server...");
            connected.set(false);
            return;
        }

        try {
            socket = new Socket("178.174.162.51", 8000);
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
            writer.println(shape.drawSVG());
        }
    }

    private void networkHandler() {
        try {
            while (true) {
                String line = reader.readLine();
                System.out.println(line);

                if (!line.contains("joined") && !line.contains("left")) {
                    Platform.runLater(() ->
                            model.shapes.add(ShapesFactory.convertSVGToShape(line)));
                }
            }
        } catch (IOException e) {
            System.out.println("I/O error. Disconnected.");
            Platform.runLater(() -> connected.set(false));
        }
    }

    public boolean isConnected() {
        return connected.get();
    }

    public BooleanProperty connectedProperty() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected.set(connected);
    }


    public String getConnectToServerMenuItemText() {
        return connectToServerMenuItemText.get();
    }

    public StringProperty connectToServerMenuItemTextProperty() {
        return connectToServerMenuItemText;
    }

    public void setConnectToServerMenuItemText(String connectToServerMenuItemText) {
        this.connectToServerMenuItemText.set(connectToServerMenuItemText);
    }
}
