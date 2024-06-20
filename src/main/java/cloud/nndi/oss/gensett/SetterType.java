package cloud.nndi.oss.gensett;

public enum SetterType {
    METHOD("Set Method", "setter", "set"),
    FIELD("Set Field", "setf", "setfield"),
    CHAINED_FIELD("Set Field Chained", "setc", "chained");
    // TODO: Add support for BUILDER;

    private final String name;
    private final String[] expressions;

    SetterType(String name, String... expressions) {
        this.name = name;
        this.expressions = expressions;
    }
}
