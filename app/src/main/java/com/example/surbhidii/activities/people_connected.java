package com.example.surbhidii.activities;

import android.content.Context;
import android.os.Bundle;

import com.example.surbhidii.R;
import com.example.surbhidii.adapters.useradapter;
import com.example.surbhidii.model.user;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class people_connected extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference reference;
    StorageReference mStorageref;
    private FirebaseRecyclerAdapter adapter;
    ArrayList<user> musers=new ArrayList<>();

    private Context mContext = people_connected.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_connected);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStorageref = FirebaseStorage.getInstance().getReference();
        musers = new ArrayList<>();

        init();


    }
    private void init() {
        //  clearAll();
        //      String DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        DatabaseReference query = FirebaseDatabase.getInstance().getReference("users");

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    user user = postSnapshot.getValue(user.class);
                    musers.add(user);
                }
                useradapter adapter=new useradapter(musers,mContext);
                Collections.reverse(musers);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
//                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }



}
