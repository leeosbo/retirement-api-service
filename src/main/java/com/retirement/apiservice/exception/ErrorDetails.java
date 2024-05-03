package com.retirement.apiservice.exception;

import java.time.LocalDateTime;

public class ErrorDetails {
    private LocalDateTime timestamp;
    private String name;
    private String message;
    private String details;

    public ErrorDetails(LocalDateTime timestamp, String name, String message, String details) {
        this.timestamp = timestamp;
        this.name = name;
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

}
