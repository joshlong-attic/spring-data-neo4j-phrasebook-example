package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@Slf4j
@Component
class SynonymInitializer implements ApplicationRunner {

    private final WordRepository wordRepository;
    private final Session session;

    SynonymInitializer(WordRepository wordRepository, Session session) {
        this.wordRepository = wordRepository;
        this.session = session;
    }

    private Collection<Word> storeWords(String... synonyms) {
        List<Word> words = new ArrayList<>();
        for (int i = 0; i < synonyms.length; i += 2) {
            words.add(new Word(synonyms[i], synonyms[i + 1]));
        }
        return words;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        wordRepository.deleteAll();

        List<Word> collectionOfSynonyms = storeWords("hello", "en", "nihao", "cn", "bonjour", "fr")
                .stream()
                .map(wordRepository::save)
                .collect(Collectors.toList());

        for (int i = 0; i < collectionOfSynonyms.size(); i++) {
            Word thisWord = collectionOfSynonyms.get(i);
            List<Word> others = collectionOfSynonyms
                    .stream()
                    .filter(w -> !w.getWord().equals(thisWord.getWord()))
                    .collect(Collectors.toList());
            thisWord.getSynonyms().addAll(others);
            this.wordRepository.save(thisWord);
        }

        String word = "nihao";
        System.out.println("-------------------------------------");
        this.wordRepository.findSynonymsFor(word).forEach(System.out::println);

        System.out.println("-------------------------------------");
        this.wordRepository.findSynonymsForLanguage(word, "fr").forEach(System.out::println);

    }
}


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
