package com.example.surbhidii.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.surbhidii.R;
import com.github.tcking.giraffecompressor.GiraffeCompressor;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

import static com.github.tcking.giraffecompressor.GiraffeCompressor.TYPE_MEDIACODEC;
import static com.iceteck.silicompressorr.Util.getFilePath;


public class uploadvideo extends AppCompatActivity {
    private static final String TAG = "uploadvideo";
    FirebaseStorage storage;
    File file;
    private String filePath;
    private Button btnUpload;
    Button mute;
    Boolean isClicked = false;
    EditText description;
    private Uri videouri;
    long length;
    private static final int REQUEST_CODE = 101;
    private StorageReference videoref;
    private Button btn;
    String myUrl = "";
    private VideoView videoView;
    //        private static final String VIDEO_DIRECTORY = "/demonuts";
    private int GALLERY = 1;
    private MediaController mediac;
    ImageView image;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadvideo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Video");
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        GiraffeCompressor.init(this);
        videoref = storageRef.child("videos");
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack, this.getTheme()));

        btn = (Button) findViewById(R.id.btn);
        mute = (Button) findViewById(R.id.mute);
        videoView = (VideoView) findViewById(R.id.iv);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        mediac = new MediaController(this);
        image=findViewById(R.id.image);
        description = (EditText) findViewById(R.id.description);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideoFromGallary();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
                if(length<=10000) {
                    uploadVideo();
                }else {
                    Toast.makeText(uploadvideo.this,"Too Large!! send file to Admin(8799618620)",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void chooseVideoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

//        public void record() {
//            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//            startActivityForResult(intent, REQUEST_CODE);
//
//        }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        videouri = data.getData();
        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            try {
                filePath = getFilePath(this, videouri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            file = new File(filePath);
            Log.e("before compression", file.getAbsolutePath() + "");
                videoView.setVideoURI(videouri);
                videoView.requestFocus();
                videoView.start();
// doCompress();
            length = file.length();
            length = length/1024;
            Toast.makeText(uploadvideo.this, "Video size:"+length+"KB",
                    Toast.LENGTH_LONG).show();


            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {
                    videoView.setMediaController(mediac);
                    mediac.setAnchorView(videoView);

                    mute.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (!isClicked) {
                                v.setBackgroundResource(R.drawable.mute);
                                mp.setVolume(0F, 0F);
                                isClicked = true;
                            } else {
                                v.setBackgroundResource(R.drawable.speaker);
                                mp.setVolume(1F, 1F);
                                isClicked = false;
                            }

                        }
                    });
                }
            });


        } else if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n" +
                        videouri, Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }

    }


    public void uploadVideo() {

        if (videouri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = videoref.child(System.currentTimeMillis()
                    + "." + getFileExtension(videouri));



            UploadTask uploadTask = ref.putFile(videouri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isComplete()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = (Uri) task.getResult();
                        myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("video");

                        String postid = reference.push().getKey();
                        String DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("videourl", myUrl);
                        hashMap.put("publisher", DeviceID);
                        hashMap.put("description", description.getText().toString());

                        reference.child(postid).setValue(hashMap);
                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(uploadvideo.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(uploadvideo.this,
                            "Upload failed: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(uploadvideo.this, "Upload complete",
                                    Toast.LENGTH_LONG).show();
                            image.setImageResource(R.drawable.videoimage);
                            videoView.setVisibility(View.GONE);

                        }
                    }).addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else {
            Toast.makeText(uploadvideo.this, "Nothing to upload",
                    Toast.LENGTH_LONG).show();
        }
    }
    private void doCompress() {

        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName() + "/media/video");
      Toast.makeText(getApplication(),file.toString(),Toast.LENGTH_SHORT).show();

        if (!file.exists() && !f.exists()) {
            Toast.makeText(getApplication(), "input file not exists", Toast.LENGTH_SHORT).show();
            return;
        }

        GiraffeCompressor.create(TYPE_MEDIACODEC)//默认采用mediacodec,可通过create(TYPE_FFMPEG)获取ffmpeg的实现
                .input(file) //set video to be compressed
                .output(f) //set compressed video output
                .bitRate(15)//set bitrate 码率
                .resizeFactor(1)
//                .filterComplex()
                .ready()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {


                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GiraffeCompressor.Result>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(uploadvideo.this, "Done", Toast.LENGTH_SHORT).show();}

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();


                    }

                    @Override
                    public void onNext(GiraffeCompressor.Result s) {
                        String msg = String.format("compress completed \ntake time:%s ms \nout put file:%s", s.getCostTime(), s.getOutput());
                        msg = msg + "\ninput file size:" + Formatter.formatFileSize(getApplication(), file.length());
                        msg = msg + "\nout file size:" + Formatter.formatFileSize(getApplication(), new File(s.getOutput()).length());
                        Log.d(TAG, "onNext: msg "+msg);
                    }
                });
    }
}