package com.imdb.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "principals")
public class Principals {

    @Id
    @Column(name = "tconst")
    private String tconst;

    @Column(name = "ordering")
    private Integer ordering;

    @Column(name = "nconst")
    private String nconst;

    @Column(name = "category")
    private String category;

    @Column(name = "job")
    private String job;

    @Column(name = "characters")
    private String characters;


    public Principals() {
    }

    public Principals(String tconst, Integer ordering, String nconst, String category, String job, String characters) {
        this.tconst = tconst;
        this.ordering = ordering;
        this.nconst = nconst;
        this.category = category;
        this.job = job;
        this.characters = characters;
    }

    public String getTconst() {
        return tconst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public String getNconst() {
        return nconst;
    }

    public void setNconst(String nconst) {
        this.nconst = nconst;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }
}
