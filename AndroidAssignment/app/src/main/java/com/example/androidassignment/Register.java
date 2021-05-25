package com.example.androidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


public class Register extends AppCompatActivity {

    private static final String TAG = "Register";
    Button btnregister;
    String sname, spassword, semail;
    EditText name, password, email;
    TextView tvlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //declear
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        btnregister = (Button) findViewById(R.id.btnRegister);
        tvlogin = (TextView) findViewById(R.id.linkLogin);
        //link to login
        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
        //register
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = (EditText) findViewById(R.id.etname);
                password = (EditText) findViewById(R.id.etpwd);
                email = (EditText) findViewById(R.id.etemail);

                sname = name.getText().toString();
                spassword = password.getText().toString();
                semail = email.getText().toString();

                //check user ac
                db.collection("users").document(semail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Toast.makeText(getApplicationContext(), "You have already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                // Create a new user
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", sname);
                                user.put("password", spassword);

                                //add user info into database
                                db.collection("users").document(semail).set(user);
                                startActivity(new Intent(Register.this, Login.class));
                            }
                        }
                    }
                });

            }
        });
    }
}