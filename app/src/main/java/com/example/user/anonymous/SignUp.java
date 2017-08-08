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

public class SignUp extends AppCompatActivity {
    Button signup, signinback;
    EditText emails,pws;
    TextView main;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistener;
    public static final String TAG = "EmailPassword";
    private DatabaseReference mdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signinback=(Button) findViewById(R.id.signinback);
        signup=(Button) findViewById(R.id.signupButton);
        emails=(EditText) findViewById(R.id.email);
        pws=(EditText) findViewById(R.id.pw);

        signinback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent now= new Intent(SignUp.this,SignIn.class);
                startActivity(now);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupbutton(view);
            }
        });

        //signning up starting
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


    public void signupbutton(View view){
        final String email_T=emails.getText().toString().trim();
        final String pw_T=pws.getText().toString().trim();
        if (!TextUtils.isEmpty(email_T)&& !TextUtils.isEmpty(pw_T)){
            mAuth.createUserWithEmailAndPassword(email_T,pw_T)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String user_id=mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user=mdatabase.child(user_id);
                                current_user.child("Name").setValue(email_T);
                                current_user.child("pw").setValue(pw_T);

                                Toast.makeText(SignUp.this,"Successfully created account"
                                        ,Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SignUp.this,Locate.class);
                                startActivity(intent);
                                finish();
                            }

                            else {
                                Log.e("Error!", task.getException().toString());
                                Toast.makeText(SignUp.this,task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
        }
    }
}

