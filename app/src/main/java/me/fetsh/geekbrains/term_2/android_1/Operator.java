package me.fetsh.geekbrains.term_2.android_1;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

public enum Operator {
    PLUS("+", "+", (a, b) -> BigDecimal.valueOf(a).add(BigDecimal.valueOf(b), MathContext.DECIMAL64).doubleValue()),
    MINUS("−", "-", (a, b) -> BigDecimal.valueOf(a).subtract(BigDecimal.valueOf(b), MathContext.DECIMAL64).doubleValue()),
    DIVIDE("÷", "/", (a, b) -> BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), MathContext.DECIMAL64).doubleValue()),
    MULTIPLY("×", "*", (a, b) -> BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b), MathContext.DECIMAL64).doubleValue());

    private final String visibleSign;
    private final String realSign;

    public BiFunction<Double, Double, Double> getOperation() {
        return operation;
    }

    private final BiFunction<Double, Double, Double> operation;

    Operator(String visibleSign, String realSign, BiFunction<Double, Double, Double> operation) {
        this.visibleSign = visibleSign;
        this.realSign = realSign;
        this.operation = operation;
    }

    public static Operator ofSign(String sign) {
        Operator result;
        switch (sign) {
            case "+":
                result = Operator.PLUS;
                break;
            case "-":
                result = Operator.MINUS;
                break;
            case "/":
                result = Operator.DIVIDE;
                break;
            case "*":
                result = Operator.MULTIPLY;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }

    public String getVisibleSign() {
        return visibleSign;
    }

    public String getRealSign() {
        return realSign;
    }
}
