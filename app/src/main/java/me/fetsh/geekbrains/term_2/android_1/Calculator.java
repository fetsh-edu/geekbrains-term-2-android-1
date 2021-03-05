package me.fetsh.geekbrains.term_2.android_1;

import java.util.List;
import java.util.stream.Stream;

public class Calculator {

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

    private void addDigit(Token.NumberToken number) {
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
        if (result instanceof Evaluation.NotReady) return;

        if (result instanceof Evaluation.Success) {
            infixExpression.clear();
            infixExpression.add(Token.of(((Evaluation.Success) result).getFormattedResult()));
        }
        setState(State.Result);
        updateDisplay(result);
    }

    public void handleDigit(String digit) {
        if (digit.length() > 1 || !Character.isDigit(digit.charAt(0))) return;

        try {
            Token numberToken = Token.NumberToken.of(digit);
            addDigit((Token.NumberToken) numberToken);
        } catch (NumberFormatException e) {
            // Ignore
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
        if (result instanceof Evaluation.Failure) {
            return ((Evaluation.Failure) result).getError();
        } else {
            return "";
        }
    }

    private String getInputStateResultString(Evaluation result) {
        if (result instanceof Evaluation.Success) {
            return ((Evaluation.Success) result).getFormattedResult();
        } else {
            return "";
        }
    }

    private void updateDisplay() {
        updateDisplay(rpnExpression.evaluate(infixExpression));
    }

    private void updateDisplay(Evaluation result) {
        activity.showFormula(infixExpression.getFormula());
        activity.showResult(getResultString(result));
    }
}
