package com.example.signuploginrealtime;

public class AttendanceClass {
    String Name, Rollno;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRollno() {
        return Rollno;
    }

    public void setRollno(String rollno) {
        Rollno = rollno;
    }

    public AttendanceClass(String name, String rollno) {
        Name = name;
        Rollno = rollno;
    }
}