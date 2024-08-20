package io.github.mapepire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a job log entry.
 */
public class JobLogEntry {
    /**
     * The unique message identifier.
     */
    @JsonProperty("MESSAGE_ID")
    private String messageId;

    /**
     * The severity level of the message.
     */
    @JsonProperty("SEVERITY")
    private String severity;

    /**
     * The timestamp when the message was generated.
     */
    @JsonProperty("MESSAGE_TIMESTAMP")
    private String messageTimestamp;

    /**
     * The library from which the message originated.
     */
    @JsonProperty("FROM_LIBRARY")
    private String fromLibrary;

    /**
     * The program from which the message originated.
     */
    @JsonProperty("FROM_PROGRAM")
    private String fromProgram;

    /**
     * The type of message.
     */
    @JsonProperty("MESSAGE_TYPE")
    private String messageType;

    /**
     * The message text.
     */
    @JsonProperty("MESSAGE_TEXT")
    private String messageText;

    /**
     * The second level text of the message, if available.
     */
    @JsonProperty("MESSAGE_SECOND_LEVEL_TEXT")
    private String messageSecondLevelText;

    /**
     * Construct a new JobLogEntry instance.
     */
    public JobLogEntry() {

    }

    /**
     * Construct a new JobLogEntry instance.
     * 
     * @param messageId              The unique message identifier.
     * @param severity               The severity level of the message.
     * @param messageTimestamp       The timestamp when the message was generated.
     * @param fromLibrary            The library from which the message originated.
     * @param fromProgram            The program from which the message originated.
     * @param messageType            The type of message.
     * @param messageText            The message text.
     * @param messageSecondLevelText The second level text of the message, if
     *                               available.
     */
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

    /**
     * Get the unique message identifier.
     * 
     * @return The unique message identifier.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Set the unique message identifier.
     * 
     * @param messageId The unique message identifier.
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Get the severity level of the message.
     * 
     * @return The severity level of the message.
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Set the severity level of the message.
     * 
     * @param severity The severity level of the message.
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * Get the timestamp when the message was generated.
     * 
     * @return The timestamp when the message was generated.
     */
    public String getMessageTimestamp() {
        return messageTimestamp;
    }

    /**
     * Set the timestamp when the message was generated.
     * 
     * @param messageTimestamp The timestamp when the message was generated.
     */
    public void setMessageTimestamp(String messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    /**
     * Get the library from which the message originated.
     * 
     * @return The library from which the message originated.
     */
    public String getFromLibrary() {
        return fromLibrary;
    }

    /**
     * Set the library from which the message originated.
     * 
     * @param fromLibrary The library from which the message originated.
     */
    public void setFromLibrary(String fromLibrary) {
        this.fromLibrary = fromLibrary;
    }

    /**
     * Get the program from which the message originated.
     * 
     * @return The program from which the message originated.
     */
    public String getFromProgram() {
        return fromProgram;
    }

    /**
     * Set the program from which the message originated.
     * 
     * @param fromProgram The program from which the message originated.
     */
    public void setFromProgram(String fromProgram) {
        this.fromProgram = fromProgram;
    }

    /**
     * Get the type of message.
     * 
     * @return The type of message.
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Set the type of message.
     * 
     * @param messageType The type of message.
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * Get the message text.
     * 
     * @return The message text.
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * Set the message text.
     * 
     * @param messageText The message text.
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /**
     * Get the second level text of the message, if available.
     * 
     * @return The second level text of the message, if available.
     */
    public String getMessageSecondLevelText() {
        return messageSecondLevelText;
    }

    /**
     * Set the second level text of the message, if available.
     * 
     * @param messageSecondLevelText The second level text of the message, if
     *                               available.
     */
    public void setMessageSecondLevelText(String messageSecondLevelText) {
        this.messageSecondLevelText = messageSecondLevelText;
    }
}
