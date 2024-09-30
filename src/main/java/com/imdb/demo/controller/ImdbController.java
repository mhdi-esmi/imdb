package com.imdb.demo.controller;

import com.imdb.demo.entities.Person;
import com.imdb.demo.entities.Title;
import com.imdb.demo.entities.Crew;
import com.imdb.demo.entities.Rating;
import com.imdb.demo.services.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/titles")
public class ImdbController {
    private final TitleService titleService;

    @Autowired
    public ImdbController(TitleService titleService) {
        this.titleService = titleService;
//        this.dataLoaderService.loadData(); // Load data on startup

    }


    @GetMapping("/sameDirectorWriterAlive")
    public List<Title> getTitlesByDirectorAndWriter() {
        return titleService.getTitles().stream()
                .filter(title -> {
                    Crew crew = titleService.getCrews().get(title.getTconst());
                    if (crew == null) return false;
                    return crew.getDirectors().contains(crew.getWriters().get(0)) && isAlive(crew.getDirectors().get(0));
                })
                .collect(Collectors.toList());
    }
    @GetMapping("/commonTitles")
    public List<Title> getCommonTitles(@RequestParam String actor1, @RequestParam String actor2) {
        return titleService.getTitles().stream()
                .filter(title -> {
                    List<String> actorsInTitle = titleService.getCrews().get(title.getTconst()).getDirectors();
                    return actorsInTitle.contains(actor1) && actorsInTitle.contains(actor2);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/bestTitlesByGenre")
    public List<Title> getBestTitlesByGenre(@RequestParam String genre) {
        Map<String, Rating> rates= titleService.getRatings();
        return titleService.getTitles().stream()
                .filter(title -> title.getGenres().contains(genre))
                .collect(Collectors.groupingBy(Title::getStartYear, Collectors.toList()))
                .values().stream()
                .flatMap(titles -> Stream.ofNullable(
                        titles.stream()
                                .max((t1, t2) -> {
                                    Rating r1 = titleService.getRatings().get(t1.getTconst());
                                    Rating r2 = titleService.getRatings().get(t2.getTconst());
                                    return Double.compare(r1.getAverageRating(), r2.getAverageRating());
                                }).orElse(null)
                ))
                .collect(Collectors.toList());
    }

    private boolean isAlive(String nconst) {
        Person person = titleService.getPersons().get(nconst);
        return person != null && person.getDeathYear() == null;
    }
}
