package com.epam.jwd.validator;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExpressionValidator {
    private static final String IS_VALID_SYNTAX = "^\\(*\\d+\\)*(([-+/*]\\(*\\d+\\)*)?)*$";

    private ExpressionValidator() {
        throw new AssertionError();
    }

    public static boolean validateExpression(String expr) {
        String compactExpr = expr.replaceAll("\\s","");
        return validateSyntax(compactExpr) && validateParenthesisBalance(compactExpr);
    }

    private static boolean validateSyntax(String str) {
        Pattern p = Pattern.compile(IS_VALID_SYNTAX);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private static boolean validateParenthesisBalance(String str) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char currChar = str.charAt(i);
            if (currChar == '(') {
                stack.push('(');
            } else if (currChar == ')') {
                if (stack.empty()) {
                    return false;
                }
                stack.pop();
            }
        }
        return stack.empty();
    }
}
