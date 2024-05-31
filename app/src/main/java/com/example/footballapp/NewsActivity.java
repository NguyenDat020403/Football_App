package com.example.footballapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.example.footballapp.databinding.ActivityNewsBinding;
import com.example.footballapp.model.News;
import com.google.android.material.navigation.NavigationBarView;

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
    LinearLayout lnAll,lnLaliga,lnOneFootball,lnGoal,lnFourFourTwo;

    NewsAdapter newsAdapter = new NewsAdapter(newsList);
    ActivityNewsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition explode = new Explode();
        explode.setDuration(1000);
        getWindow().setEnterTransition(explode);
        setContentView(binding.getRoot());
        lv = (ListView) findViewById(R.id.lvNews);
        lv.setAdapter(newsAdapter);
        Anhxa();
        addNavEvents();
        addEvents();
    }
    private void addNavEvents() {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_news);
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if(itemId == R.id.nav_tables){
                    startActivity(new Intent(NewsActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_stats) {
                    startActivity(new Intent(NewsActivity.this, PlayerTopActivity.class));
                    return true;
                } else if (itemId == R.id.nav_fixtures) {
                    startActivity(new Intent(NewsActivity.this, MatchEvents.class));
                    return true;
                } else if (itemId == R.id.nav_news) {
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(NewsActivity.this, InfoUserActivity.class));
                    return true;
                }
                return  false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void Anhxa() {
        lnAll = findViewById(R.id.lnAll);
        lnLaliga = findViewById(R.id.lnLaliga);
        lnOneFootball = findViewById(R.id.lnOneFootball);
        lnGoal = findViewById(R.id.lnGoal);
        lnFourFourTwo = findViewById(R.id.lnFourFourTwo);
    }

    private void addEvents() {
        getNews("fourfourtwo/epl", "url","news_img","title");
        binding.lnEPL.setOnClickListener(v -> {
            newsList.clear();
            binding.pgbNewsLoad.setVisibility(View.VISIBLE);
            getNews("fourfourtwo/epl", "url","news_img","title");
        });
        binding.lnOneFootball.setOnClickListener(v -> {
            newsList.clear();
            binding.pgbNewsLoad.setVisibility(View.VISIBLE);
            getNews("onefootball","url","img","title");
        });

        binding.lnUCL.setOnClickListener(v -> {
            newsList.clear();
            binding.pgbNewsLoad.setVisibility(View.VISIBLE);
            getNews("fourfourtwo/ucl","url","news_img","title");
        });
        binding.lnESPN.setOnClickListener(v -> {
            newsList.clear();
            binding.pgbNewsLoad.setVisibility(View.VISIBLE);
            getNews("espn","url","img","title");
        });
    }


    private void getNews(String type,String urlNews, String imgNews, String titleNews) {
        RequestQueue requestQueue = Volley.newRequestQueue(NewsActivity.this);
        String url = "https://football-news-aggregator-live.p.rapidapi.com/news/"+ type;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArrayList = new JSONArray(response);
                for (int i = 0; i < jsonArrayList.length(); i++) {
                    JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                    String urlNew = jsonObject.getString(urlNews);
                    String tiTle = jsonObject.getString(titleNews);
                    String news_img = jsonObject.getString(imgNews);
                    newsList.add(new News(urlNew,tiTle,news_img));
                    Log.e(TAG, urlNew + tiTle + news_img );
                }
                newsAdapter.notifyDataSetChanged();
                binding.pgbNewsLoad.setVisibility(View.GONE);
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