package com.example.footballapp;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.footballapp.adapter.matchAdapter;
import com.example.footballapp.databinding.ActivityLiveScoreBinding;
import com.example.footballapp.model.Match;
import com.google.type.Date;

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

public class LiveScoreActivity extends AppCompatActivity {

    ActivityLiveScoreBinding binding;

    List<Match> matches = new ArrayList<>();

    matchAdapter liveMatchAdapter = new matchAdapter(matches);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLiveScoreBinding.inflate(getLayoutInflater());
        Transition explode = new Explode();
        explode.setDuration(1000);
        getWindow().setEnterTransition(explode);
        setContentView(binding.getRoot());
        //==================================================
        binding.lvLiveScore.setAdapter(liveMatchAdapter);

        getMatch(getCurrentDate());
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

        RequestQueue requestQueue = Volley.newRequestQueue(LiveScoreActivity.this);
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
                    matches.add(new Match("-","-",null," ","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-" ));
                    liveMatchAdapter.notifyDataSetChanged();
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

                            matches.add(new Match("-","-",null," ","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-" ));
                            liveMatchAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }

        }, error -> {

        });
        requestQueue.add(stringRequest);

    }

}