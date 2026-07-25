package maze.frequency.block;

public enum GateMode {
    AND, OR, NOT, XAND, XOR, IMPL;

    private static final GateMode[] VALUES = values();

    public static GateMode byOrdinal(int ordinal) {
        if (ordinal < 0 || ordinal >= VALUES.length) return AND;
        return VALUES[ordinal];
    }

    public boolean evaluate(boolean input1, boolean input2) {
        return switch (this) {
            case AND -> input1 && input2;
            case OR -> input1 || input2;
            case NOT -> !input1;
            case XAND -> input1 == input2;
            case XOR -> input1 != input2;
            case IMPL -> !input1 || input2;
        };
    }
}
