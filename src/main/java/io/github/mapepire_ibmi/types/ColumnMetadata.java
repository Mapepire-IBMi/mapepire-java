package io.github.mapepire_ibmi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ColumnMetadata {
    @JsonProperty("display_size")
    private int displaySize;

    @JsonProperty("label")
    private String label;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    public ColumnMetadata() {

    }

    public ColumnMetadata(int displaySize, String label, String name, String type) {
        this.displaySize = displaySize;
        this.label = label;
        this.name = name;
        this.type = type;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
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
