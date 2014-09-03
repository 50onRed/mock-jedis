package com.fiftyonred.mock_jedis;

import com.fiftyonred.utils.WildcardMatcher;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.util.SafeEncoder;

import java.nio.charset.Charset;
import java.util.*;

public class MockPipeline extends Pipeline {
	private final WildcardMatcher wildcardMatcher = new WildcardMatcher();
	private final List<Map<DataContainer, KeyInformation>> allKeys;
	private final List<Map<DataContainer, DataContainer>> allStorage;
	private final List<Map<DataContainer, Map<DataContainer, DataContainer>>> allHashStorage;
	private final List<Map<DataContainer, List<DataContainer>>> allListStorage;
	private final List<Map<DataContainer, Set<DataContainer>>> allSetStorage;

	private static final int NUM_DBS = 16;
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final byte[] OK_RESPONSE  = "OK".getBytes(CHARSET);

	private int currentDB;
	private Map<DataContainer, KeyInformation> keys;
	private Map<DataContainer, DataContainer> storage;
	private Map<DataContainer, Map<DataContainer, DataContainer>> hashStorage;
	private Map<DataContainer, List<DataContainer>> listStorage;
	private Map<DataContainer, Set<DataContainer>> setStorage;

	public MockPipeline() {
		allKeys = new ArrayList<Map<DataContainer, KeyInformation>>(NUM_DBS);
		allStorage = new ArrayList<Map<DataContainer, DataContainer>>(NUM_DBS);
		allHashStorage = new ArrayList<Map<DataContainer, Map<DataContainer, DataContainer>>>(NUM_DBS);
		allListStorage = new ArrayList<Map<DataContainer, List<DataContainer>>>(NUM_DBS);
		allSetStorage = new ArrayList<Map<DataContainer, Set<DataContainer>>>(NUM_DBS);
		for (int i = 0; i < NUM_DBS; ++i) {
			allKeys.add(new HashMap<DataContainer, KeyInformation>());
			allStorage.add(new HashMap<DataContainer, DataContainer>());
			allHashStorage.add(new HashMap<DataContainer, Map<DataContainer, DataContainer>>());
			allListStorage.add(new HashMap<DataContainer, List<DataContainer>>());
			allSetStorage.add(new HashMap<DataContainer, Set<DataContainer>>());
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
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set("PONG".getBytes(CHARSET));
		return response;
	}

	@Override
	public Response<String> echo(final String string) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(echo(string.getBytes(CHARSET)).get());
		return response;
	}

