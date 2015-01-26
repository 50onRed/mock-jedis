package com.fiftyonred.mock_jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class MockJedisPool extends JedisPool {
	private MockJedis client = null;

	public MockJedisPool(final GenericObjectPoolConfig poolConfig, final String host) {
		super(poolConfig, host);
	}

	@Override
	public Jedis getResource() {
		if (client == null) {
			client = new MockJedis("localhost");
		}
		return client;
	}

	@Override
	public void returnResource(final Jedis resource) {

	}

	@Override
	public void returnBrokenResource(final Jedis resource) {

	}

	public void setClient(final MockJedis client) {
		this.client = client;
	}
	
	@Override
	public void returnResourceObject(final Jedis resource) { //required for compatibility with org.sedis.Sedis
	                                                         //wrapper for Scala
    	}
}
