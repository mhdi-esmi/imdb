package com.imdb.demo.utilities;

import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RequestCounterFilter implements Filter {

    private AtomicInteger requestCount = new AtomicInteger(0);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        requestCount.incrementAndGet();
        chain.doFilter(request, response);
    }

    public int getRequestCount() {
        return requestCount.get();
    }

}