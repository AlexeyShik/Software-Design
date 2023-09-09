package tokenizer;

import token.Token;

public class End implements State {

    @Override
    public Token accept(CachedInputStream is) {
        throw new IllegalStateException("Expected end of input, found: " + is.getCurrent());
    }
}
