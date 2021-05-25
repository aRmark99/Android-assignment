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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Forum extends AppCompatActivity {
    //initialize variable
    DrawerLayout drawerLayout;
    Button btnnewpost;
    TextView loc;
    String location;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Assign variable
        TAG = "POST";
        drawerLayout = findViewById(R.id.drawer_layout);
        btnnewpost = findViewById(R.id.btnaddnewpost);
        loc = findViewById(R.id.loc);
        location = getSharedPreferences("location", MODE_PRIVATE).getString("location", "");
        loc.setText(getResources().getString(R.string.loc) + location);
        EditText etsearchbar = findViewById(R.id.etsearchbar);
        ImageButton btnsearch = findViewById(R.id.btnsearch);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.postmenu);

        //get post
        db.collection("post")
                .whereEqualTo("location", location)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //add post
                                TextView textView = new TextView(Forum.this);
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
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        btnnewpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewPost.class);
                startActivity(intent);
            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            //quert
            @Override
            public void onClick(View v) {
                db.collection("post")
                        .whereEqualTo("content", etsearchbar.getText().toString())
                        .whereEqualTo("location", location)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    linearLayout.removeAllViews();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        //add post
                                        TextView textView = new TextView(Forum.this);
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
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });
    }

    public void index(View v) {
        //Toast.makeText(getApplicationContext(), getSharedPreferences("location", MODE_PRIVATE).getString("useremail", ""), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Forum.class);
        startActivity(intent);
    }

    ;


    public void ClickMenu(View view) {
        //open. drawer
        MainActivity.openDrawer(drawerLayout);
    }

    public void Clicklogo(View view) {
        //close drawer
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        //Redirect activity to home
        MainActivity.redirectActivity(this, MainActivity.class);
    }

    public void ClickDashboard(View view) {
        //recreate activity
        recreate();
    }

    public void ClickAboutUs(View view) {
        //redirect to about us
        MainActivity.redirectActivity(this, AboutUs.class);
    }

    public void ClickLogout(View view) {
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
