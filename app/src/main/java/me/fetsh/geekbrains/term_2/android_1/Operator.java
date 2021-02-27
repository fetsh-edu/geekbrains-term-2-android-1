package me.fetsh.geekbrains.term_2.android_1;

import java.util.Arrays;
import java.util.Optional;

public enum Operator {
    PLUS("+", "+"),
    MINUS("−", "-"),
    DIVIDE("÷", "/"),
    MULTIPLY("×", "*");

    private final String visibleSign;
    private final String realSign;

    Operator(String visibleSign, String realSign) {
        this.visibleSign = visibleSign;
        this.realSign = realSign;
    }

    public static boolean isOperator(String string) {
        return Arrays.stream(Operator.values()).anyMatch(o -> o.getRealSign().equals(string));
    }

    public static Optional<Operator> fromString(String str) {
        return Arrays.stream(Operator.values()).filter(o -> o.getRealSign().equals(str)).findAny();
    }

    public static boolean isOperatorExceptMinus(String string) {
        return isOperator(string) && !string.equals(MINUS.getRealSign());
    }

    public String getVisibleSign() {
        return visibleSign;
    }

    public String getRealSign() {
        return realSign;
    }
}
