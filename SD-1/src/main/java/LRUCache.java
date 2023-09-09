import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * <p>Simple cache implementation</p>
 * <p>All values are stored in linked list. Most recently added value is stored in {@link LRUCache#head}
 * and least recently added value in {@link LRUCache#tail}</p>
 * <p>In map {@link LRUCache#keyToListNodeMap} are stored links to linked list nodes for all stored values</p>
 *
 * @param <K> type of keys
 * @param <V> type of values
 */
public class LRUCache<K, V> extends AbstractLRUCache<K, V> {

    /**
     * Map stores list node link to value for given key
     */
    private final Map<K, Node> keyToListNodeMap;

    /**
     * Capacity of cache
     */
    private final int capacity;

    /**
     * Most recently added value
     */
    private Node head;

    /**
     * Dummy tail node
     */
    private final Node tail;

    /**
     * Number of cached values
     */
    private int size;

    /**
     * Creates cache, that contains not greater than {@code capacity} values
     *
     * @param capacity maximum number of cached values
     */
    public LRUCache(int capacity) {
        assert capacity >= 1;
        head = tail = new Node();
        keyToListNodeMap = new HashMap<>();
        this.capacity = capacity;
        size = 0;
    }

    @Override
    protected V doPut(@Nonnull K key, @Nonnull V value) {
        assert size <= capacity;
        assert head.next == head;
        assert tail.prev == tail;

        Node currentNode = keyToListNodeMap.get(key);
        V currentValue = null;

        if (currentNode != null) {
            // already have values associated with given key
            // just change value inside node and shift up node to head
            currentValue = currentNode.value;
            currentNode.unlink();
            currentNode.value = value;
            appendToList(currentNode);
        } else if (size < capacity) {
            // create new node for this key
            currentNode = new Node(key, value);
            appendToList(currentNode);
            ++size;
            keyToListNodeMap.put(key, currentNode);
        } else {
            // reuse least recently added node for other <key, value> entry
            // and shift up this node to head
            // objects reusing cause creation of at most 'capacity' objects, so there is no overhead for gc
            assert size == capacity;
            Node latestNode = tail.next;
            tail.next = latestNode.next;
            latestNode.unlink();
            keyToListNodeMap.remove(latestNode.key);
            latestNode.key = key;
            latestNode.value = value;
            appendToList(latestNode);
            keyToListNodeMap.put(key, latestNode);
        }

        assert size <= capacity;
        assert head.next == head;
        assert tail.prev == tail;
        return currentValue;
    }

    @Override
    protected V doGet(@Nonnull K key) {
        assert size <= capacity;
        assert head.next == head;
        assert tail.prev == tail;

        if (!keyToListNodeMap.containsKey(key)) {
            return null;
        }
        Node node = keyToListNodeMap.get(key);

        assert node != null;
        return node.value;
    }

    /**
     * Appends given node after head
     */
    private void appendToList(@Nonnull Node node) {
        head.next = node;
        node.prev = head;
        head = node;
    }

    private class Node {

        private K key;
        private V value;
        private Node prev;
        private Node next;

        private Node() {
            key = null;
            value = null;
            prev = this;
            next = this;
        }

        private Node(@Nonnull K key, @Nonnull V value) {
            this.key = key;
            this.value = value;
            this.prev = this.next = this;
        }

        /**
         * Deletes this node from linked list
         */
        private void unlink() {
            this.next.prev = this.prev;
            this.prev.next = this.next;
            this.next = this.prev = this;
        }
    }
}
