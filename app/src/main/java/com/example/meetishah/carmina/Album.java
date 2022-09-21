package com.example.meetishah.carmina;

/**
 * Created by Meeti Shah on 2/17/2018.
 */

public class Album {
    private String name;
    private int thumbnail;
    private String fileName;
    private String imagePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getImagePath(){return imagePath;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;


    }
    public Album() {
    }

    public Album(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }




    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}