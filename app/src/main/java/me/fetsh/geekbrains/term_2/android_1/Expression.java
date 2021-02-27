package me.fetsh.geekbrains.term_2.android_1;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Expression {


    private final List<String> expression  = new ArrayList<>();

    static boolean isNumber(String token) {
        try{
            Double.valueOf(token);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public boolean addAll(Collection<? extends String> collection) {
        return expression.addAll(collection);
    }

    public void addDot() {
        if (!dotIsAllowed()) return;

        appendToLast(".");
    }

    private boolean dotIsAllowed() {
        if (expression.isEmpty()) return false;
        if (!isNumber(lastToken())) return false;
        return !lastToken().contains(".");
    }

    public void addDigit(String number) {
        if (expression.isEmpty()) {
            expression.add(number);
        } else if (isNumber(lastToken())){
            if (lastToken().equals("0")) {
                replaceLast(number);
            } else {
                appendToLast(number);
            }
        } else if (lastToken().equals(Operator.MINUS.getRealSign())){
            if (expression.size() == 1 || Operator.isOperatorExceptMinus(beforeLastToken())) {
                appendToLast(number);
            } else {
                expression.add(number);
            }
        } else {
            expression.add(number);
        }
    }

    private void appendToLast(String str) {
        if (expression.isEmpty()) {
            expression.add(str);
        } else {
            String lastToken = expression.get(expression.size() - 1);
            expression.set(expression.size() - 1, lastToken.concat(str));
        }
    }

    private void replaceLast(String str) {
        if (expression.isEmpty()) {
            expression.add(str);
        } else {
            expression.set(expression.size() - 1, str);
        }
    }

    public void dropLast() {
        if (!expression.isEmpty()) {
            String lastToken = expression.get(expression.size() - 1);
            if (lastToken.length() < 2) {
                expression.remove(expression.size() - 1);
            } else {
                expression.set(expression.size() - 1, lastToken.substring(0, lastToken.length() - 1));
            }
        }
    }

    private String lastToken() {
        return expression.get(expression.size() - 1);
    }

    private String beforeLastToken() {
        return expression.get(expression.size() - 2);
    }


    public void clear() {
        expression.clear();
    }

    public void addOperator(Operator operator) {
        if (operator.equals(Operator.MINUS)) {
            addMinus();
        } else {
            addOperatorExceptMinus(operator);
        }
    }

    private void addOperatorExceptMinus(Operator operator) {
        if (expression.isEmpty()) return;
        if (Operator.isOperator(lastToken())) {
            if (expression.size() > 1 && Operator.isOperator(beforeLastToken())) {
                dropLast();
            }
            replaceLast(operator.getRealSign());
        } else {
            expression.add(operator.getRealSign());
        }
    }

    private void addMinus() {
        if (!expression.isEmpty() && lastToken().equals(Operator.MINUS.getRealSign())) return;
        expression.add(Operator.MINUS.getRealSign());
    }

    public Stream<String> getExpression() {
        return expression.stream();
    }

    public boolean isEmpty() {
        return expression.isEmpty();
    }

    public String getFormula() {
        return getExpression()
                .map(t -> Operator.fromString(t).map(Operator::getVisibleSign).orElse(t))
                .collect(Collectors.joining(""));
    }

    private void debug(){
        Log.e("Calc: Expression size ", Integer.toString(expression.size()));
        Log.e("Calc: Expression ", expression.toString());
    }

}
