package com.example.androidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewPost extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        TextView tvtitle = findViewById(R.id.posttitle);
        TextView tvcontent = findViewById(R.id.content);
        TextView tvmaster = findViewById(R.id.master);
        Button btnreply = findViewById(R.id.btnreply);
        LinearLayout linearLayout = findViewById(R.id.postcomment);
        //declear
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String TAG = "post";
        //get message
        Intent intent = getIntent();
        String postid = intent.getStringExtra("postid");
        //get post
        DocumentReference docRef = db.collection("post").document(postid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tvtitle.setText(getResources().getString(R.string.Title)+": "+postid+" ("+document.get("location").toString()+")");
                        tvcontent.setText(document.get("content").toString());
                        tvmaster.setText("#1 "+document.get("master").toString());
                        //load comment
                        db.collection("post").document(postid).collection("comment")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            int floor = 1;
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                floor++;
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                //add post
                                                TextView textView = new TextView(ViewPost.this);
                                                TextView textView2 = new TextView(ViewPost.this);
                                                textView.setText("#"+floor+" "+document.get("master").toString());
                                                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                                textView.setPadding(0,200,200,200);
                                                textView.setPadding(10,10,10,10);
                                                textView.setTextSize(25);
                                                textView.setTypeface(null, Typeface.BOLD);
                                                textView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_border));
                                                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                linearLayout.addView(textView);
                                                //2
                                                textView2.setText(document.get("content").toString());
                                                textView2.setGravity(Gravity.CENTER_HORIZONTAL);
                                                textView2.setTextSize(25);
                                                textView2.setTypeface(null, Typeface.BOLD);
                                                textView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                linearLayout.addView(textView2);
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                        Toast.makeText(getApplicationContext(), "No such account", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        btnreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Reply.class);
                intent.putExtra("postid", postid);
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

