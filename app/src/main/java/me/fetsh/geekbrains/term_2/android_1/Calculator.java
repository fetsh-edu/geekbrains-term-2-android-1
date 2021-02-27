package me.fetsh.geekbrains.term_2.android_1;

import java.util.List;
import java.util.stream.Stream;

public class Calculator {

    public enum State {
        Input, Result
    }

    private final CalculatorActivity activity;
    private final Expression expression = new Expression();
    private final RPNExpression rpnExpression = new RPNExpression();

    private State state = State.Input;
    private double result = 0D;

    public Calculator(CalculatorActivity activity) {
        this.activity = activity;
    }

    public State getState() {
        return state;
    }

    public void restore(List<String> stringArrayList, String state) {
        expression.clear();
        expression.addAll(stringArrayList);
        setState(State.valueOf(state));
        setFormula();
        setComputedResult();
    }

    public void handleDot() {
        expression.addDot();
        setState(State.Input);
        setFormula();
        setComputedResult();
    }

    private void addDigit(String number) {
        expression.addDigit(number);
        setState(State.Input);
        setFormula();
        setComputedResult();
    }

    public void handleOperator(Operator operator) {
        expression.addOperator(operator);
        setState(State.Input);
        setFormula();
        setComputedResult();
    }

    public void handleDelete() {
        expression.dropLast();
        setState(State.Input);
        setFormula();
        setComputedResult();
    }

    public boolean handleClear() {
        expression.clear();
        setState(State.Input);
        setFormula();
        setComputedResult();
        return true;
    }

    public void handleEquals() {
        if (expression.isEmpty()) return;

        rpnExpression.setInfixExpression(expression);
        if (!rpnExpression.isReady()) return;

        result = rpnExpression.evaluate();
        if (!rpnExpression.hasErrors()) {
            expression.clear();
            expression.addDigit(trimZeroes(Double.toString(result)));
        }
        setState(State.Result);
        setFormula();
        setResult(getFormattedResult());
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

    public void setFormula() {
        activity.showFormula(expression.getFormula());
    }
    public void setResult(String string) {
        activity.showResult(string);
    }

    public void setComputedResult() {
        computeResult();
        setResult(getFormattedResult());
    }

    public Stream<String> getExpression() {
        return expression.getExpression();
    }

    private void setState(State state) {
        this.state = state;
    }

    private void computeResult() {
        rpnExpression.setInfixExpression(expression);
        if (rpnExpression.isValid()) result = rpnExpression.evaluate();
    }

    private String getFormattedResult() {
        if (expression.isEmpty()) return "";
        if (!rpnExpression.isReady()) return "";
        if (state.equals(State.Input)) {
            if (rpnExpression.hasErrors()) {
                return "";
            } else {
                return trimZeroes(Double.toString(result));
            }
        } else {
            if (rpnExpression.hasErrors()) {
                return rpnExpression.getErrors().findFirst().orElse("Error");
            } else {
                return "";
            }
        }
    }

    private String trimZeroes(String str) {
        if (!str.endsWith(".0")) return str;

        return str.substring(0, str.length() - 2);
    }

}
