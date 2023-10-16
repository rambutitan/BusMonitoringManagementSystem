package com.example.busmonitoringandticketingsystem;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocationService extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Consolacion Device/GPS");
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Get the location manager
        locationManager = (LocationManager) getApplication().getSystemService(getApplication().LOCATION_SERVICE);

        // Create a location listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Update your app's UI or perform other tasks here
                /*
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);


                String yearstr = Integer.toString(year);
                String daystr = Integer.toString(dayofmonth);
                String monthstr = Integer.toString(month);

                 */

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double lati = location.getLatitude();
                        double longi = location.getLongitude();
                        databaseReference.child("f_latitude").setValue(lati);
                        databaseReference.child("f_longitude").setValue(longi);
                        Log.e( "onLocationChanged: ", "latitude: "+lati+" longitude: "+longi);

                        /*
                        ZoneId zoneId = null;
                        LocalTime currentTime = null;
                        String timeString = "";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            zoneId = ZoneId.of("Asia/Manila");
                            currentTime = LocalTime.now(zoneId);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                            timeString = currentTime.format(formatter);
                        }
                        databaseReference.child("time updated").setValue(timeString);

                         */

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        // Request location updates
        if (ActivityCompat.checkSelfPermission(getApplication(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
/*
        // Create a notification and start the service in the foreground
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle("Location Service")
                .setContentText("Detecting location even when screen is locked")
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        startForeground(1, builder.build());

 */

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop location updates
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
