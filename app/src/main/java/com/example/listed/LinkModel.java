package com.example.listed;

public class LinkModel {

    private String imageUrl;
    private String title;
    private String timesAgo;
    private String totalClicks;
    private String smartLink;

    public LinkModel(String imageUrl, String title, String timesAgo, String totalClicks, String smartLink) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.timesAgo = timesAgo;
        this.totalClicks = totalClicks;
        this.smartLink = smartLink;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getTimesAgo() {
        return timesAgo;
    }

    public String getTotalClicks() {
        return totalClicks;
    }

    public String getSmartLink() {
        return smartLink;
    }
}
