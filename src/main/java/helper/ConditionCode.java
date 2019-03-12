package helper;

import org.jetbrains.annotations.NotNull;

/**
 * These represent a relational comparison
 * (using signed comparisons)
 */
public enum ConditionCode {
    GREATER_EQUAL(">=", "ge"), GREATER(">", "g"),
    LESS_EQUAL("<=", "le"), LESS("<", "l"),
    EQUAL("==", "e"), NOT_EQUAL("!=", "ne");

    @NotNull private final String output;
    @NotNull private final String x64Code;

    ConditionCode(@NotNull String output, @NotNull String x64Code) {
        this.output = output;
        this.x64Code = x64Code;
    }

    @Override
    public String toString() {
        return output;
    }

    /** Returns the x64 condition code suffix for the set, conditional move, or jump instructions. */
    public String x64Code() {
        return x64Code;
    }
}
