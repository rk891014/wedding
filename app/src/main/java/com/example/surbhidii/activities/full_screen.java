package com.example.surbhidii.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

import com.example.surbhidii.R;
import com.example.surbhidii.video;

public class full_screen extends AppCompatActivity {

    String video_url;
    VideoView videoView;
    int width,height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoView=findViewById(R.id.videoView);
        video_url = getIntent().getStringExtra("video_url");
//        video_url="https://firebasestorage.googleapis.com/v0/b/surbhidii.appspot.com/o/videos%2F1572717396735.mp4?alt=media&token=8fd77a8f-95ce-4ee8-b9e0-4d14946e5277";

//        Uri videoPath = Uri.parse(video_url);


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent=new Intent(full_screen.this, video.class);
                startActivity(intent);
            }
        });

//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(getApplicationContext(),videoPath);
//        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
//        retriever.release();
//        if(width<=height){
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }else {
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
//        Uri uri = Uri.parse(video_url);
        videoView.requestFocus();
        videoView.start();


    }
}
