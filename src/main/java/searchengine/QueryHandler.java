package searchengine;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for answering queries to our search engine.
 *
 * @author Anna, Mônica, Natascha, Weisi Li
 */

public class QueryHandler {

    /**
     * The index the QueryHandler uses for answering queries.
     */
    private Index idx = null;
    private Score score = null;

    /**
     * The constructor
     *
     * @param idx   The index used by the QueryHandler.
     * @param score The score used by the QueryHandler.
     */
    public QueryHandler(Index idx, Score score) {

        this.idx = idx;
        this.score = score;
    }

    /**
     * getMachingWebsites answers queries of the type
     * "subquery1 OR subquery2 OR subquery3 ...". A "subquery"
     * has the form "word1 word2 word3 ...". A website matches
     * a subquery if all the words occur on the website. A website
     * matches the whole query, if it matches at least one subquery.
     *
     * @param line the query string
     * @return the list of websites that matches the query
     */
    public List<Website> getMatchingWebsites(String line) {
        List<Website> results = new ArrayList<>();
        Map<Website, Float> websiteScore = new HashMap<>();
        if (line == null) {
            return results;
        }
        // use of this regular expression to ensure one or multiple spaces, Ensures that words in upper case like WORD are not splitted
        String[] orSplit = line.split("\\s+OR\\s+");
        // loop through all query parts
        for (String queryPart : orSplit) {
            // stores the websites for a given word, or if there is more than one word in the query part
            // it stores the intersection of the website for all words in the query part
            List<Website> temporaryResults = new ArrayList<>();
            String[] wordSplit = queryPart.trim().split("\\s+");
            if (wordSplit.length > 0) {
                //Anna 4/12: This covers the case of only one word inside the query or the query part (if there is an OR)
                temporaryResults.addAll(idx.lookup(wordSplit[0]));
                // if there is more than one word in the query or query part
                // loops through the remaining words and only keeps websites which intersect with the existing results
                if (wordSplit.length > 1) {
                    for (int i = 1; i < wordSplit.length; i++) {
                        temporaryResults.retainAll(idx.lookup(wordSplit[i]));
                    }
                }
                // put values inside the map from the given queryPart, based on word list of this query part, and the
                // result list of all websites
                websiteScore = this.calculateScore(websiteScore, temporaryResults, wordSplit);
            }
        }
        //get the result based on the given statement in the description
        // List of Websites in descending order, based on the score for the given query
        results = websiteScore.entrySet().stream().sorted((map1, map2) -> map2.getValue().compareTo(map1.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
        return results;
    }

    /**
     * Defines the scores of the websites for each query part and puts it inside the map of websites and score values
     * If a website is already inside the scoreMap it is compared if the new score value of this website is larger than
     * the previous one, if so the score value for this site is replaced by the new, higher value - max of query part scores.
     *
     * @param map   Map of websites and scores.
     * @param list  List of websites.
     * @param words List of words.
     * @return The map of websites and scores.
     */
    public Map<Website, Float> calculateScore(Map<Website, Float> map, List<Website> list, String[] words) {
        //loop through the result list of websites
        for (Website site : list) {
            float sumScore = 0.0f;
            //loop through all the query words
            for (String word : words) {
                // get the score of the website as sum of all the scores for each word inside words
                sumScore += score.getScore(word, site, idx);
            }
            if (!map.containsKey(site)) {
                map.put(site, sumScore);
            } else if (sumScore > map.get(site)) {
                //override with the larger value
                map.put(site, sumScore);
            }
        }
        return map;
    }

    /**
     * get the Index
     * @return the Index
     */
    public Index getIdx() {
        return idx;
    }
}