package com.fiftyonred.mock_jedis;

import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.*;

import static com.fiftyonred.mock_jedis.DataContainer.CHARSET;

public class MockPipeline extends Pipeline {

	private static final byte[] STRING_TYPE = "string".getBytes(CHARSET);
	private static final byte[] LIST_TYPE = "list".getBytes(CHARSET);
	private static final byte[] SET_TYPE = "set".getBytes(CHARSET);
	private static final byte[] NONE_TYPE = "none".getBytes(CHARSET);
	private static final byte[] OK_RESPONSE  = "OK".getBytes(CHARSET);
	private static final byte[] PONG_RESPONSE = "PONG".getBytes(CHARSET);

	private final MockStorage mockStorage;

	public MockPipeline() {
		mockStorage = new MockStorage();
	}

	public int getCurrentDB() {
		return mockStorage.getCurrentDB();
	}

	protected static <T> T getRandomElementFromSet(final Set<T> set) {
		return (T) set.toArray()[(int) (Math.random() * set.size())];
	}

	@Override
	public Response<String> ping() {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(PONG_RESPONSE);
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
	public Response<Long> dbSize() {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.dbSize());
		return response;
	}

	@Override
	public Response<String> flushAll() {
		mockStorage.flushAll();
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<String> flushDB() {
		mockStorage.flushDB();
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<String> rename(final String oldkey, final String newkey) {
		mockStorage.rename(DataContainer.from(oldkey), DataContainer.from(newkey));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<String> rename(final byte[] oldkey, final byte[] newkey) {
		mockStorage.rename(DataContainer.from(oldkey), DataContainer.from(newkey));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<Long> renamenx(final String oldkey, final String newkey) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.renamenx(DataContainer.from(oldkey), DataContainer.from(newkey)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> renamenx(final byte[] oldkey, final byte[] newkey) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.renamenx(DataContainer.from(oldkey), DataContainer.from(newkey)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<String> set(final String key, final String value) {
		mockStorage.set(DataContainer.from(key), DataContainer.from(value));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<String> set(final byte[] key, final byte[] value) {
		mockStorage.set(DataContainer.from(key), DataContainer.from(value));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<Long> setnx(final String key, final String value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.setnx(DataContainer.from(key), DataContainer.from(value)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> setnx(final byte[] key, final byte[] value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.setnx(DataContainer.from(key), DataContainer.from(value)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<String> get(final String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final DataContainer result = mockStorage.get(DataContainer.from(key));
		response.set(DataContainer.toBytes(result));
		return response;
	}

	@Override
	public Response<byte[]> get(final byte[] key) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final DataContainer result = mockStorage.get(DataContainer.from(key));
		response.set(DataContainer.toBytes(result));
		return response;
	}

	@Override
	public Response<String> getSet(final String key, final String value) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final DataContainer result = mockStorage.getSet(DataContainer.from(key), DataContainer.from(value));
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public Response<byte[]> getSet(final byte[] key, final byte[] value) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final DataContainer result = mockStorage.getSet(DataContainer.from(key), DataContainer.from(value));
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public Response<byte[]> dump(final byte[] key) {
		return get(key);
	}

	@Override
	public Response<byte[]> dump(final String key) {
		return get(key.getBytes(CHARSET));
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
	public Response<Boolean> exists(final String key) {
		final Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		response.set(mockStorage.exists(DataContainer.from(key)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Boolean> exists(final byte[] key) {
		final Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		response.set(mockStorage.exists(DataContainer.from(key)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<String> type(final String key) {
		final KeyType type = mockStorage.type(DataContainer.from(key));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		if (type == null) {
			response.set(NONE_TYPE);
			return response;
		}
		switch (type) {
			case STRING:
				response.set(STRING_TYPE);
				break;
			case LIST:
				response.set(LIST_TYPE);
				break;
			case SET:
				response.set(SET_TYPE);
				break;
			default:
				response.set(NONE_TYPE);
		}
		return response;
	}

	@Override
	public Response<String> type(final byte[] key) {
		final KeyType type = mockStorage.type(DataContainer.from(key));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		if (type == null) {
			response.set(NONE_TYPE);
			return response;
		}
		switch (type) {
			case STRING:
				response.set(STRING_TYPE);
				break;
			case LIST:
				response.set(LIST_TYPE);
				break;
			case SET:
				response.set(SET_TYPE);
				break;
			default:
				response.set(NONE_TYPE);
		}
		return response;
	}

	@Override
	public Response<Long> move(final String key, final int dbIndex) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.move(DataContainer.from(key), dbIndex) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> move(final byte[] key, final int dbIndex) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.move(DataContainer.from(key), dbIndex) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<String> randomKey() {
		final DataContainer result = mockStorage.randomKey();
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public Response<byte[]> randomKeyBinary() {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final DataContainer result = mockStorage.randomKey();
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public Response<String> select(final int dbIndex) {
		mockStorage.select(dbIndex);
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
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
	public Response<String> psetex(final String key, final int milliseconds, final String value) {
		mockStorage.psetex(DataContainer.from(key), milliseconds, DataContainer.from(value));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<String> psetex(final byte[] key, final int milliseconds, final byte[] value) {
		mockStorage.psetex(DataContainer.from(key), milliseconds, DataContainer.from(value));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
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
	public Response<Long> pexpireAt(final String key, final long millisecondsTimestamp) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.pexpireAt(DataContainer.from(key), millisecondsTimestamp) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> pexpireAt(final byte[] key, final long millisecondsTimestamp) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.pexpireAt(DataContainer.from(key), millisecondsTimestamp) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> ttl(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.ttl(DataContainer.from(key)));
		return response;
	}

	@Override
	public Response<Long> ttl(final byte[] key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.ttl(DataContainer.from(key)));
		return response;
	}

	@Override
	public Response<Long> append(final String key, final String value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.append(DataContainer.from(key), DataContainer.from(value)));
		return response;
	}

	@Override
	public Response<Long> append(final byte[] key, final byte[] value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.append(DataContainer.from(key), DataContainer.from(value)));
		return response;
	}

	@Override
	public Response<Long> pttl(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.pttl(DataContainer.from(key)));
		return response;
	}

	@Override
	public Response<Long> pttl(final byte[] key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.pttl(DataContainer.from(key)));
		return response;
	}

	@Override
	public Response<Long> persist(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.persist(DataContainer.from(key)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> persist(final byte[] key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.persist(DataContainer.from(key)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<List<String>> mget(final String... keys) {
		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);

		final List<byte[]> result = new ArrayList<byte[]>(keys.length);
		for (final DataContainer val : mockStorage.mget(DataContainer.from(keys))) {
			result.add(val == null ? null : val.getBytes());
		}
		response.set(result);
		return response;
	}

	@Override
	public Response<List<byte[]>> mget(final byte[]... keys) {
		final Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);

		final List<byte[]> result = new ArrayList<byte[]>(keys.length);
		for (final DataContainer val : mockStorage.mget(DataContainer.from(keys))) {
			result.add(val == null ? null : val.getBytes());
		}
		response.set(result);
		return response;
	}

	@Override
	public Response<String> mset(final String... keysvalues) {
		mockStorage.mset(DataContainer.from(keysvalues));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<String> mset(final byte[]... keysvalues) {
		mockStorage.mset(DataContainer.from(keysvalues));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<Long> msetnx(final String... keysvalues) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.msetnx(DataContainer.from(keysvalues)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> msetnx(final byte[]... keysvalues) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.msetnx(DataContainer.from(keysvalues)) ? 1L : 0L);
		return response;
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
	public Response<Long> incrBy(final String key, final long integer) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.incrBy(DataContainer.from(key), integer));
		return response;
	}

	@Override
	public Response<Long> incrBy(final byte[] key, final long integer) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.incrBy(DataContainer.from(key), integer));
		return response;
	}

	@Override
	public Response<Double> incrByFloat(final String key, final double increment) {
		final Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
		response.set(mockStorage.incrByFloat(DataContainer.from(key), increment).getBytes());
		return response;
	}

	@Override
	public Response<Double> incrByFloat(final byte[] key, final double increment) {
		final Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
		response.set(mockStorage.incrByFloat(DataContainer.from(key), increment).getBytes());
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
		List<DataContainer> sortedList = mockStorage.sort(DataContainer.from(key), sortingParameters);
		response.set(DataContainer.toBytes(sortedList));
		return response;
	}

	@Override
	public Response<List<byte[]>> sort(final byte[] key, final SortingParams sortingParameters) {
		final Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
		List<DataContainer> sortedList = mockStorage.sort(DataContainer.from(key), sortingParameters);
		response.set(DataContainer.toBytes(sortedList));
		return response;
	}

	@Override
	public Response<Long> sort(final String key, final SortingParams sortingParameters, final String dstkey) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.sort(DataContainer.from(key), sortingParameters, DataContainer.from(dstkey)));
		return response;
	}

	@Override
	public Response<Long> sort(final byte[] key, final SortingParams sortingParameters, final byte[] dstkey) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.sort(DataContainer.from(key), sortingParameters, DataContainer.from(dstkey)));
		return response;
	}

	@Override
	public Response<List<byte[]>> sort(final byte[] key) {
		return sort(key, new SortingParams());
	}

	@Override
	public Response<Long> strlen(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.strlen(DataContainer.from(key)));
		return response;
	}

	@Override
	public Response<Long> strlen(final byte[] key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.strlen(DataContainer.from(key)));
		return response;
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
	public Response<Long> del(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.del(DataContainer.from(key)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> del(final byte[] key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.del(DataContainer.from(key)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<String> hget(final String key, final String field) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final DataContainer result = mockStorage.hget(DataContainer.from(key), DataContainer.from(field));
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public Response<byte[]> hget(final byte[] key, final byte[] field) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final DataContainer result = mockStorage.hget(DataContainer.from(key), DataContainer.from(field));
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public Response<Map<String, String>> hgetAll(final String key) {
		final Response<Map<String, String>> response = new Response<Map<String, String>>(BuilderFactory.STRING_MAP);
		final Map<DataContainer, DataContainer> result = mockStorage.hgetAll(DataContainer.from(key));

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
	public Response<Map<byte[], byte[]>> hgetAll(final byte[] key) {
		final Response<Map<byte[], byte[]>> response = new Response<Map<byte[], byte[]>>(BuilderFactory.BYTE_ARRAY_MAP);
		final Map<DataContainer, DataContainer> result = mockStorage.hgetAll(DataContainer.from(key));

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
	public Response<Set<String>> hkeys(final String key) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		final Set<DataContainer> result = mockStorage.hkeys(DataContainer.from(key));
		response.set(result == null ? new ArrayList<byte[]>(0) : DataContainer.toBytes(result));
		return response;
	}

	@Override
	public Response<Set<byte[]>> hkeys(final byte[] key) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		final Set<DataContainer> result = mockStorage.hkeys(DataContainer.from(key));
		response.set(result == null ? new ArrayList<byte[]>(0) : DataContainer.toBytes(result));
		return response;
	}

	@Override
	public Response<List<String>> hvals(final String key) {
		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		final Collection<DataContainer> result = mockStorage.hvals(DataContainer.from(key));
		response.set(result == null ? new ArrayList<byte[]>(0) : DataContainer.toBytes(result));
		return response;
	}

	@Override
	public Response<List<byte[]>> hvals(final byte[] key) {
		final Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
		final Collection<DataContainer> result = mockStorage.hvals(DataContainer.from(key));
		response.set(result == null ? new ArrayList<byte[]>(0) : DataContainer.toBytes(result));
		return response;
	}

	@Override
	public Response<Long> hset(final String key, final String field, final String value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.hset(DataContainer.from(key), DataContainer.from(field), DataContainer.from(value)) ? 1L : 0L);
		return response;

	}

	@Override
	public Response<Long> hset(final byte[] key, final byte[] field, final byte[] value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.hset(DataContainer.from(key), DataContainer.from(field), DataContainer.from(value)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> hsetnx(final String key, final String field, final String value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.hsetnx(DataContainer.from(key), DataContainer.from(field), DataContainer.from(value)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> hsetnx(final byte[] key, final byte[] field, final byte[] value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.hsetnx(DataContainer.from(key), DataContainer.from(field), DataContainer.from(value)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<List<String>> hmget(final String key, final String... fields) {
		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		final List<byte[]> result = new ArrayList<byte[]>(fields.length);
		final List<DataContainer> hash = mockStorage.hmget(DataContainer.from(key), DataContainer.from(fields));
		if (hash == null) {
			for (final String ignored : fields) {
				result.add(null);
			}
		} else {
			for (final DataContainer h : hash) {
				result.add(h == null ? null : h.getBytes());
			}
		}
		response.set(result);
		return response;
	}

	@Override
	public Response<List<byte[]>> hmget(final byte[] key, final byte[]... fields) {
		final Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
		final List<byte[]> result = new ArrayList<byte[]>(fields.length);
		final List<DataContainer> hash = mockStorage.hmget(DataContainer.from(key), DataContainer.from(fields));
		if (hash == null) {
			for (final byte[] ignored : fields) {
				result.add(null);
			}
		} else {
			for (final DataContainer h : hash) {
				result.add(h == null ? null : h.getBytes());
			}
		}
		response.set(result);
		return response;
	}

	@Override
	public Response<String> hmset(final String key, final Map<String, String> hash) {
		mockStorage.hmset(DataContainer.from(key), DataContainer.fromStringMap(hash));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<String> hmset(final byte[] key, final Map<byte[], byte[]> hash) {
		mockStorage.hmset(DataContainer.from(key), DataContainer.fromByteMap(hash));
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(OK_RESPONSE);
		return response;
	}

	@Override
	public Response<Long> hincrBy(final String key, final String field, final long value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.hincrBy(DataContainer.from(key), DataContainer.from(field), value));
		return response;
	}

	@Override
	public Response<Long> hincrBy(final byte[] key, final byte[] field, final long value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.hincrBy(DataContainer.from(key), DataContainer.from(field), value));
		return response;
	}

	@Override
	public Response<Double> hincrByFloat(final String key, final String field, final double increment) {
		final Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
		response.set(mockStorage.hincrByFloat(DataContainer.from(key), DataContainer.from(field), increment).getBytes());
		return response;
	}

	@Override
	public Response<Double> hincrByFloat(final byte[] key, final byte[] field, final double increment) {
		final Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
		response.set(mockStorage.hincrByFloat(DataContainer.from(key), DataContainer.from(field), increment).getBytes());
		return response;
	}

	@Override
	public Response<Long> hdel(final String key, final String... field) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.hdel(DataContainer.from(key), DataContainer.from(field)));
		return response;
	}

	@Override
	public Response<Long> hdel(final byte[] key, final byte[]... field) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.hdel(DataContainer.from(key), DataContainer.from(field)));
		return response;
	}

	@Override
	public Response<Boolean> hexists(final String key, final String field) {
		final Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		response.set(mockStorage.hexists(DataContainer.from(key), DataContainer.from(field)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Boolean> hexists(final byte[] key, final byte[] field) {
		final Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		response.set(mockStorage.hexists(DataContainer.from(key), DataContainer.from(field)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> hlen(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.hlen(DataContainer.from(key)));
		return response;
	}

	@Override
	public Response<Long> hlen(final byte[] key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.hlen(DataContainer.from(key)));
		return response;
	}

	@Override
	public Response<Long> lpush(final String key, final String... string) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.lpush(DataContainer.from(key), DataContainer.from(string)));
		return response;
	}

	@Override
	public Response<Long> lpush(final byte[] key, final byte[]... string) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.lpush(DataContainer.from(key), DataContainer.from(string)));
		return response;
	}

	@Override
	public Response<String> lpop(final String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final DataContainer result = mockStorage.lpop(DataContainer.from(key));
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public Response<byte[]> lpop(final byte[] key) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final DataContainer result = mockStorage.lpop(DataContainer.from(key));
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public Response<Long> llen(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.llen(DataContainer.from(key)));
		return response;
	}

	@Override
	public Response<Long> llen(final byte[] key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.llen(DataContainer.from(key)));
		return response;
	}

	@Override
	public Response<List<String>> lrange(final String key, final long start, final long end) {
		final Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		response.set(DataContainer.toBytes(mockStorage.lrange(DataContainer.from(key), start, end)));
		return response;
	}

	@Override
	public Response<List<byte[]>> lrange(final byte[] key, final long start, final long end) {
		final Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
		response.set(DataContainer.toBytes(mockStorage.lrange(DataContainer.from(key), start, end)));
		return response;
	}

	@Override
	public void sync() {
		// do nothing
	}

	@Override
	public Response<Set<String>> keys(final String pattern) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		response.set(DataContainer.toBytes(mockStorage.keys(DataContainer.from(pattern))));
		return response;
	}

	@Override
	public Response<Set<byte[]>> keys(final byte[] pattern) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		response.set(DataContainer.toBytes(mockStorage.keys(DataContainer.from(pattern))));
		return response;
	}

	@Override
	public Response<Long> sadd(final String key, final String... member) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.sadd(DataContainer.from(key), DataContainer.from(member)));
		return response;
	}

	@Override
	public Response<Long> sadd(final byte[] key, final byte[]... member) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.sadd(DataContainer.from(key), DataContainer.from(member)));
		return response;
	}

	@Override
	public Response<Long> srem(final String key, final String... member) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.srem(DataContainer.from(key), DataContainer.from(member)));
		return response;
	}

	@Override
	public Response<Long> srem(final byte[] key, final byte[]... member) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.srem(DataContainer.from(key), DataContainer.from(member)));
		return response;
	}

	@Override
	public Response<Long> scard(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.scard(DataContainer.from(key)));
		return response;
	}

	@Override
	public Response<Long> scard(final byte[] key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.scard(DataContainer.from(key)));
		return response;
	}

	@Override
	public Response<Set<String>> sdiff(final String... keys) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		response.set(
				DataContainer.toBytes(
						mockStorage.sdiff(DataContainer.from(keys))
				)
		);
		return response;
	}

	@Override
	public Response<Set<byte[]>> sdiff(final byte[]... keys) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		response.set(
				DataContainer.toBytes(
						mockStorage.sdiff(DataContainer.from(keys))
				)
		);
		return response;
	}

	@Override
	public Response<Long> sdiffstore(final String dstkey, final String... keys) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.sdiffstore(DataContainer.from(dstkey), DataContainer.from(keys)));
		return response;
	}

	@Override
	public Response<Long> sdiffstore(final byte[] dstkey, final byte[]... keys) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.sdiffstore(DataContainer.from(dstkey), DataContainer.from(keys)));
		return response;
	}

	@Override
	public Response<Set<String>> sinter(final String... keys) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		response.set(
				DataContainer.toBytes(
						mockStorage.sinter(DataContainer.from(keys))
				)
		);
		return response;
	}

	@Override
	public Response<Set<byte[]>> sinter(final byte[]... keys) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		response.set(
				DataContainer.toBytes(
						mockStorage.sinter(DataContainer.from(keys))
				)
		);
		return response;
	}

	@Override
	public Response<Long> sinterstore(final String dstkey, final String... keys) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.sinterstore(DataContainer.from(dstkey), DataContainer.from(keys)));
		return response;
	}

	@Override
	public Response<Long> sinterstore(final byte[] dstkey, final byte[]... keys) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.sinterstore(DataContainer.from(dstkey), DataContainer.from(keys)));
		return response;
	}

	@Override
	public Response<Boolean> sismember(final String key, final String member) {
		final Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		response.set(mockStorage.sismember(DataContainer.from(key), DataContainer.from(member)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Boolean> sismember(final byte[] key, final byte[] member) {
		final Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
		response.set(mockStorage.sismember(DataContainer.from(key), DataContainer.from(member)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> smove(final String srckey, final String dstkey, final String member) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.smove(DataContainer.from(srckey), DataContainer.from(dstkey), DataContainer.from(member)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<Long> smove(final byte[] srckey, final byte[] dstkey, final byte[] member) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(mockStorage.smove(DataContainer.from(srckey), DataContainer.from(dstkey), DataContainer.from(member)) ? 1L : 0L);
		return response;
	}

	@Override
	public Response<String> spop(final String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		response.set(
				DataContainer.toBytes(
						mockStorage.spop(DataContainer.from(key))
				)
		);
		return response;
	}

	@Override
	public Response<byte[]> spop(final byte[] key) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		response.set(
				DataContainer.toBytes(
						mockStorage.spop(DataContainer.from(key))
				)
		);
		return response;
	}

	@Override
	public Response<String> srandmember(final String key) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final DataContainer result = mockStorage.srandmember(DataContainer.from(key));
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public Response<byte[]> srandmember(final byte[] key) {
		final Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
		final DataContainer result = mockStorage.srandmember(DataContainer.from(key));
		response.set(result == null ? null : result.getBytes());
		return response;
	}

	@Override
	public Response<Set<String>> smembers(final String key) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		final Set<DataContainer> members = mockStorage.smembers(DataContainer.from(key));
		response.set(DataContainer.toBytes(members));

		return response;
	}

	@Override
	public Response<Set<byte[]>> smembers(final byte[] key) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		final Set<DataContainer> members = mockStorage.smembers(DataContainer.from(key));
		response.set(DataContainer.toBytes(members));

		return response;
	}

	@Override
	public Response<Set<String>> sunion(final String... keys) {
		final Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
		response.set(DataContainer.toBytes(mockStorage.sunion(DataContainer.from(keys))));
		return response;
	}

	@Override
	public Response<Set<byte[]>> sunion(final byte[]... keys) {
		final Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
		response.set(DataContainer.toBytes(mockStorage.sunion(DataContainer.from(keys))));
		return response;
	}

	@Override
	public Response<Long> sunionstore(final String dstkey, final String... keys) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.sunionstore(DataContainer.from(dstkey), DataContainer.from(keys)));
		return response;
	}

	@Override
	public Response<Long> sunionstore(final byte[] dstkey, final byte[]... keys) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set((long) mockStorage.sunionstore(DataContainer.from(dstkey), DataContainer.from(keys)));
		return response;
	}
}

