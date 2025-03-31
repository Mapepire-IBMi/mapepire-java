package io.github.mapepire_ibmi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a single shared instance of an ObjectMapper.
 */
class SingletonObjectMapper {
    /**
     * The single ObjectMapper instance.
     */
    private static ObjectMapper instance;

    /**
     * Get the single ObjectMapper instance.
     * 
     * @return The single ObjectMapper instance.
     */
    public static ObjectMapper getInstance() {
        if (instance == null) {
            instance = new ObjectMapper();
            instance.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }

        return instance;
    }
}
