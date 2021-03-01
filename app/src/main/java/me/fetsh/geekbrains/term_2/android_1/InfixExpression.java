package me.fetsh.geekbrains.term_2.android_1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InfixExpression {

    private final List<Token> expression  = new ArrayList<>();

    static boolean isNumber(String token) {
        try{
            Double.valueOf(token);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public Stream<Token> getExpression() {
        return expression.stream().filter(Token::isPresent);
    }

    public Stream<String> getStringExpression() {
        return getExpression().map(Token::getValue);
    }

    public String getFormula() {
        return getExpression()
                .map(Token::toUserString)
                .collect(Collectors.joining(""));
    }

    public void addAll(Collection<? extends String> collection) {
        expression.addAll(collection.stream().map(Token::of).collect(Collectors.toList()));
    }

    public void addDot() {
        replaceLast(lastToken().appendDot());
    }

    public void addDigit(String digit) {
        lastToken()
                .appendDigit(digit, secondLastToken())
                .ifIsModificationOfOrElse(lastToken(), this::replaceLast, this::add);
    }


    public void addOperator(Operator operator) {
        Token newToken = lastToken().appendOperator(operator);
        if (newToken.isNotMinusOperator()) dropTrailingWhile(Token::isOperator);
        add(newToken);
    }

    public boolean isEmpty() {
        return expression.isEmpty();
    }

    public void clear() {
        expression.clear();
    }

    public void dropLastChar() {
        lastToken().dropLastChar().ifPresentOrElse(this::replaceLast, this::dropLast);
    }
    public void add(Token token) {
        if (token.isEmpty()) return;
        expression.add(token);
    }

    private void dropTrailingWhile(Predicate<Token> predicate) {
        while (expression.size() > 0 && predicate.test(expression.get(expression.size() - 1))) {
            expression.remove(expression.size() - 1);
        }
    }

    private void replaceLast(Token token) {
        if (token.isEmpty()) return;

        if (expression.isEmpty()) {
            expression.add(token);
        } else {
            expression.set(expression.size() - 1, token);
        }
    }

    private void dropLast() {
        if (isEmpty()) return;
        expression.remove(expression.size() - 1);
    }

    private Token lastToken() {
        if (expression.isEmpty()) return Token.empty();

        return expression.get(expression.size() - 1);
    }

    private Token secondLastToken() {
        if (expression.size() < 2) return Token.empty();

        return expression.get(expression.size() - 2);
    }
}
