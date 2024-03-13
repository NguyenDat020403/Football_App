package com.example.footballapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


public class leagueSelect extends AppCompatActivity {
    ImageView imvBangXepHang, imvPlayerTOP, imvLichThiDau, imvNews;
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
    }

    private void Anhxa() {
        imvBangXepHang = (ImageView) findViewById(R.id.imvBangXepHang);
        imvPlayerTOP = (ImageView) findViewById(R.id.imvPlayerTOP );
        imvLichThiDau = (ImageView) findViewById(R.id.imvLichThiDau);
        imvNews = (ImageView) findViewById(R.id.imvNews);
    }

}