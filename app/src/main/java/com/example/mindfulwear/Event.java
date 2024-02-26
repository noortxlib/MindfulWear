package com.example.mindfulwear;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

public class Event {
    private LocalDate date;
    private String jacketImageURL;
    private String topImageURL;
    private String bottomImageURL;
    private String shoeImageURL;

    public static ArrayList<Event> eventsList = new ArrayList<>();

    public Event() {

    }

    public Event(LocalDate date, String jacketImageURL, String topImageURL, String bottomImageURL, String shoeImageURL) {
        this.date = date;
        this.jacketImageURL = jacketImageURL;
        this.topImageURL = topImageURL;
        this.bottomImageURL = bottomImageURL;
        this.shoeImageURL = shoeImageURL;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getJacketImageURL() {
        return jacketImageURL;
    }

    public void setJacketImageURL(String jacketImageURL) {
        this.jacketImageURL = jacketImageURL;
    }

    public String getTopImageURL() {
        return topImageURL;
    }

    public void setTopImageURL(String topImageURL) {
        this.topImageURL = topImageURL;
    }

    public String getBottomImageURL() {
        return bottomImageURL;
    }

    public void setBottomImageURL(String bottomImageURL) {
        this.bottomImageURL = bottomImageURL;
    }

    public String getShoeImageURL() {
        return shoeImageURL;
    }

    public void setShoeImageURL(String shoeImageURL) {
        this.shoeImageURL = shoeImageURL;
    }

}
