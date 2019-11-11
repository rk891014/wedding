package com.example.surbhidii;

import android.annotation.SuppressLint;
        import android.app.AlertDialog;
import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
import android.media.MediaPlayer;
        import android.net.Uri;
import android.provider.Settings;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
        import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
        import android.widget.ProgressBar;
import android.widget.TextView;
        import android.widget.Toast;
        import android.widget.VideoView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.bumptech.glide.request.RequestOptions;
import com.example.surbhidii.activities.CommentsActivity;
import com.example.surbhidii.activities.LikesActivity;
import com.example.surbhidii.model.Videomodel;
import com.example.surbhidii.model.user;
import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
        import java.util.List;

class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    boolean isClicked=false;
    MediaController mediac;
    private static final String TAG = "recyclerAdapter";
    public List<Videomodel> videolist;
    public Context context;
    String rohit;

    public VideoAdapter(List<Videomodel> videolist, Context context) {
        this.videolist = videolist;
        this.context = context;
        Log.e("listsize",String.valueOf(videolist.size()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_video,parent,false);

        return new ViewHolder(v);

    }


    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.crabwalk);
//        requestOptions.error(R.drawable.calf);

        final Videomodel videomodel = videolist.get(position);
        holder.textViewDesc.setText(videolist.get(position).getDescription());
        Glide.with(context).load(videolist.get(position%videolist.size()).getVideourl()).apply(requestOptions)
                .thumbnail(0.5f).skipMemoryCache(false).into(holder.thumbnail);
        String read=videomodel.getPublisher();
        Log.d(TAG, "onBindViewHolder: read: "+read);




        publisherinfo(holder.image_profile,holder.username,holder.publisher,read);
        profilepic(holder.image_profile2);
        isliked(videomodel.getPostid(),holder.like);
        nrlikes(holder.likes,videomodel.getPostid());
        getcomments(holder.comments,videomodel.getPostid());

        getvideo(holder.videoView,videomodel.getVideourl(),holder.play_button,holder.
                thumbnail,holder.progressbar,holder.btnspeak);
        rohit=videomodel.getVideourl();


        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareintent = new Intent();
                shareintent.setAction(Intent.ACTION_SEND);
                shareintent.putExtra(Intent.EXTRA_TEXT,videomodel.getVideourl());
                shareintent.setType("text/plain");
                context.startActivity(shareintent);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
                String DeviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                if( DeviceId.equals(videomodel.getPublisher())){
                    builder.setMessage("Are You Sure......").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference().child("video").child(videomodel.getPostid())
                                    .removeValue();
                            FirebaseDatabase.getInstance().getReference().child("likes").child(videomodel.getPostid())
                                    .removeValue();
                            FirebaseDatabase.getInstance().getReference().child("postComments").child(videomodel.getPostid())
                                    .removeValue();
                        }
                    }).setNegativeButton("cancel",null);
                    AlertDialog alert=builder.create();
                    alert.show();

                }
                else {
//                    builder.setMessage("u cant delete");
                    Toast.makeText(context,"u cant delete",Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.addcomments.getText().toString().equals("")){
                    Toast.makeText(context,"chacha cmnt to kro",Toast.LENGTH_SHORT).show();
                }else {
                    addcomment(holder.addcomments,videomodel.getPostid());
                    Toast.makeText(context,"posted",Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("postid",videomodel.getPostid());
                intent.putExtra("publisher",videomodel.getPublisher());
                context.startActivity(intent);
            }
        });

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LikesActivity.class);
                intent.putExtra("postid",videomodel.getPostid());
                intent.putExtra("publisher",videomodel.getPublisher());
                context.startActivity(intent);
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DeviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("likes").child(videomodel.getPostid())
                            .child(DeviceId).setValue(true);
                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("rohit")
                            .child(videomodel.getPostid());
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("publisher",DeviceId);
                    reference.push().setValue(hashMap);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("likes").child(videomodel.getPostid())
                            .child(DeviceId).removeValue();
                    FirebaseDatabase.getInstance().getReference("rohit")
                            .child(videomodel.getPostid()).removeValue();
                    v.setBackgroundResource(R.drawable.like);
                }
            }
        });
    }
    @Override
    public void onViewDetachedFromWindow(@NotNull ViewHolder holder){
        Log.d(TAG, "onViewDetachedFromWindow: called" +holder.getAdapterPosition());
        holder.videoView.clearFocus();
        if(holder.videoView.isPlaying()){
            holder.videoView.pause();
        }
    }
    @Override
    public void onViewAttachedToWindow(@NotNull ViewHolder holder){
        Log.d(TAG, "onViewAttachedToWindow: "+ holder.getAdapterPosition());
//        if(!holder.videoView.isPlaying()){
//            holder.videoView.start();
//        }
        holder.videoView.setVisibility(View.VISIBLE);
        holder.thumbnail.setVisibility(View.VISIBLE);
        holder.play_button.setVisibility(View.VISIBLE);



    }
    @Override
    public int getItemCount() {
        return videolist.size();
    }

    public void getvideo(final VideoView videoView, final String videourl, final ImageView play_button, final ImageView thumbnail,
                         final ProgressBar progressBar, final Button btnspeak) {
        mediac = new MediaController(videoView.getContext());

        progressBar.setVisibility(View.GONE);
        Uri uri = Uri.parse(videourl);
        videoView.setVideoURI(uri);
        videoView.requestFocus();

        videoView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                mediac.hide();
                videoView.pause();
            }
        });


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);

                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mediac.setMediaPlayer(videoView);
                        videoView.setMediaController(mediac);
                        mediac.setAnchorView(videoView);
                    }
                });
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                                progressBar.setVisibility(View.GONE);
                                return true;
                            }
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                                progressBar.setVisibility(View.VISIBLE);
                                return true;
                            }
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                                progressBar.setVisibility(View.GONE);
                                return true;
                            }
                            case MediaPlayer.MEDIA_INFO_VIDEO_NOT_PLAYING:{
                                play_button.setVisibility(View.VISIBLE);
                            }
                        }
                        return false;
                    }
                });

                btnspeak.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (!isClicked) {
                            v.setBackgroundResource(R.drawable.mute);
                            mp.setVolume(0F,0F);
                            isClicked = true;
                        } else {
                            v.setBackgroundResource(R.drawable.speaker);
                            mp.setVolume(1F,1F);
                            isClicked = false;
                        }

                    }
                });
            }
        });
        thumbnail.setVisibility(View.VISIBLE);

        play_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if(videoView.isPlaying()) {
                    videoView.pause();
                    thumbnail.setVisibility(View.GONE);
                    play_button.setVisibility(View.VISIBLE);
                }
                else{
                    videoView.start();
                    thumbnail.setVisibility(View.GONE);
                    play_button.setVisibility(View.GONE);
                }



            }

        });


    }



    private void getcomments(final TextView comments, String postid){
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

        VideoView videoView;
        TextView textViewDesc,username,publisher,likes,comments,post;
        ImageView image_profile,like,share,delete,image_profile2,play_button,thumbnail;
        EditText addcomments;
        ProgressBar progressbar;
        Button btnspeak,fullscreen;
        MediaController mediaController;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile=(ImageView)itemView.findViewById(R.id.image_profile);
            username=(TextView)itemView.findViewById(R.id.username);
            videoView = (VideoView) itemView.findViewById(R.id.rvideoView);
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
            play_button=(ImageView)itemView.findViewById(R.id.play_button);
            thumbnail=(ImageView)itemView.findViewById(R.id.thumbnail);
            progressbar=(ProgressBar)itemView.findViewById(R.id.progressBar);
            btnspeak=(Button)itemView.findViewById(R.id.btnSpeak);




        }
    }


}