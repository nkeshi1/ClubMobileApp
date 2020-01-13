package com.example.clubmobileapp;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.clubmobileapp.Application.CHANNEL_ONE_ID;

public class AddReminderFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
   private View view;
   private Button dateTimePicker;
   private TextView dateTimeText;
   private int day, month, year, hours, minutes;
   private String dateTimeInfo = "", reminder_message = "";
   private View startReminder;
   public static String email = "";
   private FrameLayout rootLayout;
   private Date act;
   private TextInputLayout reminderMessage;
   private ArrayList<String> medium = new ArrayList<>();
   private ArrayList<String> activity = new ArrayList<>();
   private String mediumSelected = "";
   private String activitySelected = "";
   private String otherActivityText = null;
   private String actDate = "", actTime = "";
   private EditText otherActivity;
   public static ActionBar actionBar;
   public static ArrayList<String> studentsDetails = new ArrayList<>();
   private NotificationManagerCompat notificationManagerCompat;
   public static DatabaseInterface databaseInterface;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.add_reminder_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.add_reminder_fragment, container, false);
        setHasOptionsMenu(true);
        notificationManagerCompat = NotificationManagerCompat.from(getContext());

        actionBar.setTitle("Add Reminders");
        actionBar.setSubtitle(null);
        actionBar.setDisplayHomeAsUpEnabled(false);

        rootLayout = view.findViewById(R.id.mainFrame);

        dateTimePicker = view.findViewById(R.id.dateTimeBtn);
        dateTimeText = view.findViewById(R.id.txtDateTime);

        dateTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getContext(), AddReminderFragment.this, year, month, day);
                datePicker.show();
            }
        });

        final String[] activity_data = {"An event", "A club meeting", "An exam", "Other"};
        final String[] medium_data = {"An e-mail", "An alarm", "A notification"};

        Spinner frqncySpinner = view.findViewById(R.id.frequencySpinner);
        String[] frequency = {"5 minutes", "10 minutes", "20 minutes", "30 minutes", "1 hour", "2 hours"};
        ArrayAdapter<String> frqcnyAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, frequency);
        frqncySpinner.setAdapter(frqcnyAdapter);

        activity.addAll(Arrays.asList(activity_data));
        medium.addAll(Arrays.asList(medium_data));

        Spinner activity_spinner = view.findViewById(R.id.activitySpinner);
        ArrayAdapter<String>  activity_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, activity);
        activity_spinner.setAdapter(activity_adapter);

        final Spinner medium_spinner = view.findViewById(R.id.mediumSpinner);
        ArrayAdapter<String>  medium_adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, medium);
        medium_spinner.setAdapter(medium_adapter);
        otherActivity = view.findViewById(R.id.txtOtherActivity);

        medium_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mediumSelected = medium_data[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        activity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activitySelected = activity_data[position];
                if(position == 3){
                    otherActivity.setVisibility(View.VISIBLE);
                }else{
                    otherActivity.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.save_reminder:
                String title ="";
                if(mediumSelected.equals(medium.get(0))){
//                    Toast.makeText(getActivity(), "e-mail chosen", Toast.LENGTH_SHORT).show();
                    //sendMail(email, "Reminder", "");
                    title = "E-mail Reminder Set";
                }else if(mediumSelected.equals(medium.get(1))){
//                    Toast.makeText(getActivity(), "alarm chosen", Toast.LENGTH_SHORT).show();
                    createAlarm("", hours, minutes);
                    title = "Alarm Reminder Set";
                }else{
                    title = "Notification Reminder Set";
                }

                if(dateTimeInfo.equals("")){
                    Toast.makeText(getContext(), "Select date and time", Toast.LENGTH_LONG).show();
                }else {
                    //Toast.makeText(getActivity(), "notification chosen", Toast.LENGTH_SHORT).show();
                    reminderMessage = view.findViewById(R.id.reminderMessage);
                    otherActivityText = otherActivity.getText().toString();
                    reminder_message = reminderMessage.getEditText().getText().toString();

                    if(otherActivityText == null){
                        saveReminder(mediumSelected, activitySelected, act, actTime, reminder_message);
                    }else{
                        saveReminder(mediumSelected, otherActivityText, act, actTime, reminder_message);
                    }
                    sendInitialNotification(title);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainFrame, new ViewReminderFragment(), null).commit();
                }
                break;
            case R.id.cancel_reminder:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFrame, new ViewReminderFragment(), null).commit();
                break;
        }
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        act = calendar.getTime();

        TimePickerDialog timePicker = new TimePickerDialog(getContext(), AddReminderFragment.this, hours, minutes, true);
        timePicker.show();
        actDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        dateTimeInfo = "DATE: "+actDate;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        actTime = hourOfDay +":"+ minute;
        dateTimeInfo += "\nTIME: "+ actTime;
        dateTimeText.setText(dateTimeInfo);
        dateTimeText.setVisibility(View.VISIBLE);
    }

    private void sendNotification(String message){
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_ONE_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp).setContentTitle("Get ready for your activity...")
                .setContentText(message).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE).build();

        notificationManagerCompat.notify(2, notification);
    }

    private void sendInitialNotification(String title){
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_ONE_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp).setContentTitle(title)
                .setContentText("Your notification feature has been set. You will be notified at specified time")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.colorYellow))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE).build();

        notificationManagerCompat.notify(1, notification);
    }

    private static void createAlarm(String message, int hour, int minutes){
        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
    }

    public void sendMail(String addresses, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void saveReminder(String channel, String activityType, Date activityDate, String activityTime, String reminder_message){
        databaseInterface = Retrofit_Instance.getInstance().create(DatabaseInterface.class);
        DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd", Locale.getDefault());

        ReminderDetails reminder = new ReminderDetails();
        reminder.setStudent_id(Integer.parseInt(studentsDetails.get(0)));
        reminder.setReminder_channel(channel);
        reminder.setActivityType(activityType);
        reminder.setReminder_message(reminder_message);
        reminder.setActivityDate(dateFormat.format(activityDate));
        reminder.setActivityTime(activityTime);
        Call<ReminderDetails> reminderDetails = databaseInterface.studentReminders(reminder);
        reminderDetails.enqueue(new Callback<ReminderDetails>(){
            @Override
            public void onResponse(Call<ReminderDetails> call, Response<ReminderDetails> response) {
                if(response.body().getReminderDetails().get(0).getResponse().equals("true")){
                    FrameLayout snackView = view.findViewById(R.id.parentView);
                    Snackbar snackbar = Snackbar.make(snackView, "Your reminder has been saved successfully", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ReminderDetails> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
