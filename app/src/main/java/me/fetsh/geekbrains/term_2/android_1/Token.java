package me.fetsh.geekbrains.term_2.android_1;

import androidx.annotation.NonNull;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface Token {

    Token emptyToken = new EmptyToken();

    static Token empty() {
        return emptyToken;
    }

    static Token of(String value) {
        value = value.replace(",", "");
        try{
            return NumberToken.of(value);
        } catch(NumberFormatException numberE){
           try {
               return OperatorToken.of(value);
           } catch (OperatorFormatException operatorE) {
               return emptyToken;
           }
        }
    }

    Token appendDigit(NumberToken digit, Token secondLastToken);

    Token appendDot();

    default Token appendOperator(Operator operator) {
        return OperatorToken.of(operator.getRealSign());
    };

    String getValue();

    default void ifPresent(Consumer<Token> consumer) {
        if (!(this instanceof EmptyToken)) {
            consumer.accept(this);
        }
    }

    default void ifPresentOrElse(Consumer<Token> consumer, Runnable runnable) {
        if (this instanceof EmptyToken) {
            runnable.run();
        } else {
            consumer.accept(this);
        }
    }

    default void ifIsModificationOfOrElse(
            Token other,
            Consumer<Token> trueConsumer,
            Consumer<Token> falseConsumer) {
        if (isModificationOf(other)) {
            trueConsumer.accept(this);
        } else {
            falseConsumer.accept(this);
        }
    }

    default boolean isModificationOf(Token other) {
        if (other.isEmpty()) return false;
        if (other instanceof EngineeringToken) return true;
        if (other instanceof ZeroIntegerToken) return true;
        return getValue().startsWith(other.getValue());
    }

    default boolean isEmpty() {
        return this instanceof EmptyToken;
    }

    default boolean isPresent() {
        return !isEmpty();
    }

    default Token dropLastChar() {
        return emptyToken;
    }

    default boolean isNotMinusOperator() {
        return this instanceof NotMinusOperatorToken;
    }

    default boolean isOperator() {
        return this instanceof OperatorToken;
    }

    default boolean isNumber() {
        return this instanceof NumberToken;
    }

    default boolean isDivisionOperator() {
        return false;
    };

    default BiFunction<Double, Double, Double> getOperation() {
        return null;
    };

    default Double getDouble() {
        return null;
    };

    default String toUserString() {
        return toString();
    }

    class ValueToken {
        private final String value;

        public ValueToken(@NonNull String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @NonNull
        @Override
        public String toString() {
            return value;
        }
    }

    class OperatorToken extends ValueToken implements Token {

        /**
         *
         * @param      value   the string to be parsed.
         * @return     a {@code OperatorToken} object holding the value
         *             represented by the {@code String} argument.
         * @throws     OperatorFormatException  if the string does not contain a
         *             parsable operator.
         */
        public static Token of(String value) throws OperatorFormatException {
            Operator operator = Operator.ofSign(value);
            if (operator.equals(Operator.MINUS)) {
                return new MinusOperatorToken();
            } else {
                return new NotMinusOperatorToken(value);
            }
        }

        public OperatorToken(String value) {
            super(value);
        }

        @Override
        public Token appendDigit(NumberToken digit, Token secondLastToken) {
            return digit;
        }

        @Override
        public Token appendDot() {
            return emptyToken;
        }

        @Override
        public BiFunction<Double, Double, Double> getOperation() {
            return Operator.ofSign(getValue()).getOperation();
        }

        @Override
        public String toUserString() {
            return Operator.ofSign(getValue()).getVisibleSign();
        }
    }

    class NotMinusOperatorToken extends OperatorToken implements Token {
        public NotMinusOperatorToken(String value) {
            super(value);
        }

        @Override
        public boolean isDivisionOperator() {
            return getValue().equals(Operator.DIVIDE.getRealSign());
        }
    }

    class MinusOperatorToken extends OperatorToken implements  Token {
        public MinusOperatorToken() {
            super(Operator.MINUS.getRealSign());
        }

        @Override
        public Token appendDigit(NumberToken digit, Token prevToken) {
            if (prevToken instanceof NotMinusOperatorToken || prevToken instanceof EmptyToken) {
                return NumberToken.of(getValue() + digit.getValue());
            } else {
                return digit;
            }
        }

        @Override
        public Token appendOperator(Operator operator) {
            if (operator.equals(Operator.MINUS)) {
                return emptyToken;
            } else {
                return super.appendOperator(operator);
            }
        }

    }

    abstract class NumberToken extends ValueToken implements Token {

        public NumberToken(String value) {
            super(value);
        }

        /**
         *
         * @param      value   the string to be parsed.
         * @return     a {@code NumberToken} object holding the value
         *             represented by the {@code String} argument.
         * @throws     NumberFormatException  if the string does not contain a
         *             parsable number.
         */
        public static Token of(String value) throws NumberFormatException {
            Double.valueOf(value);
            if (value.contains("E")) {
                return new EngineeringToken(value);
            } else if (value.contains(".")) {
                return new DoubleToken(value);
            } else if (value.equals("0")) {
                return new ZeroIntegerToken();
            } else {
                return new IntegerToken(value);
            }
        }

        @Override
        public Token dropLastChar() {
            return Token.of(getValue().substring(0, getValue().length() - 1));
        }

        @Override
        public Double getDouble() {
            return Double.valueOf(getValue());
        }

        @Override
        public String toUserString() {
            return Formatter.format(getValue());
        }
    }
    class EngineeringToken extends NumberToken {

        public EngineeringToken(String value) {
            super(value);
        }

        @Override
        public Token dropLastChar() {
            return emptyToken;
        }

        @Override
        public Token appendDigit(NumberToken digit, Token secondLastToken) {
            return digit;
        }

        @Override
        public Token appendDot() {
            return emptyToken;
        }

    }
    class DoubleToken extends NumberToken {

        DoubleToken(String value) {
            super(value);
        }

        @Override
        public Token appendDigit(NumberToken digit, Token secondLastToken) {
            return NumberToken.of(getValue() + digit.getValue());
        }

        @Override
        public Token appendDot() {
            return emptyToken;
        }
    }

    class IntegerToken extends NumberToken {

        public IntegerToken(String value) {
            super(value);
        }

        @Override
        public Token appendDigit(NumberToken digit, Token secondLastToken) {
            return NumberToken.of(getValue() + digit.getValue());
        }

        @Override
        public Token appendDot() {
            return new DoubleToken(getValue() + ".");
        }
    }

    class ZeroIntegerToken extends IntegerToken implements Token {

        public ZeroIntegerToken() {
            super("0");
        }

        @Override
        public Token appendDigit(NumberToken digit, Token secondLastToken) {
            return digit;
        }
    }

    class EmptyToken implements Token {
        @Override
        public Token appendDigit(NumberToken digit, Token secondLastToken) {
            return digit;
        }

        @Override
        public Token appendDot() {
            return this;
        }

        @Override
        public Token appendOperator(Operator operator) {
            if (operator.equals(Operator.MINUS)) {
                return new MinusOperatorToken();
            } else {
                return this;
            }
        }

        @Override
        public String getValue() {
            return null;
        }
    }
}
