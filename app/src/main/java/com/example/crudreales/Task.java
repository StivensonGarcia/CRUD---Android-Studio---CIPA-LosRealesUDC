package com.example.crudreales;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private long id;
    private String title;
    private int completed;
    private String hour;
    private String days;

    public Task() {

    }

    public Task(String title, int completed, String hour, String days) {
        this.title = title;
        this.completed = completed;
        this.hour = hour;
        this.days = days;
    }

    protected Task(Parcel in) {
        id = in.readLong();
        title = in.readString();
        completed = in.readInt();
        hour = in.readString();
        days = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeInt(completed);
        dest.writeString(hour);
        dest.writeString(days);
    }
}
