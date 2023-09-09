package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import draw.DrawingApi;
import draw.Position;

public class EdgeListGraph extends Graph {

    private static final long C = 40;

    private List<Edge> edgeList;

    public EdgeListGraph(DrawingApi drawingApi) {
        super(drawingApi);
    }

    @Override
    public void drawGraph() {
        Position position = drawingApi.getPosition();
        Set<Integer> vertexes = new TreeSet<>();
        for (Edge e : edgeList) {
            vertexes.add(e.from);
            vertexes.add(e.to);
        }
        int n = vertexes.size();
        int cnt = 0;
        Map<Integer, Position> vertexPositions = new HashMap<>();
        for (int v : vertexes) {
            double arg = 2.0 * Math.PI * cnt++ / n;
            Position current = new Position(
                (int) (position.width() + Math.sin(arg) * C * n),
                (int) (position.height() + Math.cos(arg) * C * n)
            );
            vertexPositions.put(v, current);
            drawingApi.drawCircle(current);
        }
        for (Edge e : edgeList) {
            drawingApi.drawLine(vertexPositions.get(e.from), vertexPositions.get(e.to));
        }
        drawingApi.drawWindow();
    }

    @Override
    public void readGraph() {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        edgeList = new ArrayList<>(m);
        for (int i = 0; i < m; ++i) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            edgeList.add(new Edge(from, to));
        }
    }

    private record Edge(int from, int to) { }
}
