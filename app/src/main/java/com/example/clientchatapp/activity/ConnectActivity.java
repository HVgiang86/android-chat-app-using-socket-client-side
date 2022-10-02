package com.example.clientchatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clientchatapp.R;
import com.example.clientchatapp.socket.MySocket;


public class ConnectActivity extends AppCompatActivity {
    private final int PORT = 5000;
    private EditText ipEdt;
    private EditText portEdt;
    private MySocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ipEdt = findViewById(R.id.ip_edit_text);
        portEdt = findViewById(R.id.port_edit_text);

        socket = MySocket.getInstance();
        socket.createSocket(PORT);

        socket.setOnConnectListener((socket) -> {
            Toast.makeText(getApplicationContext(), "Connected to socket, ip: " + socket.getSocket().getInetAddress()
                    + " port: " + socket.getSocket().getPort(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

    }

    public void connect(View view) {
        Toast.makeText(this, "Connecting to Socket server!", Toast.LENGTH_SHORT).show();
        String ip = ipEdt.getText().toString();
        String port = portEdt.getText().toString();
        if (ip.length() != 0 && port.length() != 0) {
            socket.connectSocket(this, ip, port);
        }
    }
}