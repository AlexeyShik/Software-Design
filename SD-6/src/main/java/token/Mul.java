package token;

public class Mul extends Operation {

    public Mul() {
        super(HIGH_PRIORITY);
    }

    @Override
    public String getSymbol() {
        return "*";
    }

    @Override
    public double evaluate(double x, double y) {
        return x * y;
    }
}
