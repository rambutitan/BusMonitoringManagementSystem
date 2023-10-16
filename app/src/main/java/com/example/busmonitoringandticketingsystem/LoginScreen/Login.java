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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busmonitoringandticketingsystem.GlobalVariable;
import com.example.busmonitoringandticketingsystem.HomeActivity;
import com.example.busmonitoringandticketingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail;
    TextInputEditText editTextPassword;
    Button buttonLogin;
    TextView textView;
    TextView roleLogin;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.updateLocation);
        textView = findViewById(R.id.register_Now);
        roleLogin = findViewById(R.id.role_login);

        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        ProgressBar loading = findViewById(R.id.progressBar);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RegistrationScreen.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){

                loading.setVisibility(View.VISIBLE);
                String email,password;
                email = String.valueOf(editTextEmail.getText().toString());
                password = String.valueOf(editTextPassword.getText().toString());

                Log.e(TAG, email+password);
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this,"Enter email", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this,"Enter password", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                loading.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.e("Success", "onComplete: ");
                                    if(GlobalVariable.adminModeOn){
                                        Snackbar.make(view, "Admin Login Successful", Snackbar.LENGTH_LONG).show();

                                        Toast.makeText(getApplicationContext(), "Admin Login Successful", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    GlobalVariable.adminModeOn = true;
                                    GlobalVariable.email = email;
                                    GlobalVariable.password = password;
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String path = "Users/"+user.getUid();

                                    if(GlobalVariable.role == "Admin"){
                                        Toast.makeText(getApplicationContext(), "Admin Login Successful", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    DatabaseReference rolesRef = FirebaseDatabase.getInstance().getReference(path);
                                    rolesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            GlobalVariable.email = email;
                                            GlobalVariable.password = password;
                                            GlobalVariable.name = dataSnapshot.child("Name").getValue(String.class);
                                            GlobalVariable.uid = dataSnapshot.child("uid").getValue(String.class);
                                            GlobalVariable.role = dataSnapshot.child("role").getValue(String.class);
                                            GlobalVariable.driverRoute = dataSnapshot.child("route").getValue(String.class);
                                            GlobalVariable.ticketCount = dataSnapshot.child("ticket count").getValue(String.class);
                                            if(dataSnapshot.child("linked rfid").exists()){
                                                GlobalVariable.rfidTagLinked = true;
                                                GlobalVariable.rfidTag = dataSnapshot.child("linked rfid").getValue(String.class);
                                            }
                                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }

                                    });

                                } else {

                                    Log.e("Success", "failed: "+task);
                                    // If sign in fails, display a message to the user.
                                    //
                                    Toast.makeText(Login.this, task.getException().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    loading.setVisibility(View.GONE);
                                }

                            }
                        });

            }

        });


    }
}