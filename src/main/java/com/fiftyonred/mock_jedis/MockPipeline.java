package com.fiftyonred.mock_jedis;

import com.fiftyonred.utils.WildcardMatcher;
import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.util.SafeEncoder;

import java.util.*;

public class MockPipeline extends Pipeline {
	private final WildcardMatcher wildcardMatcher = new WildcardMatcher();
	private final Map<String, KeyInformation> keys;
	private final Map<String, String> storage;
	private final Map<String, Map<String, String>> hashStorage;
	private final Map<String, List<String>> listStorage;

	public MockPipeline() {
		keys = new HashMap<String, KeyInformation>();
		storage = new HashMap<String, String>();
		hashStorage = new HashMap<String, Map<String, String>>();
		listStorage = new HashMap<String, List<String>>();
	}

	@Override
	public synchronized Response<String> flushAll() {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		keys.clear();
		storage.clear();
		hashStorage.clear();
		listStorage.clear();
		response.set("OK".getBytes());
		return response;
	}

	@Override
	public synchronized Response<String> set(String key, String value) {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		createOrUpdateKey(key, KeyType.STRING, true);
		storage.put(key, value);
		response.set("OK".getBytes());
		return response;
	}

	@Override
	public synchronized Response<String> get(String key) {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		String val = getStringFromStorage(key, false);
		response.set(val != null ? val.getBytes() : null);
		return response;
	}

	@Override
	public synchronized Response<String> setex(String key, int seconds, String value) {
		return psetex(key, seconds * 1000, value);
	}

	@Override
	public synchronized Response<String> psetex(final String key, final int milliseconds, final String value) {
		final Response<String> response = set(key, value);
		pexpire(key, milliseconds);
		return response;
	}

	@Override
	public Response<Long> expire(final String key, final int seconds) {
		return expireAt(key, System.currentTimeMillis() / 1000 + seconds);
	}

	@Override
	public Response<Long> expireAt(final String key, final long seconds) {
		return pexpireAt(key, seconds * 1000);
	}

	@Override
	public Response<Long> pexpire(final String key, final int milliseconds) {
		return pexpireAt(key, System.currentTimeMillis() + milliseconds);
	}

	@Override
	public synchronized Response<Long> pexpireAt(final String key, final long millisecondsTimestamp) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final KeyInformation info = keys.get(key);
		if (info == null || info.isTTLSetAndKeyExpired()) {
			response.set(0L);
		} else {
			info.setExpiration(millisecondsTimestamp);
			response.set(1L);
		}

