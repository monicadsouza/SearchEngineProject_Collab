package searchengine;

import java.util.TreeMap;

/**
 * Subclass of InvertedIndex.
 */
public class InvertedIndexTreeMap extends InvertedIndex {
    public InvertedIndexTreeMap() {
        this.map = new TreeMap<>();
    }
}