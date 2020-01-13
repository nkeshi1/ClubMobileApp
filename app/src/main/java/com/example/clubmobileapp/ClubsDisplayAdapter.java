package com.example.clubmobileapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ClubsDisplayAdapter extends ArrayAdapter<ClubsDetails> {

    public ClubsDisplayAdapter(Context context, ArrayList<ClubsDetails> clubDetails) {
        super(context, 0, clubDetails);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClubsDetails details = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.clubs_design_layout, parent, false);

        TextView club_name = convertView.findViewById(R.id.club_name);
        TextView club_type = convertView.findViewById(R.id.club_type);
        TextView isRegistered = convertView.findViewById(R.id.isRegistered);
        club_name.setText(details.getClub_name());
        club_type.setText(details.getClub_type());

        if(details.getIsRegistered().equals("true")){
            isRegistered.setText("Club Registered");
            isRegistered.setTextColor(Color.GREEN);
        }
        return convertView;
    }
}
