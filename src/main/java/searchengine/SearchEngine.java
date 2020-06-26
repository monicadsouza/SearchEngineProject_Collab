package searchengine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The search engine. Upon receiving a list of websites, it performs
 * the necessary configuration (i.e. building an index and a query
 * handler) to then be ready to receive search queries.
 *
 * @author Willard Rafnsson
 * @author Martin Aumüller
 * @author Leonid Rusnac
 */
public class SearchEngine {
    /**
     * Query handler to process queries.
     */
    private QueryHandler queryHandler;

    /**
     * Creates a {@code SearchEngine} object from a list of websites.
     *
     * @param sites the list of websites
     */
    public SearchEngine(List<Website> sites) {
        //Index idx = new SimpleIndex();
        InvertedIndex idx = new InvertedIndexHashMap();
        //Index idx = new InvertedIndexTreeMap();
        idx.build(sites);
        //Score score = new TFIDFScore();
        //Score score = new TFScore();
        Score score = new OkapiBM25();
        queryHandler = new QueryHandler(idx, score);
    }

    /**
     * Returns the list of websites matching the query.
     *
     * @param query the query
     * @return the list of websites matching the query
     */
    public List<Website> search(String query) {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }
        List<Website> resultList = queryHandler.getMatchingWebsites(query);

        return resultList;
    }

    /**
     * Helper method responsible for access the map created by the chosen index
     * to get and sort the list of words.
     * @return the list of query words
     */
    public List<String> getWords() {
        Map map = (Map)(queryHandler.getIdx()).getObject();
        return (List<String>) map.keySet().stream().sorted().collect(Collectors.toList());
    }
}