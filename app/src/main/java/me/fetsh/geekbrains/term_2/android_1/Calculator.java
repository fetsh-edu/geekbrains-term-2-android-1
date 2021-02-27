package me.fetsh.geekbrains.term_2.android_1;

public class Calculator {

    private final CalculatorActivity activity;
    private final Expression expression = new Expression();
    private final RPNExpression rpnExpression = new RPNExpression();
    private double result = 0D;

    public Calculator(CalculatorActivity activity) {
        this.activity = activity;
    }

    public void handleDot() {
        expression.addDot();
        setFormula();
        setComputedResult();
    }

    private void addDigit(String number) {
        expression.addDigit(number);
        setFormula();
        setComputedResult();
    }

    public void handleOperator(Operator operator) {
        expression.addOperator(operator);
        setFormula();
        setComputedResult();
    }

    public void handleDelete() {
        expression.dropLast();
        setFormula();
        setComputedResult();
    }

    public boolean handleClear() {
        expression.clear();
        setFormula();
        setComputedResult();
        return true;
    }

    public void handleEquals() {
        if (expression.isEmpty()) return;
        rpnExpression.setInfixExpression(expression);
        if (!rpnExpression.isReady()) return;

        Double tempResult = rpnExpression.evaluate();
        if (rpnExpression.hasErrors() || !Double.isFinite(tempResult)) {
            setResult(rpnExpression.getErrors().findFirst().orElse("Error"));
        } else{
            expression.clear();
            expression.addDigit(trimZeroes(Double.toString(tempResult)));
            setFormula();
            setResult("");
        }
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

    private void computeResult() {
        rpnExpression.setInfixExpression(expression);
        if (rpnExpression.isValid()) result = rpnExpression.evaluate();
    }

    private String getFormattedResult() {
        if (expression.isEmpty()) return "";
        if (!rpnExpression.isReady()) return "";
        if (rpnExpression.hasErrors()) return "";

        return trimZeroes(Double.toString(result));
    }
    private String trimZeroes(String str) {
        if (!str.endsWith(".0")) return str;

        return str.substring(0, str.length() - 2);
    }

}
