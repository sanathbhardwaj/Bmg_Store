package com.digital.dhanbadbasket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ChooseLocationActivity extends AppCompatActivity {

    ImageView back;
    RelativeLayout current_location;
    RelativeLayout other_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        back = findViewById(R.id.back);
        current_location = findViewById(R.id.current_location);
        other_location = findViewById(R.id.other_location);

        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 1;
                permissions(i);
            }
        });
        other_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 2;
                permissions(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void permissions(final int i) {
        Dexter.withActivity(ChooseLocationActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (i == 1) {
                            Intent intent = new Intent(ChooseLocationActivity.this, SelectLocationActivity.class);
                            startActivity(intent);
                        }
                        if (i == 2) {
                            Intent intent = new Intent(ChooseLocationActivity.this, SearchLocationActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        if (response.isPermanentlyDenied()) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLocationActivity.this);
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
}
