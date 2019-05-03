package com.example.finalproject123;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class body extends AppCompatActivity {

    Button edit;
    Button view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);
        edit = (Button) findViewById(R.id.editbutton);
        view = (Button) findViewById(R.id.viewbutton);

    }

    public void editbuttonpush(View v)
    {
        Intent intent = new Intent(this, editaddactivity.class );
        startActivity(intent);
    }
    public void viewbuttonpush (View v)
    {
        Intent intent = new Intent(this, Retrieve.class);
        startActivity(intent);
    }
}
