package searchengine;

/**
 * The score data structure provides a way to calculate the two different scores
 * needed for ranking the websites.
 *
 * @author Anna, Mônica, Natascha, Weisi Li
 */
public interface Score {
    /**
     * Returns the score.
     *
     * @param word  query.
     * @param site  website that needs to be ranked.
     * @param index chosen index structure.
     * @return the score after the algorithm calculations.
     */
    float getScore(String word, Website site, Index index);
}