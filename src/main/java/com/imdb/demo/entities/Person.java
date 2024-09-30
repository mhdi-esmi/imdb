package com.imdb.demo.entities;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @Column(name = "nconst")
    private String nconst;

    @Column(name = "primaryName")
    private String primaryName;

    @Column(name = "birthYear")
    private Integer birthYear;

    @Column(name = "deathYear")
    private Integer deathYear;

    @ElementCollection
    @CollectionTable(name = "person_professions", joinColumns = @JoinColumn(name = "nconst"))
    @Column(name = "profession")
    private List<String> primaryProfession;

    @ElementCollection
    @CollectionTable(name = "person_known_titles", joinColumns = @JoinColumn(name = "nconst"))
    @Column(name = "known_for_title")
    private List<String> knownForTitles;

    public Person() {
    }

    public Person(String nconst, String primaryName, Integer birthYear, Integer deathYear, List<String> primaryProfession, List<String> knownForTitles) {
        this.nconst = nconst;
        this.primaryName = primaryName;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.primaryProfession = primaryProfession;
        this.knownForTitles = knownForTitles;
    }

    public String getNconst() {
        return nconst;
    }

    public void setNconst(String nconst) {
        this.nconst = nconst;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public List<String> getPrimaryProfession() {
        return primaryProfession;
    }

    public void setPrimaryProfession(List<String> primaryProfession) {
        this.primaryProfession = primaryProfession;
    }

    public List<String> getKnownForTitles() {
        return knownForTitles;
    }

    public void setKnownForTitles(List<String> knownForTitles) {
        this.knownForTitles = knownForTitles;
    }
}
