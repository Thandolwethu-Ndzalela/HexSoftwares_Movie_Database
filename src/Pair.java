/**
 * Represents a basic key-value pair container.
 *
 * @param <K> The data type of the key.
 * @param <V> The data type of the value.
 */

public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    /**
     * Retrieves the key of this key-value pair.
     *
     * @return The key.
     */
    public K getKey() {
        return key;
    }
    /**
     * Sets the key for this key-value pair.
     *
     * @param key The key to be set.
     */
    public void setKey(K key) {
        this.key = key;
    }
    /**
     * Retrieves the value associated with the key.
     *
     * @return The value.
     */
    public V getValue() {
        return value;
    }
    /**
     * Sets the value for this key-value pair.
     *
     * @param value The value to be set.
     */
    public void setValue(V value) {
        this.value = value;
    }
    /**
     * Returns a string representation of the key-value pair.
     *
     * @return A formatted string in the form "(key = value)".
     */
    @Override
    public String toString() {
        return "(" + key + " = " + value + ")";
    }
}
