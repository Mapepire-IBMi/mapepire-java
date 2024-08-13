package io.github.mapapire.types;

public enum JobStatus {
    NotStarted("notStarted"),
    Connecting("connecting"),
    Ready("ready"),
    Busy("busy"),
    Ended("ended");

    private String value;

    JobStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static JobStatus fromValue(String value) {
        for (JobStatus type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}