package com.example.surbhidii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.surbhidii.R;
import com.example.surbhidii.adapters.CommentAdapter2;
import com.example.surbhidii.model.Comment2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommentsActivity2 extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Comment2> commentList2 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Wishes");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        commentList2 = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack, this.getTheme()));

        readcomments();
    }

    private void readcomments() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("diblessings");
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                commentList2.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.e("test", postSnapshot.toString());
                    Comment2 comment2 = postSnapshot.getValue(Comment2.class);
                    commentList2.add(comment2);
                }
                CommentAdapter2 adapter2=new CommentAdapter2(CommentsActivity2.this,commentList2);
                recyclerView.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}