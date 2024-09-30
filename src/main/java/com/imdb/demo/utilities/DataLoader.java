package com.imdb.demo.utilities;

import com.imdb.demo.entities.Crew;
import com.imdb.demo.entities.Person;
import com.imdb.demo.entities.Rating;
import com.imdb.demo.entities.Title;
import com.imdb.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;


@Component
public class DataLoader {


//    @Value("${titleUrl}")
    private  final String titleUrl="https://datasets.imdbws.com/title.basics.tsv.gz";
    private  final String personUrl = "https://datasets.imdbws.com/name.basics.tsv.gz";
    private  final String ratingsUrl = "https://datasets.imdbws.com/title.ratings.tsv.gz";
    private  final String crewUrl = "https://datasets.imdbws.com/title.crew.tsv.gz";
    private  final String saveDir = "src/main/resources/data";

//    @Value("${personUrl}")
//    private String personUrl;
//    @Value("${ratingsUrl}")
//    private String ratingsUrl;
//    @Value("${crewUrl}")
//    private String crewUrl;
//    @Value("${principleUrl}")
//    private String principleUrl;
//    @Value("${saveDir}")
//    private String saveDir

    public enum Url {
        TITLEURL, PERSONURL, RATINGURL, CREQURl
    }


    private final TitleRepository titleRepository;
    private final PersonRepository personRepository;
    private final RatingRepository ratingRepository;
    private final CrewRepository crewRepository;
//    private final PrincipalsRepository principalsRepository;



    @Autowired
    public DataLoader(TitleRepository titleRepository, PersonRepository personRepository, RatingRepository ratingRepository, CrewRepository crewRepository, PrincipalsRepository principalsRepository) {
        this.titleRepository = titleRepository;
        this.personRepository = personRepository;
        this.ratingRepository = ratingRepository;
        this.crewRepository = crewRepository;
//        this.principalsRepository = principalsRepository;

    }

    @Bean
    public void downloadFiles() {
        List<String> urls = List.of(titleUrl, personUrl, ratingsUrl, crewUrl);
//        List<String> urls = List.of(titleUrl, personUrl, ratingsUrl, crewUrl);
        urls.forEach(url ->
                CompletableFuture.runAsync(() -> {
                    try {
                        DataDownloader.downloadFile(url, saveDir);
//                        System.out.println("****"+url);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).thenRunAsync(()-> {
                    switch (url) {
                        case titleUrl -> saveTitlesToDatabase();
                        case personUrl -> savePersonsToDatabase();
                        case ratingsUrl -> saveRatingsToDatabase();
                        case crewUrl -> saveCrewsToDatabase();
                        default -> throw new IllegalArgumentException("Unknown URL: " + url);
                    }
                })
        );
    }
    public void saveTitlesToDatabase() {
        processTsvFile("src/main/resources/data/title.basics.tsv.gz", this::mapToTitle, titleRepository);
    }

    // Mapper function to convert fields to a Title entity
    private Title mapToTitle(String[] fields) {
        Title title = new Title();
        title.setTconst(fields[0]);
        title.setTitleType(fields[1]);
        title.setPrimaryTitle(fields[2]);
        title.setOriginalTitle(fields[3]);
        title.setAdult(fields[4].equals("1"));
        title.setStartYear(fields[5].equals("\\N") ? null : Integer.parseInt(fields[5]));
        title.setEndYear(fields[6].equals("\\N") ? null : Integer.parseInt(fields[6]));
        title.setRuntimeMinutes(fields[7].equals("\\N") ? null : Integer.parseInt(fields[7]));
        title.setGenres(Arrays.asList(fields[8].split(",")));
        return title;
    }

    // Specific method to process and save Rating data
    public void saveRatingsToDatabase() {
        processTsvFile("src/main/resources/data/title.ratings.tsv.gz", this::mapToRating, ratingRepository);
    }

    // Mapper function to convert fields to a Rating entity
    private Rating mapToRating(String[] fields) {
        Rating rating = new Rating();
        rating.setTconst(fields[0]);
        rating.setAverageRating(Double.parseDouble(fields[1]));
        rating.setNumVotes(Integer.parseInt(fields[2]));
        return rating;
    }
   

    public void saveCrewsToDatabase() {
        processTsvFile("src/main/resources/data/title.crew.tsv.gz", this::mapToCrew, crewRepository);
    }

    // Mapper function to convert fields to a Crew entity
    private Crew mapToCrew(String[] fields) {
        Crew crew = new Crew();
        crew.setTconst(fields[0]);
        crew.setDirectors(Arrays.asList(fields[1].split(",")));
        crew.setWriters(Arrays.asList(fields[2].split(",")));
        return crew;
    }

    // Specific method to process and save person data
    public void savePersonsToDatabase() {
        processTsvFile("src/main/resources/data/name.basics.tsv.gz", this::mapToPerson, personRepository);
    }

    private Person mapToPerson(String[] fields) {
        Person person = new Person();
        person.setNconst(fields[0]);
        person.setPrimaryName(fields[1]);
        person.setBirthYear(fields[2].isEmpty() ? null : Integer.parseInt(fields[2]));
        person.setDeathYear(fields[3].isEmpty() ? null : Integer.parseInt(fields[3]));
        person.setPrimaryProfession(Arrays.asList(fields[4].split(",")));
        person.setKnownForTitles(Arrays.asList(fields[5].split(",")));
        return person;
    }


    private <T> void processTsvFile(String filePath, Function<String[], T> entityMapper, JpaRepository<T, ?> repository) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream(filePath))))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                T entity = entityMapper.apply(fields);  // Map fields to entity using a provided function
                repository.save(entity);                // Save entity using the provided repository
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public String getTitleUrl() {
        return titleUrl;
    }

    public String getPersonUrl() {
        return personUrl;
    }

    public String getRatingsUrl() {
        return ratingsUrl;
    }

    public String getCrewUrl() {
        return crewUrl;
    }

    public String getSaveDir() {
        return saveDir;
    }
    
    
    
    
    
