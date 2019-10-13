package com.guru.managebase;

public class TaskModel {

    private String title;
    private String author;
    private String date;
    private String category;

    public TaskModel() {

    }

    public TaskModel(String title, String author, String date, String category) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
