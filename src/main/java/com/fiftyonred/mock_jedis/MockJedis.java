package com.fiftyonred.mock_jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockJedis extends Jedis {

	private final MockPipeline pipeline;

	public MockJedis(final String host) {
		super(host);
		pipeline = new MockPipeline();
	}

	@Override
	public Long getDB() {
		return (long) pipeline.getCurrentDB();
	}

	@Override
	public String echo(final String string) {
		return pipeline.echo(string).get();
	}

	@Override
	public byte[] echo(final byte[] string) {
		return pipeline.echo(string).get();
	}

	@Override
	public String randomKey() {
		return pipeline.randomKey().get();
	}

	@Override
	public String rename(String oldkey, String newkey) {
		return pipeline.rename(oldkey, newkey).get();
	}

	@Override
	public String rename(byte[] oldkey, byte[] newkey) {
		return pipeline.rename(oldkey, newkey).get();
	}

	@Override
	public Long renamenx(String oldkey, String newkey) {
		return pipeline.renamenx(oldkey, newkey).get();
	}

	@Override
	public Long renamenx(byte[] oldkey, byte[] newkey) {
		return pipeline.renamenx(oldkey, newkey).get();
	}

	@Override
	public byte[] randomBinaryKey() {
		return pipeline.randomKeyBinary().get();
	}

	@Override
	public Long persist(final String key) {
		return pipeline.persist(key).get();
	}

	@Override
	public Long persist(final byte[] key) {
		return pipeline.persist(key).get();
	}

	@Override
	public byte[] get(final byte[] key) {
		return pipeline.get(key).get();
	}

	@Override
	public byte[] dump(byte[] key) {
		return pipeline.dump(key).get();
	}

	@Override
	public byte[] dump(final String key) {
		return pipeline.dump(key).get();
	}

	@Override
	public String restore(byte[] key, int ttl, byte[] serializedValue) {
		return pipeline.restore(key, ttl, serializedValue).get();
	}

	@Override
	public String restore(String key, int ttl, byte[] serializedValue) {
		return pipeline.restore(key, ttl, serializedValue).get();
	}

	@Override
	public Long del(final byte[]... keys) {
		return pipeline.del(keys).get();
	}

	@Override
	public String flushDB() {
		return pipeline.flushDB().get();
	}

	@Override
	public Set<byte[]> keys(final byte[] pattern) {
		return pipeline.keys(pattern).get();
	}

	@Override
	public List<byte[]> mget(final byte[]... keys) {
		return pipeline.mget(keys).get();
	}

	@Override
	public String mset(byte[]... keysvalues) {
		return pipeline.mset(keysvalues).get();
	}

	@Override
	public Long incrBy(byte[] key, long integer) {
		return pipeline.incrBy(key, integer).get();
	}

	@Override
	public Long incr(byte[] key) {
		return pipeline.incr(key).get();
	}

	@Override
	public Long decr(byte[] key) {
		return pipeline.decr(key).get();
	}

	@Override
	public Long decrBy(byte[] key, long integer) {
		return pipeline.decrBy(key, integer).get();
	}

	@Override
	public Long hset(byte[] key, byte[] field, byte[] value) {
		return pipeline.hset(key, field, value).get();
	}

	@Override
	public byte[] hget(byte[] key, byte[] field) {
		return pipeline.hget(key, field).get();
	}

	@Override
	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		return pipeline.hmset(key, hash).get();
	}

	@Override
	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		return pipeline.hmget(key, fields).get();
	}

	@Override
	public Long hincrBy(byte[] key, byte[] field, long value) {
		return pipeline.hincrBy(key, field, value).get();
	}

	@Override
	public Boolean hexists(byte[] key, byte[] field) {
		return pipeline.hexists(key, field).get();
	}

	@Override
	public Long hdel(byte[] key, byte[]... fields) {
		return pipeline.hdel(key, fields).get();
	}

	@Override
	public Long hlen(byte[] key) {
		return pipeline.hlen(key).get();
	}

	@Override
	public Set<byte[]> hkeys(byte[] key) {
		return pipeline.hkeys(key).get();
	}

	@Override
	public List<byte[]> hvals(byte[] key) {
		return pipeline.hvals(key).get();
	}

	@Override
	public Map<byte[], byte[]> hgetAll(byte[] key) {
		return pipeline.hgetAll(key).get();
	}

	@Override
	public String ping() {
		return pipeline.ping().get();
	}

	@Override
	public Long dbSize() {
		return pipeline.dbSize().get();
	}

	@Override
	public String type(final String key) {
		return pipeline.type(key).get();
	}

	@Override
	public String type(final byte[] key) {
		return pipeline.type(key).get();
	}

	@Override
	public Long move(final String key, final int dbIndex) {
		return pipeline.move(key, dbIndex).get();
	}

	@Override
	public Long move(final byte[] key, final int dbIndex) {
		return pipeline.move(key, dbIndex).get();
	}

	@Override
	public String select(final int dbIndex) {
		return pipeline.select(dbIndex).get();
	}

	@Override
	public String set(final String key, final String value) {
		return pipeline.set(key, value).get();
	}

	@Override
	public String set(final byte[] key, final byte[] value) {
		return pipeline.set(key, value).get();
	}

	@Override
	public String get(final String key) {
		return pipeline.get(key).get();
	}

	@Override
	public List<String> mget(final String... keys) {
		return pipeline.mget(keys).get();
	}

	@Override
	public String flushAll() {
		return pipeline.flushAll().get();
	}

	@Override
	public Long decrBy(String key, long integer) {
		return pipeline.decrBy(key, integer).get();
	}

	@Override
	public Long decr(String key) {
		return pipeline.decr(key).get();
	}

	@Override
	public Long expire(String key, int seconds) {
		return pipeline.expire(key, seconds).get();
	}

	@Override
	public Long expire(byte[] key, int seconds) {
		return pipeline.expire(key, seconds).get();
	}

    @Override
    public Long expireAt(String key, long unixTime) {
        return pipeline.expireAt(key, unixTime).get();
    }

	@Override
	public Long expireAt(byte[] key, long unixTime) {
		return pipeline.expireAt(key, unixTime).get();
	}

    @Override
    public String psetex(String key, int milliseconds, String value) {
        return pipeline.psetex(key, milliseconds, value).get();
    }

	@Override
	public String psetex(byte[] key, int milliseconds, byte[] value) {
		return pipeline.psetex(key, milliseconds, value).get();
	}

    @Override
    public Long ttl(String key) {
        return pipeline.ttl(key).get();
    }

	@Override
	public Long ttl(byte[] key) {
		return pipeline.ttl(key).get();
	}

    @Override
    public Long pttl(String key) {
        return pipeline.pttl(key).get();
    }

	@Override
	public Long pttl(byte[] key) {
		return pipeline.pttl(key).get();
	}

    @Override
    public Long pexpire(String key, int milliseconds) {
        return pipeline.pexpire(key, milliseconds).get();
    }

	@Override
	public Long pexpire(byte[] key, int milliseconds) {
		return pipeline.pexpire(key, milliseconds).get();
	}

    @Override
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        return pipeline.pexpireAt(key, millisecondsTimestamp).get();
    }

	@Override
	public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
		return pipeline.pexpireAt(key, millisecondsTimestamp).get();
	}

	@Override
	public Boolean exists(String key) {
		return pipeline.exists(key).get();
	}

	@Override
	public Boolean exists(byte[] key) {
		return pipeline.exists(key).get();
	}

    @Override
	public Long incr(String key) {
		return pipeline.incr(key).get();
	}

	@Override
	public Long incrBy(String key, long integer) {
		return pipeline.incrBy(key, integer).get();
	}

    @Override
    public String setex(String key, int seconds, String value) {
        return pipeline.setex(key, seconds, value).get();
    }

	@Override
	public String setex(byte[] key, int seconds, byte[] value) {
		return pipeline.setex(key, seconds, value).get();
	}

	@Override
	public Long del(String... keys) {
		return pipeline.del(keys).get();
	}

	@Override
	public Long del(String key) {
		return pipeline.del(key).get();
	}

	@Override
	public Long del(byte[] key) {
		return pipeline.del(key).get();
	}

	@Override
	public Long hset(final String key, final String field, final String value) {
		return pipeline.hset(key, field, value).get();
	}

	@Override
	public Long hsetnx(final String key, final String field, final String value) {
		return pipeline.hsetnx(key, field, value).get();
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
	public Set<String> hkeys(final String key) {
		return pipeline.hkeys(key).get();
	}

	@Override
	public List<String> hvals(final String key) {
		return pipeline.hvals(key).get();
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
	public Long hincrBy(String key, String field, long value) {
		return pipeline.hincrBy(key, field, value).get();
	}

	@Override
	public Double hincrByFloat(String key, String field, double value) {
		return pipeline.hincrByFloat(key, field, value).get();
	}

	@Override
	public Long hdel(final String key, final String... fields) {
		return pipeline.hdel(key, fields).get();
	}

	@Override
	public Boolean hexists(final String key, final String field) {
		return pipeline.hexists(key, field).get();
	}

	@Override
	public Long hlen(final String key) {
		return pipeline.hlen(key).get();
	}

	@Override
	public Long lpush(final String key, String... strings) {
		return pipeline.lpush(key, strings).get();
	}

    @Override
    public Long sadd(String key, String... members) {
        return pipeline.sadd(key, members).get();
    }

	@Override
	public Long sadd(byte[] key, byte[]... members) {
		return pipeline.sadd(key, members).get();
	}

	@Override
	public Long scard(String key) {
		return pipeline.scard(key).get();
	}

	@Override
	public Long scard(byte[] key) {
		return pipeline.scard(key).get();
	}

	@Override
	public Set<String> sdiff(String... keys) {
		return pipeline.sdiff(keys).get();
	}

	@Override
	public Set<byte[]> sdiff(byte[]... keys) {
		return pipeline.sdiff(keys).get();
	}

	@Override
	public Long sdiffstore(String dstKey, String... keys) {
		return pipeline.sdiffstore(dstKey, keys).get();
	}

	@Override
	public Long sdiffstore(byte[] dstKey, byte[]... keys) {
		return pipeline.sdiffstore(dstKey, keys).get();
	}

	@Override
	public Set<String> sinter(String... keys) {
		return pipeline.sinter(keys).get();
	}

	@Override
	public Set<byte[]> sinter(byte[]... keys) {
		return pipeline.sinter(keys).get();
	}

	@Override
	public Long sinterstore(String dstKey, String... keys) {
		return pipeline.sinterstore(dstKey, keys).get();
	}

	@Override
	public Long sinterstore(byte[] dstKey, byte[]... keys) {
		return pipeline.sinterstore(dstKey, keys).get();
	}

	@Override
	public Boolean sismember(String key, String member) {
		return pipeline.sismember(key, member).get();
	}

	@Override
	public Boolean sismember(byte[] key, byte[] member) {
		return pipeline.sismember(key, member).get();
	}

	@Override
	public Set<String> smembers(String key) {
		return pipeline.smembers(key).get();
	}

	@Override
	public Set<byte[]> smembers(byte[] key) {
		return pipeline.smembers(key).get();
	}

	@Override
	public Long smove(String srcKey, String dstKey, String member) {
		return pipeline.smove(srcKey, dstKey, member).get();
	}

	@Override
	public Long smove(byte[] srcKey, byte[] dstKey, byte[] member) {
		return pipeline.smove(srcKey, dstKey, member).get();
	}

	@Override
	public String spop(String key) {
		return pipeline.spop(key).get();
	}

	@Override
	public byte[] spop(byte[] key) {
		return pipeline.spop(key).get();
	}

	@Override
	public String srandmember(String key) {
		return pipeline.srandmember(key).get();
	}

	@Override
	public byte[] srandmember(byte[] key) {
		return pipeline.srandmember(key).get();
	}

	@Override
	public Long srem(String key, String... members) {
		return pipeline.srem(key, members).get();
	}

	@Override
	public Long srem(byte[] key, byte[]... members) {
		return pipeline.srem(key, members).get();
	}

	@Override
	public Set<String> sunion(String... keys) {
		return pipeline.sunion(keys).get();
	}

	@Override
	public Set<byte[]> sunion(byte[]... keys) {
		return pipeline.sunion(keys).get();
	}

	@Override
	public Long sunionstore(String dstKey, String... keys) {
		return pipeline.sunionstore(dstKey, keys).get();
	}

	@Override
	public Long sunionstore(byte[] dstKey, byte[]... keys) {
		return pipeline.sunionstore(dstKey, keys).get();
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

	@Override
	public void connect() {
		// do nothing
	}

	@Override
	public void disconnect() {
		// do nothing
	}

	@Override
	public String quit() {
		return "OK";
	}
}
