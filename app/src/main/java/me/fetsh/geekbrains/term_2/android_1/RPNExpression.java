package me.fetsh.geekbrains.term_2.android_1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

public class RPNExpression {

    private final Map<String, Integer> precedence = new HashMap<String, Integer>() {{
        put("/", 4);
        put("*", 4);
        put("+", 2);
        put("-", 2);
    }};

    public Evaluation evaluate(InfixExpression infixExpression) {
        if(infixExpression.isEmpty()) return Evaluation.notReady;

        List<Token> sanitizedInfixList = buildSanitizedInfixList(infixExpression);
        if(notReady(sanitizedInfixList)) return Evaluation.notReady;

        Queue<Token> rpnQueue = buildRPNQueue(sanitizedInfixList);
        Stack<Double> stack =  new Stack<>();

        for (Token token: rpnQueue) {
            if(token.isOperator()) {
                Double right = stack.pop();
                Double left = stack.pop();
//                if (token.isDivisionOperator() && right.equals(0D)) errors.add("Division by zero");
                try {
                    stack.push(token.getOperation().apply(left, right));
                } catch (ArithmeticException e) {
                    return Evaluation.failure(e.getMessage());
                }

            } else {
                stack.push(token.getDouble());
            }
        }
        Double result = stack.pop();
        if (Double.isNaN(result)) return Evaluation.failure("Result is not a number");
        if (Double.isInfinite(result)) return Evaluation.failure("Result is infinite");

        return Evaluation.success(result);
    }
    private Queue<Token> buildRPNQueue(List<Token> infixList) {
        Queue<Token> result = new LinkedList<>();
        Stack<Token> stack = new Stack<>();
        for (Token token: infixList) {
            if (precedence.containsKey(token.getValue())) {
                while (!stack.empty() && precedence.get(token.getValue()) <= precedence.get(stack.peek().getValue())) {
                    result.add(stack.pop());
                }
                stack.push(token);
            } else {
                result.add(token);
            }
        }
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private List<Token> buildSanitizedInfixList(InfixExpression infixExpression) {
        List<Token> result = infixExpression.getExpression().collect(Collectors.toList());
        while (!result.isEmpty() && !result.get(result.size() - 1).isNumber()) {
            result.remove(result.get(result.size() - 1));
        }
        return result;
    }

    private boolean notReady(List<Token> infixList) {
        return infixList.size() < 3;
    }
}
