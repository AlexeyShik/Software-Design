package tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CachedInputStream implements AutoCloseable {

    private final BufferedReader reader;
    private char current;

    public CachedInputStream(InputStream is) {
        reader = new BufferedReader(new InputStreamReader(is));
        current = '\0';
    }

    public void skipWhitespace() {
        while (Character.isWhitespace(current)) {
            nextChar();
        }
    }

    public void nextChar() {
        try {
            current = (char) reader.read();
        } catch (IOException e) {
            System.err.println("Exception while reading input" + e.getMessage());
        }
    }

    public char getCurrent() {
        return current;
    }

    public boolean hasMore() {
        return current != (char) -1;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
