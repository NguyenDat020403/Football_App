package com.example.footballapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.footballapp.R;
import com.example.footballapp.model.Match;
import com.squareup.picasso.Picasso;

import java.util.List;

public class matchLiveAdapter extends BaseAdapter {
    private List<Match> matches;

    public matchLiveAdapter(List<Match> matches) {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_livescore,  parent, false);
        }
        Match match = matches.get(position);
        TextView txtLive = view.findViewById(R.id.txtLive);
        TextView txtTime = view.findViewById(R.id.txtTime);
        TextView txtnameHome = view.findViewById(R.id.txtDoi1);
        TextView txtnameAway = view.findViewById(R.id.txtDoi2);
        TextView txtScore = view.findViewById(R.id.txtTiSo);
        TextView txtBallPossesionHome = view.findViewById(R.id.txtBallPossessionHome);
        TextView txtBallPossesionAway= view.findViewById(R.id.txtBallPossessionAway);
        TextView txtShotsTotalHome = view.findViewById(R.id.txtShotsTotalHome);
        TextView txtShotsTotalAway= view.findViewById(R.id.txtShotsTotalAway);
        TextView txtShotsOnGoalAway= view.findViewById(R.id.txtShotsOnGoalAway);
        TextView txtShotsOnGoalHome= view.findViewById(R.id.txtShotsOnGoalHome);
        TextView txtCornersHome= view.findViewById(R.id.txtCornersHome);
        TextView txtCornersAway= view.findViewById(R.id.txtCornersAway);
        TextView txtOffsidesHome= view.findViewById(R.id.txtOffsidesHome);
        TextView txtOffsidesAway= view.findViewById(R.id.txtOffsidesAway);
        TextView txtFoulsHome= view.findViewById(R.id.txtFoulsHome);
        TextView txtFoulsAway= view.findViewById(R.id.txtFoulsAway);
        TextView txtTimeLive = view.findViewById(R.id.txtTimeLive);

        if(!match.getMatchStatus().equals("Finished") && !match.getMatchStatus().equals("Half Time")){
            txtTimeLive.setText(match.getMatchStatus() + "'");
            txtLive.setVisibility(View.VISIBLE);

        }else{
            txtLive.setVisibility(View.GONE);
            txtTimeLive.setText(match.getMatchStatus());
        }

        txtBallPossesionHome.setText(match.getBallPossessionHome());
        txtBallPossesionAway.setText(match.getBallPossessionAway());

        txtShotsTotalHome.setText(match.getShotsTotalHome());
        txtShotsTotalAway.setText(match.getShotsTotalAway());

        txtShotsOnGoalAway.setText(match.getShotsOnGoalAway());
        txtShotsOnGoalHome.setText(match.getShotsOnGoalHome());

        txtCornersHome.setText(match.getCornersHome());
        txtCornersAway.setText(match.getCornersAway());
        txtOffsidesHome.setText(match.getOffsidesHome());
        txtOffsidesAway.setText(match.getOffsidesAway());
        txtFoulsHome.setText(match.getFoulsHome());
        txtFoulsAway.setText(match.getFoulsAway());




        ImageView imvphotoHome = view.findViewById(R.id.imvAnhDoi1);
        ImageView imvphotoAway = view.findViewById(R.id.imvAnhDoi2);

        txtTime.setText(match.getTime());
        txtnameHome.setText(match.getNameHome());
        txtnameAway.setText(match.getNameAway());
        txtScore.setText(match.getScoreHome() + "-" +match.getScoreAway());



        if (match.getPhotoHome() != null && !match.getPhotoHome().isEmpty()) {
            Picasso.get().load(match.getPhotoHome()).into(imvphotoHome);
        } else {
            Picasso.get().load("https://apiv3.apifootball.com/badges/logo_leagues/152_premier-league.png").into(imvphotoHome);
        }

        if(match.getPhotoAway() != null && !match.getPhotoAway().isEmpty()){
            Picasso.get().load(match.getPhotoAway()).into(imvphotoAway);
        }else {
            Picasso.get().load("https://apiv3.apifootball.com/badges/logo_leagues/152_premier-league.png").into(imvphotoHome);
        }
        return view;
    }
}
