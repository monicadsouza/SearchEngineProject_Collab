package searchengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods to build and look up websites.
 */
public class SimpleIndex implements Index {
    private int numberOfWebsites = 0;
    private int avgNumberOfWords = 0;

    /**
     * The list of websites stored in the index.
     */
    private List<Website> sites = null;

    /**
     * The build method processes a list of websites into the index data structure.
     *
     * @param sites The list of websites that should be indexed
     */
    @Override
    public void build(List<Website> sites) {
        this.sites = sites;
        if (sites != null && !sites.isEmpty()) {
            numberOfWebsites = sites.size();
            int numberOfWords = 0;
            for (Website site : sites) {
                numberOfWords += site.getWords().size();
            }
            avgNumberOfWords = numberOfWords / numberOfWebsites;
        }
    }

    /**
     * Given a query string, returns a list of all websites that contain the query.
     *
     * @param query The query
     * @return the list of websites that contains the query word.
     */
    @Override
    public List<Website> lookup(String query) {
        List<Website> result = new ArrayList<Website>();
        if (query != null) {
            //trim whitespace (as defined above) from the beginning and end of a string.
            query = query.trim();

            for (Website w : sites) {
                if (w.containsWord(query)) {
                    result.add(w);
                }
            }
        }
        return result;
    }

    /**
     * @return Converts a list of websites into a String.
     */
    @Override
    public String toString() {
        return "SimpleIndex{" +
                "sites=" + sites +
                '}';
    }

    /**
     * The follow method calculate the number of websites inside a result queried list,
     * and serve as helper for calculating the TF Score.
     *
     * @return the number of websites.
     */
    @Override
    public int getNumberOfWebsites() {
        return numberOfWebsites;
    }

    /**
     * The method calculate the average number of words
     * and serves as helper for calculating the Okapi Score.
     *
     * @return average number of words.
     */
    @Override
    public int getAvgNumberOfWords(){ return avgNumberOfWords; }

    /**
     * Helper method used to support tests calculations.
     *
     * @return the object created by the index. For the SimpleIndex a list, for the others a map.
     */
    @Override
    public Object getObject() {
        return this.sites;
    }
}