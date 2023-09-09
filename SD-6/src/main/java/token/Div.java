package token;

public class Div extends Operation {

    public Div() {
        super(HIGH_PRIORITY);
    }

    @Override
    public String getSymbol() {
        return "/";
    }

    @Override
    public double evaluate(double x, double y) {
        return x / y;
    }
}
