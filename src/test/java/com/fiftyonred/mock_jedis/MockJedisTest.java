package com.fiftyonred.mock_jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisDataException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MockJedisTest {
  private Jedis j = null;

  @Before
  public void setUp() {
    j = new MockJedis("test");
  }

  @Test
  public void testSet() {
    assertEquals("OK", j.set("test", "123"));
  }

  @Test
  public void testGet() {
    j.set("test", "123");
    assertEquals("123", j.get("test"));
    assertNull(j.get("unknown"));
  }

  @Test
  public void testHashes() {
    assertEquals(0L, j.hlen("test").longValue());
    assertEquals(0L, j.hdel("test", "name").longValue());
    assertNull(j.hget("test", "name"));
    j.hset("test", "name", "value");
    final Set<String> keys = j.hkeys("test");
    final Map<String, String> entries = j.hgetAll("test");
    final List<String> vals = j.hvals("test");
    assertTrue(keys.contains("name"));
    assertEquals(1, keys.size());
    assertEquals(1, entries.size());
    assertEquals("value", entries.get("name"));
    assertTrue(vals.contains("value"));
    assertEquals(1, vals.size());
    assertTrue(j.hexists("test", "name"));
    assertFalse(j.hexists("test", "name2"));
    assertEquals(1L, j.hlen("test").longValue());
    assertEquals("value", j.hget("test", "name"));
    assertEquals(1L, j.hdel("test", "name").longValue());
  }

  @Test
  public void testSets() {
    assertFalse(j.sismember("test", "member 1"));

    assertEquals(2L, (long) j.sadd("test", "member 1", "member 2"));
    assertEquals(1L, (long) j.sadd("test", "member 3"));

    // duplicate member 1. should drop
    assertEquals(0L, (long) j.sadd("test", "member 1"));

    assertEquals(3, j.smembers("test").size());

    // should remove member 3
    assertEquals(1L, (long) j.srem("test", "member 3"));

    List<String> sortedMembers = new ArrayList<String>(2);
    sortedMembers.addAll(j.smembers("test"));
    Collections.sort(sortedMembers);

    assertEquals("member 1", sortedMembers.get(0));
    assertEquals("member 2", sortedMembers.get(1));
  }

  @Test
  public void testHincrBy() {
    j.hincrBy("test1", "name", 10);
    assertEquals("10", j.hget("test1", "name"));

    j.hincrBy("test1", "name", -2);
    assertEquals("8", j.hget("test1", "name"));

    j.hset("test1", "name", "5");
    j.hincrBy("test1", "name", 2);
    assertEquals("7", j.hget("test1", "name"));

    j.hincrByFloat("test1", "name", -0.5D);
    assertEquals("6.5", j.hget("test1", "name"));
  }

  @Test
  public void testList() {
    assertEquals(Long.valueOf(0), j.llen("test"));

    j.lpush("test", "a");
    j.lpush("test", "b");
    j.lpush("test", "c");

    assertEquals(Long.valueOf(3), j.llen("test"));

    assertEquals("c", j.lpop("test"));
    assertEquals("b", j.lpop("test"));
    assertEquals("a", j.lpop("test"));

    assertEquals(Long.valueOf(0), j.llen("test"));
  }

  @Test
  public void testLRange() {
    j.lpush("test", "a");
    j.lpush("test", "b");
    j.lpush("test", "c");
    j.lpush("test", "d");

    assertEquals(Arrays.asList("a", "b"), j.lrange("test", 0, 1));
    assertEquals(Arrays.asList("c", "d"), j.lrange("test", 2, 5));
    assertEquals(Arrays.asList("c", "d"), j.lrange("test", -2, -1));
    assertEquals(Collections.singletonList("c"), j.lrange("test", -2, -2));
    assertEquals(0, j.lrange("test", -7, -6).size());
    assertEquals(0, j.lrange("test", 6, 7).size());
  }

  @Test
  public void testLTrim() {
    j.lpush("test", "a");
    j.lpush("test", "b");
    j.lpush("test", "c");
    j.lpush("test", "d");
    j.ltrim("test", 0, 1);
    assertEquals(Arrays.asList("a", "b"), j.lrange("test", 0, -1));
    j.lpush("test", "c");
    j.lpush("test", "d");
    j.ltrim("test", 2, 5);
    assertEquals(Arrays.asList("c", "d"), j.lrange("test", 0, -1));
    j.ltrim("test", 0, 0);
    j.lpush("test", "a");
    j.lpush("test", "b");
    j.lpush("test", "c");
    j.lpush("test", "d");
    j.ltrim("test", -2, -1);
    assertEquals(Arrays.asList("c", "d"), j.lrange("test", 0, -1));
    j.ltrim("test", 0, 0);
    j.lpush("test", "a");
    j.lpush("test", "b");
    j.lpush("test", "c");
    j.lpush("test", "d");
    assertEquals(Collections.singletonList("c"), j.lrange("test", -2, -2));
    j.ltrim("test", 0, 0);
    j.lpush("test", "a");
    j.lpush("test", "b");
    j.lpush("test", "c");
    j.lpush("test", "d");
    assertEquals(0, j.lrange("test", -7, -6).size());
    j.ltrim("test", 0, 0);
    j.lpush("test", "a");
    j.lpush("test", "b");
    j.lpush("test", "c");
    j.lpush("test", "d");
    assertEquals(0, j.lrange("test", 6, 7).size());
  }

  @Test
  public void testZremrangeByRank() {
    j.zadd("test", 0, "a");
    j.zadd("test", 1, "b");
    j.zadd("test", 2, "c");
    j.zadd("test", 3, "d");
    j.zremrangeByRank("test", 0, 1);
    assertEquals(new HashSet<String>(Arrays.asList("a", "b")), j.zrange("test", 0, -1));
    j.zadd("test", 2, "c");
    j.zadd("test", 3, "d");
    j.zremrangeByRank("test", 2, 5);
    assertEquals(new HashSet<String>(Arrays.asList("c", "d")), j.zrange("test", 0, -1));
    j.zremrangeByRank("test", 0, 0);
    j.zadd("test", 0, "a");
    j.zadd("test", 1, "b");
    j.zadd("test", 2, "c");
    j.zadd("test", 3, "d");
    j.zremrangeByRank("test", -2, -1);
    assertEquals(new HashSet<String>(Arrays.asList("c", "d")), j.zrange("test", 0, -1));
    j.zremrangeByRank("test", 0, 0);
    j.zadd("test", 0, "a");
    j.zadd("test", 1, "b");
    j.zadd("test", 2, "c");
    j.zadd("test", 3, "d");
    assertEquals(Collections.singleton("c"), j.zrange("test", -2, -2));
    j.zremrangeByRank("test", 0, 0);
    j.zadd("test", 0, "a");
    j.zadd("test", 1, "b");
    j.zadd("test", 2, "c");
    j.zadd("test", 3, "d");
    assertEquals(0, j.zrange("test", -7, -6).size());
    j.zremrangeByRank("test", 0, 0);
    j.zadd("test", 0, "a");
    j.zadd("test", 1, "b");
    j.zadd("test", 2, "c");
    j.zadd("test", 3, "d");
    assertEquals(0, j.zrange("test", 6, 7).size());
  }

  @Test
  public void testZRange() {
    assertEquals(Collections.emptySet(), j.zrange("test", -1, -1));
    j.zadd("test", 2, "c");
    j.zadd("test", 1, "b");
    j.zadd("test", 0, "a");
    j.zadd("test", 3, "d");

    assertEquals(new HashSet<String>(Arrays.asList("a", "b", "c", "d")), j.zrange("test", 0, -1));
    assertEquals(new HashSet<String>(Arrays.asList("a", "b")), j.zrange("test", 0, 1));
    assertEquals(new HashSet<String>(Arrays.asList("c", "d")), j.zrange("test", 2, 5));
    assertEquals(new HashSet<String>(Arrays.asList("c", "d")), j.zrange("test", -2, -1));
    assertEquals(Collections.singleton("c"), j.zrange("test", -2, -2));
    assertEquals(0, j.zrange("test", -7, -6).size());
    assertEquals(0, j.zrange("test", 6, 7).size());
  }

  @Test
  public void testZRangeWithScores() {
    assertEquals(Collections.emptySet(), j.zrange("test", -1, -1));
    j.zadd("test", 2, "c");
    j.zadd("test", 1, "b");
    j.zadd("test", 0, "a");
    j.zadd("test", 3, "d");

    assertArrayEquals(Arrays.asList(new Tuple("a", 0D), new Tuple("b", 1D),
        new Tuple("c", 2D), new Tuple("d", 3D)).toArray(), j.zrangeWithScores("test", 0, -1).toArray());
    assertArrayEquals(Arrays.asList(new Tuple("a", 0D), new Tuple("b", 1D)).toArray(),
        j.zrangeWithScores("test", 0, 1).toArray());
    assertArrayEquals(Arrays.asList(new Tuple("c", 2D), new Tuple("d", 3D)).toArray(),
        j.zrangeWithScores("test", 2, 5).toArray());
    assertArrayEquals(Arrays.asList(new Tuple("c", 2D), new Tuple("d", 3D)).toArray(),
        j.zrangeWithScores("test", -2, -1).toArray());
    assertEquals(Collections.singleton(new Tuple("c", 2D)), j.zrangeWithScores("test", -2, -2));
    assertEquals(Collections.emptySet(), j.zrangeWithScores("test", -7, -6));
    assertEquals(Collections.emptySet(), j.zrangeWithScores("test", 6, 7));
  }

  @Test
  public void testZRevRangeWithScores() {
    assertEquals(Collections.emptySet(), j.zrange("test", -1, -1));
    j.zadd("test", 2D, "c");
    j.zadd("test", 1D, "b");
    j.zadd("test", 0D, "a");
    j.zadd("test", 3D, "d");

    assertArrayEquals(Arrays.asList(new Tuple("d", 3D), new Tuple("c", 2D),
        new Tuple("b", 1D), new Tuple("a", 0D)).toArray(), j.zrevrangeWithScores("test", 0, -1)
        .toArray());
    assertArrayEquals(Arrays.asList(new Tuple("b", 1D), new Tuple("a", 0D)).toArray(),
        j.zrevrangeWithScores("test", 0, 1).toArray());
    assertArrayEquals(Arrays.asList(new Tuple("d", 3D), new Tuple("c", 2D)).toArray(),
        j.zrevrangeWithScores("test", 2, 5).toArray());
    assertArrayEquals(Arrays.asList(new Tuple("d", 3D), new Tuple("c", 2D)).toArray(),
        j.zrevrangeWithScores("test", -2, -1).toArray());
    assertArrayEquals(new Object[]{new Tuple("c", 2D)}, j.zrevrangeWithScores("test", -2, -2)
        .toArray());
    assertEquals(Collections.emptySet(), j.zrevrangeWithScores("test", -7, -6));
    assertEquals(Collections.emptySet(), j.zrevrangeWithScores("test", 6, 7));
  }

  @Test
  public void testZCard() {
    assertEquals((Long) 0L, j.zcard("test"));
    j.zadd("test", 2, "c");
    assertEquals((Long) 1L, j.zcard("test"));
    j.zadd("test", 1, "a");
    assertEquals((Long) 2L, j.zcard("test"));
  }

  @Test
  public void testZRank() {
    assertNull(j.zrank("test", "foo"));
    j.zadd("test", 2, "c");
    j.zadd("test", 1, "b");
    j.zadd("test", 0, "a");
    j.zadd("test", 3, "d");

    assertEquals((Long) 0L, j.zrank("test", "a"));
    assertEquals((Long) 1L, j.zrank("test", "b"));
    assertEquals((Long) 2L, j.zrank("test", "c"));
    assertEquals((Long) 3L, j.zrank("test", "d"));
    assertNull(j.zrank("test", "e"));
  }

  @Test
  public void testZRangeByScore() {
    j.zadd("test", 2, "foo");
    j.zadd("test", 1, "bar");
    j.zadd("test", 3.4, "baz");
    j.zadd("test", 0, "qux");
    j.zadd("test", -1, "blah");
    j.zadd("test", 10, "kit");
    j.zadd("test", 16.92131, "kat");
    assertArrayEquals(Arrays.asList("blah", "qux", "bar", "foo", "baz", "kit", "kat").toArray(),
        j.zrangeByScore("test", "-inf", "+inf").toArray());
    assertArrayEquals(Arrays.asList("blah", "qux", "bar", "foo").toArray(),
        j.zrangeByScore("test", "-inf", "2").toArray());
    assertEquals(Collections.emptySet(), j.zrangeByScore("test", "20", "30"));
    assertEquals(Collections.emptySet(), j.zrangeByScore("test", 20, 30));
    assertEquals(Collections.emptySet(), j.zrangeByScore("test", "0", "-2"));
    assertEquals(Collections.emptySet(), j.zrangeByScore("test", 0, -2));
    assertEquals(Collections.emptySet(), j.zrangeByScore("test", "2", "1"));
    assertEquals(Collections.emptySet(), j.zrangeByScore("test", 2, 1));
    assertEquals(Collections.singleton("foo"), j.zrangeByScore("test", "2", "2"));
    assertEquals(Collections.singleton("foo"), j.zrangeByScore("test", 2, 2));
  }

  @Test
  public void testZRevRangeByScore() {
    j.zadd("test", 2, "foo");
    j.zadd("test", 1, "bar");
    j.zadd("test", 3.4, "baz");
    j.zadd("test", 0, "qux");
    j.zadd("test", -1, "blah");
    j.zadd("test", 10, "kit");
    j.zadd("test", 16.92131, "kat");
    assertArrayEquals(Arrays.asList("kat", "kit", "baz", "foo", "bar", "qux", "blah").toArray(),
        j.zrevrangeByScore("test", Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).toArray());
    assertEquals(new TreeSet<String>(Arrays.asList("blah", "qux", "bar", "foo")),
        j.zrevrangeByScore("test", "2", "-inf"));
    assertEquals(Collections.emptySet(), j.zrevrangeByScore("test", "30", "20"));
    assertEquals(Collections.emptySet(), j.zrevrangeByScore("test", 30, 20));
    assertEquals(Collections.emptySet(), j.zrevrangeByScore("test", "-2", "0"));
    assertEquals(Collections.emptySet(), j.zrevrangeByScore("test", -2, 0));
    assertEquals(Collections.emptySet(), j.zrevrangeByScore("test", "1", "2"));
    assertEquals(Collections.emptySet(), j.zrevrangeByScore("test", 1, 2));
    assertEquals(Collections.singleton("foo"), j.zrevrangeByScore("test", "2", "2"));
    assertEquals(Collections.singleton("foo"), j.zrevrangeByScore("test", 2, 2));
  }

  @Test
  public void testZAdd() {
    j.zadd("test", 0, "foo");
    assertEquals(Collections.singleton("foo"), j.zrange("test", 0, -1));
    j.zadd("test", 2, "foo");
    assertEquals(Collections.singleton("foo"), j.zrange("test", 0, -1));
    assertEquals(Collections.singleton(new Tuple("foo", 2D)), j.zrangeWithScores("test", 0, -1));
  }

  @Test
  public void testZRangeByScoreWithScores() {
    j.zadd("test", 2, "foo");
    j.zadd("test", 1, "bar");
    j.zadd("test", 3.4, "baz");
    j.zadd("test", 0, "qux");
    j.zadd("test", -1, "blah");
    j.zadd("test", 10, "kit");
    j.zadd("test", 16.92131, "kat");
    assertArrayEquals(Arrays.asList(new Tuple("blah", -1D), new Tuple("qux", 0D),
        new Tuple("bar", 1D), new Tuple("foo", 2D), new Tuple("baz", 3.4D), new Tuple("kit", 10D),
        new Tuple("kat", 16.92131D)).toArray(),
        j.zrangeByScoreWithScores("test", "-inf", "+inf").toArray());
    assertArrayEquals(Arrays.asList(new Tuple("blah", -1D), new Tuple("qux", 0D),
        new Tuple("bar", 1D), new Tuple("foo", 2D)).toArray(),
        j.zrangeByScoreWithScores("test", "-inf", "2").toArray());
    assertEquals(Collections.emptySet(), j.zrangeByScoreWithScores("test", "20", "30"));
    assertEquals(Collections.emptySet(), j.zrangeByScoreWithScores("test", 20, 30));
    assertEquals(Collections.emptySet(), j.zrangeByScoreWithScores("test", "0", "-2"));
    assertEquals(Collections.emptySet(), j.zrangeByScoreWithScores("test", 0, -2));
    assertEquals(Collections.emptySet(), j.zrangeByScoreWithScores("test", "2", "1"));
    assertEquals(Collections.emptySet(), j.zrangeByScoreWithScores("test", 2, 1));
    assertEquals(Collections.singleton(new Tuple("foo", 2D)),
        j.zrangeByScoreWithScores("test", "2", "2"));
    assertEquals(Collections.singleton(new Tuple("foo", 2D)),
        j.zrangeByScoreWithScores("test", 2, 2));
  }

  @Test
  public void testZRevrangeByScoreWithScores() {
    j.zadd("test", 2, "foo");
    j.zadd("test", 1, "bar");
    j.zadd("test", 3.4, "baz");
    j.zadd("test", 0, "qux");
    j.zadd("test", -1, "blah");
    j.zadd("test", 10, "kit");
    j.zadd("test", 16.92131, "kat");
    List<Tuple> full = Arrays.asList(new Tuple("blah", -1D), new Tuple("qux", 0D),
        new Tuple("bar", 1D), new Tuple("foo", 2D), new Tuple("baz", 3.4D), new Tuple("kit", 10D),
        new Tuple("kat", 16.92131D));
    Collections.reverse(full);
    assertArrayEquals(full.toArray(),
        j.zrevrangeByScoreWithScores("test", "+inf", "-inf").toArray());
    List<Tuple> partial = Arrays.asList(new Tuple("blah", -1D), new Tuple("qux", 0D),
        new Tuple("bar", 1D), new Tuple("foo", 2D));
    Collections.reverse(partial);
    assertArrayEquals(partial.toArray(),
        j.zrevrangeByScoreWithScores("test", "2", "-inf").toArray());
    assertEquals(Collections.emptySet(), j.zrevrangeByScoreWithScores("test", "30", "20"));
    assertEquals(Collections.emptySet(), j.zrevrangeByScoreWithScores("test", 30, 20));
    assertEquals(Collections.emptySet(), j.zrevrangeByScoreWithScores("test", "-2", "0"));
    assertEquals(Collections.emptySet(), j.zrevrangeByScoreWithScores("test", -2, 0));
    assertEquals(Collections.emptySet(), j.zrevrangeByScoreWithScores("test", "1", "2"));
    assertEquals(Collections.emptySet(), j.zrevrangeByScoreWithScores("test", 1, 2));
    assertEquals(Collections.singleton(new Tuple("foo", 2D)),
        j.zrevrangeByScoreWithScores("test", "2", "2"));
    assertEquals(Collections.singleton(new Tuple("foo", 2D)),
        j.zrevrangeByScoreWithScores("test", 2, 2));
  }

  @Test
  public void testSort() {
    j.lpush("test", "a");
    j.lpush("test", "c");
    j.lpush("test", "b");
    j.lpush("test", "d");

    try {
      j.sort("test");
      fail("Sorting numbers is default");
    } catch (JedisDataException e) {
    }

    assertEquals(Arrays.asList("a", "b", "c", "d"), j.sort("test", new SortingParams().alpha()));
    assertEquals(Arrays.asList("d", "c", "b", "a"), j.sort("test", new SortingParams().desc()
        .alpha()));

    j.sort("test", new SortingParams().alpha(), "newkey");

    assertEquals(Arrays.asList("a", "b", "c", "d"), j.lrange("newkey", 0, 10));

    j.sadd("settest", "1", "2", "3", "4", "5", "6");

    assertEquals(Arrays.asList("1", "2", "3", "4", "5", "6"), j.sort("settest"));
    assertEquals(Arrays.asList("3", "4", "5"), j.sort("settest", new SortingParams().limit(2, 3)));
    assertEquals(Arrays.asList("4", "3", "2"), j.sort("settest", new SortingParams().limit(2, 3)
        .desc()));
  }

  @Test(expected = JedisDataException.class)
  public void testInvalidKeyTypeHashToString() {
    j.hset("test", "test", "1");
    j.get("test");
  }

  @Test(expected = JedisDataException.class)
  public void testInvalidKeyTypeHashToList() {
    j.hset("test", "test", "1");
    j.llen("test");
  }

  @Test(expected = JedisDataException.class)
  public void testInvalidKeyTypeStringToHash() {
    j.set("test", "test");
    j.hget("test", "test");
  }

  @Test(expected = JedisDataException.class)
  public void testInvalidKeyTypeStringToList() {
    j.set("test", "test");
    j.lpop("test");
  }

  @Test(expected = JedisDataException.class)
  public void testInvalidKeyTypeListToHash() {
    j.lpush("test", "test");
    j.hgetAll("test");
  }

  @Test(expected = JedisDataException.class)
  public void testInvalidKeyTypeListToString() {
    j.lpush("test", "test");
    j.incr("test");
  }

  @Test
  public void testKeys() {
    j.set("A1", "value");
    j.set("A2", "value");
    j.set("A3", "value");
    j.hset("B1", "name", "value");
    j.hset("B2", "name", "value");
    j.hset("C2C", "name", "value");

    assertEquals(6, j.keys("*").size());
    assertEquals(1, j.keys("A1").size());
    assertEquals(3, j.keys("A*").size());
    assertEquals(2, j.keys("*1").size());
    assertEquals(3, j.keys("*2*").size());
    assertEquals(1, j.keys("C*C").size());

    j.set("testC2C", "value");
    assertEquals(1, j.keys("C*C").size());
  }

  @Test
  public void testMultipleDB() {
    assertEquals(0L, j.dbSize().longValue());
    j.set("test", "test");
    assertEquals(1L, j.dbSize().longValue());
    j.move("test", 5);
    assertEquals(0L, j.dbSize().longValue());
    j.select(5);
    assertEquals(1L, j.dbSize().longValue());
  }

  /**
   * In this simple proposal, we're not testing complex iterations
   * of scan cursor. SCAN is simply a wrapper for KEYS, and the result
   * is given in one single response, no matter the COUNT argument.
   */
  @Test
  public void testScan() {
    j.set("key1", "val1");
    j.set("key2", "val2");
    j.set("kk", "val3");

    ScanParams scanParams = new ScanParams();
    scanParams.count(100).match("key*");
    ScanResult<String> scanResult;
    scanResult = j.scan("0", scanParams);


    assertEquals(2L, scanResult.getResult().size());
    assertEquals("0", scanResult.getStringCursor());
  }
}
