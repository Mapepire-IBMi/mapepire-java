package io.github.mapapire.types;

public class JobLogEntry {
    private final String MESSAGE_ID;
    private final String SEVERITY;
    private final String MESSAGE_TIMESTAMP;
    private final String FROM_LIBRARY;
    private final String FROM_PROGRAM;
    private final String MESSAGE_TYPE;
    private final String MESSAGE_TEXT;
    private final String MESSAGE_SECOND_LEVEL_TEXT;

    public JobLogEntry(String _MESSAGE_ID, String _SEVERITY, String _MESSAGE_TIMESTAMP,
            String _FROM_LIBRARY, String _FROM_PROGRAM, String _MESSAGE_TYPE,
            String _MESSAGE_TEXT, String _MESSAGE_SECOND_LEVEL_TEXT) {
        this.MESSAGE_ID = _MESSAGE_ID;
        this.SEVERITY = _SEVERITY;
        this.MESSAGE_TIMESTAMP = _MESSAGE_TIMESTAMP;
        this.FROM_LIBRARY = _FROM_LIBRARY;
        this.FROM_PROGRAM = _FROM_PROGRAM;
        this.MESSAGE_TYPE = _MESSAGE_TYPE;
        this.MESSAGE_TEXT = _MESSAGE_TEXT;
        this.MESSAGE_SECOND_LEVEL_TEXT = _MESSAGE_SECOND_LEVEL_TEXT;
    }
}
