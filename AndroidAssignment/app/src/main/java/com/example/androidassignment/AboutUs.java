package com.example.androidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AboutUs extends AppCompatActivity {
    //initialize variable
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Assign variable
        ImageButton btnsearch = findViewById(R.id.btnsearch);
        EditText etsearchbar = findViewById(R.id.etsearchbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        //share preferences
        SharedPreferences sharedpreferences = getSharedPreferences("location", Context.MODE_PRIVATE);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.postmenu);
        //get post
        db.collection("post")
                .whereEqualTo("master", sharedpreferences.getString("useremail", ""))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                //add post
                                TextView textView = new TextView(AboutUs.this);
                                textView.setText(document.getId());
                                textView.setBackgroundColor(Color.parseColor("#FF0000"));
                                textView.setTextColor(Color.parseColor("#00FF00"));
                                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                textView.setTextSize(25);
                                textView.setTypeface(null, Typeface.BOLD);
                                textView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_border));
                                textView.setClickable(true);
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), ViewPost.class);
                                        intent.putExtra("postid", document.getId());
                                        startActivity(intent);
                                    }
                                });
                                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                linearLayout.addView(textView);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            //quert
            @Override
            public void onClick(View v) {
                db.collection("post")
                        .whereEqualTo("content", etsearchbar.getText().toString())
                        .whereEqualTo("master", sharedpreferences.getString("useremail", ""))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    linearLayout.removeAllViews();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                        //add post
                                        TextView textView = new TextView(AboutUs.this);
                                        textView.setText(document.getId());
                                        textView.setBackgroundColor(Color.parseColor("#FF0000"));
                                        textView.setTextColor(Color.parseColor("#00FF00"));
                                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                        textView.setTextSize(25);
                                        textView.setTypeface(null, Typeface.BOLD);
                                        textView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.textview_border));
                                        textView.setClickable(true);
                                        textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), ViewPost.class);
                                                intent.putExtra("postid", document.getId());
                                                startActivity(intent);
                                            }
                                        });
                                        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        linearLayout.addView(textView);
                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });
    }

    public void ClickMenu(View view){
        //open. drawer
        MainActivity.openDrawer(drawerLayout);
    }

    public void Clicklogo(View view){
        //close drawer
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        //Redirect activity to home
        MainActivity.redirectActivity(this,MainActivity.class);
    }

    public void ClickDashboard(View view){
        //redirect to dashboard
        MainActivity.redirectActivity(this, Forum.class);
    }

    public void ClickAboutUs(View view){
        //recreate activity
        recreate();
    }

    public void ClickLogout(View view){
        //Close app
        MainActivity.redirectActivity(this, Login.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}