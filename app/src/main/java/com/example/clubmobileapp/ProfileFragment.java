package com.example.clubmobileapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private View view;
    public static ActionBar actionBar;
    public static ArrayList<String> studentsDetails = new ArrayList<>();
    public static DatabaseInterface databaseInterface;
    private TextInputLayout oldPasswordText, newPasswordText, confirmPasswordText;

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.changePassword){
            final Dialog dialog = new Dialog(getContext());
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.change_password_profile_layout);
            dialog.show();
            dialog.getWindow().setLayout(650, 700);

            Button confirmBtn = dialog.findViewById(R.id.submit);
            confirmBtn.setFocusable(true);

            Button cancelBtn = dialog.findViewById(R.id.cancel);
            cancelBtn.setFocusable(true);

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            oldPasswordText = dialog.findViewById(R.id.oldPasswordText);
            newPasswordText = dialog.findViewById(R.id.newPasswordText);
            confirmPasswordText = dialog.findViewById(R.id.confirmPasswordText);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String oldPassword = oldPasswordText.getEditText().getText().toString();
                    String newPassword = newPasswordText.getEditText().getText().toString();
                    String confirmPassword = confirmPasswordText.getEditText().getText().toString();

                    if ((oldPassword.isEmpty()) && (newPassword.isEmpty()) && (confirmPassword.isEmpty())) {
                        confirmPasswordText.setError("Required Field");
                        newPasswordText.setError("Required Field");
                        oldPasswordText.setError("Required Field");
                    }else if(newPassword.isEmpty()){
                        newPasswordText.setError("Required Field");
                    }else if(confirmPassword.isEmpty()) {
                        confirmPasswordText.setError("Required Field");
                    }else if(oldPassword.isEmpty()){
                            oldPasswordText.setError("Required Field");
                    }else if(!(newPassword.equals(confirmPassword))){
                        confirmPasswordText.setError("Doesn't match");
                        newPasswordText.setError("Doesn't match");
                    }else if(!(confirmPassword.length() >= 8)){
                        Toast.makeText(getContext(), "Password should be 8 or more characters", Toast.LENGTH_LONG).show();
                    }else if(!(studentsDetails.get(5).equals(oldPassword))){
                        Toast.makeText(getContext(), "Incorrect old password", Toast.LENGTH_LONG).show();
                    }else {
                        //Changing the default password and login count to the new password set by the student
                        Call<StudentsDetails> databaseClass = databaseInterface.changeProfilePassword(Integer.parseInt(studentsDetails.get(0)), newPassword);
                        databaseClass.enqueue(new Callback<StudentsDetails>() {
                            @Override
                            public void onResponse(Call<StudentsDetails> call, Response<StudentsDetails> response) {
                                if(response.body().getResponse().equals("true")){
                                    Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(getContext(), "ERROR: Password update unsuccessfully", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<StudentsDetails> call, Throwable t) {
                                Toast.makeText(getContext(),"Can't change password" , Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }
            });
        }else if(item.getItemId() == R.id.closeProfile){
            startActivity(new Intent(getContext(), MainActivity.class));
            MainActivity.snackbar.dismiss();
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        databaseInterface = Retrofit_Instance.getInstance().create(DatabaseInterface.class);
        TextView student_name = view.findViewById(R.id.profile_name);
        TextView student_level = view.findViewById(R.id.profile_level);
        TextView student_id = view.findViewById(R.id.profile_id);
        TextView student_prog = view.findViewById(R.id.profile_prog);
        TextView student_mail = view.findViewById(R.id.profile_mail);

        student_id.setText(studentsDetails.get(0));
        student_name.setText(studentsDetails.get(1) +" "+studentsDetails.get(2));
        student_level.setText(studentsDetails.get(6));
        student_prog.setText(studentsDetails.get(7));
        student_mail.setText(studentsDetails.get(4));

        Call<ClubsDetails> clubDetails = databaseInterface.registeredClubs();
        clubDetails.enqueue(new Callback<ClubsDetails>() {
            @Override
            public void onResponse(Call<ClubsDetails> call, Response<ClubsDetails> response) {
                if(response.body().getClubsDetails().get(0).getResponse().equals("true")){
                    RegisteredClubsAdapter adapter = new RegisteredClubsAdapter(getContext(), response.body().getClubsDetails());
                    ListView registeredClubs = view.findViewById(R.id.registeredClubsList);
                    registeredClubs.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ClubsDetails> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to load registered clubs!", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

}
