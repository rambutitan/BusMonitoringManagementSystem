package com.example.busmonitoringandticketingsystem.ui.account;

import static android.content.Context.CLIPBOARD_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.busmonitoringandticketingsystem.AdminSetSchedule;
import com.example.busmonitoringandticketingsystem.GlobalVariable;
import com.example.busmonitoringandticketingsystem.HomeActivity;
import com.example.busmonitoringandticketingsystem.LoginScreen.RegistrationScreen;
import com.example.busmonitoringandticketingsystem.R;
import com.example.busmonitoringandticketingsystem.LoginScreen.Login;
import com.example.busmonitoringandticketingsystem.RfidMasterScreen;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AccountFragment extends Fragment {


    String[] ticket_number_items = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    Button setSchedButton;
    Button logoutBtn;
    TextView emailView;
    TextView name;
    TextView roleView;
    TextInputLayout trackIdInputLayout;
    TextInputLayout rfidInputLayout;

    TextInputEditText rfidInput;
    ImageView linkRfidImage;
    Button getTrackuid;
    Button linkRfidInputBtn;
    Button addTicketBtn;
    TextView countTicket;
    TextView adminModeText;
    TextView tagidLabel;
    Button trackingBtn;
    Button addPassengerTicket;

    Button registerNewUser;
    boolean rfidInputViewBtnToggle = false;
    boolean adminModeL = false;
    boolean rfidLinkInput = false;
    boolean trackHistoryInput = false;
    boolean rfidTagAlreadyExists = false;
    boolean uidExists = false;

    Toast currentToast;


    private LocationRequest locationRequest;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        name = view.findViewById(R.id.textview_username);

        registerNewUser = view.findViewById(R.id.register_new_user);
        tagidLabel = view.findViewById(R.id.tagIDLabel);
        emailView = view.findViewById(R.id.global_email);
        roleView = view.findViewById(R.id.textview_role);

        adminModeText = view.findViewById(R.id.admin_mode);


        rfidInput = view.findViewById(R.id.rfidTagInput);
        linkRfidInputBtn = view.findViewById(R.id.link_rfid_input);
        rfidInputLayout = view. findViewById(R.id.rfidTagInputL);

        RelativeLayout adminLayoutViewer = view.findViewById(R.id.adminLayout);
        countTicket = view.findViewById(R.id.ticketCount);

        linkRfidImage = view.findViewById(R.id.link_rfidTag);

        trackIdInputLayout = view.findViewById(R.id.track_id_inputL);

        addTicketBtn = view.findViewById(R.id.add_passenger_ticket);

        trackingBtn = view.findViewById(R.id.trackBtn);

        logoutBtn = view.findViewById(R.id.button_logout);

        Log.e("onCreateView", "Global variables"+GlobalVariable.name+GlobalVariable.email+GlobalVariable.role+GlobalVariable.uid);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(9);

        //hide rfid tag input

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);


        adminModeText.setVisibility(View.GONE);

        //hide history tracking input
        trackIdInputLayout.setVisibility(View.GONE);
        trackingBtn.setVisibility(View.GONE);


        registerNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RegistrationScreen.class);
                startActivity(intent);
            }
        });
        addTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RfidMasterScreen.class);
                startActivity(intent);
            }
        });



        emailView.setText(GlobalVariable.email);
        roleView.setText(GlobalVariable.role);
        name.setText(GlobalVariable.name);
        countTicket.setText(GlobalVariable.ticketCount);

        TextView textEmailLabel = view.findViewById(R.id.textview_email);
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users/"+GlobalVariable.uid);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Object roleObject = dataSnapshot.child("role").getValue();

                if (roleObject instanceof String) {
                    String role = (String) roleObject;
                    if (role.equals("Admin")) {

                        adminModeText.setVisibility(View.VISIBLE);
                    } else if(role.equals("Driver")){
                        adminModeText.setVisibility(View.VISIBLE);
                        adminModeText.setText("Driver");
                    }
                    else{

                        adminModeText.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        Log.e("Account", "1Linked: "+GlobalVariable.rfidTagLinked);
        if(GlobalVariable.rfidTagLinked){
            tagidLabel.setText(("Rfid tag linked"));
            rfidInput.setText(GlobalVariable.rfidTag);
            tagidLabel.setTextColor(0xFF00FF00);
            rfidInput.setFocusable(false);
            rfidInput.setFocusableInTouchMode(false);
            linkRfidInputBtn.setText("Remove");
        }

        adminModeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout modeSetLayout = view.findViewById(R.id.modeLayout);
                modeSetLayout.setVisibility(View.GONE);
                if(!adminModeL){
                    adminModeText.setTextColor(0xFF000000);
                    adminModeText.setBackgroundColor(0xFFFFFF00);
                    adminLayoutViewer.setVisibility(View.VISIBLE);

                    getCurrentLocation();
                    adminModeL = true;
                }
                else {
                    adminModeText.setTextColor(0xFFFFFFFF);
                    adminModeText.setBackgroundColor(0xFF0000FF);
                    adminModeL = false;

                    getCurrentLocation();
                    modeSetLayout.setVisibility(View.VISIBLE);
                    adminLayoutViewer.setVisibility(View.GONE);
                }

            }
        });
        /*
        final EditText rfidInput = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Assign RFID tag to user");
        builder.setMessage("Scan rfid once then pres ok: ");
        builder.setView(rfidInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String rfid_tag = rfidInput.getText().toString();
                String uid = GlobalVariable.uid;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child((uid));
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){

                            databaseReference.child("rfid tag").setValue(rfid_tag);
                            databaseReference.child("rfid message").setValue("Rfid linked");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });

        AlertDialog rfidDialog = builder.create();
        linkRfid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rfidDialog.show();
            }
        });



         */

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure you want to link this rfid to your account?");
        builder.setMessage("Linking this rfid tag now means you cannot link this tag to other accounts.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GlobalVariable.rfidTag = rfidInput.getText().toString();
                String path = "RFID master/"+rfidInput.getText().toString();
                DatabaseReference rolesRef = FirebaseDatabase.getInstance().getReference(path);

                rolesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        rolesRef.child("Name").setValue(GlobalVariable.name);
                        rolesRef.child("linked email").setValue(GlobalVariable.email);
                        rolesRef.child("linked uid").setValue(GlobalVariable.uid);
                        rolesRef.child("ticket count").setValue("10");
                        //rolesRef.child("linked email").setValue(GlobalVariable.email);
                        GlobalVariable.rfidTagLinked = true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                DatabaseReference uidRef = FirebaseDatabase.getInstance().getReference("Users/"+GlobalVariable.uid);

                rolesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        uidRef.child("linked rfid").setValue(GlobalVariable.rfidTag);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                DatabaseReference ticketRef = FirebaseDatabase.getInstance().getReference("RFID master/"+rfidInput.getText().toString()+"/ticket count");

                ticketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String countStr = dataSnapshot.child("ticket count").getValue(String.class);
                        //rolesRef.child("linked email").setValue(GlobalVariable.email);
                        countTicket.setText(countStr);

                        GlobalVariable.rfidTagLinked = true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
                String inputString = GlobalVariable.email;
                String emailClean = inputString.replaceAll("[@.#$\\[\\]]", "");
                DatabaseReference emailRef = FirebaseDatabase.getInstance().getReference("Email linked/"+emailClean);

                emailRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        emailRef.child("linked rfid").setValue(GlobalVariable.rfidTag);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


                Log.e("Account", "linkLinked: "+GlobalVariable.rfidTagLinked);

                rfidInputViewBtnToggle = false;

                GlobalVariable.rfidTagLinked = true;

                Log.e("Account", "3Linked: "+GlobalVariable.rfidTagLinked);
                rfidInput.setFocusable(false);
                rfidInput.setFocusableInTouchMode(false);
                tagidLabel.setText("Rfid tag linked");
                linkRfidInputBtn.setText("Unlink");
                tagidLabel.setTextColor(0xFF00FF00);
                rfidInput.setText(GlobalVariable.rfidTag);
                rfidInputLayout.setVisibility(View.GONE);
                linkRfidInputBtn.setVisibility(View.GONE);

            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing - dialog will be dismissed

            }
        });




        AlertDialog dialog = builder.create();

        linkRfidImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if(!rfidInputViewBtnToggle){

                    rfidInputViewBtnToggle = true;
                    rfidInputLayout.setVisibility(View.VISIBLE);
                    linkRfidInputBtn.setVisibility(View.VISIBLE);
                }
                else {
                    rfidInputViewBtnToggle = false;
                    rfidInputLayout.setVisibility(View.GONE);
                    linkRfidInputBtn.setVisibility(View.GONE);
                }

            }
        });




        rfidInput.setFilters(filters);
        rfidInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // Do something when new line is entered
                    dialog.show();
                    return true;
                }
                return false;
            }

        });


        AlertDialog.Builder editRfidBuidler = new AlertDialog.Builder(getContext());
        editRfidBuidler.setTitle("Are you sure you want to unlink this rfid to your account?");
        editRfidBuidler.setMessage("Unlinking this rfid tag now means you can now link the previous tag to other accounts.");
        editRfidBuidler.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path = "Rfid read/"+GlobalVariable.rfidTag;
                DatabaseReference rolesRef = FirebaseDatabase.getInstance().getReference(path);

                rolesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.e("snapshot count", "Bus ++ "+dataSnapshot.getValue());
                        rolesRef.child("linked email").removeValue();
                        rolesRef.child("linked uid").removeValue();
                        GlobalVariable.rfidTagLinked = false;
                        GlobalVariable.rfidTag = "";
                        //rolesRef.child("linked email").setValue(GlobalVariable.email);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


                DatabaseReference uidRef = FirebaseDatabase.getInstance().getReference("Users/"+GlobalVariable.uid);

                rolesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.e("snapshot count", "Bus ++ "+dataSnapshot.getValue());
                        uidRef.child("linked rfid").removeValue();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });



                String inputString = GlobalVariable.email;
                String emailClean = inputString.replaceAll("[@.#$\\[\\]]", "");
                DatabaseReference emailRef = FirebaseDatabase.getInstance().getReference("Email linked/"+emailClean);

                emailRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        emailRef.child("linked rfid").removeValue();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                GlobalVariable.rfidTagLinked = false;

                Log.e("Account", "3Linked: "+GlobalVariable.rfidTagLinked);
                rfidInput.setFocusable(true);
                rfidInput.setFocusableInTouchMode(true);
                tagidLabel.setText("Rfid tag id not linked");
                linkRfidInputBtn.setText("Link");
                tagidLabel.setTextColor(0xFFFF0000);
                rfidInput.setText("");
                rfidInputViewBtnToggle = true;
                rfidInputLayout.setVisibility(View.VISIBLE);
                linkRfidInputBtn.setVisibility(View.VISIBLE);

            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing - dialog will be dismissed

            }
        });




        AlertDialog removeTagDialog = editRfidBuidler.create();


        linkRfidInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!GlobalVariable.rfidTagLinked){
                    GlobalVariable.rfidTag = rfidInput.getText().toString();
                    String path = "RFID Global/"+rfidInput.getText().toString();
                    DatabaseReference rolesRef = FirebaseDatabase.getInstance().getReference(path);

                    rolesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Child node exists
                                String path = "RFID master/"+rfidInput.getText().toString();
                                DatabaseReference rolesRef = FirebaseDatabase.getInstance().getReference(path);

                                rolesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // Child node exist
                                            Toast.makeText(getContext(), "Rfid is already linked", Toast.LENGTH_SHORT).show();
                                            return;

                                        } else {

                                            dialog.show();
                                        }

                                        return;

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }

                                });
                                if(dataSnapshot.child(rfidInput.getText().toString()).exists()){
                                    dialog.show();
                                }
                                else {
                                    return;
                                }

                                Log.e("TAG", "datasnap exists: ");


                            } else {
                                // Child node does not exist
                                Toast.makeText(getContext(), "Rfid input does not exist", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            return;

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                }
                else{
                    removeTagDialog.show();
                }


            }
        });





        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);

            }
        });





        setSchedButton = view.findViewById(R.id.setSchedule);

        setSchedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdminSetSchedule.class);
                startActivity(intent);
            }


        });



        AlertDialog.Builder addTicketBuilder = new AlertDialog.Builder(getContext());
        addTicketBuilder.setTitle("Are you sure you want to link this rfid to your account?");
        addTicketBuilder.setMessage("Linking this rfid tag now means you cannot link this tag to other accounts.");
        addTicketBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path = "Users/"+GlobalVariable.uid;
                DatabaseReference rolesRef = FirebaseDatabase.getInstance().getReference(path);

                rolesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.e("snapshot count", "Bus ++ "+dataSnapshot.getValue());
                        rolesRef.child("rfid tag").setValue(rfidInput.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing - dialog will be dismissed

            }
        });




        AlertDialog addTicketBuilderDialog = addTicketBuilder.create();
/*
        addPassengerTicketInput.setFilters(filters);
        addPassengerTicketInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // Do something when new line is entered
                    addTicketBuilderDialog.show();
                    return true;
                }
                return false;
            }

        });


 */


        return view;
    }

    public void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(getContext())
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(getContext())
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        DatabaseReference mactanRef = FirebaseDatabase.getInstance().getReference("Mactan Device/GPS");
                                        mactanRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                double latitude = locationResult.getLocations().get(index).getLatitude();
                                                double longitude = locationResult.getLocations().get(index).getLongitude();
                                                mactanRef.child("f_latitude").setValue(latitude);
                                                mactanRef.child("f_longitude").setValue(longitude);


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }

                                        });
                                        }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(getContext(), "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult((Activity) getContext(), 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {

            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }
}