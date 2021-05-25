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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    Button btnlogin;
    String spassword, semail;
    EditText password, email;
    TextView tvregister;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //declear
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        btnlogin = (Button) findViewById(R.id.btnLogin);
        tvregister = (TextView) findViewById(R.id.linkRegister);
        //link to Register
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = (EditText) findViewById(R.id.etpwd);
                email = (EditText) findViewById(R.id.etemail);

                spassword = password.getText().toString();
                semail = email.getText().toString();
                //share preferences
                sharedpreferences = getSharedPreferences("location", Context.MODE_PRIVATE);
                //auth
                DocumentReference docRef = db.collection("users").document(semail);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.get("password"));
                                if(document.get("password").equals(spassword)) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.logining), Toast.LENGTH_SHORT).show();
                                    // set
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("useremail", semail);
                                    editor.commit();
                                    Log.d("username", sharedpreferences.getString("useremail",""));
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                }else{
                                    Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d(TAG, "No such document");
                                Toast.makeText(getApplicationContext(), "No such account", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });
    }
}