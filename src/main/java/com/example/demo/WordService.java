package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="josh@joshlong.com">Josh Long</a>
 */
@Service
class WordService {

    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public void storeSynonyms(String... words) {

        List<Word> collectionOfSynonyms = wordsFrom(words)
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


    }

    private Collection<Word> wordsFrom(String... synonyms) {
        List<Word> words = new ArrayList<>();
        for (int i = 0; i < synonyms.length; i += 2) {
            words.add(new Word(synonyms[i], synonyms[i + 1]));
        }
        return words;
    }
}
