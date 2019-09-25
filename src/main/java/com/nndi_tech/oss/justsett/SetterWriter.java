package com.nndi_tech.oss.justsett;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class SetterWriter {
    private final SetterType setterType;
    private final String expression;
    private static final Pattern SET_EXPR_PATTERN = Pattern.compile("/\\/\\/\\s?set[f|b]?:/");

    public SetterWriter(String expression) {
        this.expression = checkAndNormalizeExpression(expression);
        this.setterType = determineSetterType(expression);
    }

    /**
     * A SetterWriter expression is one of the following
     *
     * // set:
     * // setf:
     * // setb:
     *
     * @param expression
     * @return
     */
    public SetterType determineSetterType(String expression) {
       // TODO: if (expression.contains("setb")) return SetterType.BUILDER;

       if(expression.contains("setf"))
           return SetterType.FIELD;

       return SetterType.METHOD;
    }

    private void writeFromExpression(String sourceExpression, StringBuilder sb) throws IOException {
        String expression = sourceExpression.replaceFirst("set[f|b]?:", "");
        String[] parts = expression.split(":");
        if (parts.length != 2) {
            throw new IOException("Invalid expression: " + expression);
        }
        String obj = parts[0];
        // TODO: check if obj is a valid name for an instance obj
        // TODO: Check if the type of obj is a class we recognize
        String fieldStr = parts[1];
        String[] fields = fieldStr.split(",");
        String tmp = null;
        String template = getTemplate();

        for (String fieldName: fields) {
            // TODO: check if fieldName is a valid java field name
            tmp = fieldName;
            if (fieldName.length() > 1 && this.setterType == SetterType.METHOD) {
                tmp = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            }
            sb.append(String.format(template, obj, tmp));
        }
    }

    private String getTemplate() {
        switch (this.setterType) {
            // TODO: case BUILDER: return ".%s(/* TODO: set value here */)\n";
            case FIELD:
                return "%s.%s = /* TODO: set value here */;\n";
            case METHOD:
            default:
                return "%s.set%s(/* TODO: set value here */);\n";
        }
    }

    public String generateCode() throws IOException {
        StringBuilder sb = new StringBuilder();
        this.writeFromExpression(normalizeExpr(this.expression), sb);
        return sb.toString();
    }

    private String checkAndNormalizeExpression(String expr) {
        Objects.requireNonNull(expr, "Expression is null");
        if (expr.isEmpty() && ! SET_EXPR_PATTERN.matcher(expr).matches()) {
            throw new IllegalArgumentException("Invalid expression provided: " + expr);
        }
        return normalizeExpr(expr);
    }

    private static String normalizeExpr(String expr) {
        if (expr == null) return expr;
        return expr.replaceFirst("//\\s+", "")
                .replaceAll("\\s", "");
    }
}
