package com.example.footballapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView txtleagueName;
    ImageView imvBXH,imvPlayerTop;
    ListView lv;
    List<Doibong> teams = new ArrayList<>();
    TeamAdapter teamAdapter = new TeamAdapter(teams);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        getCurrentDoiBong();
        imvPlayerTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PlayerTopActivity.class);
                startActivity(intent);
            }
        });
        imvBXH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        imvBXH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCurrentDoiBong() {

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://apiv3.apifootball.com/?action=get_standings&league_id=152&APIkey=3610b80f6e2a8098d44998dd4727472e20e396dacbe7a0cea1f201d13330dd3c";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                // Assuming successful response parsing
                JSONArray jsonArrayList = new JSONArray(response);
                JSONObject jsonObjectFirst = jsonArrayList.getJSONObject(0);
                String giaiDau = jsonObjectFirst.getString("league_name");
                txtleagueName.setText("Bảng xếp hạng " + giaiDau);
                // Loop through each object in the array
                for (int i = 0; i < jsonArrayList.length(); i++) {
                    JSONObject jsonObject = jsonArrayList.getJSONObject(i);

                    // Access object data using keys
                    String teamName = jsonObject.getString("team_name");
                    String position = jsonObject.getString("overall_league_position");
                    String matchP = jsonObject.getString("overall_league_payed");
                    String winM = jsonObject.getString("overall_league_W");
                    String drawM = jsonObject.getString("overall_league_D");
                    String loseM = jsonObject.getString("overall_league_L");
                    String pointA = jsonObject.getString("overall_league_PTS");
                    String photoTeam = jsonObject.getString("team_badge");

                    teams.add(new Doibong(position,teamName,matchP,winM,drawM,loseM,pointA,photoTeam));
                    // Process the data
                    Log.d("DoiBong", "Ten doi bong: " + teamName + ", Vi tri: " + position);
                }
                teamAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e("DoiBong", "Error parsing JSON: " + e.getMessage()); // Log the error
            } catch (Exception e) {
                Log.e("DoiBong", "Unexpected error: " + e.getMessage()); // Log other potential errors
            }
        }, error -> {
            Log.e("DoiBong", "Error fetching data: " + error.getMessage()); // Log network error
        });
        requestQueue.add(stringRequest);

    }

    private void Anhxa() {

        txtleagueName = (TextView) findViewById(R.id.txtGiaidau);
        imvBXH = (ImageView) findViewById(R.id.imvBangXepHang);
        imvPlayerTop = (ImageView) findViewById(R.id.imvPlayerTop);
        lv = (ListView) findViewById(R.id.lvTeam);
        lv.setAdapter(teamAdapter);
    }
}