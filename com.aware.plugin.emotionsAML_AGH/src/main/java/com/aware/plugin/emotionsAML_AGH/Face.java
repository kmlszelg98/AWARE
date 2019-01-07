package com.aware.plugin.emotionsAML_AGH;

public class Face {

    private String photo = "";
    private String anger;
    private String contempt;
    private String disgust;
    private String fear;
    private String happiness;
    private String neutral;
    private String sadness;
    private String surprise;

    public Face() {
    }

    public Face(String photo, String anger, String contempt, String disgust, String fear, String happiness, String neutral, String sadness, String surprise) {
        this.photo = photo;
        this.anger = anger;
        this.contempt = contempt;
        this.disgust = disgust;
        this.fear = fear;
        this.happiness = happiness;
        this.neutral = neutral;
        this.sadness = sadness;
        this.surprise = surprise;
    }

    public String getPhoto() {
        return photo;
    }

    public String getAnger() {
        return anger;
    }

    public String getContempt() {
        return contempt;
    }

    public String getDisgust() {
        return disgust;
    }

    public String getFear() {
        return fear;
    }

    public String getHappiness() {
        return happiness;
    }

    public String getNeutral() {
        return neutral;
    }

    public String getSadness() {
        return sadness;
    }

    public String getSurprise() {
        return surprise;
    }
}
