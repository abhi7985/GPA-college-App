package com.developergpaurai.collegeapp;

public class message_dataholder {
    String name,message,date,time,image_name,pdf_name;




    public message_dataholder(String name, String message, String date, String time, String image_name, String pdf_name) {
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
        this.image_name = image_name;
        this.pdf_name = pdf_name;
    }

    public message_dataholder(){

    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPdf_name() {
        return pdf_name;
    }

    public void setPdf_name(String pdf_name) {
        this.pdf_name = pdf_name;
    }


}
