package com.nndi_tech.oss.gensett;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class SetterWriter {
    private final SetterType setterType;
    private final String expression;
    private static final Pattern SET_EXPR_PATTERN = Pattern.compile("/\\/\\/\\s?set[f|b]?:/");
    private static final Pattern NAIVE_FIELD_EXPR = Pattern.compile("[A-Za-z]+\\d*[A-Za-z]!?");
    private static final Pattern NAIVE_CLASS_NAME = Pattern.compile("[A-Za-z]+\\d*[A-Za-z]");

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
        if (! NAIVE_CLASS_NAME.matcher(obj).matches()) {
            throw new IllegalArgumentException("Invalid name for the object instance: " + obj);
        }

        // TODO: Check if the type of obj is a class we recognize
        String fieldStr = parts[1];
        String[] fields = fieldStr.split(",");

        for (String fieldName: fields) {
            sb.append(getTemplate(obj, fieldName)).append("\n");
        }
    }

    private String getTemplate(String obj, String rawFieldExpression) {
        String fieldName = rawFieldExpression;
        String wrapWithMethod = "";
        boolean hasRequireNonNull = rawFieldExpression.endsWith("!");
        boolean hasWrapperMethod = rawFieldExpression.contains("*");

        if (hasRequireNonNull && hasWrapperMethod) {
            throw new IllegalArgumentException(String.format("Cannot combine * and ! on field='%s'", fieldName));
        }

        if (hasRequireNonNull) {
            fieldName = fieldName.substring(0, fieldName.length() - 1);
        }

        if (hasWrapperMethod) {
            String[] parts = fieldName.split("\\*", 2);
            fieldName = parts[0];
            wrapWithMethod = parts[1];

            validateMethodName(wrapWithMethod, fieldName);
        }

        validateFieldName(fieldName, obj);

        if (hasRequireNonNull && this.setterType == SetterType.METHOD) {
            return String.format(
                "%s.%s(Objects.requireNonNull(/* TODO: set it! */, \"%s\"));",
                obj,
                beanSetterName(fieldName),
                fieldName
            );
        } else if (hasWrapperMethod && this.setterType == SetterType.METHOD) {
            return String.format(
                "%s.%s(%s(/* TODO: set it! */));",
                obj,
                beanSetterName(fieldName),
                wrapWithMethod
            );
        } else if (hasRequireNonNull && this.setterType == SetterType.FIELD) {
            return String.format(
                "%s.%s = Objects.requireNonNull(/* TODO: set it! */, \"%s\");",
                obj,
                fieldName,
                fieldName
            );
        } else if (hasWrapperMethod && this.setterType == SetterType.FIELD) {
            return String.format(
                "%s.%s = %s(/* TODO: set it! */);",
                obj,
                fieldName,
                wrapWithMethod
            );
        } else if (this.setterType == SetterType.FIELD) {
            return String.format(
                "%s.%s = /* TODO: set it! */;",
                obj,
                fieldName
            );
        } else {
            return String.format(
                "%s.%s(/* TODO: set it! */);",
                obj,
                beanSetterName(fieldName)
            );
        }
    }

    private void validateMethodName(String wrapWithMethod, String fieldName) {
        if (wrapWithMethod.isEmpty()) {
            throw new IllegalArgumentException(String.format("Invalid method on field='%s'", fieldName));
        }

    }

    private void validateFieldName(String fieldName, String obj) {
        if (fieldName.isEmpty()) {
            throw new IllegalArgumentException(String.format("Field name cannot be empty"));
        }
        if (! NAIVE_FIELD_EXPR.matcher(fieldName).matches()) {
            throw new IllegalArgumentException(String.format(
                    "Invalid name for the field: '%s' on %s", fieldName, obj));
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

    /**
     * From https://sourcegraph.com/github.com/rzwitserloot/lombok/-/blob/src/core/lombok/core/handlers/HandlerUtil.java#L657
     *
     * @param prefix Something like {@code get} or {@code set} or {@code is}.
     * @param suffix Something like {@code running}.
     * @return prefix + smartly title-cased suffix. For example, {@code setRunning}.
     */
    public static String buildAccessorName(String prefix, String suffix) {
        if (suffix.length() == 0) return prefix;
        if (prefix.length() == 0) return suffix;

        char first = suffix.charAt(0);
        if (Character.isLowerCase(first)) {
            boolean useUpperCase = suffix.length() > 2 &&
                    (Character.isTitleCase(suffix.charAt(1)) || Character.isUpperCase(suffix.charAt(1)));
            suffix = String.format("%s%s",
                    useUpperCase ? Character.toUpperCase(first) : Character.toTitleCase(first),
                    suffix.subSequence(1, suffix.length()));
        }
        return String.format("%s%s", prefix, suffix);
    }

    /**
     * Returns the field name for a "setter" for a POJO, i.e. the part after the "set" text.
     * <br/>
     * Example: {@code beanSetterName("name") // "setName"}
     *
     * @param fieldName
     * @return
     */
    static String beanSetterName(String fieldName) {
        return buildAccessorName("set", fieldName);
    }
}
