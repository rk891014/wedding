package com.example.surbhidii.model;

public class user {
    String username,imageurl,description,contactno;
    public user(){}



    public user(String username, String imageurl, String description,String contactno) {

        this.username = username;
        this.imageurl = imageurl;
        this.description=description;
        this.contactno=contactno;

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }
}