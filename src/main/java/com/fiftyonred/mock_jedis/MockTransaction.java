package com.fiftyonred.mock_jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.Response;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

public class MockTransaction extends Transaction {
  private final MockPipeline mockPipeline;

  MockTransaction(MockPipeline mockPipeline) {
    this.mockPipeline = mockPipeline;
  }

  @Override public List<Object> exec() {
    return mockPipeline.exec().get();
  }

  @Override public String discard() {
    return mockPipeline.discard().get();
  }

  @Override public Response<Long> append(String key, String value) {
    return mockPipeline.append(key, value);
  }

  @Override public Response<Long> append(byte[] key, byte[] value) {
    return mockPipeline.append(key, value);
  }

  @Override public Response<List<String>> blpop(String key) {
    return mockPipeline.blpop(key);
  }

  @Override public Response<List<String>> brpop(String key) {
    return mockPipeline.brpop(key);
  }

  @Override public Response<List<byte[]>> blpop(byte[] key) {
    return mockPipeline.blpop(key);
  }

  @Override public Response<List<byte[]>> brpop(byte[] key) {
    return mockPipeline.brpop(key);
  }

  @Override public Response<Long> decr(String key) {
    return mockPipeline.decr(key);
  }

  @Override public Response<Long> decr(byte[] key) {
    return mockPipeline.decr(key);
  }

  @Override public Response<Long> decrBy(String key, long integer) {
    return mockPipeline.decrBy(key, integer);
  }

  @Override public Response<Long> decrBy(byte[] key, long integer) {
    return mockPipeline.decrBy(key, integer);
  }

  @Override public Response<Long> del(String key) {
    return mockPipeline.del(key);
  }

  @Override public Response<Long> del(byte[] key) {
    return mockPipeline.del(key);
  }

  @Override public Response<String> echo(String string) {
    return mockPipeline.echo(string);
  }

  @Override public Response<byte[]> echo(byte[] string) {
    return mockPipeline.echo(string);
  }

  @Override public Response<Boolean> exists(String key) {
    return mockPipeline.exists(key);
  }

  @Override public Response<Boolean> exists(byte[] key) {
    return mockPipeline.exists(key);
  }

  @Override public Response<Long> expire(String key, int seconds) {
    return mockPipeline.expire(key, seconds);
  }

  @Override public Response<Long> expire(byte[] key, int seconds) {
    return mockPipeline.expire(key, seconds);
  }

  @Override public Response<Long> expireAt(String key, long unixTime) {
    return mockPipeline.expireAt(key, unixTime);
  }

  @Override public Response<Long> expireAt(byte[] key, long unixTime) {
    return mockPipeline.expireAt(key, unixTime);
  }

  @Override public Response<String> get(String key) {
    return mockPipeline.get(key);
  }

  @Override public Response<byte[]> get(byte[] key) {
    return mockPipeline.get(key);
  }

  @Override public Response<Boolean> getbit(String key, long offset) {
    return mockPipeline.getbit(key, offset);
  }

  @Override public Response<Boolean> getbit(byte[] key, long offset) {
    return mockPipeline.getbit(key, offset);
  }

  @Override public Response<Long> bitpos(String key, boolean value) {
    return mockPipeline.bitpos(key, value);
  }

  @Override public Response<Long> bitpos(String key, boolean value, BitPosParams params) {
    return mockPipeline.bitpos(key, value, params);
  }

  @Override public Response<Long> bitpos(byte[] key, boolean value) {
    return mockPipeline.bitpos(key, value);
  }

  @Override public Response<Long> bitpos(byte[] key, boolean value, BitPosParams params) {
    return mockPipeline.bitpos(key, value, params);
  }

  @Override public Response<String> getrange(String key, long startOffset, long endOffset) {
    return mockPipeline.getrange(key, startOffset, endOffset);
  }

  @Override public Response<String> getSet(String key, String value) {
    return mockPipeline.getSet(key, value);
  }

  @Override public Response<byte[]> getSet(byte[] key, byte[] value) {
    return mockPipeline.getSet(key, value);
  }

  @Override public Response<Long> getrange(byte[] key, long startOffset, long endOffset) {
    return mockPipeline.getrange(key, startOffset, endOffset);
  }

  @Override public Response<Long> hdel(String key, String... field) {
    return mockPipeline.hdel(key, field);
  }

  @Override public Response<Long> hdel(byte[] key, byte[]... field) {
    return mockPipeline.hdel(key, field);
  }

  @Override public Response<Boolean> hexists(String key, String field) {
    return mockPipeline.hexists(key, field);
  }

  @Override public Response<Boolean> hexists(byte[] key, byte[] field) {
    return mockPipeline.hexists(key, field);
  }

  @Override public Response<String> hget(String key, String field) {
    return mockPipeline.hget(key, field);
  }

  @Override public Response<byte[]> hget(byte[] key, byte[] field) {
    return mockPipeline.hget(key, field);
  }

  @Override public Response<Map<String, String>> hgetAll(String key) {
    return mockPipeline.hgetAll(key);
  }

  @Override public Response<Map<byte[], byte[]>> hgetAll(byte[] key) {
    return mockPipeline.hgetAll(key);
  }

  @Override public Response<Long> hincrBy(String key, String field, long value) {
    return mockPipeline.hincrBy(key, field, value);
  }

