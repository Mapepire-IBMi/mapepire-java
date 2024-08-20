package io.github.mapepire.types;

/**
 * Enum representing the possible job statuses.
 */
public enum JobStatus {
    /**
     * The job has not started yet.
     */
    NotStarted("notStarted"),

    /**
     * The job is currently connecting to the server.
     */
    Connecting("connecting"),

    /**
     * The job is ready to process queries.
     */
    Ready("ready"),

    /**
     * The job is currently processing requests.
     */
    Busy("busy"),

    /**
     * The job has ended.
     */
    Ended("ended");

    /**
     * The job status.
     */
    private String value;

    /**
     * Construct a new JobStatus instance.
     * 
     * @param value The job status.
     */
    JobStatus(String value) {
        this.value = value;
    }

    /**
     * Get the job status.
     * 
     * @return The job status.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum job status representation of a string.
     * 
     * @param value The string representation of the job status.
     * @return The enum representation of the job status.
     */
    public static JobStatus fromValue(String value) {
        for (JobStatus type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
