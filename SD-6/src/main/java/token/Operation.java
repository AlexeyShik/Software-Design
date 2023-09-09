package token;

import visitor.Visitor;

public abstract class Operation implements Token {

    protected static final int HIGH_PRIORITY = 2;
    protected static final int LOW_PRIORITY = 1;

    private final int priority;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Operation(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public abstract String getSymbol();

    public abstract double evaluate(double x, double y);
}
