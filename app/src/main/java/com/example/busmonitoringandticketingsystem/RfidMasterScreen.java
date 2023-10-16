package com.example.busmonitoringandticketingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RfidMasterScreen extends AppCompatActivity {

    Button submitButton;
    Button addTicketButton;
    Button closeButton;
    TextView rfid;
    TextView name;
    TextView lastUpdated;
    TextView ticketCount;
    TextInputEditText addTicketCount;
    boolean addTicketButtonToggle = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfid_master_screen);

        submitButton = findViewById(R.id.submitBtn);
        addTicketButton = findViewById(R.id.add_ticket);;
        closeButton = findViewById(R.id.close);;
        rfid = findViewById(R.id.rfid_current_user);;
        name = findViewById(R.id.name_current_user);
        ticketCount = findViewById(R.id.ticketCount_current_user);
        addTicketCount = findViewById(R.id.ticketCount);
        lastUpdated = findViewById(R.id.last_updated);

        String path = "Cashier 1/RFID read";
        DatabaseReference cashier1Ref = FirebaseDatabase.getInstance().getReference(path);

        cashier1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rfid.setText(dataSnapshot.child("RFID tag").getValue(String.class));
                lastUpdated.setText(dataSnapshot.child("Time read").getValue(String.class));
                String path = "RFID master/"+rfid.getText();
                DatabaseReference cashier1Ref = FirebaseDatabase.getInstance().getReference(path);

                cashier1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            try{
                                if(dataSnapshot.child("Name").exists() && dataSnapshot.child("Name").getValue() != null){
                                    name.setText("Name:"+dataSnapshot.child("Name").getValue(String.class));
                                }
                                else{
                                    name.setText("No Name linked.");
                                }
                            }catch (Exception e){
                                name.setText("No Name linked.");
                            }

                            try{
                                if(dataSnapshot.child("ticket count").exists()){
                                    ticketCount.setText(dataSnapshot.child("ticket count").getValue(String.class));
                                }

                                else{
                                    ticketCount.setText("No bus tickets");
                                }
                            }
                            catch (Exception e){

                                ticketCount.setText("No rgdfgtickets");
                            }

                        }
                        else{
                            name.setText("No Name linked.");
                            ticketCount.setText("No bus tickets");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        addTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //show rfid tag input
                if(!addTicketButtonToggle){

                    addTicketButtonToggle = true;
                    addTicketCount.setVisibility(View.VISIBLE);
                    submitButton.setVisibility(View.VISIBLE);
                }
                else {
                    addTicketButtonToggle = false;
                    addTicketCount.setVisibility(View.GONE);
                    submitButton.setVisibility(View.GONE);
                }

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String path = "RFID master/"+rfid.getText();
                DatabaseReference rfidref = FirebaseDatabase.getInstance().getReference(path);

                rfidref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int oldTicketCount = Integer.parseInt(dataSnapshot.child("ticket count").getValue(String.class));
                        int newTicketCount = oldTicketCount + Integer.parseInt(String.valueOf(addTicketCount.getText()));
                        rfidref.child("ticket count").setValue(Integer.toString(newTicketCount));

                        addTicketCount.setText("");
                        addTicketButtonToggle = false;
                        addTicketCount.setVisibility(View.GONE);
                        submitButton.setVisibility(View.GONE);
                        //rfidref.child("ticket count").setValue(addTicketCount.getText().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        });

    }
}