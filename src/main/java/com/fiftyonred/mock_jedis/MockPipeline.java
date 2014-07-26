package com.fiftyonred.mock_jedis;

import com.fiftyonred.utils.WildcardMatcher;
import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Response;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.util.SafeEncoder;

import java.util.*;

public class MockPipeline extends Pipeline {
	private final WildcardMatcher wildcardMatcher = new WildcardMatcher();
	private final List<Map<String, KeyInformation>> allKeys;
	private final List<Map<String, String>> allStorage;
	private final List<Map<String, Map<String, String>>> allHashStorage;
    private final List<Map<String, List<String>>> allListStorage;
    private final List<Map<String, Set<String>>> allSetStorage;

	private int currentDB;
	private static final int NUM_DBS = 16;
	private Map<String, KeyInformation> keys;
	private Map<String, String> storage;
	private Map<String, Map<String, String>> hashStorage;
    private Map<String, List<String>> listStorage;
    private Map<String, Set<String>> setStorage;

	public MockPipeline() {
		allKeys = new ArrayList<Map<String, KeyInformation>>(NUM_DBS);
		allStorage = new ArrayList<Map<String, String>>(NUM_DBS);
		allHashStorage = new ArrayList<Map<String, Map<String, String>>>(NUM_DBS);
		allListStorage = new ArrayList<Map<String, List<String>>>(NUM_DBS);
        allSetStorage = new ArrayList<Map<String, Set<String>>>(NUM_DBS);
		for (int i = 0; i < NUM_DBS; ++i) {
			allKeys.add(new HashMap<String, KeyInformation>());
			allStorage.add(new HashMap<String, String>());
			allHashStorage.add(new HashMap<String, Map<String, String>>());
            allListStorage.add(new HashMap<String, List<String>>());
            allSetStorage.add(new HashMap<String, Set<String>>());
		}
		select(0);
	}

	public int getCurrentDB() {
		return currentDB;
	}

	protected static <T> T getRandomElementFromSet(final Set<T> set) {
		return (T) set.toArray()[(int) (Math.random() * set.size())];
	}

	@Override
	public Response<String> ping() {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set("PONG".getBytes());
		return response;
	}

	@Override
	public Response<String> echo(final String string) {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(echo(string.getBytes()).get());
		return response;
	}

	@Override
	public Response<byte[]> echo(final byte[] string) {
		Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		response.set(string);
		return response;
	}

