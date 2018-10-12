package com.lsfg.model;

public class Encrypt {

    public String Encrypt(String ch, int startLen, int endLen) {
        int len = ch.length() - startLen - endLen;
        String str = "";

        for (int i = 0; i < len; i++) {
            str += "*";
        }

        return ch.substring(0, startLen) + str + ch.substring(ch.length() - endLen);
    }
}
