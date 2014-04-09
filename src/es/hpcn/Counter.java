package es.hpcn;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by carlosvega on 01/04/14.
 */
public class Counter<X> {

    //COMPARATORS

    public static class CounterComparator<X> implements Comparator<Map.Entry<X, Integer>> {

        @Override
        public int compare(Map.Entry<X, Integer> x, Map.Entry<X, Integer> y) {
            if (x.getValue() < y.getValue()) {
                return 1;
            }

            if (x.getValue() > y.getValue()) {
                return -1;
            }

            return 0;

        }

    }



    public static class CounterComparatorByKey<X extends Comparable<X>> implements Comparator<Map.Entry<X, ?>> {
        @Override
        public int compare(Map.Entry<X, ?> x, Map.Entry<X, ?> y) {
            return x.getKey().compareTo(y.getKey());
        }

    }


    //END COMPARATORS


    private Map<X, Integer> dictionary = null;

    public Counter() {
        this.dictionary = new HashMap<X, Integer>();
    }

    public Counter(int initialSize, float load) {
        this.dictionary = new HashMap<X, Integer>(initialSize, load);
    }

    public Counter(int initialSize) {
        this.dictionary = new HashMap<X, Integer>(initialSize);
    }

    public Map<X, Integer> getDictionary() {
        return dictionary;
    }

    public int size() {
        return dictionary.size();
    }

    public void clear() {
        dictionary.clear();
    }

    public void update(X[] keys) {

        for (X s : keys) {

            if (dictionary.containsKey(s))
                dictionary.put(s, dictionary.get(s) + 1);
            else
                dictionary.put(s, 1);
        }

    }

    public void update(X key) {

        if (dictionary.containsKey(key))
            dictionary.put(key, dictionary.get(key) + 1);
        else
            dictionary.put(key, 1);

    }
}
