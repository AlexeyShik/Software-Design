package tokenizer;

import token.Token;

public class Error implements State {
    @Override
    public Token accept(CachedInputStream is) {
        throw new IllegalArgumentException("Unexpected token found: " + is.getCurrent()
            + ". Expected integer number, '+', '-', '*', '/' operation, or '(', ')' braces");
    }
}
