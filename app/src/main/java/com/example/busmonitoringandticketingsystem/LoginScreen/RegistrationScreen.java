package com.example.busmonitoringandticketingsystem.LoginScreen;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busmonitoringandticketingsystem.GlobalVariable;
import com.example.busmonitoringandticketingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationScreen extends AppCompatActivity {

    String[] roleItems = {"Choose role","Admin", "Passenger", "Driver"};

    TextInputEditText editTextEmail, editTextPassword,editName;
    Button buttonRegister;
    FirebaseAuth mAuth;
    TextView textView;
    Spinner roleSpinner;
    String roleSelected = "Choose role";
    String routeSelected = "Choose route";

    String email,password,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        ProgressBar loading = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email_address);
        editTextPassword = findViewById(R.id.password);
        editName = findViewById(R.id.user_name);
        buttonRegister = findViewById(R.id.btn_register);
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        roleSpinner = findViewById(R.id.roleSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roleItems);
        roleSpinner.setAdapter(adapter2);


        //String path = "Users/"+roleSelected;
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users/"+GlobalVariable.uid);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Object roleObject = dataSnapshot.child("role").getValue();

                if (roleObject instanceof String) {
                    String role = (String) roleObject;
                    if (role.equals("Admin")) {
                        roleSpinner.setVisibility(View.VISIBLE);
                    } else {
                        roleSpinner.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roleSelected = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        loading.setVisibility(View.GONE);



        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);
                email = String.valueOf(editTextEmail.getText().toString());
                password = String.valueOf(editTextPassword.getText().toString());
                name = String.valueOf(editName.getText().toString());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegistrationScreen.this,"Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegistrationScreen.this,"Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(RegistrationScreen.this,"Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(roleSelected == "Choose role") {
                    Toast.makeText(RegistrationScreen.this, "Choose a valid role", Toast.LENGTH_SHORT).show();

                    if(routeSelected == "Choose route") {
                        Toast.makeText(RegistrationScreen.this, "Choose a valid route", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    return;
                }

                Log.e("Route selected", ""+routeSelected);


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                Log.e(TAG, "REgistering email");
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Add role to user
                                    setUserRole(user, roleSelected);
                                    Log.e(TAG, "REgister email success");
                                    Toast.makeText(RegistrationScreen.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Log.e(TAG, "REgister email failed");

                                    Toast.makeText(RegistrationScreen.this, task.getException().toString(),
                                            Toast.LENGTH_SHORT).show();

                                }

                            }

                        });


            }



        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // finish the current activity
    }

    private void setUserRole(FirebaseUser user, String role) {
        // Get reference to the database node where roles are stored

        String path = "Users/"+user.getUid();
        //String path = "Users/"+roleSelected;
        DatabaseReference rolesRef = FirebaseDatabase.getInstance().getReference(path);


        rolesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("snapshot count", "Bus ++ "+dataSnapshot.getValue());

                rolesRef.child("email").setValue(email);
                rolesRef.child("password").setValue(password);
                rolesRef.child("role").setValue(roleSelected);
                rolesRef.child("Name").setValue(name);
                rolesRef.child("uid").setValue(user.getUid());
                rolesRef.child("route").setValue(routeSelected);
                rolesRef.child("rfid tag").setValue("");
                rolesRef.child("ticket count").setValue("0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

}