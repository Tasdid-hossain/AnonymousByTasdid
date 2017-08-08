package com.example.user.anonymous;

import android.content.Intent;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sackcentury.shinebuttonlib.ShineButton;

public class PostAct extends AppCompatActivity {

    private Toolbar mtoolbar;

    private RecyclerView postviews;
    private DatabaseReference mdatabase;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mlistener;
    private boolean mproceslike=false;
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postviews=(RecyclerView) findViewById(R.id.recycle);
        postviews.setHasFixedSize(true);

        postviews.setLayoutManager(new LinearLayoutManager(this));
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Posts");


        mtoolbar=(Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.common_google_signin_btn_icon_dark);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mauth=FirebaseAuth.getInstance();
        mlistener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    Intent log=new Intent(PostAct.this,MainActivity.class);
                    log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(log);
                }
            }
        };

    }

    Intent intent=getIntent();


    @Override
    protected void onStart() {
        super.onStart();

        mauth.addAuthStateListener(mlistener);
        FirebaseRecyclerAdapter<Info,PostAdapter>FBRA=new FirebaseRecyclerAdapter<Info, PostAdapter>(
                Info.class,R.layout.eachpost,PostAdapter.class,mdatabase
        ) {
            @Override
            protected void populateViewHolder(final PostAdapter viewHolder, final Info model, int position) {
                final String post_key=getRef(position).getKey();
                viewHolder.setText2(model.getPosted());



                mdatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.child(post_key).child("totalLikes").getValue()!=null){
                            viewHolder.voteCount.setText(dataSnapshot.child(post_key).child("totalLikes").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                viewHolder.upvoted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mproceslike=true;

                            mdatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (mproceslike) {
                                        if (dataSnapshot.child(post_key).child("users").hasChild(mauth.getCurrentUser().getUid())) {

                                            mproceslike = false;
                                        } else {
                                            mdatabase.child(post_key).child("users").child(mauth.getCurrentUser().getUid()).setValue(mauth.getCurrentUser().getUid());
                                            if (dataSnapshot.child(post_key).child("totalLikes").getValue()==null){
                                                long i=1;
                                                mdatabase.child(post_key).child("totalLikes").setValue(i);
                                            }
                                            else {
                                                long value=  (long)dataSnapshot.child(post_key).child("totalLikes").getValue();

                                                long increasedIntValue = value + 1;

                                                mdatabase.child(post_key).child("totalLikes").setValue(increasedIntValue);
                                            }
                                            mproceslike = false;
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    }
                });
            }
        };
        postviews.setAdapter(FBRA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void doing(MenuItem item){
        Intent start=new Intent(PostAct.this,CreatePost.class);
        startActivity(start);
    }
    public void did(MenuItem item){
        Intent start= new Intent(PostAct.this,HotPost.class);
        startActivity(start);
    }

    public static class PostAdapter extends RecyclerView.ViewHolder{
        View mview;
        ShineButton upvoted;
        TextView voteCount;


        public PostAdapter(View itemView) {
            super(itemView);
            mview=itemView;
            upvoted=(ShineButton) mview.findViewById(R.id.upVote);
            voteCount=(TextView) mview.findViewById(R.id.voteCount);

        }

        public void setText2(String text){
            TextView textView=(TextView) mview.findViewById(R.id.texting);
            textView.setText(text);
        }

        public void setText3(long text){
            voteCount=(TextView) mview.findViewById(R.id.voteCount);
            voteCount.setText(String.valueOf(text));
        }


    }



}
