package com.example.clubmobileapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewReminderFragment extends Fragment {
    private View view;
    public static ActionBar actionBar;
    public static ListView reminderList;
    private SearchView searchBar;
    public static ArrayList<String> studentsDetails = new ArrayList<>();
    public static ArrayList<ReminderDetails> reminderDetails = new ArrayList<>();
    public static DatabaseInterface databaseInterface;
    public static ReminderAdapter adapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search_icon){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, new SearchFragment(), "SEARCH_FRAG").addToBackStack("REMINDER_FRAG").commit();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.view_reminders_fragment, container, false);
        databaseInterface = Retrofit_Instance.getInstance().create(DatabaseInterface.class);
        actionBar.setTitle("Reminders");
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton addReminder = view.findViewById(R.id.add_reminder_fab);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFrame, new AddReminderFragment(), "ADD_REMINDER_FRAG").addToBackStack("REMINDER_FRAG").commit();
            }
        });

        reminderList = view.findViewById(R.id.reminderList);
        final TextView nonReminder = view.findViewById(R.id.non_ReminderText);
        Call<ReminderDetails> reminderDetails = databaseInterface.getReminder(Integer.parseInt(studentsDetails.get(0)));
        reminderDetails.enqueue(new Callback<ReminderDetails>() {
            @Override
            public void onResponse(Call<ReminderDetails> call, Response<ReminderDetails> response) {
//                if(response.body().getReminderDetails().get(0).getResponse().equals("true")){
                    //Toast.makeText(getContext(), "Loading successfully", Toast.LENGTH_LONG).show();
                actionBar.setSubtitle(response.body().getReminderDetails().size()+" reminders");
                if(response.body().getReminderDetails().size() == 0){
                    nonReminder.setVisibility(View.VISIBLE);
                }else {
                    adapter = new ReminderAdapter(getContext(), response.body().getReminderDetails(), nonReminder);
                    reminderList.setFocusable(false);
                    reminderList.setAdapter(adapter);
                    //           }
                }
            }

            @Override
            public void onFailure(Call<ReminderDetails> call, Throwable t) {

            }
        });




        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.search_icon);
        searchBar = (SearchView) item.getActionView();
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
