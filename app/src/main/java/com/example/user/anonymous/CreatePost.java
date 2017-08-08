package com.example.user.anonymous;

import android.content.Intent;
import android.location.Location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreatePost extends AppCompatActivity {

    EditText postText;
    Button send, show;
    DatabaseReference mref, mpostLoca;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);


        mref=FirebaseDatabase.getInstance().getReference("Posts");
        mpostLoca=FirebaseDatabase.getInstance().getReference("PostLocation");
        mauth=FirebaseAuth.getInstance();


        show=(Button) findViewById(R.id.butt);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        postText=(EditText) findViewById(R.id.postText);
        send=(Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additembuttonclicked(view);
            }
        });
    }

    public void additembuttonclicked(View view){

        final String name_T=postText.getText().toString().trim();
        if(!TextUtils.isEmpty(name_T)){
            final DatabaseReference newpost=mref.push();
            String itemID=newpost.getKey();
            newpost.child("Posted").setValue(name_T);
            Locate loco=new Locate();
            GeoFire geoFire=new GeoFire(mpostLoca);
            geoFire.setLocation(itemID,new GeoLocation(loco.latHERE,loco.longHERE));

            Toast.makeText(this,"Posted!",Toast.LENGTH_SHORT).show();
            Intent work=new Intent(CreatePost.this,PostAct.class);
            startActivity(work);
        }

    }


}
