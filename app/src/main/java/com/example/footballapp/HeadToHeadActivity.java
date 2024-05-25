package com.example.footballapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.footballapp.adapter.matchHeadAdapter;
import com.example.footballapp.adapter.matchHeadResultAdapter;
import com.example.footballapp.databinding.ActivityHeadToHeadBinding;
import com.example.footballapp.model.Match;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HeadToHeadActivity extends AppCompatActivity {

    ActivityHeadToHeadBinding binding;
    List<Match> matches = new ArrayList<>();
    List<Match> matchesResultHome = new ArrayList<>();
    List<Match> matchesResultAway = new ArrayList<>();
    matchHeadAdapter adapter = new matchHeadAdapter(matches, this);
    matchHeadResultAdapter adapterResultHome = new matchHeadResultAdapter(matchesResultHome,this);
    matchHeadResultAdapter adapterResultAway = new matchHeadResultAdapter(matchesResultAway,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHeadToHeadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        String idTeamHome = i.getStringExtra("idTeamHome");
        String idTeamAway = i.getStringExtra("idTeamAway");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        binding.rcvLichSuDau.setLayoutManager(layoutManager);

        binding.rcvHomeTeam.setLayoutManager(layoutManager2);
        binding.rcvAwayTeam.setLayoutManager(layoutManager3);


        fetchData(idTeamHome,idTeamAway);
        firstTeamResult10(idTeamHome,idTeamAway);
        secondTeamResult10(idTeamHome,idTeamAway);

        binding.rcvHomeTeam.setAdapter(adapterResultHome);
        binding.rcvAwayTeam.setAdapter(adapterResultAway);
//
        binding.rcvLichSuDau.setAdapter(adapter);

    }

    private void secondTeamResult10(String idTeamHome, String idTeamAway) {
        RequestQueue requestQueue = Volley.newRequestQueue(HeadToHeadActivity.this);
        String url = "https://apiv3.apifootball.com/?action=get_H2H&firstTeamId="+idTeamHome+"&secondTeamId="+idTeamAway+"&APIkey=" + getString(R.string.api_key);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, s -> {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("secondTeam_lastResults");
                for (int index = 0; index < jsonArray.length(); index++){
                    JSONObject jsonObjectChild = jsonArray.getJSONObject(index);
                    String matchID = jsonObjectChild.getString("match_id");
                    String date = jsonObjectChild.getString("match_date");
                    String status = jsonObjectChild.getString("match_status");
                    String nameHome = jsonObjectChild.getString("match_hometeam_name");
                    String nameAway = jsonObjectChild.getString("match_awayteam_name");
                    String scoreHome = jsonObjectChild.getString("match_hometeam_score");
                    String scoreAway = jsonObjectChild.getString("match_awayteam_score");
                    String photoHome = jsonObjectChild.getString("team_home_badge");
                    String photoAway = jsonObjectChild.getString("team_away_badge");
                    matchesResultAway.add(new Match(matchID,date,status,nameHome,nameAway,scoreHome,scoreAway,photoHome,photoAway));
                    adapterResultAway.notifyDataSetChanged();
                }
                binding.pgbLast10MatchAway.setVisibility(View.GONE);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }



        }, volleyError -> {

        });
        requestQueue.add(stringRequest);
    }

    private void firstTeamResult10(String idTeamHome, String idTeamAway) {
        RequestQueue requestQueue = Volley.newRequestQueue(HeadToHeadActivity.this);
        String url = "https://apiv3.apifootball.com/?action=get_H2H&firstTeamId="+idTeamHome+"&secondTeamId="+idTeamAway+"&APIkey=" + getString(R.string.api_key);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, s -> {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("firstTeam_lastResults");
                for (int index = 0; index < jsonArray.length(); index++){
                    JSONObject jsonObjectChild = jsonArray.getJSONObject(index);
                    String matchID = jsonObjectChild.getString("match_id");
                    String date = jsonObjectChild.getString("match_date");
                    String status = jsonObjectChild.getString("match_status");
                    String nameHome = jsonObjectChild.getString("match_hometeam_name");
                    String nameAway = jsonObjectChild.getString("match_awayteam_name");
                    String scoreHome = jsonObjectChild.getString("match_hometeam_score");
                    String scoreAway = jsonObjectChild.getString("match_awayteam_score");
                    String photoHome = jsonObjectChild.getString("team_home_badge");
                    String photoAway = jsonObjectChild.getString("team_away_badge");
                    matchesResultHome.add(new Match(matchID,date,status,nameHome,nameAway,scoreHome,scoreAway,photoHome,photoAway));
                    adapterResultHome.notifyDataSetChanged();
                }
                binding.pgbLast10MatchHome.setVisibility(View.GONE);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }



        }, volleyError -> {

        });
        requestQueue.add(stringRequest);
    }

    private void fetchData(String idTeamHome, String idTeamAway) {

        RequestQueue requestQueue = Volley.newRequestQueue(HeadToHeadActivity.this);
        String url = "https://apiv3.apifootball.com/?action=get_H2H&firstTeamId="+idTeamHome+"&secondTeamId="+idTeamAway+"&APIkey=" + getString(R.string.api_key);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, s -> {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("firstTeam_VS_secondTeam");
                for (int index = 0; index < jsonArray.length(); index++){
                    JSONObject jsonObjectChild = jsonArray.getJSONObject(index);
                    String matchID = jsonObjectChild.getString("match_id");
                    String date = jsonObjectChild.getString("match_date");
                    String status = jsonObjectChild.getString("match_status");
                    String nameHome = jsonObjectChild.getString("match_hometeam_name");
                    String nameAway = jsonObjectChild.getString("match_awayteam_name");
                    String scoreHome = jsonObjectChild.getString("match_hometeam_score");
                    String scoreAway = jsonObjectChild.getString("match_awayteam_score");
                    String photoHome = jsonObjectChild.getString("team_home_badge");
                    String photoAway = jsonObjectChild.getString("team_away_badge");
                    matches.add(new Match(matchID,date,status,nameHome,nameAway,scoreHome,scoreAway,photoHome,photoAway));
                    adapter.notifyDataSetChanged();
                }
                binding.pgbHeadtoHead.setVisibility(View.GONE);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }



        }, volleyError -> {

        });
        requestQueue.add(stringRequest);
    }
}