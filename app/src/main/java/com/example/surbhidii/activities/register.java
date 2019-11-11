package com.example.surbhidii.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.surbhidii.R;
import com.example.surbhidii.model.user;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class register extends AppCompatActivity {
    private static final String TAG = "register";
    EditText username,description,contactno;
    private Button  btnUpload;
    ImageView image_profile;
    private Uri filePath;
    String myUrl="";
    StorageTask uploadTask;
    File actualImage;
    Bitmap bitmap2;
    ByteArrayOutputStream bytearrayoutputstream;
    byte[] BYTE;
    private int lengthbmp;
    private final int PHOTOS = 71;
    StorageReference storageReference;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnUpload = findViewById(R.id.btnUpload);
        image_profile = findViewById(R.id.image_profile);
        username= findViewById(R.id.username);
        description=findViewById(R.id.description);
        contactno=findViewById(R.id.contactno);

        storageReference = FirebaseStorage.getInstance().getReference("users");
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack, this.getTheme()));

        Toast.makeText(register.this,"Tap picture to choose",Toast.LENGTH_LONG).show();


        image_profile.setOnClickListener(new View.OnClickListener() {
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

        String DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(DeviceID);
        reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                user u=dataSnapshot.getValue(user.class);
                Log.d(TAG, "onDataChange: data : "+dataSnapshot.getValue().toString());
                Glide.with(getApplicationContext()).load(u.getImageurl()).into(image_profile);
                username.setText(u.getUsername());
                description.setText(u.getDescription());
                contactno.setText(u.getContactno());
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG, "onCancelled: called");
        }
    });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void chooseImage() {

        Intent intent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                image_profile.setImageBitmap(bitmap);
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
        }
    }
    public void uploadImage() {

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


            uploadTask = ref.putFile(filePath);
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

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                        String DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                        String postid = reference.push().getKey();

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("imageurl",myUrl);
                        hashMap.put("username",username.getText().toString());
                        hashMap.put("contactno",contactno.getText().toString());
                        hashMap.put("description",description.getText().toString());
                        hashMap.put("publisher",DeviceID);
                        reference.child(DeviceID).setValue(hashMap);
                        progressDialog.dismiss();

                    }else{
                        Toast.makeText(register.this,"failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(register.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(register.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }else{
            Toast.makeText(this,"no file selected",Toast.LENGTH_SHORT).show();
        }
    }

}



