package io.github.mapepire_ibmi;

public class UnknownClientException extends Exception {
    public UnknownClientException(String message) {
        super(message);
    }

    public UnknownClientException(Throwable cause) {
        super(cause);
    }

    public UnknownClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