  @Override public Response<Long> hincrBy(byte[] key, byte[] field, long value) {
    return mockPipeline.hincrBy(key, field, value);
  }

  @Override public Response<Set<String>> hkeys(String key) {
    return mockPipeline.hkeys(key);
  }

  @Override public Response<Set<byte[]>> hkeys(byte[] key) {
    return mockPipeline.hkeys(key);
  }

  @Override public Response<Long> hlen(String key) {
    return mockPipeline.hlen(key);
  }

  @Override public Response<Long> hlen(byte[] key) {
    return mockPipeline.hlen(key);
  }

  @Override public Response<List<String>> hmget(String key, String... fields) {
    return mockPipeline.hmget(key, fields);
  }

  @Override public Response<List<byte[]>> hmget(byte[] key, byte[]... fields) {
    return mockPipeline.hmget(key, fields);
  }

  @Override public Response<String> hmset(String key, Map<String, String> hash) {
    return mockPipeline.hmset(key, hash);
  }

  @Override public Response<String> hmset(byte[] key, Map<byte[], byte[]> hash) {
    return mockPipeline.hmset(key, hash);
  }

  @Override public Response<Long> hset(String key, String field, String value) {
    return mockPipeline.hset(key, field, value);
  }

  @Override public Response<Long> hset(byte[] key, byte[] field, byte[] value) {
    return mockPipeline.hset(key, field, value);
  }

  @Override public Response<Long> hsetnx(String key, String field, String value) {
    return mockPipeline.hsetnx(key, field, value);
  }

  @Override public Response<Long> hsetnx(byte[] key, byte[] field, byte[] value) {
    return mockPipeline.hsetnx(key, field, value);
  }

  @Override public Response<List<String>> hvals(String key) {
    return mockPipeline.hvals(key);
  }

  @Override public Response<List<byte[]>> hvals(byte[] key) {
    return mockPipeline.hvals(key);
  }

  @Override public Response<Long> incr(String key) {
    return mockPipeline.incr(key);
  }

  @Override public Response<Long> incr(byte[] key) {
    return mockPipeline.incr(key);
  }

  @Override public Response<Long> incrBy(String key, long integer) {
    return mockPipeline.incrBy(key, integer);
  }

  @Override public Response<Long> incrBy(byte[] key, long integer) {
    return mockPipeline.incrBy(key, integer);
  }

  @Override public Response<String> lindex(String key, long index) {
    return mockPipeline.lindex(key, index);
  }

  @Override public Response<byte[]> lindex(byte[] key, long index) {
    return mockPipeline.lindex(key, index);
  }

  @Override
  public Response<Long> linsert(String key, BinaryClient.LIST_POSITION where, String pivot,
      String value) {
    return mockPipeline.linsert(key, where, pivot, value);
  }

