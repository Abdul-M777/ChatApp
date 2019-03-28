package com.example.chatapp.Model;

// This is a model class that will be used for users
// All users should have an id, username and imageURL.
public class User {

    private String id;
    private String username;
    private String imageURL;


    public User(String id, String username, String imageURL) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
    }

    // An empty constructor is needed to create a new instance.
    // If i didn't make another contructor an empty constructor will be default.
    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
