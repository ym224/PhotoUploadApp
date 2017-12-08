package com.yuniemao.photouploadapp;

/**
 * Created by yuniemao on 12/3/17.
 */

public class Image {

    private String authorId;
    private String description;
    private String filePath;

    public Image(){}

    public Image(String authorId, String description, String filePath) {
        this.authorId = authorId;
        this.description = description;
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

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}