package com.example.footballapp;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class leagueSelect extends AppCompatActivity {
    LinearLayout imvBangXepHang, imvPlayerTOP, imvLichThiDau, imvNews;
    ImageView imvInfoUser;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_select);
        Anhxa();
        imvBangXepHang.setOnClickListener(v -> {
            Intent intent = new Intent(leagueSelect.this,MainActivity.class);
            startActivity(intent);
        });
        imvPlayerTOP.setOnClickListener(v -> {
            Intent intent = new Intent(leagueSelect.this,PlayerTopActivity.class);
            startActivity(intent);
        });
        imvLichThiDau.setOnClickListener(v -> {
            Intent intent = new Intent(leagueSelect.this,MatchEvents.class);

            startActivity(intent);
            Log.d("Da bat lich","Da bat lich" );
        });
        imvNews.setOnClickListener(v -> {
            Intent intent = new Intent(leagueSelect.this,NewsActivity.class);

            startActivity(intent);
            Log.d("Da bat NEws","Da bat NEws" );
        });
        imvInfoUser.setOnClickListener(v -> {
            Intent intent = new Intent(leagueSelect.this,InfoUserActivity.class);
            startActivity(intent);
        });

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity();
                    return;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(leagueSelect.this, "Nhấn back thêm một lần nữa để thoát", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void Anhxa() {
        imvBangXepHang = (LinearLayout) findViewById(R.id.imvBangXepHang);
        imvPlayerTOP = (LinearLayout) findViewById(R.id.imvPlayerTOP );
        imvLichThiDau = (LinearLayout) findViewById(R.id.imvLichThiDau);
        imvNews = (LinearLayout) findViewById(R.id.imvNews);
        imvInfoUser = findViewById(R.id.imvInfoUser);
    }

}