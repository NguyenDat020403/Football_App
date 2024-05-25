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

public class matchHeadResultAdapter extends RecyclerView.Adapter<matchHeadResultAdapter.ViewHolder>{
    private List<Match> matchList;
    private Context context;

    public matchHeadResultAdapter(List<Match> matchList, Context context) {
        this.matchList = matchList;
        this.context = context;
    }


    @NonNull
    @Override
    public matchHeadResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_head_result, parent, false);
        return new matchHeadResultAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.homeTeamName.setText(match.getNameHome());
        holder.matchScore.setText(" " +match.getScoreHome() + " - " + match.getScoreAway()+ " ");
        holder.awayTeamName.setText(match.getNameAway());
        holder.txtTimeMatch.setText(match.getTime());
        holder.match_status.setText(match.getMatchStatus());
        Glide.with(context).load(match.getPhotoHome()).into(holder.homeTeamBadge);
        Glide.with(context).load(match.getPhotoAway()).into(holder.awayTeamBadge);
    }




    @Override
    public int getItemCount() {
        return matchList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView match_status;
        public TextView homeTeamName;
        public TextView matchScore;
        public TextView awayTeamName;
        public TextView txtTimeMatch;
        public ImageView homeTeamBadge;
        public ImageView awayTeamBadge;

        public ViewHolder(View itemView) {
            super(itemView);
            homeTeamName = itemView.findViewById(R.id.home_team_name);
            matchScore = itemView.findViewById(R.id.match_score);
            txtTimeMatch = itemView.findViewById(R.id.match_date);
            awayTeamName = itemView.findViewById(R.id.away_team_name);
            match_status = itemView.findViewById(R.id.match_status);
            homeTeamBadge = itemView.findViewById(R.id.team_home_badge);
            awayTeamBadge = itemView.findViewById(R.id.team_away_badge);
        }
    }
}
