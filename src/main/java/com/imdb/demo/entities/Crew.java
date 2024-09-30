package com.imdb.demo.entities;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "crew")
public class Crew {

    @Id
    @Column(name = "tconst")
    private String tconst;

    @ElementCollection
    @CollectionTable(name = "crew_directors", joinColumns = @JoinColumn(name = "tconst"))
    @Column(name = "director")
    private List<String> directors;

    @ElementCollection
    @CollectionTable(name = "crew_writers", joinColumns = @JoinColumn(name = "tconst"))
    @Column(name = "writer")
    private List<String> writers;

    public Crew() {
    }

    public Crew(String tconst, List<String> directors, List<String> writers) {
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
