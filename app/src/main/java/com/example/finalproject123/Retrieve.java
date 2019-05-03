package com.example.finalproject123;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class Retrieve extends AppCompatActivity {
ListView list;
FirebaseDatabase database;
DatabaseReference ref;
ArrayList<String> arr;
ArrayList<Object> objects;
ArrayAdapter<String> adapter;
Button backbutton;
Object newobj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newobj = new Object();
        setContentView(R.layout.activity_retrieve);
        list = (ListView) findViewById(R.id.objects);
        backbutton = (Button) findViewById(R.id.backtoadd);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Object");
        arr= new ArrayList<>();
        objects = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.objectslist,R.id.objectslist,arr);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Retrieve.this, body.class);
                startActivity(intent);
            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    newobj = ds.getValue(Object.class);
                    objects.add(newobj);
                    String newstr= String.valueOf(newobj.getQuantity());
                    arr.add("Name: "+newobj.getName().toString()+" Number of Items:"+newstr.toString());
                }
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent updatedelete = new Intent(Retrieve.this, editremove.class);
                        Object o = objects.get(position);
                        updatedelete.putExtra("name", o.getName());
                        updatedelete.putExtra("id", o.getId());
                        updatedelete.putExtra("num", o.getQuantity());

                        startActivity(updatedelete);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
