package com.example.finalproject123;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start =(Button) findViewById(R.id.buttonstart);
    }
    public void startbuttonpush(View v)
    {
        Intent intent =new Intent (this, authentication.class);
        startActivity(intent);
    }

}