  @Override
  public Response<Long> linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot,
      byte[] value) {
    return mockPipeline.linsert(key, where, pivot, value);
  }

  @Override public Response<Long> llen(String key) {
    return mockPipeline.llen(key);
  }

  @Override public Response<Long> llen(byte[] key) {
    return mockPipeline.llen(key);
  }

  @Override public Response<String> lpop(String key) {
    return mockPipeline.lpop(key);
  }

  @Override public Response<byte[]> lpop(byte[] key) {
    return mockPipeline.lpop(key);
  }

  @Override public Response<Long> lpush(String key, String... string) {
    return mockPipeline.lpush(key, string);
  }

  @Override public Response<Long> lpush(byte[] key, byte[]... string) {
    return mockPipeline.lpush(key, string);
  }

  @Override public Response<Long> lpushx(String key, String... string) {
    return mockPipeline.lpushx(key, string);
  }

  @Override public Response<Long> lpushx(byte[] key, byte[]... bytes) {
    return mockPipeline.lpushx(key, bytes);
  }

  @Override public Response<List<String>> lrange(String key, long start, long end) {
    return mockPipeline.lrange(key, start, end);
  }

  @Override public Response<List<byte[]>> lrange(byte[] key, long start, long end) {
    return mockPipeline.lrange(key, start, end);
  }

  @Override public Response<Long> lrem(String key, long count, String value) {
    return mockPipeline.lrem(key, count, value);
  }

  @Override public Response<Long> lrem(byte[] key, long count, byte[] value) {
    return mockPipeline.lrem(key, count, value);
  }

  @Override public Response<String> lset(String key, long index, String value) {
    return mockPipeline.lset(key, index, value);
  }

  @Override public Response<String> lset(byte[] key, long index, byte[] value) {
    return mockPipeline.lset(key, index, value);
  }

  @Override public Response<String> ltrim(String key, long start, long end) {
    return mockPipeline.ltrim(key, start, end);
  }

  @Override public Response<String> ltrim(byte[] key, long start, long end) {
    return mockPipeline.ltrim(key, start, end);
  }

  @Override public Response<Long> move(String key, int dbIndex) {
    return mockPipeline.move(key, dbIndex);
  }

  @Override public Response<Long> move(byte[] key, int dbIndex) {
    return mockPipeline.move(key, dbIndex);
  }

  @Override public Response<Long> persist(String key) {
    return mockPipeline.persist(key);
  }

  @Override public Response<Long> persist(byte[] key) {
    return mockPipeline.persist(key);
  }

  @Override public Response<String> rpop(String key) {
    return mockPipeline.rpop(key);
  }

  @Override public Response<byte[]> rpop(byte[] key) {
    return mockPipeline.rpop(key);
  }

  @Override public Response<Long> rpush(String key, String... string) {
    return mockPipeline.rpush(key, string);
  }

  @Override public Response<Long> rpush(byte[] key, byte[]... string) {
    return mockPipeline.rpush(key, string);
  }

  @Override public Response<Long> rpushx(String key, String... string) {
    return mockPipeline.rpushx(key, string);
  }

  @Override public Response<Long> rpushx(byte[] key, byte[]... string) {
    return mockPipeline.rpushx(key, string);
  }

  @Override public Response<Long> sadd(String key, String... member) {
    return mockPipeline.sadd(key, member);
  }

  @Override public Response<Long> sadd(byte[] key, byte[]... member) {
    return mockPipeline.sadd(key, member);
  }

  @Override public Response<Long> scard(String key) {
    return mockPipeline.scard(key);
  }

  @Override public Response<Long> scard(byte[] key) {
    return mockPipeline.scard(key);
  }

  @Override public Response<String> set(String key, String value) {
    return mockPipeline.set(key, value);
  }

  @Override public Response<String> set(byte[] key, byte[] value) {
    return mockPipeline.set(key, value);
  }

  @Override public Response<Boolean> setbit(String key, long offset, boolean value) {
    return mockPipeline.setbit(key, offset, value);
  }

  @Override public Response<Boolean> setbit(byte[] key, long offset, byte[] value) {
    return mockPipeline.setbit(key, offset, value);
  }

  @Override public Response<String> setex(String key, int seconds, String value) {
    return mockPipeline.setex(key, seconds, value);
  }

  @Override public Response<String> setex(byte[] key, int seconds, byte[] value) {
    return mockPipeline.setex(key, seconds, value);
  }

  @Override public Response<Long> setnx(String key, String value) {
    return mockPipeline.setnx(key, value);
  }

  @Override public Response<Long> setnx(byte[] key, byte[] value) {
    return mockPipeline.setnx(key, value);
  }

  @Override public Response<Long> setrange(String key, long offset, String value) {
    return mockPipeline.setrange(key, offset, value);
  }

  @Override public Response<Long> setrange(byte[] key, long offset, byte[] value) {
    return mockPipeline.setrange(key, offset, value);
  }

  @Override public Response<Boolean> sismember(String key, String member) {
    return mockPipeline.sismember(key, member);
  }

  @Override public Response<Boolean> sismember(byte[] key, byte[] member) {
    return mockPipeline.sismember(key, member);
  }

  @Override public Response<Set<String>> smembers(String key) {
    return mockPipeline.smembers(key);
  }

  @Override public Response<Set<byte[]>> smembers(byte[] key) {
    return mockPipeline.smembers(key);
  }

  @Override public Response<List<String>> sort(String key) {
    return mockPipeline.sort(key);
  }

  @Override public Response<List<byte[]>> sort(byte[] key) {
    return mockPipeline.sort(key);
  }

  @Override public Response<List<String>> sort(String key, SortingParams sortingParameters) {
    return mockPipeline.sort(key, sortingParameters);
  }

  @Override public Response<List<byte[]>> sort(byte[] key, SortingParams sortingParameters) {
    return mockPipeline.sort(key, sortingParameters);
  }

  @Override public Response<String> spop(String key) {
    return mockPipeline.spop(key);
  }

  @Override public Response<byte[]> spop(byte[] key) {
    return mockPipeline.spop(key);
  }

  @Override public Response<String> srandmember(String key) {
    return mockPipeline.srandmember(key);
  }

  @Override public Response<List<String>> srandmember(String key, int count) {
    return mockPipeline.srandmember(key, count);
  }

  @Override public Response<byte[]> srandmember(byte[] key) {
    return mockPipeline.srandmember(key);
  }

  @Override public Response<List<byte[]>> srandmember(byte[] key, int count) {
    return mockPipeline.srandmember(key, count);
  }

  @Override public Response<Long> srem(String key, String... member) {
    return mockPipeline.srem(key, member);
  }

  @Override public Response<Long> srem(byte[] key, byte[]... member) {
    return mockPipeline.srem(key, member);
  }

  @Override public Response<Long> strlen(String key) {
    return mockPipeline.strlen(key);
  }

  @Override public Response<Long> strlen(byte[] key) {
    return mockPipeline.strlen(key);
  }

  @Override public Response<String> substr(String key, int start, int end) {
    return mockPipeline.substr(key, start, end);
  }

  @Override public Response<String> substr(byte[] key, int start, int end) {
    return mockPipeline.substr(key, start, end);
  }

  @Override public Response<Long> ttl(String key) {
    return mockPipeline.ttl(key);
  }

  @Override public Response<Long> ttl(byte[] key) {
    return mockPipeline.ttl(key);
  }

  @Override public Response<String> type(String key) {
    return mockPipeline.type(key);
  }

  @Override public Response<String> type(byte[] key) {
    return mockPipeline.type(key);
  }

  @Override public Response<Long> zadd(String key, double score, String member) {
    return mockPipeline.zadd(key, score, member);
  }

  @Override public Response<Long> zadd(String key, Map<String, Double> scoreMembers) {
    return mockPipeline.zadd(key, scoreMembers);
  }

  @Override public Response<Long> zadd(byte[] key, double score, byte[] member) {
    return mockPipeline.zadd(key, score, member);
  }

  @Override public Response<Long> zcard(String key) {
    return mockPipeline.zcard(key);
  }

  @Override public Response<Long> zcard(byte[] key) {
    return mockPipeline.zcard(key);
  }

  @Override public Response<Long> zcount(String key, double min, double max) {
    return mockPipeline.zcount(key, min, max);
  }

  @Override public Response<Long> zcount(String key, String min, String max) {
    return mockPipeline.zcount(key, min, max);
  }

  @Override public Response<Long> zcount(byte[] key, double min, double max) {
    return mockPipeline.zcount(key, min, max);
  }

  @Override public Response<Double> zincrby(String key, double score, String member) {
    return mockPipeline.zincrby(key, score, member);
  }

  @Override public Response<Double> zincrby(byte[] key, double score, byte[] member) {
    return mockPipeline.zincrby(key, score, member);
  }

  @Override public Response<Set<String>> zrange(String key, long start, long end) {
    return mockPipeline.zrange(key, start, end);
  }

  @Override public Response<Set<byte[]>> zrange(byte[] key, long start, long end) {
    return mockPipeline.zrange(key, start, end);
  }

  @Override public Response<Set<String>> zrangeByScore(String key, double min, double max) {
    return mockPipeline.zrangeByScore(key, min, max);
  }

  @Override public Response<Set<byte[]>> zrangeByScore(byte[] key, double min, double max) {
    return mockPipeline.zrangeByScore(key, min, max);
  }

  @Override public Response<Set<String>> zrangeByScore(String key, String min, String max) {
    return mockPipeline.zrangeByScore(key, min, max);
  }

  @Override public Response<Set<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max) {
    return mockPipeline.zrangeByScore(key, min, max);
  }

  @Override
  public Response<Set<String>> zrangeByScore(String key, double min, double max, int offset, int
      count) {
    return mockPipeline.zrangeByScore(key, min, max, offset, count);
  }

  @Override
  public Response<Set<String>> zrangeByScore(String key, String min, String max, int offset, int
      count) {
    return mockPipeline.zrangeByScore(key, min, max, offset, count);
  }

  @Override
  public Response<Set<byte[]>> zrangeByScore(byte[] key, double min, double max, int offset, int
      count) {
    return mockPipeline.zrangeByScore(key, min, max, offset, count);
  }

  @Override
  public Response<Set<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int
      count) {
    return mockPipeline.zrangeByScore(key, min, max, offset, count);
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(String key, double min, double max) {
    return mockPipeline.zrangeByScoreWithScores(key, min, max);
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(String key, String min, String max) {
    return mockPipeline.zrangeByScoreWithScores(key, min, max);
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max) {
    return mockPipeline.zrangeByScoreWithScores(key, min, max);
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
    return mockPipeline.zrangeByScoreWithScores(key, min, max);
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(String key, double min, double max, int
      offset, int count) {
    return mockPipeline.zrangeByScoreWithScores(key, min, max, offset, count);
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(String key, String min, String max, int
      offset, int count) {
    return mockPipeline.zrangeByScoreWithScores(key, min, max, offset, count);
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max, int
      offset, int count) {
    return mockPipeline.zrangeByScoreWithScores(key, min, max, offset, count);
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int
      offset, int count) {
    return mockPipeline.zrangeByScoreWithScores(key, min, max, offset, count);
  }

  @Override public Response<Set<String>> zrevrangeByScore(String key, double max, double min) {
    return mockPipeline.zrevrangeByScore(key, max, min);
  }

  @Override public Response<Set<byte[]>> zrevrangeByScore(byte[] key, double max, double min) {
    return mockPipeline.zrevrangeByScore(key, max, min);
  }

  @Override public Response<Set<String>> zrevrangeByScore(String key, String max, String min) {
    return mockPipeline.zrevrangeByScore(key, max, min);
  }

  @Override public Response<Set<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
    return mockPipeline.zrevrangeByScore(key, max, min);
  }

  @Override
  public Response<Set<String>> zrevrangeByScore(String key, double max, double min, int offset,
      int count) {
    return mockPipeline.zrevrangeByScore(key, max, min, offset, count);
  }

  @Override
  public Response<Set<String>> zrevrangeByScore(String key, String max, String min, int offset,
      int count) {
    return mockPipeline.zrevrangeByScore(key, max, min, offset, count);
  }

  @Override
  public Response<Set<byte[]>> zrevrangeByScore(byte[] key, double max, double min, int offset,
      int count) {
    return mockPipeline.zrevrangeByScore(key, max, min, offset, count);
  }

  @Override
  public Response<Set<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset,
      int count) {
    return mockPipeline.zrevrangeByScore(key, max, min, offset, count);
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min) {
    return mockPipeline.zrevrangeByScoreWithScores(key, max, min);
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min) {
    return mockPipeline.zrevrangeByScoreWithScores(key, max, min);
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
    return mockPipeline.zrevrangeByScoreWithScores(key, max, min);
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
    return mockPipeline.zrevrangeByScoreWithScores(key, max, min);
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min, int
      offset, int count) {
    return mockPipeline.zrevrangeByScoreWithScores(key, max, min, offset, count);
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min, int
      offset, int count) {
    return mockPipeline.zrevrangeByScoreWithScores(key, max, min, offset, count);
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min, int
      offset, int count) {
    return mockPipeline.zrevrangeByScoreWithScores(key, max, min, offset, count);
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int
      offset, int count) {
    return mockPipeline.zrevrangeByScoreWithScores(key, max, min, offset, count);
  }

  @Override public Response<Set<Tuple>> zrangeWithScores(String key, long start, long end) {
    return mockPipeline.zrangeWithScores(key, start, end);
  }

  @Override public Response<Set<Tuple>> zrangeWithScores(byte[] key, long start, long end) {
    return mockPipeline.zrangeWithScores(key, start, end);
  }

  @Override public Response<Long> zrank(String key, String member) {
    return mockPipeline.zrank(key, member);
  }

  @Override public Response<Long> zrank(byte[] key, byte[] member) {
    return mockPipeline.zrank(key, member);
  }

  @Override public Response<Long> zrem(String key, String... member) {
    return mockPipeline.zrem(key, member);
  }

  @Override public Response<Long> zrem(byte[] key, byte[]... member) {
    return mockPipeline.zrem(key, member);
  }

  @Override public Response<Long> zremrangeByRank(String key, long start, long end) {
    return mockPipeline.zremrangeByRank(key, start, end);
  }

  @Override public Response<Long> zremrangeByRank(byte[] key, long start, long end) {
    return mockPipeline.zremrangeByRank(key, start, end);
  }

  @Override public Response<Long> zremrangeByScore(String key, double start, double end) {
    return mockPipeline.zremrangeByScore(key, start, end);
  }

  @Override public Response<Long> zremrangeByScore(String key, String start, String end) {
    return mockPipeline.zremrangeByScore(key, start, end);
  }

  @Override public Response<Long> zremrangeByScore(byte[] key, double start, double end) {
    return mockPipeline.zremrangeByScore(key, start, end);
  }

  @Override public Response<Long> zremrangeByScore(byte[] key, byte[] start, byte[] end) {
    return mockPipeline.zremrangeByScore(key, start, end);
  }

  @Override public Response<Set<String>> zrevrange(String key, long start, long end) {
    return mockPipeline.zrevrange(key, start, end);
  }

  @Override public Response<Set<byte[]>> zrevrange(byte[] key, long start, long end) {
    return mockPipeline.zrevrange(key, start, end);
  }

  @Override public Response<Set<Tuple>> zrevrangeWithScores(String key, long start, long end) {
    return mockPipeline.zrevrangeWithScores(key, start, end);
  }

  @Override public Response<Set<Tuple>> zrevrangeWithScores(byte[] key, long start, long end) {
    return mockPipeline.zrevrangeWithScores(key, start, end);
  }

  @Override public Response<Long> zrevrank(String key, String member) {
    return mockPipeline.zrevrank(key, member);
  }

  @Override public Response<Long> zrevrank(byte[] key, byte[] member) {
    return mockPipeline.zrevrank(key, member);
  }

  @Override public Response<Double> zscore(String key, String member) {
    return mockPipeline.zscore(key, member);
  }

  @Override public Response<Double> zscore(byte[] key, byte[] member) {
    return mockPipeline.zscore(key, member);
  }

  @Override public Response<Long> zlexcount(byte[] key, byte[] min, byte[] max) {
    return mockPipeline.zlexcount(key, min, max);
  }

  @Override public Response<Long> zlexcount(String key, String min, String max) {
    return mockPipeline.zlexcount(key, min, max);
  }

  @Override public Response<Set<byte[]>> zrangeByLex(byte[] key, byte[] max, byte[] min) {
    return mockPipeline.zrangeByLex(key, max, min);
  }

  @Override public Response<Set<String>> zrangeByLex(String key, String max, String min) {
    return mockPipeline.zrangeByLex(key, max, min);
  }

  @Override
  public Response<Set<byte[]>> zrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int
      count) {
    return mockPipeline.zrangeByLex(key, max, min, offset, count);
  }

  @Override
  public Response<Set<String>> zrangeByLex(String key, String max, String min, int offset, int
      count) {
    return mockPipeline.zrangeByLex(key, max, min, offset, count);
  }

  @Override public Response<Long> zremrangeByLex(byte[] key, byte[] min, byte[] max) {
    return mockPipeline.zremrangeByLex(key, min, max);
  }

  @Override public Response<Long> zremrangeByLex(String key, String min, String max) {
    return mockPipeline.zremrangeByLex(key, min, max);
  }

  @Override public Response<Long> bitcount(String key) {
    return mockPipeline.bitcount(key);
  }

  @Override public Response<Long> bitcount(String key, long start, long end) {
    return mockPipeline.bitcount(key, start, end);
  }

  @Override public Response<Long> bitcount(byte[] key) {
    return mockPipeline.bitcount(key);
  }

  @Override public Response<Long> bitcount(byte[] key, long start, long end) {
    return mockPipeline.bitcount(key, start, end);
  }

  @Override public Response<byte[]> dump(String key) {
    return mockPipeline.dump(key);
  }

  @Override public Response<byte[]> dump(byte[] key) {
    return mockPipeline.dump(key);
  }

  @Override
  public Response<String> migrate(String host, int port, String key, int destinationDb, int
      timeout) {
    return mockPipeline.migrate(host, port, key, destinationDb, timeout);
  }

  @Override
  public Response<String> migrate(byte[] host, int port, byte[] key, int destinationDb, int
      timeout) {
    return mockPipeline.migrate(host, port, key, destinationDb, timeout);
  }

  @Override public Response<Long> objectRefcount(String key) {
    return mockPipeline.objectRefcount(key);
  }

  @Override public Response<Long> objectRefcount(byte[] key) {
    return mockPipeline.objectRefcount(key);
  }

  @Override public Response<String> objectEncoding(String key) {
    return mockPipeline.objectEncoding(key);
  }

  @Override public Response<byte[]> objectEncoding(byte[] key) {
    return mockPipeline.objectEncoding(key);
  }

  @Override public Response<Long> objectIdletime(String key) {
    return mockPipeline.objectIdletime(key);
  }

  @Override public Response<Long> objectIdletime(byte[] key) {
    return mockPipeline.objectIdletime(key);
  }

  @Override public Response<Long> pexpire(String key, int milliseconds) {
    return mockPipeline.pexpire(key, milliseconds);
  }

  @Override public Response<Long> pexpire(byte[] key, int milliseconds) {
    return mockPipeline.pexpire(key, milliseconds);
  }

  @Override public Response<Long> pexpire(String key, long milliseconds) {
    return mockPipeline.pexpire(key, milliseconds);
  }

  @Override public Response<Long> pexpire(byte[] key, long milliseconds) {
    return mockPipeline.pexpire(key, milliseconds);
  }

  @Override public Response<Long> pexpireAt(String key, long millisecondsTimestamp) {
    return mockPipeline.pexpireAt(key, millisecondsTimestamp);
  }

  @Override public Response<Long> pexpireAt(byte[] key, long millisecondsTimestamp) {
    return mockPipeline.pexpireAt(key, millisecondsTimestamp);
  }

  @Override public Response<Long> pttl(String key) {
    return mockPipeline.pttl(key);
  }

  @Override public Response<Long> pttl(byte[] key) {
    return mockPipeline.pttl(key);
  }

  @Override public Response<String> restore(String key, int ttl, byte[] serializedValue) {
    return mockPipeline.restore(key, ttl, serializedValue);
  }

  @Override public Response<String> restore(byte[] key, int ttl, byte[] serializedValue) {
    return mockPipeline.restore(key, ttl, serializedValue);
  }

  @Override public Response<Double> incrByFloat(String key, double increment) {
    return mockPipeline.incrByFloat(key, increment);
  }

  @Override public Response<Double> incrByFloat(byte[] key, double increment) {
    return mockPipeline.incrByFloat(key, increment);
  }

  @Override public Response<String> psetex(String key, int milliseconds, String value) {
    return mockPipeline.psetex(key, milliseconds, value);
  }

  @Override public Response<String> psetex(byte[] key, int milliseconds, byte[] value) {
    return mockPipeline.psetex(key, milliseconds, value);
  }

  @Override public Response<String> set(String key, String value, String nxxx) {
    return mockPipeline.set(key, value, nxxx);
  }

  @Override public Response<String> set(byte[] key, byte[] value, byte[] nxxx) {
    return mockPipeline.set(key, value, nxxx);
  }

  @Override
  public Response<String> set(String key, String value, String nxxx, String expx, int time) {
    return mockPipeline.set(key, value, nxxx, expx, time);
  }

  @Override
  public Response<String> set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, int time) {
    return mockPipeline.set(key, value, nxxx, expx, time);
  }

  @Override public Response<Double> hincrByFloat(String key, String field, double increment) {
    return mockPipeline.hincrByFloat(key, field, increment);
  }

  @Override public Response<Double> hincrByFloat(byte[] key, byte[] field, double increment) {
    return mockPipeline.hincrByFloat(key, field, increment);
  }

  @Override public Response<String> eval(String script) {
    return mockPipeline.eval(script);
  }

  @Override public Response<String> eval(String script, List<String> keys, List<String> args) {
    return mockPipeline.eval(script, keys, args);
  }

  @Override public Response<String> eval(String script, int numKeys, String[] argv) {
    return mockPipeline.eval(script, numKeys, argv);
  }

  @Override public Response<String> evalsha(String script) {
    return mockPipeline.evalsha(script);
  }

  @Override public Response<String> evalsha(String sha1, List<String> keys, List<String> args) {
    return mockPipeline.evalsha(sha1, keys, args);
  }

  @Override public Response<String> evalsha(String sha1, int numKeys, String[] argv) {
    return mockPipeline.evalsha(sha1, numKeys, argv);
  }

  @Override public Response<Long> pfadd(byte[] key, byte[]... elements) {
    return mockPipeline.pfadd(key, elements);
  }

  @Override public Response<Long> pfcount(byte[] key) {
    return mockPipeline.pfcount(key);
  }

  @Override public Response<Long> pfadd(String key, String... elements) {
    return mockPipeline.pfadd(key, elements);
  }

  @Override public Response<Long> pfcount(String key) {
    return mockPipeline.pfcount(key);
  }

  @Override public Response<List<String>> brpop(String... args) {
    return mockPipeline.brpop(args);
  }

  @Override public Response<List<String>> brpop(int timeout, String... keys) {
    return mockPipeline.brpop(timeout, keys);
  }

  @Override public Response<List<String>> blpop(String... args) {
    return mockPipeline.blpop(args);
  }

  @Override public Response<List<String>> blpop(int timeout, String... keys) {
    return mockPipeline.blpop(timeout, keys);
  }

  @Override public Response<Map<String, String>> blpopMap(int timeout, String... keys) {
    return mockPipeline.blpopMap(timeout, keys);
  }

  @Override public Response<List<byte[]>> brpop(byte[]... args) {
    return mockPipeline.brpop(args);
  }

  @Override public Response<List<String>> brpop(int timeout, byte[]... keys) {
    return mockPipeline.brpop(timeout, keys);
  }

  @Override public Response<Map<String, String>> brpopMap(int timeout, String... keys) {
    return mockPipeline.brpopMap(timeout, keys);
  }

  @Override public Response<List<byte[]>> blpop(byte[]... args) {
    return mockPipeline.blpop(args);
  }

  @Override public Response<List<String>> blpop(int timeout, byte[]... keys) {
    return mockPipeline.blpop(timeout, keys);
  }

  @Override public Response<Long> del(String... keys) {
    return mockPipeline.del(keys);
  }

  @Override public Response<Long> del(byte[]... keys) {
    return mockPipeline.del(keys);
  }

  @Override public Response<Set<String>> keys(String pattern) {
    return mockPipeline.keys(pattern);
  }

  @Override public Response<Set<byte[]>> keys(byte[] pattern) {
    return mockPipeline.keys(pattern);
  }

  @Override public Response<List<String>> mget(String... keys) {
    return mockPipeline.mget(keys);
  }

  @Override public Response<List<byte[]>> mget(byte[]... keys) {
    return mockPipeline.mget(keys);
  }

  @Override public Response<String> mset(String... keysvalues) {
    return mockPipeline.mset(keysvalues);
  }

  @Override public Response<String> mset(byte[]... keysvalues) {
    return mockPipeline.mset(keysvalues);
  }

  @Override public Response<Long> msetnx(String... keysvalues) {
    return mockPipeline.msetnx(keysvalues);
  }

  @Override public Response<Long> msetnx(byte[]... keysvalues) {
    return mockPipeline.msetnx(keysvalues);
  }

  @Override public Response<String> rename(String oldkey, String newkey) {
    return mockPipeline.rename(oldkey, newkey);
  }

  @Override public Response<String> rename(byte[] oldkey, byte[] newkey) {
    return mockPipeline.rename(oldkey, newkey);
  }

  @Override public Response<Long> renamenx(String oldkey, String newkey) {
    return mockPipeline.renamenx(oldkey, newkey);
  }

  @Override public Response<Long> renamenx(byte[] oldkey, byte[] newkey) {
    return mockPipeline.renamenx(oldkey, newkey);
  }

  @Override public Response<String> rpoplpush(String srckey, String dstkey) {
    return mockPipeline.rpoplpush(srckey, dstkey);
  }

  @Override public Response<byte[]> rpoplpush(byte[] srckey, byte[] dstkey) {
    return mockPipeline.rpoplpush(srckey, dstkey);
  }

  @Override public Response<Set<String>> sdiff(String... keys) {
    return mockPipeline.sdiff(keys);
  }

  @Override public Response<Set<byte[]>> sdiff(byte[]... keys) {
    return mockPipeline.sdiff(keys);
  }

  @Override public Response<Long> sdiffstore(String dstkey, String... keys) {
    return mockPipeline.sdiffstore(dstkey, keys);
  }

  @Override public Response<Long> sdiffstore(byte[] dstkey, byte[]... keys) {
    return mockPipeline.sdiffstore(dstkey, keys);
  }

  @Override public Response<Set<String>> sinter(String... keys) {
    return mockPipeline.sinter(keys);
  }

  @Override public Response<Set<byte[]>> sinter(byte[]... keys) {
    return mockPipeline.sinter(keys);
  }

  @Override public Response<Long> sinterstore(String dstkey, String... keys) {
    return mockPipeline.sinterstore(dstkey, keys);
  }

  @Override public Response<Long> sinterstore(byte[] dstkey, byte[]... keys) {
    return mockPipeline.sinterstore(dstkey, keys);
  }

  @Override public Response<Long> smove(String srckey, String dstkey, String member) {
    return mockPipeline.smove(srckey, dstkey, member);
  }

  @Override public Response<Long> smove(byte[] srckey, byte[] dstkey, byte[] member) {
    return mockPipeline.smove(srckey, dstkey, member);
  }

  @Override public Response<Long> sort(String key, SortingParams sortingParameters, String dstkey) {
    return mockPipeline.sort(key, sortingParameters, dstkey);
  }

  @Override public Response<Long> sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
    return mockPipeline.sort(key, sortingParameters, dstkey);
  }

  @Override public Response<Long> sort(String key, String dstkey) {
    return mockPipeline.sort(key, dstkey);
  }

  @Override public Response<Long> sort(byte[] key, byte[] dstkey) {
    return mockPipeline.sort(key, dstkey);
  }

  @Override public Response<Set<String>> sunion(String... keys) {
    return mockPipeline.sunion(keys);
  }

  @Override public Response<Set<byte[]>> sunion(byte[]... keys) {
    return mockPipeline.sunion(keys);
  }

  @Override public Response<Long> sunionstore(String dstkey, String... keys) {
    return mockPipeline.sunionstore(dstkey, keys);
  }

  @Override public Response<Long> sunionstore(byte[] dstkey, byte[]... keys) {
    return mockPipeline.sunionstore(dstkey, keys);
  }

  @Override public Response<String> watch(String... keys) {
    return mockPipeline.watch(keys);
  }

  @Override public Response<String> watch(byte[]... keys) {
    return mockPipeline.watch(keys);
  }

  @Override public Response<Long> zinterstore(String dstkey, String... sets) {
    return mockPipeline.zinterstore(dstkey, sets);
  }

  @Override public Response<Long> zinterstore(byte[] dstkey, byte[]... sets) {
    return mockPipeline.zinterstore(dstkey, sets);
  }

  @Override public Response<Long> zinterstore(String dstkey, ZParams params, String... sets) {
    return mockPipeline.zinterstore(dstkey, params, sets);
  }

  @Override public Response<Long> zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
    return mockPipeline.zinterstore(dstkey, params, sets);
  }

  @Override public Response<Long> zunionstore(String dstkey, String... sets) {
    return mockPipeline.zunionstore(dstkey, sets);
  }

  @Override public Response<Long> zunionstore(byte[] dstkey, byte[]... sets) {
    return mockPipeline.zunionstore(dstkey, sets);
  }

  @Override public Response<Long> zunionstore(String dstkey, ZParams params, String... sets) {
    return mockPipeline.zunionstore(dstkey, params, sets);
  }

  @Override public Response<Long> zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
    return mockPipeline.zunionstore(dstkey, params, sets);
  }

  @Override public Response<String> bgrewriteaof() {
    return mockPipeline.bgrewriteaof();
  }

  @Override public Response<String> bgsave() {
    return mockPipeline.bgsave();
  }

  @Override public Response<String> configGet(String pattern) {
    return mockPipeline.configGet(pattern);
  }

  @Override public Response<String> configSet(String parameter, String value) {
    return mockPipeline.configSet(parameter, value);
  }

  @Override public Response<String> brpoplpush(String source, String destination, int timeout) {
    return mockPipeline.brpoplpush(source, destination, timeout);
  }

  @Override public Response<byte[]> brpoplpush(byte[] source, byte[] destination, int timeout) {
    return mockPipeline.brpoplpush(source, destination, timeout);
  }

  @Override public Response<String> configResetStat() {
    return mockPipeline.configResetStat();
  }

  @Override public Response<String> save() {
    return mockPipeline.save();
  }

  @Override public Response<Long> lastsave() {
    return mockPipeline.lastsave();
  }

  @Override public Response<Long> publish(String channel, String message) {
    return mockPipeline.publish(channel, message);
  }

  @Override public Response<Long> publish(byte[] channel, byte[] message) {
    return mockPipeline.publish(channel, message);
  }

  @Override public Response<String> randomKey() {
    return mockPipeline.randomKey();
  }

  @Override public Response<byte[]> randomKeyBinary() {
    return mockPipeline.randomKeyBinary();
  }

  @Override public Response<String> flushDB() {
    return mockPipeline.flushDB();
  }

  @Override public Response<String> flushAll() {
    return mockPipeline.flushAll();
  }

  @Override public Response<String> info() {
    return mockPipeline.info();
  }

  @Override public Response<List<String>> time() {
    return mockPipeline.time();
  }

  @Override public Response<Long> dbSize() {
    return mockPipeline.dbSize();
  }

  @Override public Response<String> shutdown() {
    return mockPipeline.shutdown();
  }

  @Override public Response<String> ping() {
    return mockPipeline.ping();
  }

  @Override public Response<String> select(int index) {
    return mockPipeline.select(index);
  }

  @Override public Response<Long> bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
    return mockPipeline.bitop(op, destKey, srcKeys);
  }

  @Override public Response<Long> bitop(BitOP op, String destKey, String... srcKeys) {
    return mockPipeline.bitop(op, destKey, srcKeys);
  }

  @Override public Response<String> clusterNodes() {
    return mockPipeline.clusterNodes();
  }

  @Override public Response<String> clusterMeet(String ip, int port) {
    return mockPipeline.clusterMeet(ip, port);
  }

  @Override public Response<String> clusterAddSlots(int... slots) {
    return mockPipeline.clusterAddSlots(slots);
  }

  @Override public Response<String> clusterDelSlots(int... slots) {
    return mockPipeline.clusterDelSlots(slots);
  }

  @Override public Response<String> clusterInfo() {
    return mockPipeline.clusterInfo();
  }

  @Override public Response<List<String>> clusterGetKeysInSlot(int slot, int count) {
    return mockPipeline.clusterGetKeysInSlot(slot, count);
  }

  @Override public Response<String> clusterSetSlotNode(int slot, String nodeId) {
    return mockPipeline.clusterSetSlotNode(slot, nodeId);
  }

  @Override public Response<String> clusterSetSlotMigrating(int slot, String nodeId) {
    return mockPipeline.clusterSetSlotMigrating(slot, nodeId);
  }

  @Override public Response<String> clusterSetSlotImporting(int slot, String nodeId) {
    return mockPipeline.clusterSetSlotImporting(slot, nodeId);
  }

  @Override public Response<String> pfmerge(byte[] destkey, byte[]... sourcekeys) {
    return mockPipeline.pfmerge(destkey, sourcekeys);
  }

  @Override public Response<String> pfmerge(String destkey, String... sourcekeys) {
    return mockPipeline.pfmerge(destkey, sourcekeys);
  }

  @Override public Response<Long> pfcount(String... keys) {
    return mockPipeline.pfcount(keys);
  }

  @Override public Response<Long> pfcount(byte[]... keys) {
    return mockPipeline.pfcount(keys);
  }
}
