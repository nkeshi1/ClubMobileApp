package com.example.clubmobileapp;

public class StudentsDetails {
    private String password, first_name, middle_name, last_name, email, response, programme;
    private int student_id, login_count, phone, student_level;

    public StudentsDetails(int id, String password){
        this.student_id = id;
        this.password = password;
    }

    public StudentsDetails(){}

    public void setPassword(String password) { this.password = password; }

    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public void setMiddle_name(String middle_name) { this.middle_name = middle_name; }

    public void setLoginCount(int loginCount) { this.login_count = loginCount; }

    public void setLast_name(String last_name) { this.last_name = last_name; }

    public void setEmail(String email) { this.email = email; }

    public void setResponse(String response) { this.response = response; }

    public void setPhone(int phone) { this.phone = phone; }

    public void setStudent_id(int student_id) { this.student_id = student_id; }

    //Getters
    public String getResponse() { return response; }

    public String getEmail() { return email; }

    public int getStudent_id() {
        return student_id;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() { return first_name; }

    public int getPhone() { return phone; }

    public String getProgramme() { return programme; }

    public int getStudent_level() { return student_level; }

    public int getLoginCount() { return login_count; }

    public String getMiddle_name() {
        return middle_name;
    }

    public String getLast_name() {
        return last_name;
    }
}
