package com.example.clubmobileapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentLogin extends AppCompatActivity {
    private String index_number, password, new_password, confirm_password;
    private TextInputLayout indexTextInput, passwordTextInput, newPasswordText, confirmPasswordText;
    private ProgressDialog progressBar;
    public static DatabaseInterface databaseInterface;
    private int id, loginCount;
    private ArrayList<String> details = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorAsh));
        databaseInterface = Retrofit_Instance.getInstance().create(DatabaseInterface.class);

        indexTextInput = findViewById(R.id.index_number_Textinput);
        passwordTextInput = findViewById(R.id.passwordTextInput);

        progressBar = new ProgressDialog(StudentLogin.this);
        progressBar.setTitle("Checking details");
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(true);

        Button login_btn = findViewById(R.id.loginBtn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating a progressbar
                progressBar.show();
                index_number = indexTextInput.getEditText().getText().toString();
                password = passwordTextInput.getEditText().getText().toString();

                if (index_number.isEmpty() && password.isEmpty()) {
                    indexTextInput.setError("Index number is required");
                    passwordTextInput.setError("Password is required");
                    progressBar.dismiss();
                } else if (index_number.isEmpty()) {
                    indexTextInput.setError("Index number is required");
                    passwordTextInput.setError(null);
                    progressBar.dismiss();
                } else if (password.isEmpty()) {
                    passwordTextInput.setError("Password is required");
                    indexTextInput.setError(null);
                    progressBar.dismiss();
                } else {
                    indexTextInput.setError(null);
                    passwordTextInput.setError(null);
                    StudentsDetails database = new StudentsDetails(Integer.parseInt(index_number), password);
                    Call<StudentsDetails> databaseClass = databaseInterface.checkCredentials(database);
                    databaseClass.enqueue(new Callback<StudentsDetails>() {
                        @Override
                        public void onResponse(Call<StudentsDetails> call, Response<StudentsDetails> response) {
                            if (response.body().getResponse().equals("true")) {
                                id = response.body().getStudent_id();
                                loginCount = response.body().getLoginCount();

                                //Toast.makeText(getApplicationContext(), ""+loginCount, Toast.LENGTH_LONG).show();

                                details.add(String.valueOf(response.body().getStudent_id()));
                                details.add(response.body().getFirst_name());
                                details.add(response.body().getLast_name());
                                details.add(String.valueOf(response.body().getPhone()));
                                details.add(response.body().getEmail());
                                details.add(response.body().getPassword());
                                details.add(String.valueOf(response.body().getStudent_level()));
                                details.add(response.body().getProgramme());

                                //Sending user details to every part of the app

                                AddReminderFragment.email = response.body().getEmail();

                                //See if this is the first login of the student, if yes change the password else just login..
                                if(loginCount == 0){
                                    progressBar.dismiss();
                                    dialog = new Dialog(StudentLogin.this);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.change_password_layout);
                                    dialog.show();
                                    dialog.getWindow().setLayout(650, 600);

                                    Button confirmBtn = dialog.findViewById(R.id.confirmBtn);
                                    confirmBtn.setFocusable(true);

                                    Button cancelBtn = dialog.findViewById(R.id.cancelBtn);
                                    cancelBtn.setFocusable(true);

                                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    newPasswordText = dialog.findViewById(R.id.newPasswordText);
                                    confirmPasswordText = dialog.findViewById(R.id.confirmPasswordText);
                                    confirmBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new_password = newPasswordText.getEditText().getText().toString().trim();
                                            confirm_password = confirmPasswordText.getEditText().getText().toString().trim();

                                            if ((new_password.isEmpty()) && (confirm_password.isEmpty())) {
                                                confirmPasswordText.setError("Required Field");
                                                newPasswordText.setError("Required Field");
                                            }else if(new_password.isEmpty()){
                                                newPasswordText.setError("Required Field");
                                                confirmPasswordText.setError(null);
                                            }else if(confirm_password.isEmpty()){
                                                confirmPasswordText.setError("Required Field");
                                                newPasswordText.setError(null);
                                            }else if(!(new_password.equals(confirm_password))){
                                                confirmPasswordText.setError("Doesn't match");
                                                newPasswordText.setError("Doesn't match");
                                            }else if(!(new_password.length() >= 8)){
                                                Toast.makeText(getApplicationContext(), "Password should be 8 or more characters", Toast.LENGTH_LONG).show();
                                                confirmPasswordText.setError(null);
                                                newPasswordText.setError(null);
                                            }else {
                                                //Changing the default password and login count to the new password set by the student
                                                Call<StudentsDetails> databaseClass = databaseInterface.changePassword(id, new_password);
                                                databaseClass.enqueue(new Callback<StudentsDetails>() {
                                                    @Override
                                                    public void onResponse(Call<StudentsDetails> call, Response<StudentsDetails> response) {
                                                        if(response.body().getResponse().equals("true")){
                                                            details.set(5, new_password);
                                                            ProfileFragment.studentsDetails.addAll(details);
                                                            AddReminderFragment.studentsDetails.addAll(details);
                                                            ViewReminderFragment.studentsDetails.addAll(details);
                                                            Intent mainPage = new Intent(getApplicationContext(), MainActivity.class);
                                                            //Toast.makeText(getApplicationContext(), response.body().getFirst_name() + " " + response.body().getLast_name(), Toast.LENGTH_LONG).show();
                                                            mainPage.putExtra("fullname", details.get(1) + " " + details.get(2));
                                                            mainPage.putExtra("id", details.get(0));
                                                            startActivity(mainPage);
                                                        }else{
                                                            Toast.makeText(getApplicationContext(),"False response", Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<StudentsDetails> call, Throwable t) {
                                                        Toast.makeText(getApplicationContext(),"Can't change password" , Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else{
                                    Intent mainPage = new Intent(getApplicationContext(), MainActivity.class);
                                    mainPage.putExtra("fullname", response.body().getFirst_name() + " " + response.body().getLast_name());
                                    mainPage.putExtra("id", details.get(0));
                                    startActivity(mainPage);
                                }
                                //progressBar.dismiss();
                            }else{
                                Toast.makeText(getApplicationContext(), "Not a student of UPSA", Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<StudentsDetails> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),t.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    });
                    //progressBar.dismiss();
                }
            }
        });
    }
}