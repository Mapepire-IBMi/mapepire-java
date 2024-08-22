package io.github.mapepire_ibmi.types.exceptions;

/**
 * Represents an exception from the Mapepire client.
 */
public class ClientException extends Exception {
    /**
     * Construct a new ClientException instance from a message.
     * 
     * @param message The exception message.
     */
    public ClientException(String message) {
        super(message);
    }

    /**
     * Construct a new ClientException instance from a throwable cause.
     * 
     * @param cause The exception cause.
     */
    public ClientException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct a new ClientException instance from a message and throwable
     * cause.
     * 
     * @param message The exception message.
     * @param cause   The exception cause.
     */
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
