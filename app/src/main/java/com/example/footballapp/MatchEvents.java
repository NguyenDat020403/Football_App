package com.example.footballapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MatchEvents extends AppCompatActivity {
    Spinner spinner;
    TextView txtWeek,txtNgay;
    ListView lv;
    List<Match> matches = new ArrayList<>();
    List<String> dateList = getDateList();
    matchAdapter matchAdapter = new matchAdapter(matches);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_events);
        Anhxa();
        Date[] dates = layNgayHomNayVaHomQua();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, dateList);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy ngày được chọn từ Spinner
                String selectedDate = (String) parent.getItemAtPosition(position);

                // Tính toán ngày trước đó
                String previousDate = getPreviousDate(selectedDate);
                matches.clear();

                getMatch(previousDate, selectedDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });
        // Định dạng lại ngày thành chuỗi với định dạng yyyy-MM-dd
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String ngayHienTaiStr = dateFormat.format(dates[0]);
//        String ngayHomQuaStr = dateFormat.format(dates[1]);
//        getMatch(ngayHomQuaStr,ngayHienTaiStr);

    }
    private List<String> getDateList() {
        List<String> dates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        for (int i = -3; i <= 3; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, i);
            dates.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -i);
        }
        return dates;
    }
    private String getPreviousDate(String currentDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = sdf.parse(currentDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void getMatch(String date1,String date2){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        RequestQueue requestQueue = Volley.newRequestQueue(MatchEvents.this);
        String url = "https://apiv3.apifootball.com/?action=get_events&from="+date1+"&to="+date2+"&league_id=152&APIkey=3610b80f6e2a8098d44998dd4727472e20e396dacbe7a0cea1f201d13330dd3c";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    String mess = jsonObject1.getString("error");
                        Log.d("DoiBong",mess   );
                        txtWeek.setText("Matchweek");
                        txtNgay.setText(date2);

                        matches.clear();
                        matches.add(new Match("-","-","-","Không có trận đấu ngày hôm nay","-","-","-"));
                        matchAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObjectFirst = jsonArray.getJSONObject(0);
                        String matchRound = jsonObjectFirst.getString("match_round");
                        Log.d("DoiBong",   "Vong DAU" + matchRound);
                        txtWeek.setText("Matchweek " + matchRound);

                        for (int i = 0; i < jsonArray.length(); i++){

                                Log.d("SAi i ", "SAI LOOP" +i);

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String time = jsonObject.getString("match_time");
                            String DATE = jsonObject.getString("match_date");
                            LocalDateTime dateTime1 = LocalDateTime.parse(DATE + " " + time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                            LocalDateTime fixedDateTimeObj = LocalDateTime.parse(date1 + " 18:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                            LocalDateTime fixedDateTimeObj2 = LocalDateTime.parse(date2 + " 18:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));



                            if(dateTime1.isAfter(fixedDateTimeObj) &&   dateTime1.isBefore(fixedDateTimeObj2)){

                                ConvertDateTime convertDateTime = chuyenDoiMoiMuiGio(time,DATE);

                                    String nameHome = jsonObject.getString("match_hometeam_name");
                                    String nameAway = jsonObject.getString("match_awayteam_name");
                                    String scoreHome = jsonObject.getString("match_hometeam_score");
                                    String scoreAway = jsonObject.getString("match_awayteam_score");
                                    String photoHome = jsonObject.getString("team_home_badge");
                                    String photoAway = jsonObject.getString("team_away_badge");
                                    matches.add(new Match(convertDateTime.getConvertedTime(),nameHome,nameAway,scoreHome,scoreAway,photoHome,photoAway));
                                    Log.d("DoiBong",   " " + convertDateTime.getConvertedTime() + " " + nameHome + " " + nameAway + " " + scoreHome+ " " +scoreAway + " " +photoAway + " " +photoAway );
                                    Collections.sort(matches, Match.timeComparator);

                                    matchAdapter.notifyDataSetChanged();

                            }
                        }
                        if(matches.isEmpty()){
                            txtWeek.setText("Matchweek");
                            txtNgay.setText(date2);

                            matches.clear();
                            matches.add(new Match("-","-","-","Không có trận đấu ngày hôm nay","-","-","-"));
                            matchAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }

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




    public static Date[] layNgayHomNayVaHomQua() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();

        // Lấy ngày hôm nay
        Date ngayHienTai = calendar.getTime();

        // Lấy ngày hôm qua
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date ngayHomQua = calendar.getTime();

        // Trả về một mảng chứa cả hai giá trị
        return new Date[]{ngayHienTai, ngayHomQua};
    }
    private void Anhxa() {
        txtWeek = (TextView) findViewById(R.id.txtWeek);
        txtNgay = (TextView) findViewById(R.id.txtNgay);
        spinner = (Spinner) findViewById(R.id.spinner) ;
        lv = (ListView) findViewById(R.id.lvMatch);
        lv.setAdapter(matchAdapter);
    }
}