package searchengine;

import java.util.HashMap;

/**
 * Subclass of InvertedIndex.
 */
public class InvertedIndexHashMap extends InvertedIndex {
    public InvertedIndexHashMap() {
        this.map = new HashMap<>();
    }
}