package com.example.clubmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private View view;
    public static DatabaseInterface databaseInterface;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search_icon){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, new SearchFragment(), "SEARCH_FRAG").addToBackStack("HOME_FRAG").commit();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.home_fragment, container, false);
        databaseInterface = Retrofit_Instance.getInstance().create(DatabaseInterface.class);

        Call<ClubsDetails> clubDetails = databaseInterface.clubDetails();
        clubDetails.enqueue(new Callback<ClubsDetails>() {
            @Override
            public void onResponse(Call<ClubsDetails> call, final Response<ClubsDetails> response) {
                if(response.body().getClubsDetails().get(0).getResponse().equals("true")){
                    //Toast.makeText(getContext(), "Loading clubs", Toast.LENGTH_LONG).show();
                    ListView clubList = view.findViewById(R.id.clubsList);
                    MainActivity.clubsSize = response.body().getClubsDetails().size();
                    ClubsDisplayAdapter adapter = new ClubsDisplayAdapter(getActivity(), response.body().getClubsDetails());
                    clubList.setAdapter(adapter);
                    clubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(getActivity(), ClubsInfo.class).putExtra("club_selected",position +1));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ClubsDetails> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
