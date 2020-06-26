package searchengine;

/**
 * TFScore contains the method for processing the TF algorithms.
 *
 * @author Anna, Mônica, Natascha, Weisi Li
 */
public class TFScore implements Score {

    /**
     * Returns the TFScore.
     *
     * @param queryWord query.
     * @param site      the website that needs to be ranked.
     * @param index     chosen index structure.
     * @return the score after the algorithm calculation.
     */
    @Override
    public float getScore(String queryWord, Website site, Index index) {
        float tfScore = 0.0f;
        for (String w : site.getWords()) {
            if (w.equals(queryWord)) {
                tfScore++;
            }
        }
        return tfScore;
    }
}