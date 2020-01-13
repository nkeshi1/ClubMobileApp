package com.example.clubmobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private AlertDialog.Builder logoutAlert;
    private static boolean isLoggedIn = false;
    public static Snackbar snackbar;
    private String full_name, id;
    public static int clubsSize;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AddReminderFragment.actionBar = getSupportActionBar();
        ProfileFragment.actionBar = getSupportActionBar();
        ViewReminderFragment.actionBar = getSupportActionBar();
        SearchFragment.actionBar = getSupportActionBar();

        full_name = getIntent().getStringExtra("fullname");
        id = getIntent().getStringExtra("id");

        View view = findViewById(R.id.mainFrame);
        snackbar = Snackbar.make(view,  "Welcome " + full_name, Snackbar.LENGTH_LONG);
        snackbar.show();

        //Giving Accessibility of actionBar to grand-child fragments of current activity
        isLoggedIn = true;

        getSupportActionBar().setTitle("UPSA Clubs");
        getSupportActionBar().setSubtitle(clubsSize+" clubs available");

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorYellow));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, new HomeFragment(), "HOME_FRAG").commit();

        logoutAlert = new AlertDialog.Builder(this);
        logoutAlert.setTitle("Confirm Logout");
        logoutAlert.setMessage("Are you sure you want to logout?");
        logoutAlert.setCancelable(false);
        logoutAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isLoggedIn = false;
                startActivity(new Intent(MainActivity.this, StudentLogin.class));
            }
        });
        logoutAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutAlert.create().dismiss();
            }
        });

        drawerLayout = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView drawerName = headerView.findViewById(R.id.drawer_name);
        drawerName.setText(full_name);
        TextView drawerID = headerView.findViewById(R.id.drawer_id);
        drawerID.setText(id);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home:
                        getSupportActionBar().setTitle("UPSA Clubs");
                        getSupportActionBar().setSubtitle(clubsSize+" clubs available");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFrame, new HomeFragment(), "HOME_FRAG").commit();
                        break;
                    case R.id.reminders:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFrame, new ViewReminderFragment(), "REMINDER_FRAG").addToBackStack("HOME_FRAG").commit();
                        break;
                    case R.id.events:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportActionBar().setTitle("Campus events");
                        getSupportActionBar().setSubtitle(null);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFrame, new CampusEventFragment(), "EVENTS_FRAG").addToBackStack("HOME_FRAG").commit();
                        break;
                    case R.id.profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        getSupportActionBar().setTitle("User Profile");
                        getSupportActionBar().setSubtitle(null);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFrame, new ProfileFragment(), "PROFILE_FRAG").addToBackStack("HOME_FRAG").commit();
                        break;
                    case R.id.logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        logoutAlert.create().show();
                        break;
                    default:
                        getSupportActionBar().setTitle(null);
                        getSupportActionBar().setSubtitle(null);
                        drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }else if(item.getItemId() == R.id.search_icon){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame, new SearchFragment(), "SEARCH_FRAG").addToBackStack("HOME_FRAG").commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0){
            getSupportFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }
}
