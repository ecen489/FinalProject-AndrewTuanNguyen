package com.example.finalproject123;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class editaddactivity extends AppCompatActivity {
    Button add;
    Button remove;
    boolean status = false;
    boolean status2 = false;
    Button addreal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editaddactivity);
        add = (Button) findViewById(R.id.addbutton);
        remove = (Button) findViewById(R.id.removebutton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (!status) {

                    addfrag frag = new addfrag();
                    add.setVisibility(View.INVISIBLE);
                    remove.setVisibility(View.INVISIBLE);
                    fragmentTransaction.add(R.id.fragment_container , frag);
                    fragmentTransaction.commit();
                    status = true;


                } else {
                    status = false;
                }

            }
        });



        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (editaddactivity.this, uploadtofirebase.class);
                startActivity(intent);
             /*   FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                if (!status2) {

                    removefrag frag = new removefrag();
                    add.setVisibility(View.INVISIBLE);
                    remove.setVisibility(View.INVISIBLE);
                    fragmentTransaction2.add(R.id.fragment_container_2, frag);
                    fragmentTransaction2.commit();
                    status = true;


                } else {
                    status = false;
                }*/

            }
        });






    }
}
