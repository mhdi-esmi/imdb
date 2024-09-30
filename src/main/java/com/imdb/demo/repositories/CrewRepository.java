package com.imdb.demo.repositories;

import com.imdb.demo.entities.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<Crew, String> {
}
