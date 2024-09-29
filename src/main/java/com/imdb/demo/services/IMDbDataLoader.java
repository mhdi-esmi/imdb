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
        List<TitleBasics> titleBasics = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             BufferedReader bufferedReader = new BufferedReader(reader);
             Stream<String> lines = bufferedReader.lines()) {
            lines.skip(1).forEach(line -> {
                String[] fields = line.split("\t");
                titleBasics.add(new TitleBasics(fields[0],fields[1], fields[2], fields[3],
                        Boolean.parseBoolean(fields[4]),
                        fields[5].equals("\\N") ? null : Integer.parseInt(fields[5]),
                        fields[6].equals("\\N") ? null : Integer.parseInt(fields[6]),
                        fields[7].equals("\\N") ? null : Integer.parseInt(fields[7]),
                        Arrays.asList(fields[8].split(","))));
            });
        }
        return titleBasics;
    }

    private List<TitleCrew> loadCrews(String filePath) throws IOException {
        List<TitleCrew> titleCrews = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             BufferedReader bufferedReader = new BufferedReader(reader);
             Stream<String> lines = bufferedReader.lines()) {
            lines.skip(1).forEach(line -> {
                String[] fields = line.split("\t");
                titleCrews.add(new TitleCrew(fields[0],
                        Arrays.asList(fields[1].split(",")),
                        Arrays.asList(fields[2].split(","))));
            });
        }
        return titleCrews;    }


    private List<NameBasics> loadPeople(String filePath) throws IOException {
        List<NameBasics> nameBasics = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             BufferedReader bufferedReader = new BufferedReader(reader);
             Stream<String> lines = bufferedReader.lines()) {
            lines.skip(1).forEach(line -> {
                String[] fields = line.split("\t");
                nameBasics.add(new NameBasics(fields[0],fields[1],
                        fields[2].equals("\\N") ? null : Integer.parseInt(fields[2]),
                        fields[3].equals("\\N") ? null : Integer.parseInt(fields[3]),
                        Arrays.asList(fields[4].split(",")),
                        Arrays.asList(fields[5].split(","))
                ));
            });
        }
        return nameBasics;
    }

    private List<TitlePrincipals> loadPrincipals(String filePath) throws IOException {
        List<TitlePrincipals> titlePrincipals = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             BufferedReader bufferedReader = new BufferedReader(reader);
             Stream<String> lines = bufferedReader.lines()) {
            lines.skip(1).forEach(line -> {
                String[] fields = line.split("\t");
                titlePrincipals.add(new TitlePrincipals(fields[0],
                        fields[1].equals("\\N") ? null : Integer.parseInt(fields[1]),
                        fields[2],fields[3],fields[4],fields[5]));
            });
        }
        return titlePrincipals;
    }

    private List<TitleRatings> loadRatings(String filePath) throws IOException {
        List<TitleRatings> titleRatings = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             BufferedReader bufferedReader = new BufferedReader(reader);
             Stream<String> lines = bufferedReader.lines()) {
            lines.skip(1).forEach(line -> {
                String[] fields = line.split("\t");
                titleRatings.add(new TitleRatings(fields[0],
                        fields[1].equals("\\N") ? null : Double.parseDouble(fields[1]),
                        fields[2].equals("\\N") ? null : Integer.parseInt(fields[2])));
            });
        }
        return titleRatings;
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
