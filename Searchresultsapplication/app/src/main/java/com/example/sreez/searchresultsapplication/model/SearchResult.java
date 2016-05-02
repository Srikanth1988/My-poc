package com.example.sreez.searchresultsapplication.model;

/**
 * Created by sreez on 4/30/2016.
 */
public class SearchResult {

    public String description;
    public String place_id;

    public void setPlaceID(String placeID) {
        place_id = placeID;
    }

    public void setDescription(String description) {
        description = description;
    }

    public String getDescription() {

        return description;
    }

    public String getPlaceID() {
        return place_id;
    }
}
