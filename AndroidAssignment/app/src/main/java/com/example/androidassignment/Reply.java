package com.example.androidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Reply extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        //declear
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TextView tvtitle = findViewById(R.id.tvposttitle);
        Button btnreply = findViewById(R.id.btnaddnewreply);
        EditText etreply = findViewById(R.id.etcontent);
        //get message
        Intent intent = getIntent();
        String postid = intent.getStringExtra("postid");
        tvtitle.setText("Post Title: "+ postid);
        //comment
        btnreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share preferences
                sharedpreferences = getSharedPreferences("location", Context.MODE_PRIVATE);
                // Create a new user
                Map<String, Object> comment = new HashMap<>();
                Log.d("username", sharedpreferences.getString("useremail", ""));
                comment.put("master", sharedpreferences.getString("useremail", ""));
                comment.put("content", etreply.getText().toString());

                //add user info into database
                db.collection("post").document(postid).collection("comment").document().set(comment);
                //
                Context context = getApplicationContext();
                Toast.makeText(context, "Replied!", Toast.LENGTH_LONG).show();
                //redirect
                Intent intent = new Intent(getApplicationContext(), Forum.class);
                startActivity(intent);
            }
        });
    }
    public void index(View v){
        //Toast.makeText(getApplicationContext(), getSharedPreferences("location", MODE_PRIVATE).getString("useremail", ""), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Forum.class);
        startActivity(intent);
    };
}