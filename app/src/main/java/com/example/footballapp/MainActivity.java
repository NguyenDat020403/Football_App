package com.example.footballapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    Spinner spinner;
    TextView txtleagueName;
    ImageView imvBXH;
    ListView lv;
    List<Doibong> teams = new ArrayList<>();
    TeamAdapter teamAdapter = new TeamAdapter(teams);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        String[] years = {"2023/2024", "2022/2023"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        getCurrentDoiBong();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đối tượng Doibong từ Adapter thay vì getItemAtPosition(position)
                Doibong doibong = teams.get(position);
                Intent intent = new Intent(MainActivity.this, TeamDetail.class);

                // Đặt dữ liệu cần truyền qua Intent
                intent.putExtra("team_name", doibong.getName());
                intent.putExtra("position", doibong.getStt());

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
        spinner = findViewById(R.id.spinnerBXH);
        txtleagueName = (TextView) findViewById(R.id.txtGiaiDau);
        imvBXH = (ImageView) findViewById(R.id.imvBangXepHang);
        lv = (ListView) findViewById(R.id.lvTeam);
        lv.setAdapter(teamAdapter);
    }
}