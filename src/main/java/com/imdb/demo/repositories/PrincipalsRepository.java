package com.imdb.demo.repositories;

import com.imdb.demo.entities.Principals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipalsRepository extends JpaRepository<Principals, String> {
}
