package com.fiftyonred.mock_jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.*;
import redis.clients.util.Pool;
import redis.clients.util.Slowlog;

public class MockJedis extends Jedis {

	public static final String NOT_IMPLEMENTED = "Not implemented";

	private final MockPipeline pipeline;

	public MockJedis(final String host) {
		super(host);
		pipeline = new MockPipeline();
	}

	@Override
	public Long getDB() {
		return (long)pipeline.getCurrentDB();
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

	// ************************************************************************************
	// Unsupported operations
	// ************************************************************************************

	@Override
	public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String set(String key, String value, String nxxx, String expx, long time) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String substr(String key, int start, int end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long rpush(String key, String... strings) {
        	return pipeline.rpush(key, strings).get();
    	}

	@Override
	public String ltrim(String key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String lindex(String key, long index) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String lset(String key, long index, String value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long lrem(String key, long count, String value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String rpop(String key) {
        	return pipeline.rpop(key).get();
    	}

	@Override
	public String rpoplpush(String srckey, String dstkey) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> srandmember(String key, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zadd(String key, double score, String member) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<String> zrange(String key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zrem(String key, String... members) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Double zincrby(String key, double score, String member) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zrank(String key, String member) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zrevrank(String key, String member) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<String> zrevrange(String key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zcard(String key) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Double zscore(String key, String member) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String watch(String... keys) {
		return "test";
	}

	@Override
	public List<String> blpop(int timeout, String... keys) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> blpop(String... args) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> brpop(String... args) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> blpop(String arg) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> brpop(String arg) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> brpop(int timeout, String... keys) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zcount(String key, double min, double max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zcount(String key, String min, String max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zremrangeByRank(String key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zremrangeByScore(String key, double start, double end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zremrangeByScore(String key, String start, String end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zunionstore(String dstkey, String... sets) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zunionstore(String dstkey, ZParams params, String... sets) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zinterstore(String dstkey, String... sets) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zinterstore(String dstkey, ZParams params, String... sets) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long lpushx(String key, String... string) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long rpushx(String key, String... string) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String brpoplpush(String source, String destination, int timeout) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Boolean setbit(String key, long offset, boolean value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Boolean setbit(String key, long offset, String value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Boolean getbit(String key, long offset) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long setrange(String key, long offset, String value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String getrange(String key, long startOffset, long endOffset) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long bitpos(String key, boolean value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long bitpos(String key, boolean value, BitPosParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> configGet(String pattern) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String configSet(String parameter, String value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Object eval(String script, int keyCount, String... params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		super.subscribe(jedisPubSub, channels);
	}

	@Override
	public Long publish(String channel, String message) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		super.psubscribe(jedisPubSub, patterns);
	}

	@Override
	public Object eval(String script, List<String> keys, List<String> args) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Object eval(String script) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Object evalsha(String script) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Object evalsha(String sha1, List<String> keys, List<String> args) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Object evalsha(String sha1, int keyCount, String... params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Boolean scriptExists(String sha1) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<Boolean> scriptExists(String... sha1) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String scriptLoad(String script) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<Slowlog> slowlogGet() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<Slowlog> slowlogGet(long entries) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long objectRefcount(String string) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String objectEncoding(String string) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long objectIdletime(String string) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long bitcount(String key) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long bitcount(String key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long bitop(BitOP op, String destKey, String... srcKeys) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<Map<String, String>> sentinelMasters() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> sentinelGetMasterAddrByName(String masterName) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long sentinelReset(String pattern) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<Map<String, String>> sentinelSlaves(String masterName) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String sentinelFailover(String masterName) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String sentinelMonitor(String masterName, String ip, int port, int quorum) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String sentinelRemove(String masterName) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String sentinelSet(String masterName, Map<String, String> parameterMap) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clientKill(String client) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clientSetname(String name) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<String> scan(int cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<String> scan(int cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<Map.Entry<String, String>> hscan(String key, int cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<Map.Entry<String, String>> hscan(String key, int cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<String> sscan(String key, int cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<String> sscan(String key, int cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<Tuple> zscan(String key, int cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<Tuple> zscan(String key, int cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<String> scan(String cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<String> scan(String cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<String> sscan(String key, String cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<Tuple> zscan(String key, String cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterNodes() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterMeet(String ip, int port) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterAddSlots(int... slots) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterDelSlots(int... slots) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterInfo() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> clusterGetKeysInSlot(int slot, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterSetSlotNode(int slot, String nodeId) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterSetSlotMigrating(int slot, String nodeId) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterSetSlotImporting(int slot, String nodeId) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterSetSlotStable(int slot) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterForget(String nodeId) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterFlushSlots() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long clusterKeySlot(String key) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long clusterCountKeysInSlot(int slot) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterSaveConfig() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterReplicate(String nodeId) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> clusterSlaves(String nodeId) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clusterFailover() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String asking() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> pubsubChannels(String pattern) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long pubsubNumPat() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Map<String, String> pubsubNumSub(String... channels) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public void close() {
		super.close();
	}

	@Override
	public void setDataSource(Pool<Jedis> jedisPool) {
		super.setDataSource(jedisPool);
	}

	@Override
	public Long pfadd(String key, String... elements) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public long pfcount(String key) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public long pfcount(String... keys) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String pfmerge(String destkey, String... sourcekeys) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public byte[] substr(byte[] key, int start, int end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Double hincrByFloat(byte[] key, byte[] field, double value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long rpush(byte[] key, byte[]... strings) {
        	return pipeline.rpush(key, strings).get();
    	}

	@Override
	public String ltrim(byte[] key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public byte[] lindex(byte[] key, long index) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String lset(byte[] key, long index, byte[] value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long lrem(byte[] key, long count, byte[] value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public byte[] rpop(byte[] key) {
        	return pipeline.rpop(key).get();
    	}

	@Override
	public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<byte[]> srandmember(byte[] key, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zadd(byte[] key, double score, byte[] member) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<byte[]> zrange(byte[] key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zrem(byte[] key, byte[]... members) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Double zincrby(byte[] key, double score, byte[] member) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zrank(byte[] key, byte[] member) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zrevrank(byte[] key, byte[] member) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<byte[]> zrevrange(byte[] key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zcard(byte[] key) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Double zscore(byte[] key, byte[] member) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Transaction multi() {
		return new MockTransaction(pipeline);
	}

	@Override
	public List<Object> multi(TransactionBlock jedisTransaction) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	protected void checkIsInMulti() {
		super.checkIsInMulti();
	}

	@Override
	public void resetState() {
		super.resetState();
	}

	@Override
	public String watch(byte[]... keys) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String unwatch() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<byte[]> blpop(int timeout, byte[]... keys) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<byte[]> brpop(int timeout, byte[]... keys) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<byte[]> blpop(byte[] arg) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<byte[]> brpop(byte[] arg) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<byte[]> blpop(byte[]... args) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<byte[]> brpop(byte[]... args) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String auth(String password) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<Object> pipelined(PipelineBlock jedisPipeline) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zcount(byte[] key, double min, double max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zcount(byte[] key, byte[] min, byte[] max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zremrangeByRank(byte[] key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zremrangeByScore(byte[] key, double start, double end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zremrangeByScore(byte[] key, byte[] start, byte[] end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zunionstore(byte[] dstkey, byte[]... sets) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zinterstore(byte[] dstkey, byte[]... sets) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String save() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String bgsave() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String bgrewriteaof() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long lastsave() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String shutdown() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String info() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String info(String section) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public void monitor(JedisMonitor jedisMonitor) {
		super.monitor(jedisMonitor);
	}

	@Override
	public String slaveof(String host, int port) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String slaveofNoOne() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<byte[]> configGet(byte[] pattern) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String configResetStat() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public byte[] configSet(byte[] parameter, byte[] value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public boolean isConnected() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public void sync() {
		super.sync();
	}

	@Override
	public Long lpushx(byte[] key, byte[]... string) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long rpushx(byte[] key, byte[]... string) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot, byte[] value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String debug(DebugParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Client getClient() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Boolean setbit(byte[] key, long offset, boolean value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Boolean setbit(byte[] key, long offset, byte[] value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Boolean getbit(byte[] key, long offset) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long bitpos(byte[] key, boolean value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long bitpos(byte[] key, boolean value, BitPosParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long setrange(byte[] key, long offset, byte[] value) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public byte[] getrange(byte[] key, long startOffset, long endOffset) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long publish(byte[] channel, byte[] message) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
		super.subscribe(jedisPubSub, channels);
	}

	@Override
	public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
		super.psubscribe(jedisPubSub, patterns);
	}

	@Override
	public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Object eval(byte[] script, byte[] keyCount, byte[]... params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Object eval(byte[] script, int keyCount, byte[]... params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Object eval(byte[] script) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Object evalsha(byte[] sha1) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String scriptFlush() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<Long> scriptExists(byte[]... sha1) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public byte[] scriptLoad(byte[] script) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String scriptKill() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String slowlogReset() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long slowlogLen() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<byte[]> slowlogGetBinary() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<byte[]> slowlogGetBinary(long entries) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long objectRefcount(byte[] key) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public byte[] objectEncoding(byte[] key) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long objectIdletime(byte[] key) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long bitcount(byte[] key) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long bitcount(byte[] key, long start, long end) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long pexpire(byte[] key, long milliseconds) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String set(byte[] key, byte[] value, byte[] nxxx) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, int time) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clientKill(byte[] client) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clientGetname() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clientList() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String clientSetname(byte[] name) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public List<String> time() {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String migrate(byte[] host, int port, byte[] key, int destinationDb, int timeout) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long waitReplicas(int replicas, long timeout) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long pfadd(byte[] key, byte[]... elements) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public long pfcount(byte[] key) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public String pfmerge(byte[] destkey, byte[]... sourcekeys) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public Long pfcount(byte[]... keys) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<byte[]> scan(byte[] cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	@Override
	public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}
}
