package com.example.HuceBack.dto.common;

import lombok.Data;

import java.io.Serializable;
@Data
public class BaseResponse<T> implements Serializable {
    private Integer status;
    private MessageResponse error;
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(Integer status, String error, T data) {
        this.status = status;
        this.error = new MessageResponse(error);
        this.data = data;
    }

    public BaseResponse(String error) {
        this.status = Const.STATUS_RESPONSE.ERROR;
        this.error = new MessageResponse(error);
    }
    public BaseResponse(T data) {
        this.status = Const.STATUS_RESPONSE.SUCCESS;
        this.data = data;
    }

}
