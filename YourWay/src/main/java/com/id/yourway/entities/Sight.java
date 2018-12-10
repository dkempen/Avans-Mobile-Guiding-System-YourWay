package com.id.yourway.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class Sight implements Comparable<Sight>, Serializable {
    private final static String TAG = Sight.class.getSimpleName();

    private int id;
    private int published;
    private long date;
    private long dateModified;
    private int authorID;
    private double latitude;
    private double longitude;
    private String address;
    private int numberOnMap;
    private String videoUrl;
    private String year;
    private String photographer;
    private String author;
    private String titleNL;
    private String titleEN;
    private String urlNL;
    private String urlEN;
    private String descriptionNL;
    private String descriptionEN;
    private String materialNL;
    private String materialEN;
    private String catogoryNL;
    private String catogoryEN;
    private List<String> images;

    public Sight(int id, int published, long date, long dateModified, int authorID, double latitude, double longitude, String address, int numberOnMap, String videoUrl, String year, String photographer, String author, String titleNL, String titleEN, String urlNL, String urlEN, String descriptionNL, String descriptionEN, String materialNL, String materialEN, String catogoryNL, String catogoryEN, List<String> images) {
        this.id = id;
        this.published = published;
        this.date = date;
        this.dateModified = dateModified;
        this.authorID = authorID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.numberOnMap = numberOnMap;
        this.videoUrl = videoUrl;
        this.year = year;
        this.photographer = photographer;
        this.author = author;
        this.titleNL = titleNL;
        this.titleEN = titleEN;
        this.urlNL = urlNL;
        this.urlEN = urlEN;
        this.descriptionNL = descriptionNL;
        this.descriptionEN = descriptionEN;
        this.materialNL = materialNL;
        this.materialEN = materialEN;
        this.catogoryNL = catogoryNL;
        this.catogoryEN = catogoryEN;
        this.images = images;
    }

    private String getvariable(String NL, String EN){
        if(Locale.getDefault().getLanguage().equals("nl"))
            return NL;
        return EN;
    }

    public int getId() {
        return id;
    }

    public int getPublished() {
        return published;
    }

    public long getDate() {
        return date;
    }

    public long getDateModified() {
        return dateModified;
    }

    public int getAuthorID() {
        return authorID;
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

    public int getNumberOnMap() {
        return numberOnMap;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getYear() {
        return year;
    }

    public String getPhotographer() {
        return photographer;
    }


    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return getvariable(titleNL, titleEN);
    }

    public String getUrl() {
        return getvariable(urlNL, urlEN);
    }

    public String getDescription() {
        return getvariable(descriptionNL, descriptionEN);
    }

    public String getMaterial() {
        return getvariable(materialNL, materialEN);
}

    public String getCatogoryNL() {
        return getvariable(catogoryNL, catogoryEN);
    }

    public List<String> getImages() {
            return images;
        }

    @Override
    public String toString() {
        return "BlindWall{" +
                "id=" + id +
                ", published=" + published +
                ", date=" + date +
                ", dateModified=" + dateModified +
                ", authorID=" + authorID +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", numberOnMap=" + numberOnMap +
                ", videoUrl='" + videoUrl + '\'' +
                ", year='" + year + '\'' +
                ", photographer='" + photographer + '\'' +
                ", author='" + author + '\'' +
                ", title=" + getTitle() +
                ", url=" + getUrl() +
                ", description=" + getDescription() +
                ", images=" + images.toString() +
                '}';
    }

    @Override
    public int compareTo(Sight o) {
        return 0;
    }

}
