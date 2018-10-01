package com.picolab.utils;

import org.json.JSONException;

public class JSonNullException extends JSONException {

    public JSonNullException(){
        super("No hay JSON");
    }

    public JSonNullException(String msg){
        super(msg);
    }

}