	@Override
	public synchronized Response<Long> dbSize() {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) keys.size());
		return response;
	}

	@Override
	public synchronized Response<String> flushAll() {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		for (int dbNum = 0; dbNum < NUM_DBS; ++dbNum) {
			allKeys.get(dbNum).clear();
			allStorage.get(dbNum).clear();
			allHashStorage.get(dbNum).clear();
			allListStorage.get(dbNum).clear();
		}
		response.set("OK".getBytes());
		return response;
	}

	@Override
	public synchronized Response<String> flushDB() {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		keys.clear();
		storage.clear();
		hashStorage.clear();
		listStorage.clear();
		response.set("OK".getBytes());
		return response;
	}

	@Override
	public synchronized Response<String> rename(final String oldkey, final String newkey) {
		if (oldkey.equals(newkey)) {
			throw new JedisDataException("ERR source and destination objects are the same");
		}
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		final KeyInformation info = keys.get(oldkey);
		switch (info.getType()) {
			case HASH:
				hashStorage.put(newkey, hashStorage.get(oldkey));
				hashStorage.remove(oldkey);
				break;
			case LIST:
				listStorage.put(newkey, listStorage.get(oldkey));
				listStorage.remove(oldkey);
				break;
			case STRING:
			default:
				storage.put(newkey, storage.get(oldkey));
				storage.remove(oldkey);
		}
		keys.put(newkey, info);
		keys.remove(oldkey);
		response.set("OK".getBytes());
		return response;
	}

	@Override
	public Response<String> rename(final byte[] oldkey, final byte[] newkey) {
		return rename(new String(oldkey), new String(newkey));
	}

	@Override
	public synchronized Response<Long> renamenx(final String oldkey, final String newkey) {
		if (oldkey.equals(newkey)) {
			throw new JedisDataException("ERR source and destination objects are the same");
		}
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final KeyInformation newInfo = keys.get(newkey);
		if (newInfo == null) {
			rename(oldkey, newkey);
			response.set(1L);
		} else {
			response.set(0L);
		}
		return response;
	}

	@Override
	public Response<Long> renamenx(final byte[] oldkey, final byte[] newkey) {
		return renamenx(new String(oldkey), new String(newkey));
	}

	@Override
	public synchronized Response<String> set(final String key, final String value) {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		createOrUpdateKey(key, KeyType.STRING, true);
		storage.put(key, value);
		response.set("OK".getBytes());
		return response;
	}

	@Override
	public synchronized Response<String> set(final byte[] key, final byte[] value) {
		return set(new String(key), new String(value));
	}

	@Override
	public synchronized Response<Long> setnx(final String key, final String value) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final String result = getStringFromStorage(key, false);
		if (result == null) {
			set(key, value);
			response.set(1L);
		} else {
			response.set(0L);
		}

		return response;
	}

	@Override
	public synchronized Response<Long> setnx(final byte[] key, final byte[] value) {
		return setnx(new String(key), new String(value));
	}

	@Override
	public synchronized Response<String> get(String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final String val = getStringFromStorage(key, false);
		response.set(val == null ? null : val.getBytes());
		return response;
	}

	@Override
	public Response<byte[]> get(final byte[] key) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final String result = get(new String(key)).get();
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public synchronized Response<String> getSet(final String key, final String value) {
		final Response<String> response = get(key);
		set(key, value);
		return response;
	}

	@Override
	public synchronized Response<byte[]> getSet(final byte[] key, final byte[] value) {
		final Response<byte[]> response = get(key);
		set(key, value);
		return response;
	}

	@Override
	public Response<byte[]> dump(byte[] key) {
		return get(key);
	}

	@Override
	public Response<byte[]> dump(String key) {
		return get(key.getBytes());
	}

	@Override
	public Response<String> restore(String key, int ttl, byte[] serializedValue) {
		return setex(key.getBytes(), ttl, serializedValue);
	}

	@Override
	public Response<String> restore(byte[] key, int ttl, byte[] serializedValue) {
		return setex(key, ttl, serializedValue);
	}

	@Override
	public synchronized Response<Boolean> exists(final String key) {
		Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		response.set(keys.containsKey(key) ? 1L : 0L);
		return response;
	}

	@Override
	public synchronized Response<Boolean> exists(final byte[] key) {
		return exists(new String(key));
	}

	@Override
	public synchronized Response<String> type(final String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final KeyInformation info = keys.get(key);
		if (info != null && info.getType() == KeyType.STRING) {
			response.set("string".getBytes());
		} else if (info != null && info.getType() == KeyType.LIST) {
			response.set("list".getBytes());
		} else if (info != null && info.getType() == KeyType.SET) {
			response.set("set".getBytes());
		} else {
			response.set("none".getBytes());
		}
		return response;
	}

	@Override
	public synchronized Response<String> type(final byte[] key) {
		return type(new String(key));
	}

	@Override
	public Response<Long> move(final String key, final int dbIndex) {
		if (dbIndex < 0 || dbIndex > 15) {
			throw new JedisDataException("ERR index out of range");
		}
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final KeyInformation info = keys.get(key);
		if (info == null) {
			response.set(0L);
		} else {
			final KeyInformation infoNew = allKeys.get(dbIndex).get(key);
			if (infoNew == null) {
				allKeys.get(dbIndex).put(key, info);
				switch (info.getType()) {
					case HASH:
						allHashStorage.get(dbIndex).put(key, hashStorage.get(key));
						hashStorage.remove(key);
						break;
					case LIST:
						allListStorage.get(dbIndex).put(key, listStorage.get(key));
						listStorage.remove(key);
						break;
					default:
					case STRING:
						allStorage.get(dbIndex).put(key, storage.get(key));
						storage.remove(key);
				}
				keys.remove(key);
				response.set(1L);
			} else {
				response.set(0L);
			}
		}
		return response;

	}

	@Override
	public synchronized Response<Long> move(final byte[] key, final int dbIndex) {
		return move(new String(key), dbIndex);
	}

	@Override
	public synchronized Response<String> randomKey() {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		if (keys.size() == 0) {
			response.set(null);
		} else {
			final String result = getRandomElementFromSet(keys.keySet());
			response.set(result.getBytes());
		}
		return response;
	}

	@Override
	public Response<byte[]> randomKeyBinary() {
		Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final String result = randomKey().get();
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public Response<String> select(final int dbIndex) {
		if (dbIndex < 0 || dbIndex > 15) {
			throw new JedisDataException("ERR invalid DB index");
		}
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		currentDB = dbIndex;
		keys = allKeys.get(dbIndex);
		storage = allStorage.get(dbIndex);
		hashStorage = allHashStorage.get(dbIndex);
		listStorage = allListStorage.get(dbIndex);
        setStorage = allSetStorage.get(dbIndex);
		response.set("OK".getBytes());
		return response;
	}

	@Override
	public Response<String> setex(String key, int seconds, String value) {
		return psetex(key, seconds * 1000, value);
	}

	@Override
	public Response<String> setex(byte[] key, int seconds, byte[] value) {
		return setex(new String(key), seconds, new String(value));
	}

	@Override
	public synchronized Response<String> psetex(final String key, final int milliseconds, final String value) {
		final Response<String> response = set(key, value);
		pexpire(key, milliseconds);
		return response;
	}

	@Override
	public Response<String> psetex(final byte[] key, final int milliseconds, final byte[] value) {
		return psetex(new String(key), milliseconds, new String(value));
	}

	@Override
	public Response<Long> expire(final String key, final int seconds) {
		return expireAt(key, System.currentTimeMillis() / 1000 + seconds);
	}

	@Override
	public Response<Long> expire(final byte[] key, final int seconds) {
		return expire(new String(key), seconds);
	}

	@Override
	public Response<Long> expireAt(final String key, final long seconds) {
		return pexpireAt(key, seconds * 1000);
	}

	@Override
	public Response<Long> expireAt(final byte[] key, final long seconds) {
		return expireAt(new String(key), seconds);
	}

	@Override
	public Response<Long> pexpire(final String key, final int milliseconds) {
		return pexpireAt(key, System.currentTimeMillis() + milliseconds);
	}

	@Override
	public Response<Long> pexpire(final byte[] key, final int milliseconds) {
		return pexpire(new String(key), milliseconds);
	}

	@Override
	public Response<Long> pexpire(String key, long milliseconds) {
		return pexpireAt(key, System.currentTimeMillis() + milliseconds);
	}

	@Override
	public Response<Long> pexpire(byte[] key, long milliseconds) {
		return pexpire(new String(key), milliseconds);
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
	public synchronized Response<Long> pexpireAt(final byte[] key, final long millisecondsTimestamp) {
		return pexpireAt(new String(key), millisecondsTimestamp);
	}

	@Override
	public Response<Long> ttl(String key) {
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
	public synchronized Response<Long> append(final String key, final String value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final String newVal = getStringFromStorage(key, true) + value;
		set(key, newVal);
		response.set((long) newVal.length());
		return response;
	}

	@Override
	public Response<Long> append(final byte[] key, final byte[] value) {
		return append(new String(key), new String(value));
	}

	@Override
	public Response<Long> ttl(byte[] key) {
		return ttl(new String(key));
	}

	@Override
	public synchronized Response<Long> pttl(final String key) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final KeyInformation info = keys.get(key);
		response.set(info == null ? -1L : info.getTTL());
		return response;
	}

	@Override
	public synchronized Response<Long> persist(String key) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final KeyInformation info = keys.get(key);
		if (response == null || info.getTTL() == -1) {
			response.set(0L);
		} else {
			info.setExpiration(-1L);
			response.set(1L);
		}
		return response;
	}

	@Override
	public Response<Long> persist(byte[] key) {
		return persist(new String(key));
	}

	@Override
	public synchronized Response<List<String>> mget(final String... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'mget' command");
		}

		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);

		final List<byte[]> result = new ArrayList<byte[]>();
		for (final String key : keys) {
			final String val = getStringFromStorage(key, false);
			result.add(val == null ? null : val.getBytes());
		}
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<List<byte[]>> mget(final byte[]... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'mget' command");
		}

		final Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);

		final List<byte[]> result = new ArrayList<byte[]>();
		for (final byte[] key : keys) {
			final String val = getStringFromStorage(new String(key), false);
			result.add(val == null ? null : val.getBytes());
		}
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<String> mset(final String... keys) {
		if (keys.length <= 0 || keys.length % 2 != 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'mset' command");
		}

		for (int i = 0; i < keys.length; i += 2) {
			set(keys[i], keys[i + 1]);
		}

		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set("OK".getBytes());
		return response;
	}

	@Override
	public Response<String> mset(final byte[]... keys) {
		return mset(convertToStrings(keys));
	}

	@Override
	public synchronized Response<Long> msetnx(final String... keys) {
		if (keys.length <= 0 || keys.length % 2 != 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'msetnx' command");
		}

		long result = 1L;
		for (int i = 0; i < keys.length; i += 2) {
			if (setnx(keys[i], keys[i + 1]).get() == 0L) {
                result = 0L;
			}
		}

		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(result);
		return response;
	}

	@Override
	public Response<Long> msetnx(final byte[]... keys) {
		return msetnx(convertToStrings(keys));
	}

	@Override
	public Response<Long> decr(String key) {
		return decrBy(key, 1);
	}

	@Override
	public Response<Long> decr(final byte[] key) {
		return decr(new String(key));
	}

	@Override
	public Response<Long> decrBy(String key, long integer) {
		return incrBy(key, -integer);
	}

	@Override
	public Response<Long> decrBy(final byte[] key, final long integer) {
		return decrBy(new String(key), integer);
	}

	@Override
	public Response<Long> incr(String key) {
		return incrBy(key, 1);
	}

	@Override
	public Response<Long> incr(final byte[] key) {
		return incr(new String(key));
	}

	@Override
	public synchronized Response<Long> incrBy(String key, long integer) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final String val = getStringFromStorage(key, true);

		final long oldValue;
		try {
			oldValue = val == null || "".equals(val) ? 0L : Long.parseLong(val);
		} catch (final NumberFormatException ignored) {
			throw new JedisDataException("ERR value is not an integer or out of range");
		}

		// check for overflow
		if (oldValue > 0 ? integer > Long.MAX_VALUE - oldValue : integer < Long.MIN_VALUE - oldValue) {
			throw new JedisDataException("ERR value is not an integer or out of range");
		}

		final long result = oldValue + integer;
		storage.put(key, Long.toString(result));
		response.set(result);
		return response;
	}

	@Override
	public Response<Long> incrBy(final byte[] key, long integer) {
		return incrBy(new String(key), integer);
	}

	@Override
	public synchronized Response<Double> incrByFloat(final String key, final double integer) {
		final Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
		final String val = getStringFromStorage(key, true);
		final Double result;
		try {
			result = val == null || "".equals(val) ? integer : Double.parseDouble(val) + integer;
		} catch (final NumberFormatException ignored) {
			throw new JedisDataException("ERR value is not a valid float");
		}
		storage.put(key, result.toString());
		response.set(result.toString().getBytes());
		return response;
	}

	@Override
	public Response<Double> incrByFloat(final byte[] key, final double integer) {
		return incrByFloat(new String(key), integer);
	}

	@Override
	public Response<List<String>> sort(String key) {
		return sort(key, new SortingParams());
	}

	@Override
	public Response<Long> sort(String key, String dstkey) {
		return sort(key, new SortingParams(), dstkey);
	}

	private Comparator<String> makeComparator(Collection<String> params) {
		Comparator<String> comparator;
		final int direction = params.contains(Protocol.Keyword.DESC.name().toLowerCase()) ? -1 : 1;
		if (params.contains(Protocol.Keyword.ALPHA.name().toLowerCase())) {
			comparator = new Comparator<String>() {
				public int compare(String o1, String o2) {
					return o1.compareTo(o2) * direction;
				}
			};
		} else {
			comparator = new Comparator<String>() {
				public int compare(String o1, String o2) {
					Long i1, i2;
					try {
						i1 = Long.parseLong(o1);
						i2 = Long.parseLong(o2);
					} catch (NumberFormatException e) {
						throw new JedisDataException("ERR One or more scores can't be converted into double");
					}
					return i1.compareTo(i2) * direction;
				}
			};
		}
		return comparator;
	}

	@Override
	public Response<List<String>> sort(String key, SortingParams sortingParameters) {
		List<String> result = new LinkedList<String>();
		KeyInformation info = keys.get(key);
		if(info != null) {
			switch (info.getType()) {
				case LIST:
					result.addAll(listStorage.get(key));
					break;
				case SET:
					result.addAll(setStorage.get(key));
					break;
				case SORTED_SET:
					throw new RuntimeException("Not implemented");
				default:
					throw new JedisDataException("WRONGTYPE Operation against a key holding the wrong kind of value");
			}
		}

		List<String> params = convertToStrings(sortingParameters.getParams());

		Collections.sort(result, makeComparator(params));

		ArrayList<byte[]> byteResult = new ArrayList<byte[]>(result.size());
		int limitpos = params.indexOf(Protocol.Keyword.LIMIT.name().toLowerCase());
		if(limitpos >=0) {
			int start = Math.max(Integer.parseInt(params.get(limitpos + 1)), 0);
			int end = Math.min(Integer.parseInt(params.get(limitpos + 2)) + start, result.size());
			for(String entry : result.subList(start, end)) {
				byteResult.add(entry.getBytes());
			}
		} else {
			for(String entry : result) {
				byteResult.add(entry.getBytes());
			}
		}

		Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		response.set(byteResult);
		return response;
	}

	@Override
	public Response<Long> sort(String key, SortingParams sortingParameters, String dstkey) {
		List<String> sorted = sort(key, sortingParameters).get();

		del(dstkey);
		keys.put(dstkey, new KeyInformation(KeyType.LIST));
		listStorage.put(dstkey, sorted);

		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) sorted.size());
		return response;
	}

	@Override
	public Response<List<byte[]>> sort(byte[] key) {
		return sort(key, new SortingParams());
	}

	@Override
	public Response<Long> sort(byte[] key, byte[] dstkey) {
		return null;
	}

	@Override
	public Response<List<byte[]>> sort(byte[] key, SortingParams sortingParameters) {
		return null;
	}

	@Override
	public Response<Long> sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
		return null;
	}

	@Override
	public Response<Long> strlen(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final String val = getStringFromStorage(key, false);
		response.set(val == null ? 0L : (long) val.length());
		return response;
	}

	@Override
	public Response<Long> strlen(final byte[] key) {
		return strlen(new String(key));
	}

	@Override
	public Response<Long> del(final String... keys) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		long result = 0L;
		for (String key : keys) {
			result += del(key).get();
		}

		response.set(result);
		return response;
	}

	@Override
	public Response<Long> del(final byte[]... keys) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		long result = 0L;
		for (byte[] key : keys) {
			result += del(key).get();
		}

		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<Long> del(final String key) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		long result = 0L;
		final KeyInformation info = this.keys.remove(key);
		if (info != null) {
			switch (info.getType()) {
				case HASH:
					hashStorage.remove(key);
					break;
				case LIST:
					listStorage.remove(key);
					break;
				case STRING:
				default:
					storage.remove(key);
			}
			++result;
		}
		response.set(result);
		return response;
	}

	@Override
	public Response<Long> del(final byte[] key) {
		return del(new String(key));
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
		final Long result;
		try {
			result = Long.valueOf(val) + value;
		} catch (final NumberFormatException ignored) {
			throw new JedisDataException("ERR value is not an integer or out of range");
		}
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
		final Double result;
		try {
			result = Double.parseDouble(val) + value;
		} catch (final NumberFormatException ignored) {
			throw new JedisDataException("ERR value is not a valid float");
		}
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
	public synchronized Response<Long> lpush(final String key, final String... string) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		List<String> list = getListFromStorage(key, true);
		if (list == null) {
			list = new ArrayList<String>();
			listStorage.put(key, list);
		}
		Collections.addAll(list, string);
		response.set((long) list.size());
		return response;
	}

	@Override
	public Response<Long> lpush(final byte[] key, final byte[]... string) {
		return lpush(new String(key), convertToStrings(string));
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
	public synchronized Response<byte[]> lpop(final byte[] key) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final List<String> list = getListFromStorage(new String(key), true);
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
	public Response<Long> llen(final byte[] key) {
		return llen(new String(key));
	}

	@Override
	public Response<List<String>> lrange(String key, long start, long end) {
		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		final List<String> full = getListFromStorage(key, false);

		final List<byte[]> selected = new ArrayList<byte[]>();
		response.set(selected);

		if(start < 0) { start = Math.max(full.size() + start, 0); }
		if(end < 0)   { end = full.size() + end; }
		if(start > full.size() || start > end) return response;

		end = Math.min(full.size() - 1, end);

		for(int i = (int) start; i <= end; i++) {
			selected.add(full.get(i).getBytes());
		}
		return response;
	}

	@Override
	public Response<List<byte[]>> lrange(byte[] key, long start, long end) {
		final Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
		final List<String> full = getListFromStorage(new String(key), false);

		final List<byte[]> selected = new ArrayList<byte[]>();
		response.set(selected);

		if(start < 0) { start = Math.max(full.size() + start, 0); }
		if(end < 0)   { end = full.size() + end; }
		if(start > full.size() || start > end) return response;

		end = Math.min(full.size() - 1, end);

		for(int i = (int) start; i <= end; i++) {
			selected.add(full.get(i).getBytes());
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

	@Override
	public Response<Set<byte[]>> keys(final byte[] pattern) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		final List<byte[]> result = new ArrayList<byte[]>();
		for (final String key : keys(new String(pattern)).get()) {
			result.add(key.getBytes());
		}

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

	protected boolean createOrUpdateKey(final String key, final KeyType type, final boolean resetTTL) {
		KeyInformation info = keys.get(key);
		if (info == null) {
			info = new KeyInformation(type);
			keys.put(key, info);
			return true;
		} else {
			if (info.getType() != type) {
				throw new JedisDataException("ERR Operation against a key holding the wrong kind of value");
			}
			if (resetTTL) {
				info.setExpiration(-1L);
			}
			return false;
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

    protected Set<String> getSetFromStorage(final String key, boolean createIfNotExist) {
        final KeyInformation info = keys.get(key);
        if (info == null) {
            if (createIfNotExist) {
                createOrUpdateKey(key, KeyType.SET, false);
                Set<String> result = new HashSet<String>();
                setStorage.put(key, result);
                return result;
            }
            return null; // no such key exists
        }
        if (info.getType() != KeyType.SET) {
            throw new JedisDataException("ERR Operation against a key holding the wrong kind of value");
        }
        if (info.isTTLSetAndKeyExpired()) {
            setStorage.remove(key);
            keys.remove(key);
            return null;
        }
        return setStorage.get(key);
    }

	protected static String[] convertToStrings(final byte[][] b) {
		final String[] result = new String[b.length];
		for (int i = 0; i < b.length; ++i) {
			result[i] = new String(b[i]);
		}
		return result;
	}

	protected static List<String> convertToStrings(final Collection<byte[]> collection) {
		final List<String> result = new LinkedList<String>();
		for(byte[] entry : collection) {
			result.add(new String(entry));
		}
		return result;
	}

    @Override
    public synchronized Response<Long> sadd(String key, String... members) {
        final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
        Set<String> set = getSetFromStorage(key, true);

        Long added = 0L;
        for (String s: members) {
            if (set.add(s)) added++;
        }

        response.set(added);
        return response;
    }

	@Override
	public Response<Long> sadd(byte[] key, byte[]... members) {
		return sadd(new String(key), convertToStrings(members));
	}

    @Override
    public synchronized Response<Long> srem(String key, String... members) {
        final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
        Set<String> set = getSetFromStorage(key, true);
        Long removed = 0L;
        for (String s: members) {
            if (set.remove(s)) removed++;
        }
        response.set(removed);
        return response;
    }

	@Override
	public Response<Long> srem(byte[] key, byte[]... members) {
		return srem(new String(key), convertToStrings(members));
	}

	@Override
	public synchronized Response<Long> scard(String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<String> set = getSetFromStorage(key, true);
		response.set((long) set.size());

		return response;
	}

	@Override
	public Response<Long> scard(byte[] key) {
		return scard(new String(key));
	}

	@Override
	public synchronized Response<Set<String>> sdiff(final String... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sdiff' command");
		}
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		final Set<String> firstSet = new HashSet<String>(getSetFromStorage(keys[0], true));
		for (int i = 1; i < keys.length; ++i) {
			final Set<String> set = getSetFromStorage(keys[i], true);
			firstSet.removeAll(set);
		}

		final List<byte[]> builderData = new ArrayList<byte[]>(firstSet.size());
		for (final String value : firstSet) {
			builderData.add(value.getBytes());
		}
		response.set(builderData);

		return response;
	}

	@Override
	public Response<Set<byte[]>> sdiff(final byte[]... keys) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		final Set<String> members = sdiff(convertToStrings(keys)).get();
		final List<byte[]> result = new ArrayList<byte[]>(members.size());
		for (final String member : members) {
			result.add(member.getBytes());
		}
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<Long> sdiffstore(final String dstKey, final String... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sdiff' command");
		}
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<String> diff = sdiff(keys).get();
		final Set<String> dst = getSetFromStorage(dstKey, true);
		if (dst.size() > 0) {
			dst.clear();
		}
		dst.addAll(diff);
		response.set((long) diff.size());

		return response;
	}

	@Override
	public Response<Long> sdiffstore(final byte[] dstKey, final byte[]... keys) {
		return sdiffstore(new String(dstKey), convertToStrings(keys));
	}

	@Override
	public synchronized Response<Set<String>> sinter(final String... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sinter' command");
		}
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		final Set<String> firstSet = new HashSet<String>(getSetFromStorage(keys[0], true));
		for (int i = 1; i < keys.length; ++i) {
			final Set<String> set = getSetFromStorage(keys[i], true);
			firstSet.retainAll(set);
		}

		final List<byte[]> builderData = new ArrayList<byte[]>(firstSet.size());
		for (final String value : firstSet) {
			builderData.add(value.getBytes());
		}
		response.set(builderData);

		return response;
	}

	@Override
	public synchronized Response<Set<byte[]>> sinter(final byte[]... keys) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		final Set<String> members = sinter(convertToStrings(keys)).get();
		final List<byte[]> result = new ArrayList<byte[]>(members.size());
		for (final String member : members) {
			result.add(member.getBytes());
		}
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<Long> sinterstore(final String dstKey, final String... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sinterstore' command");
		}
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<String> inter = sinter(keys).get();
		final Set<String> dst = getSetFromStorage(dstKey, true);
		if (dst.size() > 0) {
			dst.clear();
		}
		dst.addAll(inter);
		response.set((long) inter.size());

		return response;
	}

	@Override
	public Response<Long> sinterstore(final byte[] dstKey, final byte[]... keys) {
		return sinterstore(new String(dstKey), convertToStrings(keys));
	}

	@Override
	public synchronized Response<Boolean> sismember(final String key, final String member) {
		final Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		final Set<String> set = getSetFromStorage(key, false);
		response.set(set != null && set.contains(member) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Boolean> sismember(final byte[] key, final byte[] member) {
		return sismember(new String(key), new String(member));
	}

	@Override
	public synchronized Response<Long> smove(final String srckey, final String dstkey, final String member) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<String> src = getSetFromStorage(srckey, false);
		final Set<String> dst = getSetFromStorage(dstkey, true);
		if (src.remove(member)) {
			dst.add(member);
			response.set(1L);
		} else {
			response.set(0L);
		}

		return response;
	}

	@Override
	public Response<Long> smove(final byte[] srckey, final byte[] dstkey, final byte[] member) {
		return smove(new String(srckey), new String(dstkey), new String(member));
	}

	@Override
	public synchronized Response<String> spop(final String key) {
		final Response<String> response = srandmember(key);
		if (response.get() != null) {
			final Set<String> src = getSetFromStorage(key, false);
			src.remove(response.get());
		}
		return response;
	}

	@Override
	public Response<byte[]> spop(final byte[] key) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final String result = spop(new String(key)).get();
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public synchronized Response<String> srandmember(final String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final Set<String> src = getSetFromStorage(key, false);
		if (src == null) {
			response.set(null);
		} else {
			final String result = getRandomElementFromSet(src);
			response.set(result.getBytes());
		}
		return response;
	}

	@Override
	public Response<byte[]> srandmember(final byte[] key) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final String result = srandmember(new String(key)).get();
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public synchronized Response<Set<String>> smembers(String key) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		Set<String> set = getSetFromStorage(key, true);

		// BuilderFactory.STRING_SET uses a List<byte[]> internally so we can't just send the Set
		List<byte[]> builderData = new ArrayList<byte[]>(set.size());
		for (String s : set) {
			builderData.add(s.getBytes());
		}

		response.set(builderData);

		return response;
	}

	@Override
	public Response<Set<byte[]>> smembers(byte[] key) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		final Set<String> members = smembers(new String(key)).get();
		final List<byte[]> result = new ArrayList<byte[]>(members.size());
		for (final String member : members) {
			result.add(member.getBytes());
		}
		response.set(result);

		return response;
	}

	@Override
	public synchronized Response<Set<String>> sunion(final String... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sunion' command");
		}
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		final Set<String> firstSet = new HashSet<String>(getSetFromStorage(keys[0], true));
		for (int i = 1; i < keys.length; ++i) {
			final Set<String> set = getSetFromStorage(keys[i], true);
			firstSet.addAll(set);
		}

		final List<byte[]> builderData = new ArrayList<byte[]>(firstSet.size());
		for (final String value : firstSet) {
			builderData.add(value.getBytes());
		}
		response.set(builderData);

		return response;
	}

	@Override
	public Response<Set<byte[]>> sunion(final byte[]... keys) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		final Set<String> members = sunion(convertToStrings(keys)).get();
		final List<byte[]> result = new ArrayList<byte[]>(members.size());
		for (final String member : members) {
			result.add(member.getBytes());
		}
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<Long> sunionstore(final String dstKey, final String... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sunionstore' command");
		}
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<String> inter = sunion(keys).get();
		final Set<String> dst = getSetFromStorage(dstKey, true);
		if (dst.size() > 0) {
			dst.clear();
		}
		dst.addAll(inter);
		response.set((long) inter.size());

		return response;
	}

	@Override
	public Response<Long> sunionstore(final byte[] dstKey, final byte[]... keys) {
		return sunionstore(new String(dstKey), convertToStrings(keys));
	}
}

