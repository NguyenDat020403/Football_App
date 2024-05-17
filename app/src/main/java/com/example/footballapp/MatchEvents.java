    package com.example.footballapp;

    import androidx.annotation.NonNull;
    import androidx.annotation.RequiresApi;
    import androidx.appcompat.app.AppCompatActivity;
    import android.content.Intent;
    import android.os.Build;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.LinearLayout;
    import android.widget.ListView;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;
    import android.widget.VideoView;

    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;
    import com.example.footballapp.adapter.matchAdapter;
    import com.example.footballapp.databinding.ActivityMatchEventsBinding;
    import com.example.footballapp.model.Match;
    import com.google.android.material.navigation.NavigationBarView;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

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
        LinearLayout  lnmatchDetail;
        ListView lv;
        Button btnHighLight;
        VideoView videoView;
        List<Match> matches = new ArrayList<>();
        List<String> dateList = getDateList();
        com.example.footballapp.adapter.matchAdapter matchAdapter = new matchAdapter(matches);
        ActivityMatchEventsBinding binding;
        int check = 0;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityMatchEventsBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            //////--------------------------------------------------------------////
            Anhxa();
            addNavEvents();
            //////--------------------------------------------------------------////
            Date[] dates = layNgayHomNayVaHomQua();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, dateList);
            adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(3);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Lấy ngày được chọn từ Spinner
                    String selectedDate = (String) parent.getItemAtPosition(position);

                    String previousDate = getPreviousDate(selectedDate);
                    matches.clear();

                    getMatch(previousDate, selectedDate);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Xử lý khi không có mục nào được chọn
                }
            });

            //////--------------------------------------------------------------////
            matchDetailChose();


        }
        private void addNavEvents() {
            binding.bottomNavigation.setSelectedItemId(R.id.nav_fixtures);
            binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int itemId = menuItem.getItemId();
                    if(itemId == R.id.nav_tables){
                        startActivity(new Intent(MatchEvents.this, MainActivity.class));
                        return true;
                    } else if (itemId == R.id.nav_stats) {
                        startActivity(new Intent(MatchEvents.this, PlayerTopActivity.class));
                        return true;
                    } else if (itemId == R.id.nav_fixtures) {
                        return true;
                    } else if (itemId == R.id.nav_news) {
                        startActivity(new Intent(MatchEvents.this, NewsActivity.class));
                        return true;
                    } else if (itemId == R.id.nav_profile) {
                        startActivity(new Intent(MatchEvents.this, InfoUserActivity.class));
                        return true;
                    }
                    return  false;
                }
            });
        }



        private void matchDetailChose() {

            lv.setOnItemClickListener((parent, view, position, id) -> {
                Match matchClicked = matches.get(position);
                Log.d("IDMATCH"," "+ matchClicked.ID);

                // Lấy ra matchID từ Match tương ứng với position
                for (int i = 0; i < parent.getChildCount(); i++) {
                    LinearLayout layout = parent.getChildAt(i).findViewById(R.id.lnMatchDetail);
                    if (layout != null) {
                        if (i == position) {

                            if (layout.getVisibility() == View.VISIBLE) { // Nếu đang hiển thị, ẩn đi
                                layout.setVisibility(View.GONE);

                            } else { // Nếu đang ẩn, hiển thị và thực hiện các hành động khác cần thiết
                                layout.setVisibility(View.VISIBLE);
                                Button btnHighlight = layout.findViewById(R.id.btnHighlight);

                                if (btnHighlight != null) { // Kiểm tra xem btnHighlight có null hay không
                                    btnHighlight.setOnClickListener(v -> {
                                        getvideoHighLight(matchClicked.getID());
                                        Log.d("IDMATCH", " " + matchClicked.getID());
                                    });
                                }
                            }

                        } else {
                            layout.setVisibility(View.GONE);
                        }
                    }
                }
            });

        }
        private void showVideoDialog(String videoUrl) {
            Intent intent = new Intent(MatchEvents.this, MatchVideoActivity.class);
            intent.putExtra("urlVideo",videoUrl);
            startActivity(intent);
        }



        private void getvideoHighLight(String id) {
            RequestQueue requestQueue = Volley.newRequestQueue(MatchEvents.this);
            String url = "https://apiv3.apifootball.com/?action=get_videos&match_id="+id+"&APIkey=" + getString(R.string.api_key);
            Log.d("URL", " " + id);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int errorCode = jsonObject.getInt("error");
                        if (errorCode == 404) {
                            Toast.makeText(MatchEvents.this,"Không tồn tại Highlight của trận đấu này.",Toast.LENGTH_SHORT).show();
                        }


                } catch (JSONException e) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObjectFirst = jsonArray.getJSONObject(0);
                        String urlVideo = jsonObjectFirst.getString("video_url");
                        Log.d("URL", "12" + urlVideo);
                        showVideoDialog(urlVideo);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }, volleyError -> {

            });
            requestQueue.add(stringRequest);
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
            dates.add("2024-01-13");
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
            String url = "https://apiv3.apifootball.com/?action=get_events&from="+date1+"&to="+date2+"&league_id=152&APIkey=" + getString(R.string.api_key);
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
                            matches.add(new Match("-","-"," ","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-" ));
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
                                String id = jsonObject.getString("match_id");
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
                                        matches.add(new Match(id,convertDateTime.getConvertedTime(),nameHome,nameAway,scoreHome,scoreAway,photoHome,photoAway,ballPH,ballPA, ShotsTH,ShotsTA ,ShotsOGH ,ShotsOGA , FoulsH, FoulsA,CHome , CAway, OsHome,OsAway));
                                        Log.d("DoiBong",   " " + convertDateTime.getConvertedTime() + " " + nameHome + " " + nameAway + " " + scoreHome+ " " +scoreAway + " " +photoAway + " " +photoAway );
                                        Collections.sort(matches, Match.timeComparator);

                                        matchAdapter.notifyDataSetChanged();

                                }
                            }
                            if(matches.isEmpty()){
                                txtWeek.setText("Matchweek");
                                txtNgay.setText(date2);

                                matches.clear();
                                matches.add(new Match("-","-"," ","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-" ));
                                matchAdapter.notifyDataSetChanged();
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
            lnmatchDetail = (LinearLayout) findViewById(R.id.lnMatchDetail);
            btnHighLight = (Button) findViewById(R.id.btnHighlight);
            videoView = findViewById(R.id.videoView);
            lv = (ListView) findViewById(R.id.lvMatch);
            lv.setAdapter(matchAdapter);

        }
    }