package io.github.mapepire_ibmi.types.exceptions;

/**
 * Represents an unknown exception from the Mapepire server.
 */
public class UnknownServerException extends Exception {
    /**
     * Construct a new UnknownServerException instance from a message.
     * 
     * @param message The exception message.
     */
    public UnknownServerException(String message) {
        super(message);
    }

    /**
     * Construct a new UnknownServerException instance from a throwable cause.
     * 
     * @param cause The exception cause.
     */
    public UnknownServerException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct a new UnknownServerException instance from a message and throwable
     * cause.
     * 
     * @param message The exception message.
     * @param cause   The exception cause.
     */
    public UnknownServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
