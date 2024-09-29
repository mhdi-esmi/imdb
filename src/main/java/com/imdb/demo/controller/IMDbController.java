package com.imdb.demo.controller;

import com.imdb.demo.entities.NameBasics;
import com.imdb.demo.entities.TitleBasics;
import com.imdb.demo.entities.TitlePrincipals;
import com.imdb.demo.entities.TitleRatings;
import com.imdb.demo.services.IMDbDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
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
    public ResponseEntity<List<TitleBasics>> getTitlesSameDirectorWriterAlive() {
        Map<String, NameBasics> peopleMap = dataLoader.getPeople().stream()
                .collect(Collectors.toMap(NameBasics::getNconst, person -> person));

        List<TitleBasics> titles= dataLoader.getCrews().stream()
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

        return new ResponseEntity<>(titles,HttpStatus.OK);
    }


    @GetMapping("/actors")
    public ResponseEntity<List<TitleBasics>> getTitlesByActors(@RequestParam String actor1, @RequestParam String actor2) {
        Map<String, List<String>> titlesWithActors = dataLoader.getPrincipals().stream()
                .collect(Collectors.groupingBy( TitlePrincipals::getTconst ,
                        Collectors.mapping(TitlePrincipals::getNconst, Collectors.toList())));
        List<TitleBasics> actors=titlesWithActors.entrySet().stream()
                .filter(entry -> entry.getValue().contains(actor1) && entry.getValue().contains(actor2))
                .map(entry -> dataLoader.getTitles().stream()
                        .filter(title -> title.getTconst().equals(entry.getKey()))
                        .findFirst().orElse(null))
                .collect(Collectors.toList());
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @GetMapping("/best-titles-by-genre")
    public ResponseEntity<List<TitleBasics>> getBestTitlesByGenre(@RequestParam String genre) {
        Map<String, TitleRatings> ratingsMap = dataLoader.getRatings().stream()
                .collect(Collectors.toMap(TitleRatings::getTconst, rating -> rating));
       List<TitleBasics> bestTitles= dataLoader.getTitles().stream()
                .filter(title -> title.getGenres().contains(genre))
                .collect(Collectors.groupingBy(TitleBasics::getStartYear))
                .entrySet().stream()
                .map(entry -> entry.getValue().stream()
                        .max(Comparator.comparing(
                                title -> {
                                    TitleRatings rating = ratingsMap.get(title.getTconst());
                                    return (rating == null) ? 0 : rating.getNumVotes() + rating.getAverageRating();
                                }))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bestTitles,HttpStatus.OK);
    }
}
