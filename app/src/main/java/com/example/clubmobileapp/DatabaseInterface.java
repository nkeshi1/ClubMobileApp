package com.example.clubmobileapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DatabaseInterface {
    @POST("login_check.php")
    Call<StudentsDetails> checkCredentials(@Body StudentsDetails userData);

    @GET("displayClubs.php")
    Call<ClubsDetails> clubDetails();

    @GET("registeredClubs.php")
    Call<ClubsDetails> registeredClubs();

    @FormUrlEncoded
    @POST("changeProfilePassword.php")
    Call<StudentsDetails> changeProfilePassword(@Field("student_id") int id, @Field("password") String password);

    @FormUrlEncoded
    @POST("changePassword.php")
    Call<StudentsDetails> changePassword(@Field("student_id") int id, @Field("password") String password);

    @POST("reminders.php")
    Call<ReminderDetails> studentReminders(@Body ReminderDetails reminderDetails);

    @FormUrlEncoded
    @POST("deleteReminder.php")
    Call<ReminderDetails> deleteReminder(@Field("reminder_id") int rid, @Field("student_id") int sid);

    @FormUrlEncoded
    @POST("getReminders.php")
    Call<ReminderDetails> getReminder(@Field("student_id") int sid);


    @FormUrlEncoded
    @POST("loadClubInfo.php")
    Call<ClubsDetails> clubSingleInfo(@Field("club_id") int clubID);
}
