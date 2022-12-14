package com.example.clientchatapp.model;

public class Message {
    private String sender;
    private String content;
    private boolean isServer;
    private boolean isFile;

    public Message(String content, boolean isServer, boolean isFile) {
        this.content = content;
        this.isServer = isServer;
        this.isFile = isFile;
    }

    public Message(String sender, String content, boolean isServer, boolean isFile) {
        this.sender = sender;
        this.content = content;
        this.isServer = isServer;
        this.isFile = isFile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isServer() {
        return isServer;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public String getSender() {
        return sender;
    }

}
