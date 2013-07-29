package com.fiftyonred.mock_jedis;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class MockJedisPool extends JedisPool {
	private MockJedis client = null;

	public MockJedisPool(Config poolConfig, String host) {
		super(poolConfig, host);
	}
	
	public Jedis getResource() {
        if (client == null) {
        	client = new MockJedis("localhost");
        }
        return client;
    }
	
}
