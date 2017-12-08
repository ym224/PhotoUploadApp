package com.yuniemao.photouploadapp;

/**
 * Created by yuniemao on 12/3/17.
 */

public class Image {

    private String authorId;
    private String description;
    private String filePath;
    private String fileName;

    public Image(){}

    public Image(String authorId, String description, String fileName, String filePath) {
        this.authorId = authorId;
        this.description = description;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }


    // file path in firebase storage, used to display image
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}