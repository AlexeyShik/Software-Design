package token;

public class Sub extends Operation {

    public Sub() {
        super(LOW_PRIORITY);
    }

    @Override
    public String getSymbol() {
        return "-";
    }

    @Override
    public double evaluate(double x, double y) {
        return x - y;
    }
}
