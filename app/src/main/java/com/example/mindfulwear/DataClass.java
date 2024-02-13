package com.example.mindfulwear;

public class DataClass {
    private String dataCategory;
    private String dataColour;
    private boolean favourite;
    private String dataImage;
    private String key;

    public DataClass() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataCategory() {
        return dataCategory;
    }

    public String getDataColour() {
        return dataColour;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getDataImage() {
        return dataImage;
    }


    public DataClass(String dataCategory, String dataColour, boolean favourite, String dataImage, String key) {
        this.dataCategory = dataCategory;
        this.dataColour = dataColour;
        this.favourite = favourite;
        this.dataImage = dataImage;
        this.key = key;
    }
}