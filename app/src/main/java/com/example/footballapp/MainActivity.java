package com.example.footballapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.footballapp.adapter.TeamAdapter;
import com.example.footballapp.model.Doibong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    TextView txtleagueName;
    ListView lv;
    List<Doibong> teams = new ArrayList<>();
    TeamAdapter teamAdapter = new TeamAdapter(teams);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        getSpinnerYears();

        lv.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy đối tượng Doibong từ Adapter thay vì getItemAtPosition(position)
            Doibong doibong = teams.get(position);
            Intent intent = new Intent(MainActivity.this, TeamDetail.class);

            // Đặt dữ liệu cần truyền qua Intent
            intent.putExtra("id", doibong.Id);
            intent.putExtra("position", doibong.Stt);

            startActivity(intent);
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = (String) parent.getItemAtPosition(position);
                if(!selectedYear.equals("2023")){
                    int temp = Integer.parseInt(selectedYear) +1;
                    txtleagueName.setText("PREMIER LEAGUE TABLE "+selectedYear+"/"+ String.valueOf(temp));
                    teams.clear();
                    fetchData(selectedYear);
                }
                else{
                    teams.clear();
                    getCurrentDoiBong();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void fetchData(String selectedYear) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api-football-beta.p.rapidapi.com/standings?season=" + selectedYear + "&league=39";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray jsonArrayResponse = jsonResponse.getJSONArray("response");
                JSONObject jsonObjectResponse = jsonArrayResponse.getJSONObject(0);
                JSONObject jsonObjectLeague = jsonObjectResponse.getJSONObject("league");
                JSONArray jsonArrayStandings = jsonObjectLeague.getJSONArray("standings");

                if (jsonArrayStandings.length() > 0) {
                    // Truy cập vào phần tử đầu tiên trong mảng standings
                    JSONArray jsonArrayListTeam = jsonArrayStandings.getJSONArray(0);

                    // Lặp qua các đối tượng JSON trong mảng standings
                    for (int i = 0; i < jsonArrayListTeam.length(); i++) {
                        JSONObject jsonObject = jsonArrayListTeam.getJSONObject(i);
                        JSONObject jsonObjectTeam = jsonObject.getJSONObject("team");
                        String id = jsonObjectTeam.getString("id");
                        String teamName = jsonObjectTeam.getString("name");
                        String position = jsonObject.getString("rank");
                        JSONObject jsonObjectPoints = jsonObject.getJSONObject("all");
                        String matchP = jsonObjectPoints.getString("played");
                        String winM = jsonObjectPoints.getString("win");
                        String drawM = jsonObjectPoints.getString("draw");
                        String loseM = jsonObjectPoints.getString("lose");
                        String pointA = jsonObject.getString("points");
                        String photoTeam = jsonObjectTeam.getString("logo");
                        teams.add(new Doibong(id, position, teamName, matchP, winM, drawM, loseM, pointA, photoTeam));
                    }
                    teamAdapter.notifyDataSetChanged();
                } else {
                    // Xử lý trường hợp không có phần tử trong mảng standings
                    Toast.makeText(MainActivity.this, "Không có dữ liệu standings", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, volleyError -> {

        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-RapidAPI-Key", "5b42df3284mshd7c37721fe01078p1c981djsn033a2e836eaa");
                headers.put("X-RapidAPI-Host", "api-football-beta.p.rapidapi.com");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getSpinnerYears() {
        List<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR) -1 ;
        for (int i = currentYear; i >= currentYear - 20; i--) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

    }


    private void getCurrentDoiBong() {

        //JSON
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://apiv3.apifootball.com/?action=get_standings&league_id=152&APIkey="+ getString(R.string.api_key);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {

                // Assuming successful response parsing
                JSONArray jsonArrayList = new JSONArray(response);
                // Loop through each object in the array
                for (int i = 0; i < jsonArrayList.length(); i++) {
                    JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                    txtleagueName.setText("PREMIER LEAGUE TABLE 2023/2024");
                    // Access object data using keys
                    String id = jsonObject.getString("team_id");
                    String teamName = jsonObject.getString("team_name");
                    String position = jsonObject.getString("overall_league_position");
                    String matchP = jsonObject.getString("overall_league_payed");
                    String winM = jsonObject.getString("overall_league_W");
                    String drawM = jsonObject.getString("overall_league_D");
                    String loseM = jsonObject.getString("overall_league_L");
                    String pointA = jsonObject.getString("overall_league_PTS");
                    String photoTeam = jsonObject.getString("team_badge");

                    teams.add(new Doibong(id,position,teamName,matchP,winM,drawM,loseM,pointA,photoTeam));
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
        spinner = findViewById(R.id.spinnerBXH);
        txtleagueName = (TextView) findViewById(R.id.txtGiaiDau);
        lv = (ListView) findViewById(R.id.lvTeam);
        lv.setAdapter(teamAdapter);
    }
}