package com.fiftyonred.mock_jedis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertEquals;

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
	public void testHget() {
		j.hset("test", "name", "value");
		assertEquals("value", j.hget("test", "name"));
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
	}
}
