package com.example.HuceBack.dto.error;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorDetail { // dto này giúp thông báo ra form của lỗi
    private Date timestamp;
    private String message;
    private String detail;

    public ErrorDetail() {
    }

    public ErrorDetail(Date timestamp, String message, String detail) {
        this.timestamp = timestamp;
        this.message = message;
        this.detail = detail;
    }

}

