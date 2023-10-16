package com.example.busmonitoringandticketingsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class AdminSetSchedule extends AppCompatActivity {

    String[] my_spinner_items = {"Mactan", "Cordova", "Consolacion", "Liloan"};
    private EditText driverNameEditText;
    private Button chooseDateButton;
    private Button cancelButton;
    private EditText driverNumberEditText;
    private ToggleButton routeToggleButton;
    private TextView driverNameLabel;
    private TextView driverNumberLabel;
    private TextView availableScheduleLabel;
    private CheckBox schedule1CheckBox;
    private CheckBox schedule2CheckBox;
    private CheckBox schedule3CheckBox;
    private CheckBox schedule4CheckBox;
    private CheckBox schedule5CheckBox;

    private Button submitButton;

    private String setSchedule_600_am = "UNAVAILALBE";
    private String setSchedule_1230_pm= "UNAVAILALBE";
    private String setSchedule_330_pm= "UNAVAILALBE";
    private String setSchedule_600_pm= "UNAVAILALBE";
    private String setSchedule_900_pm= "UNAVAILALBE";


    String routeSelected = "Mactan";
    String timeString = "";
    String dateFormat = "";
    String onUCLM = "Bus Unavailable";
    String availability = "Unavailable";
    String dateEdited = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set_schedule);

        driverNameEditText = findViewById(R.id.driverNameEditText);
        driverNumberEditText = findViewById(R.id.driverNumberEditText);
        chooseDateButton = findViewById(R.id.button_choose_date);
        routeToggleButton = findViewById(R.id.routeToggleButton);
        schedule1CheckBox = findViewById(R.id.schedule1CheckBox);
        schedule2CheckBox = findViewById(R.id.schedule2CheckBox);
        schedule3CheckBox = findViewById(R.id.schedule3CheckBox);
        schedule4CheckBox = findViewById(R.id.schedule4CheckBox);
        schedule5CheckBox = findViewById(R.id.schedule5CheckBox);
        schedule5CheckBox = findViewById(R.id.schedule5CheckBox);
        submitButton = findViewById(R.id.submit_Button);
        driverNameLabel = findViewById(R.id.driverNameLabel);
        driverNumberLabel = findViewById(R.id.driverNumberLabel);
        availableScheduleLabel = findViewById(R.id.scheduleLabel);


        driverNameEditText.setVisibility(View.GONE);
        driverNumberEditText .setVisibility(View.GONE);
        schedule1CheckBox.setVisibility(View.GONE);
        schedule2CheckBox.setVisibility(View.GONE);
        schedule3CheckBox.setVisibility(View.GONE);
        schedule4CheckBox.setVisibility(View.GONE);
        schedule5CheckBox .setVisibility(View.GONE);
        schedule5CheckBox.setVisibility(View.GONE);
        driverNameLabel.setVisibility(View.GONE);
        driverNumberLabel.setVisibility(View.GONE);
        availableScheduleLabel.setVisibility(View.GONE);



        BusData busSched = new BusData();

        Spinner mySpinner = findViewById(R.id.routeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, my_spinner_items);
        mySpinner.setAdapter(adapter);


        // Add a listener to the route toggle button
        routeToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the toggle button state change event here
                if (isChecked) {
                    routeToggleButton.setAlpha(1f);
                    availability = "Available Time";
                    onUCLM = " Bus is on the way";
                    driverNameEditText.setVisibility(View.VISIBLE);
                    driverNumberEditText .setVisibility(View.VISIBLE);
                    schedule1CheckBox.setVisibility(View.VISIBLE);
                    schedule2CheckBox.setVisibility(View.VISIBLE);
                    schedule3CheckBox.setVisibility(View.VISIBLE);
                    schedule4CheckBox.setVisibility(View.VISIBLE);
                    schedule5CheckBox .setVisibility(View.VISIBLE);
                    schedule5CheckBox.setVisibility(View.VISIBLE);
                    driverNameLabel.setVisibility(View.VISIBLE);
                    driverNumberLabel.setVisibility(View.VISIBLE);
                    availableScheduleLabel.setVisibility(View.VISIBLE);
                } else {
                    routeToggleButton.setAlpha(0.5f);
                    availability = "Bus Unavailable";
                    onUCLM = "Bus Unavailable";
                    driverNameEditText.setVisibility(View.GONE);
                    driverNumberEditText .setVisibility(View.GONE);
                    schedule1CheckBox.setVisibility(View.GONE);
                    schedule2CheckBox.setVisibility(View.GONE);
                    schedule3CheckBox.setVisibility(View.GONE);
                    schedule4CheckBox.setVisibility(View.GONE);
                    schedule5CheckBox .setVisibility(View.GONE);
                    schedule5CheckBox.setVisibility(View.GONE);
                    driverNameLabel.setVisibility(View.GONE);
                    driverNumberLabel.setVisibility(View.GONE);
                    availableScheduleLabel.setVisibility(View.GONE);
                }


            }
        });

        // Add listeners to the schedule checkboxes
        schedule1CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the checkbox state change event here
                if(schedule1CheckBox.isChecked()){
                    setSchedule_600_am = "AVAILABLE";
                }
                else {
                    setSchedule_600_am = "UNAVAILABLE";
                }
            }
        });
        schedule2CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the checkbox state change event here
                if(schedule2CheckBox.isChecked()){
                    setSchedule_1230_pm = "AVAILABLE";
                }
                else {
                    setSchedule_1230_pm = "UNAVAILABLE";
                }
            }
        });
        schedule3CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the checkbox state change event here
                if(schedule3CheckBox.isChecked()){
                    setSchedule_330_pm = "AVAILABLE";
                }
                else {
                    setSchedule_330_pm = "UNAVAILABLE";
                }
            }
        });
        schedule4CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the checkbox state change event here
                if(schedule4CheckBox.isChecked()){
                    setSchedule_600_pm = "AVAILABLE";
                }
                else {
                    setSchedule_600_pm = "UNAVAILABLE";
                }
            }
        });
        schedule5CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the checkbox state change event here
                if(schedule5CheckBox.isChecked()){
                    setSchedule_900_pm = "AVAILABLE";

                }
                else {
                    setSchedule_900_pm = "UNAVAILABLE";

                }
            }
        });


        // Get a reference to the button in your layout

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        final int[] year = {calendar.get(Calendar.YEAR)};
        final int[] month = {calendar.get(Calendar.MONTH)};
        final int[] dayOfMonth = {calendar.get(Calendar.DAY_OF_MONTH)};
        LocalTime currentTime = null;

        String yearstr2 = Integer.toString(year[0]);
        String daystr2 = Integer.toString(dayOfMonth[0]);
        String monthstr2 = Integer.toString(month[0]+1);
        dateEdited = daystr2+"-"+monthstr2+"-"+yearstr2;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            timeString = currentTime.format(formatter);

        }
        chooseDateButton.setText(dateEdited);
