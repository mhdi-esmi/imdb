package com.imdb.demo.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "titles")
public class Title {

    @Id
    @Column(name = "tconst")
    private String tconst;

    @Column(name = "titleType")
    private String titleType;

    @Column(name = "primaryTitle")
    private String primaryTitle;

    @Column(name = "originalTitle")
    private String originalTitle;

    @Column(name = "isAdult")
    private Boolean isAdult;

    @Column(name = "startYear")
    private Integer startYear;

    @Column(name = "endYear")
    private Integer endYear;

    @Column(name = "runtimeMinutes")
    private Integer runtimeMinutes;

    @ElementCollection
    @CollectionTable(name = "title_genres", joinColumns = @JoinColumn(name = "tconst"))
    @Column(name = "genre")
    private List<String> genres;

    // Relationships to other entities

    // Crew information (one-to-one, assuming each title has a unique crew)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tconst", referencedColumnName = "tconst")
    private Crew crew;

    // Ratings (one-to-one, each title has one rating)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tconst", referencedColumnName = "tconst")
    private Rating rating;

    // Principals (one-to-many, one title can have multiple principals)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tconst")
    private List<Principals> principals;

    public Title() {
    }

    public Title(String tconst, String titleType, String primaryTitle, String originalTitle, boolean isAdult, Integer startYear, Integer endYear, Integer runtimeMinutes, List<String> genres) {
        this.tconst = tconst;
        this.titleType = titleType;
        this.primaryTitle = primaryTitle;
        this.originalTitle = originalTitle;
        this.isAdult = isAdult;
        this.startYear = startYear;
        this.endYear = endYear;
        this.runtimeMinutes = runtimeMinutes;
        this.genres = genres;
    }



    public String getTconst() {
        return tconst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Integer getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(Integer runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

}
