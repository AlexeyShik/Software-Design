package visitor;

import java.util.ArrayList;
import java.util.List;

import token.Brace;
import token.Number;
import token.Operation;

public class CalcVisitor implements Visitor {

    private final List<Double> arguments;

    public CalcVisitor() {
        arguments = new ArrayList<>();
    }

    @Override
    public void visit(Number number) {
        arguments.add((double) number.getNumber());
    }

    @Override
    public void visit(Operation operation) {
        int lastIndex = arguments.size() - 1;
        if (lastIndex < 1) {
            throw new IllegalArgumentException("Expected valid expression, each operation should have 2 arguments");
        }
        double y = arguments.get(lastIndex);
        arguments.remove(lastIndex);
        --lastIndex;
        double x = arguments.get(lastIndex);
        arguments.remove(lastIndex);
        arguments.add(operation.evaluate(x, y));
    }

    @Override
    public void visit(Brace brace) {
        throw new UnsupportedOperationException("Cannot evaluate brace");
    }

    public double getResult() {
        if (arguments.isEmpty()) {
            throw new IllegalArgumentException("Expected valid expression, found empty input");
        }
        if (arguments.size() > 1) {
            throw new IllegalArgumentException("Expected valid expression, found extra tokens");
        }
        return arguments.get(0);
    }
}
