package com.example.surbhidii;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.surbhidii.activities.CommentsActivity;
import com.example.surbhidii.activities.LikesActivity;
import com.example.surbhidii.model.Model;
import com.example.surbhidii.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final String TAG = "recyclerAdapter";

//    ArrayList<Model> imageList = new ArrayList<>();

    public List<Model> imageList;
    public Context context;
    SwipeRefreshLayout swiper;

    public MyAdapter(List<Model> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
        this.swiper=swiper;
//        Log.e("listsize",String.valueOf(imageList.size()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.color.colorGrey);
        requestOptions.error(R.color.colorGrey);

        final Model model = imageList.get(position);
        holder.textViewDesc.setText(imageList.get(position).getDescription());
        Glide.with(context).load(imageList.get(position%imageList.size()).getUrl()).apply(requestOptions)
        .thumbnail(0.5f).skipMemoryCache(false).into(holder.imageView);

        String read=model.getPublisher();
        Log.d(TAG, "onBindViewHolder: read: "+read);




        publisherinfo(holder.image_profile,holder.username,holder.publisher,read);
        profilepic(holder.image_profile2);
        isliked(model.getPostid(),holder.like);
        nrlikes(holder.likes,model.getPostid());
        getcomments(holder.comments,model.getPostid());

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareintent = new Intent();
                shareintent.setAction(Intent.ACTION_SEND);
                shareintent.putExtra(Intent.EXTRA_TEXT,model.getUrl());
                shareintent.setType("text/plain");
                context.startActivity(shareintent);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                       FirebaseDatabase.getInstance().getReference().child("data").child(model.getPostid())
                                    .removeValue();
                            FirebaseDatabase.getInstance().getReference().child("likes").child(model.getPostid())
                                    .removeValue();
                            FirebaseDatabase.getInstance().getReference().child("postComments").child(model.getPostid())
                                    .removeValue();
                        }
        });

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LikesActivity.class);
                intent.putExtra("postid",model.getPostid());
                intent.putExtra("publisher",model.getPublisher());
                context.startActivity(intent);
            }
        });

        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.addcomments.getText().toString().equals("")){
                    Toast.makeText(context,"chacha cmnt to kro",Toast.LENGTH_SHORT).show();
                }else {
                    addcomment(holder.addcomments,model.getPostid());
                    Toast.makeText(context,"posted",Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("postid",model.getPostid());
                intent.putExtra("publisher",model.getPublisher());
                context.startActivity(intent);
            }
        });



        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DeviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("likes").child(model.getPostid())
                            .child(DeviceId).setValue(true);

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("rohit")
                            .child(model.getPostid());
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("publisher",DeviceId);
                    reference.push().setValue(hashMap);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("likes").child(model.getPostid())
                            .child(DeviceId).removeValue();
                    FirebaseDatabase.getInstance().getReference("rohit")
                            .child(model.getPostid()).removeValue();
                    v.setBackgroundResource(R.drawable.like);
                }
            }
        });
    }
    private void refresh() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override public void run() {
                imageList.add(0,imageList.get(new Random().nextInt(imageList.size())));
//                imageList.remove(imageList.get(new Random().nextInt()))
                MyAdapter.this.notifyDataSetChanged();

                swiper.setRefreshing(false);
            }
        }, 300);
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void getcomments(final TextView comments,String postid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("postComments")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.setText("view all "+ dataSnapshot.getChildrenCount() +" comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void isliked(String postid, final ImageView imageView) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference()
                .child("likes").child(postid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String DeviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                if(dataSnapshot.child(DeviceId).exists()) {
                    imageView.setImageResource(R.drawable.liked);
                    imageView.setTag("liked");
                }else {
                    imageView.setImageResource(R.drawable.like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void profilepic(final ImageView image_profile2) {
        @SuppressLint("HardwareIds") String DeviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users")
                .child(DeviceId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    user u=dataSnapshot.getValue(user.class);
                    Log.d(TAG, "onDataChange: data : "+dataSnapshot.getValue().toString());
                    Glide.with(context).load(u.getImageurl()).into(image_profile2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: called");
            }
        });
    }

    public void nrlikes(final TextView likes, String postid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+"likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addcomment(final EditText addcomments, String postid) {
        @SuppressLint("HardwareIds") String DeviceID = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("postComments")
                .child(postid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("comment",addcomments.getText().toString());
        hashMap.put("publisher",DeviceID);
        reference.push().setValue(hashMap);
        addcomments.setText("");

    }

    private void publisherinfo(final ImageView image_profile,final TextView username,final TextView publisher,String DeviceId) {
     if (DeviceId==null) {
         DeviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
     }
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(DeviceId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    user u=dataSnapshot.getValue(user.class);
                    Log.d(TAG, "onDataChange: data : "+dataSnapshot.getValue().toString());
                    Glide.with(context).load(u.getImageurl()).into(image_profile);
                    username.setText(u.getUsername());
                    publisher.setText(u.getUsername());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: called");
            }
        });
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

         TextView textViewDesc,username,publisher,likes,comments,post;
        ImageView imageView,image_profile,like,share,delete,image_profile2;
        EditText addcomments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile=(ImageView)itemView.findViewById(R.id.image_profile);
            username=(TextView)itemView.findViewById(R.id.username);
            imageView = (ImageView)itemView.findViewById(R.id.rimageView);
            textViewDesc = (TextView) itemView.findViewById(R.id.textviewdesc);
            publisher=(TextView)itemView.findViewById(R.id.publisher);
            like = (ImageView)itemView.findViewById(R.id.like);
            share = (ImageView)itemView.findViewById(R.id.share);
            delete = (ImageView)itemView.findViewById(R.id.delete);
            likes = (TextView)itemView.findViewById(R.id.likes);
            comments = (TextView)itemView.findViewById(R.id.comments);
            post=(TextView)itemView.findViewById(R.id.post);
            image_profile2=(ImageView)itemView.findViewById(R.id.image_profile2);
            addcomments=(EditText)itemView.findViewById(R.id.add_comment);





        }
    }


}