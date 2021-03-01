package me.fetsh.geekbrains.term_2.android_1;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

public enum Operator {
    PLUS("+", "+", Double::sum),
    MINUS("−", "-", (a, b) -> a - b),
    DIVIDE("÷", "/", (a, b) -> a / b),
    MULTIPLY("×", "*", (a, b) -> a * b);

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
