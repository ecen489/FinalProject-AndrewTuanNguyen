package com.example.finalproject123;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class Uploadpicture extends AppCompatActivity {
    private Button btnChoose;
        Button btnUpload;
    private ImageView imageView;
    private final int PICK_IMAGE_REQUEST=71;
    private int REQ_CODE = 1;
    Button save;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    Button btnCapture;
    Bitmap bp;
    FileOutputStream outputStream;
    private Uri filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpicture);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.thisdoesntwork);
        imageView = (ImageView) findViewById(R.id.imgView);
        save = (Button) findViewById(R.id.savebutton);
        btnCapture = (Button) findViewById(R.id.picbutton);
        ActivityCompat.requestPermissions(Uploadpicture.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        btnChoose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chooseImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                uploadpicture();
            }
        });
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt,REQ_CODE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Bitmap image = BitmapFactory.decodeResource(getResources(),R.drawable.check);
                File path= Environment.getExternalStorageDirectory();
                File dir = new File (path+"/save");
                dir.mkdirs();
                File newfile =  new File(dir,"newimage.png");
                OutputStream out = null;
                try
                {
                    out = new FileOutputStream(newfile);
                    image.compress(Bitmap.CompressFormat.PNG,100,out);
                    out.flush();
                    out.close();
                } catch (java.io.IOException e)
                {
                    e.printStackTrace();
                }*/
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                File newFilepath = Environment.getExternalStorageDirectory();
                File dir = new File(newFilepath.getAbsolutePath()+"/Demo/");
                dir.mkdir();
                File newfile =  new File (dir, System.currentTimeMillis()+".jpg");
                try
                {
                     outputStream = new FileOutputStream(newfile);
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
                Toast.makeText(getApplicationContext(),"IMAGE SAVED TO INTERNAL", Toast.LENGTH_SHORT).show();
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }



    private void uploadpicture() {
        Toast.makeText(Uploadpicture.this, "started",Toast.LENGTH_SHORT);
        if (filepath != null)
        {
           // final ProgressDialog progressDialog = new ProgressDialog(this);
           // progressDialog.setTitle("Uploading...");
           // progressDialog.show();
            StorageReference ref = storageReference.child("Images/" + UUID.randomUUID().toString());
            ref.putFile(filepath)

           .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess( UploadTask.TaskSnapshot task)
               {
             //      progressDialog.dismiss();
                   Toast.makeText(Uploadpicture.this, "Uploaded", Toast.LENGTH_SHORT).show();
               }
                })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
               //             progressDialog.dismiss();;
                            Toast.makeText(Uploadpicture.this,"Failed",Toast.LENGTH_SHORT);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress= (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                 //           progressDialog.setMessage("Uploaded "+ (int)progress +"%");

                        }
                    });

           }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            //file = data.getData();
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
            filepath= Uri.fromFile(file);
            bp = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bp);

        }
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData() != null)
        {
            filepath = data.getData();
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
            bp = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bp);


        }
    }
}
