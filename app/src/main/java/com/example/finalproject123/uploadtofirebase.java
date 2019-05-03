package com.example.finalproject123;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
public class uploadtofirebase extends AppCompatActivity {
private static final String TAG = "UPLOADTOFIREBASE";
private static final String OBJECT = "Object";
private static final String quantity = "quantity";
Button save;
private EditText editObject;
private EditText editQuantity;
DatabaseReference dbrf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadtofirebase);
        editObject = (EditText) findViewById(R.id.object);
        editQuantity = (EditText) findViewById(R.id.quantity);
        save = (Button) findViewById(R.id.save);
        dbrf = FirebaseDatabase.getInstance().getReference("Object");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
                Intent intent = new Intent(uploadtofirebase.this, Retrieve.class);
                startActivity(intent);
            }
        });

    }
    private void addItem()
    {
        String name = editObject.getText().toString().trim();
        String numitems = editQuantity.getText().toString().trim();
        int numberitems = Integer.parseInt(numitems);
        if (!TextUtils.isEmpty(name)){
            String id = dbrf.push().getKey();
            Object newobj= new Object(id,name,numberitems);
            dbrf.child(id).setValue(newobj);
            Toast.makeText(this,"Object Added", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this,"Enter a value", Toast.LENGTH_SHORT).show();
        }


    }

}
