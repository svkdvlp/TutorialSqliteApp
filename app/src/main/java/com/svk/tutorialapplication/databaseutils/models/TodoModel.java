package com.svk.tutorialapplication.databaseutils.models;

/**
 * Created by SvK on 05-05-2018.
 */

public class TodoModel {

    String title;
    String description;
    long notifyTime;

    public TodoModel(String title, String description, long notifyTime) {
        this.title = title;
        this.description = description;
        this.notifyTime = notifyTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getNotifyTime() {
        return notifyTime;
    }
}
