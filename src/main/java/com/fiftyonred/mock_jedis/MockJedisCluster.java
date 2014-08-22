package com.fiftyonred.mock_jedis;

import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockJedisCluster extends JedisCluster {
	private MockJedis client = null;

	public MockJedisCluster(final Set<HostAndPort> nodes, final int timeout) {
		super(nodes, timeout);
		this.client = new MockJedis("someunknownhost");
	}

	public MockJedisCluster(final Set<HostAndPort> nodes) {
		super(nodes);
		this.client = new MockJedis("someunknownhost");
	}

	public MockJedisCluster(final Set<HostAndPort> jedisClusterNode, final int timeout, final int maxRedirections) {
		super(jedisClusterNode, timeout, maxRedirections);
		this.client = new MockJedis("someunknownhost");
	}

	@Override
	public String set(final String key, final String value) {
		return client.set(key, value);
	}

	@Override
	public String set(final String key, final String value, final String nxxx, final String expx, final long time) {
		return client.set(key, value, nxxx, expx, time);
	}

	@Override
	public String get(final String key) {
		return client.get(key);
	}

	@Override
	public Boolean exists(final String key) {
		return client.exists(key);
	}

	@Override
	public Long persist(final String key) {
		return client.persist(key);
	}

	@Override
	public String type(final String key) {
		return client.type(key);
	}

	@Override
	public Long expire(final String key, final int seconds) {
		return client.expire(key, seconds);
	}

	@Override
	public Long expireAt(final String key, final long unixTime) {
		return client.expireAt(key, unixTime);
	}

	@Override
	public Long ttl(final String key) {
		return client.ttl(key);
	}

	@Override
	public Boolean setbit(final String key, final long offset, final boolean value) {
		return client.setbit(key, offset, value);
	}

	@Override
	public Boolean setbit(final String key, final long offset, final String value) {
		return client.setbit(key, offset, value);
	}

	@Override
	public Boolean getbit(final String key, final long offset) {
		return client.getbit(key, offset);
	}

	@Override
	public Long setrange(final String key, final long offset, final String value) {
		return client.setrange(key, offset, value);
	}

	@Override
	public String getrange(final String key, final long startOffset, final long endOffset) {
		return client.getrange(key, startOffset, endOffset);
	}

	@Override
	public String getSet(final String key, final String value) {
		return client.getSet(key, value);
	}

	@Override
	public Long setnx(final String key, final String value) {
		return client.setnx(key, value);
	}

	@Override
	public String setex(final String key, final int seconds, final String value) {
		return client.setex(key, seconds, value);
	}

	@Override
	public Long decrBy(final String key, final long integer) {
		return client.decrBy(key, integer);
	}

	@Override
	public Long decr(final String key) {
		return client.decr(key);
	}

	@Override
	public Long incrBy(final String key, final long integer) {
		return client.incrBy(key, integer);
	}

	@Override
	public Long incr(final String key) {
		return client.incr(key);
	}

	@Override
	public Long append(final String key, final String value) {
		return client.append(key, value);
	}

	@Override
	public String substr(final String key, final int start, final int end) {
		return client.substr(key, start, end);
	}

	@Override
	public Long hset(final String key, final String field, final String value) {
		return client.hset(key, field, value);
	}

	@Override
	public String hget(final String key, final String field) {
		return client.hget(key, field);
	}

	@Override
	public Long hsetnx(final String key, final String field, final String value) {
		return client.hsetnx(key, field, value);
	}

	@Override
	public String hmset(final String key, final Map<String, String> hash) {
		return client.hmset(key, hash);
	}

	@Override
	public List<String> hmget(final String key, final String... fields) {
		return client.hmget(key, fields);
	}

	@Override
	public Long hincrBy(final String key, final String field, final long value) {
		return client.hincrBy(key, field, value);
	}

	@Override
	public Boolean hexists(final String key, final String field) {
		return client.hexists(key, field);
	}

	@Override
	public Long hdel(final String key, final String... field) {
		return client.hdel(key, field);
	}

	@Override
	public Long hlen(final String key) {
		return client.hlen(key);
	}

	@Override
	public Set<String> hkeys(final String key) {
		return client.hkeys(key);
	}

	@Override
	public List<String> hvals(final String key) {
		return client.hvals(key);
	}

	@Override
	public Map<String, String> hgetAll(final String key) {
		return client.hgetAll(key);
	}

	@Override
	public Long rpush(final String key, final String... string) {
		return client.rpush(key, string);
	}

	@Override
	public Long lpush(final String key, final String... string) {
		return client.lpush(key, string);
	}

	@Override
	public Long llen(final String key) {
		return client.llen(key);
	}

	@Override
	public List<String> lrange(final String key, final long start, final long end) {
		return client.lrange(key, start, end);
	}

	@Override
	public String ltrim(final String key, final long start, final long end) {
		return client.ltrim(key, start, end);
	}

	@Override
	public String lindex(final String key, final long index) {
		return client.lindex(key, index);
	}

	@Override
	public String lset(final String key, final long index, final String value) {
		return client.lset(key, index, value);
	}

	@Override
	public Long lrem(final String key, final long count, final String value) {
		return client.lrem(key, count, value);
	}

	@Override
	public String lpop(final String key) {
		return client.lpop(key);
	}

	@Override
	public String rpop(final String key) {
		return client.rpop(key);
	}

	@Override
	public Long sadd(final String key, final String... member) {
		return client.sadd(key, member);
	}

	@Override
	public Set<String> smembers(final String key) {
		return client.smembers(key);
	}

	@Override
	public Long srem(final String key, final String... member) {
		return client.srem(key, member);
	}

	@Override
	public String spop(final String key) {
		return client.spop(key);
	}

	@Override
	public Long scard(final String key) {
		return client.scard(key);
	}

	@Override
	public Boolean sismember(final String key, final String member) {
		return client.sismember(key, member);
	}

	@Override
	public String srandmember(final String key) {
		return client.srandmember(key);
	}

	@Override
	public Long strlen(final String key) {
		return client.strlen(key);
	}

	@Override
	public Long zadd(final String key, final double score, final String member) {
		return client.zadd(key, score, member);
	}

	@Override
	public Long zadd(final String key, final Map<String, Double> scoreMembers) {
		return client.zadd(key, scoreMembers);
	}

	@Override
	public Set<String> zrange(final String key, final long start, final long end) {
		return client.zrange(key, start, end);
	}

	@Override
	public Long zrem(final String key, final String... member) {
		return client.zrem(key, member);
	}

	@Override
	public Double zincrby(final String key, final double score, final String member) {
		return client.zincrby(key, score, member);
	}

	@Override
	public Long zrank(final String key, final String member) {
		return client.zrank(key, member);
	}

	@Override
	public Long zrevrank(final String key, final String member) {
		return client.zrevrank(key, member);
	}

	@Override
	public Set<String> zrevrange(final String key, final long start, final long end) {
		return client.zrevrange(key, start, end);
	}

	@Override
	public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
		return client.zrangeWithScores(key, start, end);
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
		return client.zrevrangeWithScores(key, start, end);
	}

	@Override
	public Long zcard(final String key) {
		return client.zcard(key);
	}

	@Override
	public Double zscore(final String key, final String member) {
		return client.zscore(key, member);
	}

	@Override
	public List<String> sort(final String key) {
		return client.sort(key);
	}

	@Override
	public List<String> sort(final String key, final SortingParams sortingParameters) {
		return client.sort(key, sortingParameters);
	}

	@Override
	public Long zcount(final String key, final double min, final double max) {
		return client.zcount(key, min, max);
	}

	@Override
	public Long zcount(final String key, final String min, final String max) {
		return client.zcount(key, min, max);
	}

	@Override
	public Set<String> zrangeByScore(final String key, final double min, final double max) {
		return client.zrangeByScore(key, min, max);
	}

	@Override
	public Set<String> zrangeByScore(final String key, final String min, final String max) {
		return client.zrangeByScore(key, min, max);
	}

	@Override
	public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
		return client.zrevrangeByScore(key, max, min);
	}

	@Override
	public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
		return client.zrangeByScore(key, min, max, offset, count);
	}

	@Override
	public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
		return client.zrevrangeByScore(key, max, min);
	}

	@Override
	public Set<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
		return client.zrangeByScore(key, min, max, offset, count);
	}

	@Override
	public Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
		return client.zrevrangeByScore(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
		return client.zrangeByScoreWithScores(key, min, max);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
		return client.zrevrangeByScoreWithScores(key, max, min);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
		return client.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	@Override
	public Set<String> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
		return client.zrevrangeByScore(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
		return client.zrangeByScoreWithScores(key, min, max);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
		return client.zrevrangeByScoreWithScores(key, max, min);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
		return client.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
		return client.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count) {
		return client.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	@Override
	public Long zremrangeByRank(final String key, final long start, final long end) {
		return client.zremrangeByRank(key, start, end);
	}

	@Override
	public Long zremrangeByScore(final String key, final double start, final double end) {
		return client.zremrangeByScore(key, start, end);
	}

	@Override
	public Long zremrangeByScore(final String key, final String start, final String end) {
		return client.zremrangeByScore(key, start, end);
	}

	@Override
	public Long linsert(final String key, final BinaryClient.LIST_POSITION where, final String pivot, final String value) {
		return client.linsert(key, where, pivot, value);
	}

	@Override
	public Long lpushx(final String key, final String... string) {
		return client.lpushx(key, string);
	}

	@Override
	public Long rpushx(final String key, final String... string) {
		return client.rpushx(key, string);
	}

	@Override
	public List<String> blpop(final String arg) {
		return client.blpop(arg);
	}

	@Override
	public List<String> brpop(final String arg) {
		return client.brpop(arg);
	}

	@Override
	public Long del(final String key) {
		return client.del(key);
	}

	@Override
	public String echo(final String string) {
		return client.echo(string);
	}

	@Override
	public Long move(final String key, final int dbIndex) {
		return client.move(key, dbIndex);
	}

	@Override
	public Long bitcount(final String key) {
		return client.bitcount(key);
	}

	@Override
	public Long bitcount(final String key, final long start, final long end) {
		return client.bitcount(key, start, end);
	}

	@Override
	public String ping() {
		return client.ping();
	}

	@Override
	public String quit() {
		return client.quit();
	}

	@Override
	public String flushDB() {
		return client.flushDB();
	}

	@Override
	public Long dbSize() {
		return client.dbSize();
	}

	@Override
	public String select(final int index) {
		return client.select(index);
	}

	@Override
	public String flushAll() {
		return client.flushAll();
	}

	@Override
	public String auth(final String password) {
		return client.auth(password);
	}

	@Override
	public String save() {
		return client.save();
	}

	@Override
	public String bgsave() {
		return client.bgsave();
	}

	@Override
	public String bgrewriteaof() {
		return client.bgrewriteaof();
	}

	@Override
	public Long lastsave() {
		return client.lastsave();
	}

	@Override
	public String shutdown() {
		return client.shutdown();
	}

	@Override
	public String info() {
		return client.info();
	}

	@Override
	public String info(final String section) {
		return client.info(section);
	}

	@Override
	public String slaveof(final String host, final int port) {
		return client.slaveof(host, port);
	}

	@Override
	public String slaveofNoOne() {
		return client.slaveofNoOne();
	}

	@Override
	public Long getDB() {
		return client.getDB();
	}

	@Override
	public String debug(final DebugParams params) {
		return client.debug(params);
	}

	@Override
	public String configResetStat() {
		return client.configResetStat();
	}

	@Override
	public Long waitReplicas(final int replicas, final long timeout) {
		return client.waitReplicas(replicas, timeout);
	}

	@Override
	public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
		return client.hscan(key, cursor);
	}

	@Override
	public ScanResult<String> sscan(final String key, final String cursor) {
		return client.sscan(key, cursor);
	}

	@Override
	public ScanResult<Tuple> zscan(final String key, final String cursor) {
		return client.zscan(key, cursor);
	}

	@Override
	public Long pfadd(final String key, final String... elements) {
		return client.pfadd(key, elements);
	}

	@Override
	public long pfcount(final String key) {
		return client.pfcount(key);
	}

	public void setClient(final MockJedis client) {
		this.client = client;
	}
}
