package io.github.mapapire.types;

public enum JobStatus {
    NotStarted("notStarted"),
    Connecting("connecting"),
    Ready("ready"),
    Busy("busy"),
    Ended("ended");

    private String jobStatus;

    JobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }
}