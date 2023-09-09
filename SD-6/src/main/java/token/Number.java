package token;

import visitor.Visitor;

public class Number implements Token {

    private final int number;

    public Number(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
