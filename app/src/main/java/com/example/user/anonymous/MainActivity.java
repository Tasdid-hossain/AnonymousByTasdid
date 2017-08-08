package com.example.user.anonymous;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView maintext;
    Button signin,signup;
    FirebaseAuth mauth;
    FirebaseAuth.AuthStateListener mauthstatelistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signin=(Button) findViewById(R.id.signin);
        signup=(Button) findViewById(R.id.signup);

        mauth=FirebaseAuth.getInstance();
        mauthstatelistener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                    Intent log=new Intent(MainActivity.this,Locate.class);
                    log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(log);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(mauthstatelistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mauth.removeAuthStateListener(mauthstatelistener);
    }

    public void signin(View view){
        Intent now=new Intent(MainActivity.this,SignIn.class);
        now.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        now.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(now);

    }

    public void signup(View view){
        Intent now=new Intent(MainActivity.this,SignUp.class);
        now.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        now.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(now);
    }


}
