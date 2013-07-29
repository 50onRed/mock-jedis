package com.fiftyonred.mock_jedis;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class MockJedisTest {
	private Jedis j;
	
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
}
