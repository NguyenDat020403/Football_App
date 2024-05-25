package com.example.footballapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.footballapp.R;
import com.example.footballapp.model.Match;

import java.util.List;

public class matchHeadAdapter extends RecyclerView.Adapter<matchHeadAdapter.ViewHolder> {

    private List<Match> matchList;
    private Context context;

    public matchHeadAdapter(List<Match> matchList, Context context) {
        this.matchList = matchList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_headtohead_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.homeTeamName.setText(match.getNameHome());
        holder.matchScore.setText(" " +match.getScoreHome() + " - " + match.getScoreAway()+ " ");
        holder.awayTeamName.setText(match.getNameAway());
        holder.txtTimeMatch.setText(match.getTime() +" - " + match.getMatchStatus());

        Glide.with(context).load(match.getPhotoHome()).into(holder.homeTeamBadge);
        Glide.with(context).load(match.getPhotoAway()).into(holder.awayTeamBadge);
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView homeTeamBadge;
        public TextView homeTeamName;
        public TextView matchScore;
        public TextView awayTeamName;
        public TextView txtTimeMatch;
        public ImageView awayTeamBadge;

        public ViewHolder(View itemView) {
            super(itemView);
            homeTeamBadge = itemView.findViewById(R.id.team_home_badge);
            homeTeamName = itemView.findViewById(R.id.team_home_name);
            matchScore = itemView.findViewById(R.id.match_score);
            txtTimeMatch = itemView.findViewById(R.id.txtTimeMatch);
            awayTeamName = itemView.findViewById(R.id.team_away_name);
            awayTeamBadge = itemView.findViewById(R.id.team_away_badge);
        }
    }
}
