package com.example.enes.stajapp1;

/**
 * Created by enes on 5.07.2017.
 */

public class Credantial {

    public static String name;
    public static String pass;


    public Credantial(String name,String pass)
    {
        this.name = name;
        this.pass = pass;

    }

    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }
    public void setPass(String pas)
    {
        this.name = name;
    }
    public String getPass()
    {
        return pass;
    }


}
