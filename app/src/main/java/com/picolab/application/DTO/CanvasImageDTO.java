package com.picolab.application.DTO;

public class CanvasImageDTO {
    private int id;
    private String url;

    public CanvasImageDTO(int id, String url){
        this.id=id;
        this.url=url;
    }

    public int getId(){
        return id;
    }

    public String getUrl(){
        return url;
    }
}
