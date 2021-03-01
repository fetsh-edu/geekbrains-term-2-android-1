package me.fetsh.geekbrains.term_2.android_1;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class Calculator {

    public static DecimalFormat defaultFormatter;
    static {
        defaultFormatter = new DecimalFormat("###,###.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        defaultFormatter.setMaximumFractionDigits(340);
    }

    public enum State {
        Input, Result
    }

    private final CalculatorActivity activity;
    private final InfixExpression infixExpression = new InfixExpression();
    private final RPNExpression rpnExpression = new RPNExpression();

    private State state = State.Input;

    public Calculator(CalculatorActivity activity) {
        this.activity = activity;
    }

    public State getState() {
        return state;
    }

    public void restore(List<String> stringArrayList, String state) {
        infixExpression.clear();
        infixExpression.addAll(stringArrayList);
        setState(State.valueOf(state));
        updateDisplay();
    }

    public void handleDot() {
        infixExpression.addDot();
        setState(State.Input);
        updateDisplay();
    }

    private void addDigit(String number) {
        infixExpression.addDigit(number);
        setState(State.Input);
        updateDisplay();
    }

    public void handleOperator(Operator operator) {
        infixExpression.addOperator(operator);
        setState(State.Input);
        updateDisplay();
    }

    public void handleDelete() {
        infixExpression.dropLastChar();
        setState(State.Input);
        updateDisplay();
    }

    public boolean handleClear() {
        infixExpression.clear();
        setState(State.Input);
        updateDisplay();
        return true;
    }

    public void handleEquals() {
        Evaluation result = rpnExpression.evaluate(infixExpression);
        if (result instanceof NotReady) return;

        if (result instanceof Success) {
            infixExpression.clear();
            infixExpression.add(Token.of(((Success) result).getFormattedResult()));
        }
        setState(State.Result);
        updateDisplay(result);
    }

    public void handleNumber(int number) {
        switch (number) {
            case (R.id.keyboard_num0):
                addDigit("0");
                break;
            case (R.id.keyboard_num1):
                addDigit("1");
                break;
            case(R.id.keyboard_num2):
                addDigit("2");
                break;
            case(R.id.keyboard_num3):
                addDigit("3");
                break;
            case(R.id.keyboard_num4):
                addDigit("4");
                break;
            case(R.id.keyboard_num5):
                addDigit("5");
                break;
            case(R.id.keyboard_num6):
                addDigit("6");
                break;
            case(R.id.keyboard_num7):
                addDigit("7");
                break;
            case(R.id.keyboard_num8):
                addDigit("8");
                break;
            case(R.id.keyboard_num9):
                addDigit("9");
                break;
        }
    }

    public Stream<String> getExpression() {
        return infixExpression.getStringExpression();
    }

    private void setState(State state) {
        this.state = state;
    }

    private String getResultString(Evaluation result) {
        if (state.equals(State.Input)) {
            return getInputStateResultString(result);
        } else if (state.equals(State.Result)) {
            return getResultStateResultString(result);
        } else {
            return "Illegal state";
        }
    }

    private String getResultStateResultString(Evaluation result) {
        if (result instanceof Failure) {
            return ((Failure) result).getErrors().findFirst().orElse("Error");
        } else {
            return "";
        }
    }

    private String getInputStateResultString(Evaluation result) {
        if (result instanceof Success) {
            return ((Success) result).getFormattedResult();
        } else {
            return "";
        }
    }

    public void updateDisplay() {
        updateDisplay(rpnExpression.evaluate(infixExpression));
    }

    private void updateDisplay(Evaluation result) {
        activity.showFormula(infixExpression.getFormula());
        activity.showResult(getResultString(result));
    }
}