/*     private void saveTitlesToDatabase()  {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("src/main/resources/data/title.basics.tsv.gz"))))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");

                Title title = new Title();
                title.setTconst(fields[0]);
                title.setTitleType(fields[1]);
                title.setPrimaryTitle(fields[2]);
                title.setOriginalTitle(fields[3]);
                title.setAdult(fields[4].equals("1"));
                title.setStartYear(fields[5].equals("\\N") ? null : Integer.parseInt(fields[5]));
                title.setEndYear(fields[6].equals("\\N") ? null : Integer.parseInt(fields[6]));
                title.setRuntimeMinutes(fields[7].equals("\\N") ? null : Integer.parseInt(fields[7]));
                title.setGenres(Arrays.asList(fields[8].split(",")));

                // Save title to the database
                titleRepository.save(title);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCrewsToDatabase() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("src/main/resources/data/title.crew.tsv.gz"))))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");

                Crew crew = new Crew();
                crew.setTconst(fields[0]);
                crew.setDirectors(Arrays.asList(fields[1].split(",")));
                crew.setWriters(Arrays.asList(fields[2].split(",")));

                crewRepository.save(crew);  // Save to H2 database
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePersonsToDatabase() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("src/main/resources/data/name.basics.tsv.gz"))))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");

                Person person = new Person();
                person.setNconst(fields[0]);
                person.setPrimaryName(fields[1]);
                person.setBirthYear(fields[2].isEmpty() ? null : Integer.parseInt(fields[2]));
                person.setDeathYear(fields[3].isEmpty() ? null : Integer.parseInt(fields[3]));
                person.setPrimaryProfession(Arrays.asList(fields[4].split(",")));
                person.setKnownForTitles(Arrays.asList(fields[5].split(",")));

                personRepository.save(person);  // Save to H2 database
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveRatingsToDatabase() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("src/main/resources/data/title.ratings.tsv.gz"))))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");

                Rating rating = new Rating();
                rating.setTconst(fields[0]);
                rating.setAverageRating(Double.parseDouble(fields[1]));
                rating.setNumVotes(Integer.parseInt(fields[2]));

                ratingRepository.save(rating);  // Save to H2 database
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    /*
    private void savePrincipalsToDatabase() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("src/main/resources/data/title.principals.tsv.gz"))))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");

                Principals principal = new Principals();
                principal.setTconst(fields[0]);
                principal.setOrdering(fields[1].equals("\\N") ? null : Integer.parseInt(fields[1]));
                principal.setNconst(fields[2]);
                principal.setCategory(fields[3]);
                principal.setJob(fields[4]);
                principal.setCharacters(fields[5]);

                principalsRepository.save(principal);  // Save to H2 database
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}
