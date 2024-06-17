package com.example.signuploginrealtime;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    //SharedPreferences sharedPreferences;
    public static final String filename = "Login";
    public static String username = "username";
    public static  String password = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
//        if(sharedPreferences.contains(username)){
//            checkUser(sharedPreferences.getString(username,""),sharedPreferences.getString(password,""));
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginUsername = findViewById(R.id.login_user);
        loginPassword = findViewById(R.id.login_password);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = loginUsername.getText().toString();
                String pass = loginPassword.getText().toString();

                if (user.isEmpty() || pass.isEmpty()){
                    loginUsername.setError("Empty Credentials");
                } else {
//                    SharedPreferences.Editor edt = sharedPreferences.edit();
//                    edt.putString(username,user);
//                    edt.putString(password,pass);
//                    edt.commit();
                    checkUser(user,pass);
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }


    public void checkUser( String userUsername,String userPassword) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students");
        Query checkUserDatabase = reference.orderByChild("rollno").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)){
                        loginUsername.setError(null);

                        //Pass the data using intent

                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("rollno").getValue(String.class);

                        Intent intent = new Intent(LoginActivity.this, ScanQR.class);

                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("rollno", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);

                        startActivity(intent);
                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}