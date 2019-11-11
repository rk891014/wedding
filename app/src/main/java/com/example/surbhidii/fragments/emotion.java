package com.example.surbhidii.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.method.ScrollingMovementMethod;

import com.example.surbhidii.R;
import com.example.surbhidii.activities.CommentsActivity1;
import com.example.surbhidii.activities.CommentsActivity2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class emotion extends Fragment implements View.OnClickListener {
    TextView jc,dc;
    ImageView jijajiheart,diiheart;
    Animation animSlideDown;
    TextView textView1,textView2;
     Toast toast;
    String ddc,jjc;
    private String ft,dh,jh;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_emotion, container, false);

        View view = inflater.inflate(R.layout.cust_toast_layout,(ViewGroup)v.findViewById(R.id.relativeLayout1));

        toast = new Toast(getContext());
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();




        textView1=view.findViewById(R.id.textView1);
        textView2=view.findViewById(R.id.textView2);
        jc = v.findViewById(R.id.jc);
        jc.setMovementMethod(new ScrollingMovementMethod());
        dc=v.findViewById(R.id.dc);
        dc.setMovementMethod(new ScrollingMovementMethod());

        final ImageView image1=v.findViewById(R.id.image1);
        final ImageView image2=v.findViewById(R.id.image2);
        final ImageView image3=v.findViewById(R.id.image3);
        final ImageView image4=v.findViewById(R.id.image4);
        final ImageView image5=v.findViewById(R.id.image5);
        ImageView image11=v.findViewById(R.id.image11);
        final ImageView image12=v.findViewById(R.id.image12);
        ImageView image13=v.findViewById(R.id.image13);
        final ImageView image14=v.findViewById(R.id.image14);



        image1.setX(40);
        image1.setY(320);
        image2.setX(790);
        image2.setY(379);
        image3.setX(700);
        image3.setY(495);
        image4.setX(300);
        image4.setY(560);
        image5.setX(560);
        image5.setY(620);
        image11.setX(490);
        image11.setY(1115);
        image12.setX(790);
        image12.setY(1180);
        image13.setX(325);
        image13.setY(1285);
        image14.setX(300);
        image14.setY(1410);




        jijajiheart=v.findViewById(R.id.imagejijaji);
        diiheart=v.findViewById(R.id.imagedidi);



        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        image5.setOnClickListener(this);
        image11.setOnClickListener(this);
        image12.setOnClickListener(this);
        image13.setOnClickListener(this);
        image14.setOnClickListener(this);

        final String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        final String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        animSlideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out);
        view.startAnimation(animSlideDown);
        if ("06-11-2019".equals(date)) {
            jijajiheart.setX(520);
            jijajiheart.setY(30);
        }else if("09-11-2019".equals(date)) {
            jijajiheart.setX(760);
            jijajiheart.setY(30);
        }else if ("10-11-2019".equals(date)) {
            jijajiheart.setX(900);
            jijajiheart.setY(150);
        }else if ("11-11-2019".equals(date)) {
            jijajiheart.setX(680);
            jijajiheart.setY(195);
        }else if ("12-11-2019".equals(date)) {
            jijajiheart.setX(500);
            jijajiheart.setY(211);
        }else if ("13-11-2019".equals(date)) {
            jijajiheart.setX(375);
            jijajiheart.setY(259);
        }else if ("14-11-2019".equals(date)) {
            jijajiheart.setX(30);
            jijajiheart.setY(310);
            image1.setVisibility(View.GONE);
        }else if ("15-11-2019".equals(date)) {
            jijajiheart.setX(500);
            jijajiheart.setY(338);
        }else if ("16-11-2019".equals(date)) {
            jijajiheart.setX(790);
            jijajiheart.setY(379);
            image2.setVisibility(View.GONE);
        }else if ("17-11-2019".equals(date)) {
            jijajiheart.setX(800);
            jijajiheart.setY(468);
        }else if ("18-11-2019".equals(date)) {
            jijajiheart.setX(700);
            jijajiheart.setY(470);
            image3.setVisibility(View.GONE);
        }else if ("19-11-2019".equals(date)) {
            jijajiheart.setX(300);
            jijajiheart.setY(560);
            image4.setVisibility(View.GONE);
        }else if ("20-11-2019".equals(date)) {
            jijajiheart.setX(560);
            jijajiheart.setY(620);
            image5.setVisibility(View.GONE);
        }else{
            jijajiheart.setX(560);
            jijajiheart.setY(620);
            image5.setVisibility(View.GONE);

        }
            //diiii
