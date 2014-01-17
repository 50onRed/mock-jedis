package com.fiftyonred.mock_jedis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.*;

import static org.junit.Assert.*;

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
		assertEquals(null, j.get("unknown"));
	}

	@Test
	public void testHashes() {
		assertEquals(0L, j.hlen("test").longValue());
		assertEquals(0L, j.hdel("test", "name").longValue());
		assertEquals(null, j.hget("test", "name"));
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
        assertEquals(2L, (long)j.sadd("test", "member 1", "member 2"));
        assertEquals(1L, (long)j.sadd("test", "member 3"));

        // duplicate member 1. should drop
        assertEquals(0L, (long) j.sadd("test", "member 1"));

        assertEquals(3, j.smembers("test").size());

        // should remove member 3
        assertEquals(1L, (long)j.srem("test", "member 3"));

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
		assertEquals(Arrays.asList("c"), j.lrange("test", -2, -2));
		assertEquals(0, j.lrange("test", -7, -6).size());
		assertEquals(0, j.lrange("test", 6, 7).size());
	}

	@Test(expected=JedisDataException.class)
	public void testInvalidKeyTypeHashToString() {
		j.hset("test", "test", "1");
		j.get("test");
	}

	@Test(expected=JedisDataException.class)
	public void testInvalidKeyTypeHashToList() {
		j.hset("test", "test", "1");
		j.llen("test");
	}

	@Test(expected=JedisDataException.class)
	public void testInvalidKeyTypeStringToHash() {
		j.set("test", "test");
		j.hget("test", "test");
	}

	@Test(expected=JedisDataException.class)
	public void testInvalidKeyTypeStringToList() {
		j.set("test", "test");
		j.lpop("test");
	}

	@Test(expected=JedisDataException.class)
	public void testInvalidKeyTypeListToHash() {
		j.lpush("test", "test");
		j.hgetAll("test");
	}

	@Test(expected=JedisDataException.class)
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
}
