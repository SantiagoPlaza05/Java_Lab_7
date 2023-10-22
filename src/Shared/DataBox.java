package Shared;

import Entidades.Movie;

import java.io.Serializable;

public class DataBox implements Serializable {
    Movie movie;
    String response;
    String dataFile;
    Long key;
    long oscarsCount;

    public long getOscarsCount() {
        return oscarsCount;
    }

    public void setOscarsCount(long oscarsCount) {
        this.oscarsCount = oscarsCount;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }
}