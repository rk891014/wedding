package com.example.surbhidii.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surbhidii.model.Comment2;
import com.example.surbhidii.R;

import java.util.List;

public class CommentAdapter2 extends RecyclerView.Adapter<CommentAdapter2.ViewHolder>{

    private Context mcontext;
    private List<Comment2> mComment2;

    public CommentAdapter2(Context mcontext, List<Comment2> mComment2) {
        this.mComment2=mComment2;
        this.mcontext=mcontext;
        Log.e("listsizes",String.valueOf(mComment2.size()));
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dii_cmnt,parent,false);
        return new CommentAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Comment2 comment2 = mComment2.get(position);
        holder.comment2.setText(mComment2.get(position).getComment2());
        holder.name.setText(mComment2.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mComment2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView comment2,name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment2=itemView.findViewById(R.id.diicmnt);
            name=itemView.findViewById(R.id.name);

        }
    }

}
