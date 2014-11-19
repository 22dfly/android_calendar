package com.example.anl2;

public class Event {

    public int id;
    public int year;
    public int month;
    public int day;
    public int hour;
    public String event;
    public int dismiss;

    public void setTime(int year, int month, int day, int hour) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
    }

    public String getTime() {
        String time;
        if (this.hour == 0) {
            time = "12\nam";
        } else if (this.hour == 12) {
            time = "12\npm";
        } else if (this.hour < 12) {
            time = this.hour + "\n" + "am";
        } else {
            time = (this.hour - 12) + "\n" + "pm";
        }

        return time;
    }

    public static String getTime(int hour, boolean lineBreak) {
        String time;
        String charBreak = lineBreak ? "\n" : "";
        if (hour == 0) {
            time = "12" + charBreak + "am";
        } else if (hour == 12) {
            time = "12" + charBreak + "pm";
        } else if (hour < 12) {
            time = hour + charBreak + "am";
        } else {
            time = (hour - 12) + charBreak + "pm";
        }

        return time;
    }
}