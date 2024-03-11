package com.example.footballapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.footballapp.R;
import com.example.footballapp.model.Player;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlayerTopAdapter extends BaseAdapter {
    private List<Player> players;

    public PlayerTopAdapter(List<Player> players) {
        this.players = players;
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Object getItem(int position) {
        return players.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topscore_layout,  parent, false);
        }
        Player cauthu = players.get(position);
        TextView txtTenCauThu = view.findViewById(R.id.txtNameVDV);
        TextView txtSTT = view.findViewById(R.id.txtSTTScore);
        TextView txtCLB = view.findViewById(R.id.txtCauLacBo);
        TextView txtSoBanThang = view.findViewById(R.id.txtSoBanThang);
        ImageView imvPhotoVDV = view.findViewById(R.id.imvPhotoVDV);

        txtTenCauThu.setText(cauthu.getName());
        txtSTT.setText(cauthu.getSTT());
        txtCLB.setText(cauthu.getCLB());
        txtSoBanThang.setText(cauthu.getSoBanThang());
            if(cauthu.Photo != null){
                Picasso.get().load(cauthu.Photo).into(imvPhotoVDV);
            }
            else{
                Picasso.get().load("https://apiv3.apifootball.com/?action=get_players&player_name=E.%20Haaland&APIkey=3610b80f6e2a8098d44998dd4727472e20e396dacbe7a0cea1f201d13330dd3c").into(imvPhotoVDV);
            }



        return view;
    }
}