		return response;
	}

	@Override
	public synchronized Response<Long> ttl(String key) {
		Long pttlInResponse = pttl(key).get();

		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		if (pttlInResponse != -1L) {
			if (pttlInResponse > 0L && pttlInResponse < 1000L) {
				pttlInResponse = 1000L;
			}
			response.set(pttlInResponse / 1000L);
		} else {
			response.set(pttlInResponse);
		}
		return response;
	}

	@Override
	public synchronized Response<Long> pttl(final String key) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final KeyInformation info = keys.get(key);
		response.set(info.getTTL());
		return response;
	}

	@Override
	public synchronized Response<List<String>> mget(final String... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'mget' command");
		}

		Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);

		List<byte[]> result = new ArrayList<byte[]>();
		for (String key : keys) {
			final String val = getStringFromStorage(key, false);
			if (val != null) {
				result.add(val.getBytes());
			} else {
				result.add(null);
			}
		}
		response.set(result);
		return response;
	}

	@Override
	public Response<Long> decr(String key) {
		return decrBy(key, 1);
	}

	@Override
	public synchronized Response<Long> decrBy(String key, long integer) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final String val = getStringFromStorage(key, true);
		final Long result = val == null ? 0L - integer : Long.valueOf(val) - integer;
		storage.put(key, result.toString());
		response.set(result);
		return response;
	}

	@Override
	public Response<Long> incr(String key) {
		return incrBy(key, 1);
	}

	@Override
	public synchronized Response<Long> incrBy(String key, long integer) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final String val = getStringFromStorage(key, true);
		Long result = val == null || "".equals(val) ? integer : Long.valueOf(val) + integer;
		storage.put(key, result.toString());
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<Long> del(String... keys) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		Long result = 0L;
		for (String key : keys) {
			final KeyInformation info = this.keys.remove(key);
			if (info != null) {
				switch (info.getType()) {
					case HASH:
						hashStorage.remove(key);
						break;
					case LIST:
						listStorage.remove(key);
						break;
					default:
						storage.remove(key);
						break;
				}
				++result;
			}
		}

		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<String> hget(final String key, final String field) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final Map<String, String> result = getHashFromStorage(key, false);
		if (result != null) {
			response.set(result.containsKey(field) ? result.get(field).getBytes() : null);
		}
		return response;
	}

	@Override
	public synchronized Response<Map<String, String>> hgetAll(final String key) {
		final Response<Map<String, String>> response = new Response<Map<String, String>>(BuilderFactory.STRING_MAP);
		final Map<String, String> result = getHashFromStorage(key, false);

		if (result != null) {
			final List<byte[]> encodedResult = new ArrayList<byte[]>();
			for (Map.Entry<String, String> e : result.entrySet()) {
				encodedResult.add(SafeEncoder.encode(e.getKey()));
				encodedResult.add(SafeEncoder.encode(e.getValue()));
			}
			response.set(encodedResult);
		} else {
			response.set(new ArrayList<byte[]>());
		}
		return response;
	}

	@Override
	public synchronized Response<Set<String>> hkeys(final String key) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		final Map<String, String> result = getHashFromStorage(key, false);

		if (result != null) {
			final List<byte[]> encodedResult = new ArrayList<byte[]>();
			for (String k : result.keySet()) {
				encodedResult.add(SafeEncoder.encode(k));
			}
			response.set(encodedResult);
		} else {
			response.set(new ArrayList<byte[]>());
		}
		return response;
	}

	@Override
	public synchronized Response<List<String>> hvals(final String key) {
		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		final Map<String, String> result = getHashFromStorage(key, false);

		if (result != null) {
			final List<byte[]> encodedResult = new ArrayList<byte[]>();
			for (String v : result.values()) {
				encodedResult.add(SafeEncoder.encode(v));
			}
			response.set(encodedResult);
		} else {
			response.set(new ArrayList<byte[]>());
		}
		return response;
	}

	@Override
	public synchronized Response<Long> hset(final String key, final String field, final String value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<String, String> m = getHashFromStorage(key, true);
		response.set(m.containsKey(field) ? 0L : 1L);
		m.put(field, value);

		return response;
	}

	@Override
	public synchronized Response<Long> hsetnx(final String key, final String field, final String value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<String, String> m = getHashFromStorage(key, true);
		long result = 0L;
		if (!m.containsKey(field)) {
			m.put(field, value);
			result = 1L;
		}
		response.set(result);

		return response;
	}

	@Override
	public synchronized Response<List<String>> hmget(final String key, final String... fields) {
		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		final List<byte[]> result = new ArrayList<byte[]>();
		final Map<String, String> hash = getHashFromStorage(key, false);
		if (hash == null) {
			for (String field : fields) {
				result.add(null);
			}
			response.set(result);
			return response;
		}

		for (String field : fields) {
			final String v = getHashFromStorage(key, false).get(field);
			result.add(v != null ? v.getBytes() : null);
		}
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<String> hmset(final String key, Map<String, String> hash) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final Map<String, String> m = getHashFromStorage(key, true);
		for (Map.Entry<String, String> e : hash.entrySet()) {
			m.put(e.getKey(), e.getValue());
		}
		response.set("OK".getBytes());
		return response;
	}

	@Override
	public synchronized Response<Long> hincrBy(final String key, final String field, final long value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<String, String> m = getHashFromStorage(key, true);

		String val = m.get(field);
		if (val == null) {
			val = Long.valueOf(0L).toString();
		}
		final Long result = Long.valueOf(val) + value; // TODO: raise exception if value is not a long
		m.put(field, result.toString());
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<Double> hincrByFloat(final String key, final String field, final double value) {
		final Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
		final Map<String, String> m = getHashFromStorage(key, true);

		String val = m.get(field);
		if (val == null) {
			val = Double.valueOf(0D).toString();
		}
		final Double result = Double.parseDouble(val) + value; // TODO: raise exception if value is not a double
		m.put(field, result.toString());
		response.set(result.toString().getBytes());
		return response;
	}

	@Override
	public synchronized Response<Long> hdel(final String key, final String... fields) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<String, String> m = getHashFromStorage(key, true);
		long result = 0;
		for (String field : fields) {
			if (m.remove(field) != null) {
				++result;
			}
		}
		response.set(result);

		return response;
	}

	@Override
	public synchronized Response<Boolean> hexists(final String key, final String field) {
		final Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		final Map<String, String> hash = getHashFromStorage(key, false);
		if (hash != null) {
			response.set(hash.containsKey(field) ? 1L : 0L);
		}

		return response;
	}

	@Override
	public synchronized Response<Long> hlen(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<String, String> hash = getHashFromStorage(key, false);
		if (hash != null) {
			response.set((long) hash.size());
		} else {
			response.set(0L);
		}

		return response;
	}

	@Override
	public synchronized Response<Long> lpush(String key, String... string) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		List<String> list = getListFromStorage(key, true);
		if (list == null) {
			list = new ArrayList<String>();
			listStorage.put(key, list);
		}
		Collections.addAll(list, string);
		response.set((long) string.length);
		return response;
	}

	@Override
	public synchronized Response<String> lpop(String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final List<String> list = getListFromStorage(key, true);
		if (list == null || list.isEmpty()) {
			response.set(null);
		} else {
			response.set(list.remove(list.size() - 1).getBytes());
		}
		return response;
	}

	@Override
	public synchronized Response<Long> llen(String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final List<String> list = getListFromStorage(key, false);
		if (list == null) {
			response.set(0L);
		} else {
			response.set((long) list.size());
		}
		return response;
	}

	@Override
	public void sync() {
		// do nothing
	}

	@Override
	public synchronized Response<Set<String>> keys(final String pattern) {
		Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);

		List<byte[]> result = new ArrayList<byte[]>();
		filterKeys(pattern, keys.keySet(), result);

		response.set(result);
		return response;
	}

	public void filterKeys(final String pattern, final Collection<String> collection, final List<byte[]> result) {
		for (String key : collection) {
			if (wildcardMatcher.match(key, pattern)) {
				result.add(key.getBytes());
			}
		}
	}

	protected void createOrUpdateKey(final String key, final KeyType type, final boolean resetTTL) {
		KeyInformation info = keys.get(key);
		if (info == null) {
			info = new KeyInformation(type);
			keys.put(key, info);
		} else if (resetTTL) {
			info.setExpiration(-1L);
		}
	}

	protected String getStringFromStorage(final String key, final boolean createIfNotExist) {
		final KeyInformation info = keys.get(key);
		if (info == null) {
			if (createIfNotExist) {
				createOrUpdateKey(key, KeyType.STRING, true);
				storage.put(key, "");
				return "";
			}
			return null; // no such key exists
		}
		if (info.getType() != KeyType.STRING) {
			throw new JedisDataException("ERR Operation against a key holding the wrong kind of value");
		}
		if (info.isTTLSetAndKeyExpired()) {
			storage.remove(key);
			keys.remove(key);
			return null;
		}
		return storage.get(key);
	}

	protected Map<String, String> getHashFromStorage(final String key, final boolean createIfNotExist) {
		final KeyInformation info = keys.get(key);
		if (info == null) {
			if (createIfNotExist) {
				createOrUpdateKey(key, KeyType.HASH, false);
				Map<String, String> result = new HashMap<String, String>();
				hashStorage.put(key, result);
				return result;
			}
			return null; // no such key exists
		}
		if (info.getType() != KeyType.HASH) {
			throw new JedisDataException("ERR Operation against a key holding the wrong kind of value");
		}
		if (info.isTTLSetAndKeyExpired()) {
			hashStorage.remove(key);
			keys.remove(key);
			return null;
		}
		return hashStorage.get(key);
	}

	protected List<String> getListFromStorage(final String key, final boolean createIfNotExist) {
		final KeyInformation info = keys.get(key);
		if (info == null) {
			if (createIfNotExist) {
				createOrUpdateKey(key, KeyType.LIST, false);
				List<String> result = new ArrayList<String>();
				listStorage.put(key, result);
				return result;
			}
			return null; // no such key exists
		}
		if (info.getType() != KeyType.LIST) {
			throw new JedisDataException("ERR Operation against a key holding the wrong kind of value");
		}
		if (info.isTTLSetAndKeyExpired()) {
			listStorage.remove(key);
			keys.remove(key);
			return null;
		}
		return listStorage.get(key);
	}
}
