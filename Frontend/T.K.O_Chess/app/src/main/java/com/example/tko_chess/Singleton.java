package com.example.tko_chess;

public class Singleton {

    private static Singleton single_instance = null;
    public String s;

    private Singleton(){

    }

    public static Singleton getInstance(String s){

        if(single_instance == null)
            single_instance = new Singleton();

        return single_instance;
    }
}
