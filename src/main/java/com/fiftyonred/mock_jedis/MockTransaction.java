package com.fiftyonred.mock_jedis;

import redis.clients.jedis.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockTransaction extends Transaction {
    private final Pipeline pipeline;

    private final List<Response<?>> responses = new ArrayList<Response<?>>();

    public MockTransaction(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    protected Client getClient(String key) {
        throw new UnsupportedOperationException(MockJedis.NOT_IMPLEMENTED);
    }

    @Override
    protected Client getClient(byte[] key) {
        throw new UnsupportedOperationException(MockJedis.NOT_IMPLEMENTED);
    }

    @Override
    public List<Object> exec() {
        List<Object> objects = new ArrayList<Object>();
        for  (Response resp : responses)
            objects.add(resp.get());
        return objects;
    }

    @Override
    public List<Response<?>> execGetResponse() {
        return responses;
    }

    @Override
    public String discard() {
        return null;
    }

    @Override
    public Response<Long> append(final String key, final String value) {
        Response resp = pipeline.append(key, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> append(final byte[] key, final byte[] value) {
        Response resp = pipeline.append(key, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> getSet(final String key, final String value) {
        Response resp = pipeline.getSet(key, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<byte[]> getSet(final byte[] key, final byte[] value) {
        Response resp = pipeline.getSet(key, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> echo(final String string) {
        Response resp = pipeline.echo(string);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<byte[]> echo(final byte[] string) {
        Response resp = pipeline.echo(string);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> randomKey() {
        Response resp = pipeline.randomKey();
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> rename(final String oldkey, final String newkey) {
        Response resp = pipeline.rename(oldkey, newkey);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> rename(final byte[] oldkey, final byte[] newkey) {
        Response resp = pipeline.rename(oldkey, newkey);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> renamenx(final String oldkey, final String newkey) {
        Response resp = pipeline.renamenx(oldkey, newkey);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> renamenx(final byte[] oldkey, final byte[] newkey) {
        Response resp = pipeline.renamenx(oldkey, newkey);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> persist(final String key) {
        Response resp = pipeline.persist(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> persist(final byte[] key) {
        Response resp = pipeline.persist(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<byte[]> get(final byte[] key) {
        Response resp = pipeline.get(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<byte[]> dump(final byte[] key) {
        Response resp = pipeline.dump(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<byte[]> dump(final String key) {
        Response resp = pipeline.dump(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> restore(final byte[] key, final int ttl, final byte[] serializedValue) {
        Response resp = pipeline.restore(key, ttl, serializedValue);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> restore(final String key, final int ttl, final byte[] serializedValue) {
        Response resp = pipeline.restore(key, ttl, serializedValue);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> del(final byte[]... keys) {
        Response resp = pipeline.del(keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> flushDB() {
        Response resp = pipeline.flushDB();
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<byte[]>> keys(final byte[] pattern) {
        Response resp = pipeline.keys(pattern);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<byte[]>> mget(final byte[]... keys) {
        Response resp = pipeline.mget(keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> mset(final String... keysvalues) {
        Response resp = pipeline.mset(keysvalues);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> mset(final byte[]... keysvalues) {
        Response resp = pipeline.mset(keysvalues);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> msetnx(final String... keysvalues) {
        Response resp = pipeline.msetnx(keysvalues);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> msetnx(final byte[]... keysvalues) {
        Response resp = pipeline.msetnx(keysvalues);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> incrBy(final byte[] key, final long integer) {
        Response resp = pipeline.incrBy(key, integer);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Double> incrByFloat(final String key, final double integer) {
        Response resp = pipeline.incrByFloat(key, integer);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Double> incrByFloat(final byte[] key, final double integer) {
        Response resp = pipeline.incrByFloat(key, integer);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> strlen(final String key) {
        Response resp = pipeline.strlen(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> strlen(final byte[] key) {
        Response resp = pipeline.strlen(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> incr(final byte[] key) {
        Response resp = pipeline.incr(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> decr(final byte[] key) {
        Response resp = pipeline.decr(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> decrBy(final byte[] key, final long integer) {
        Response resp = pipeline.decrBy(key, integer);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<String>> sort(final String key) {
        Response resp = pipeline.sort(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<byte[]>> sort(final byte[] key) {
        Response resp = pipeline.sort(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sort(final String key, final String dstkey) {
        Response resp = pipeline.sort(key, dstkey);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sort(final byte[] key, final byte[] dstkey) {
        Response resp = pipeline.sort(key, dstkey);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<String>> sort(final String key, final SortingParams sortingParameters) {
        Response resp = pipeline.sort(key, sortingParameters);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<byte[]>> sort(final byte[] key, final SortingParams sortingParameters) {
        Response resp = pipeline.sort(key, sortingParameters);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sort(final String key, final SortingParams sortingParameters, final String dstkey) {
        Response resp = pipeline.sort(key, sortingParameters, dstkey);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sort(final byte[] key, final SortingParams sortingParameters, final byte[] dstkey) {
        Response resp = pipeline.sort(key, sortingParameters, dstkey);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> hset(final byte[] key, final byte[] field, final byte[] value) {
        Response resp = pipeline.hset(key, field, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<byte[]> hget(final byte[] key, final byte[] field) {
        Response resp = pipeline.hget(key, field);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> hmset(final byte[] key, final Map<byte[], byte[]> hash) {
        Response resp = pipeline.hmset(key, hash);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<byte[]>> hmget(final byte[] key, final byte[]... fields) {
        Response resp = pipeline.hmget(key, fields);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> hincrBy(final byte[] key, final byte[] field, final long value) {
        Response resp = pipeline.hincrBy(key, field, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Boolean> hexists(final byte[] key, final byte[] field) {
        Response resp = pipeline.hexists(key, field);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> hdel(final byte[] key, final byte[]... fields) {
        Response resp = pipeline.hdel(key, fields);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> hlen(final byte[] key) {
        Response resp = pipeline.hlen(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<byte[]>> hkeys(final byte[] key) {
        Response resp = pipeline.hkeys(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<byte[]>> hvals(final byte[] key) {
        Response resp = pipeline.hvals(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Map<byte[], byte[]>> hgetAll(final byte[] key) {
        Response resp = pipeline.hgetAll(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> ping() {
        Response resp = pipeline.ping();
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> dbSize() {
        Response resp = pipeline.dbSize();
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> type(final String key) {
        Response resp = pipeline.type(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> type(final byte[] key) {
        Response resp = pipeline.type(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> move(final String key, final int dbIndex) {
        Response resp = pipeline.move(key, dbIndex);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> move(final byte[] key, final int dbIndex) {
        Response resp = pipeline.move(key, dbIndex);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> select(final int dbIndex) {
        Response resp = pipeline.select(dbIndex);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> set(final String key, final String value) {
        Response resp = pipeline.set(key, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> set(final byte[] key, final byte[] value) {
        Response resp = pipeline.set(key, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> setnx(final String key, final String value) {
        Response resp = pipeline.setnx(key, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> setnx(final byte[] key, final byte[] value) {
        Response resp = pipeline.setnx(key, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> get(final String key) {
        Response resp = pipeline.get(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<String>> mget(final String... keys) {
        Response resp = pipeline.mget(keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> flushAll() {
        Response resp = pipeline.flushAll();
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> decrBy(final String key, final long integer) {
        Response resp = pipeline.decrBy(key, integer);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> decr(final String key) {
        Response resp = pipeline.decr(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> expire(final String key, final int seconds) {
        Response resp = pipeline.expire(key, seconds);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> expire(final byte[] key, final int seconds) {
        Response resp = pipeline.expire(key, seconds);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> expireAt(final String key, final long unixTime) {
        Response resp = pipeline.expireAt(key, unixTime);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> expireAt(final byte[] key, final long unixTime) {
        Response resp = pipeline.expireAt(key, unixTime);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> psetex(final String key, final int milliseconds, final String value) {
        Response resp = pipeline.psetex(key, milliseconds, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> set(final String key, final String value, final String nxxx) {
        Response resp = pipeline.set(key, value, nxxx);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> set(final String key, final String value, final String nxxx, final String expx, final int time) {
        Response resp = pipeline.set(key, value, nxxx, expx, time);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> migrate(final String host, final int port, final String key, final int destinationDb, final int timeout) {
        Response resp = pipeline.migrate(host, port, key, destinationDb, timeout);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> psetex(final byte[] key, final int milliseconds, final byte[] value) {
        Response resp = pipeline.psetex(key, milliseconds, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> ttl(final String key) {
        Response resp = pipeline.ttl(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> ttl(final byte[] key) {
        Response resp = pipeline.ttl(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> pttl(final String key) {
        Response resp = pipeline.pttl(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> pttl(final byte[] key) {
        Response resp = pipeline.pttl(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> pexpire(final String key, final int milliseconds) {
        Response resp = pipeline.pexpire(key, milliseconds);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> pexpire(final String key, final long milliseconds) {
        Response resp = pipeline.pexpire(key, milliseconds);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> pexpire(final byte[] key, final int milliseconds) {
        Response resp = pipeline.pexpire(key, milliseconds);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> pexpireAt(final String key, final long millisecondsTimestamp) {
        Response resp = pipeline.pexpireAt(key, millisecondsTimestamp);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> pexpireAt(final byte[] key, final long millisecondsTimestamp) {
        Response resp = pipeline.pexpireAt(key, millisecondsTimestamp);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Boolean> exists(final String key) {
        Response resp = pipeline.exists(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Boolean> exists(final byte[] key) {
        Response resp = pipeline.exists(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> incr(final String key) {
        Response resp = pipeline.incr(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> incrBy(final String key, final long integer) {
        Response resp = pipeline.incrBy(key, integer);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> setex(final String key, final int seconds, final String value) {
        Response resp = pipeline.setex(key, seconds, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> setex(final byte[] key, final int seconds, final byte[] value) {
        Response resp = pipeline.setex(key, seconds, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> del(final String... keys) {
        Response resp = pipeline.del(keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> del(final String key) {
        Response resp = pipeline.del(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> del(final byte[] key) {
        Response resp = pipeline.del(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> hset(final String key, final String field, final String value) {
        Response resp = pipeline.hset(key, field, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> hsetnx(final String key, final String field, final String value) {
        Response resp = pipeline.hsetnx(key, field, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> hsetnx(final byte[] key, final byte[] field, final byte[] value) {
        Response resp = pipeline.hsetnx(key, field, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> hget(final String key, final String field) {
        Response resp = pipeline.hget(key, field);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Map<String, String>> hgetAll(final String key) {
        Response resp = pipeline.hgetAll(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<String>> hkeys(final String key) {
        Response resp = pipeline.hkeys(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<String>> hvals(final String key) {
        Response resp = pipeline.hvals(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> hmset(final String key, final Map<String, String> hash) {
        Response resp = pipeline.hmset(key, hash);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<String>> hmget(final String key, final String... fields) {
        Response resp = pipeline.hmget(key, fields);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> hincrBy(final String key, final String field, final long value) {
        Response resp = pipeline.hincrBy(key, field, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Double> hincrByFloat(final String key, final String field, final double value) {
        Response resp = pipeline.hincrByFloat(key, field, value);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> hdel(final String key, final String... fields) {
        Response resp = pipeline.hdel(key, fields);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Boolean> hexists(final String key, final String field) {
        Response resp = pipeline.hexists(key, field);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> hlen(final String key) {
        Response resp = pipeline.hlen(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> lpush(final String key, final String... strings) {
        Response resp = pipeline.lpush(key, strings);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> lpush(final byte[] key, final byte[]... strings) {
        Response resp = pipeline.lpush(key, strings);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sadd(final String key, final String... members) {
        Response resp = pipeline.sadd(key, members);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sadd(final byte[] key, final byte[]... members) {
        Response resp = pipeline.sadd(key, members);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> scard(final String key) {
        Response resp = pipeline.scard(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> scard(final byte[] key) {
        Response resp = pipeline.scard(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<String>> sdiff(final String... keys) {
        Response resp = pipeline.sdiff(keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<byte[]>> sdiff(final byte[]... keys) {
        Response resp = pipeline.sdiff(keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sdiffstore(final String dstKey, final String... keys) {
        Response resp = pipeline.sdiffstore(dstKey, keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sdiffstore(final byte[] dstKey, final byte[]... keys) {
        Response resp = pipeline.sdiffstore(dstKey, keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<String>> sinter(final String... keys) {
        Response resp = pipeline.sinter(keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<byte[]>> sinter(final byte[]... keys) {
        Response resp = pipeline.sinter(keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sinterstore(final String dstKey, final String... keys) {
        Response resp = pipeline.sinterstore(dstKey, keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sinterstore(final byte[] dstKey, final byte[]... keys) {
        Response resp = pipeline.sinterstore(dstKey, keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Boolean> sismember(final String key, final String member) {
        Response resp = pipeline.sismember(key, member);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Boolean> sismember(final byte[] key, final byte[] member) {
        Response resp = pipeline.sismember(key, member);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<String>> smembers(final String key) {
        Response resp = pipeline.smembers(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<byte[]>> smembers(final byte[] key) {
        Response resp = pipeline.smembers(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> smove(final String srcKey, final String dstKey, final String member) {
        Response resp = pipeline.smove(srcKey, dstKey, member);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> smove(final byte[] srcKey, final byte[] dstKey, final byte[] member) {
        Response resp = pipeline.smove(srcKey, dstKey, member);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> spop(final String key) {
        Response resp = pipeline.spop(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<byte[]> spop(final byte[] key) {
        Response resp = pipeline.spop(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> srandmember(final String key) {
        Response resp = pipeline.srandmember(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<byte[]> srandmember(final byte[] key) {
        Response resp = pipeline.srandmember(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> srem(final String key, final String... members) {
        Response resp = pipeline.srem(key, members);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> srem(final byte[] key, final byte[]... members) {
        Response resp = pipeline.srem(key, members);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<String>> sunion(final String... keys) {
        Response resp = pipeline.sunion(keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<byte[]>> sunion(final byte[]... keys) {
        Response resp = pipeline.sunion(keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sunionstore(final String dstKey, final String... keys) {
        Response resp = pipeline.sunionstore(dstKey, keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> sunionstore(final byte[] dstKey, final byte[]... keys) {
        Response resp = pipeline.sunionstore(dstKey, keys);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<String> lpop(final String key) {
        Response resp = pipeline.lpop(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<byte[]> lpop(final byte[] key) {
        Response resp = pipeline.lpop(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> llen(final String key) {
        Response resp = pipeline.llen(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Long> llen(final byte[] key) {
        Response resp = pipeline.llen(key);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<String>> lrange(final String key, final long start, final long end) {
        Response resp = pipeline.lrange(key, start, end);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<List<byte[]>> lrange(final byte[] key, final long start, final long end) {
        Response resp = pipeline.lrange(key, start, end);
        responses.add(resp);
        return resp;
    }

    @Override
    public Response<Set<String>> keys(final String pattern) {
        Response resp = pipeline.keys(pattern);
        responses.add(resp);
        return resp;
    }
}
