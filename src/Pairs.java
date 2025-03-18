import java.util.ArrayList;
/**
 * A basic data structure that stores key-value pairs, allowing values to be retrieved
 * using unique keys. This is a <b>minimal</b> implementation of a dictionary or hash table.
 * For a more feature-rich solution, consider using the {@code Map} interface or the {@code HashMap} class.
 *
 * @param <K> The type of keys used in this collection.
 * @param <V> The type of values associated with the keys.
 */

public class Pairs<K, V> {
    private ArrayList<Pair<K, V>> pairs;

    /**
     * Create an empty collection of pairs.
     */
    public Pairs() {
        pairs = new ArrayList<Pair<K, V>>();
    }
    /**
     * Checks if a key-value pair exists for the specified key.
     *
     * @param key The key to search for.
     * @return {@code true} if a pair with the given key is found, {@code false} otherwise.
     */
    public boolean contains(K key) {
        boolean found = false;

        for (Pair<K, V> pair : pairs) {
            if (pair.getKey().equals(key))
                found = true;
        }

        return found;
    }
    /**
     * Adds a new key-value pair to the collection. If a pair with the given key already exists,
     * the value associated with the key is updated.
     *
     * @param key   The key for the pair.
     * @param value The value to be associated with the key.
     */

    public void set(K key, V value) {
        if (!contains(key)) {
            // doesn't contain the key, so set a new pairing
            pairs.add(new Pair<K, V>(key, value));
        } else {
            // key exists, so update the value paired with it
            for (Pair<K, V> pair : pairs) {
                if (pair.getKey().equals(key))
                    pair.setValue(value);
            }
        }
    }
    /**
     * Retrieves the value linked to the specified key. If no value is found for the key,
     * the provided default value is returned instead.
     *
     * @param key          The key to search for.
     * @param defaultValue The value returned when no pair is found for the key.
     * @return The value associated with the key, or the default value if the key is not found.
     */
    public V get(K key, V defaultValue) {
        V value = defaultValue;

        for (Pair<K, V> pair : pairs) {
            if (pair.getKey().equals(key))
                value = pair.getValue();
        }

        return value;
    }
    /**
     * Returns the number of key-value pairs in the collection.
     *
     * @return The size of the collection.
     */
    public int size() {
        return pairs.size();
    }

    /**
     * Retrieves the key-value pair at the specified index.
     *
     * @param index The index of the pair to retrieve.
     * @return The key-value pair at the specified index.
     */
    public Pair<K, V> get(int index) {
        return pairs.get(index);
    }

    /**
     * Returns a string representation of the collection of key-value pairs.
     *
     * @return A string that represents the collection of pairs.
     */
    @Override
    public String toString() {
        return pairs.toString();
    }
}
