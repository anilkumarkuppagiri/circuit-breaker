package com.test.anil;

public class Response {

    private int status;
    private String responseText;

    public Response(int status, String responseText) {
        this.status = status;
        this.responseText = responseText;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}
