package com.example.busmonitoringandticketingsystem;

import com.example.busmonitoringandticketingsystem.LoginScreen.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Server on Maintenance")
                .setMessage("Come again later")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Perform any action you want when "Done" button is clicked

                        dialog.dismiss(); // Close the dialog box
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();

        DatabaseReference rolesRef = FirebaseDatabase.getInstance().getReference("/Server Maintenance");
        rolesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ProgressBar loading = findViewById(R.id.progressBar);

                Boolean onMaintenance = dataSnapshot.getValue(Boolean.class);
                Log.e( "onDataChange: ","LOADING VALUE "+onMaintenance );
                if(onMaintenance != true){
                    loading.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    dialog.show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error 404", Toast.LENGTH_SHORT).show();
            }


        });

    }
}