package com.okay.testcenter.exception.myException;

public class OpenUrlException extends RuntimeException  {
    public OpenUrlException() {

    }

    public OpenUrlException(String str) {
        super(str);
    }
}
