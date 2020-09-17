package com.digital.dhanbadbasket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.digital.dhanbadbasket.Adapters.PlacesAutoCompleteAdapter;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class SearchLocationActivity extends AppCompatActivity implements PlacesAutoCompleteAdapter.ClickListener {
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    LinearLayout linearLayout;
    RelativeLayout clear, backarrow;
    EditText place_search;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        backarrow = findViewById(R.id.backarrow);
        clear = findViewById(R.id.clear);
        place_search = findViewById(R.id.place_search);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place_search.setText("");

            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        linearLayout = (LinearLayout)findViewById(R.id.cur_location);
        Places.initialize(this, getResources().getString(R.string.google_maps_key));
        progressBar = findViewById(R.id.progressbar);

        recyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);
        ((EditText) findViewById(R.id.place_search)).addTextChangedListener(filterTextWatcher);

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(SearchLocationActivity.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                Intent intent = new Intent(SearchLocationActivity.this, SelectLocationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                                if (response.isPermanentlyDenied()) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchLocationActivity.this);
                                    builder.setTitle("Permission is Required")
                                            .setMessage("Permission is not granted. You need to go to settings and allow the required permission")
                                            .setNegativeButton("Cancel", null)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent();
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));

                                                }
                                            })
                                            .show();
                                }

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                token.continuePermissionRequest();

                            }
                        })
                        .check();

            }
        });
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                progressBar.setVisibility(View.VISIBLE);
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {recyclerView.setVisibility(View.VISIBLE);}
            } else {
                progressBar.setVisibility(View.GONE);
                if (recyclerView.getVisibility() == View.VISIBLE) {recyclerView.setVisibility(View.GONE);}
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
    };

    @Override
    public void click(Place place) {

        Intent intent = new Intent(SearchLocationActivity.this, SelectLocationActivity.class);
        intent.putExtra("latitude", String.valueOf(place.getLatLng().latitude));
        intent.putExtra("longitude", String.valueOf(place.getLatLng().longitude));
        startActivity(intent);
        //Toast.makeText(LocationEnable.this, place.getName()+", Hello"+place.getLatLng().latitude+place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
    }
}