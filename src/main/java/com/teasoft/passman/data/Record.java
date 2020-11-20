package com.teasoft.passman.data;

import java.util.ArrayList;
import java.util.Date;

public class Record implements Comparable<Record> {

    private final ArrayList<RecordEventListenser> eventListeners = new ArrayList<>();
    private String name;
    private String url;
    private String userName;
    private String password;
    private String notes;
    private Date createdDate;
    private Date modifiedDate;

    public Record() {
        this.createdDate = new Date();
        this.modifiedDate = new Date();
    }

    public Record(Record record) {
        this.name = record.name;
        this.url = record.url;
        this.userName = record.userName;
        this.password = record.password;
        this.notes = record.notes;
        this.createdDate = record.createdDate;
        this.modifiedDate = record.modifiedDate;
    }

    public void addEventListener(RecordEventListenser eventListener) {
        this.eventListeners.add(eventListener);
    }

    public void removeEventListener(RecordEventListenser eventListener) {
        this.eventListeners.remove(eventListener);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        this.modifiedDate = new Date();
        for (RecordEventListenser eventListener : this.eventListeners) {
            eventListener.nameChanged(oldName, this.name);
        }
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.modifiedDate = new Date();
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.modifiedDate = new Date();
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.modifiedDate = new Date();
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
        this.modifiedDate = new Date();
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date date) {
        this.modifiedDate = date;
    }

    @Override
    public String toString() {
        return "Name: " + this.getName() + System.lineSeparator() +
                "Url: " + this.getUrl() + System.lineSeparator() +
                "User Name: " + this.getUserName() + System.lineSeparator() +
                "Password: " + this.getPassword() + System.lineSeparator() +
                "Notes: " + this.getNotes() + System.lineSeparator() +
                "Created Date: " + this.getCreatedDate() + System.lineSeparator() +
                "Modified Date: " + this.getModifiedDate() + System.lineSeparator();
    }

    @Override
    public int compareTo(Record o) {
        int result;

        result = this.getName().compareTo(o.getName());
        if (result != 0) {
            return result;
        }

        result = this.getUrl().compareTo(o.getUrl());
        if (result != 0) {
            return result;
        }

        result = this.getUserName().compareTo(o.getUserName());
        if (result != 0) {
            return result;
        }

        result = this.getPassword().compareTo(o.getPassword());
        if (result != 0) {
            return result;
        }

        result = this.getNotes().compareTo(o.getNotes());
        if (result != 0) {
            return result;
        }

        result = this.getCreatedDate().compareTo(o.getCreatedDate());
        if (result != 0) {
            return result;
        }

        result = this.getModifiedDate().compareTo(o.getModifiedDate());
        if (result != 0) {
            return result;
        }

        return result;
    }
}
