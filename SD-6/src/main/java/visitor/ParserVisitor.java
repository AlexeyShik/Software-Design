package visitor;

import java.util.ArrayList;
import java.util.List;

import token.Brace;
import token.LeftBrace;
import token.Number;
import token.Operation;
import token.RightBrace;
import token.Token;

public class ParserVisitor implements Visitor {

    private final List<Token> tokens;
    private final List<Token> stack;

    public ParserVisitor() {
        tokens = new ArrayList<>();
        stack = new ArrayList<>();
    }

    @Override
    public void visit(Number number) {
        tokens.add(number);
    }

    @Override
    public void visit(Operation operation) {
        int lastIndex = stack.size() - 1;
        while (!stack.isEmpty()
            && stack.get(lastIndex) instanceof Operation
            && ((Operation) stack.get(lastIndex)).getPriority() >= operation.getPriority()) {
            tokens.add(stack.get(lastIndex));
            stack.remove(lastIndex);
            --lastIndex;
        }
        stack.add(operation);
    }

    @Override
    public void visit(Brace brace) {
        if (brace instanceof LeftBrace) {
            stack.add(brace);
        } else {
            if (stack.isEmpty()) {
                throw new IllegalArgumentException("Invalid brace balance, not enough much '('");
            }

            int lastIndex = stack.size() - 1;
            while (!(stack.get(lastIndex) instanceof LeftBrace)) {
                if (stack.get(lastIndex) instanceof RightBrace) {
                    throw new IllegalArgumentException("Invalid brace balance, not enough much '('");
                }

                tokens.add(stack.get(lastIndex));
                stack.remove(lastIndex);
                --lastIndex;

                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid brace balance, not enough much '('");
                }
            }
            stack.remove(lastIndex);
        }
    }

    public List<Token> getTokens() {
        int lastIndex = stack.size() - 1;
        while (lastIndex >= 0) {
            tokens.add(stack.get(lastIndex));
            stack.remove(lastIndex);
            --lastIndex;
        }
        return tokens;
    }
}
