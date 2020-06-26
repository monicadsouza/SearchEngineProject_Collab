package searchengine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IndexTest {
    Index simpleIndex = null;
    InvertedIndex invertedIndexHashMap = null;
    InvertedIndex invertedIndexTreeMap = null;

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<Website>();
        sites.add(new Website("example1.com", "example1", Arrays.asList("word1", "word2", "word1")));
        sites.add(new Website("example2.com", "example2", Arrays.asList("word2", "word3")));
        simpleIndex = new SimpleIndex();
        simpleIndex.build(sites);
        invertedIndexHashMap = new InvertedIndexHashMap();
        invertedIndexHashMap.build(sites);
        invertedIndexTreeMap = new InvertedIndexTreeMap();
        invertedIndexTreeMap.build(sites);
    }

    @AfterEach
    void tearDown() {
        simpleIndex = null;
        invertedIndexHashMap = null;
        invertedIndexTreeMap = null;
    }

    @Test
    void buildSimpleIndex() {
        assertEquals("SimpleIndex{sites=[Website{title='example1', url='example1.com', words=[word1, word2, word1]}," +
                " Website{title='example2', url='example2.com', words=[word2, word3]}]}", simpleIndex.toString());
    }

    @Test
    void buildInvertedIndexHashMap() {
        Map map = (Map) invertedIndexHashMap.getObject();
        // Tips
        // "assertEquals(A,B)" in Junit is "return A.equal(B)", only check the value
        // "assertSame(A,B)" in Junit is "return A == B" , check the object, hashcode
        assertEquals(3, map.size());
        assertTrue(map.containsKey("word1"));
        assertTrue(map.containsKey("word2"));
        assertTrue(map.containsKey("word3"));
        assertEquals("[Website{title='example1', url='example1.com', words=[word1, word2, word1]}]", map.get("word1").toString());
        assertEquals("[Website{title='example1', url='example1.com', words=[word1, word2, word1]}," +
                " Website{title='example2', url='example2.com', words=[word2, word3]}]", map.get("word2").toString());
        assertEquals("[Website{title='example2', url='example2.com', words=[word2, word3]}]", map.get("word3").toString());
    }

    @Test
    void buildInvertedIndexTreeMap() {
        Map map = (Map) invertedIndexTreeMap.getObject();
        assertEquals(3, map.size());
        assertEquals("{word1=[Website{title='example1', url='example1.com', words=[word1, word2, word1]}], " +
                "word2=[Website{title='example1', url='example1.com', words=[word1, word2, word1]}, " +
                "Website{title='example2', url='example2.com', words=[word2, word3]}], " +
                "word3=[Website{title='example2', url='example2.com', words=[word2, word3]}]}", map.toString());
    }

    @Test
    void buildInvertedIndexWrongList() {
        //Test if build can run with a null list
        invertedIndexHashMap = new InvertedIndexHashMap();
        List<Website> sitesNull = null;
        invertedIndexHashMap.build(sitesNull);
        Map mapNull = (Map) invertedIndexHashMap.getObject();
        assertTrue(mapNull.isEmpty());

        // Test if build can run with an empty list
        invertedIndexHashMap = new InvertedIndexHashMap();
        List<Website> sitesEmpty = new ArrayList<>();
        invertedIndexHashMap.build(sitesEmpty);
        Map mapEmpty = (Map) invertedIndexHashMap.getObject();
        assertTrue(mapEmpty.isEmpty());
    }

    @Test
    void lookupSimpleIndex() {
        lookupTest(simpleIndex);
        lookupNotExistTest(simpleIndex);
        lookupExtraSpacebarTest(simpleIndex);
        lookupNullAndEmptyStringTest(simpleIndex);
    }

    @Test
    void lookupInvertedIndexHashMap() {
        lookupTest(invertedIndexHashMap);
        lookupNotExistTest(invertedIndexHashMap);
        lookupExtraSpacebarTest(invertedIndexHashMap);
        lookupNullAndEmptyStringTest(invertedIndexHashMap);
    }

    @Test
    void lookupInvertedIndexTreeMap() {
        lookupTest(invertedIndexTreeMap);
        lookupNotExistTest(invertedIndexTreeMap);
        lookupExtraSpacebarTest(invertedIndexTreeMap);
        lookupNullAndEmptyStringTest(invertedIndexTreeMap);
    }

    /**
     * Test lookup with correct input
     *
     * @param index the tested Index
     */
    private void lookupTest(Index index) {
        //Test the lookup method's results by comparing the Website objects inside them (and their titles, URLs, words) to what you expected to be returned.
        assertEquals(1, index.lookup("word1").size());
        assertEquals("Website{title='example1', url='example1.com', words=[word1, word2, word1]}", index.lookup("word1").get(0).toString());
        assertEquals(2, index.lookup("word2").size());
        assertEquals("Website{title='example1', url='example1.com', words=[word1, word2, word1]}", index.lookup("word2").get(0).toString());
        assertEquals("Website{title='example2', url='example2.com', words=[word2, word3]}", index.lookup("word2").get(1).toString());
    }

    /**
     * Test lookup with a word that shouldn't return any results.
     *
     * @param index the tested Index
     */
    private void lookupNotExistTest(Index index) {
        //Test the lookup with a word that shouldn't return any results.
        assertEquals(0, index.lookup("word4").size());
    }

    /**
     * Test lookup with a word which has extra space
     *
     * @param index the tested Index
     */
    private void lookupExtraSpacebarTest(Index index) {
        //Test the lookup with a word that should return results,
        //but has many extra space in the front or the back or both.
        assertEquals(1, index.lookup("   word1").size());
        assertEquals(1, index.lookup("word1  ").size());
        assertEquals(1, index.lookup("  word1  ").size());
        assertEquals(0, index.lookup("    ").size());
    }

    /**
     * Test lookup with an empty string, or a null
     *
     * @param index the tested Index
     */
    private void lookupNullAndEmptyStringTest(Index index) {
        //Test the lookup with an empty string, or a null
        assertEquals(0, index.lookup("").size());
        assertEquals(0, index.lookup(null).size());
    }
}
