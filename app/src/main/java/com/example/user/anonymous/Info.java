package com.example.user.anonymous;

/**
 * Created by User on 7/31/2017.
 */

public class Info {
    private String Posted;
    private long totalLikes;

    public Info(String posted, long totalLikes) {
        Posted = posted;
        this.totalLikes = totalLikes;
    }
    public Info(){

    }

    public String getPosted() {

        return Posted;
    }

    public void setPosted(String posted) {
        Posted = posted;
    }

    public long getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(long totalLikes) {
        this.totalLikes = totalLikes;
    }
}
