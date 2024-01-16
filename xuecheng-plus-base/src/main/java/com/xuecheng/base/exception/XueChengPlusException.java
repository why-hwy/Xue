package com.xuecheng.base.exception;

public class XueChengPlusException extends RuntimeException {

    private String errMessage;

    public XueChengPlusException() {
    }

    public XueChengPlusException(String message, String errMessage) {
        super(message);
        this.errMessage = errMessage;
    }

    public static void cast(String errMessage) {
        throw new RuntimeException(errMessage);
    }
}