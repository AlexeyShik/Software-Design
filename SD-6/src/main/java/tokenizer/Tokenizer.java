package tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import token.Token;

public class Tokenizer {

    private static final Set<Character> OPERATIONS = Set.of('+', '-', '*', '/');
    private static final Set<Character> BRACES = Set.of('(', ')');

    private static final State START = new Start();
    private static final State NUMBER = new Number();
    private static final State END = new End();
    private static final State ERROR = new Error();

    public List<Token> tokenize(InputStream inputStream) {
        List<Token> tokens = new ArrayList<>();
        try (CachedInputStream is = new CachedInputStream(inputStream)) {
            is.nextChar();
            is.skipWhitespace();
            while (is.hasMore()) {
                tokens.add(getNextState(is).accept(is));
                is.skipWhitespace();
            }
        } catch (IOException e) {
            System.err.println("Cannot close input stream resource " + e.getMessage());
        }

        return tokens;
    }

    private static State getNextState(CachedInputStream is) {
        char current = is.getCurrent();
        if (BRACES.contains(current) || OPERATIONS.contains(current)) {
            return START;
        } else if (Character.isDigit(current)) {
            return NUMBER;
        } else if (!is.hasMore()) {
            return END;
        }
        return ERROR;
    }
}
