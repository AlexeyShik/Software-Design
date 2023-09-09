package tokenizer;

import token.Token;

public class Number implements State {

    @Override
    public Token accept(CachedInputStream is) {
        int number = 0;
        while (Character.isDigit(is.getCurrent())) {
            number = 10 * number + (is.getCurrent() - '0');
            is.nextChar();
        }
        return new token.Number(number);
    }
}
