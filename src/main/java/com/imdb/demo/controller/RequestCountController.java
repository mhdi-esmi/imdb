package com.imdb.demo.controller;

import com.imdb.demo.utilities.RequestCounterFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/requests")
public class RequestCountController {

    private RequestCounterFilter requestCounterFilter;

    @Autowired
    public RequestCountController(RequestCounterFilter requestCounterFilter) {
        this.requestCounterFilter = requestCounterFilter;
    }



    @GetMapping("/count")
    public int getRequestCount() {
        return requestCounterFilter.getRequestCount();
    }
}