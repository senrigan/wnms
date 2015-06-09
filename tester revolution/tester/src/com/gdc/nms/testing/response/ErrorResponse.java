package com.gdc.nms.testing.response;


public class ErrorResponse implements Response {

    private String message;

    public ErrorResponse(String message) {
        if (message == null) {
            throw new NullPointerException();
        }
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
