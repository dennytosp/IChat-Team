package com.example.ichat.HauNguyen.Model;

public class User_Profile {
    private String email;
    private String password;
    private String username;
    private String fullname;
    private String gender;
    private String birthday;

    public User_Profile() {
    }

    public User_Profile(String email, String password, String username, String fullname, String gender, String birthday) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.fullname = fullname;
        this.gender = gender;
        this.birthday = birthday;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
