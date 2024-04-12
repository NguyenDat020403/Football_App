package com.example.footballapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.footballapp.R.color.colorButton;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.footballapp.adapter.NewsAdapter;
import com.example.footballapp.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    ListView lv;
    List<News> newsList = new ArrayList<>();

    NewsAdapter newsAdapter = new NewsAdapter(newsList);
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        lv = (ListView) findViewById(R.id.lvNews);
        lv.setAdapter(newsAdapter);
        getNews();
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity();
                    return;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(HomeActivity.this, "Nhấn back thêm một lần nữa để thoát", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

            }
        });
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
    public void onImageClick(View view) {
        // Đặt trạng thái selected cho ImageView được nhấp
        view.setSelected(true);

        // Xóa trạng thái selected của tất cả các ImageView khác
        ImageView imageView1 = findViewById(R.id.imvHome);
        ImageView imageView2 = findViewById(R.id.imvFixtures);
        ImageView imageView3 = findViewById(R.id.imvPlayers);
        ImageView imageView4 = findViewById(R.id.imvTables);
        ImageView imageView5 = findViewById(R.id.imvOption);

        // Thêm các ImageView còn lại tương tự
        int defaultColor = ContextCompat.getColor(this, R.color.white);
        imageView1.setBackgroundColor(defaultColor);
        imageView2.setBackgroundColor(defaultColor);
        imageView3.setBackgroundColor(defaultColor);
        imageView4.setBackgroundColor(defaultColor);
        imageView5.setBackgroundColor(defaultColor);
        int selectedColor = ContextCompat.getColor(this, R.color.colorButton); // Định nghĩa màu nền khi được chọn

        view.setBackgroundColor(selectedColor);

    }

    private void getNews() {
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        String url = "https://football-news-aggregator-live.p.rapidapi.com/news/onefootball";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArrayList = new JSONArray(response);
                for (int i = 0; i < jsonArrayList.length(); i++) {
                    JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                    String urlNew = jsonObject.getString("url");
                    String title = jsonObject.getString("title");
                    String news_img = jsonObject.getString("img");
//                    String short_desc = jsonObject.getString("short_desc");
                    newsList.add(new News(urlNew,title,news_img));
                    Log.e(TAG, " " +urlNew + title + news_img );
                }
                newsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, volleyError -> Log.e(TAG, "Error: " + volleyError.toString())){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("X-RapidAPI-Key", "5b42df3284mshd7c37721fe01078p1c981djsn033a2e836eaa");
                headers.put("X-RapidAPI-Host", "football-news-aggregator-live.p.rapidapi.com");
                return headers;
            }
        };

        requestQueue.add(stringRequest);

    }
}