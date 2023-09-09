package tokenizer;

import java.util.Map;
import java.util.function.Supplier;

import token.Add;
import token.Div;
import token.LeftBrace;
import token.Mul;
import token.RightBrace;
import token.Sub;
import token.Token;

public class Start implements State {

    private static final Map<Character, Supplier<Token>> CHAR_TO_TOKEN = Map.of(
        '+', Add::new,
        '-', Sub::new,
        '*', Mul::new,
        '/', Div::new,
        '(', LeftBrace::new,
        ')', RightBrace::new
    );

    @Override
    public Token accept(CachedInputStream is) {
        char current = is.getCurrent();
        is.nextChar();
        return CHAR_TO_TOKEN.get(current).get();
    }
}
