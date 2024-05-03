package com.retirement.apiservice.exception;

import java.time.LocalDateTime;

public record ValidationErrorMessage(LocalDateTime timestamp, String name, String message, String details) {

}
