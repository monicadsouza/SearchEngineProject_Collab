package searchengine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {
    Index invertedIndexHashMap = null;
    Score tfScore = null;
    Score tfidfScore = null;
    Score okapiBM25 = null;

    @BeforeEach
    void setUp() {
        invertedIndexHashMap = new InvertedIndexHashMap();
        tfScore = new TFScore();
        tfidfScore = new TFIDFScore();
        okapiBM25 = new OkapiBM25();
        List<Website> sites = new ArrayList<Website>();
        sites.add(new Website("example1.com", "example1", Arrays.asList("word1", "word2", "word1")));
        sites.add(new Website("example2.com", "example2", Arrays.asList("word2", "word3")));
        invertedIndexHashMap.build(sites);
    }

    @AfterEach
    void tearDown() {
        invertedIndexHashMap = null;
        tfScore = null;
        tfidfScore = null;
        okapiBM25 = null;
    }

    @Test
    void testScore() {
        Website website = new Website("example1.com", "example1", Arrays.asList("word1", "word2", "word1"));
        assertEquals(2.0f, tfScore.getScore("word1", website, invertedIndexHashMap), 0.000001);
        assertEquals(Math.log(2.0f / 1.0f) * 2.0f, tfidfScore.getScore("word1", website, invertedIndexHashMap), 0.000001);
        assertEquals((Math.log(2.0f / 1.0f) * 1.375), okapiBM25.getScore("word1", website, invertedIndexHashMap), 0.000001);
    }
}