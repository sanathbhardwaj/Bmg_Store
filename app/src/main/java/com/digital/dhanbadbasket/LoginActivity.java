package com.digital.dhanbadbasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bigbangbutton.editcodeview.EditCodeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout phone_entry_rl, phone_verify_rl;

    EditText ph;
    private CountryCodePicker c;
    Button next, skip;
    String phoneNumber;
    private FirebaseAuth mAuth;

    private String verificationId;
    ProgressBar progressBar, phone_entry_progress;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    Button btn_verify_otp;
    private EditCodeView etOtp;
    EditText et_phone_number;

    ImageView backarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone_entry_rl = findViewById(R.id.phone_entry_rl);
        phone_verify_rl = findViewById(R.id.phone_verify_rl);

        next = findViewById(R.id.nextButton);
        ph = findViewById(R.id.phno);
        c = findViewById(R.id.ccp);
        skip = findViewById(R.id.skip);
        mAuth = FirebaseAuth.getInstance();
        String start = getIntent().getStringExtra("start");
        backarrow = findViewById(R.id.backarrow);

        progressBar = findViewById(R.id.progress_bar);
        phone_entry_progress = findViewById(R.id.phone_entry_progress);
        et_phone_number = findViewById(R.id.et_phone_number);
        btn_verify_otp = findViewById(R.id.btn_verify_otp);
        etOtp = findViewById(R.id.et_otp);
        progressBar.setVisibility(View.VISIBLE);
        btn_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etOtp.getCode();
                if (code.isEmpty() || code.length() < 6) {
                    etOtp.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

//        if (start==null){
//            skip.setVisibility(View.INVISIBLE);
//        }
//        else {
//            skip.setVisibility(View.VISIBLE);
//        }

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_entry_progress.setVisibility(View.VISIBLE);
//                updateUI(currentUser);

                mAuth.signInAnonymously()
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    phone_entry_progress.setVisibility(View.GONE);
                                    Intent intent = new Intent(LoginActivity.this, ChooseLocationActivity.class);
                                    startActivity(intent);
                                } else {
//                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_entry_progress.setVisibility(View.VISIBLE);

                String phone = ph.getText().toString().trim();
                String code = c.getSelectedCountryCodeWithPlus();
                phoneNumber = code+phone;

                if (phone.isEmpty() || phone.length() < 10) {
                    ph.setError("Valid number is required");
                    ph.requestFocus();
                    return;
                }
                phone_entry_rl.setVisibility(View.GONE);
                phone_entry_progress.setVisibility(View.GONE);
                phone_verify_rl.setVisibility(View.VISIBLE);
                et_phone_number.setText(phoneNumber);
                sendVerificationCode(phoneNumber);
            }
        });
    }
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            signInWithCredential(credential);
        }
        else{
            FirebaseAuth.getInstance().getCurrentUser().delete();
            signInWithCredential2(credential);
        }
    }

    private void signInWithCredential2(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(LoginActivity.this, ChooseLocationActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                etOtp.setCode(code);
                verifyCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            Log.d("Network", "Connected");
            return true;
        }
        else{
            Log.d("Network","Not Connected");
            return false;
        }
    }
}
