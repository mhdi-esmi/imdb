package com.imdb.demo.entities;

import java.util.List;

public class TitleCrew {

    private String tconst;
    private List<String> directors;
    private List<String> writers;

    public TitleCrew() {
    }

    public TitleCrew(String tconst, List<String> directors, List<String> writers) {
        this.tconst = tconst;
        this.directors = directors;
        this.writers = writers;
    }

    public String getTconst() {
        return tconst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public List<String> getWriters() {
        return writers;
    }

    public void setWriters(List<String> writers) {
        this.writers = writers;
    }
}
