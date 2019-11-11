package com.example.surbhidii.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.surbhidii.R;
import com.example.surbhidii.adapters.LikeAdapter;
import com.example.surbhidii.model.LikeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class LikesActivity extends AppCompatActivity {


    EditText addcomments;
    TextView post;
    ImageView image_profile;
    String postid;
    String publisherid;
    RecyclerView recyclerView;
    ArrayList<LikeModel> likeList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Likes");

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        likeList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack, this.getTheme()));


        Intent intent= getIntent();
        postid=intent.getStringExtra("postid");
        publisherid=intent.getStringExtra("publisherid");


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


    private void readcomments(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("rohit")
                .child(postid);
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                likeList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    LikeModel likemodel = postSnapshot.getValue(LikeModel.class);
                    likeList.add(likemodel);
                }
                LikeAdapter adapter=new LikeAdapter(LikesActivity.this,likeList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
