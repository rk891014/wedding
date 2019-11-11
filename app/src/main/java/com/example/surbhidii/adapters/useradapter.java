package com.example.surbhidii.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.surbhidii.model.user;

import java.util.List;

public class useradapter extends RecyclerView.Adapter<useradapter.ViewHolder> {

//    private FirebaseUser firebaseUser;

    public List<user> musers;
    public Context mContext;

    public useradapter(List<user> musers, Context mContext) {
        this.musers = musers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public useradapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull useradapter.ViewHolder holder, int position) {

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final user user=musers.get(position);
        holder.username.setText(user.getUsername());
        holder.description.setText(user.getDescription());
        holder.contactno.setText(user.getContactno());
        Log.e("userIMage",user.getImageurl()==null ? "null": user.getImageurl());
        Glide.with(mContext).load(user.getImageurl()).into(holder.image_profile);
    }

    @Override
    public int getItemCount() {
        return musers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView description,contactno;
        ImageView image_profile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = (ImageView) itemView.findViewById(R.id.image_profile);
            username = (TextView) itemView.findViewById(R.id.username);
            description = (TextView) itemView.findViewById(R.id.description);
            contactno=(TextView)itemView.findViewById(R.id.contactno);

        }
    }
}
