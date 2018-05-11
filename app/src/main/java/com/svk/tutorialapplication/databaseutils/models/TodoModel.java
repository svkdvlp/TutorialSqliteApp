package com.svk.tutorialapplication.databaseutils.models;

import java.io.Serializable;

/**
 * Created by SvK on 05-05-2018.
 */

public class TodoModel implements Serializable {

    String title;
    String description;
    long notifyTime;
    int id;

    public TodoModel(String title, String description, long notifyTime) {
        this.title = title;
        this.description = description;
        this.notifyTime = notifyTime;
    }

    public TodoModel(int id, String title, String description, long notifyTime) {
        this.id = id;
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

    public int getId() {
        return id;
    }
}
