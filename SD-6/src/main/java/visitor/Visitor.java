package visitor;

import token.Brace;
import token.Number;
import token.Operation;

public interface Visitor {

    void visit(Number number);

    void visit(Operation operation);

    void visit(Brace brace);

}
