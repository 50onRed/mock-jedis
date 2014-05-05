package com.fiftyonred.mock_jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class MockJedisPoolTest {
	@Test
	public void testPool() {
		JedisPool pool = new MockJedisPool(new JedisPoolConfig(), "someunknownhost");
		Jedis jedis = pool.getResource();
		pool.returnResource(jedis);
	}
}
