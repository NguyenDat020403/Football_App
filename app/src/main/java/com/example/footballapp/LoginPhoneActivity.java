package com.example.footballapp;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.footballapp.databinding.ActivityLoginPhoneBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

public class LoginPhoneActivity extends AppCompatActivity {

    ActivityLoginPhoneBinding binding;
    FirebaseAuth mAuth;
    String verificationID;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginPhoneActivity.this, "Xác thực thất bại!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;
            Toast.makeText(LoginPhoneActivity.this, "Code Sent!", Toast.LENGTH_SHORT).show();
            binding.btnVerifOTP.setEnabled(true);
            binding.pgbPhone.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                // Optionally, auto-fill the OTP field for the user
                binding.edtOTP.setText(code);
                Toast.makeText(LoginPhoneActivity.this, "Mã OTP đã được nhận tự động. Vui lòng nhấn Xác thực.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(LoginPhoneActivity.this, leagueSelect.class);
            startActivity(i);
            finish();
        }
        addEvents();
    }

    private void addEvents() {
        binding.btnSendOTP.setOnClickListener(v -> {
            binding.edtOTP.setEnabled(true);
            if (TextUtils.isEmpty(binding.edtphoneNO.getText().toString())) {
                Toast.makeText(this, "Vui lòng nhập SĐT!", Toast.LENGTH_SHORT).show();
            } else {
                String number = binding.edtphoneNO.getText().toString();
                binding.pgbPhone.setVisibility(View.VISIBLE);
                sendverificationcode(number);
            }
        });

        binding.btnVerifOTP.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.edtOTP.getText().toString())) {
                Toast.makeText(this, "Vui lòng nhập OTP!", Toast.LENGTH_SHORT).show();
            } else {
                String otp = binding.edtOTP.getText().toString();
                verifycode(otp);
            }
        });
    }

    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signingbyCredential(credential);
    }

    private void signingbyCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginPhoneActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            binding.btnVerifOTP.setEnabled(false);
                            binding.edtOTP.setEnabled(false);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName("User SMS")
                                    .setPhotoUri(Uri.parse(String.valueOf(R.drawable.profile)))
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                }
                            });
                            Intent i = new Intent(LoginPhoneActivity.this, leagueSelect.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(LoginPhoneActivity.this, "Đăng nhập thất bại. Vui lòng kiểm tra mã OTP và thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void sendverificationcode(String numberPhone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + numberPhone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
