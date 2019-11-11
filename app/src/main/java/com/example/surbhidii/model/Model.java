package com.example.surbhidii.model;

public class Model {
    String publisher;
    String url;
    String description;
    String postid;

    public Model(){}

    public Model(String publisher, String url, String description,String postid) {
        this.publisher = publisher;
        this.url = url;
        this.description = description;
        this.postid=postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
