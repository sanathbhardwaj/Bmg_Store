package com.digital.dhanbadbasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skyfishjy.library.RippleBackground;

import java.util.List;
import java.util.Locale;

public class SelectLocationActivity extends AppCompatActivity implements GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback, TextWatcher {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private View mapView;
    TextView address;
    DatabaseReference mRef;
    String uid = FirebaseAuth.getInstance().getUid();
//    private Button btnFind, select_location;
//    private RippleBackground rippleBg;
//    TextView current_Sub_Locality, current_address;
//    ImageButton backarrow;
//    EditText address_line_1, address_line_2;
//    private DatabaseReference mDatabase;
//    private final float DEFAULT_ZOOM = 18;
//    BottomSheetDialog bottomSheetDialog;
//    TextView locality, city, state, country;
//    ProgressBar progress_circular;
//
    ImageButton form_opener, backarrow, imgMyLocation;
    RelativeLayout form, save;

    TextInputEditText landmark, street, apartment, house_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_location);
        form_opener = findViewById(R.id.form_opener);
        form = findViewById(R.id.form);
        backarrow = findViewById(R.id.backarrow);
        imgMyLocation = findViewById(R.id.imgMyLocation);
        address = findViewById(R.id.address);
        save = findViewById(R.id.save);

        house_no = findViewById(R.id.house_no);
        apartment = findViewById(R.id.apartment);
        street = findViewById(R.id.street);
        landmark = findViewById(R.id.landmark);

        apartment.addTextChangedListener(this);

        mRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("address");

        form_opener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form_opener.setVisibility(View.GONE);
                form.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String position = "1";
                String house = house_no.getText().toString();
                String apart = apartment.getText().toString();
                String stre = street.getText().toString();
                String land = landmark.getText().toString();
                LatLng currentMarkerLocation = mMap.getCameraPosition().target;
                String area = getSubLocalityFromLatLng(currentMarkerLocation);
                String area2 = getLocalityFromLatLng(currentMarkerLocation);

                mRef.child(position).child("full_add").setValue(house+" "+apart+" "+stre+" "+land+" "+getAddressFromLatLng(currentMarkerLocation));
                mRef.child(position).child("sub_locality").setValue(area);
                mRef.child(position).child("locality").setValue(area2);
                Intent intent = new Intent(SelectLocationActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
                getDeviceLocation();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SelectLocationActivity.this);
    }

    @Override
    public void onCameraIdle() {
        LatLng currentMarkerLocation = mMap.getCameraPosition().target;
        address.setText(getAddressFromLatLng(currentMarkerLocation));
    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {
        address.setText("Locating..");
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
//            Toast.makeText(this, "The user gestured on the map.",
//                    Toast.LENGTH_SHORT).show();
            form_opener.setVisibility(View.VISIBLE);
            form.setVisibility(View.GONE);
        } else if (i == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
//            Toast.makeText(this, "The user tapped something on the map.",
//                    Toast.LENGTH_SHORT).show();
        } else if (i == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
//            Toast.makeText(this, "The app moved the camera.",
//                    Toast.LENGTH_SHORT).show();
        }

    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        getDeviceLocation();

        mMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(23.8051, 86.427793), 14));

        //check if gps is enabled or not and then request user to enable it
        check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 14));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(1000);
                                locationRequest.setFastestInterval(500);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 14));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(SelectLocationActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

        private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            String address = addresses.get(0).getAddressLine(0);
//            current_location_tv.setText(address);
            return addresses.get(0).getAddressLine(0);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public void check(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(SelectLocationActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(SelectLocationActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(SelectLocationActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(SelectLocationActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        save.setEnabled(false);
        save.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count<1){
            save.setEnabled(false);
            save.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
        }
        else {
            save.setEnabled(true);
            save.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

        private String getSubLocalityFromLatLng(LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            String address = addresses.get(0).getAddressLine(0);
//            current_location_tv.setText(address);
            return addresses.get(0).getSubLocality();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getLocalityFromLatLng(LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            String address = addresses.get(0).getAddressLine(0);
//            current_location_tv.setText(address);
            return addresses.get(0).getLocality();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}




























//        btnFind = findViewById(R.id.btn_find);
//        current_Sub_Locality = findViewById(R.id.current_Sub_Locality);
//        current_address = findViewById(R.id.current_address);
//        rippleBg = findViewById(R.id.ripple_bg);
//        backarrow = findViewById(R.id.backarrow);
//        bottomSheetDialog = new BottomSheetDialog(this);
//        progress_circular = findViewById(R.id.progress_circular);
//
//        ImageView imgMyLocation;
//        imgMyLocation = findViewById(R.id.imgMyLocation);
//

//        save = findViewById(R.id.save);
//

//
//

//        imgMyLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getDeviceLocation();
//            }
//        });
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        mapView = mapFragment.getView();
//
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SelectLocationActivity.this);
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rippleBg.startRippleAnimation();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(SelectLocationActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//
//                    }
//                }, 3000);
//
//            }
//        });
//    }
//
//    @Override
//    public void onCameraMoveStarted(int reason) {
//

//    }
//
//    @Override
//    public void onCameraMove() {
//        current_Sub_Locality.setText("locating...");
//    }
//
//    @Override
//    public void onCameraMoveCanceled() {
////        Toast.makeText(this, "Camera movement canceled.",
////                Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onCameraIdle() {
//
//        LatLng currentMarkerLocation = mMap.getCameraPosition().target;
//        String SubLocality = getSubLocalityFromLatLng(currentMarkerLocation);
//        String city = getLocalityFromLatLng(currentMarkerLocation);
//        String state = getStateFromLatLng(currentMarkerLocation);
//        String country = getCountryFromLatLng(currentMarkerLocation);
//        String Address = SubLocality+" "+city+", "+state+", "+country;
//        current_Sub_Locality.setText(SubLocality);
//        current_address.setText(Address);
////        String you = currentMarkerLocation.toString();
////        current_location_tv.setText(you);
//
//    }

//
//    private String getStateFromLatLng(LatLng latLng) {
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
////            String address = addresses.get(0).getAddressLine(0);
////            current_location_tv.setText(address);
//            return addresses.get(0).getAdminArea();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    private String getCountryFromLatLng(LatLng latLng) {
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
////            String address = addresses.get(0).getAddressLine(0);
////            current_location_tv.setText(address);
//            return addresses.get(0).getCountryName();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//
//    private String getAddressFromLatLng(LatLng latLng) {
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
////            String address = addresses.get(0).getAddressLine(0);
////            current_location_tv.setText(address);
//            return addresses.get(0).getAddressLine(0);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
//        mMap.setOnCameraIdleListener(this);
//        mMap.setOnCameraMoveStartedListener(this);
//        mMap.setOnCameraMoveListener(this);
//        mMap.setOnCameraMoveCanceledListener(this);
//
//        String latitude = getIntent().getStringExtra("latitude");
//        String longitude = getIntent().getStringExtra("longitude");
//
//        if (latitude==null&&longitude==null){
//
//
//        }
//        else {
//            // Show Sydney on the map.
//            double lat = Double.parseDouble(latitude);
//            double lon = Double.parseDouble(longitude);
//
//            mMap.moveCamera(CameraUpdateFactory
//                    .newLatLngZoom(new LatLng(lat, lon), 14));
//            progress_circular.setVisibility(View.GONE);
//
//        }
//
//        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
//            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//            layoutParams.setMargins(0, 0, 50, 50);
//        }
//
//        //check if gps is enabled or not and then request user to enable it
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
//
//        SettingsClient settingsClient = LocationServices.getSettingsClient(SelectLocationActivity.this);
//        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
//
//        task.addOnSuccessListener(SelectLocationActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
//            @Override
//            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//
//                String latitude = getIntent().getStringExtra("latitude");
//                String longitude = getIntent().getStringExtra("longitude");
//
//                if (latitude!=null && longitude!=null){
//                    getDeviceLocation();
//                }
//                else {
//                    getDeviceLocation();
//
//                }
//            }
//        });
//
//        task.addOnFailureListener(SelectLocationActivity.this, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                if (e instanceof ResolvableApiException) {
//                    ResolvableApiException resolvable = (ResolvableApiException) e;
//                    try {
//                        resolvable.startResolutionForResult(SelectLocationActivity.this, 51);
//                    } catch (IntentSender.SendIntentException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        String latitude = getIntent().getStringExtra("latitude");
//        String longitude = getIntent().getStringExtra("longitude");
//
//        if (requestCode == 51) {
//            if (resultCode == RESULT_OK&&latitude!=null&&longitude!=null) {
//                getDeviceLocation();
//            }
//            else {
//                getDeviceLocation();
//            }
//        }
//    }
//    @SuppressLint("MissingPermission")
//    private void getDeviceLocation() {
//        mFusedLocationProviderClient.getLastLocation()
//                .addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            mLastKnownLocation = task.getResult();
//                            if (mLastKnownLocation != null) {
//
//                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 14));
//                                progress_circular.setVisibility(View.GONE);
//
//
//                            } else {
//                                final LocationRequest locationRequest = LocationRequest.create();
//                                locationRequest.setInterval(1000);
//                                locationRequest.setFastestInterval(500);
//                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                                locationCallback = new LocationCallback() {
//                                    @Override
//                                    public void onLocationResult(LocationResult locationResult) {
//                                        super.onLocationResult(locationResult);
//                                        if (locationResult == null) {
//                                            return;
//                                        }
//                                        mLastKnownLocation = locationResult.getLastLocation();
//                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 14));
//                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
//                                        progress_circular.setVisibility(View.GONE);
//                                    }
//                                };
//                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
//
//                            }
//                        } else {
//                            Toast.makeText(SelectLocationActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//}
//
