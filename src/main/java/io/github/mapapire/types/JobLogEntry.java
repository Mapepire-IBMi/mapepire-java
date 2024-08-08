package io.github.mapapire.types;

public class JobLogEntry {
    private String MESSAGE_ID;
    private String SEVERITY;
    private String MESSAGE_TIMESTAMP;
    private String FROM_LIBRARY;
    private String FROM_PROGRAM;
    private String MESSAGE_TYPE;
    private String MESSAGE_TEXT;
    private String MESSAGE_SECOND_LEVEL_TEXT;

    public JobLogEntry() {

    }

    public JobLogEntry(String MESSAGE_ID, String SEVERITY, String MESSAGE_TIMESTAMP,
            String FROM_LIBRARY, String FROM_PROGRAM, String MESSAGE_TYPE,
            String MESSAGE_TEXT, String MESSAGE_SECOND_LEVEL_TEXT) {
        this.MESSAGE_ID = MESSAGE_ID;
        this.SEVERITY = SEVERITY;
        this.MESSAGE_TIMESTAMP = MESSAGE_TIMESTAMP;
        this.FROM_LIBRARY = FROM_LIBRARY;
        this.FROM_PROGRAM = FROM_PROGRAM;
        this.MESSAGE_TYPE = MESSAGE_TYPE;
        this.MESSAGE_TEXT = MESSAGE_TEXT;
        this.MESSAGE_SECOND_LEVEL_TEXT = MESSAGE_SECOND_LEVEL_TEXT;
    }

    public String getMessageId() {
        return MESSAGE_ID;
    }

    public void setMessageId(String MESSAGE_ID) {
        this.MESSAGE_ID = MESSAGE_ID;
    }

    public String getSeverity() {
        return SEVERITY;
    }

    public void setSeverity(String SEVERITY) {
        this.SEVERITY = SEVERITY;
    }

    public String getMessageTimestamp() {
        return MESSAGE_TIMESTAMP;
    }

    public void setMessageTimestamp(String MESSAGE_TIMESTAMP) {
        this.MESSAGE_TIMESTAMP = MESSAGE_TIMESTAMP;
    }

    public String getFromLibrary() {
        return FROM_LIBRARY;
    }

    public void setFromLibrary(String FROM_LIBRARY) {
        this.FROM_LIBRARY = FROM_LIBRARY;
    }

    public String getFromProgram() {
        return FROM_PROGRAM;
    }

    public void setFromProgram(String FROM_PROGRAM) {
        this.FROM_PROGRAM = FROM_PROGRAM;
    }

    public String getMessageType() {
        return MESSAGE_TYPE;
    }

    public void setMessageType(String MESSAGE_TYPE) {
        this.MESSAGE_TYPE = MESSAGE_TYPE;
    }

    public String getMessageText() {
        return MESSAGE_TEXT;
    }

    public void setMessageText(String MESSAGE_TEXT) {
        this.MESSAGE_TEXT = MESSAGE_TEXT;
    }

    public String getMessageSecondLevelText() {
        return MESSAGE_SECOND_LEVEL_TEXT;
    }

    public void setMessageSecondLevelText(String MESSAGE_SECOND_LEVEL_TEXT) {
        this.MESSAGE_SECOND_LEVEL_TEXT = MESSAGE_SECOND_LEVEL_TEXT;
    }
}
