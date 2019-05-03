package com.example.finalproject123;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class addfrag extends Fragment {
    Button back;
    Button upload;
    private static final int REQ_CODE = 20;
    private ImageView imgCapture;

    private Uri imageUri=null;
    private FirebaseAuth mAuth;
    private Button savePicButton, displayPicButton, deletePicButton;
    private Camera camera;
    private Uri filepath;
    private EditText Name;
    Button btnCapture;
    String currentPhotoPath;
    FirebaseStorage storage;
    StorageReference storageReference;

    Databaseadapter helper;
    Bitmap bp;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addfrag, container, false);
        btnCapture =(Button) view.findViewById(R.id.takepic);
        imgCapture = (ImageView)view.findViewById(R.id.ImageViewer);
        back= (Button) view.findViewById(R.id.backbutton);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        upload = (Button) view.findViewById(R.id.upload);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(getActivity(), editaddactivity.class );
                startActivityForResult(cInt,REQ_CODE);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt,REQ_CODE);
            }
        });
        savePicButton = (Button) view.findViewById(R.id.button2);
        savePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                addPicture(view);
            }
        });
        displayPicButton = (Button) view.findViewById(R.id.button3);
        displayPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewdata(v);
            }
        });
        Name = (EditText) view.findViewById(R.id.editText);
        helper = new Databaseadapter( getActivity());

        return view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == getActivity().RESULT_OK) {
           //file = data.getData();
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
             filepath= Uri.fromFile(file);
             bp = (Bitmap) data.getExtras().get("data");
            imgCapture.setImageBitmap(bp);

        }
    }

    private void uploadImage() {
        Toast.makeText(getActivity(), "upload started", Toast.LENGTH_SHORT).show();
        if(bp != null)
        {
            Toast.makeText(getActivity(), "upload part 2", Toast.LENGTH_SHORT).show();
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                        }
                    });
        }
    }


    public void addPicture (View view) {
        String t1 = Name.getText().toString();
        byte[] bytePic = getBytes(bp);
        if (t1.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "please Enter a name", Toast.LENGTH_LONG).show();
        }
        else {
            // long id = helper.insertData(t1);
            long id = helper.insertData(t1, bytePic);
            if (id <= 0) {
                Toast.makeText(getActivity().getApplicationContext(), "Insertion unsuccessful", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity().getApplicationContext(), "insertion successful", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void viewdata (View view) {
        String data = helper.getDataString();
        String name = Name.getText().toString();
        //byte[] imagetemp = helper.getDataImage();
        //Bitmap bitmap
        byte[] imagetemp = helper.getDataImage(name);
        bp = getImage(imagetemp);
        imgCapture.setImageBitmap(bp);
        Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQ_CODE);


    }

    public static byte[] getBytes (Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }
    // convert from byte array to bitmap
    public static Bitmap getImage (byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }


    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

}

