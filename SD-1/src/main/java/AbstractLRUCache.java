import java.util.Objects;

import javax.annotation.Nonnull;

public abstract class AbstractLRUCache<K, V> implements ILRUCache<K, V> {

    @Override
    public V put(K key, V value) {
        assert key != null && value != null;
        V previousValue = doGet(key);
        V result = doPut(key, value);
        assert Objects.equals(previousValue, result);
        V currentValue = doGet(key);
        assert Objects.equals(currentValue, value);
        return result;
    }

    @Override
    public V get(K key) {
        assert key != null;
        return doGet(key);
    }

    protected abstract V doPut(@Nonnull K key, @Nonnull V value);

    protected abstract V doGet(@Nonnull K key);
}
