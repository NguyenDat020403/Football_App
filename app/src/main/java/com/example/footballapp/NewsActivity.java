package com.example.footballapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

public class NewsActivity extends AppCompatActivity {
    ListView lv;
    List<News> newsList = new ArrayList<>();

    NewsAdapter newsAdapter = new NewsAdapter(newsList);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);
        lv = (ListView) findViewById(R.id.lvNews);
        lv.setAdapter(newsAdapter);
        getNews();
    }

    private void getNews() {
        RequestQueue requestQueue = Volley.newRequestQueue(NewsActivity.this);
        String url = "https://football-news-aggregator-live.p.rapidapi.com/news/fourfourtwo/epl";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArrayList = new JSONArray(response);
                for (int i = 0; i < jsonArrayList.length(); i++) {
                    JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                    String urlNew = jsonObject.getString("url");
                    String title = jsonObject.getString("title");
                    String news_img = jsonObject.getString("news_img");
                    String short_desc = jsonObject.getString("short_desc");
                    newsList.add(new News(urlNew,title,news_img,short_desc));
                    Log.e(TAG, " " +urlNew + title + news_img +short_desc);
                }
                newsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, volleyError -> Log.e(TAG, "Error: " + volleyError.toString())){
            @Override
            public Map<String, String> getHeaders() {
                // Thêm các header cần thiết ở đây
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("X-RapidAPI-Key", "5b42df3284mshd7c37721fe01078p1c981djsn033a2e836eaa");
                headers.put("X-RapidAPI-Host", "football-news-aggregator-live.p.rapidapi.com");
                return headers;
            }
        };

        requestQueue.add(stringRequest);

    }

}