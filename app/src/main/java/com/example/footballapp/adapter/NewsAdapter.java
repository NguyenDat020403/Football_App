package com.example.footballapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.footballapp.R;
import com.example.footballapp.model.Match;
import com.example.footballapp.model.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends BaseAdapter {
    private List<News> newsList;

    public NewsAdapter(List<News> news) {
        this.newsList = news;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout,  parent, false);
        }
        News news = newsList.get(position);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        if (txtTitle != null) {
            txtTitle.setText(news.getTitle());
        }
        TextView txtShortDesc = view.findViewById(R.id.txtShortDesc);
        if (txtShortDesc != null) {
            txtShortDesc.setText(news.getShort_desc());
        }

        ImageView imvPhotoNews = view.findViewById(R.id.imvNewsPhoto);
        if (news.getImage() != null && !news.getImage().isEmpty()) {
            Picasso.get().load(news.getImage()).into(imvPhotoNews);
        } else {
            Picasso.get().load("https://apiv3.apifootball.com/badges/logo_leagues/152_premier-league.png").into(imvPhotoNews);
        }

        return view;
    }
}
