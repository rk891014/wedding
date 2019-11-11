package com.example.surbhidii;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.surbhidii.activities.uploadvideo;
import com.example.surbhidii.model.Videomodel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class video extends Fragment {


    private static final String TAG = "NewsActivity";
    RecyclerView recyclerView;
    DatabaseReference reference;
    StorageReference mStorageref;
    private FirebaseRecyclerAdapter adapter;
    ArrayList<Videomodel> videolist = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_video, container, false);
        FloatingActionButton fab =view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), uploadvideo.class);
                startActivity(intent);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mStorageref = FirebaseStorage.getInstance().getReference();
        videolist = new ArrayList<>();

        init();
        return view;
    }

    private void init() {
        clearAll();
        //      String DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        DatabaseReference query = FirebaseDatabase.getInstance().getReference("video");

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                videolist.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Videomodel videomodel = postSnapshot.getValue(Videomodel.class);
                    videolist.add(videomodel);
                }
                VideoAdapter adapter=new VideoAdapter(videolist,getContext());
                Collections.reverse(videolist);
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

    private void clearAll() {
        if(videolist!=null){
            videolist.clear();
        }
        videolist = new ArrayList<>();
    }

}