package com.imdb.demo.services;

import com.imdb.demo.entities.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class IMDbDataLoader {

    private List<TitleBasics> titles;
    private List<TitleCrew> crews;
    private List<NameBasics> people;
    private List<TitlePrincipals> principals;
    private List<TitleRatings> ratings;

    @PostConstruct
    public void loadData() throws IOException {
        titles = loadTitles("title.basics.tsv.gz");
        crews = loadCrews("title.crew.tsv.gz");
        people = loadPeople("name.basics.tsv.gz");
        principals = loadPrincipals("title.principals.tsv.gz");
        ratings = loadRatings("title.ratings.tsv.gz");
    }

    private List<TitleBasics> loadTitles(String filePath) throws IOException {
        List<TitleBasics> titles = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             BufferedReader bufferedReader = new BufferedReader(reader);
             Stream<String> lines = bufferedReader.lines()) {
            lines.skip(1).forEach(line -> {
                String[] fields = line.split("\t");
                titles.add(new TitleBasics(fields[0],fields[1], fields[2], fields[3],
                        Boolean.parseBoolean(fields[4]),
                        fields[5].equals("\\N") ? null : Integer.parseInt(fields[5]),
                        fields[6].equals("\\N") ? null : Integer.parseInt(fields[6]),
                        fields[7].equals("\\N") ? null : Integer.parseInt(fields[7]),
                        Arrays.asList(fields[8].split(","))));
            });
        }
        return titles;
    }

    private List<TitleCrew> loadCrews(String filePath) throws IOException {
        List<TitleCrew> crews = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             BufferedReader bufferedReader = new BufferedReader(reader);
             Stream<String> lines = bufferedReader.lines()) {
            lines.skip(1).forEach(line -> {
                String[] fields = line.split("\t");
                crews.add(new TitleCrew(fields[0],
                        Arrays.asList(fields[1].split(",")),
                        Arrays.asList(fields[2].split(","))));
            });
        }
        return crews;    }


    private List<NameBasics> loadPeople(String filePath) throws IOException {
        List<NameBasics> people = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             BufferedReader bufferedReader = new BufferedReader(reader);
             Stream<String> lines = bufferedReader.lines()) {
            lines.skip(1).forEach(line -> {
                String[] fields = line.split("\t");
                people.add(new NameBasics(fields[0],fields[1],
                        fields[2].equals("\\N") ? null : Integer.parseInt(fields[2]),
                        fields[3].equals("\\N") ? null : Integer.parseInt(fields[3]),
                        Arrays.asList(fields[4].split(",")),
                        Arrays.asList(fields[5].split(","))
                ));
            });
        }
        return people;
    }

    private List<TitlePrincipals> loadPrincipals(String filePath) throws IOException {
        List<TitlePrincipals> principals = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             BufferedReader bufferedReader = new BufferedReader(reader);
             Stream<String> lines = bufferedReader.lines()) {
            lines.skip(1).forEach(line -> {
                String[] fields = line.split("\t");
                principals.add(new TitlePrincipals(fields[0],
                        fields[1].equals("\\N") ? null : Integer.parseInt(fields[1]),
                        fields[2],fields[3],fields[4],fields[5]));
            });
        }
        return principals;
    }

    private List<TitleRatings> loadRatings(String filePath) throws IOException {
        List<TitleRatings> ratings = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             BufferedReader bufferedReader = new BufferedReader(reader);
             Stream<String> lines = bufferedReader.lines()) {
            lines.skip(1).forEach(line -> {
                String[] fields = line.split("\t");
                ratings.add(new TitleRatings(fields[0],
                        fields[1].equals("\\N") ? null : Double.parseDouble(fields[1]),
                        fields[2].equals("\\N") ? null : Integer.parseInt(fields[2])));
            });
        }
        return ratings;
    }

    public List<TitleBasics> getTitles() {
        return titles;
    }

    public List<TitleCrew> getCrews() {
        return crews;
    }

    public List<NameBasics> getPeople() {
        return people;
    }

    public List<TitlePrincipals> getPrincipals() {
        return principals;
    }

    public List<TitleRatings> getRatings() {
        return ratings;
    }
}
