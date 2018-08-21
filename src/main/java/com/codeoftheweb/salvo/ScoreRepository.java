package com.codeoftheweb.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

//A Repository class is analogous to a table, i.e., it is a class that manages a collection of instances

/*Spring also includes libraries to create Rest Repositories, which make it easy to send instances of Java
        classes to browsers and other web clients in JavaScript Object Notation (JSON) format,
        which is important for REST applications*/

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByPlayer(Player player);
}
