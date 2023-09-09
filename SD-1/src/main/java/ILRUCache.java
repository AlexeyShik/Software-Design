public interface ILRUCache<K, V> {

    /**
     * Puts value in cache for given key
     *
     * @param key key
     * @param value object associated with given key
     * @return previous value associated with given key, {@code null} if no value was associated with this key
     */
    V put(K key, V value);

    /**
     * Gets value associated with given key from cache
     *
     * @param key key
     * @return value associated with given key from cache, {@code null} if no value was associated with this key
     */
    V get(K key);

}
