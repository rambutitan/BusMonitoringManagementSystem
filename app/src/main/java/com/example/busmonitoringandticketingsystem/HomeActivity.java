package com.example.busmonitoringandticketingsystem;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.busmonitoringandticketingsystem.ui.account.AccountFragment;
import com.example.busmonitoringandticketingsystem.ui.home.ScheduleFragment;
import com.example.busmonitoringandticketingsystem.ui.location.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView navView;
    ScheduleFragment scheduleFragment = new ScheduleFragment();
    MapFragment mapFragment = new MapFragment();
    AccountFragment accountFragment = new AccountFragment();
    boolean serviceStarted = false;

    @Override

    protected void onStart() {
        super.onStart();
        Intent serviceIntent = new Intent(this, LocationListenerService.class);
        startService(serviceIntent);

        Log.e("KAMOTE", "onStart: ");
        //LocationServiceIntent intent = new Intent(this, LocationService.class);
        //bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

  @Override
  protected void onStop() {
      super.onStop();
/*
      if (isBound) {
          unbindService(serviceConnection);
          isBound = false;
      }

 */
  }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        Log.e("onCreateView", "Global variables"+GlobalVariable.name+GlobalVariable.email+GlobalVariable.role+GlobalVariable.uid);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "location_channel",
                    "Location Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        navView = findViewById(R.id.nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, scheduleFragment).commit();
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_schedule:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, scheduleFragment).commit();

                        return true;
                    case R.id.navigation_location:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
                        return true;

                    case R.id.navigation_account:
                        //makeNotification();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, accountFragment).commit();

                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void makeNotification(){
        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), channelID);
        builder.setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Busayo Notification")
                .setContentText("The Cordova Bus has arrived!")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(), ArrivalNotification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", "SAKAY KO NOY");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel((channelID));
            if(notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID,
                        "The Mactan Bus sheesh", importance);

                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0, builder.build());

    }
    /*
    private LocationService locationService;
    private boolean isBound = false;

    // Bind to the location service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            locationService = binder.getService();
            isBound = true;

            // Start location updates
            locationService.startLocationUpdates();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

     */
    private static final int TIME_INTERVAL = 2000; // Time interval for back button press in milliseconds
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}