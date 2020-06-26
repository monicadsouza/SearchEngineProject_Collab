package searchengine;

/**
 * OkapiBM25 calculates the website score based on the Okapi algorithm.
 *
 * @author Anna, Mônica, Natascha, Weisi Li
 */
public class OkapiBM25 implements Score {
    Score tfScore = new TFScore();
    TFIDFScore tfidfScore = new TFIDFScore();

    /**
     * Returns the TFScore.
     *
     * @param word query.
     * @param site the website that needs to be ranked.
     * @param index chosen index structure.
     * @return the score after the algorithm calculation.
     */
    @Override
    public float getScore(String word, Website site, Index index) {
        float TFScore = tfScore.getScore(word, site, index);
        float IDFScore = tfidfScore.calculateIDFScore(word, site, index);
        float okapi = 0.0f;
        float k1 = 1.2f;
        float b = 0.75f;
        float formulaPart = (float) ((TFScore * (k1 + 1)) / (TFScore + 1.2 * (1 - b + b * (site.getWords().size() / index.getAvgNumberOfWords()))));
        okapi = IDFScore * formulaPart;
        return okapi;
    }

}
