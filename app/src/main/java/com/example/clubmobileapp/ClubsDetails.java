package com.example.clubmobileapp;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ClubsDetails {
    private String club_id, club_name, club_history, meeting_days, meeting_time, club_type, response;
    private int phone_number;
    private String isRegistered, registry_time;

    @SerializedName("clubsDetails")
    private ArrayList<ClubsDetails> clubsDetails;

    public ArrayList<ClubsDetails> getClubsDetails() { return clubsDetails; }

    public String getClub_name() { return club_name; }

    public String getMeeting_time() { return meeting_time; }

    public int getPhone_number() { return phone_number; }

    public String getRegistry_time() { return registry_time; }

    public void setMeeting_time(String meeting_time) { this.meeting_time = meeting_time; }

    public String getResponse() { return response; }

    public String getIsRegistered() { return isRegistered; }

    public void setIsRegistered(String isRegistered) { this.isRegistered = isRegistered; }

    public String getClub_id() { return club_id; }

    public void setClub_id(String club_id) { this.club_id = club_id; }

    public void setClub_name(String club_name) { this.club_name = club_name; }

    public String getClub_history() { return club_history; }

    public void setClub_history(String club_history) { this.club_history = club_history; }

    public String getMeeting_days() { return meeting_days; }

    public void setMeeting_days(String meeting_days) { this.meeting_days = meeting_days; }

    public String getClub_type() { return club_type; }

    public void setClub_type(String club_type) { this.club_type = club_type; }
}
