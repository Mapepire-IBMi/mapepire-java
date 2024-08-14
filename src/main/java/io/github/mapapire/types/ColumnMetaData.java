package io.github.mapapire.types;

public class ColumnMetaData {
    private int display_size;
    private String label;
    private String name;
    private String type;

    public ColumnMetaData() {

    }

    public ColumnMetaData(int display_size, String label, String name, String type) {
        this.display_size = display_size;
        this.label = label;
        this.name = name;
        this.type = type;
    }

    public int getDisplaySize() {
        return display_size;
    }

    public void setDisplaySize(int display_size) {
        this.display_size = display_size;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
