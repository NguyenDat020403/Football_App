package com.example.footballapp;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.footballapp.adapter.matchLiveAdapter;
import com.example.footballapp.databinding.ActivityLeagueSelectBinding;
import com.example.footballapp.model.Match;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class leagueSelect extends AppCompatActivity {
    ActivityLeagueSelectBinding binding;
    LinearLayout imvBangXepHang, imvPlayerTOP, imvLichThiDau, imvNews;
    ImageView imvInfoUser;
    private boolean doubleBackToExitPressedOnce = false;
    List<Match> matches = new ArrayList<>();

    matchLiveAdapter liveMatchAdapter = new matchLiveAdapter(matches);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeagueSelectBinding.inflate(getLayoutInflater());
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition explode = new Explode();
        explode.setDuration(1000);
        getWindow().setEnterTransition(explode);
        setContentView(binding.getRoot());
        Anhxa();
        binding.lvLiveMatch.setAdapter(liveMatchAdapter);

        getMatch(getCurrentDate());

        addEvents();
        backPressDoubleTouch();

    }
    private String getCurrentDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime current = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = current.format(formatter);
            return formattedDate;
        }
        else{
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = formatter.format(calendar.getTime());
            return formattedDate;
        }
    }


    private void getMatch(String date){
        binding.pgbLiveMatch.setVisibility(View.VISIBLE);
        binding.relLiveMatch.setVisibility(View.VISIBLE);
        binding.txtNotMatchLive.setVisibility(View.GONE);
        RequestQueue requestQueue = Volley.newRequestQueue(leagueSelect.this);
        String url = "https://apiv3.apifootball.com/?action=get_events&from="+date+"&to="+date+"&APIkey=" + getString(R.string.api_key) +"&timezone=Asia/Bangkok&match_live=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    String mess = jsonObject1.getString("error");
                    Log.d("DoiBong",mess   );
                    matches.clear();
                    matches.add(new Match("-","-"," "," ","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-" ));
                    liveMatchAdapter.notifyDataSetChanged();
                    binding.pgbLiveMatch.setVisibility(View.GONE);
                    binding.lvLiveMatch.setVisibility(View.GONE);
                    binding.txtNotMatchLive.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObjectFirst = jsonArray.getJSONObject(0);
                        String matchRound = jsonObjectFirst.getString("match_round");
                        Log.d("DoiBong",   "Vong DAU" + matchRound);

                        for (int i = 0; i < jsonArray.length(); i++){

                            Log.d("SAi i ", "SAI LOOP" +i);

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("match_id");
                            String matchStatus = jsonObject.getString("match_status");

                            String time = jsonObject.getString("match_time");
                            String DATE = jsonObject.getString("match_date");

                            String nameHome = jsonObject.getString("match_hometeam_name");
                            String nameAway = jsonObject.getString("match_awayteam_name");
                            String scoreHome = jsonObject.getString("match_hometeam_score");
                            String scoreAway = jsonObject.getString("match_awayteam_score");
                            String photoHome = jsonObject.getString("team_home_badge");
                            String photoAway = jsonObject.getString("team_away_badge");
                            JSONArray statisticsArray = jsonObject.getJSONArray("statistics");
                            String ballPH =null;
                            String ballPA = null ;
                            String ShotsTH =null;
                            String ShotsTA = null ;
                            String ShotsOGH =null;
                            String ShotsOGA = null ;
                            String OsHome =null;
                            String OsAway = null ;
                            String FoulsH =null;
                            String FoulsA = null ;
                            String CHome =null;
                            String CAway = null ;
                            for (int j = 0;j < statisticsArray.length(); j++) {
                                JSONObject statisticObject = statisticsArray.getJSONObject(j);
                                String type = statisticObject.getString("type");
                                String homeValue = statisticObject.getString("home");
                                String awayValue = statisticObject.getString("away");
                                switch (type) {
                                    case "Ball Possession":
                                        ballPH = homeValue;
                                        ballPA = awayValue ;
                                        break;
                                    case "Shots Total":
                                        ShotsTH = homeValue;
                                        ShotsTA = awayValue ;
                                        break;
                                    case "Shots On Goal":
                                        ShotsOGH = homeValue;
                                        ShotsOGA = awayValue ;
                                        break;
                                    case "Offsides":
                                        OsHome= homeValue;
                                        OsAway= awayValue ;
                                        break;
                                    case "Corners":
                                        CHome = homeValue;
                                        CAway = awayValue ;
                                        break;
                                    case "Fouls":
                                        FoulsH = homeValue;
                                        FoulsA = awayValue ;
                                        break;
                                }
                            }
                            matches.add(new Match(id,time,matchStatus,nameHome,nameAway,scoreHome,scoreAway,photoHome,photoAway,ballPH,ballPA, ShotsTH,ShotsTA ,ShotsOGH ,ShotsOGA , FoulsH, FoulsA,CHome , CAway, OsHome,OsAway));
                            Log.d("DoiBong",   " " + time + " " + nameHome + " " + nameAway + " " + scoreHome+ " " +scoreAway + " " +photoAway + " " +photoAway );
                            Collections.sort(matches, Match.timeComparator);

                            liveMatchAdapter.notifyDataSetChanged();


                        }
                        if(matches.isEmpty()){
                            binding.relLiveMatch.setVisibility(View.GONE);
                        }
                        binding.pgbLiveMatch.setVisibility(View.GONE);

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        }, error -> {

        });
        requestQueue.add(stringRequest);

    }
    private void backPressDoubleTouch() {
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

            }
        });
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void addEvents() {
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