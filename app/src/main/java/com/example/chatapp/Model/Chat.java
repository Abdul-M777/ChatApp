package com.example.chatapp.Model;

public class Chat {


    private String sender;
    private String receiver;
    private String message;

    // This is a message model class that will include everything that a message should have.
    // This is the constructor of this class.
    public Chat(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }



    // This is an empty constructor.
    public Chat() {
    }

    // Here we have our getter and setter methods.
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
