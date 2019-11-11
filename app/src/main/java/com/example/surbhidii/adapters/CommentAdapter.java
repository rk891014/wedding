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
import com.example.surbhidii.model.Comment;
import com.example.surbhidii.R;
import com.example.surbhidii.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private Context mcontext;
    private List<Comment> mComment;

    public CommentAdapter(Context mcontext, List<Comment> mComment) {
        this.mComment=mComment;
        this.mcontext=mcontext;
        Log.e("listsizes",String.valueOf(mComment.size()));
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item,parent,false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Comment comment = mComment.get(position);
        holder.comment.setText(mComment.get(position).getComment());

        String read=comment.getPublisher();

        getUserinfo(holder.image_profile,holder.username,read);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("postComments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                Comment comment = snapshot1.getValue(Comment.class);
                                if(comment.equals(mComment.get(position).getPublisher())){
                                   snapshot1.getRef().removeValue();
                                }
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

    @Override
    public int getItemCount() {
        return mComment.size();
    }
    private void getUserinfo(final ImageView image_profile,final TextView username,String DeviceId) {
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

        public ImageView image_profile,delete;
        public TextView username,comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile=itemView.findViewById(R.id.image_profile);
            username=itemView.findViewById(R.id.username);
            comment=itemView.findViewById(R.id.comment);
            delete=itemView.findViewById(R.id.delete1);

        }
    }

}
