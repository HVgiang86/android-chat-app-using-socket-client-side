package com.example.clientchatapp.my_socket_library.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.clientchatapp.my_socket_library.SingleSocket;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

//This async task used to setup a connection to socket server
public class SocketConnectAsync extends AsyncTask<String, Void, Void> {
    private final SingleSocket mSocket;
    WeakReference<Context> contextWeakReference;
    private boolean isConnected = false;
    private final static String TAG = "SOCKET CONNECT LOG";

    public SocketConnectAsync(SingleSocket mSocket, WeakReference<Context> contextWeakReference) {
        this.mSocket = mSocket;
        this.contextWeakReference = contextWeakReference;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String ip = strings[0];
        String portStr = strings[1];
        int port = Integer.parseInt(portStr);

        try {

            mSocket.getSocket().connect(new InetSocketAddress(ip, port), 5000);
            Log.d(TAG, "Connected to server! ip: " + mSocket.getSocket().getInetAddress() + " port: " + mSocket.getSocket().getPort());
            isConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Connected fail!");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        if (isConnected) {
            mSocket.onConnectListener.onConnect(mSocket);
        }
    }


}
