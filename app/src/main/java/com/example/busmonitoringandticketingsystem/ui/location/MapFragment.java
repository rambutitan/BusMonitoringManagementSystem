package com.example.busmonitoringandticketingsystem.ui.location;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.busmonitoringandticketingsystem.GlobalVariable;
import com.example.busmonitoringandticketingsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Timer;


public class MapFragment extends Fragment  {
    private MapView mapView;
    String[] my_spinner_items = {"Mactan", "Cordova", "Consolacion", "Liloan"};

    private LocationManager locationManager;
    private double viewMapLat, viewMapLong;
    private boolean isMapLoaded = false;
    private Handler handler;
    private Timer timer;
    private  String pathToDatabase = "";
    private ToggleButton mactanBtn;
    private ToggleButton consolacionBtn;
    private ToggleButton cordovaBtn;
    private ToggleButton liloanBtn;

    String routeSelected = "";

    Drawable busIcon;
    TableLayout tableLayout;
    IMapController mapController;
    Bitmap iconBitmap;

    Marker marker;
    boolean buttonPressed = true;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_map, container, false);

        tableLayout = view.findViewById(R.id.table_layout);
        mapView = new MapView(getContext());
        mapView.setTileSource(TileSourceFactory.MAPNIK);




        if(isMapLoaded==false) {
            Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
            isMapLoaded =true;
        }

        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(16.0);
        mapView.getOverlays().clear();
        marker = new Marker(mapView);
        Spinner mySpinner = view.findViewById(R.id.route_spinner);

        mactanBtn = view.findViewById(R.id.mactanBtn);
        cordovaBtn = view.findViewById(R.id.cordovaBtn);
        liloanBtn = view.findViewById(R.id.liloanBtn);
        consolacionBtn = view.findViewById(R.id.consolacionBtn);
        TextView driverLabel = view.findViewById(R.id.driver_label);
        if(GlobalVariable.role == "Driver"){
            mySpinner.setVisibility(View.GONE);
            tableLayout.setVisibility(View.GONE);
            driverLabel.setVisibility(View.VISIBLE);
            RelativeLayout driverLabelLayout = view.findViewById(R.id.drive_label_layout);
            driverLabelLayout.setElevation(3f);

            DatabaseReference gpsRef;

            gpsRef = FirebaseDatabase.getInstance().getReference(GlobalVariable.driverRoute+" Device/GPS");
            gpsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot gpsDataSnapshot) {
                    mapView.getOverlays().remove(marker);
                    double gpsLat = gpsDataSnapshot.child("f_latitude").getValue(Double.class);
                    double gpsLong = gpsDataSnapshot.child("f_longitude").getValue(Double.class);

                    viewMapLat = gpsLat;
                    viewMapLong = gpsLong;
                    marker.setPosition(new GeoPoint(viewMapLat, viewMapLong));
                    mapController.setCenter(new GeoPoint(viewMapLat, viewMapLong));

                    marker.setIcon(busIcon);
                    mapView.getOverlays().add(marker);
                    mapView.invalidate();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        mySpinner.setVisibility(View.VISIBLE);
        tableLayout.setVisibility(View.GONE);


            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, my_spinner_items);
            mySpinner.setAdapter(adapter);

            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    routeSelected= (String) parent.getItemAtPosition(position);
                    Log.e("Route Selected", "onItemSelected: "+routeSelected );
                    databaseCall(routeSelected);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        RelativeLayout relativeLayout = view.findViewById(R.id.relative_layout);
        RelativeLayout tableRelative = view.findViewById(R.id.table_relative_layout);
        tableRelative.setElevation(4f);

        RelativeLayout spinnerRelative = view.findViewById(R.id.spinner_relative);
            spinnerRelative.setElevation(3f);



        mapView.setElevation(2f);
        mapView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        relativeLayout.addView(mapView);


        return view;
    }

    public void databaseCall(String pathToDB){
        DatabaseReference gpsRef;

        gpsRef = FirebaseDatabase.getInstance().getReference(pathToDB+" Device/GPS");
        gpsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot gpsDataSnapshot) {
                mapView.getOverlays().remove(marker);
                double gpsLat = gpsDataSnapshot.child("f_latitude").getValue(Double.class);
                double gpsLong = gpsDataSnapshot.child("f_longitude").getValue(Double.class);

                viewMapLat = gpsLat;
                viewMapLong = gpsLong;
                marker.setPosition(new GeoPoint(viewMapLat, viewMapLong));
                mapController.setCenter(new GeoPoint(viewMapLat, viewMapLong));

                marker.setIcon(busIcon);
                mapView.getOverlays().add(marker);
                mapView.invalidate();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isMapLoaded = false;
    }

}

