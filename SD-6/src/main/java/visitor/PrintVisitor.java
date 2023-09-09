package visitor;

import token.Brace;
import token.Number;
import token.Operation;

public class PrintVisitor implements Visitor {

    private final StringBuilder builder;

    public PrintVisitor(StringBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void visit(Number number) {
        builder.append(number.getNumber());
    }

    @Override
    public void visit(Operation operation) {
        builder.append(operation.getSymbol());
    }

    @Override
    public void visit(Brace brace) {
        builder.append(brace.getSymbol());
    }

    public String getString() {
        return builder.toString();
    }
}
