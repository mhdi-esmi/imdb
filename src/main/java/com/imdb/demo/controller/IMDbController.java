package com.imdb.demo.controller;

import com.imdb.demo.entities.NameBasics;
import com.imdb.demo.entities.TitleBasics;
import com.imdb.demo.services.IMDbDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("imdb/api/v1")
public class IMDbController {
    private final IMDbDataLoader dataLoader;

    @Autowired
    public IMDbController(IMDbDataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }
    @GetMapping("/same-director-writer-alive")
    public List<TitleBasics> getTitlesSameDirectorWriterAlive() {
        Map<String, NameBasics> peopleMap = dataLoader.getPeople().stream()
                .collect(Collectors.toMap(NameBasics::getNconst, person -> person));

        return dataLoader.getCrews().stream()
                .filter(crew -> crew.getDirectors().equals(crew.getWriters()))
                .filter(crew -> crew.getDirectors().stream().anyMatch(
                        directorId -> {
                            NameBasics director = peopleMap.get(directorId);
                            return director != null && director.getDeathYear() == null;
                        }))
                .map(crew -> dataLoader.getTitles().stream()
                        .filter(title -> title.getTconst().equals(crew.getTconst()))
                        .findFirst().orElse(null))
                .collect(Collectors.toList());
    }
    @GetMapping("/actors")
    public List<TitleBasics> getTitlesByActors(@RequestParam String actor1, @RequestParam String actor2) {
        Map<String, List<String>> titlesWithActors = dataLoader.getPrincipals().stream()
                .collect(Collectors.groupingBy(TitlePrincipals::getTconst ,
                        Collectors.mapping(TitlePrincipals::getNconst, Collectors.toList())));

        return titlesWithActors.entrySet().stream()
                .filter(entry -> entry.getValue().contains(actor1) && entry.getValue().contains(actor2))
                .map(entry -> dataLoader.getTitles().stream()
                        .filter(title -> title.getTconst().equals(entry.getKey()))
                        .findFirst().orElse(null))
                .collect(Collectors.toList());
    }
    @GetMapping("/best-titles-by-genre")
    public ResponseEntity<List<TitleBasics>> getBestTitlesByGenre(@RequestParam String genre) {
        Map<Integer, TitleBasics> bestTitlesPerYear = dataLoader.getTitles().stream()
                .filter(title -> title.getGenres().contains(genre))
                .collect(Collectors.groupingBy(TitleBasics::getStartYear,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(TitleBasics::getRating)),
                                Optional::get)));

        return  new ResponseEntity<>( new ArrayList<>(bestTitlesPerYear.values()), HttpStatus.OK);
    }
}
