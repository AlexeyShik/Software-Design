import java.util.concurrent.ThreadLocalRandom;

import org.junit.Assert;
import org.junit.Test;

public class LRUCacheTest {

    @Test
    public void test01SingleValueCache() {
        ILRUCache<Integer, Integer> cache = new LRUCache<>(1);
        Assert.assertNull(cache.get(0));

        addToCacheNewAndCheck(cache, 0, 1);
        Assert.assertNull(cache.get(1));

        addToCacheNewAndCheck(cache, 1, 2);
        Assert.assertNull(cache.get(0));
    }

    @Test
    public void test02MultipleValuesCache() {
        int capacity = 5;
        ILRUCache<Integer, Integer> cache = new LRUCache<>(capacity);

        for (int i = 0; i < capacity; ++i) {
            Assert.assertNull(cache.get(i));
            addToCacheNewAndCheck(cache, i, 10 * i);
        }

        for (int i = 0; i < capacity; ++i) {
            Assert.assertEquals(Integer.valueOf(10 * i), cache.get(i));
            addToCacheAndCheck(cache, i, 10 * i + 1, 10 * i);
        }

        for (int i = 0; i < capacity; ++i) {
            for (int j = 0; j < capacity; ++j) {
                if (j < i) {
                    Assert.assertNull(cache.get(j));
                } else {
                    Assert.assertEquals(Integer.valueOf(10 * j + 1), cache.get(j));
                }
            }

            addToCacheAndCheck(cache, i + capacity, 100 * i, null);
        }
    }

    @Test
    public void test03CornerCases() {
        Assert.assertThrows("Capacity shouldn't positive", AssertionError.class, () -> new LRUCache<>(-1));
        Assert.assertThrows("Capacity shouldn't positive", AssertionError.class, () -> new LRUCache<>(0));
        LRUCache<Integer, Integer> cache = new LRUCache<>(10);
        Assert.assertThrows("Get parameter shouldn't be null", AssertionError.class, () -> cache.get(null));
        Assert.assertThrows("Get parameter shouldn't be null", AssertionError.class, () -> cache.put(null, null));
        Assert.assertThrows("Get parameter shouldn't be null", AssertionError.class, () -> cache.put(null, 0));
        Assert.assertThrows("Get parameter shouldn't be null", AssertionError.class, () -> cache.put(0, null));
    }

    @Test
    public void test04ComplexDataTypes() {
        class K {
            final int a;
            final int b;

            public K(int a, int b) {
                this.a = a;
                this.b = b;
            }
        }

        K k1 = new K(1, 1);
        K k2 = new K(2, 2);

        class V {
            final int c;
            final int d;

            public V(int c, int d) {
                this.c = c;
                this.d = d;
            }
        }

        V v1 = new V(10, 10);
        V v2 = new V(20, 20);

        ILRUCache<K, V> cache = new LRUCache<>(2);
        Assert.assertNull(cache.get(k1));

        addToCacheNewAndCheck(cache, k1, v1);
        addToCacheNewAndCheck(cache, k2, v2);
        addToCacheAndCheck(cache, k1, v2, v1);
    }

    @Test
    public void test05Performance() {
        int N = (int) 1e7;
        for (int capacity = 1; capacity <= 10000; capacity *= 10) {
            System.out.println("100% cache hit, capacity = " + capacity);
            checkPerformance(capacity, 1, N);

            System.out.println("\n10% cache hit, capacity = " + capacity);
            checkPerformance(capacity, 10, N);

            System.out.println();
        }
    }

    private static <K, V> void addToCacheNewAndCheck(ILRUCache<K, V> cache, K key, V value) {
        V oldValue = cache.put(key, value);
        Assert.assertNull(oldValue);
        Assert.assertEquals(value, cache.get(key));
    }

    private static <K, V> void addToCacheAndCheck(ILRUCache<K, V> cache, K key, V value, V expectedOldValue) {
        V oldValue = cache.put(key, value);
        Assert.assertEquals(expectedOldValue, oldValue);
        Assert.assertEquals(value, cache.get(key));
    }

    private static void checkPerformance(int capacity, int keyOverheadFactor, int nValues) {
        double time = System.currentTimeMillis();

        ILRUCache<Integer, Integer> cache = new LRUCache<>(capacity);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < nValues; ++i) {
            Integer key = random.nextInt(0, capacity * keyOverheadFactor);
            Integer value = random.nextInt();
            cache.put(key, value);
            Assert.assertEquals(value, cache.get(key));
        }
        System.out.println("Time passed: " + (System.currentTimeMillis() - time));
    }
}
