package com.example.clubmobileapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubBasicInfoFragment extends Fragment {
    private View view;
    ArrayList<String> clubBasicInfo = ClubsInfo.basicInfo;
    public static DatabaseInterface databaseInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.club_basic_info_fragment, container, false);
        databaseInterface = Retrofit_Instance.getInstance().create(DatabaseInterface.class);

        final TextView club_history = view.findViewById(R.id.historyText);
        final TextView club_mission = view.findViewById(R.id.missionStatement);
        final TextView club_meetings = view.findViewById(R.id.meetingDaysText);

        //clubBasicInfo.addAll(ClubsInfo.basicInfo);


        final Call<ClubsDetails> clubDetails = databaseInterface.clubSingleInfo(ClubsInfo.club_position);
        clubDetails.enqueue(new Callback<ClubsDetails>() {
            @Override
            public void onResponse(Call<ClubsDetails> call, final Response<ClubsDetails> response) {
                if(response.body().getClubsDetails().get(0).getResponse().equals("true")){
                    club_history.setText(response.body().getClubsDetails().get(0).getClub_history());
                    club_mission.setText("Club mission will be communicated soon");
                    club_meetings.setText(response.body().getClubsDetails().get(0).getMeeting_days()+" at "+response.body().getClubsDetails().get(0).getMeeting_time());

                    //ClubBasicInfoFragment.clubBasicInfo.addAll(basicInfo);

                    //Toast.makeText(getApplicationContext(), basicInfo.size()+"", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ClubsDetails> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //Toast.makeText(getContext(), ClubsInfo.basicInfo.size()+"", Toast.LENGTH_SHORT).show();
//        club_history.setText(basicInfo.get(3));
//        club_mission.setText("Will be updated shortly");
//        club_meetings.setText(basicInfo.get(0) +" at "+basicInfo.get(1)+ " exactly");

        return view;
    }
}
