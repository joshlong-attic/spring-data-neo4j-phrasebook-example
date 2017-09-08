package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author <a href="josh@joshlong.com">Josh Long</a>
 */
@Slf4j
@Component
class SynonymInitializer implements ApplicationRunner {

    private final WordRepository wordRepository;
    private final WordService wordService;

    SynonymInitializer(WordRepository wordRepository, WordService wordService) {
        this.wordRepository = wordRepository;
        this.wordService = wordService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        wordRepository.deleteAll();

        wordService.storeSynonyms("hello", "en", "nihao", "cn", "bonjour", "fr");

        String word = "nihao";
        System.out.println("-------------------------------------");
        this.wordRepository.findSynonymsFor(word).forEach(System.out::println);

        System.out.println("-------------------------------------");
        this.wordRepository.findSynonymsForLanguage(word, "fr").forEach(System.out::println);

    }
}
