package com.lsfg.model;

/**
 * 返回状态
 * Created by lpeng on 2018/03/31.
 */
public class Status {

    /**
     * 状态编码
     */
    private int code;
    /**
     * 内容
     */
    private String text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
