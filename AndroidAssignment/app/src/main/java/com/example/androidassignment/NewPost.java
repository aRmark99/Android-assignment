package com.example.androidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewPost extends AppCompatActivity {
    private static final String TAG = "New post";
    //declear
    EditText ettitle, etcontent;
    Button addnewpost;
    String stitle, scontent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        //assign variable
        ettitle = findViewById(R.id.ettitle);
        etcontent = findViewById(R.id.etcontent);
        addnewpost = findViewById(R.id.btnaddnewpost);

        addnewpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get string
                stitle = ettitle.getText().toString();
                scontent = etcontent.getText().toString();
                //create new post according to region
                db.collection("post").document(stitle).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Toast.makeText(getApplicationContext(), "Title already exist", Toast.LENGTH_SHORT).show();
                            } else {
                                // Create a new post
                                Map<String, Object> post = new HashMap<>();
                                post.put("content", scontent);
                                post.put("master", getSharedPreferences("location", MODE_PRIVATE).getString("useremail", ""));
                                post.put("location", getSharedPreferences("location", MODE_PRIVATE).getString("location", ""));
                                //add post info into database
                                db.collection("post").document(stitle).set(post);
                                //redirect
                                Intent intent = new Intent(getApplicationContext(), Forum.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
    }
}