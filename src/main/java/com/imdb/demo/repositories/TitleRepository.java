package com.imdb.demo.repositories;
 import com.imdb.demo.entities.Title;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository extends JpaRepository<Title, String> {

}
