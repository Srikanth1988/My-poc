package com.example.sreez.searchresultsapplication.model;

import java.util.List;

/**
 * Created by sreez on 4/30/2016.
 */
public class RawSearchResultResponse {
   public String status;

    public List<SearchResult> getPredictions() {
        return predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setPredictions(List<SearchResult> predictions) {
        this.predictions = predictions;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SearchResult> predictions;

}
