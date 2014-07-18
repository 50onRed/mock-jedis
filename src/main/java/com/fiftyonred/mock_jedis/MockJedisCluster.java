package com.fiftyonred.mock_jedis;

import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockJedisCluster extends JedisCluster {
	private MockJedis client = null;

	public MockJedisCluster(Set<HostAndPort> nodes, int timeout) {
		super(nodes, timeout);
		this.client = new MockJedis("someunknownhost");
	}

	public MockJedisCluster(Set<HostAndPort> nodes) {
		super(nodes);
		this.client = new MockJedis("someunknownhost");
	}

	public MockJedisCluster(Set<HostAndPort> jedisClusterNode, int timeout, int maxRedirections) {
		super(jedisClusterNode, timeout, maxRedirections);
		this.client = new MockJedis("someunknownhost");
	}

	@Override
	public String set(String key, String value) {
		return client.set(key, value);
	}

	@Override
	public String set(String key, String value, String nxxx, String expx, long time) {
		return client.set(key, value, nxxx, expx, time);
	}

	@Override
	public String get(String key) {
		return client.get(key);
	}

	@Override
	public Boolean exists(String key) {
		return client.exists(key);
	}

	@Override
	public Long persist(String key) {
		return client.persist(key);
	}

	@Override
	public String type(String key) {
		return client.type(key);
	}

	@Override
	public Long expire(String key, int seconds) {
		return client.expire(key, seconds);
	}

	@Override
	public Long expireAt(String key, long unixTime) {
		return client.expireAt(key, unixTime);
	}

	@Override
	public Long ttl(String key) {
		return client.ttl(key);
	}

	@Override
	public Boolean setbit(String key, long offset, boolean value) {
		return client.setbit(key, offset, value);
	}

	@Override
	public Boolean setbit(String key, long offset, String value) {
		return client.setbit(key, offset, value);
	}

	@Override
	public Boolean getbit(String key, long offset) {
		return client.getbit(key, offset);
	}

	@Override
	public Long setrange(String key, long offset, String value) {
		return client.setrange(key, offset, value);
	}

	@Override
	public String getrange(String key, long startOffset, long endOffset) {
		return client.getrange(key, startOffset, endOffset);
	}

	@Override
	public String getSet(String key, String value) {
		return client.getSet(key, value);
	}

	@Override
	public Long setnx(String key, String value) {
		return client.setnx(key, value);
	}

	@Override
	public String setex(String key, int seconds, String value) {
		return client.setex(key, seconds, value);
	}

	@Override
	public Long decrBy(String key, long integer) {
		return client.decrBy(key, integer);
	}

	@Override
	public Long decr(String key) {
		return client.decr(key);
	}

	@Override
	public Long incrBy(String key, long integer) {
		return client.incrBy(key, integer);
	}

	@Override
	public Long incr(String key) {
		return client.incr(key);
	}

	@Override
	public Long append(String key, String value) {
		return client.append(key, value);
	}

	@Override
	public String substr(String key, int start, int end) {
		return client.substr(key, start, end);
	}

	@Override
	public Long hset(String key, String field, String value) {
		return client.hset(key, field, value);
	}

	@Override
	public String hget(String key, String field) {
		return client.hget(key, field);
	}

	@Override
	public Long hsetnx(String key, String field, String value) {
		return client.hsetnx(key, field, value);
	}

	@Override
	public String hmset(String key, Map<String, String> hash) {
		return client.hmset(key, hash);
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		return client.hmget(key, fields);
	}

	@Override
	public Long hincrBy(String key, String field, long value) {
		return client.hincrBy(key, field, value);
	}

	@Override
	public Boolean hexists(String key, String field) {
		return client.hexists(key, field);
	}

	@Override
	public Long hdel(String key, String... field) {
		return client.hdel(key, field);
	}

	@Override
	public Long hlen(String key) {
		return client.hlen(key);
	}

	@Override
	public Set<String> hkeys(String key) {
		return client.hkeys(key);
	}

	@Override
	public List<String> hvals(String key) {
		return client.hvals(key);
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		return client.hgetAll(key);
	}

	@Override
	public Long rpush(String key, String... string) {
		return client.rpush(key, string);
	}

	@Override
	public Long lpush(String key, String... string) {
		return client.lpush(key, string);
	}

	@Override
	public Long llen(String key) {
		return client.llen(key);
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		return client.lrange(key, start, end);
	}

	@Override
	public String ltrim(String key, long start, long end) {
		return client.ltrim(key, start, end);
	}

	@Override
	public String lindex(String key, long index) {
		return client.lindex(key, index);
	}

	@Override
	public String lset(String key, long index, String value) {
		return client.lset(key, index, value);
	}

	@Override
	public Long lrem(String key, long count, String value) {
		return client.lrem(key, count, value);
	}

	@Override
	public String lpop(String key) {
		return client.lpop(key);
	}

	@Override
	public String rpop(String key) {
		return client.rpop(key);
	}

	@Override
	public Long sadd(String key, String... member) {
		return client.sadd(key, member);
	}

	@Override
	public Set<String> smembers(String key) {
		return client.smembers(key);
	}

	@Override
	public Long srem(String key, String... member) {
		return client.srem(key, member);
	}

	@Override
	public String spop(String key) {
		return client.spop(key);
	}

	@Override
	public Long scard(String key) {
		return client.scard(key);
	}

	@Override
	public Boolean sismember(String key, String member) {
		return client.sismember(key, member);
	}

	@Override
	public String srandmember(String key) {
		return client.srandmember(key);
	}

	@Override
	public Long strlen(String key) {
		return client.strlen(key);
	}

	@Override
	public Long zadd(String key, double score, String member) {
		return client.zadd(key, score, member);
	}

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		return client.zadd(key, scoreMembers);
	}

	@Override
	public Set<String> zrange(String key, long start, long end) {
		return client.zrange(key, start, end);
	}

	@Override
	public Long zrem(String key, String... member) {
		return client.zrem(key, member);
	}

	@Override
	public Double zincrby(String key, double score, String member) {
		return client.zincrby(key, score, member);
	}

	@Override
	public Long zrank(String key, String member) {
		return client.zrank(key, member);
	}

	@Override
	public Long zrevrank(String key, String member) {
		return client.zrevrank(key, member);
	}

	@Override
	public Set<String> zrevrange(String key, long start, long end) {
		return client.zrevrange(key, start, end);
	}

	@Override
	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		return client.zrangeWithScores(key, start, end);
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		return client.zrevrangeWithScores(key, start, end);
	}

	@Override
	public Long zcard(String key) {
		return client.zcard(key);
	}

	@Override
	public Double zscore(String key, String member) {
		return client.zscore(key, member);
	}

	@Override
	public List<String> sort(String key) {
		return client.sort(key);
	}

	@Override
	public List<String> sort(String key, SortingParams sortingParameters) {
		return client.sort(key, sortingParameters);
	}

	@Override
	public Long zcount(String key, double min, double max) {
		return client.zcount(key, min, max);
	}

	@Override
	public Long zcount(String key, String min, String max) {
		return client.zcount(key, min, max);
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		return client.zrangeByScore(key, min, max);
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max) {
		return client.zrangeByScore(key, min, max);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		return client.zrevrangeByScore(key, max, min);
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		return client.zrangeByScore(key, min, max, offset, count);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min) {
		return client.zrevrangeByScore(key, max, min);
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
		return client.zrangeByScore(key, min, max, offset, count);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		return client.zrevrangeByScore(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		return client.zrangeByScoreWithScores(key, min, max);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		return client.zrevrangeByScoreWithScores(key, max, min);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		return client.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
		return client.zrevrangeByScore(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
		return client.zrangeByScoreWithScores(key, min, max);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
		return client.zrevrangeByScoreWithScores(key, max, min);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
		return client.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		return client.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
		return client.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	@Override
	public Long zremrangeByRank(String key, long start, long end) {
		return client.zremrangeByRank(key, start, end);
	}

	@Override
	public Long zremrangeByScore(String key, double start, double end) {
		return client.zremrangeByScore(key, start, end);
	}

	@Override
	public Long zremrangeByScore(String key, String start, String end) {
		return client.zremrangeByScore(key, start, end);
	}

	@Override
	public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
		return client.linsert(key, where, pivot, value);
	}

	@Override
	public Long lpushx(String key, String... string) {
		return client.lpushx(key, string);
	}

	@Override
	public Long rpushx(String key, String... string) {
		return client.rpushx(key, string);
	}

	@Override
	public List<String> blpop(String arg) {
		return client.blpop(arg);
	}

	@Override
	public List<String> brpop(String arg) {
		return client.brpop(arg);
	}

	@Override
	public Long del(String key) {
		return client.del(key);
	}

	@Override
	public String echo(String string) {
		return client.echo(string);
	}

	@Override
	public Long move(String key, int dbIndex) {
		return client.move(key, dbIndex);
	}

	@Override
	public Long bitcount(String key) {
		return client.bitcount(key);
	}

	@Override
	public Long bitcount(String key, long start, long end) {
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
	public String select(int index) {
		return client.select(index);
	}

	@Override
	public String flushAll() {
		return client.flushAll();
	}

	@Override
	public String auth(String password) {
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
	public String info(String section) {
		return client.info(section);
	}

	@Override
	public String slaveof(String host, int port) {
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
	public String debug(DebugParams params) {
		return client.debug(params);
	}

	@Override
	public String configResetStat() {
		return client.configResetStat();
	}

	@Override
	public Long waitReplicas(int replicas, long timeout) {
		return client.waitReplicas(replicas, timeout);
	}

	@Override
	public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
		return client.hscan(key, cursor);
	}

	@Override
	public ScanResult<String> sscan(String key, String cursor) {
		return client.sscan(key, cursor);
	}

	@Override
	public ScanResult<Tuple> zscan(String key, String cursor) {
		return client.zscan(key, cursor);
	}

	@Override
	public Long pfadd(String key, String... elements) {
		return client.pfadd(key, elements);
	}

	@Override
	public long pfcount(String key) {
		return client.pfcount(key);
	}

	public void setClient(final MockJedis client) {
		this.client = client;
	}
}
