package com.example.clientchatapp.my_socket_library.model;

import java.io.Serializable;

//We will pack all things we need to send once time into a package
//include: event code, sender name, message, file size (if file), data (if file)
public class MessagePackage {
    private final String sender;
    private final String event;
    private final boolean isFile;
    private final String message;
    private final byte[] data;
    private final int dataSizeInByte;

    public MessagePackage(String sender, String event, boolean isFile, String message) {
        this.event = event;
        this.isFile = isFile;
        this.message = message;
        data = null;
        dataSizeInByte = 0;
        this.sender = sender;
    }

    public MessagePackage(String sender, String event, boolean isFile, String message, byte[] data, int dataSizeInByte) {
        this.event = event;
        this.isFile = isFile;
        this.data = data;
        this.dataSizeInByte = dataSizeInByte;
        this.message = message;
        this.sender = sender;
    }

    public boolean isFile() {
        return isFile;
    }

    public String getEvent() {
        return event;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getData() {
        return data;
    }

    public int getDataSizeInByte() {
        return dataSizeInByte;
    }

    public String getSender() {
        return sender;
    }
}
