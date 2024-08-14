package io.github.mapapire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobLogEntry {
    @JsonProperty("MESSAGE_ID")
    private String messageId;

    @JsonProperty("SEVERITY")
    private String severity;

    @JsonProperty("MESSAGE_TIMESTAMP")
    private String messageTimestamp;

    @JsonProperty("FROM_LIBRARY")
    private String fromLibrary;

    @JsonProperty("FROM_PROGRAM")
    private String fromProgram;

    @JsonProperty("MESSAGE_TYPE")
    private String messageType;

    @JsonProperty("MESSAGE_TEXT")
    private String messageText;

    @JsonProperty("MESSAGE_SECOND_LEVEL_TEXT")
    private String messageSecondLevelText;

    public JobLogEntry() {

    }

    public JobLogEntry(String messageId, String severity, String messageTimestamp,
            String fromLibrary, String fromProgram, String messageType,
            String messageText, String messageSecondLevelText) {
        this.messageId = messageId;
        this.severity = severity;
        this.messageTimestamp = messageTimestamp;
        this.fromLibrary = fromLibrary;
        this.fromProgram = fromProgram;
        this.messageType = messageType;
        this.messageText = messageText;
        this.messageSecondLevelText = messageSecondLevelText;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(String messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public String getFromLibrary() {
        return fromLibrary;
    }

    public void setFromLibrary(String fromLibrary) {
        this.fromLibrary = fromLibrary;
    }

    public String getFromProgram() {
        return fromProgram;
    }

    public void setFromProgram(String fromProgram) {
        this.fromProgram = fromProgram;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageSecondLevelText() {
        return messageSecondLevelText;
    }

    public void setMessageSecondLevelText(String messageSecondLevelText) {
        this.messageSecondLevelText = messageSecondLevelText;
    }
}
