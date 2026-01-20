package io.github.mapepire_ibmi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the metadata for a single column in a query.
 */
public class ColumnMetadata {
    /**
     * The display size of the column.
     */
    @JsonProperty("display_size")
    private int displaySize;

    /**
     * The label of the column.
     */
    @JsonProperty("label")
    private String label;

    /**
     * The name of the column.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The type of the column.
     */
    @JsonProperty("type")
    private String type;

    /**
     * The precision/length of the column.
     */
    @JsonProperty("precision")
    private int precision;

    /**
     * The scale of the column.
     */
    @JsonProperty("scale")
    private int scale;

    /**
     * Indicates whether the column is automatically numbered.
     */
    @JsonProperty("autoIncrement")
    private boolean autoIncrement;

    /**
     * Indicates the nullability of values in the column.
     */
    @JsonProperty("nullable")
    private int nullable;

    /**
     * Indicates whether the column is definitely not writable.
     */
    @JsonProperty("readOnly")
    private boolean readOnly;

    /**
     * Indicates whether it is possible for a write on the column to succeed.
     */
    @JsonProperty("writeable")
    private boolean writeable;

    /**
     * The column's table name.
     */
    @JsonProperty("table")
    private String table;
    
    /**
     * Construct a new ColumnMetadata instance.
     */
    public ColumnMetadata() {

    }

    /**
     * Construct a new ColumnMetadata instance.
     *
     * @param displaySize   The display size of the column.
     * @param label         The label of the column.
     * @param name          The name of the column.
     * @param type          The type of the column.
     * @param precision     The precision/length of the column.
     * @param scale         The scale of the column.
     * @param autoIncrement Indicates whether the column is automatically numbered.
     * @param nullable      Indicates the nullability of values in the column.
     * @param readOnly      Indicates whether the column is definitely not writable.
     * @param writeable     Indicates whether it is possible for a write on the
     *                      column to succeed.
     */
    public ColumnMetadata(int displaySize, String label, String name, String type, int precision, int scale,
            boolean autoIncrement, int nullable, boolean readOnly, boolean writeable) {
        this.displaySize = displaySize;
        this.label = label;
        this.name = name;
        this.type = type;
        this.precision = precision;
        this.scale = scale;
        this.autoIncrement = autoIncrement;
        this.nullable = nullable;
        this.readOnly = readOnly;
        this.writeable = writeable;
    }

    /**
     * Get the display size of the column.
     *
     * @return The display size of the column.
     */
    public int getDisplaySize() {
        return displaySize;
    }

    /**
     * Set the display size of the column.
     *
     * @param displaySize The display size of the column.
     */
    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }

    /**
     * Get the label of the column.
     *
     * @return The label of the column.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label of the column.
     *
     * @param label The label of the column.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get the name of the column.
     *
     * @return The name of the column.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the column.
     *
     * @param name The name of the column.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the type of the column.
     *
     * @return The type of the column.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the column.
     *
     * @param type The type of the column.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the precision/length of the column.
     *
     * @return The precision/length of the column.
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * Set the precision/length of the column.
     *
     * @param precision The precision/length of the column.
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * Get the scale of the column.
     *
     * @return The scale of the column.
     */
    public int getScale() {
        return scale;
    }

    /**
     * Set the scale of the column.
     *
     * @param scale The scale of the column.
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * Get whether the column is automatically numbered.
     *
     * @return Whether the column is automatically numbered.
     */
    public boolean getAutoIncrement() {
        return autoIncrement;
    }

    /**
     * Set whether the column is automatically numbered.
     *
     * @param autoIncrement Whether the column is automatically numbered.
     */
    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    /**
     * Get the nullability of values in the column.
     *
     * @return The nullability of values in the column.
     */
    public int getNullable() {
        return nullable;
    }

    /**
     * Set the nullability of values in the column.
     *
     * @param nullable The nullability of values in the column.
     */
    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    /**
     * Get whether the column is definitely not writable.
     *
     * @return Whether the column is definitely not writable.
     */
    public boolean getReadOnly() {
        return readOnly;
    }

    /**
     * Set whether the column is definitely not writable.
     *
     * @param readOnly Whether the column is definitely not writable.
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Get whether it is possible for a write on the column to succeed.
     *
     * @return Whether it is possible for a write on the column to succeed.
     */
    public boolean getWriteable() {
        return writeable;
    }

    /**
     * Set whether it is possible for a write on the column to succeed.
     *
     * @param writeable Whether it is possible for a write on the column to succeed.
     */
    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    /**
     * Get the column's table name.
     *
     * @return The column's table name.
     */
    public String getTable() {
        return table;
    }

    /**
     * Set the column's table name.
     *
     * @param table The column's table name.
     */
    public void setTable(String table) {
        this.table = table;
    }
}

