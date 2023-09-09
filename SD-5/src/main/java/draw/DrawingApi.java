package draw;


public interface DrawingApi {

    Position getPosition();

    void drawCircle(Position position);

    void drawLine(Position position1, Position position2);

    void drawWindow();
}