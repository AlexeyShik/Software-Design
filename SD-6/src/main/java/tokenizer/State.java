package tokenizer;

import token.Token;

public interface State {

    Token accept(CachedInputStream is);
}
