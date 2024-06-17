package com.example.signuploginrealtime;

public class HelperClass {

    String name, email, rollno, password;

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public HelperClass(String name, String email, String rollno, String password) {
        this.name = name;
        this.email = email;
        this.rollno = rollno;
        this.password = password;
    }

    public HelperClass() {
    }
}
