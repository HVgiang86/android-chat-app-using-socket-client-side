package com.example.clientchatapp.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clientchatapp.R;
import com.example.clientchatapp.adapter.MessageAdapter;
import com.example.clientchatapp.model.Message;
import com.example.clientchatapp.model.MessageManager;
import com.example.clientchatapp.socket.MySocket;
import com.example.clientchatapp.socket.OnNewMessageListener;
import com.example.clientchatapp.utilities.FilePathGetter;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final MySocket socket = MySocket.getInstance();
    private final int MAX_FILE_SIZE = 50 * 1024 * 1024;
    private EditText messageEdt;
    private RecyclerView recyclerView;
    private MessageManager messageManager;
    private ImageButton attachFileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //After connect to socket server, start task to reading incoming data from server
        socket.startReadingStream();

        socket.setDisconnectListener((socket) -> {
            Toast.makeText(getApplicationContext(), "Disconnected!", Toast.LENGTH_SHORT).show();
            finish();
        });
        socket.setNewMessageListener(new OnNewMessageListener());

        messageEdt = findViewById(R.id.message_edt);
        recyclerView = findViewById(R.id.recycler_view);
        attachFileBtn = findViewById(R.id.attach_file_btn);

        //ask for external storage permission
        if (shouldAskPermissions()) {
            askPermissions();
        }

        attachFileBtn.setOnClickListener((v) -> chooseFile());

        displayMessageList();
    }

    public void displayMessageList() {
        messageManager = MessageManager.getInstance();
        List<Message> messageList = messageManager.getMessageList();

        messageManager.setActivity(this);
        MessageAdapter adapter = new MessageAdapter(messageList, this);
        recyclerView.setAdapter(adapter);

        messageManager.setRv(recyclerView);
        messageManager.setAdapter(adapter);
    }

    public void sendMessage(View v) {
        String s = messageEdt.getText().toString().trim();
        if (s.length() == 0) return;

        socket.emitMessage(s);
        messageManager.addMessage(new Message(s, true, false));
        messageEdt.setText("");
    }

    public void chooseFile() {

        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != 2000 || resultCode != RESULT_OK) return;
        openFileChooser(data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openFileChooser(Intent data) {
        String src = "";
        if (data != null) {
            Uri fileUri = data.getData();
            Log.i("FILE CHOSEN LOG", "Uri: " + fileUri);

            try {
                src = FilePathGetter.getPath(this, fileUri);
                Log.i("FILE CHOSEN LOG", "Uri: " + src);
            } catch (Exception e) {
                Log.e("FILE CHOSEN LOG", "Error: " + e);
                Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        }
        final String filePath = src;
        int lastIndex = src.lastIndexOf("/");
        final String filename = src.substring(lastIndex + 1);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Send file?").setMessage("Do you want to send local file?\nFile path: " + src);
        builder.setPositiveButton("Ok", (dialog, which) -> {
            File file = new File(filePath, filename);
            if (file.length() > MAX_FILE_SIZE) {
                Toast.makeText(this, "File must be lower than 50MB", Toast.LENGTH_SHORT).show();
                return;
            }
            socket.emitFile(filePath, filename);
            messageManager.addMessage(new Message(filePath, true, true));
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        socket.onDestroy();
        super.onDestroy();

    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {"android.permission.MANAGE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }


}