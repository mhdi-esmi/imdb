package com.imdb.demo.services;

import com.imdb.demo.entities.*;
import com.imdb.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Map;

@Service
public class TitleService {

    private final TitleRepository titleRepository;

    private final CrewRepository crewRepository;

    private final PersonRepository personRepository;

    private  final RatingRepository ratingRepository;

//    private final PrincipalsRepository principalsRepository;

    @Autowired
    public TitleService(TitleRepository titleRepository, CrewRepository crewRepository,
                        PersonRepository personRepository, RatingRepository ratingRepository,
                        PrincipalsRepository principalsRepository) {
        this.titleRepository = titleRepository;
        this.crewRepository = crewRepository;
        this.personRepository = personRepository;
        this.ratingRepository = ratingRepository;
//        this.principalsRepository = principalsRepository;
    }

    public List<Title> getTitles() {
        return titleRepository.findAll();
    }

    public Map<String, Crew> getCrews() {
        List<Crew> crews = crewRepository.findAll();
        return crews.stream().collect(Collectors.toMap(Crew::getTconst, crew -> crew));
    }

    public Map<String, Person> getPersons() {
        List<Person> persons = personRepository.findAll();
        return persons.stream().collect(Collectors.toMap(Person::getNconst, person -> person));
    }

    public Map<String, Rating> getRatings() {
        List<Rating> ratings = ratingRepository.findAll();
        return ratings.stream().collect(Collectors.toMap(Rating::getTconst, rating -> rating));
    }

//    public List<Principals> getPrincipals() {
//        return principalsRepository.findAll();
//    }
}

