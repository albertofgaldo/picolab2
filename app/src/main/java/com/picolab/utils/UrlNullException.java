package com.picolab.utils;

public class UrlNullException extends Exception {

    public UrlNullException(){
        super("La URL no está disponible");
    }

    public UrlNullException(String msg){
        super(msg);
    }

}
