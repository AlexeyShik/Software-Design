package draw;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AwtDrawingApi extends Frame implements DrawingApi {

    private static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int RADIUS = 10;

    private final List<Consumer<Graphics2D>> drawOperations;

    public AwtDrawingApi() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        drawOperations = new ArrayList<>();
        setSize(SCREEN_DIMENSION);
        setResizable(false);
    }

    @Override
    public void paint(Graphics g) {
        drawOperations.forEach(op -> op.accept((Graphics2D) g));
    }

    @Override
    public Position getPosition() {
        return new Position(SCREEN_DIMENSION.width / 2, SCREEN_DIMENSION.height / 2);
    }

    @Override
    public void drawCircle(Position position) {
        drawOperations.add(graphics2D -> graphics2D.fill(
            new Ellipse2D.Double(
                position.width() - RADIUS,
                position.height() - RADIUS,
                2 * RADIUS,
                2 * RADIUS)
        ));
    }

    @Override
    public void drawLine(Position position1, Position position2) {
        drawOperations.add(graphics2D -> graphics2D.draw(
            new Line2D.Double(
                position1.width(),
                position1.height(),
                position2.width(),
                position2.height())
        ));
    }

    @Override
    public void drawWindow() {
        setVisible(true);
    }
}
