package com.developergpaurai.collegeapp;

public class notice_dataholder {
    String name,date,time,notice,id;
    public notice_dataholder(){

    }

    public notice_dataholder(String name, String date, String time, String notice, String id) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.notice = notice;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }


}
