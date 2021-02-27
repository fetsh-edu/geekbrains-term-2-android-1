package me.fetsh.geekbrains.term_2.android_1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RPNExpression {

    private final List<String> errors = new ArrayList<>();
    private List<String> sanitizedInfixList;
    private final Queue<String> expression = new LinkedList<>();
    private final Map<String, Integer> precedence = Map.of(
            "/", 4,
            "*", 4,
            "+", 2,
            "-", 2);
    private final Map<String, BiFunction<Double, Double, Double>> operations = Map.of(
            "/", (a, b) -> a / b,
            "*", (a, b) -> a * b,
            "+", (a, b) -> a + b,
            "-", (a, b) -> a - b
    );

    public void setInfixExpression(Expression infixExpression) {
        sanitizedInfixList = sanitizeExpression(infixExpression);
        errors.clear();
    }

    public boolean isValid() {
        return !sanitizedInfixList.isEmpty();
    }

    public boolean isReady() {
        return sanitizedInfixList.size() > 2 && sanitizedInfixList.stream().anyMatch(Operator::isOperator);
    }

    private void build() {
        expression.clear();
        Stack<String> stack = new Stack<>();
        for (String token : sanitizedInfixList) {
            if (precedence.containsKey(token)) {
                while (!stack.empty() && precedence.get(token) <= precedence.get(stack.peek())) {
                    expression.add(stack.pop());
                }
                stack.push(token);
            } else {
                expression.add(token);
            }
        }
        while (!stack.isEmpty()) {
            expression.add(stack.pop());
        }
    }

    public Double evaluate() {
        build();
        Stack<Double> stack =  new Stack<>();

        for (String cur: expression) {
            if(operations.containsKey(cur)) {
                Double right = stack.pop();
                Double left = stack.pop();
                if (cur.equals("/") && right.equals(0D)) errors.add("Division by zero");
                stack.push(operations.get(cur).apply(left, right));

            } else {
                stack.push(Double.valueOf(cur));
            }
        }
        Double result = stack.pop();
        if (Double.isNaN(result)) errors.add("Result is not a number");
        if (Double.isInfinite(result)) errors.add("Result is infinite");
        return result;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public Stream<String> getErrors() {
        return errors.stream();
    }

    private List<String> sanitizeExpression(Expression expression) {
        List<String> sanitized = expression.getExpression().collect(Collectors.toList());
        while (!sanitized.isEmpty() && !Expression.isNumber(sanitized.get(sanitized.size() - 1))) {
            sanitized.remove(sanitized.get(sanitized.size() - 1));
        }
        return sanitized;
    }

}
