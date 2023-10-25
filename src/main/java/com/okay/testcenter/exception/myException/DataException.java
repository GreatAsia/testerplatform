package com.okay.testcenter.exception.myException;

/**
 * 数据异常
 */
public class DataException extends RuntimeException {
    public DataException() {

    }

    public DataException(String str) {
        super(str);
    }

}
