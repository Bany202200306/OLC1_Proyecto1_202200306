package analyzer;

public class PropertyEntry {

    private final String name;
    private final Object value;
    private final int line;
    private final int column;

    public PropertyEntry(
            String name,
            Object value,
            int line,
            int column
    ) {

        this.name = name;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}