// Set a click listener on the button
        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new DatePickerDialog instance with the current date as the default date
                Calendar calendar = Calendar.getInstance();
                year[0] = calendar.get(Calendar.YEAR);
                month[0] = calendar.get(Calendar.MONTH);
                dayOfMonth[0] = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AdminSetSchedule.this, // Replace with your activity or fragment reference
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String yearstr = Integer.toString(year);
                                String daystr = Integer.toString(dayOfMonth);
                                String monthstr = Integer.toString(month+1);
                                dateFormat = daystr+"-"+monthstr+"-"+yearstr;
                                chooseDateButton.setText(dateFormat);
                            }
                        },
                        year[0],
                        month[0],
                        dayOfMonth[0]
                );
                datePickerDialog.show();
            }
        });


        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                routeSelected = (String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        busSched.setRoute(routeSelected);
        GlobalVariable.routeInput = routeSelected;



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm action");
        builder.setMessage("Are you sure you want to perform this action?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Perform the action when the positive button is clicked
                //String pathToDB = "Bus Schedule Info/8-5-2023/Cordova";

                Log.e("Route selected", " "+busSched.getRouteView());
                Log.e("Route selected", " "+GlobalVariable.routeInput);

                String pathToDB = "Bus Schedule Info/"+chooseDateButton.getText()+"/"+routeSelected;
                Log.e("Path to db", " "+pathToDB);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(pathToDB);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Log.e("snapshot count", "Bus ++ "+dataSnapshot.getValue());


                            databaseReference.child("Availability").setValue(busSched.getAvailabilityView());

                            databaseReference.child("dateEdited").setValue(busSched.getDateEditedView());
                            //databaseReference.child("Scheduled Date").setValue(busSched.getDateID());
                            databaseReference.child("driver").setValue(busSched.getDriverView());
                            databaseReference.child("driverNumber").setValue(busSched.getDriverNumberView());
                            databaseReference.child("onUCLM").setValue(busSched.getOnUCLMView());
                            databaseReference.child("route").setValue(routeSelected);
                            databaseReference.child("schedule_1230_pm").setValue(busSched.getSchedule_1230_pmView());
                            databaseReference.child("schedule_1230_pm").setValue(busSched.getSchedule_1230_pmView());

                            databaseReference.child("schedule_330_pm").setValue(busSched.getSchedule_330_pmView());
                            databaseReference.child("schedule_600_am").setValue(busSched.getSchedule_600_amView());
                            databaseReference.child("schedule_600_pm").setValue(busSched.getSchedule_600_pmView());
                            databaseReference.child("schedule_900_pm").setValue(busSched.getSchedule_900_pmView());
                            databaseReference.child("timeEdited").setValue(busSched.getTimeEditedView());


                            Toast.makeText(AdminSetSchedule.this, "Bus details edited successfully clicked", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminSetSchedule.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });


                /*
                    DatabaseReference removeRef = FirebaseDatabase.getInstance().getReference("Bus Schedule Info/"+chooseDateButton.getText());

                    DatabaseReference childReference = removeRef.child(busSched.getRouteView());

// delete the child node
                    childReference.removeValue();

                 */
                    Toast.makeText(AdminSetSchedule.this, "Bus details edited successfully clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminSetSchedule.this, HomeActivity.class);
                    startActivity(intent);
                    finish();


                //String pathToDB = "Bus Schedule Info/"+busSched.getDateID()+"/"+busSched.getRouteView();

            }

        });
        // Perform the action here


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing - dialog will be dismissed

            }
        });

        AlertDialog dialog = builder.create();
