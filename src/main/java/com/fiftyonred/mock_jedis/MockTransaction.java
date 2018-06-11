package com.fiftyonred.mock_jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BitOP;
import redis.clients.jedis.Response;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
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

  @Override public Response<String> set(String key, String value) {
    return mockPipeline.set(key, value);
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
