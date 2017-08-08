package com.example.user.anonymous;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HotPost extends AppCompatActivity {

    private RecyclerView postviews2;
    private DatabaseReference mdatabase,mref;
    private LatLng userPosition;
    private boolean hotting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_post);

        postviews2=(RecyclerView) findViewById(R.id.recycle2);
        postviews2.setHasFixedSize(true);

        LinearLayoutManager mlayout=new LinearLayoutManager(this);
        mlayout.setReverseLayout(true);
        mlayout.setStackFromEnd(true);

        Locate loco=new Locate();
        postviews2.setLayoutManager(mlayout);
        userPosition=new LatLng(loco.latHERE,loco.longHERE);

        mdatabase= FirebaseDatabase.getInstance().getReference().child("Posts");
        mref=FirebaseDatabase.getInstance().getReference("Posting");

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference georef=FirebaseDatabase.getInstance().getReference().child("PostLocation");
        GeoFire geoFire=new GeoFire(georef);
        GeoQuery geoQuery=geoFire.queryAtLocation(new GeoLocation(userPosition.latitude,userPosition.longitude),1);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
              
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
        FirebaseRecyclerAdapter<Info, PostAct.PostAdapter>FBRA2= new FirebaseRecyclerAdapter<Info, PostAct.PostAdapter>
                (Info.class,R.layout.eachpost,PostAct.PostAdapter.class,mdatabase) {
            @Override
            protected void populateViewHolder(final PostAct.PostAdapter viewHolder, Info model, int position) {
                final String post_key=getRef(position).getKey();


                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setText2((String) dataSnapshot.child(post_key).child("Posted").getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        };
        postviews2.setAdapter(FBRA2);

    }
}
