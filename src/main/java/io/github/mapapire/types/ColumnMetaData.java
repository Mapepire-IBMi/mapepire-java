package io.github.mapapire.types;

public class ColumnMetaData {
    private final int display_size;
    private final String label;
    private final String name;
    private final String type;

    public ColumnMetaData(int _display_size, String _label, String _name, String _type) {
        this.display_size = _display_size;
        this.label = _label;
        this.name = _name;
        this.type = _type;
    }
}