package com.example.footballapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.footballapp.adapter.PlayerTopAdapter;
import com.example.footballapp.model.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerTopActivity extends AppCompatActivity {

    TextView txtSTT, txtTenCauThu, txtTenCLB, txtSoBanThang;
    ImageView imvPhotoCauThu;
    ListView lv;
    List<Player> players = new ArrayList<>();
    PlayerTopAdapter playerTopAdapter = new PlayerTopAdapter(players);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_top);
        Anhxa();
        getCurrentPlayerTop();
    }

    private void getCurrentPlayerTop() {

        RequestQueue requestQueue = Volley.newRequestQueue(PlayerTopActivity.this);

        String url = "https://apiv3.apifootball.com/?action=get_topscorers&league_id=152&APIkey=d8041cc80be090e349c861b44fc185ddf774128288e55bf514206d1af6964456";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArrayPlayerList = new JSONArray(response);

                for (int i = 0; i < jsonArrayPlayerList.length(); i++) {
                    JSONObject jsonObject = jsonArrayPlayerList.getJSONObject(i);

                    // Access object data using keys
                    String playerName = jsonObject.getString("player_name");
                    String position = jsonObject.getString("player_place");
                    String teamName = jsonObject.getString("team_name");
                    String soBanThang = jsonObject.getString("goals");
                    String playerKey = jsonObject.getString("player_key");
                    //image
                    getPlayerPhoto(playerName, playerKey, new PhotoCallback() {
                        @Override
                        public void onPhotoReceived(String photoUrl) {
                            players.add(new Player(position, playerName, teamName, soBanThang, photoUrl));
                            Collections.sort(players, Player.positionComparator);
                            playerTopAdapter.notifyDataSetChanged();
                        }
                    });
                }
                playerTopAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {

        });
        requestQueue.add(stringRequest);
    }
    private void getPlayerPhoto(String playerName, String playerKey, PhotoCallback callback) {
        playerName = playerName.replace(" ", "%20");
        RequestQueue requestQueue = Volley.newRequestQueue(PlayerTopActivity.this);

        String url = "https://apiv3.apifootball.com/?action=get_players&player_name=" + playerName + "&APIkey=d8041cc80be090e349c861b44fc185ddf774128288e55bf514206d1af6964456";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArrayInfoPlayer = new JSONArray(response);

                for (int j = 0; j < jsonArrayInfoPlayer.length(); j++) {
                    JSONObject jsonObject1 = jsonArrayInfoPlayer.getJSONObject(j);
                    if (playerKey.equals(jsonObject1.getString("player_key"))) {
                        String photoUrl = jsonObject1.getString("player_image");
                        callback.onPhotoReceived(photoUrl);
                        return;
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            // Handle error
        });
        requestQueue.add(stringRequest);
    }
    interface PhotoCallback {
        void onPhotoReceived(String photoUrl);
    }
    private void Anhxa() {
        txtSTT = (TextView) findViewById(R.id.txtSTTScore);
        txtTenCauThu = (TextView) findViewById(R.id.txtNameVDV);
        txtTenCLB = (TextView) findViewById(R.id.txtCauLacBo);
        txtSoBanThang = (TextView) findViewById(R.id.txtSoBanThang);
        imvPhotoCauThu = (ImageView) findViewById(R.id.imvPhotoVDV);
        lv = (ListView) findViewById(R.id.lvTopScore);
        lv.setAdapter(playerTopAdapter);
    }
}