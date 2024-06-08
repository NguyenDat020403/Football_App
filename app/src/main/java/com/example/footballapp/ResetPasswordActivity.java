package com.example.footballapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.footballapp.databinding.ActivityResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

public class ResetPasswordActivity extends AppCompatActivity {
    ActivityResetPasswordBinding binding;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        Transition explode = new Explode();
        explode.setDuration(1000);
        getWindow().setEnterTransition(explode);
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        binding.imvBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        binding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = binding.edtemailReset.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Nhập địa chỉ email đã đăng ký của bạn", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(ResetPasswordActivity.this, "Gửi yêu cầu thành công! Vui lòng kiểm tra email để đặt lại mật khẩu!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Yêu cầu thất bại!", Toast.LENGTH_SHORT).show();
                            }
                            binding.progressBar.setVisibility(View.GONE);
                        });
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}