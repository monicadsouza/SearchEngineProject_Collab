package searchengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * InvertedIndex contains all methods that builds and look up websites.
 */
abstract public class InvertedIndex implements Index {
    protected Map<String, List<Website>> map;
    protected int numberWebsites = 0;
    protected int avgNumberOfWords = 0;

    /**
     * The build method processes a list of websites into the index data structure.
     *
     * @param sites The list of websites that should be indexed
     */
    @Override
    public void build(List<Website> sites) {
        // Check null first, because if sites is null then sites does not have isEmpty(),
        // it will get running time error
        if (sites != null && !sites.isEmpty()) {
            numberWebsites = sites.size();
            int numberOfWords = 0;
            //go through each website provided by the list
            for (Website website : sites) {
                numberOfWords += website.getWords().size();
                //process each word from the list of words of the given website
                for (String word : website.getWords()) {
                    //covers the case where the word doesn't exist in the map yet
                    if (!map.containsKey(word)) {
                        //creates a new list of websites
                        List<Website> websiteList = new ArrayList<>();
                        //adds the website to the list
                        websiteList.add(website);
                        //puts the word and the list with (at that point) one website inside the map
                        map.put(word, websiteList);
                    }
                    //checks that the website doesn't already exist in the list of websites for the given word
                    else if (!map.get(word).contains(website)) {
                        //retrieves the list of existing websites and add the new website to the list
                        map.get(word).add(website);
                    }
                }
            }
            avgNumberOfWords = numberOfWords / numberWebsites;
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
        List<Website> result = new ArrayList<>();
        // check if the query word is included in map
        // 26 Nov add null check
        if (query != null) {
            // trim whitespace from the beginning and end of a string
            // "  word","  word   ","  "
            query = query.trim();
            if (map.containsKey(query)) {
                // addAll takes a Collection as a parameter and adds it to the result list
                result.addAll(map.get(query));
            }
        }
        // Return a list object, it can be empty but not null
        return result;
    }

    /**
     * The method calculate the number of websites
     * and serves as helper for calculating the TF Score.
     *
     * @return the number of websites.
     */
    @Override
    public int getNumberOfWebsites() {
        return numberWebsites;
    }

    /**
     * The method calculate the average number of words
     * and serves as helper for calculating the Okapi Score.
     *
     * @return average number of words.
     */
    @Override
    public int getAvgNumberOfWords() {
        return avgNumberOfWords;
    }

    /**
     * Helper method used to support test calculations.
     *
     * @return the object created by the index. For the inverted indices, a map.
     */
    @Override
    public Object getObject() {
        return this.map;
    }
}