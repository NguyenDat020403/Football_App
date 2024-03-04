package com.example.footballapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class matchAdapter extends BaseAdapter {


    private List<Match> matches;
    public matchAdapter(List<Match> matches) {
        this.matches = matches;
    }
    @Override
    public int getCount() {
        return matches.size();
    }

    @Override
    public Object getItem(int position) {
        return matches.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_layout,  parent, false);
        }
        Match match = matches.get(position);
        TextView txtTime = view.findViewById(R.id.txtTime);
        TextView txtnameHome = view.findViewById(R.id.txtDoi1);
        TextView txtnameAway = view.findViewById(R.id.txtDoi2);
        TextView txtScore = view.findViewById(R.id.txtTiSo);
        ImageView imvphotoHome = view.findViewById(R.id.imvAnhDoi1);
        ImageView imvphotoAway = view.findViewById(R.id.imvAnhDoi2);

        txtTime.setText(match.getTime());
        txtnameHome.setText(match.getNameHome());;
        txtnameAway.setText(match.getNameAway());;
        txtScore.setText(match.getScoreHome() + "-" +match.getScoreAway());


        if(match.getPhotoHome() != null){
            Picasso.get().load(match.photoHome).into(imvphotoHome);
        }
        if(match.getPhotoAway() != null){
            Picasso.get().load(match.photoAway).into(imvphotoAway);
        }
        return view;
    }
}
