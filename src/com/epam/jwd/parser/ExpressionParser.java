package com.epam.jwd.parser;

public final class ExpressionParser {
    private static final String NUMS_AND_OPERATIONS = "(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)|(?<=\\D)(?=\\D)";

    private ExpressionParser() {
        throw new AssertionError();
    }

    public static String[] parseExpression(String expr) {
        return expr.split(NUMS_AND_OPERATIONS);
    }
}
