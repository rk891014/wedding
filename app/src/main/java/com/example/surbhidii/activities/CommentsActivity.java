package com.example.surbhidii.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.surbhidii.R;
import com.example.surbhidii.adapters.CommentAdapter;
import com.example.surbhidii.model.Comment;
import com.example.surbhidii.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {

    EditText addcomments;
    TextView post;
    ImageView image_profile;
     String postid;
     String publisherid;
    RecyclerView recyclerView;
    ArrayList<Comment> commentList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Comments");

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        commentList = new ArrayList<>();
        addcomments=findViewById(R.id.add_comment);
        post=findViewById(R.id.post);
        image_profile=findViewById(R.id.image_profile);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack, this.getTheme()));


        Intent intent= getIntent();
        postid=intent.getStringExtra("postid");
        publisherid=intent.getStringExtra("publisherid");
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addcomments.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this,"chacha cmnt to kro",Toast.LENGTH_SHORT).show();
                }else {
                    addcomment();
                }
            }
        });
        getImage();
        readcomments();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addcomment() {
        @SuppressLint("HardwareIds") String DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("postComments")
                .child(postid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("comment",addcomments.getText().toString());
        hashMap.put("publisher",DeviceID);
        reference.push().setValue(hashMap);
        addcomments.setText("");

    }

    private void getImage()
    {
        @SuppressLint("HardwareIds") String DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users")
                .child(DeviceID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    user u = dataSnapshot.getValue(user.class);
                    Glide.with(getApplicationContext()).load(u.getImageurl()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readcomments(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("postComments")
                .child(postid);
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Comment comment = postSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                CommentAdapter adapter=new CommentAdapter(CommentsActivity.this,commentList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }


                @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
