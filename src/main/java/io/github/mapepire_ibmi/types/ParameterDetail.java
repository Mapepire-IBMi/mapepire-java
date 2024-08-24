package io.github.mapepire_ibmi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the parameter details for a query.
 */
public class ParameterDetail {
    /**
     * The parameter name.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The parameter type.
     */
    @JsonProperty("type")
    private String type;

    /**
     * The parameter mode.
     */
    @JsonProperty("mode")
    private ParameterMode mode;

    /**
     * The parameter precision.
     */
    @JsonProperty("precision")
    private int precision;

    /**
     * The parameter scale.
     */
    @JsonProperty("scale")
    private int scale;

    /**
     * Construct a new ParameterDetail instance.
     * 
     */
    public ParameterDetail() {

    }

    /**
     * Construct a new ParameterDetail instance.
     * 
     * @param name      The parameter name.
     * @param type      The parameter type.
     * @param mode      The parameter mode.
     * @param precision The parameter precision.
     * @param scale     The parameter scale.
     */
    public ParameterDetail(String name, String type, ParameterMode mode, int precision, int scale) {
        this.name = name;
        this.type = type;
        this.mode = mode;
        this.precision = precision;
        this.scale = scale;
    }

    /**
     * Get the parameter name.
     * 
     * @return The parameter name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the parameter name.
     * 
     * @param name The parameter name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the parameter type.
     * 
     * @return The parameter type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the parameter type.
     * 
     * @param type The parameter type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the parameter mode.
     * 
     * @return The parameter mode.
     */
    public ParameterMode getMode() {
        return mode;
    }

    /**
     * Set the parameter mode.
     * 
     * @param mode The parameter mode.
     */
    public void setMode(ParameterMode mode) {
        this.mode = mode;
    }

    /**
     * Get the parameter precision.
     * 
     * @return The parameter precision.
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * Set the parameter precision.
     * 
     * @param precision The parameter precision.
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * Get the parameter scale.
     * 
     * @return The parameter scale.
     */
    public int getScale() {
        return scale;
    }

    /**
     * Set the parameter scale.
     * 
     * @param scale The parameter scale.
     */
    public void setScale(int scale) {
        this.scale = scale;
    }
}
