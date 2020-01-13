package com.example.clubmobileapp;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ReminderDetails {
    private int timeInterval, hours, minutes, student_id, reminder_id;
    private String userDate, activityType, reminder_channel, activityDate, activityTime, response, timeRegistered, reminder_message;

    @SerializedName("reminderDetails")
    private ArrayList<ReminderDetails> reminderDetails;

    public ReminderDetails(){}

    public ReminderDetails(int timeInterval, int hours, int minutes, String userDate) {
        this.timeInterval = timeInterval;
        this.hours = hours;
        this.minutes = minutes;
        this.userDate = userDate;
    }

    public ArrayList<ReminderDetails> getReminderDetails() { return reminderDetails; }

    public String getActivityDate() { return activityDate; }

    public String getReminder_message() { return reminder_message; }

    public void setReminder_message(String reminder_message) { this.reminder_message = reminder_message; }

    public int getStudent_id() { return student_id; }

    public int getReminder_id() { return reminder_id; }

    public String getReminder_channel() { return reminder_channel; }

    public String getActivityType() { return activityType; }

    public String getTimeRegistered() { return timeRegistered; }

    public void setActivityDate(String activityDate) { this.activityDate = activityDate; }

    public String getActivityTime() {
        return activityTime;
    }

    public String getResponse() { return response; }

    public void setActivityTime(String activityTime) { this.activityTime = activityTime; }

    public void setTimeInterval(int timeInterval) { this.timeInterval = timeInterval; }

    public void setHours(int hours) { this.hours = hours; }

    public void setMinutes(int minutes) { this.minutes = minutes; }

    public void setStudent_id(int student_id) { this.student_id = student_id; }

    public void setReminder_id(int reminder_id) { this.reminder_id = reminder_id; }

    public void setUserDate(String userDate) { this.userDate = userDate; }

    public void setActivityType(String activityType) { this.activityType = activityType; }

    public void setReminder_channel(String reminder_channel) { this.reminder_channel = reminder_channel; }

    public int getTimeInterval() { return timeInterval; }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getUserDate() {
        return userDate;
    }
}
