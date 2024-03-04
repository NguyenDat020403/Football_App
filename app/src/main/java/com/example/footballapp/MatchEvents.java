package com.example.footballapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MatchEvents extends AppCompatActivity {
    String ngayHienTai;
    TextView txtWeek,txtNgay;
    ListView lv;
    List<Match> matches = new ArrayList<>();
    matchAdapter matchAdapter = new matchAdapter(matches);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_events);
        Anhxa();
        Log.d("Da anh xa","DA ANH XA");
        getMatch(layNgayHienTai());
        Log.d("Da anh xa","DA ANH XA" + layNgayHienTai());
    }

    private void getMatch(String date){
        RequestQueue requestQueue = Volley.newRequestQueue(MatchEvents.this);
        String url = "https://apiv3.apifootball.com/?action=get_events&from="+date+"&to="+date+"&league_id=152&APIkey=3610b80f6e2a8098d44998dd4727472e20e396dacbe7a0cea1f201d13330dd3c";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObjectFirst = jsonArray.getJSONObject(0);
                    String matchRound = jsonObjectFirst.getString("match_round");
                    Log.d("DoiBong",   "Vong DAU" + matchRound);
                    txtWeek.setText("Matchweek " + matchRound);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String time = jsonObject.getString("match_time");

                        String DATE = jsonObject.getString("match_date");
                        ConvertDateTime convertDateTime = chuyenDoiMoiMuiGio(time,DATE);

                        String nameHome = jsonObject.getString("match_hometeam_name");
                        String nameAway = jsonObject.getString("match_awayteam_name");
                        String scoreHome = jsonObject.getString("match_hometeam_score");
                        String scoreAway = jsonObject.getString("match_awayteam_score");
                        String photoHome = jsonObject.getString("team_home_badge");
                        String photoAway = jsonObject.getString("team_away_badge");
                        matches.add(new Match(convertDateTime.getConvertedTime(),nameHome,nameAway,scoreHome,scoreAway,photoHome,photoAway));
                        Log.d("DoiBong",   " " + convertDateTime.getConvertedTime() + " " + nameHome + " " + nameAway + " " + scoreHome+ " " +scoreAway + " " +photoAway + " " +photoAway );

                    }
                    matchAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);

    }


    private ConvertDateTime chuyenDoiMoiMuiGio(String time, String DATE) {
        try {
            // Định dạng thời gian từ chuỗi ngày và thời gian
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("GMT+1")); // Đặt múi giờ của API (UTC)


            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // Đặt múi giờ của bạn (Asia/Ho_Chi_Minh)


            SimpleDateFormat outputDATEFormat = new SimpleDateFormat("EEEE dd-MM-yyyy", Locale.getDefault());
            outputDATEFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // Đặt múi giờ của bạn (Asia/Ho_Chi_Minh)

            // Parse thời gian từ chuỗi "2024-03-04 21:00"
            Date date = inputFormat.parse(DATE + " " + time);

            String convertedDate = outputDATEFormat.format(date);
            // Format lại thời gian theo định dạng mong muốn
            String convertedTime = outputFormat.format(date);
            txtNgay.setText(convertedDate);
            // Trả về một đối tượng ConvertedDateTime chứa cả hai giá trị
            return new ConvertDateTime(convertedTime, convertedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    private String layNgayHienTai() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();

        // Định dạng ngày theo YYYY-MM-DD
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ngayHienTai = dateFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEEE yyyy-MM-dd", Locale.getDefault());
        String ngayHienTai1 = dateFormat2.format(calendar.getTime());

        // Hiển thị ngày hiện tại lên TextView
        return ngayHienTai;
    }
    private void Anhxa() {
        txtWeek = (TextView) findViewById(R.id.txtWeek);
        txtNgay = (TextView) findViewById(R.id.txtNgay);
        lv = (ListView) findViewById(R.id.lvMatch);
        lv.setAdapter(matchAdapter);
    }
}