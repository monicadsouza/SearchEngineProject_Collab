package searchengine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class QueryHandlerTest {
    private QueryHandler qh = null;
    private QueryHandler qh2 = null;
    private QueryHandler qh3 = null;
    private QueryHandler qh4 = null;
    private List<Website> sites = null;
    private List<Website> sites2 = null;

    @BeforeEach
    void setUp() {
        sites = new ArrayList<>();
        sites.add(new Website("1.com", "example1", Arrays.asList("word1", "word2")));
        sites.add(new Website("2.com", "example2", Arrays.asList("word2", "word3")));
        sites.add(new Website("3.com", "example3", Arrays.asList("word3", "word4", "word5")));
        //second example to test score
        sites2 = new ArrayList<>();
        sites2.add(new Website("itu.dk", "ITU", Arrays.asList("itu", "itu", "itu", "itu", "university", "university", "university", "university", "denmark", "denmark", "denmark", "denmark", "denmark")));
        sites2.add(new Website("danishuni.dk", "Danish Uni", Arrays.asList("itu", "ku", "dtu", "university", "university", "university", "university", "denmark", "denmark", "denmark")));
        sites2.add(new Website("ku.dk", "KU", Arrays.asList("ku", "ku", "university", "university", "denmark", "denmark")));
        Index idx = new InvertedIndexHashMap();
        Index idx2 = new InvertedIndexHashMap();
        Score score = new TFIDFScore();
        Score score2 = new TFScore();
        Score score3 = new OkapiBM25();
        idx.build(sites);
        idx2.build(sites2);
        qh = new QueryHandler(idx, score);
        qh2 = new QueryHandler(idx2, score);
        qh3 = new QueryHandler(idx2, score2);
        qh4 = new QueryHandler(idx2, score3);
    }

    @AfterEach
    void tearDown() {
        sites = null;
        qh = null;
        sites2 = null;
        qh2 = null;
        qh3 = null;
        qh4 = null;
    }

    @Test
    void testSingleWord() {
        assertEquals(1, qh.getMatchingWebsites("word1").size());
        assertEquals("example1", qh.getMatchingWebsites("word1").get(0).getTitle());
        assertEquals(2, qh.getMatchingWebsites("word2").size());
    }

    @Test
    void testMultipleWords() {
        assertEquals(1, qh.getMatchingWebsites("word1 word2").size());
        assertEquals(1, qh.getMatchingWebsites("word3 word4").size());
        assertEquals(1, qh.getMatchingWebsites("word4 word3 word5").size());
    }

    @Test
    void testORQueries() {
        assertEquals(3, qh.getMatchingWebsites("word2 OR word3").size());
        assertEquals(2, qh.getMatchingWebsites("word1 OR word4").size());
        // Corner case: Does code remove duplicates?
        assertEquals(1, qh.getMatchingWebsites("word1 OR word1").size());

    }

    // Test for problematic input
    @Test
    void testCornerCases() {
        assertEquals(2, qh.getMatchingWebsites("word1 word2 OR word2").size());
        // test with multiple words in one query part
        assertEquals(2, qh.getMatchingWebsites("word3 word4 word5 OR word1").size());
        // test query with more than one word in each query part
        assertEquals(1, qh.getMatchingWebsites("word3 word4 OR word1 word3").size());
        // test for erroneous query (typos, non existing query words, remove of white spaces)
        assertEquals(1, qh.getMatchingWebsites("word 3 word 4 OR word1").size());
        assertEquals(1, qh.getMatchingWebsites("    word3    word4  OR   word1    word3    ").size());
        assertEquals(0, qh.getMatchingWebsites("programming OR introduction").size());
        // test for empty and null query
        assertEquals(0, qh.getMatchingWebsites("").size());
        assertEquals(0, qh.getMatchingWebsites(" ").size());
        assertEquals(0, qh.getMatchingWebsites(null).size());
        // test for multiple OR
        assertEquals(3, qh.getMatchingWebsites("word3 OR word1 OR word2").size());
        assertEquals(2, qh.getMatchingWebsites("word3 word4 OR word1 word2 OR word1 word3").size());
    }

    @Test
    void calculateScore() { //white box, unit test no combination
        Map<Website, Float> map = new HashMap<>();
        String[] word = {"word1", "word2"};
        map = qh.calculateScore(map, sites, word);
        assertTrue(map.containsKey(sites.get(0)));
        assertNotNull(map.get(sites.get(0)));
        assertTrue(map.containsKey(sites.get(1)));
        assertNotNull(map.get(sites.get(1)));
    }

    @Test
    void testRankCasesTFIDFScoreSingleWord() {
        assertEquals("ITU", qh2.getMatchingWebsites("itu").get(0).getTitle());
        assertEquals("KU", qh2.getMatchingWebsites("ku").get(0).getTitle());
        assertEquals("Danish Uni", qh2.getMatchingWebsites("ku").get(1).getTitle());
    }

    @Test
    void testRankCasesTFIDFScoreMultipleWords() {
        assertEquals("ITU", qh2.getMatchingWebsites("itu university").get(0).getTitle());
        assertEquals("Danish Uni", qh2.getMatchingWebsites("itu university").get(1).getTitle());
        assertEquals("Danish Uni", qh2.getMatchingWebsites("dtu denmark").get(0).getTitle());
    }

    @Test
    void testRankCasesTFIDFScoreWithOR() {
        assertEquals("ITU", qh2.getMatchingWebsites("itu OR university").get(0).getTitle());
        assertEquals("KU", qh2.getMatchingWebsites("ku OR university").get(0).getTitle());
    }

    @Test
    void testRankCasesTFScoreSingleWord() {
        assertEquals("ITU", qh3.getMatchingWebsites("itu").get(0).getTitle());
        assertEquals("KU", qh3.getMatchingWebsites("ku").get(0).getTitle());
        assertEquals("Danish Uni", qh3.getMatchingWebsites("ku").get(1).getTitle());
        assertEquals("Danish Uni", qh3.getMatchingWebsites("denmark").get(1).getTitle());
    }

    @Test
    void testRankCasesTFScoreMultipleWords() {
        assertEquals("ITU", qh3.getMatchingWebsites("itu OR university OR denmark").get(0).getTitle());
        assertEquals("ITU", qh3.getMatchingWebsites("itu university").get(0).getTitle());
        assertEquals("ITU", qh3.getMatchingWebsites("denmark university").get(0).getTitle());
        assertEquals("KU", qh3.getMatchingWebsites("denmark university").get(2).getTitle());
        assertEquals("Danish Uni", qh3.getMatchingWebsites("denmark university").get(1).getTitle());
    }

    @Test
    void testRankCasesTFScoreWithOR() {
        assertEquals("ITU", qh3.getMatchingWebsites("ku OR denmark").get(0).getTitle());
        assertEquals("Danish Uni", qh3.getMatchingWebsites("denmark").get(1).getTitle());
    }

    @Test
    void testRankCaseOkapiScoreSingleWord() {
        assertEquals("ITU", qh4.getMatchingWebsites("itu").get(0).getTitle());
        assertEquals("KU", qh4.getMatchingWebsites("ku").get(0).getTitle());
        assertEquals("Danish Uni", qh4.getMatchingWebsites("ku").get(1).getTitle());
    }

    @Test
    void testRankCasesOkapiScoreMultipleWords() {
        assertEquals("ITU", qh4.getMatchingWebsites("itu university").get(0).getTitle());
        assertEquals("Danish Uni", qh4.getMatchingWebsites("itu university").get(1).getTitle());
        assertEquals("Danish Uni", qh4.getMatchingWebsites("dtu denmark").get(0).getTitle());
    }

    @Test
    void testRankCasesOkapiScoreWithOR() {
        assertEquals("ITU", qh4.getMatchingWebsites("itu OR university").get(0).getTitle());
        assertEquals("KU", qh4.getMatchingWebsites("ku OR university").get(0).getTitle());
    }
}
