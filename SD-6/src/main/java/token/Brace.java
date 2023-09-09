package token;

import visitor.Visitor;

public abstract class Brace implements Token {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public abstract String getSymbol();
}