	@Override
	public Response<byte[]> echo(final byte[] string) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		response.set(string);
		return response;
	}

	@Override
	public synchronized Response<Long> dbSize() {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) keys.size());
		return response;
	}

	@Override
	public synchronized Response<String> flushAll() {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		for (int dbNum = 0; dbNum < NUM_DBS; ++dbNum) {
			allKeys.get(dbNum).clear();
			allStorage.get(dbNum).clear();
			allHashStorage.get(dbNum).clear();
			allListStorage.get(dbNum).clear();
		}
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public synchronized Response<String> flushDB() {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		keys.clear();
		storage.clear();
		hashStorage.clear();
		listStorage.clear();
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public synchronized Response<String> rename(final String oldkey, final String newkey) {
		return rename(DataContainer.from(oldkey), DataContainer.from(newkey));
	}

	@Override
	public Response<String> rename(final byte[] oldkey, final byte[] newkey) {
		return rename(DataContainer.from(oldkey), DataContainer.from(newkey));
	}

	private Response<String> rename(final DataContainer oldkey, final DataContainer newkey) {
		if (oldkey.equals(newkey)) {
			throw new JedisDataException("ERR source and destination objects are the same");
		}
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
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
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public synchronized Response<Long> renamenx(final String oldkey, final String newkey) {
		return renamenx(DataContainer.from(oldkey), DataContainer.from(newkey));
	}

	@Override
	public Response<Long> renamenx(final byte[] oldkey, final byte[] newkey) {
		return renamenx(DataContainer.from(oldkey), DataContainer.from(newkey));
	}

	private Response<Long> renamenx(final DataContainer oldkey, final DataContainer newkey) {
		if (oldkey.equals(newkey)) {
			throw new JedisDataException("ERR source and destination objects are the same");
		}
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
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
	public synchronized Response<String> set(final String key, final String value) {
		return set(DataContainer.from(key), DataContainer.from(value));
	}

	@Override
	public synchronized Response<String> set(final byte[] key, final byte[] value) {
		return set(DataContainer.from(key), DataContainer.from(value));
	}

	private Response<String> set(final DataContainer key, final DataContainer value) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		createOrUpdateKey(key, KeyType.STRING, true);
		storage.put(key, value);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public synchronized Response<Long> setnx(final String key, final String value) {
		return setnx(DataContainer.from(key), DataContainer.from(value));
	}

	@Override
	public synchronized Response<Long> setnx(final byte[] key, final byte[] value) {
		return setnx(DataContainer.from(key), DataContainer.from(value));
	}

	private Response<Long> setnx(final DataContainer key, final DataContainer value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final DataContainer result = getContainerFromStorage(key, false);
		if (result == null) {
			set(key, value);
			response.set(1L);
		} else {
			response.set(0L);
		}

		return response;
	}

	@Override
	public synchronized Response<String> get(final String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final DataContainer val = getContainerFromStorage(DataContainer.from(key), false);
		response.set(DataContainer.toBytes(val));
		return response;
	}

	@Override
	public synchronized Response<byte[]> get(final byte[] key) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final DataContainer val = getContainerFromStorage(DataContainer.from(key), false);
		response.set(DataContainer.toBytes(val));
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
	public Response<byte[]> dump(final byte[] key) {
		return get(key);
	}

	@Override
	public Response<byte[]> dump(final String key) {
		return get(key.getBytes());
	}

	@Override
	public Response<String> restore(final String key, final int ttl, final byte[] serializedValue) {
		return setex(key.getBytes(CHARSET), ttl, serializedValue);
	}

	@Override
	public Response<String> restore(final byte[] key, final int ttl, final byte[] serializedValue) {
		return setex(key, ttl, serializedValue);
	}

	@Override
	public synchronized Response<Boolean> exists(final String key) {
		return exists(DataContainer.from(key));
	}

	@Override
	public synchronized Response<Boolean> exists(final byte[] key) {
		return exists(DataContainer.from(key));
	}

	private Response<Boolean> exists(final DataContainer key) {
		final Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		response.set(keys.containsKey(key) ? 1L : 0L);
		return response;
	}

	@Override
	public synchronized Response<String> type(final String key) {
		return type(DataContainer.from(key));
	}

	@Override
	public synchronized Response<String> type(final byte[] key) {
		return type(DataContainer.from(key));
	}

	private Response<String> type(final DataContainer key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final KeyInformation info = keys.get(key);
		if (info != null && info.getType() == KeyType.STRING) {
			response.set("string".getBytes(CHARSET));
		} else if (info != null && info.getType() == KeyType.LIST) {
			response.set("list".getBytes(CHARSET));
		} else if (info != null && info.getType() == KeyType.SET) {
			response.set("set".getBytes(CHARSET));
		} else {
			response.set("none".getBytes(CHARSET));
		}
		return response;
	}

	@Override
	public synchronized Response<Long> move(final String key, final int dbIndex) {
		return move(DataContainer.from(key), dbIndex);
	}

	@Override
	public synchronized Response<Long> move(final byte[] key, final int dbIndex) {
		return move(DataContainer.from(key), dbIndex);
	}

	private Response<Long> move(final DataContainer key, final int dbIndex) {
		if (dbIndex < 0 || dbIndex >= NUM_DBS) {
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
					case STRING:
					default:
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
	public synchronized Response<String> randomKey() {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		if (keys.isEmpty()) {
			response.set(null);
		} else {
			final DataContainer result = getRandomElementFromSet(keys.keySet());
			response.set(result.getBytes());
		}
		return response;
	}

	@Override
	public Response<byte[]> randomKeyBinary() {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final String result = randomKey().get();
		response.set(result == null ? null : result.getBytes(CHARSET));
		return response;
	}

	@Override
	public Response<String> select(final int dbIndex) {
		if (dbIndex < 0 || dbIndex >= NUM_DBS) {
			throw new JedisDataException("ERR invalid DB index");
		}
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		currentDB = dbIndex;
		keys = allKeys.get(dbIndex);
		storage = allStorage.get(dbIndex);
		hashStorage = allHashStorage.get(dbIndex);
		listStorage = allListStorage.get(dbIndex);
		setStorage = allSetStorage.get(dbIndex);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<String> setex(final String key, final int seconds, final String value) {
		return psetex(key, seconds * 1000, value);
	}

	@Override
	public Response<String> setex(final byte[] key, final int seconds, final byte[] value) {
		return psetex(key, seconds * 1000, value);
	}

	@Override
	public synchronized Response<String> psetex(final String key, final int milliseconds, final String value) {
		final Response<String> response = set(key, value);
		pexpire(key, milliseconds);
		return response;
	}

	@Override
	public Response<String> psetex(final byte[] key, final int milliseconds, final byte[] value) {
		final Response<String> response = set(key, value);
		pexpire(key, milliseconds);
		return response;
	}

	@Override
	public Response<Long> expire(final String key, final int seconds) {
		return expireAt(key, System.currentTimeMillis() / 1000 + seconds);
	}

	@Override
	public Response<Long> expire(final byte[] key, final int seconds) {
		return expireAt(key, System.currentTimeMillis() / 1000 + seconds);
	}

	@Override
	public Response<Long> expireAt(final String key, final long unixTime) {
		return pexpireAt(key, unixTime * 1000L);
	}

	@Override
	public Response<Long> expireAt(final byte[] key, final long unixTime) {
		return pexpireAt(key, unixTime * 1000L);
	}

	@Override
	public Response<Long> pexpire(final String key, final int milliseconds) {
		return pexpireAt(key, System.currentTimeMillis() + milliseconds);
	}

	@Override
	public Response<Long> pexpire(final byte[] key, final int milliseconds) {
		return pexpireAt(key, System.currentTimeMillis() + milliseconds);
	}

	@Override
	public Response<Long> pexpire(final String key, final long milliseconds) {
		return pexpireAt(key, System.currentTimeMillis() + milliseconds);
	}

	@Override
	public Response<Long> pexpire(final byte[] key, final long milliseconds) {
		return pexpireAt(key, System.currentTimeMillis() + milliseconds);
	}

	@Override
	public synchronized Response<Long> pexpireAt(final String key, final long millisecondsTimestamp) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(pexpireAt(DataContainer.from(key), millisecondsTimestamp));
		return response;
	}

	@Override
	public synchronized Response<Long> pexpireAt(final byte[] key, final long millisecondsTimestamp) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(pexpireAt(DataContainer.from(key), millisecondsTimestamp));
		return response;
	}

	public synchronized Long pexpireAt(final DataContainer key, final long millisecondsTimestamp) {
		final KeyInformation info = keys.get(key);
		if (info == null || info.isTTLSetAndKeyExpired()) {
			return 0L;
		} else {
			info.setExpiration(millisecondsTimestamp);
			return 1L;
		}
	}

	@Override
	public Response<Long> ttl(final String key) {
		return ttl(DataContainer.from(key));
	}

	@Override
	public Response<Long> ttl(final byte[] key) {
		return ttl(DataContainer.from(key));
	}

	public Response<Long> ttl(final DataContainer key) {
		Long pttlInResponse = pttl(key).get();

		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
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
		return append(DataContainer.from(key), DataContainer.from(value));
	}

	@Override
	public synchronized Response<Long> append(final byte[] key, final byte[] value) {
		return append(DataContainer.from(key), DataContainer.from(value));
	}

	private Response<Long> append(final DataContainer key, final DataContainer value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		DataContainer container = getContainerFromStorage(key, true);
		final DataContainer newVal = container.append(value);
		set(key, newVal);
		response.set((long) newVal.getString().length());
		return response;
	}

	@Override
	public synchronized Response<Long> pttl(final String key) {
		return pttl(DataContainer.from(key));
	}

	private Response<Long> pttl(final DataContainer key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final KeyInformation info = keys.get(key);
		response.set(info == null ? -1L : info.getTTL());
		return response;
	}

	@Override
	public synchronized Response<Long> persist(final String key) {
		return persist(DataContainer.from(key));
	}

	@Override
	public synchronized Response<Long> persist(final byte[] key) {
		return persist(DataContainer.from(key));
	}

	private Response<Long> persist(final DataContainer key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final KeyInformation info = keys.get(key);
		if (info.getTTL() == -1) {
			response.set(0L);
		} else {
			info.setExpiration(-1L);
			response.set(1L);
		}
		return response;
	}

	@Override
	public synchronized Response<List<String>> mget(final String... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'mget' command");
		}

		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);

		final List<byte[]> result = new ArrayList<byte[]>(keys.length);
		for (final String key : keys) {
			final DataContainer val = getContainerFromStorage(DataContainer.from(key), false);
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

		final List<byte[]> result = new ArrayList<byte[]>(keys.length);
		for (final byte[] key : keys) {
			final DataContainer val = getContainerFromStorage(DataContainer.from(key), false);
			result.add(val == null ? null : val.getBytes());
		}
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<String> mset(final String... keysvalues) {
		final int l = keysvalues.length;
		if (l <= 0 || l % 2 != 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'mset' command");
		}

		for (int i = 0; i < l; i += 2) {
			set(keysvalues[i], keysvalues[i + 1]);
		}

		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<String> mset(final byte[]... keysvalues) {
		return mset(convertToStrings(keysvalues));
	}

	@Override
	public synchronized Response<Long> msetnx(final String... keysvalues) {
		final int l = keysvalues.length;
		if (l <= 0 || l % 2 != 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'msetnx' command");
		}

		long result = 1L;
		for (int i = 0; i < l; i += 2) {
			if (setnx(keysvalues[i], keysvalues[i + 1]).get() == 0L) {
				result = 0L;
			}
		}

		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(result);
		return response;
	}

	@Override
	public Response<Long> msetnx(final byte[]... keysvalues) {
		return msetnx(convertToStrings(keysvalues));
	}

	@Override
	public Response<Long> decr(final String key) {
		return decrBy(key, 1L);
	}

	@Override
	public Response<Long> decr(final byte[] key) {
		return decrBy(key, 1L);
	}

	@Override
	public Response<Long> decrBy(final String key, final long integer) {
		return incrBy(key, -integer);
	}

	@Override
	public Response<Long> decrBy(final byte[] key, final long integer) {
		return incrBy(key, -integer);
	}

	@Override
	public Response<Long> incr(final String key) {
		return incrBy(key, 1L);
	}

	@Override
	public Response<Long> incr(final byte[] key) {
		return incrBy(key, 1L);
	}

	@Override
	public synchronized Response<Long> incrBy(final String key, final long integer) {
		return incrBy(DataContainer.from(key), integer);
	}

	@Override
	public Response<Long> incrBy(final byte[] key, final long integer) {
		return incrBy(DataContainer.from(key), integer);
	}

	private Response<Long> incrBy(final DataContainer key, final long integer) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final DataContainer val = getContainerFromStorage(key, true);

		final long oldValue;
		try {
			oldValue = val == null || val.getString().isEmpty() ? 0L : Long.parseLong(val.getString());
		} catch (final NumberFormatException ignored) {
			throw new JedisDataException("ERR value is not an integer or out of range");
		}

		// check for overflow
		if (oldValue > 0L ? integer > Long.MAX_VALUE - oldValue : integer < Long.MIN_VALUE - oldValue) {
			throw new JedisDataException("ERR value is not an integer or out of range");
		}

		final long result = oldValue + integer;
		storage.put(key, DataContainer.from(Long.toString(result)));
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<Double> incrByFloat(final String key, final double increment) {
		return incrByFloat(DataContainer.from(key), increment);
	}

	@Override
	public Response<Double> incrByFloat(final byte[] key, final double increment) {
		return incrByFloat(DataContainer.from(key), increment);
	}

	public synchronized Response<Double> incrByFloat(final DataContainer key, final double increment) {
		final Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
		final DataContainer val = getContainerFromStorage(key, true);
		final Double result;
		try {
			result = val == null || val.getString().isEmpty() ? increment : Double.parseDouble(val.getString()) + increment;
		} catch (final NumberFormatException ignored) {
			throw new JedisDataException("ERR value is not a valid float");
		}
		storage.put(key, DataContainer.from(result.toString()));
		response.set(result.toString().getBytes(CHARSET));
		return response;
	}

	@Override
	public Response<List<String>> sort(final String key) {
		return sort(key, new SortingParams());
	}

	@Override
	public Response<Long> sort(final String key, final String dstkey) {
		return sort(key, new SortingParams(), dstkey);
	}

	@Override
	public Response<Long> sort(final byte[] key, final byte[] dstkey) {
		return sort(key, new SortingParams(), dstkey);
	}

	private static Comparator<DataContainer> makeComparator(final Collection<String> params) {
		final Comparator<DataContainer> comparator;
		final int direction = params.contains(Protocol.Keyword.DESC.name().toLowerCase()) ? -1 : 1;
		if (params.contains(Protocol.Keyword.ALPHA.name().toLowerCase())) {
			comparator = new Comparator<DataContainer>() {
				@Override
				public int compare(final DataContainer o1, final DataContainer o2) {
					return o1.compareTo(o2) * direction;
				}
			};
		} else {
			comparator = new Comparator<DataContainer>() {
				@Override
				public int compare(final DataContainer o1, final DataContainer o2) {
					final Long i1, i2;
					try {
						i1 = Long.parseLong(o1.getString());
						i2 = Long.parseLong(o2.getString());
					} catch (final NumberFormatException e) {
						throw new JedisDataException("ERR One or more scores can't be converted into double");
					}
					return i1.compareTo(i2) * direction;
				}
			};
		}
		return comparator;
	}

	@Override
	public Response<List<String>> sort(final String key, final SortingParams sortingParameters) {
		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		List<DataContainer> sortedList = sort(DataContainer.from(key), sortingParameters);
		response.set(DataContainer.toBytes(sortedList));
		return response;
	}

	@Override
	public Response<List<byte[]>> sort(final byte[] key, final SortingParams sortingParameters) {
		final Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
		List<DataContainer> sortedList = sort(DataContainer.from(key), sortingParameters);
		response.set(DataContainer.toBytes(sortedList));
		return response;
	}

	public List<DataContainer> sort(final DataContainer key, final SortingParams sortingParameters) {
		final List<DataContainer> result = new ArrayList<DataContainer>();
		final KeyInformation info = keys.get(key);
		if (info != null) {
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

		final List<String> params = convertToStrings(sortingParameters.getParams());

		Collections.sort(result, makeComparator(params));

		final List<DataContainer> filteredResult = new ArrayList<DataContainer>(result.size());
		final int limitpos = params.indexOf(Protocol.Keyword.LIMIT.name().toLowerCase());
		if (limitpos >= 0) {
			final int start = Math.max(Integer.parseInt(params.get(limitpos + 1)), 0);
			final int end = Math.min(Integer.parseInt(params.get(limitpos + 2)) + start, result.size());
			for (final DataContainer entry : result.subList(start, end)) {
				filteredResult.add(entry);
			}
		} else {
			for (final DataContainer entry : result) {
				filteredResult.add(entry);
			}
		}
		return filteredResult;
	}

	@Override
	public synchronized Response<Long> sort(final String key, final SortingParams sortingParameters, final String dstkey) {
		return sort(DataContainer.from(key), sortingParameters, DataContainer.from(dstkey));
	}

	@Override
	public Response<Long> sort(final byte[] key, final SortingParams sortingParameters, final byte[] dstkey) {
		return sort(DataContainer.from(key), sortingParameters, DataContainer.from(dstkey));
	}

	public synchronized Response<Long> sort(final DataContainer key, final SortingParams sortingParameters, final DataContainer dstkey) {
		List<DataContainer> sorted = sort(key, sortingParameters);

		del(dstkey);
		keys.put(dstkey, new KeyInformation(KeyType.LIST));
		listStorage.put(dstkey, sorted);

		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) sorted.size());
		return response;
	}

	@Override
	public Response<List<byte[]>> sort(final byte[] key) {
		return sort(key, new SortingParams());
	}

	@Override
	public Response<Long> strlen(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final DataContainer val = getContainerFromStorage(DataContainer.from(key), false);
		response.set(val == null ? 0L : val.getString().length());
		return response;
	}

	@Override
	public Response<Long> strlen(final byte[] key) {
		return strlen(new String(key));
	}

	@Override
	public Response<Long> del(final String... keys) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		long result = 0L;
		for (final String key : keys) {
			result += del(key).get();
		}

		response.set(result);
		return response;
	}

	@Override
	public Response<Long> del(final byte[]... keys) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		long result = 0L;
		for (final byte[] key : keys) {
			result += del(key).get();
		}

		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<Long> del(final String key) {
		return del(DataContainer.from(key));
	}

	@Override
	public synchronized Response<Long> del(final byte[] key) {
		return del(DataContainer.from(key));
	}

	private Response<Long> del(final DataContainer key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
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
	public synchronized Response<String> hget(final String key, final String field) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final Map<DataContainer, DataContainer> result = getHashFromStorage(DataContainer.from(key), false);
		if (result == null) {
			response.set(null);
			return response;
		}
		final DataContainer fieldValue = result.get(DataContainer.from(field));
		response.set(DataContainer.toBytes(fieldValue));
		return response;
	}

	@Override
	public synchronized Response<byte[]> hget(final byte[] key, final byte[] field) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final Map<DataContainer, DataContainer> result = getHashFromStorage(DataContainer.from(key), false);
		if (result == null) {
			response.set(null);
			return response;
		}
		final DataContainer fieldValue = result.get(DataContainer.from(field));
		response.set(DataContainer.toBytes(fieldValue));
		return response;
	}

	@Override
	public synchronized Response<Map<String, String>> hgetAll(final String key) {
		final Response<Map<String, String>> response = new Response<Map<String, String>>(BuilderFactory.STRING_MAP);
		final Map<DataContainer, DataContainer> result = getHashFromStorage(DataContainer.from(key), false);

		if (result != null) {
			final List<byte[]> encodedResult = new ArrayList<byte[]>(result.size());
			for (final Map.Entry<DataContainer, DataContainer> e : result.entrySet()) {
				encodedResult.add(e.getKey().getBytes());
				encodedResult.add(e.getValue().getBytes());
			}
			response.set(encodedResult);
		} else {
			response.set(new ArrayList<byte[]>(0));
		}
		return response;
	}

	@Override
	public synchronized Response<Map<byte[], byte[]>> hgetAll(final byte[] key) {
		final Response<Map<byte[], byte[]>> response = new Response<Map<byte[], byte[]>>(BuilderFactory.BYTE_ARRAY_MAP);
		final Map<DataContainer, DataContainer> result = getHashFromStorage(DataContainer.from(key), false);

		if (result != null) {
			final List<byte[]> encodedResult = new ArrayList<byte[]>(result.size());
			for (final Map.Entry<DataContainer, DataContainer> e : result.entrySet()) {
				encodedResult.add(e.getKey().getBytes());
				encodedResult.add(e.getValue().getBytes());
			}
			response.set(encodedResult);
		} else {
			response.set(new ArrayList<byte[]>(0));
		}
		return response;
	}

	@Override
	public synchronized Response<Set<String>> hkeys(final String key) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		final Map<DataContainer, DataContainer> result = getHashFromStorage(DataContainer.from(key), false);

		if (result != null) {
			response.set(DataContainer.toBytes(result.keySet()));
		} else {
			response.set(new ArrayList<byte[]>());
		}
		return response;
	}

	@Override
	public synchronized Response<Set<byte[]>> hkeys(final byte[] key) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		final Map<DataContainer, DataContainer> result = getHashFromStorage(DataContainer.from(key), false);

		if (result != null) {
			response.set(DataContainer.toBytes(result.keySet()));
		} else {
			response.set(new ArrayList<byte[]>());
		}
		return response;
	}

	@Override
	public synchronized Response<List<String>> hvals(final String key) {
		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		final Map<DataContainer, DataContainer> result = getHashFromStorage(DataContainer.from(key), false);

		if (result != null) {
			response.set(DataContainer.toBytes(result.values()));
		} else {
			response.set(new ArrayList<byte[]>());
		}

		return response;
	}

	@Override
	public synchronized Response<List<byte[]>> hvals(final byte[] key) {
		final Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
		final Map<DataContainer, DataContainer> result = getHashFromStorage(DataContainer.from(key), false);

		if (result != null) {
			response.set(DataContainer.toBytes(result.values()));
		} else {
			response.set(new ArrayList<byte[]>());
		}

		return response;
	}

	@Override
	public synchronized Response<Long> hset(final String key, final String field, final String value) {
		return hset(DataContainer.from(key), DataContainer.from(field), DataContainer.from(value));

	}

	@Override
	public synchronized Response<Long> hset(final byte[] key, final byte[] field, final byte[] value) {
		return hset(DataContainer.from(key), DataContainer.from(field), DataContainer.from(value));
	}

	private Response<Long> hset(final DataContainer key, final DataContainer field, final DataContainer value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);
		response.set(m.containsKey(field) ? 0L : 1L);
		m.put(field, value);

		return response;
	}

	@Override
	public synchronized Response<Long> hsetnx(final String key, final String field, final String value) {
		return hsetnx(DataContainer.from(key), DataContainer.from(field), DataContainer.from(value));
	}

	@Override
	public synchronized Response<Long> hsetnx(final byte[] key, final byte[] field, final byte[] value) {
		return hsetnx(DataContainer.from(key), DataContainer.from(field), DataContainer.from(value));
	}

	private Response<Long> hsetnx(final DataContainer key, final DataContainer field, final DataContainer value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);
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
		final Map<DataContainer, DataContainer> hash = getHashFromStorage(DataContainer.from(key), false);
		if (hash == null) {
			for (final String ignored : fields) {
				result.add(null);
			}
			response.set(result);
			return response;
		}

		for (final String field : fields) {
			final DataContainer v = hash.get(DataContainer.from(field));
			result.add(DataContainer.toBytes(v));
		}
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<List<byte[]>> hmget(final byte[] key, final byte[]... fields) {
		final Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
		final List<byte[]> result = new ArrayList<byte[]>();
		final Map<DataContainer, DataContainer> hash = getHashFromStorage(DataContainer.from(key), false);
		if (hash == null) {
			for (final byte[] ignored : fields) {
				result.add(null);
			}
			response.set(result);
			return response;
		}

		for (final byte[] field : fields) {
			final DataContainer v = hash.get(DataContainer.from(field));
			result.add(DataContainer.toBytes(v));
		}
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<String> hmset(final String key, final Map<String, String> hash) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final Map<DataContainer, DataContainer> m = getHashFromStorage(DataContainer.from(key), true);
		for (final Map.Entry<String, String> e : hash.entrySet()) {
			m.put(DataContainer.from(e.getKey()), DataContainer.from(e.getValue()));
		}
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public synchronized Response<String> hmset(final byte[] key, final Map<byte[], byte[]> hash) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final Map<DataContainer, DataContainer> m = getHashFromStorage(DataContainer.from(key), true);
		for (final Map.Entry<byte[], byte[]> e : hash.entrySet()) {
			m.put(DataContainer.from(e.getKey()), DataContainer.from(e.getValue()));
		}
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public synchronized Response<Long> hincrBy(final String key, final String field, final long value) {
		return hincrBy(DataContainer.from(key), DataContainer.from(field), value);
	}

	@Override
	public synchronized Response<Long> hincrBy(final byte[] key, final byte[] field, final long value) {
		return hincrBy(DataContainer.from(key), DataContainer.from(field), value);
	}

	private Response<Long> hincrBy(final DataContainer key, final DataContainer field, final long value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);

		DataContainer val = m.get(field);
		if (val == null) {
			val = DataContainer.from(Long.valueOf(0L).toString());
		}
		final Long result;
		try {
			result = Long.valueOf(val.getString()) + value;
		} catch (final NumberFormatException ignored) {
			throw new JedisDataException("ERR value is not an integer or out of range");
		}
		m.put(field, DataContainer.from(result.toString()));
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<Double> hincrByFloat(final String key, final String field, final double increment) {
		return hincrByFloat(DataContainer.from(key), DataContainer.from(field), increment);
	}

	@Override
	public synchronized Response<Double> hincrByFloat(final byte[] key, final byte[] field, final double increment) {
		return hincrByFloat(DataContainer.from(key), DataContainer.from(field), increment);
	}

	private Response<Double> hincrByFloat(final DataContainer key, final DataContainer field, final double increment) {
		final Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
		final Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);

		DataContainer val = m.get(field);
		if (val == null) {
			val = DataContainer.from(Double.valueOf(0.0D).toString());
		}
		final Double result;
		try {
			result = Double.parseDouble(val.toString()) + increment;
		} catch (final NumberFormatException ignored) {
			throw new JedisDataException("ERR value is not a valid float");
		}
		DataContainer resultContainer = DataContainer.from(result.toString());
		m.put(field, resultContainer);
		response.set(resultContainer.getBytes());
		return response;
	}


	@Override
	public synchronized Response<Long> hdel(final String key, final String... field) {
		return hdel(DataContainer.from(key), DataContainer.from(field));
	}

	@Override
	public synchronized Response<Long> hdel(final byte[] key, final byte[]... field) {
		return hdel(DataContainer.from(key), DataContainer.from(field));
	}

	private Response<Long> hdel(final DataContainer key, final DataContainer... fields) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);
		long result = 0L;
		for (final DataContainer currentField : fields) {
			if (m.remove(currentField) != null) {
				++result;
			}
		}
		response.set(result);

		return response;
	}

	@Override
	public synchronized Response<Boolean> hexists(final String key, final String field) {
		return hexists(DataContainer.from(key), DataContainer.from(field));
	}

	@Override
	public synchronized Response<Boolean> hexists(final byte[] key, final byte[] field) {
		return hexists(DataContainer.from(key), DataContainer.from(field));
	}

	private Response<Boolean> hexists(final DataContainer key, final DataContainer field) {
		final Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		final Map<DataContainer, DataContainer> hash = getHashFromStorage(key, false);
		response.set(hash != null && hash.containsKey(field) ? 1L : 0L);

		return response;
	}


	@Override
	public synchronized Response<Long> hlen(final String key) {
		return hlen(DataContainer.from(key));
	}

	@Override
	public synchronized Response<Long> hlen(final byte[] key) {
		return hlen(DataContainer.from(key));
	}

	private Response<Long> hlen(final DataContainer key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<DataContainer, DataContainer> hash = getHashFromStorage(key, false);
		if (hash != null) {
			response.set((long) hash.size());
		} else {
			response.set(0L);
		}

		return response;
	}

	@Override
	public synchronized Response<Long> lpush(final String key, final String... string) {
		return lpush(DataContainer.from(key), DataContainer.from(string));
	}

	@Override
	public synchronized Response<Long> lpush(final byte[] key, final byte[]... string) {
		return lpush(DataContainer.from(key), DataContainer.from(string));
	}

	private Response<Long> lpush(final DataContainer key, final DataContainer... string) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		List<DataContainer> list = getListFromStorage(key, true);
		if (list == null) {
			list = new ArrayList<DataContainer>();
			listStorage.put(key, list);
		}
		Collections.addAll(list, string);
		response.set((long) list.size());
		return response;
	}

	@Override
	public synchronized Response<String> lpop(final String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final List<DataContainer> list = getListFromStorage(DataContainer.from(key), true);
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
		final List<DataContainer> list = getListFromStorage(DataContainer.from(key), true);
		if (list == null || list.isEmpty()) {
			response.set(null);
		} else {
			response.set(list.remove(list.size() - 1).getBytes());
		}
		return response;
	}

	@Override
	public synchronized Response<Long> llen(final String key) {
		return llen(DataContainer.from(key));
	}

	@Override
	public Response<Long> llen(final byte[] key) {
		return llen(DataContainer.from(key));
	}

	public synchronized Response<Long> llen(final DataContainer key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final List<DataContainer> list = getListFromStorage(key, false);
		if (list == null) {
			response.set(0L);
		} else {
			response.set((long) list.size());
		}
		return response;
	}

	@Override
	public Response<List<String>> lrange(final String key, long start, long end) {
		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		List<DataContainer> result = lrange(DataContainer.from(key), start, end);
		response.set(DataContainer.toBytes(result));
		return response;
	}

	@Override
	public Response<List<byte[]>> lrange(final byte[] key, long start, long end) {
		final Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
		List<DataContainer> result = lrange(DataContainer.from(key), start, end);
		response.set(DataContainer.toBytes(result));
		return response;
	}

	public List<DataContainer> lrange(final DataContainer key, long start, long end) {
		final List<DataContainer> full = getListFromStorage(key, false);

		List<DataContainer> result = new ArrayList<DataContainer>();

		if (start < 0L) {
			start = Math.max(full.size() + start, 0L);
		}
		if (end < 0L) {
			end = full.size() + end;
		}
		if (start > full.size() || start > end) {
			return Collections.emptyList();
		}

		end = Math.min(full.size() - 1, end);

		for (int i = (int) start; i <= end; i++) {
			result.add(full.get(i));
		}
		return result;
	}

	@Override
	public void sync() {
		// do nothing
	}

	@Override
	public synchronized Response<Set<String>> keys(final String pattern) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		Set<DataContainer> result = keys(DataContainer.from(pattern));
		response.set(DataContainer.toBytes(result));
		return response;
	}

	@Override
	public synchronized Response<Set<byte[]>> keys(final byte[] pattern) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		Set<DataContainer> result = keys(DataContainer.from(pattern));
		response.set(DataContainer.toBytes(result));
		return response;
	}

	private Set<DataContainer> keys(final DataContainer pattern) {
		Set<DataContainer> result = new HashSet<DataContainer>();
		for (final DataContainer key : keys.keySet()) {
			if (wildcardMatcher.match(key.getString(), pattern.getString())) {
				result.add(key);
			}
		}
		return result;
	}

	protected boolean createOrUpdateKey(final DataContainer key, final KeyType type, final boolean resetTTL) {
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

	protected DataContainer getContainerFromStorage(final DataContainer key, final boolean createIfNotExist) {
		final KeyInformation info = keys.get(key);
		if (info == null) {
			if (createIfNotExist) {
				createOrUpdateKey(key, KeyType.STRING, true);
				DataContainer container = DataContainer.from("");
				storage.put(key, container);
				return container;
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

	protected Map<DataContainer, DataContainer> getHashFromStorage(final DataContainer key, final boolean createIfNotExist) {
		final KeyInformation info = keys.get(key);
		if (info == null) {
			if (createIfNotExist) {
				createOrUpdateKey(key, KeyType.HASH, false);
				final Map<DataContainer, DataContainer> result = new HashMap<DataContainer, DataContainer>();
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

	protected List<DataContainer> getListFromStorage(final DataContainer key, final boolean createIfNotExist) {
		final KeyInformation info = keys.get(key);
		if (info == null) {
			if (createIfNotExist) {
				createOrUpdateKey(key, KeyType.LIST, false);
				final List<DataContainer> result = new ArrayList<DataContainer>();
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

	protected Set<DataContainer> getSetFromStorage(final DataContainer key, final boolean createIfNotExist) {
		final KeyInformation info = keys.get(key);
		if (info == null) {
			if (createIfNotExist) {
				createOrUpdateKey(key, KeyType.SET, false);
				final Set<DataContainer> result = new HashSet<DataContainer>();
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
		final int l = b.length;
		final String[] result = new String[l];
		for (int i = 0; i < l; ++i) {
			result[i] = new String(b[i], CHARSET);
		}
		return result;
	}

	protected static List<String> convertToStrings(final Collection<byte[]> collection) {
		final List<String> result = new LinkedList<String>();
		for (final byte[] entry : collection) {
			result.add(new String(entry, CHARSET));
		}
		return result;
	}

	@Override
	public synchronized Response<Long> sadd(final String key, final String... member) {
		return sadd(DataContainer.from(key), DataContainer.from(member));
	}

	@Override
	public synchronized Response<Long> sadd(final byte[] key, final byte[]... member) {
		return sadd(DataContainer.from(key), DataContainer.from(member));
	}

	private Response<Long> sadd(final DataContainer key, final DataContainer... members) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<DataContainer> set = getSetFromStorage(key, true);

		Long added = 0L;
		for (final DataContainer s : members) {
			if (set.add(s)) {
				added++;
			}
		}

		response.set(added);
		return response;
	}

	@Override
	public synchronized Response<Long> srem(final String key, final String... member) {
		return srem(DataContainer.from(key), DataContainer.from(member));
	}

	@Override
	public synchronized Response<Long> srem(final byte[] key, final byte[]... member) {
		return srem(DataContainer.from(key), DataContainer.from(member));
	}

	private Response<Long> srem(final DataContainer key, final DataContainer... member) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<DataContainer> set = getSetFromStorage(key, true);
		Long removed = 0L;
		for (final DataContainer s : member) {
			if (set.remove(s)) {
				removed++;
			}
		}
		response.set(removed);
		return response;
	}


	@Override
	public synchronized Response<Long> scard(final String key) {
		return scard(DataContainer.from(key));
	}

	@Override
	public synchronized Response<Long> scard(final byte[] key) {
		return scard(DataContainer.from(key));
	}

	private Response<Long> scard(final DataContainer key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<DataContainer> set = getSetFromStorage(key, true);
		response.set((long) set.size());

		return response;
	}

	@Override
	public synchronized Response<Set<String>> sdiff(final String... keys) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		DataContainer[] keyContainers = DataContainer.from(keys);
		Collection<DataContainer> collection = sinter(keyContainers);
		List<byte[]> data = DataContainer.toBytes(collection);
		response.set(data);
		return response;
	}

	@Override
	public synchronized Response<Set<byte[]>> sdiff(final byte[]... keys) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		DataContainer[] keyContainers = DataContainer.from(keys);
		Collection<DataContainer> collection = sinter(keyContainers);
		List<byte[]> data = DataContainer.toBytes(collection);
		response.set(data);
		return response;
	}

	private Set<DataContainer> sdiff(final DataContainer... keys) {
		final int l = keys.length;
		if (l <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sdiff' command");
		}
		final Set<DataContainer> result = new HashSet<DataContainer>(getSetFromStorage(keys[0], true));
		for (int i = 1; i < l; ++i) {
			final Set<DataContainer> set = getSetFromStorage(keys[i], true);
			result.removeAll(set);
		}

		return result;
	}

	@Override
	public synchronized Response<Long> sdiffstore(final String dstKey, final String... keys) {
		return sdiffstore(DataContainer.from(dstKey), DataContainer.from(keys));
	}

	@Override
	public synchronized Response<Long> sdiffstore(final byte[] dstKey, final byte[]... keys) {
		return sdiffstore(DataContainer.from(dstKey), DataContainer.from(keys));
	}

	private Response<Long> sdiffstore(final DataContainer dstKey, final DataContainer... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sdiff' command");
		}
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<DataContainer> diff = sdiff(keys);
		final Set<DataContainer> dst = getSetFromStorage(dstKey, true);
		if (!dst.isEmpty()) {
			dst.clear();
		}
		dst.addAll(diff);
		response.set((long) diff.size());

		return response;
	}

	@Override
	public synchronized Response<Set<String>> sinter(final String... keys) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		DataContainer[] keyContainers = DataContainer.from(keys);
		Collection<DataContainer> collection = sinter(keyContainers);
		List<byte[]> data = DataContainer.toBytes(collection);
		response.set(data);
		return response;
	}

	@Override
	public synchronized Response<Set<byte[]>> sinter(final byte[]... keys) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		DataContainer[] keyContainers = DataContainer.from(keys);
		Collection<DataContainer> collection = sinter(keyContainers);
		List<byte[]> data = DataContainer.toBytes(collection);
		response.set(data);
		return response;
	}

	private Set<DataContainer> sinter(final DataContainer... keys) {
		final int l = keys.length;
		if (l <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sinter' command");
		}

		final Set<DataContainer> firstSet = new HashSet<DataContainer>(getSetFromStorage(keys[0], true));
		for (int i = 1; i < l; ++i) {
			final Set<DataContainer> set = getSetFromStorage(keys[i], true);
			firstSet.retainAll(set);
		}

		return firstSet;
	}

	@Override
	public synchronized Response<Long> sinterstore(final String dstKey, final String... keys) {
		return sinterstore(DataContainer.from(dstKey), DataContainer.from(keys));
	}

	@Override
	public synchronized Response<Long> sinterstore(final byte[] dstkey, final byte[]... keys) {
		return sinterstore(DataContainer.from(dstkey), DataContainer.from(keys));
	}

	private Response<Long> sinterstore(final DataContainer dstKey, final DataContainer... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sinterstore' command");
		}
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<DataContainer> inter = sinter(keys);
		final Set<DataContainer> dst = getSetFromStorage(dstKey, true);
		if (!dst.isEmpty()) {
			dst.clear();
		}
		dst.addAll(inter);
		response.set((long) inter.size());

		return response;
	}

	@Override
	public synchronized Response<Boolean> sismember(final String key, final String member) {
		return sismember(DataContainer.from(key), DataContainer.from(member));
	}

	@Override
	public synchronized Response<Boolean> sismember(final byte[] key, final byte[] member) {
		return sismember(DataContainer.from(key), DataContainer.from(member));
	}

	private Response<Boolean> sismember(final DataContainer key, final DataContainer member) {
		final Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		final Set<DataContainer> set = getSetFromStorage(key, false);
		response.set(set != null && set.contains(member) ? 1L : 0L);
		return response;
	}

	@Override
	public synchronized Response<Long> smove(final String srckey, final String dstkey, final String member) {
		return smove(DataContainer.from(srckey), DataContainer.from(dstkey), DataContainer.from(member));
	}

	@Override
	public synchronized Response<Long> smove(final byte[] srckey, final byte[] dstkey, final byte[] member) {
		return smove(DataContainer.from(srckey), DataContainer.from(dstkey), DataContainer.from(member));
	}

	private Response<Long> smove(final DataContainer srckey, final DataContainer dstkey, final DataContainer member) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<DataContainer> src = getSetFromStorage(srckey, false);
		final Set<DataContainer> dst = getSetFromStorage(dstkey, true);
		if (src.remove(member)) {
			dst.add(member);
			response.set(1L);
		} else {
			response.set(0L);
		}

		return response;
	}

	@Override
	public synchronized Response<String> spop(final String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		DataContainer result = spop(DataContainer.from(key));
		response.set(DataContainer.toBytes(result));
		return response;
	}

	@Override
	public synchronized Response<byte[]> spop(final byte[] key) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		DataContainer result = spop(DataContainer.from(key));
		response.set(DataContainer.toBytes(result));
		return response;
	}

	private DataContainer spop(final DataContainer key) {
		DataContainer member = srandmember(key);
		if (member != null) {
			final Set<DataContainer> src = getSetFromStorage(key, false);
			src.remove(member);
		}
		return member;
	}

	@Override
	public synchronized Response<String> srandmember(final String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final DataContainer result = srandmember(DataContainer.from(key));
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public synchronized Response<byte[]> srandmember(final byte[] key) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final DataContainer result = srandmember(DataContainer.from(key));
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	private DataContainer srandmember(final DataContainer key) {
		final Set<DataContainer> src = getSetFromStorage(key, false);
		if (src == null) {
			return null;
		} else {
			return getRandomElementFromSet(src);
		}
	}

	@Override
	public synchronized Response<Set<String>> smembers(final String key) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		final Set<DataContainer> members = smembers(DataContainer.from(key));
		response.set(DataContainer.toBytes(members));

		return response;
	}

	@Override
	public synchronized Response<Set<byte[]>> smembers(final byte[] key) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		final Set<DataContainer> members = smembers(DataContainer.from(key));
		response.set(DataContainer.toBytes(members));

		return response;
	}

	private Set<DataContainer> smembers(final DataContainer key) {
		return getSetFromStorage(key, true);
	}


	@Override
	public synchronized Response<Set<String>> sunion(final String... keys) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		Set<DataContainer> result = sunion(DataContainer.from(keys));
		response.set(DataContainer.toBytes(result));
		return response;
	}

	@Override
	public synchronized Response<Set<byte[]>> sunion(final byte[]... keys) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		Set<DataContainer> result = sunion(DataContainer.from(keys));
		response.set(DataContainer.toBytes(result));
		return response;
	}

	private Set<DataContainer> sunion(final DataContainer... keys) {
		final int l = keys.length;
		if (l <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sunion' command");
		}
		final Set<DataContainer> result = new HashSet<DataContainer>(getSetFromStorage(keys[0], true));
		for (int i = 1; i < l; ++i) {
			final Set<DataContainer> set = getSetFromStorage(keys[i], true);
			result.addAll(set);
		}

		return result;
	}

	@Override
	public synchronized Response<Long> sunionstore(final String dstkey, final String... keys) {
		return sunionstore(DataContainer.from(dstkey), DataContainer.from(keys));
	}

	@Override
	public synchronized Response<Long> sunionstore(final byte[] dstkey, final byte[]... keys) {
		return sunionstore(DataContainer.from(dstkey), DataContainer.from(keys));
	}

	private Response<Long> sunionstore(final DataContainer dstkey, final DataContainer... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sunionstore' command");
		}
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Set<DataContainer> inter = sunion(keys);
		final Set<DataContainer> dst = getSetFromStorage(dstkey, true);
		if (!dst.isEmpty()) {
			dst.clear();
		}
		dst.addAll(inter);
		response.set((long) inter.size());

		return response;
	}
}

