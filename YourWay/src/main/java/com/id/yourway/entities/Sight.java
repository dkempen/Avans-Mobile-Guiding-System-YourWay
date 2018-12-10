package com.id.yourway.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class Sight implements Comparable<Sight>, Serializable {
    private final static String TAG = Sight.class.getSimpleName();

    private int id;
    private long date;
    private double latitude;
    private double longitude;
    private String address;
    private String videoUrl;
    private String photographer;
    private String author;
    private String titleNL;
    private String titleEN;
    private String descriptionNL;
    private String descriptionEN;
    private String materialNL;
    private String materialEN;
    private String catogoryNL;
    private String catogoryEN;
    private List<String> images;

    public Sight(int id, long date, double latitude, double longitude, String address,
                 String videoUrl, String photographer, String author, String titleNL,
                 String titleEN, String descriptionNL, String descriptionEN, String materialNL,
                 String materialEN, String catogoryNL, String catogoryEN, List<String> images) {
        this.id = id;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.videoUrl = videoUrl;
        this.photographer = photographer;
        this.author = author;
        this.titleNL = titleNL;
        this.titleEN = titleEN;
        this.descriptionNL = descriptionNL;
        this.descriptionEN = descriptionEN;
        this.materialNL = materialNL;
        this.materialEN = materialEN;
        this.catogoryNL = catogoryNL;
        this.catogoryEN = catogoryEN;
        this.images = images;
    }

    private String getVariable(String NL, String EN){
        if(Locale.getDefault().getLanguage().equals("nl"))
            return NL;
        return EN;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getPhotographer() {
        return photographer;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return getVariable(titleNL, titleEN);
    }

    public String getDescription() {
        return getVariable(descriptionNL, descriptionEN);
    }

    public String getMaterial() {
        return getVariable(materialNL, materialEN);
}

    public String getCatogory() {
        return getVariable(catogoryNL, catogoryEN);
    }

    public List<String> getImages() {
            return images;
        }

    @Override
    public String toString() {
        return "BlindWall{" +
                "id=" + id +
                ", date=" + date +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", photographer='" + photographer + '\'' +
                ", author='" + author + '\'' +
                ", title=" + getTitle() +
                ", description=" + getDescription() +
                ", images=" + images.toString() +
                '}';
    }

    @Override
    public int compareTo(Sight o) {
        return 0;
    }
}
