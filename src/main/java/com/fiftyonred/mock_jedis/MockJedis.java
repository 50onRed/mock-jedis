package com.fiftyonred.mock_jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.SortingParams;

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
	public Long append(final String key, final String value) {
		return pipeline.append(key, value).get();
	}

	@Override
	public Long append(final byte[] key, final byte[] value) {
		return pipeline.append(key, value).get();
	}

	@Override
	public String getSet(final String key, final String value) {
		return pipeline.getSet(key, value).get();
	}

	@Override
	public byte[] getSet(final byte[] key, final byte[] value) {
		return pipeline.getSet(key, value).get();
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
	public String rename(final String oldkey, final String newkey) {
		return pipeline.rename(oldkey, newkey).get();
	}

	@Override
	public String rename(final byte[] oldkey, final byte[] newkey) {
		return pipeline.rename(oldkey, newkey).get();
	}

	@Override
	public Long renamenx(final String oldkey, final String newkey) {
		return pipeline.renamenx(oldkey, newkey).get();
	}

	@Override
	public Long renamenx(final byte[] oldkey, final byte[] newkey) {
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
	public byte[] dump(final byte[] key) {
		return pipeline.dump(key).get();
	}

	@Override
	public byte[] dump(final String key) {
		return pipeline.dump(key).get();
	}

	@Override
	public String restore(final byte[] key, final int ttl, final byte[] serializedValue) {
		return pipeline.restore(key, ttl, serializedValue).get();
	}

	@Override
	public String restore(final String key, final int ttl, final byte[] serializedValue) {
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
	public String mset(final String... keysvalues) {
		return pipeline.mset(keysvalues).get();
	}

	@Override
	public String mset(final byte[]... keysvalues) {
		return pipeline.mset(keysvalues).get();
	}

	@Override
	public Long msetnx(final String... keysvalues) {
		return pipeline.msetnx(keysvalues).get();
	}

	@Override
	public Long msetnx(final byte[]... keysvalues) {
		return pipeline.msetnx(keysvalues).get();
	}

	@Override
	public Long incrBy(final byte[] key, final long integer) {
		return pipeline.incrBy(key, integer).get();
	}

	@Override
	public Double incrByFloat(final String key, final double integer) {
		return pipeline.incrByFloat(key, integer).get();
	}

	@Override
	public Double incrByFloat(final byte[] key, final double integer) {
		return pipeline.incrByFloat(key, integer).get();
	}

	@Override
	public Long strlen(final String key) {
		return pipeline.strlen(key).get();
	}

	@Override
	public Long strlen(final byte[] key) {
		return pipeline.strlen(key).get();
	}

	@Override
	public Long incr(final byte[] key) {
		return pipeline.incr(key).get();
	}

	@Override
	public Long decr(final byte[] key) {
		return pipeline.decr(key).get();
	}

	@Override
	public Long decrBy(final byte[] key, final long integer) {
		return pipeline.decrBy(key, integer).get();
	}

	@Override
	public List<String> sort(final String key) {
		return pipeline.sort(key).get();
	}

	@Override
	public List<byte[]> sort(final byte[] key) {
		return pipeline.sort(key).get();
	}

	@Override
	public Long sort(final String key, final String dstkey) {
		return pipeline.sort(key, dstkey).get();
	}

	@Override
	public Long sort(final byte[] key, final byte[] dstkey) {
		return pipeline.sort(key, dstkey).get();
	}

	@Override
	public List<String> sort(final String key, final SortingParams sortingParameters) {
		return pipeline.sort(key, sortingParameters).get();
	}

	@Override
	public List<byte[]> sort(final byte[] key, final SortingParams sortingParameters) {
		return pipeline.sort(key, sortingParameters).get();
	}

	@Override
	public Long sort(final String key, final SortingParams sortingParameters, final String dstkey) {
		return pipeline.sort(key, sortingParameters, dstkey).get();
	}

	@Override
	public Long sort(final byte[] key, final SortingParams sortingParameters, final byte[] dstkey) {
		return pipeline.sort(key, sortingParameters, dstkey).get();
	}

	@Override
	public Long hset(final byte[] key, final byte[] field, final byte[] value) {
		return pipeline.hset(key, field, value).get();
	}

	@Override
	public byte[] hget(final byte[] key, final byte[] field) {
		return pipeline.hget(key, field).get();
	}

	@Override
	public String hmset(final byte[] key, final Map<byte[], byte[]> hash) {
		return pipeline.hmset(key, hash).get();
	}

	@Override
	public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
		return pipeline.hmget(key, fields).get();
	}

	@Override
	public Long hincrBy(final byte[] key, final byte[] field, final long value) {
		return pipeline.hincrBy(key, field, value).get();
	}

	@Override
	public Boolean hexists(final byte[] key, final byte[] field) {
		return pipeline.hexists(key, field).get();
	}

	@Override
	public Long hdel(final byte[] key, final byte[]... fields) {
		return pipeline.hdel(key, fields).get();
	}

	@Override
	public Long hlen(final byte[] key) {
		return pipeline.hlen(key).get();
	}

	@Override
	public Set<byte[]> hkeys(final byte[] key) {
		return pipeline.hkeys(key).get();
	}

	@Override
	public List<byte[]> hvals(final byte[] key) {
		return pipeline.hvals(key).get();
	}

	@Override
	public Map<byte[], byte[]> hgetAll(final byte[] key) {
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
	public Long setnx(final String key, final String value) {
		return pipeline.setnx(key, value).get();
	}

	@Override
	public Long setnx(final byte[] key, final byte[] value) {
		return pipeline.setnx(key, value).get();
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
	public Long decrBy(final String key, final long integer) {
		return pipeline.decrBy(key, integer).get();
	}

	@Override
	public Long decr(final String key) {
		return pipeline.decr(key).get();
	}

	@Override
	public Long expire(final String key, final int seconds) {
		return pipeline.expire(key, seconds).get();
	}

	@Override
	public Long expire(final byte[] key, final int seconds) {
		return pipeline.expire(key, seconds).get();
	}

	@Override
	public Long expireAt(final String key, final long unixTime) {
		return pipeline.expireAt(key, unixTime).get();
	}

	@Override
	public Long expireAt(final byte[] key, final long unixTime) {
		return pipeline.expireAt(key, unixTime).get();
	}

	@Override
	public String psetex(final String key, final int milliseconds, final String value) {
		return pipeline.psetex(key, milliseconds, value).get();
	}

	@Override
	public String set(final String key, final String value, final String nxxx) {
		return pipeline.set(key, value, nxxx).get();
	}

	@Override
	public String set(final String key, final String value, final String nxxx, final String expx, final int time) {
		return pipeline.set(key, value, nxxx, expx, time).get();
	}

	@Override
	public String migrate(final String host, final int port, final String key, final int destinationDb, final int timeout) {
		return pipeline.migrate(host, port, key, destinationDb, timeout).get();
	}

	@Override
	public String psetex(final byte[] key, final int milliseconds, final byte[] value) {
		return pipeline.psetex(key, milliseconds, value).get();
	}

	@Override
	public Long ttl(final String key) {
		return pipeline.ttl(key).get();
	}

	@Override
	public Long ttl(final byte[] key) {
		return pipeline.ttl(key).get();
	}

	@Override
	public Long pttl(final String key) {
		return pipeline.pttl(key).get();
	}

	@Override
	public Long pttl(final byte[] key) {
		return pipeline.pttl(key).get();
	}

	@Override
	public Long pexpire(final String key, final int milliseconds) {
		return pipeline.pexpire(key, milliseconds).get();
	}

	@Override
	public Long pexpire(final String key, final long milliseconds) {
		return pipeline.pexpire(key, milliseconds).get();
	}

	@Override
	public Long pexpire(final byte[] key, final int milliseconds) {
		return pipeline.pexpire(key, milliseconds).get();
	}

	@Override
	public Long pexpireAt(final String key, final long millisecondsTimestamp) {
		return pipeline.pexpireAt(key, millisecondsTimestamp).get();
	}

	@Override
	public Long pexpireAt(final byte[] key, final long millisecondsTimestamp) {
		return pipeline.pexpireAt(key, millisecondsTimestamp).get();
	}

	@Override
	public Boolean exists(final String key) {
		return pipeline.exists(key).get();
	}

	@Override
	public Boolean exists(final byte[] key) {
		return pipeline.exists(key).get();
	}

	@Override
	public Long incr(final String key) {
		return pipeline.incr(key).get();
	}

	@Override
	public Long incrBy(final String key, final long integer) {
		return pipeline.incrBy(key, integer).get();
	}

	@Override
	public String setex(final String key, final int seconds, final String value) {
		return pipeline.setex(key, seconds, value).get();
	}

	@Override
	public String setex(final byte[] key, final int seconds, final byte[] value) {
		return pipeline.setex(key, seconds, value).get();
	}

	@Override
	public Long del(final String... keys) {
		return pipeline.del(keys).get();
	}

	@Override
	public Long del(final String key) {
		return pipeline.del(key).get();
	}

	@Override
	public Long del(final byte[] key) {
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
	public Long hsetnx(final byte[] key, final byte[] field, final byte[] value) {
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
	public Long hincrBy(final String key, final String field, final long value) {
		return pipeline.hincrBy(key, field, value).get();
	}

	@Override
	public Double hincrByFloat(final String key, final String field, final double value) {
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
	public Long lpush(final String key, final String... strings) {
		return pipeline.lpush(key, strings).get();
	}

	@Override
	public Long lpush(final byte[] key, final byte[]... strings) {
		return pipeline.lpush(key, strings).get();
	}

	@Override
	public Long sadd(final String key, final String... members) {
		return pipeline.sadd(key, members).get();
	}

	@Override
	public Long sadd(final byte[] key, final byte[]... members) {
		return pipeline.sadd(key, members).get();
	}

	@Override
	public Long scard(final String key) {
		return pipeline.scard(key).get();
	}

	@Override
	public Long scard(final byte[] key) {
		return pipeline.scard(key).get();
	}

	@Override
	public Set<String> sdiff(final String... keys) {
		return pipeline.sdiff(keys).get();
	}

	@Override
	public Set<byte[]> sdiff(final byte[]... keys) {
		return pipeline.sdiff(keys).get();
	}

	@Override
	public Long sdiffstore(final String dstKey, final String... keys) {
		return pipeline.sdiffstore(dstKey, keys).get();
	}

	@Override
	public Long sdiffstore(final byte[] dstKey, final byte[]... keys) {
		return pipeline.sdiffstore(dstKey, keys).get();
	}

	@Override
	public Set<String> sinter(final String... keys) {
		return pipeline.sinter(keys).get();
	}

	@Override
	public Set<byte[]> sinter(final byte[]... keys) {
		return pipeline.sinter(keys).get();
	}

	@Override
	public Long sinterstore(final String dstKey, final String... keys) {
		return pipeline.sinterstore(dstKey, keys).get();
	}

	@Override
	public Long sinterstore(final byte[] dstKey, final byte[]... keys) {
		return pipeline.sinterstore(dstKey, keys).get();
	}

	@Override
	public Boolean sismember(final String key, final String member) {
		return pipeline.sismember(key, member).get();
	}

	@Override
	public Boolean sismember(final byte[] key, final byte[] member) {
		return pipeline.sismember(key, member).get();
	}

	@Override
	public Set<String> smembers(final String key) {
		return pipeline.smembers(key).get();
	}

	@Override
	public Set<byte[]> smembers(final byte[] key) {
		return pipeline.smembers(key).get();
	}

	@Override
	public Long smove(final String srcKey, final String dstKey, final String member) {
		return pipeline.smove(srcKey, dstKey, member).get();
	}

	@Override
	public Long smove(final byte[] srcKey, final byte[] dstKey, final byte[] member) {
		return pipeline.smove(srcKey, dstKey, member).get();
	}

	@Override
	public String spop(final String key) {
		return pipeline.spop(key).get();
	}

	@Override
	public byte[] spop(final byte[] key) {
		return pipeline.spop(key).get();
	}

	@Override
	public String srandmember(final String key) {
		return pipeline.srandmember(key).get();
	}

	@Override
	public byte[] srandmember(final byte[] key) {
		return pipeline.srandmember(key).get();
	}

	@Override
	public Long srem(final String key, final String... members) {
		return pipeline.srem(key, members).get();
	}

	@Override
	public Long srem(final byte[] key, final byte[]... members) {
		return pipeline.srem(key, members).get();
	}

	@Override
	public Set<String> sunion(final String... keys) {
		return pipeline.sunion(keys).get();
	}

	@Override
	public Set<byte[]> sunion(final byte[]... keys) {
		return pipeline.sunion(keys).get();
	}

	@Override
	public Long sunionstore(final String dstKey, final String... keys) {
		return pipeline.sunionstore(dstKey, keys).get();
	}

	@Override
	public Long sunionstore(final byte[] dstKey, final byte[]... keys) {
		return pipeline.sunionstore(dstKey, keys).get();
	}

	@Override
	public String lpop(final String key) {
		return pipeline.lpop(key).get();
	}

	@Override
	public byte[] lpop(final byte[] key) {
		return pipeline.lpop(key).get();
	}

	@Override
	public Long llen(final String key) {
		return pipeline.llen(key).get();
	}

	@Override
	public Long llen(final byte[] key) {
		return pipeline.llen(key).get();
	}

	@Override
	public List<String> lrange(final String key, final long start, final long end) {
		return pipeline.lrange(key, start, end).get();
	}

	@Override
	public List<byte[]> lrange(final byte[] key, final long start, final long end) {
		return pipeline.lrange(key, start, end).get();
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