//        vbnm

        if ("07-11-2019".equals(date)) {
            diiheart.setX(480);
            diiheart.setY(1700);
            textView1.setText("13 Days to go....");
            textView2.setText("");
        }else if ("08-11-2019".equals(date)) {
            diiheart.setX(320);
            diiheart.setY(1664);
            textView1.setText("12 Days to go....");
            textView2.setText("");
        }else if ("09-11-2019".equals(date)) {
            diiheart.setX(135);
            diiheart.setY(1654);
            textView1.setText("11 Days to go....");
            textView2.setText("");
        }else if ("10-11-2019".equals(date)) {
            diiheart.setX(260);
            diiheart.setY(1525);
            textView1.setText("10 Days to go....");
            textView2.setText("");
        }else if ("11-11-2019".equals(date)) {
            diiheart.setX(460);
            diiheart.setY(1510);
            textView1.setText("9 Days to go....");
            textView2.setText("");
        }else if ("12-11-2019".equals(date)) {
            diiheart.setX(640);
            diiheart.setY(1490);
            textView1.setText("8 Days to go....");
            textView2.setText("");
        }else if ("13-11-2019".equals(date)) {
            diiheart.setX(800);
            diiheart.setY(1475);
            textView1.setText("7 Days to go....");
            textView2.setText("");
        }else if ("14-11-2019".equals(date)) {
            diiheart.setX(900);
            diiheart.setY(1370);
            textView1.setText("");
            textView2.setText("Haldi Dhaan has Arrived");
        }else if ("15-11-2019".equals(date)) {
            diiheart.setX(730);
            diiheart.setY(1365);
            textView1.setText("5 Days to go....");
            textView2.setText("");
        }else if ("16-11-2019".equals(date)) {
            diiheart.setX(580);
            diiheart.setY(1365);
            textView1.setText("4 Days to go....");
            textView2.setText("");
        }else if ("17-11-2019".equals(date)) {
            diiheart.setX(420);
            diiheart.setY(1378);
            textView1.setText("3 Days to go....");
            textView2.setText("");
        }else if ("18-11-2019".equals(date)) {
            diiheart.setX(325);
            diiheart.setY(1240);
            textView1.setText("Sangeet has Arrived");
            textView2.setText("");
            image13.setVisibility(View.GONE);
            image14.setVisibility(View.GONE);

        }else if ("19-11-2019".equals(date)) {
            diiheart.setX(790);
            diiheart.setY(1180);
            textView1.setText("Mehendi has Arrived");
            textView2.setText("");
            image12.setVisibility(View.GONE);
        }else if ("20-11-2019".equals(date)) {
            diiheart.setX(490);
            diiheart.setY(1115);
            textView1.setText("This is the Day.");
            textView2.setText("");
            image11.setVisibility(View.GONE);

        }else {
            diiheart.setX(490);
            diiheart.setY(1115);
            textView2.setText("");
            image11.setVisibility(View.GONE);
            textView1.setText("They live Happily after");
        }


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("emotion");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ddc = dataSnapshot.child("diicomment").getValue().toString();
                jjc = dataSnapshot.child("jijajicomment").getValue().toString();
                ft=dataSnapshot.child("firsttoast").getValue().toString();
                jh=dataSnapshot.child("jijajiheart").getValue().toString();
                dh=dataSnapshot.child("diiheart").getValue().toString();
                jc.setText(jjc);
                dc.setText(ddc);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        jc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), CommentsActivity1.class);
                startActivity(intent);
            }
        });
        dc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), CommentsActivity2.class);
                startActivity(intent);
            }
        });

        jijajiheart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText(jh);
                textView2.setText("");
                textView1.setTextSize(24);
                toast.show();

            }
        });
        diiheart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText(dh);
                textView2.setText("");
                textView1.setTextSize(24);
                toast.show();

            }
        });
        return v;
    }

    @Override
    public void onClick(View v) {
        toast.show();
        switch (v.getId()){
            case R.id.image1:
                textView1.setText("14 Nov 2019");
                textView2.setText("Haldi Dhan");
                break;
            case R.id.image2:
                textView1.setText("16 Nov 2019");
                textView2.setText("Haldii");
                break;
            case R.id.image3:
                textView1.setText("18 Nov 2019");
                textView2.setText("Sangeet");
                break;
            case R.id.image4:
                textView1.setText("19 Nov 2019");
                textView2.setText("Mehndi");
                break;
            case R.id.image5:
                textView1.setText("20 Nov 2019");
                textView2.setText("Shubh  Vivah");
                break;
            case R.id.image11:
                textView1.setText("20 Nov 2019");
                textView2.setText("Shubh  Vivah");
                break;
            case R.id.image12:
                textView1.setText("19 Nov 2019");
                textView2.setText("Mehndi");
                break;
            case R.id.image13:
                textView1.setText("18 Nov 2019");
                textView2.setText("Sangeet");
                break;
            case R.id.image14:
                textView1.setText("18 Nov 2019");
                textView2.setText("Haldi");
                break;
        }

    }

}