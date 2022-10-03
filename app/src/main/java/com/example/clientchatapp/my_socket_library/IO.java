package com.example.clientchatapp.my_socket_library;

import com.example.clientchatapp.my_socket_library.model.MessagePackage;

import java.net.Socket;

//this is interface to define event of incoming data from socket
public interface IO {
    String SEND_MESSAGE = "send_message";
    String SEND_FILE = "send_file";
    String CLIENT_DISCONNECT = "disconnect";
    interface OnConnectListener{
        void onConnect(SingleSocket socket);
    }

    interface OnDisconnectListener{
        void onDisconnect(SingleSocket socket);
    }

    interface OnNewMessageListener{
        void onNewMessage(SingleSocket socket, MessagePackage messagePackage);
    }
}
