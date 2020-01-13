package com.example.clubmobileapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RegisteredClubsAdapter extends ArrayAdapter<ClubsDetails> {

    public RegisteredClubsAdapter(Context context, List<ClubsDetails> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClubsDetails details = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.clubs_registered_layout, parent, false);

        TextView club_name = convertView.findViewById(R.id.reg_clubName);
        TextView club_type = convertView.findViewById(R.id.reg_clubType);
        TextView date = convertView.findViewById(R.id.reg_dateReg);
        club_name.setText(details.getClub_name());
        club_type.setText(details.getClub_type());
        date.setText(details.getRegistry_time());

        return convertView;
    }
}
