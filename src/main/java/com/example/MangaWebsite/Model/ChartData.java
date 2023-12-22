package com.example.MangaWebsite.Model;

public class ChartData {

    private String dayOfWeek;
    private Long numberOfStories;

    public ChartData(String dayOfWeek, Long numberOfStories) {
        this.dayOfWeek = dayOfWeek;
        this.numberOfStories = numberOfStories;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public Long getNumberOfStories() {
        return numberOfStories;
    }

    public void setNumberOfStories(Long numberOfStories) {
        this.numberOfStories = numberOfStories;
    }

    // Getters và setters
}
