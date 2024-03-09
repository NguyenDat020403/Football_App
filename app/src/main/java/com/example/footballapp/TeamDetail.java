package com.example.footballapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamDetail extends AppCompatActivity {
    ImageView imvAnhDaiDien,imvPlayer,imvDetail;
    TextView txtXepHang;
    ListView lv;
    List<Detail> details = new ArrayList<>();
    DetailAdapter detailAdapter = new DetailAdapter(details);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        Anhxa();
        Intent intent = getIntent();
        String id = null;
        String position= null;
        if (intent != null) {
            // Lấy dữ liệu từ Intent
            id= intent.getStringExtra("id");

            position = intent.getStringExtra("position");
            if (id != null && position != null) {
                Log.d("Ten", "a " + id);
                getCurrentPlayer(id);
                txtXepHang.setText("#"+position);
            } else {
                Log.e("DoiBong", "Dữ liệu truyền từ Intent không đầy đủ");
                getCurrentPlayer(id);
                txtXepHang.setText("#error");

            }
        }
        Log.d("Ten","a " +id);
    }


    private void getCurrentPlayer(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(TeamDetail.this);
        String url = "https://apiv3.apifootball.com/?action=get_teams&league_id=152&APIkey=3610b80f6e2a8098d44998dd4727472e20e396dacbe7a0cea1f201d13330dd3c";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Assuming successful response parsing
                    JSONArray jsonArrayList = new JSONArray(response);
                    // Loop through each object in the array
                    for (int i = 0; i < jsonArrayList.length(); i++) {
                        JSONObject jsonObject = jsonArrayList.getJSONObject(i);

                        // Access object data using keys
                        String idTEAM = jsonObject.getString("team_key");
                        if(idTEAM.equals(id)){

                            String photo = jsonObject.getString("team_badge");
                            Picasso.get().load(photo).into(imvAnhDaiDien);


                            JSONArray playersArray = jsonObject.getJSONArray("players");
                            for (int j = 0; j < playersArray.length(); j++) {
                                JSONObject playerObject = playersArray.getJSONObject(j);
                                String playerPhoto = playerObject.getString("player_image");
                                String playerName = playerObject.getString("player_name");
                                String playerType = playerObject.getString("player_type");
                                String playerAge = playerObject.getString("player_age");
                                String playerNumber= playerObject.getString("player_number");
                                details.add(new Detail(playerPhoto,playerName,playerType,playerAge,playerNumber));
                                Log.d("Cau thu", "TenCT: " + playerName + ", Vi tri: " + playerType);
                                detailAdapter.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                    detailAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

                requestQueue.add(stringRequest);
    }

    private void Anhxa() {
        imvAnhDaiDien = findViewById(R.id.imvAnhTEAM);
        imvPlayer = findViewById(R.id.imvPlayer);
        imvDetail= findViewById(R.id.imvDetail);
        txtXepHang = findViewById(R.id.txtXepHang);
        lv = findViewById(R.id.lvTeamDetail);
        lv.setAdapter(detailAdapter);
    }
}