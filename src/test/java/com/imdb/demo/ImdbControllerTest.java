package com.imdb.demo;

import com.imdb.demo.controller.ImdbController;
import com.imdb.demo.entities.Rating;
import com.imdb.demo.entities.Title;
import com.imdb.demo.services.TitleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ImdbControllerTest {

    @Mock
    private TitleService titleService;

    @InjectMocks
    private ImdbController imdbController;

    private List<Title> titles;
    private Map<String, Rating> ratings;

    @BeforeEach
    public void setup() {
        titles = new ArrayList<>();
        titles.add(new Title("tt001", "Action Movie 1", "Action Movie 1", "Action", true, 2020, 2021, 120, Arrays.asList("Action", "Drama")));
        titles.add(new Title("tt002", "Action Movie 2", "Action Movie 2", "Action", false, 2019, 2022, 130, List.of("Action", "Thriller")));
        titles.add(new Title("tt003", "Action Movie 3", "Action Movie 3", "Action", false, 2018, 2014, 150, List.of("Drama")));

        ratings = mockedRatingsMap();
        // Set up the mocks to return the titles and ratings
        when(titleService.getTitles()).thenReturn(titles);
        when(titleService.getRatings()).thenReturn(ratings);
    }

    @Test
    public void testGetBestTitlesByGenre() {
        List<Title> bestTitles = imdbController.getBestTitlesByGenre("Action");

        assertNotNull(bestTitles);
        assertEquals(2, bestTitles.size());

        assertEquals("Action Movie 2", bestTitles.get(0).getPrimaryTitle());  // Highest rating first
        assertEquals("Action Movie 1", bestTitles.get(1).getPrimaryTitle());  // Second highest rating

        verify(titleService, times(1)).getTitles();
        verify(titleService, times(1)).getRatings();
    }

    @Test
    public void testGetBestTitlesByGenre_NoMatchingGenre() {
        List<Title> bestTitles = imdbController.getBestTitlesByGenre("Comedy");

        assertTrue(bestTitles.isEmpty());

        verify(titleService, times(1)).getTitles();
        verify(titleService, times(1)).getRatings();
    }

    private Map<String, Rating> mockedRatingsMap() {
        Map<String, Rating> ratings = new HashMap<>();
        ratings.put("tt001", new Rating("tt001", 7.5, 1000));  // Rating for "Action Movie 1"
        ratings.put("tt002", new Rating("tt002", 8.1, 1500));  // Rating for "Action Movie 2"
        ratings.put("tt003", new Rating("tt003", 6.8, 500));   // Rating for "Action Movie 3"

        return ratings;
    }
}
