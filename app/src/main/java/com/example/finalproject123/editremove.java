package com.example.finalproject123;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class editremove extends AppCompatActivity {
    EditText nameEdit;
    EditText quantityEdit;
    EditText idEdit;
    DatabaseReference dataref;
    Button btnUpdate;
    Button btnRemove;
    ArrayList<Object> object;
    DatabaseReference id;
    String newstr2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_editremove);
        btnUpdate = (Button) findViewById(R.id.update3);
        btnRemove = (Button) findViewById(R.id.Removebutton2);
       nameEdit = (EditText) findViewById(R.id.editText2);
        quantityEdit = (EditText) findViewById(R.id.quantityedit);
        idEdit = (EditText) findViewById(R.id.editText4);
        dataref = FirebaseDatabase.getInstance().getReference();
        nameEdit.setText(getIntent().getStringExtra("name"));
        idEdit.setText(getIntent().getStringExtra("id"));
        int newint = getIntent().getIntExtra("num",0);
        String newstr = String.valueOf(newint);
        quantityEdit.setText(newstr);
        DatabaseReference obj = FirebaseDatabase.getInstance().getReference("Object");
         id = obj.child(idEdit.getText().toString());
         newstr2 = quantityEdit.getText().toString();

    }

    public void updateclick(View v)
    {
        id.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int newint2 = Integer.parseInt(newstr2);
                id.child("quantity").setValue(newint2);
                id.child("name").setValue(nameEdit.getText().toString());
                Toast.makeText(editremove.this, "Change Successful", Toast.LENGTH_SHORT).show();
                if (newint2 == 0 )
                {
                    notification();
                }
                Intent intent = new Intent(editremove.this ,Retrieve.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void removeclick(View v)
    {
        id.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(editremove.this, "Remove Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(editremove.this ,Retrieve.class);
                    startActivity(intent);                }
                else
                {
                    Toast.makeText(editremove.this, "Remove Not Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void notification()
    {
        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_01")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Time To Restock!!!")
                .setContentText("You have run out of an item! Restock Soon!!");

//Intent resultIntent = new Intent(ctx, MainActivity.class);
//TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
//stackBuilder.addParentStack(MainActivity.class);
//stackBuilder.addNextIntent(resultIntent);
//PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

//builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
