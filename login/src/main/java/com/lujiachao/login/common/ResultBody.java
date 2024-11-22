package com.lujiachao.login.common;

import lombok.Data;

@Data
public class ResultBody {
    private int code;
    private String msg;
    private Object data;

    public ResultBody(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultBody(int code, String msg) {
        this.code = code;
        this.msg = msg;

    }

    public ResultBody(Object data) {
        this.code = 200;
        this.data = data;
    }
}
