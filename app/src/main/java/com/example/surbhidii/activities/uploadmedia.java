package com.example.surbhidii.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.surbhidii.R;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

//import com.theartofdev.edmodo.cropper.CropImage;

public class uploadmedia extends AppCompatActivity {

    private static final String TAG = "Home_Workout_Training";


    private Button btnChoose, btnUpload;
    private ImageView imageView;
    private EditText description;
    private Uri filePath=null;
    String myUrl="";
    StorageTask uploadTask;
    private final int PHOTOS = 71;
    StorageReference storageReference;
    Bitmap bitmap=null;
    File actualImage;
    byte[] final_image;

    Bitmap bitmap1, bitmap2;
    ByteArrayOutputStream bytearrayoutputstream;
    byte[] BYTE,BYTE2;
    private int lengthbmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadmedia);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Image");
   //     BottomNavigationView navView =(BottomNavigationView) findViewById(R.id.nav_view2);
//        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.navigation_home:
////                    mTextMessage.setText(R.string.title_home);
//                        Intent i = new Intent(uploadmedia.this,Home_Workout_Training.class);
//                        startActivity(i);
//                        overridePendingTransition(R.anim.fui_slide_in_right,R.anim.slide_out);
//                        break;
//                    //case R.id.navigation_dashboard:
//
//                        //break;
//                    case R.id.challenges:
////                    mTextMessage.setText(R.string.title_notifications);
//                        break;
//                }
//                return true;
//            }
//        });

        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);
        description= findViewById(R.id.description);

        storageReference = FirebaseStorage.getInstance().getReference("data");

        Intent intent = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTOS);


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


    }


    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void chooseImage() {

        Intent intent = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTOS);




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTOS && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
                    actualImage=new File(filePath.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            lengthbmp = imageInByte.length;
            lengthbmp = lengthbmp/1024;
//            lengthbmp in kb
            //"data" is your "byte[] data"
            bytearrayoutputstream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,30,bytearrayoutputstream);

            BYTE = bytearrayoutputstream.toByteArray();

            bitmap2 = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);
//
//


//            try {
//                        bitmap = new Compressor(getApplicationContext())
//                        .setMaxWidth(640)
//                        .setMaxHeight(480)
//                        .setQuality(75)
//                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                        .compressToBitmap(actualImage);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.WEBP,75,baos);
//                final_image = baos.toByteArray();


        }
    }
    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        if(filePath != null)
        {


            final StorageReference ref = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(filePath));

if(lengthbmp<=50){
    uploadTask = ref.putFile(filePath);
}else {
    uploadTask = ref.putBytes(BYTE);
}



            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                   if(!task.isComplete()){
                       throw task.getException();
                    }
                   return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = (Uri) task.getResult();
                        myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("data");

                        String postid = reference.push().getKey();
                        String DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("postid",postid);
                        hashMap.put("url",myUrl);
                        hashMap.put("publisher",DeviceID);
                        hashMap.put("description",description.getText().toString());

                        reference.child(postid).setValue(hashMap);
                        progressDialog.dismiss();

                    }else{
                        Toast.makeText(uploadmedia.this,"failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            imageView.setImageResource(R.drawable.big);

                            Toast.makeText(uploadmedia.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(uploadmedia.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        }
                    });
        }else{
            Toast.makeText(this,"no file selected",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

}



