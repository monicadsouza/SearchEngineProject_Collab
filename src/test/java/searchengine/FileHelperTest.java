package searchengine;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class FileHelperTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    void isFileEmpty() {
        //Works with an empty file, returns an empty list
        assertTrue(FileHelper.parseFile("data/test-file-empty.txt").isEmpty());
    }

    @Test
    void fileNotExisting() {
        //Works with exception about non existed file
        thrown.expect(FileNotFoundException.class);
        FileHelper.parseFile("data/test-fileNotFound.txt");
    }

    @Test
    void parseGoodFile() {
        List<Website> sites = FileHelper.parseFile("data/test-file.txt");
        assertEquals(4, sites.size());
        assertEquals("title1", sites.get(0).getTitle());
        assertEquals("title2", sites.get(1).getTitle());
        assertTrue(sites.get(0).containsWord("word1"));
        assertFalse(sites.get(0).containsWord("word3"));
    }

    @Test
    void parseBadFile() {
        List<Website> sites = FileHelper.parseFile("data/test-file-errors.txt");
        /*
        Wrong cases already cover:
        1. url, title, and listOfWords do not exist;
        2. url and title exist, but listOfWords does not;
        3. url exist, but title and listOfWords do not exist;
         */
        //Check if only creates a Website with all the correct data, database only have 2 correct structures
        assertEquals(2, sites.size());
        //Check if the first title was correctly created and it's not null
        assertEquals("title1", sites.get(0).getTitle());
        //Check if the second title was correctly created and it's not null
        assertEquals("title2", sites.get(1).getTitle());
        //Check a word inside the first Website's list
        assertTrue(sites.get(0).containsWord("word2"));
        //Check if a word is not inside the first Website's list
        assertFalse(sites.get(0).containsWord("word3"));
        //Check a word inside the second Website's list
        assertTrue(sites.get(1).containsWord("word1"));
        //Check if a word is not inside the second Website's list
        assertFalse(sites.get(1).containsWord("word2"));
    }
}
