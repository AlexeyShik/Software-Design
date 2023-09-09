package draw;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JavaFxDrawingApi implements DrawingApi {

    private static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int RADIUS = 15;

    private final Frame frame;

    public JavaFxDrawingApi() {
        frame = new Frame();
    }

    @Override
    public Position getPosition() {
        return new Position((int) SCREEN_DIMENSION.getWidth() / 2, (int) SCREEN_DIMENSION.getHeight() / 2);
    }

    @Override
    public void drawCircle(Position position) {
        frame.add(
            canvas -> canvas.getGraphicsContext2D().fillOval(
                position.width() - RADIUS,
                position.height() - RADIUS,
                2 * RADIUS,
                2 * RADIUS
            )
        );
    }

    @Override
    public void drawLine(Position position1, Position position2) {
        frame.add(
            canvas -> canvas.getGraphicsContext2D().strokeLine(
                position1.width(),
                position1.height(),
                position2.width(),
                position2.height()
            )
        );
    }

    @Override
    public void drawWindow() {
        Application.launch(Frame.class);
    }

    public static class Frame extends Application {

        private static final List<Consumer<Canvas>> drawOperations = new ArrayList<>();

        public Frame() {
        }

        @Override
        public void start(Stage stage) {
            Canvas canvas = new Canvas(SCREEN_DIMENSION.getWidth(), SCREEN_DIMENSION.getHeight());
            stage.setScene(new Scene(new Group(canvas), Color.WHITE));
            stage.setResizable(false);
            canvas.getGraphicsContext2D().setFill(Color.GRAY);
            canvas.getGraphicsContext2D().setStroke(Color.GRAY);
            drawOperations.forEach(op -> op.accept(canvas));
            drawOperations.clear();
            stage.show();
        }

        public void add(Consumer<Canvas> operation) {
            drawOperations.add(operation);
        }
    }
}
