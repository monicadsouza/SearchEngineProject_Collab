package searchengine;

import java.util.List;

/**
 * The index data structure provides a way to build an index from
 * a list of websites. It allows to lookup the websites that contain a query word.
 *
 * @author Martin Aumüller
 */
public interface Index {
    /**
     * The build method processes a list of websites into the index data structure.
     *
     * @param sites The list of websites that should be indexed
     */
    void build(List<Website> sites);

    /**
     * Given a query string, returns a list of all websites that contain the query.
     *
     * @param query The query
     * @return the list of websites that contains the query word.
     */
    List<Website> lookup(String query);

    /**
     * The method calculates the number of websites
     * and serves as helper for calculating the TF Score.
     *
     * @return the number of websites.
     */
    int getNumberOfWebsites();

    /**
     * The method calculates the average number of words
     * and serves as helper for calculating the OkapiBM25 Score.
     *
     * @return average number of words.
     */
    int getAvgNumberOfWords();

    /**
     * Helper method used to support tests calculations.
     *
     * @return the object created by the index. For the SimpleIndex a list, for the others a map.
     */
    Object getObject();
}