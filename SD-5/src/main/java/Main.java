import draw.AwtDrawingApi;
import draw.DrawingApi;
import draw.JavaFxDrawingApi;
import graph.AdjacencyMatrixGraph;
import graph.EdgeListGraph;
import graph.Graph;

public class Main {

    private static final String MATRIX_OPTION = "-m";
    private static final String EDGE_OPTION = "-e";
    private static final String AWT_OPTION = "-awt";
    private static final String JAVA_FX_OPTION = "-jfx";

    public static void main(String[] args) {
        if (args == null || args.length < 2 || args[0] == null || args[1] == null) {
            System.err.println("Invalid arguments, should be: <type of graph> <type of drawing api>");
            return;
        }

        DrawingApi drawingApi;
        if (AWT_OPTION.equals(args[1])) {
            drawingApi = new AwtDrawingApi();
        } else if (JAVA_FX_OPTION.equals(args[1])) {
            drawingApi = new JavaFxDrawingApi();
        } else {
            System.err.println("Second argument should be <type of drawing api>, is '-awt' or '-jfx'");
            return;
        }

        Graph graph;
        if (MATRIX_OPTION.equals(args[0])) {
            graph = new AdjacencyMatrixGraph(drawingApi);
        } else if (EDGE_OPTION.equals(args[0])) {
            graph = new EdgeListGraph(drawingApi);
        } else {
            System.err.println("First argument should be <type of graph>, is '-m' or '-e'");
            return;
        }

        graph.readGraph();
        graph.drawGraph();
    }
}
