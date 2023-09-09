package token;

import visitor.Visitor;

public interface Token {

    void accept(Visitor visitor);
}
