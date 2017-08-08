package com.example.user.anonymous;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class SignIn extends AppCompatActivity {
    Button signin, signupback;
    EditText emails,pws;
    TextView main;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistener;
    public static final String TAG = "EmailPassword";
    private DatabaseReference mdatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signupback=(Button) findViewById(R.id.signupback);
        signin=(Button) findViewById(R.id.signinButton);
        emails=(EditText) findViewById(R.id.emailSignin);
        pws=(EditText) findViewById(R.id.pwSignIn);


        signupback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent now= new Intent(SignIn.this,SignUp.class);
                startActivity(now);
                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinbutton(view);
            }
        });

        mdatabase= FirebaseDatabase.getInstance().getReference().child("users");

        mAuth=FirebaseAuth.getInstance();
        mAuthstatelistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthstatelistener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthstatelistener != null) {
            mAuth.removeAuthStateListener(mAuthstatelistener);
        }
    }


    public void signinbutton(View view) {
        final String email = emails.getText().toString().trim();
        final String password = pws.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                Toast.makeText(SignIn.this, "Successful login",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SignIn.this, Locate.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.e("Error!", task.getException().toString());
                                Toast.makeText(SignIn.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }


    }}
