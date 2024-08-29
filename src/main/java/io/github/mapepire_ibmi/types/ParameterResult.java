package io.github.mapepire_ibmi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the parameter result for a query.
 */
public class ParameterResult {
    /**
     * The parameter result name.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The parameter result type.
     */
    @JsonProperty("type")
    private String type;

    /**
     * The parameter result index.
     */
    @JsonProperty("index")
    private int index;

    /**
     * The parameter result precision.
     */
    @JsonProperty("precision")
    private int precision;

    /**
     * The parameter result scale.
     */
    @JsonProperty("scale")
    private int scale;

    /**
     * The parameter result CCSID.
     */
    @JsonProperty("ccsid")
    private int ccsid;

    /**
     * The parameter result value (only available for OUT/INOUT).
     */
    @JsonProperty("value")
    private Object value;

    /**
     * Construct a new ParameterResult instance.
     */
    public ParameterResult() {

    }

    /**
     * Construct a new ParameterResult instance.
     * 
     * @param name      The parameter result name.
     * @param type      The parameter result type.
     * @param index     The parameter result index.
     * @param precision The parameter result precision.
     * @param scale     The parameter result scale.
     * @param ccsid     The parameter result ccsid.
     * @param value     The parameter result value (only available for OUT/INOUT).
     */
    public ParameterResult(String name, String type, int index, int precision, int scale, int ccsid, Object value) {
        this.name = name;
        this.type = type;
        this.index = index;
        this.precision = precision;
        this.scale = scale;
    }

    /**
     * Get the parameter result name.
     * 
     * @return The parameter result name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the parameter result name.
     * 
     * @param name The parameter result name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the parameter result type.
     * 
     * @return The parameter result type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the parameter result type.
     * 
     * @param type The parameter result type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the parameter result index.
     * 
     * @return The parameter result index.
     */
    public int getindex() {
        return index;
    }

    /**
     * Set the parameter result index.
     * 
     * @param index The parameter result index.
     */
    public void setindex(int index) {
        this.index = index;
    }

    /**
     * Get the parameter result precision.
     * 
     * @return The parameter result precision.
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * Set the parameter result precision.
     * 
     * @param precision The parameter result precision.
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * Get the parameter result scale.
     * 
     * @return The parameter result scale.
     */
    public int getScale() {
        return scale;
    }

    /**
     * Set the parameter result scale.
     * 
     * @param scale The parameter result scale.
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * Get the parameter result ccsid.
     * 
     * @return The parameter result ccsid.
     */
    public int getCcsid() {
        return ccsid;
    }

    /**
     * Set the parameter result ccsid.
     * 
     * @param ccsid The parameter result ccsid.
     */
    public void setCcsid(int ccsid) {
        this.ccsid = ccsid;
    }

    /**
     * Get the parameter result value (only available for OUT/INOUT).
     * 
     * @return The parameter result value (only available for OUT/INOUT).
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the parameter result value (only available for OUT/INOUT).
     * 
     * @param value The parameter result value (only available for OUT/INOUT).
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
