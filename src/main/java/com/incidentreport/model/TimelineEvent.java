package com.incidentreport.model;

import java.io.Serializable;

/**
 * TimelineEvent
 * -------------
 * Represents a single entry in the incident timeline: what happened, and
 * when. A list of these is displayed in the Timeline Builder JTable.
 */
public class TimelineEvent implements Serializable {

    private String date;        // yyyy-MM-dd
    private String time;        // HH:mm
    private String description;

    public TimelineEvent() {
    }

    public TimelineEvent(String date, String time, String description) {
        this.date = date;
        this.time = time;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return date + " " + time + " - " + description;
    }
}