// Create and show the dialog

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on click
                Log.e("TAG", "onCreate: getdateid "+busSched.getDateID() );
                if(String.valueOf(driverNameEditText.getText()) == "" && (driverNameEditText.getVisibility() == View.VISIBLE)){
                    Toast.makeText(AdminSetSchedule.this, "Please enter driver name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(String.valueOf(driverNumberEditText.getText()) == "" && (driverNumberEditText.getVisibility() == View.VISIBLE)){
                    Toast.makeText(AdminSetSchedule.this, "Please enter driver nanumbere", Toast.LENGTH_SHORT).show();
                    return;
                }

                else{
                    busSched.setAvailability(availability);
                    busSched.setDateEdited(dateEdited);
                    busSched.setDriver(String.valueOf(driverNameEditText.getText()));
                    busSched.setDriverNumber(String.valueOf(driverNumberEditText.getText()));
                    busSched.setOnUCLM(onUCLM);
                    busSched.setRoute(routeSelected);
                    busSched.setSchedule_1230_pm(setSchedule_1230_pm);
                    busSched.setSchedule_330_pm(setSchedule_330_pm);
                    busSched.setSchedule_600_am(setSchedule_600_am);
                    busSched.setSchedule_600_pm(setSchedule_600_pm);
                    busSched.setSchedule_900_pm(setSchedule_900_pm);
                    busSched.setTimeEdited(timeString);
                    dialog.show();
                }
            }
        });


        TextView cancel = findViewById(R.id.cancelText);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}