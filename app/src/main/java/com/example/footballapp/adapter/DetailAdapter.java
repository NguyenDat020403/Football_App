package com.example.footballapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.footballapp.R;
import com.example.footballapp.model.Detail;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailAdapter extends BaseAdapter {
    private List<Detail> details;

    public DetailAdapter(List<Detail> details) {
        this.details = details;
    }

    @Override
    public int getCount() {
        return details.size();
    }

    @Override
    public Object getItem(int position) {
        return details.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_team,  parent, false);
        }
        Detail detail = details.get(position);
        TextView txtNamePlayer = view.findViewById(R.id.txtnamePlayer);
        if (txtNamePlayer != null) {
            txtNamePlayer.setText(detail.getNamePlayer());
        }

        TextView txtTypePlayer = view.findViewById(R.id.txttypePlayer);
        if (txtTypePlayer != null) {
            txtTypePlayer.setText(detail.getTypePlayer());
        }
        TextView txtAgePlayer = view.findViewById(R.id.txtagePlayer);
        if (txtAgePlayer != null) {
            txtAgePlayer.setText(detail.getAgePlayer());
        }
        TextView txtNumberPlayer = view.findViewById(R.id.txtnumberPlayer);
        if (txtNumberPlayer != null) {
            txtNumberPlayer.setText(detail.getNumberPlayer());
        }
        ImageView imvPhotoPlayer = view.findViewById(R.id.imvphotoPlayer);


        if(detail.getPhotoPlayer() != null){
            Picasso.get().load(detail.getPhotoPlayer()).into(imvPhotoPlayer);
        } else {
            // Thay thế URL mặc định bên dưới bằng URL mong muốn
            Picasso.get().load("https://apiv3.apifootball.com/badges/players/32198_s-carson.jpg").into(imvPhotoPlayer);
        }


        return view;
    }
}
