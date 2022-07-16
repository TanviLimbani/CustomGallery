package com.tl.gallerydemo;


public class ImageData {
    String folder;
    String path;

    public ImageData(String str, String str2) {
        this.path = str;
        this.folder = str2;
    }

    public String getFolder() {
        return this.folder;
    }

    public void setFolder(String str) {
        this.folder = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }
}
