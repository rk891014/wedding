package com.example.surbhidii.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.surbhidii.R;
import com.example.surbhidii.activities.people_connected;
import com.example.surbhidii.activities.register;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class invitation extends Fragment {

//    public LinearLayout dotslayout;
//    public int custom_position = 0;
    VideoView video;
    MediaPlayer mp;
    Boolean isClicked=false;


     boolean isViewShown = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_invitation, container, false);
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        final TextView users=rootView.findViewById(R.id.users);
        video=(VideoView) rootView.findViewById(R.id.videoView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), register.class);
                startActivity(intent);
            }
        });


        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getContext(), people_connected.class);
                startActivity(intent);
            }
        });
        Uri videoPath2 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.wedding);
        final MediaController mediaController=new MediaController(getContext());
        video.setVideoURI(videoPath2);
        video.requestFocus();
       video.start();
       video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
           @Override
           public void onPrepared(MediaPlayer mp) {
               mp.setLooping(true);
           }
       });

       video.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!isClicked) {
                   video.pause();
                   isClicked = true;
               } else {
                   isClicked = false;
                   video.start();
               }
           }
       });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        video.start();
    }
}













//        ViewPager viewPager=v.findViewById(R.id.viewPager2);
//        dotslayout=v.findViewById(R.id.sliderdots);
//        SliderAdapterExample adapterExample=new SliderAdapterExample(getContext());
//        viewPager.setAdapter(adapterExample);
//        preparedots(custom_position++);
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if(custom_position>4)
//                    custom_position=0;
//                preparedots(custom_position++);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//        return v;
//    }
//    private void preparedots(int currentslideposition)
//    {
//        if(dotslayout.getChildCount()>0)
//            dotslayout.removeAllViews();
//        ImageView dots[]=new ImageView[5];
//        for(int i=0;i<5;i++){
//            dots[i]=new ImageView(getContext());
//            if(i==currentslideposition)
//                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.active_dots));
//            else
//                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.inactive_dots));
//
//            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
//            ,ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(4,0,4,0);
//            dotslayout.addView(dots[i],layoutParams);
//
//        }
//    }
//}
