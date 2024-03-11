package com.example.footballapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.footballapp.adapter.DetailAdapter;
import com.example.footballapp.model.Detail;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamDetail extends AppCompatActivity {
    ImageView imvAnhDaiDien,imvPlayer,imvDetail;
    LinearLayout lilteamInfo;

    TextView txtXepHang,txtngayTL,txtdiadiem,txthuanluyenvien;
    ListView lv;
    List<Detail> details = new ArrayList<>();
    DetailAdapter detailAdapter = new DetailAdapter(details);
    String id = null;
    String position= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        Anhxa();
        initInfo();
        hienPlayer();
        addEvents();

    }

    private void initInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            // Lấy dữ liệu từ Intent
            id= intent.getStringExtra("id");

            position = intent.getStringExtra("position");

        }
    }


    private void hienThongtindoibong() {
        Intent intent = getIntent();
        if (intent != null) {
            // Lấy dữ liệu từ Intent
            id= intent.getStringExtra("id");

            position = intent.getStringExtra("position");
            if (id != null && position != null) {
                getCurrentTeamInfo(id);
                txtXepHang.setText("#"+position);
            } else {
                getCurrentTeamInfo(id);
                txtXepHang.setText("#error");
            }
        }
    }

    private void addEvents() {
        imvPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvPlayer.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                imvDetail.setBackgroundColor(Color.TRANSPARENT);
                lv.setVisibility(View.VISIBLE);
                lilteamInfo.setVisibility(View.GONE);
                hienPlayer();
            }
        });

        imvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvDetail.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                imvPlayer.setBackgroundColor(Color.TRANSPARENT);
                lv.setVisibility(View.GONE);
                lilteamInfo.setVisibility(View.VISIBLE);
                hienThongtindoibong();
            }
        });
    }

    private void hienPlayer() {
        Intent intent = getIntent();
        if (intent != null) {
            // Lấy dữ liệu từ Intent
            id= intent.getStringExtra("id");

            position = intent.getStringExtra("position");
            if (id != null && position != null) {
                getCurrentPlayer(id);
                txtXepHang.setText("#"+position);
            } else {
                getCurrentPlayer(id);
                txtXepHang.setText("#error");
            }
        }
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
    private void getCurrentTeamInfo(String id) {
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
                            String ngayThanhLap = jsonObject.getString("team_founded");
                            txtngayTL.setText(ngayThanhLap);
                            JSONObject jsonObjectDiaDiem = jsonObject.getJSONObject("venue");
                            String diaDiem = jsonObjectDiaDiem.getString("venue_name");
                            txtdiadiem.setText(diaDiem);

                            JSONArray jsonArrayCoach = jsonObject.getJSONArray("coaches");
                            JSONObject jsonObjectCoach = jsonArrayCoach.getJSONObject(0);
                            String coachName= jsonObjectCoach.getString("coach_name");
                            txthuanluyenvien.setText(coachName);
                            break;
                        }
                    }
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
        imvPlayer = findViewById(R.id.imvPlayer);
        imvPlayer.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        imvDetail = findViewById(R.id.imvDetail);
        imvAnhDaiDien = findViewById(R.id.imvAnhTEAM);
        imvDetail= findViewById(R.id.imvDetail);
        txtXepHang = findViewById(R.id.txtXepHang);
        txtngayTL = findViewById(R.id.txtNgayThanhLap);
        txtdiadiem = findViewById(R.id.txtDiaDiem);
        txthuanluyenvien = findViewById(R.id.txtHuanLuyenVien);
        lilteamInfo = findViewById(R.id.lilteaminfo);

        lv = findViewById(R.id.lvTeamDetail);
        lv.setAdapter(detailAdapter);
    }
}