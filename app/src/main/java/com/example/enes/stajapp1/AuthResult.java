package com.example.enes.stajapp1;

/**
 * Created by enes on 5.07.2017.
 */

public class AuthResult {
    public static int getErrorCode;

    public AuthResult(int result)
    {
        this.getErrorCode = result;
    }

    public void setResult(int result){ this.getErrorCode = result; }

    public int getResult() { return getErrorCode; }


}
