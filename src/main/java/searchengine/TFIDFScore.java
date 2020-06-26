package searchengine;

/**
 * TFIDFScore contains all the methods for calculating the final
 * website score after processing both TF and IDF algorithms.
 *
 * @author Anna, Mônica, Natascha, Weisi Li
 */
public class TFIDFScore implements Score {

    private Score tfScore = new TFScore();

    /**
     * Returns the TFIDF score.
     *
     * @param word query.
     * @param site website that needs to be ranked.
     * @param idx  chosen index structure.
     * @return the final website score.
     */
    @Override
    public float getScore(String word, Website site, Index idx) {
        float tfSc = tfScore.getScore(word, site, idx);
        float iDFSc = calculateIDFScore(word, site, idx);
        float tfIDS = tfSc * iDFSc;
        return tfIDS;
    }

    /**
     * Calculates the IDF score.
     *
     * @param word query.
     * @param site website that needs to be ranked.
     * @param idx  chosen index structure.
     * @return the IDF website score.
     */
    float calculateIDFScore(String word, Website site, Index idx) {
        float IDFScore = 0.0f;
        float sitesWord = (float) idx.lookup(word).size();
        // check if the word is inside the website if not the IDF score is zero and directly return.
        // it is necessary bc otherwise we have a 0 in the log division
        if (sitesWord == 0) {
            return IDFScore;
        }
        float sitesTotal = idx.getNumberOfWebsites();
        IDFScore = (float) Math.log(sitesTotal / sitesWord);
        return IDFScore;
    }
}