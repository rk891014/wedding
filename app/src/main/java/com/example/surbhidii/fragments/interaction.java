package com.example.surbhidii.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.surbhidii.adapters.PhotoAdapter;
import com.example.surbhidii.R;
import com.example.surbhidii.photo;
import com.example.surbhidii.video;
import com.google.android.material.tabs.TabLayout;

public class interaction extends Fragment {


    View myFragment;

    public ViewPager viewPager;
    public TabLayout tabLayout;



    public static photo getInstance()    {
        return new photo();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment=inflater.inflate(R.layout.fragment_interaction, container, false);

        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);



        return myFragment;
    }

    //Call onActivity Create method


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        PhotoAdapter adapter = new PhotoAdapter(getChildFragmentManager());

        adapter.addFragment(new photo(),"Photos");
        adapter.addFragment(new video(),"Videos");

        viewPager.setAdapter(adapter);
    }
}