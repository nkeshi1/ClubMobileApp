package com.example.clubmobileapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReminderAdapter extends ArrayAdapter<ReminderDetails> {
    public static DatabaseInterface databaseInterface;
    private TextView nonReminderText;

    public ReminderAdapter(Context context, List<ReminderDetails> objects, TextView textView) {
        super(context, 0, objects);
        this.nonReminderText = textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ReminderDetails details = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_layout, parent, false);

        TextView activity = convertView.findViewById(R.id.activityText);
        TextView medium = convertView.findViewById(R.id.mediumText);
        TextView message = convertView.findViewById(R.id.messageText);
        TextView date = convertView.findViewById(R.id.dateText);
        TextView timeRegistered = convertView.findViewById(R.id.timeRegistered);
        TextView reminderTime = convertView.findViewById(R.id.reminderTime);
        ImageView closeBtn = convertView.findViewById(R.id.closeBtn);

        closeBtn.setFocusable(true);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Reminder");
                builder.setMessage("Are you sure you want to delete this reminder?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog progressBar = new ProgressDialog(getContext());
                        progressBar.setTitle("Delete Reminder");
                        progressBar.setMessage("Please wait a minute...");
                        progressBar.setCancelable(true);
                        progressBar.show();

                        //Making a call to database to delete reminder by id...

                        databaseInterface = Retrofit_Instance.getInstance().create(DatabaseInterface.class);
                        Call<ReminderDetails> deleteReminder = databaseInterface.deleteReminder(details.getReminder_id(), details.getStudent_id());
                        deleteReminder.enqueue(new Callback<ReminderDetails>() {
                            @Override
                            public void onResponse(Call<ReminderDetails> call, Response<ReminderDetails> response) {
                                if(response.body().getReminderDetails() != null){
                                    if(response.body().getReminderDetails().size() == 0){
                                        nonReminderText.setVisibility(View.VISIBLE);
                                    }
                                    notifyDataSetChanged();
                                    Toast.makeText(getContext(), "Reminder deleted", Toast.LENGTH_LONG).show();
                                    ReminderAdapter adapter = new ReminderAdapter(getContext(), response.body().getReminderDetails(), nonReminderText);
                                    ViewReminderFragment.reminderList.setAdapter(adapter);
                                    notifyDataSetChanged();
                                    progressBar.dismiss();
                                }else{
                                    nonReminderText.setVisibility(View.VISIBLE);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<ReminderDetails> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();
                    }
                });
                builder.create().show();
            }
        });

        activity.setText(details.getActivityType());
        medium.setText(details.getReminder_channel());
        timeRegistered.setText(details.getTimeRegistered());
        reminderTime.setText(details.getActivityTime());
        message.setText(details.getReminder_message());
        date.setText(details.getActivityDate());
        return convertView;
    }
}
