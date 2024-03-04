package com.example.footballapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TeamAdapter extends BaseAdapter {
    private List<Doibong> teams;

    public TeamAdapter(List<Doibong> teams) {
        this.teams = teams;
    }

    @Override
    public int getCount() {
        return teams.size();
    }

    @Override
    public Object getItem(int position) {
        return teams.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout,  parent, false);
        }

        Doibong team = teams.get(position);

        TextView txtTenDoiBong = view.findViewById(R.id.txtTenDoiBong);
        TextView txtSTT = view.findViewById(R.id.txtSTT);
        TextView txtMatch = view.findViewById(R.id.txtMatch);
        TextView txtWin = view.findViewById(R.id.txtWin);
        TextView txtDraw = view.findViewById(R.id.txtDraw);
        TextView txtLose = view.findViewById(R.id.txtLose);
        TextView txtPoint = view.findViewById(R.id.txtPoint);
        ImageView imvPhotoTeam = view.findViewById(R.id.imvPhotoTeam);
        txtTenDoiBong.setText(team.getName());

        txtSTT.setText(team.getStt());
        txtMatch.setText(team.getMatch());
        txtWin.setText(team.getWin());
        txtDraw.setText(team.getDraw());
        txtLose.setText(team.getLose());
        txtPoint.setText(team.getPoint());
        if(team.Photo != null){
            Picasso.get().load(team.Photo).into(imvPhotoTeam);
        }


        return view;
    }
}
