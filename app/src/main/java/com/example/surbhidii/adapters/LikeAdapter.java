package com.example.surbhidii.adapters;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.surbhidii.R;
import com.example.surbhidii.model.LikeModel;
import com.example.surbhidii.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {
    private Context mcontext;
    private List<LikeModel> mlikes;

    public LikeAdapter(Context mcontext, List<LikeModel> mlikes) {
        this.mlikes=mlikes;
        this.mcontext=mcontext;
        Log.e("listsizes",String.valueOf(mlikes.size()));
    }


    @NonNull
    @Override
    public LikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.like_item,parent,false);
        return new LikeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LikeModel likes = mlikes.get(position);

        String read=likes.getPublisher();

        getUserinfo(holder.image_profile,holder.username,read);
    }


    @Override
    public int getItemCount() {
        return mlikes.size();
    }
    private void getUserinfo(final ImageView image_profile, final TextView username, String DeviceId) {
        if (DeviceId==null) {
            DeviceId = Settings.Secure.getString(mcontext.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(DeviceId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    user u=dataSnapshot.getValue(user.class);
                    Log.d(TAG, "onDataChange: data : "+dataSnapshot.getValue().toString());
                    Glide.with(mcontext).load(u.getImageurl()).into(image_profile);
                    username.setText(u.getUsername());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: called");
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile;
        public TextView username;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile=itemView.findViewById(R.id.image_profile);
            username=itemView.findViewById(R.id.username);

        }
    }

}
