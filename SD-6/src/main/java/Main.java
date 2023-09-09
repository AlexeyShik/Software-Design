import java.util.List;

import token.Token;
import tokenizer.Tokenizer;
import visitor.CalcVisitor;
import visitor.ParserVisitor;
import visitor.PrintVisitor;

public class Main {

    private static final String WHITESPACE = " ";

    public static void main(String[] args) {
        List<Token> tokens = handleParseEvent(new Tokenizer().tokenize(System.in));
        handlePrintEvent(tokens);
        handleCalcEvent(tokens);
    }

    private static List<Token> handleParseEvent(List<Token> inputTokens) {
        ParserVisitor visitor = new ParserVisitor();
        for (Token token : inputTokens) {
            token.accept(visitor);
        }
        return visitor.getTokens();
    }

    private static void handlePrintEvent(List<Token> tokens) {
        StringBuilder builder = new StringBuilder();
        PrintVisitor visitor = new PrintVisitor(builder);
        int remain = tokens.size();
        for (Token token : tokens) {
            token.accept(visitor);
            --remain;
            if (remain > 0) {
                builder.append(WHITESPACE);
            }
        }
        System.out.println("Reverse polish notation: " + visitor.getString());
    }

    private static void handleCalcEvent(List<Token> tokens) {
        CalcVisitor visitor = new CalcVisitor();
        for (Token token : tokens) {
            token.accept(visitor);
        }
        System.out.println("Result of evaluation: " + visitor.getResult());
    }
}
