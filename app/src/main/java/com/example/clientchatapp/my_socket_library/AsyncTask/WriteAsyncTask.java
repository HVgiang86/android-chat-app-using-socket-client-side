package com.example.clientchatapp.my_socket_library.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.clientchatapp.my_socket_library.model.MessagePackage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class WriteAsyncTask extends AsyncTask<MessagePackage, Void, Void> {
    private final static String TAG = "Transferring Log";
    private final Socket mSocket;

    public WriteAsyncTask(Socket mSocket) {
        this.mSocket = mSocket;
    }

    @Override
    protected Void doInBackground(MessagePackage... messagePackages) {
        MessagePackage messagePackage = messagePackages[0];
        Log.d(TAG, "Data to send: " + messagePackage.toString());
        Log.d(TAG, "Event: " + messagePackage.getEvent());

        try {
            OutputStream os = mSocket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.flush();
            dos.writeBytes(messagePackage.getEvent() + "\n");

            if (messagePackage.isFile()) {
                dos.writeBytes(messagePackage.getDataSizeInByte() + "\n");
                Log.d(TAG, "size: " + messagePackage.getDataSizeInByte());
                dos.writeBytes(messagePackage.getMessage() + "\n");
                Log.d(TAG, "message: " + messagePackage.getMessage());
                byte[] data = messagePackage.getData();
                os.write(messagePackage.getData(), 0, messagePackage.getDataSizeInByte());
            } else {
                dos.writeUTF(messagePackage.getMessage()+"\n");
//                dos.writeBytes(messagePackage.getMessage() + "\n");
            }

            Log.d(TAG, "transferred");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Data transferred fail!");
        }
        return null;
    }

}
