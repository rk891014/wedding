package com.example.surbhidii.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surbhidii.model.Comment1;
import com.example.surbhidii.R;

import java.util.List;

public class CommentAdapter1 extends RecyclerView.Adapter<CommentAdapter1.ViewHolder>{

    private Context mcontext;
    private List<Comment1> mComment1;

    public CommentAdapter1(Context mcontext, List<Comment1> mComment1) {
        this.mComment1=mComment1;
        this.mcontext=mcontext;
        Log.e("listsizes",String.valueOf(mComment1.size()));
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dii_cmnt,parent,false);
        return new CommentAdapter1.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Comment1 comment1 = mComment1.get(position);
        holder.comment1.setText(mComment1.get(position).getComment1());
        holder.name.setText(mComment1.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mComment1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView comment1,name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment1=itemView.findViewById(R.id.diicmnt);
            name=itemView.findViewById(R.id.name);

        }
    }

}
