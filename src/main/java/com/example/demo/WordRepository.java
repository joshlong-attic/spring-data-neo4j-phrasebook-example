package com.example.demo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;

/**
 * @author <a href="josh@joshlong.com">Josh Long</a>
 */
@RepositoryRestResource
interface WordRepository extends Neo4jRepository<Word, Long> {

    @RestResource(path = "by-word")
    @Query("MATCH (word { word:{w} })-[:synonym]->(syn)   \n" +
            "RETURN    syn ")
    Collection<Word> findSynonymsFor(@Param("w") String word);

    @RestResource(path = "by-word-and-language")
    @Query("MATCH (word { word:{w} })-[:synonym]->(syn)   \n" +
            "match (syn { language:{lang} } ) \n" +
            "RETURN   syn ")
    Collection<Word> findSynonymsForLanguage(@Param("w") String word, @Param("lang") String lang);
}
