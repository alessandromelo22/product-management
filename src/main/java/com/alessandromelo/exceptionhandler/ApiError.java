package com.alessandromelo.exceptionhandler;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {

    private Integer status;
    private String message;
    private String path;
    private LocalDateTime timestamp;

    public ApiError(HttpStatus status, String message, String path) {
        this.status = status.value();
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
