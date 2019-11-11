package com.example.surbhidii.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.example.surbhidii.R;
import com.example.surbhidii.fragments.emotion;
import com.example.surbhidii.fragments.interaction;
import com.example.surbhidii.fragments.invitation;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private TabLayout mTabLayout;
    private ViewPager mViewpager;
    Button btnSpeak;
    Boolean isClicked=false;
    Dialog mDialog,mDialog1;
    MediaPlayer mp;
    EditText username,description;
    public Button btnChoose, btnUpload;
    ImageView image_profile;
    public Button mDialogyes,mDialogyes1,mDialogno1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.AppTheme);
        mTabLayout=(TabLayout)findViewById(R.id.tabLayout);
        mViewpager=(ViewPager)findViewById(R.id.viewPager);
        setupViewPager(mViewpager);
        mTabLayout.setupWithViewPager(mViewpager);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack, this.getTheme()));
        createDialog();
        mp = MediaPlayer.create(MainActivity.this, R.raw.nm);
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart=prefs.getBoolean("firstStart",true);
//        showStartDialog();
        if(firstStart){
            mViewpager.setCurrentItem(0);
            showStartDialog();
            mp.start();
        }else{
            mViewpager.setCurrentItem(1);
        }
mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
    }
});

        btnSpeak = (Button) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isClicked) {
                    v.setBackgroundResource(R.drawable.mute2);
                    isClicked = true;
                    mp.start();
                } else {
                    v.setBackgroundResource(R.drawable.speak);
                    mp.pause();
                    isClicked = false;
                }
            }
        });
    }

    private void createDialog() {
        mDialog1 = new Dialog(this);
        mDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog1.setContentView(R.layout.dialog_exit2);
        mDialog1.setCanceledOnTouchOutside(true);
        mDialog1.setCancelable(true);
        mDialogyes1 = (Button) mDialog1.findViewById(R.id.yes1);
        mDialogno1 = (Button) mDialog1.findViewById(R.id.No1);
        mDialogyes1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // moveTaskToBack(true);
                // finish();
                // finishAffinity();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
                System.exit(0);
                // android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        mDialogno1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog1.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        mDialog1.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }

    private void showStartDialog() {
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_exit);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialogyes = (Button) mDialog.findViewById(R.id.yes);
        mDialogyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), register.class);
                startActivity(intent);
                mDialog.dismiss();
            }
        });
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mDialog.show();
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
       SharedPreferences.Editor editor=prefs.edit();
       editor.putBoolean("firstStart",false);
       editor.apply();
    }

    private void setupViewPager(ViewPager viewPager){
        viewPagerAdapter adapter = new viewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new invitation(),"Invitation");
        adapter.addFragment(new emotion(),"Tracker");
        adapter.addFragment(new interaction(),"Interaction");
        viewPager.setAdapter(adapter);
    }


    public class viewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> titleList = new ArrayList<>();

        public viewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        public void addFragment(Fragment fragment, String title)    {
            fragmentList.add(fragment);
            titleList.add(title);
        }
    }



}
