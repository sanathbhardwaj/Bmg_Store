package com.digital.dhanbadbasket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.UUID;

public class PaymentChooseActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    ImageButton backarrow;
    RadioButton paytm, phonepe, gpay, amazonpay, cod;
    RelativeLayout paytm_rl, phonepe_rl, gpay_rl, amazonpay_rl, cod_rl;
    Button payment;

    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_choose);

        backarrow = findViewById(R.id.backarrow);
        paytm = findViewById(R.id.paytm);
        phonepe = findViewById(R.id.phonepe);
        gpay = findViewById(R.id.gpay);
        amazonpay = findViewById(R.id.amazonpay);
        cod = findViewById(R.id.cod);
        paytm_rl = findViewById(R.id.paytm_rl);
        phonepe_rl = findViewById(R.id.phone_pe_rl);
        gpay_rl = findViewById(R.id.gpay_rl);
        amazonpay_rl = findViewById(R.id.amazonpay_rl);
        cod_rl = findViewById(R.id.cod_rl);
        payment = findViewById(R.id.button);


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", "kumarshailendra4825@okicici")
                                .appendQueryParameter("pn", "Bmg Store")
                                .appendQueryParameter("mc", "your-merchant-code")
                                .appendQueryParameter("tr", "klo9587625986ko13")
                                .appendQueryParameter("tn", "your-transaction-note")
                                .appendQueryParameter("am", "1")
                                .appendQueryParameter("cu", "INR")
                                .appendQueryParameter("url", "your-transaction-url")
                                .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
                startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
            }
        });

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paytm.setChecked(true);
                phonepe.setChecked(false);
                gpay.setChecked(false);
                amazonpay.setChecked(false);
                cod.setChecked(false);
                payment.setEnabled(true);
                payment.setBackgroundResource(R.drawable.button_border_radius);
            }
        });
        phonepe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytm.setChecked(false);
                phonepe.setChecked(true);
                gpay.setChecked(false);
                amazonpay.setChecked(false);
                cod.setChecked(false);
                payment.setEnabled(true);
                payment.setBackgroundResource(R.drawable.button_border_radius);

            }
        });
        gpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytm.setChecked(false);
                phonepe.setChecked(false);
                gpay.setChecked(true);
                amazonpay.setChecked(false);
                cod.setChecked(false);
                payment.setEnabled(true);
                payment.setBackgroundResource(R.drawable.button_border_radius);

            }
        });
        amazonpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytm.setChecked(false);
                phonepe.setChecked(false);
                gpay.setChecked(false);
                amazonpay.setChecked(true);
                cod.setChecked(false);
                payment.setEnabled(true);
                payment.setBackgroundResource(R.drawable.button_border_radius);

            }
        });
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paytm.setChecked(false);
                phonepe.setChecked(false);
                gpay.setChecked(false);
                amazonpay.setChecked(false);
                cod.setChecked(true);
                payment.setEnabled(true);
                payment.setBackgroundResource(R.drawable.button_border_radius);

            }
        });
        paytm_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paytm.setChecked(true);
                phonepe.setChecked(false);
                gpay.setChecked(false);
                amazonpay.setChecked(false);
                cod.setChecked(false);
                payment.setEnabled(true);
                payment.setBackgroundResource(R.drawable.button_border_radius);
            }
        });
        phonepe_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paytm.setChecked(false);
                phonepe.setChecked(true);
                gpay.setChecked(false);
                amazonpay.setChecked(false);
                cod.setChecked(false);
                payment.setEnabled(true);
                payment.setBackgroundResource(R.drawable.button_border_radius);
            }
        });
        gpay_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paytm.setChecked(false);
                phonepe.setChecked(false);
                gpay.setChecked(true);
                amazonpay.setChecked(false);
                cod.setChecked(false);
                payment.setEnabled(true);
                payment.setBackgroundResource(R.drawable.button_border_radius);
            }
        });
        amazonpay_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paytm.setChecked(false);
                phonepe.setChecked(false);
                gpay.setChecked(false);
                amazonpay.setChecked(true);
                cod.setChecked(false);
                payment.setEnabled(true);
                payment.setBackgroundResource(R.drawable.button_border_radius);
            }
        });
        cod_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paytm.setChecked(false);
                phonepe.setChecked(false);
                gpay.setChecked(false);
                amazonpay.setChecked(false);
                cod.setChecked(true);
                payment.setEnabled(true);
                payment.setBackgroundResource(R.drawable.button_border_radius);
            }
        });
        paytm.setOnCheckedChangeListener(this);
        phonepe.setOnCheckedChangeListener(this);
        gpay.setOnCheckedChangeListener(this);
        amazonpay.setOnCheckedChangeListener(this);
        cod.setOnCheckedChangeListener(this);
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            switch (buttonView.getId()) {
                case R.id.paytm:
                    System.out.println("paytm");
                    break;
                case R.id.phonepe:
                    System.out.println("phonepe");
                    break;
                case R.id.gpay:
                    System.out.println("gpay");
                    break;
                case R.id.amazonpay:
                    System.out.println("amazonpay");
                    break;
                case R.id.cod:
                    System.out.println("cod");
                    break;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_PAY_REQUEST_CODE) {
            // Process based on the data in response.
            Toast.makeText(this, data.getStringExtra("Status"), Toast.LENGTH_SHORT).show();
        }
    }
}