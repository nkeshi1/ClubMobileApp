package com.example.clubmobileapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubsInfo extends AppCompatActivity {
    public static DatabaseInterface databaseInterface;
    private static int REQUEST_CALL = 1;
    public static int club_position;
    private TextView clubType, clubName, registered;
    private TextView title;
    public static ArrayList<String> basicInfo = new ArrayList<>();
    private String history, phone, meeting_days, meeting_time;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.club_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.call_club:
                call_start(phone);
                break;
            case R.id.close_btn:
                onBackPressed();
                break;
            case R.id.mail_club:
                email(clubName.getText().toString(),"",clubName.getText().toString());
                break;
            case R.id.reg_process:
                title.setText("Registration Details");
                getSupportFragmentManager().beginTransaction().replace(R.id.clubInfoFrame, new RegistrationProcess()).commit();
                break;
            case R.id.basic_info:
                title.setText("Basic information");
                getSupportFragmentManager().beginTransaction().replace(R.id.clubInfoFrame, new ClubBasicInfoFragment()).commit();
                break;
            case R.id.club_exec:
                title.setText("Executives Details");
                getSupportFragmentManager().beginTransaction().replace(R.id.clubInfoFrame, new ClubsExecutivesFragment()).commit();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs_info);
        databaseInterface = Retrofit_Instance.getInstance().create(DatabaseInterface.class);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.clubInfoFrame, new ClubBasicInfoFragment(), null).commit();

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorYellow));
        getSupportActionBar().setTitle("Club Info");
        club_position = getIntent().getIntExtra("club_selected", 0);

        Toast.makeText(getApplicationContext(), club_position+"", Toast.LENGTH_LONG).show();

        clubName = findViewById(R.id.clubNameText);
        clubType = findViewById(R.id.clubTypeText);
        registered = findViewById(R.id.registeredText);
        title = findViewById(R.id.infoTitle);

        Call<ClubsDetails> clubDetails = databaseInterface.clubSingleInfo(club_position);
        clubDetails.enqueue(new Callback<ClubsDetails>() {
            @Override
            public void onResponse(Call<ClubsDetails> call, final Response<ClubsDetails> response) {
                if(response.body().getClubsDetails().get(0).getResponse().equals("true")){
                    clubName.setText(response.body().getClubsDetails().get(0).getClub_name());
                    clubType.setText(response.body().getClubsDetails().get(0).getClub_type() +" club");
                    phone = "0"+response.body().getClubsDetails().get(0).getPhone_number();

                    String isReg = response.body().getClubsDetails().get(0).getIsRegistered();
                    if(isReg.equals("true")){
                        registered.setText("Registered");
                    }else{
                        registered.setText("Not Registered");
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "False", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClubsDetails> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void call_start(String phone){
        if(ContextCompat.checkSelfPermission(ClubsInfo.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ClubsInfo.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+phone)));
        }
    }

    private void email(String club_name, String message, String to){
        Intent mail = new Intent(Intent.ACTION_SENDTO);
        mail.setData(Uri.parse("mailto:"));
        mail.putExtra(mail.EXTRA_EMAIL, to);
        mail.putExtra(mail.EXTRA_SUBJECT,"An e-mail to: " + club_name);
        mail.putExtra(mail.EXTRA_TEXT, message);

        if (mail.resolveActivity(getPackageManager()) != null){
            startActivity(mail);
        }
    }
}
