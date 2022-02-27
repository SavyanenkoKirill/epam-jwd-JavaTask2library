package com.epam.jwd.evaluator;

import com.epam.jwd.exception.InvalidExpressionException;
import com.epam.jwd.parser.ExpressionParser;
import com.epam.jwd.validator.ExpressionValidator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExpressionEvaluator {
    private static final Pattern IS_NUMERIC = Pattern.compile("\\d+");
    private static final Map<String, Integer> priorities = new HashMap<>();

    static {
        priorities.put("+", 1);
        priorities.put("-", 1);
        priorities.put("*", 2);
        priorities.put("/", 2);
    }

    private ExpressionEvaluator() {
        throw new AssertionError();
    }

    public static double evaluateExpression(String expr) throws InvalidExpressionException {
        if (!ExpressionValidator.validateExpression(expr)) {
            throw new InvalidExpressionException("Expression \"" + expr + "\" is invalid.");
        }
        String[] tokens = ExpressionParser.parseExpression(expr);
        List<String> postfixNotation = constructPostfixNotation(tokens);
        return calculatePostfixNotation(postfixNotation);
    }

    private static List<String> constructPostfixNotation(String[] tokens) {
        List<String> postfixNotation = new ArrayList<>();
        Stack<String> operationsStack = new Stack<>();
        for (String token : tokens) {
            Matcher numMatcher = IS_NUMERIC.matcher(token);
            if (numMatcher.matches()) {
                postfixNotation.add(token);
            } else if (token.equals("(")) {
                operationsStack.push("(");
            } else if (token.equals(")")) {
                while (!operationsStack.peek().equals("(")) {
                    postfixNotation.add(operationsStack.pop());
                }
                operationsStack.pop();
            } else if (priorities.containsKey(token)) {
                while (!operationsStack.empty() && priorities.containsKey(operationsStack.peek())
                        && priorities.get(operationsStack.peek()) >= priorities.get(token)) {
                    postfixNotation.add(operationsStack.pop());
                }
                operationsStack.push(token);
            }
        }
        while (!operationsStack.empty()) {
            postfixNotation.add(operationsStack.pop());
        }
        return postfixNotation;
    }

    private static double calculatePostfixNotation(List<String> postfixNotation) throws InvalidExpressionException {
        Stack<Double> operandsStack = new Stack<>();
        for (String token : postfixNotation) {
            if (priorities.containsKey(token)) {
                double secondOperand = operandsStack.pop();
                double firstOperand = operandsStack.pop();
                switch (token) {
                    case "+" -> operandsStack.push(firstOperand + secondOperand);
                    case "-" -> operandsStack.push(firstOperand - secondOperand);
                    case "*" -> operandsStack.push(firstOperand * secondOperand);
                    case "/" -> {
                        if (secondOperand == 0) {
                            throw new InvalidExpressionException("Invalid expression: zero division is found.");
                        }
                        operandsStack.push(firstOperand / secondOperand);
                    }
                }
            } else {
                operandsStack.push(Double.parseDouble(token));
            }
        }
        return operandsStack.peek();
    }
}
