package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import draw.DrawingApi;
import draw.Position;

public class AdjacencyMatrixGraph extends Graph {

    private static final long C = 30;

    private boolean[][] isEdge;

    public AdjacencyMatrixGraph(DrawingApi drawingApi) {
        super(drawingApi);
    }

    @Override
    public void drawGraph() {
        Position position = drawingApi.getPosition();
        int n = isEdge.length;
        List<Position> nodes = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            double arg = 2.0 * Math.PI * i / n;
            nodes.add(new Position(
                (int) (position.width() + Math.sin(arg) * C * n),
                (int) (position.height() + Math.cos(arg) * C * n)
            ));
        }
        for (int i = 0; i < n; ++i) {
            drawingApi.drawCircle(nodes.get(i));
            for (int j = 0; j < n; ++j) {
                if (isEdge[i][j]) {
                    drawingApi.drawLine(nodes.get(i), nodes.get(j));
                }
            }
        }
        drawingApi.drawWindow();
    }

    @Override
    public void readGraph() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        isEdge = new boolean[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                isEdge[i][j] = scanner.nextByte() != 0;
            }
        }
    }
}
