package com.okay.testcenter.exception.myException;

public class TypeCastException extends RuntimeException {
    public TypeCastException() {

    }

    public TypeCastException(String str) {
        super(str);
    }
}
