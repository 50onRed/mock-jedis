package com.fiftyonred.mock_jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockJedis extends Jedis {
	
	private MockPipeline pipeline = null;

	public MockJedis(String host) {
		super(host);
		pipeline = new MockPipeline();
	}

	@Override
	public String set(final String key, String value) {
		return pipeline.set(key, value).get();
	}

	@Override
	public String get(final String key) {
		return pipeline.get(key).get();
	}
	
	@Override
	public List<String> mget(final String... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'mget' command");
		}
		return pipeline.mget(keys).get();
	}
	
	@Override
	public String flushAll() {
		pipeline.clear();
		return "OK";
	}
	
	@Override
	public Long decrBy(String key, long integer) {
		return pipeline.decrBy(key, integer).get();
	}

	@Override
	public Long decr(String key) {
		return decrBy(key, 1L);
	}

	@Override
	public Long expire(String key, int seconds) {
		return 1L;
	}

	@Override
	public Long incr(String key) {
		return incrBy(key, 1L);
	}

	@Override
	public Long incrBy(String key, long integer) {
		return pipeline.incrBy(key, integer).get();
	}

	@Override
	public String setex(String key, int seconds, String value) {
		return set(key, value);
	}
	
	@Override
	public Long del(String... keys) {
		return pipeline.del(keys).get();
	}
	
	@Override
	public Long hset(final String key, final String field, final String value) {
		return pipeline.hset(key, field, value).get();
	}
	
	@Override
	public String hget(final String key, final String field) {
		return pipeline.hget(key, field).get();
	}

	@Override
	public Map<String, String> hgetAll(final String key) {
		return pipeline.hgetAll(key).get();
	}
	
	@Override
	public String hmset(final String key, final Map<String, String> hash) {
		return pipeline.hmset(key, hash).get();
	}
	
	@Override
	public List<String> hmget(final String key, final String... fields) {
		return pipeline.hmget(key, fields).get();
	}

	@Override
	public Long lpush(final String key, String... strings) {
		return pipeline.lpush(key, strings).get();
	}

	@Override
	public String lpop(final String key) {
		return pipeline.lpop(key).get();
	}

	@Override
	public Long llen(final String key) {
		return pipeline.llen(key).get();
	}
	
	@Override
	public Pipeline pipelined() {
		return pipeline;
	}

	@Override
	public Set<String> keys(final String pattern) {
		return pipeline.keys(pattern).get();
	}
}
