package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Word {

    @GraphId
    private Long id;

    private String word, language;

    @Relationship(direction = Relationship.UNDIRECTED,  type = "synonym")
    private Set<Word> synonyms = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Word word1 = (Word) o;

        if (id != null ? !id.equals(word1.id) : word1.id != null) return false;
        if (word != null ? !word.equals(word1.word) : word1.word != null)
            return false;
        return language != null ? language.equals(word1.language) : word1.language == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }

    public Word(String word, String language) {
        this.word = word;
        this.language = language;
    }
}