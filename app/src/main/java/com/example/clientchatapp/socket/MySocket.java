package com.example.clientchatapp.socket;

import android.content.Context;

import com.example.clientchatapp.activity.ConnectActivity;
import com.example.clientchatapp.my_socket_library.IO;
import com.example.clientchatapp.my_socket_library.SingleSocket;
import com.example.clientchatapp.my_socket_library.model.MessagePackageBuilder;

import java.io.File;
import java.io.FileNotFoundException;

public class MySocket extends SingleSocket {
    //Singleton
    private final static MySocket INSTANCE = new MySocket();

    private MySocket() {

    }

    public static MySocket getInstance() {
        return INSTANCE;
    }

    public void emitMessage(String message) {
        MessagePackageBuilder builder = new MessagePackageBuilder();
        builder.setEvent(IO.SEND_MESSAGE);
        builder.setType(IO.SEND_MESSAGE);
        builder.setMessage(message);
        emit(builder.build());
    }

    public void emitFile(String filePath, String filename) {
        File file = new File(filePath);
        MessagePackageBuilder builder = new MessagePackageBuilder();
        builder.setMessage(filename);
        builder.setEvent(IO.SEND_FILE);
        builder.setType(IO.SEND_FILE);

        try {
            builder.setDataFromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        emit(builder.build());
    }


}
