package io.github.mapepire_ibmi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a message generated during CL command execution.
 */
public class CLMessage {
    /**
     * The unique message identifier.
     */
    @JsonProperty("id")
    private String id;

    /**
     * The message type.
     */
    @JsonProperty("type")
    private String type;

    /**
     * The severity level of the message.
     */
    @JsonProperty("severity")
    private int severity;

    /**
     * The main text of the message.
     */
    @JsonProperty("text")
    private String text;

    /**
     * The help text of the message, if available.
     */
    @JsonProperty("help")
    private String help;

    /**
     * The Unix timestamp when the message was generated.
     */
    @JsonProperty("time")
    private long time;

    /**
     * The library from which the message originated.
     */
    @JsonProperty("messageLibrary")
    private String messageLibrary;

    /**
     * The message file from which the message originated.
     */
    @JsonProperty("messageFile")
    private String messageFile;

    /**
     * The program from which the message originated.
     */
    @JsonProperty("sendingProgram")
    private String sendingProgram;

    /**
     * The instruction in the program from which the message originated.
     */
    @JsonProperty("sendingInstruction")
    private String sendingInstruction;

    /**
     * The program to which the message was sent.
     */
    @JsonProperty("receivingProgram")
    private String receivingProgram;

    /**
     * The instruction in the program to which the message was sent.
     */
    @JsonProperty("receivingInstruction")
    private String receivingInstruction;

    /**
     * Construct a new CLMessage instance.
     */
    public CLMessage() {

    }

    /**
     * Construct a new CLMessage instance.
     * 
     * @param id                   The unique message identifier.
     * @param type                 The message type.
     * @param severity             The severity level of the message.
     * @param text                 The main text of the message.
     * @param help                 The help text of the message, if available.
     * @param time                 The Unix timestamp when the message was
     *                             generated.
     * @param messageLibrary       The library from which the message originated.
     * @param messageFile          The message file from which the message
     *                             originated.
     * @param sendingProgram       The program from which the message originated.
     * @param sendingInstruction   The instruction in the program from which the
     *                             message originated.
     * @param receivingProgram     The program to which the message was sent.
     * @param receivingInstruction The instruction in the program to which the
     *                             message was sent.
     */
    public CLMessage(String id, String type, int severity, String text, String help, long time,
            String messageLibrary, String messageFile, String sendingProgram, String sendingInstruction,
            String receivingProgram, String receivingInstruction) {
        this.id = id;
        this.type = type;
        this.severity = severity;
        this.text = text;
        this.help = help;
        this.time = time;
        this.messageLibrary = messageLibrary;
        this.messageFile = messageFile;
        this.sendingProgram = sendingProgram;
        this.sendingInstruction = sendingInstruction;
        this.receivingProgram = receivingProgram;
        this.receivingInstruction = receivingInstruction;
    }

    /**
     * Get the unique message identifier.
     * 
     * @return The unique message identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the unique message identifier.
     * 
     * @param id The unique message identifier.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the message type.
     * 
     * @return The message type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the message type.
     * 
     * @param type The message type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the severity level of the message.
     * 
     * @return The severity level of the message.
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * Set the severity level of the message.
     * 
     * @param severity The severity level of the message.
     */
    public void setSeverity(int severity) {
        this.severity = severity;
    }

    /**
     * Get the main text of the message.
     * 
     * @return The main text of the message.
     */
    public String getText() {
        return text;
    }

    /**
     * Set the main text of the message.
     * 
     * @param text The main text of the message.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the help text of the message, if available.
     * 
     * @return The help text of the message, if available.
     */
    public String getHelp() {
        return help;
    }

    /**
     * Set the help text of the message, if available.
     * 
     * @param help The help text of the message, if available.
     */
    public void setHelp(String help) {
        this.help = help;
    }

    /**
     * Get the Unix timestamp when the message was generated.
     * 
     * @return The Unix timestamp when the message was generated.
     */
    public long getTime() {
        return time;
    }

    /**
     * Set the Unix timestamp when the message was generated.
     * 
     * @param time The Unix timestamp when the message was generated.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Get the library from which the message originated.
     * 
     * @return The library from which the message originated.
     */
    public String getMessageLibrary() {
        return messageLibrary;
    }

    /**
     * Set the library from which the message originated.
     * 
     * @param messageLibrary The library from which the message originated.
     */
    public void setMessageLibrary(String messageLibrary) {
        this.messageLibrary = messageLibrary;
    }

    /**
     * Get the message file from which the message originated.
     * 
     * @return The message file from which the message originated.
     */
    public String getMessageFile() {
        return messageFile;
    }

    /**
     * Set the message file from which the message originated.
     * 
     * @param messageFile The message file from which the message originated.
     */
    public void setMessageFile(String messageFile) {
        this.messageFile = messageFile;
    }

    /**
     * Get the program from which the message originated.
     * 
     * @return The program from which the message originated.
     */
    public String getSendingProgram() {
        return sendingProgram;
    }

    /**
     * Set the program from which the message originated.
     * 
     * @param sendingProgram The program from which the message originated.
     */
    public void setSendingProgram(String sendingProgram) {
        this.sendingProgram = sendingProgram;
    }

    /**
     * Get the instruction in the program from which the message originated.
     * 
     * @return The instruction in the program from which the message originated.
     */
    public String getSendingInstruction() {
        return sendingInstruction;
    }

    /**
     * Set the instruction in the program from which the message originated.
     * 
     * @param sendingInstruction The instruction in the program from which the
     *                           message originated.
     */
    public void setSendingInstruction(String sendingInstruction) {
        this.sendingInstruction = sendingInstruction;
    }

    /**
     * Get the program to which the message was sent.
     * 
     * @return The program to which the message was sent.
     */
    public String getReceivingProgram() {
        return receivingProgram;
    }

    /**
     * Set the program to which the message was sent.
     * 
     * @param receivingProgram The program to which the message was sent.
     */
    public void setReceivingProgram(String receivingProgram) {
        this.receivingProgram = receivingProgram;
    }

    /**
     * Get the instruction in the program to which the message was sent.
     * 
     * @return The instruction in the program to which the message was sent.
     */
    public String getReceivingInstruction() {
        return receivingInstruction;
    }

    /**
     * Set the instruction in the program to which the message was sent.
     * 
     * @param receivingInstruction The instruction in the program to which the
     *                             message was sent.
     */
    public void setReceivingInstruction(String receivingInstruction) {
        this.receivingInstruction = receivingInstruction;
    }
}
