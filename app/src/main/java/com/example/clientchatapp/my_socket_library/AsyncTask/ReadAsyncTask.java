package com.example.clientchatapp.my_socket_library.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.clientchatapp.my_socket_library.IO;
import com.example.clientchatapp.my_socket_library.SingleSocket;
import com.example.clientchatapp.my_socket_library.model.MessagePackage;
import com.example.clientchatapp.my_socket_library.model.MessagePackageBuilder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadAsyncTask extends AsyncTask<Void, Void, Void> {
    private final static String TAG = "Data Received Tag";
    private final SingleSocket socket;

    public ReadAsyncTask(SingleSocket socket) {
        this.socket = socket;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            while (true) {
                InputStream is = socket.getSocket().getInputStream();
                DataInputStream dis = new DataInputStream(is);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                Log.d(TAG, "New Data");
                MessagePackageBuilder builder = new MessagePackageBuilder();
                String event = dis.readLine();
                builder.setEvent(event);
                Log.d(TAG, "Event: " + event);
                if (event.equalsIgnoreCase(IO.SEND_FILE)) {
                    builder.setType(IO.SEND_FILE);

                    int fileSize = Integer.parseInt(dis.readLine());
                    Log.d(TAG, "file size: " + fileSize);
                    String filename = dis.readLine();
                    Log.d(TAG, "Filename: " + filename);
                    byte[] bytes = new byte[fileSize];
                    int count  = 0;
                    //dis.read(bytes);
                    dis.readFully(bytes);
                    Log.d(TAG, "byte array done!");
                    builder.setMessage(filename);
                    builder.setData(bytes);
                    builder.setDataSizeInByte(fileSize);


                } else {
                    builder.setType(IO.SEND_MESSAGE);
                    String message = dis.readUTF();
                    //String message = br.readLine();
                    builder.setMessage(message);
                }

                MessagePackage messagePackage = builder.build();
                Log.d(TAG, "Data received: " + messagePackage);
                if (messagePackage.getEvent().equalsIgnoreCase(IO.CLIENT_DISCONNECT)) {
                    socket.disconnectListener.onDisconnect(socket);
                    onDestroy();
                    return null;
                }

                socket.newMessageListener.onNewMessage(socket, messagePackage);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void onDestroy() {
        try {
            socket.getSocket().close();
            socket.disconnectListener.onDisconnect(socket);
            Log.d("SERVER TAG", "Client disconnected!");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
