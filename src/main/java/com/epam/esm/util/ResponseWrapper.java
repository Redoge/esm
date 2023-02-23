package com.epam.esm.util;

public class ResponseWrapper {
    private int code;
    private String message;
    private int serverCode;

    public ResponseWrapper() {
    }

    public ResponseWrapper(int code, String message, int serverCode) {
        this.code = code;
        this.message = message;
        this.serverCode = serverCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getServerCode() {
        return serverCode;
    }

    public void setServerCode(int errorCode) {
        this.serverCode = errorCode;
    }

    @Override
    public String toString() {
        return "ErrorWrapper{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", errorCode=" + serverCode +
                '}';
    }
}
