package com.btcc.korea.databroker.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class ResultDto {

    private static final String SUCCESS_MESSAGE = "success";
    private static final String SERVER_ERROR_MESSAGE = "fail";

    private HttpStatus statusCode;
    private String message;
    private Object data;
    private int totalCount;

    public ResultDto success() {
        statusCode = HttpStatus.OK;
        message = SUCCESS_MESSAGE;
        return this;
    }

    public ResultDto fail(){
        statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        message = SERVER_ERROR_MESSAGE;
        return this;
    }

    public ResultDto setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ResultDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResultDto setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    
    public ResultDto setData(Object data) {
    	this.data = data;
    	return this;
    }
    
    public int getStatusCode() {
        return statusCode.value();
    }
    
}
