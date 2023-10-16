package com.example.busmonitoringandticketingsystem.ui.home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busmonitoringandticketingsystem.AdminSetSchedule;
import com.example.busmonitoringandticketingsystem.BusAdapter;
import com.example.busmonitoringandticketingsystem.BusData;
import com.example.busmonitoringandticketingsystem.GlobalVariable;
import com.example.busmonitoringandticketingsystem.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleFragment extends Fragment {
    private RecyclerView recyclerView;
    private BusAdapter busAdapter;
    private List<BusData> busUserList;

    private DatabaseReference databaseReference;
    TextView textView;
    TabLayout tabLayout;
    Button chooseDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);


        recyclerView = view.findViewById(R.id.scheduleRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chooseDate = view.findViewById(R.id.choose_date);
        busUserList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int yearToday = calendar.get(Calendar.YEAR);
        int monthToday = calendar.get(Calendar.MONTH);
        int dayOfMonthToday= calendar.get(Calendar.DAY_OF_MONTH);

        String yearstrToday = Integer.toString(yearToday);
        String daystrToday = Integer.toString(dayOfMonthToday);
        String monthstrToday = Integer.toString(monthToday+1);
        String dateToday = daystrToday + "-" + monthstrToday + "-" + yearstrToday;

        Date today = calendar.getTime();




        final int[] year = {calendar.get(Calendar.YEAR)};
        final int[] month = {calendar.get(Calendar.MONTH)};
        final int[] dayOfMonth = {calendar.get(Calendar.DAY_OF_MONTH)};

        chooseDate.setText(dateToday);

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new DatePickerDialog instance with the current date as the default date
                Calendar calendar = Calendar.getInstance();
                year[0] = calendar.get(Calendar.YEAR);
                month[0] = calendar.get(Calendar.MONTH);
                dayOfMonth[0] = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), // Replace with your activity or fragment reference
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String yearstr = Integer.toString(year);
                                String daystr = Integer.toString(dayOfMonth);
                                String monthstr = Integer.toString(month+1);
                                String dateSelected = daystr+"-"+monthstr+"-"+yearstr;
                                chooseDate.setText(dateSelected);
                            }
                        },
                        year[0],
                        month[0],
                        dayOfMonth[0]
                );
                datePickerDialog.show();
            }
        });


        Button showSchedule;
        showSchedule = view.findViewById(R.id.show_schedule);
        showSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Bus Schedule Info/"+ chooseDate.getText());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        busUserList.clear();
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                            BusData busUser = childSnapshot.getValue(BusData.class);
                            busUser.setAvailability(childSnapshot.child("Availability").getValue(String.class));
                            busUser.setDateEdited(childSnapshot.child("dateEdited").getValue(String.class));
                            busUser.setDriver(childSnapshot.child("driver").getValue(String.class));
                            busUser.setDriverNumber(childSnapshot.child("driverNumber").getValue(String.class));
                            busUser.setOnUCLM(childSnapshot.child("onUCLM").getValue(String.class));
                            busUser.setRoute(childSnapshot.child("route").getValue(String.class));
                            busUser.setSchedule_330_pm(childSnapshot.child("schedule_330_pm").getValue(String.class));
                            busUser.setSchedule_600_am(childSnapshot.child("schedule_600_am").getValue(String.class));
                            busUser.setSchedule_600_pm(childSnapshot.child("schedule_600_pm").getValue(String.class));
                            busUser.setSchedule_900_pm(childSnapshot.child("schedule_900_pm").getValue(String.class));
                            busUser.setSchedule_1230_pm(childSnapshot.child("schedule_1230_pm").getValue(String.class));
                            busUser.setTimeEdited(childSnapshot.child("timeEdited").getValue(String.class));

                            busUserList.add(busUser);

                        }
                        Log.e("snapshot count", "Bus ++ " + dataSnapshot.getValue());
                        Log.e("Count:",""+String.valueOf(dataSnapshot.getChildrenCount()));




                        busAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("TAG", "onCancelled", databaseError.toException());
                    }
                });

                busAdapter = new BusAdapter(busUserList);
                recyclerView.setAdapter(busAdapter);

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Bus Schedule Info/"+ chooseDate.getText());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                busUserList.clear();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                    BusData busUser = childSnapshot.getValue(BusData.class);
                    busUser.setAvailability(childSnapshot.child("Availability").getValue(String.class));
                    busUser.setDateEdited(childSnapshot.child("dateEdited").getValue(String.class));
                    busUser.setDriver(childSnapshot.child("driver").getValue(String.class));
                    busUser.setDriverNumber(childSnapshot.child("driverNumber").getValue(String.class));
                    busUser.setOnUCLM(childSnapshot.child("onUCLM").getValue(String.class));
                    busUser.setRoute(childSnapshot.child("route").getValue(String.class));
                    busUser.setSchedule_330_pm(childSnapshot.child("schedule_330_pm").getValue(String.class));
                    busUser.setSchedule_600_am(childSnapshot.child("schedule_600_am").getValue(String.class));
                    busUser.setSchedule_600_pm(childSnapshot.child("schedule_600_pm").getValue(String.class));
                    busUser.setSchedule_900_pm(childSnapshot.child("schedule_900_pm").getValue(String.class));
                    busUser.setSchedule_1230_pm(childSnapshot.child("schedule_1230_pm").getValue(String.class));
                    busUser.setTimeEdited(childSnapshot.child("timeEdited").getValue(String.class));

                    busUserList.add(busUser);

                }
                Log.e("snapshot count", "Bus ++ " + dataSnapshot.getValue());
                Log.e("Count:",""+String.valueOf(dataSnapshot.getChildrenCount()));

                /*
                if(GlobalVariable.role!="Admin"){
                    setSched.setVisibility(View.GONE);
                }

                 */

                busAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });

        busAdapter = new BusAdapter(busUserList);
        recyclerView.setAdapter(busAdapter);
        return view;
    }
}