package com.fiftyonred.mock_jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Builder;
import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Response;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisDataException;

import static com.fiftyonred.mock_jedis.DataContainerImpl.CHARSET;

public class MockPipeline extends Pipeline {

  private static final byte[] STRING_TYPE = "string".getBytes(CHARSET);
  private static final byte[] LIST_TYPE = "list".getBytes(CHARSET);
  private static final byte[] SET_TYPE = "set".getBytes(CHARSET);
  private static final byte[] NONE_TYPE = "none".getBytes(CHARSET);
  private static final byte[] OK_RESPONSE = "OK".getBytes(CHARSET);
  private static final byte[] PONG_RESPONSE = "PONG".getBytes(CHARSET);

  private static final Builder<List<Object>> OBJECT_LIST = new Builder<List<Object>>() {
    @SuppressWarnings("unchecked")
    public List<Object> build(Object data) {
      if (null == data) {
        return null;
      }
      List<byte[]> l = (List<byte[]>) data;
      final ArrayList<Object> result = new ArrayList<Object>(l.size());
      for (final byte[] barray : l) {
        if (barray == null) {
          result.add(null);
        } else {
          result.add(barray);
        }
      }
      return result;
    }

    public String toString() {
      return "List<Object>";
    }

  };

  private MockStorage mockStorage;
  private MockStorage oldMockStorage;

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
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(PONG_RESPONSE);
    return response;
  }

  @Override
  public Response<String> echo(final String string) {
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(echo(string.getBytes(CHARSET)).get());
    return response;
  }

  @Override
  public Response<byte[]> echo(final byte[] string) {
    Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
    response.set(string);
    return response;
  }

  @Override
  public Response<Long> dbSize() {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.dbSize());
    return response;
  }

  @Override
  public Response<String> flushAll() {
    mockStorage.flushAll();
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<String> flushDB() {
    mockStorage.flushDB();
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<String> rename(final String oldkey, final String newkey) {
    mockStorage.rename(DataContainerImpl.from(oldkey), DataContainerImpl.from(newkey));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<String> rename(final byte[] oldkey, final byte[] newkey) {
    mockStorage.rename(DataContainerImpl.from(oldkey), DataContainerImpl.from(newkey));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<Long> renamenx(final String oldkey, final String newkey) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.renamenx(DataContainerImpl.from(oldkey), DataContainerImpl.from
        (newkey)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Long> renamenx(final byte[] oldkey, final byte[] newkey) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.renamenx(DataContainerImpl.from(oldkey), DataContainerImpl.from
        (newkey)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<String> set(final String key, final String value) {
    mockStorage.set(DataContainerImpl.from(key), DataContainerImpl.from(value));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<String> set(final byte[] key, final byte[] value) {
    mockStorage.set(DataContainerImpl.from(key), DataContainerImpl.from(value));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<Long> setnx(final String key, final String value) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.setnx(DataContainerImpl.from(key), DataContainerImpl.from(value)) ?
        1L : 0L);
    return response;
  }

  @Override
  public Response<Long> setnx(final byte[] key, final byte[] value) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.setnx(DataContainerImpl.from(key), DataContainerImpl.from(value)) ?
        1L : 0L);
    return response;
  }

  @Override
  public Response<String> get(final String key) {
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    final DataContainer result = mockStorage.get(DataContainerImpl.from(key));
    response.set(DataContainerImpl.toBytes(result));
    return response;
  }

  @Override
  public Response<byte[]> get(final byte[] key) {
    Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
    final DataContainer result = mockStorage.get(DataContainerImpl.from(key));
    response.set(DataContainerImpl.toBytes(result));
    return response;
  }

  @Override
  public Response<String> getSet(final String key, final String value) {
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    final DataContainer result = mockStorage.getSet(DataContainerImpl.from(key),
        DataContainerImpl.from(value));
    response.set(result == null ? null : result.getBytes());
    return response;
  }

  @Override
  public Response<byte[]> getSet(final byte[] key, final byte[] value) {
    Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
    final DataContainer result = mockStorage.getSet(DataContainerImpl.from(key),
        DataContainerImpl.from(value));
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
    Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
    response.set(mockStorage.exists(DataContainerImpl.from(key)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Boolean> exists(final byte[] key) {
    Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
    response.set(mockStorage.exists(DataContainerImpl.from(key)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<String> type(final String key) {
    final KeyType type = mockStorage.type(DataContainerImpl.from(key));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
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
    final KeyType type = mockStorage.type(DataContainerImpl.from(key));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
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
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.move(DataContainerImpl.from(key), dbIndex) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Long> move(final byte[] key, final int dbIndex) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.move(DataContainerImpl.from(key), dbIndex) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<String> randomKey() {
    final DataContainer result = mockStorage.randomKey();
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(result == null ? null : result.getBytes());
    return response;
  }

  @Override
  public Response<byte[]> randomKeyBinary() {
    Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
    final DataContainer result = mockStorage.randomKey();
    response.set(result == null ? null : result.getBytes());
    return response;
  }

  @Override
  public Response<String> select(final int dbIndex) {
    mockStorage.select(dbIndex);
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<String> setex(final String key, final int seconds, final String value) {
    return psetex(key, (long) seconds * 1000, value);
  }

  @Override
  public Response<String> setex(final byte[] key, final int seconds, final byte[] value) {
    return psetex(key, (long) seconds * 1000, value);
  }

  @Override
  public Response<String> psetex(final String key, final int milliseconds, final String value) {
    return psetex(key, (long) milliseconds, value);
  }

  public Response<String> psetex(final String key, final long milliseconds, final String value) {
    mockStorage.psetex(DataContainerImpl.from(key), milliseconds, DataContainerImpl.from(value));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<String> psetex(final byte[] key, final int milliseconds, final byte[] value) {
    return psetex(key, (long) milliseconds, value);
  }

  public Response<String> psetex(final byte[] key, final long milliseconds, final byte[] value) {
    mockStorage.psetex(DataContainerImpl.from(key), milliseconds, DataContainerImpl.from(value));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
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
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.pexpireAt(DataContainerImpl.from(key), millisecondsTimestamp) ? 1L :
        0L);
    return response;
  }

  @Override
  public Response<Long> pexpireAt(final byte[] key, final long millisecondsTimestamp) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.pexpireAt(DataContainerImpl.from(key), millisecondsTimestamp) ? 1L :
        0L);
    return response;
  }

  @Override
  public Response<Long> ttl(final String key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.ttl(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<Long> ttl(final byte[] key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.ttl(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<Long> append(final String key, final String value) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.append(DataContainerImpl.from(key), DataContainerImpl.from(value)));
    return response;
  }

  @Override
  public Response<Long> append(final byte[] key, final byte[] value) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.append(DataContainerImpl.from(key), DataContainerImpl.from(value)));
    return response;
  }

  @Override
  public Response<Long> pttl(final String key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.pttl(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<Long> pttl(final byte[] key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.pttl(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<Long> persist(final String key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.persist(DataContainerImpl.from(key)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Long> persist(final byte[] key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.persist(DataContainerImpl.from(key)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<List<String>> mget(final String... keys) {
    Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);

    final List<byte[]> result = new ArrayList<byte[]>(keys.length);
    for (final DataContainer val : mockStorage.mget(DataContainerImpl.from(keys))) {
      result.add(val == null ? null : val.getBytes());
    }
    response.set(result);
    return response;
  }

  @Override
  public Response<List<byte[]>> mget(final byte[]... keys) {
    Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);

    final List<byte[]> result = new ArrayList<byte[]>(keys.length);
    for (final DataContainer val : mockStorage.mget(DataContainerImpl.from(keys))) {
      result.add(val == null ? null : val.getBytes());
    }
    response.set(result);
    return response;
  }

  @Override
  public Response<String> mset(final String... keysvalues) {
    mockStorage.mset(DataContainerImpl.from(keysvalues));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<String> mset(final byte[]... keysvalues) {
    mockStorage.mset(DataContainerImpl.from(keysvalues));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<Long> msetnx(final String... keysvalues) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.msetnx(DataContainerImpl.from(keysvalues)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Long> msetnx(final byte[]... keysvalues) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.msetnx(DataContainerImpl.from(keysvalues)) ? 1L : 0L);
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
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.incrBy(DataContainerImpl.from(key), integer));
    return response;
  }

  @Override
  public Response<Long> incrBy(final byte[] key, final long integer) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.incrBy(DataContainerImpl.from(key), integer));
    return response;
  }

  @Override
  public Response<Double> incrByFloat(final String key, final double increment) {
    Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
    response.set(mockStorage.incrByFloat(DataContainerImpl.from(key), increment).getBytes());
    return response;
  }

  @Override
  public Response<Double> incrByFloat(final byte[] key, final double increment) {
    Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
    response.set(mockStorage.incrByFloat(DataContainerImpl.from(key), increment).getBytes());
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
    Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
    List<DataContainer> sortedList = mockStorage.sort(DataContainerImpl.from(key),
        sortingParameters);
    response.set(DataContainerImpl.toBytes(sortedList));
    return response;
  }

  @Override
  public Response<List<byte[]>> sort(final byte[] key, final SortingParams sortingParameters) {
    Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
    List<DataContainer> sortedList = mockStorage.sort(DataContainerImpl.from(key),
        sortingParameters);
    response.set(DataContainerImpl.toBytes(sortedList));
    return response;
  }

  @Override
  public Response<Long> sort(final String key, final SortingParams sortingParameters, final
  String dstkey) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.sort(DataContainerImpl.from(key), sortingParameters,
        DataContainerImpl.from(dstkey)));
    return response;
  }

  @Override
  public Response<Long> sort(final byte[] key, final SortingParams sortingParameters, final
  byte[] dstkey) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.sort(DataContainerImpl.from(key), sortingParameters,
        DataContainerImpl.from(dstkey)));
    return response;
  }

  @Override
  public Response<List<byte[]>> sort(final byte[] key) {
    return sort(key, new SortingParams());
  }

  @Override
  public Response<Long> strlen(final String key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.strlen(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<Long> strlen(final byte[] key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.strlen(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<Long> del(final String... keys) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    long result = 0L;
    for (final String key : keys) {
      result += del(key).get();
    }

    response.set(result);
    return response;
  }

  @Override
  public Response<Long> del(final byte[]... keys) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    long result = 0L;
    for (final byte[] key : keys) {
      result += del(key).get();
    }

    response.set(result);
    return response;
  }

  @Override
  public Response<Long> del(final String key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.del(DataContainerImpl.from(key)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Long> del(final byte[] key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.del(DataContainerImpl.from(key)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<String> hget(final String key, final String field) {
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    final DataContainer result = mockStorage.hget(DataContainerImpl.from(key), DataContainerImpl
        .from(field));
    response.set(result == null ? null : result.getBytes());
    return response;
  }

  @Override
  public Response<byte[]> hget(final byte[] key, final byte[] field) {
    Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
    final DataContainer result = mockStorage.hget(DataContainerImpl.from(key), DataContainerImpl
        .from(field));
    response.set(result == null ? null : result.getBytes());
    return response;
  }

  @Override
  public Response<Map<String, String>> hgetAll(final String key) {
    Response<Map<String, String>> response = new Response<Map<String, String>>(BuilderFactory
        .STRING_MAP);
    final Map<DataContainer, DataContainer> result = mockStorage.hgetAll(DataContainerImpl.from
        (key));

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
    Response<Map<byte[], byte[]>> response = new Response<Map<byte[], byte[]>>(BuilderFactory
        .BYTE_ARRAY_MAP);
    final Map<DataContainer, DataContainer> result = mockStorage.hgetAll(DataContainerImpl.from
        (key));

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
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
    final Set<DataContainer> result = mockStorage.hkeys(DataContainerImpl.from(key));
    response.set(result == null ? new ArrayList<byte[]>(0) : DataContainerImpl.toBytes(result));
    return response;
  }

  @Override
  public Response<Set<byte[]>> hkeys(final byte[] key) {
    Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
    final Set<DataContainer> result = mockStorage.hkeys(DataContainerImpl.from(key));
    response.set(result == null ? new ArrayList<byte[]>(0) : DataContainerImpl.toBytes(result));
    return response;
  }

  @Override
  public Response<List<String>> hvals(final String key) {
    Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
    final Collection<DataContainer> result = mockStorage.hvals(DataContainerImpl.from(key));
    response.set(result == null ? new ArrayList<byte[]>(0) : DataContainerImpl.toBytes(result));
    return response;
  }

  @Override
  public Response<List<byte[]>> hvals(final byte[] key) {
    Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
    final Collection<DataContainer> result = mockStorage.hvals(DataContainerImpl.from(key));
    response.set(result == null ? new ArrayList<byte[]>(0) : DataContainerImpl.toBytes(result));
    return response;
  }

  @Override
  public Response<Long> hset(final String key, final String field, final String value) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.hset(DataContainerImpl.from(key), DataContainerImpl.from(field),
        DataContainerImpl.from(value)) ? 1L : 0L);
    return response;

  }

  @Override
  public Response<Long> hset(final byte[] key, final byte[] field, final byte[] value) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.hset(DataContainerImpl.from(key), DataContainerImpl.from(field),
        DataContainerImpl.from(value)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Long> hsetnx(final String key, final String field, final String value) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.hsetnx(DataContainerImpl.from(key), DataContainerImpl.from(field),
        DataContainerImpl.from(value)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Long> hsetnx(final byte[] key, final byte[] field, final byte[] value) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.hsetnx(DataContainerImpl.from(key), DataContainerImpl.from(field),
        DataContainerImpl.from(value)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<List<String>> hmget(final String key, final String... fields) {
    Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
    final List<byte[]> result = new ArrayList<byte[]>(fields.length);
    final List<DataContainer> hash = mockStorage.hmget(DataContainerImpl.from(key),
        DataContainerImpl.from(fields));
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
    Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
    final List<byte[]> result = new ArrayList<byte[]>(fields.length);
    final List<DataContainer> hash = mockStorage.hmget(DataContainerImpl.from(key),
        DataContainerImpl.from(fields));
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
    mockStorage.hmset(DataContainerImpl.from(key), DataContainerImpl.fromStringMap(hash));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<String> hmset(final byte[] key, final Map<byte[], byte[]> hash) {
    mockStorage.hmset(DataContainerImpl.from(key), DataContainerImpl.fromByteMap(hash));
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override
  public Response<Long> hincrBy(final String key, final String field, final long value) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.hincrBy(DataContainerImpl.from(key), DataContainerImpl.from(field),
        value));
    return response;
  }

  @Override
  public Response<Long> hincrBy(final byte[] key, final byte[] field, final long value) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.hincrBy(DataContainerImpl.from(key), DataContainerImpl.from(field),
        value));
    return response;
  }

  @Override
  public Response<Double> hincrByFloat(final String key, final String field, final double
      increment) {
    Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
    response.set(mockStorage.hincrByFloat(DataContainerImpl.from(key), DataContainerImpl.from
        (field), increment).getBytes());
    return response;
  }

  @Override
  public Response<Double> hincrByFloat(final byte[] key, final byte[] field, final double
      increment) {
    Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
    response.set(mockStorage.hincrByFloat(DataContainerImpl.from(key), DataContainerImpl.from
        (field), increment).getBytes());
    return response;
  }

  @Override
  public Response<Long> hdel(final String key, final String... field) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.hdel(DataContainerImpl.from(key), DataContainerImpl.from(field)));
    return response;
  }

  @Override
  public Response<Long> hdel(final byte[] key, final byte[]... field) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.hdel(DataContainerImpl.from(key), DataContainerImpl.from(field)));
    return response;
  }

  @Override
  public Response<Boolean> hexists(final String key, final String field) {
    Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
    response.set(mockStorage.hexists(DataContainerImpl.from(key), DataContainerImpl.from(field))
        ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Boolean> hexists(final byte[] key, final byte[] field) {
    Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
    response.set(mockStorage.hexists(DataContainerImpl.from(key), DataContainerImpl.from(field))
        ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Long> hlen(final String key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.hlen(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<Long> hlen(final byte[] key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.hlen(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<Long> lpush(final String key, final String... string) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.lpush(DataContainerImpl.from(key), DataContainerImpl.from
        (string)));
    return response;
  }

  @Override
  public Response<Long> lpush(final byte[] key, final byte[]... string) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.lpush(DataContainerImpl.from(key), DataContainerImpl.from
        (string)));
    return response;
  }

  @Override
  public Response<String> lpop(final String key) {
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    final DataContainer result = mockStorage.lpop(DataContainerImpl.from(key));
    response.set(result == null ? null : result.getBytes());
    return response;
  }

  @Override
  public Response<byte[]> lpop(final byte[] key) {
    Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
    final DataContainer result = mockStorage.lpop(DataContainerImpl.from(key));
    response.set(result == null ? null : result.getBytes());
    return response;
  }

  @Override
  public Response<Long> llen(final String key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.llen(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<Long> llen(final byte[] key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.llen(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<List<String>> lrange(final String key, final long start, final long end) {
    Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
    response.set(DataContainerImpl.toBytes(mockStorage.lrange(DataContainerImpl.from(key), start,
        end)));
    return response;
  }

  @Override
  public Response<List<byte[]>> lrange(final byte[] key, final long start, final long end) {
    Response<List<byte[]>> response = new Response<List<byte[]>>(BuilderFactory.BYTE_ARRAY_LIST);
    response.set(DataContainerImpl.toBytes(mockStorage.lrange(DataContainerImpl.from(key), start,
        end)));
    return response;
  }

  @Override
  public void sync() {
    // do nothing
  }

  @Override
  public Response<Set<String>> keys(final String pattern) {
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
    response.set(DataContainerImpl.toBytes(mockStorage.keys(DataContainerImpl.from(pattern))));
    return response;
  }

  @Override
  public Response<Set<byte[]>> keys(final byte[] pattern) {
    Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
    response.set(DataContainerImpl.toBytes(mockStorage.keys(DataContainerImpl.from(pattern))));
    return response;
  }

  @Override
  public Response<Long> sadd(final String key, final String... member) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.sadd(DataContainerImpl.from(key), DataContainerImpl.from(member)));
    return response;
  }

  @Override
  public Response<Long> sadd(final byte[] key, final byte[]... member) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.sadd(DataContainerImpl.from(key), DataContainerImpl.from(member)));
    return response;
  }

  @Override
  public Response<Long> srem(final String key, final String... member) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.srem(DataContainerImpl.from(key), DataContainerImpl.from(member)));
    return response;
  }

  @Override
  public Response<Long> srem(final byte[] key, final byte[]... member) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.srem(DataContainerImpl.from(key), DataContainerImpl.from(member)));
    return response;
  }

  @Override
  public Response<Long> scard(final String key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.scard(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<Long> scard(final byte[] key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.scard(DataContainerImpl.from(key)));
    return response;
  }

  @Override
  public Response<Set<String>> sdiff(final String... keys) {
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
    response.set(
        DataContainerImpl.toBytes(
            mockStorage.sdiff(DataContainerImpl.from(keys))
        )
    );
    return response;
  }

  @Override
  public Response<Set<byte[]>> sdiff(final byte[]... keys) {
    Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
    response.set(
        DataContainerImpl.toBytes(
            mockStorage.sdiff(DataContainerImpl.from(keys))
        )
    );
    return response;
  }

  @Override
  public Response<Long> sdiffstore(final String dstkey, final String... keys) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.sdiffstore(DataContainerImpl.from(dstkey), DataContainerImpl
        .from(keys)));
    return response;
  }

  @Override
  public Response<Long> sdiffstore(final byte[] dstkey, final byte[]... keys) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.sdiffstore(DataContainerImpl.from(dstkey), DataContainerImpl
        .from(keys)));
    return response;
  }

  @Override
  public Response<Set<String>> sinter(final String... keys) {
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
    response.set(
        DataContainerImpl.toBytes(
            mockStorage.sinter(DataContainerImpl.from(keys))
        )
    );
    return response;
  }

  @Override
  public Response<Set<byte[]>> sinter(final byte[]... keys) {
    Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
    response.set(
        DataContainerImpl.toBytes(
            mockStorage.sinter(DataContainerImpl.from(keys))
        )
    );
    return response;
  }

  @Override
  public Response<Long> sinterstore(final String dstkey, final String... keys) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.sinterstore(DataContainerImpl.from(dstkey), DataContainerImpl
        .from(keys)));
    return response;
  }

  @Override
  public Response<Long> sinterstore(final byte[] dstkey, final byte[]... keys) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.sinterstore(DataContainerImpl.from(dstkey), DataContainerImpl
        .from(keys)));
    return response;
  }

  @Override
  public Response<Boolean> sismember(final String key, final String member) {
    Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
    response.set(mockStorage.sismember(DataContainerImpl.from(key), DataContainerImpl.from
        (member)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Boolean> sismember(final byte[] key, final byte[] member) {
    Response<Boolean> response = new Response<Boolean>(BuilderFactory.BOOLEAN);
    response.set(mockStorage.sismember(DataContainerImpl.from(key), DataContainerImpl.from
        (member)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Long> smove(final String srckey, final String dstkey, final String member) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.smove(DataContainerImpl.from(srckey), DataContainerImpl.from(dstkey)
        , DataContainerImpl.from(member)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<Long> smove(final byte[] srckey, final byte[] dstkey, final byte[] member) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.smove(DataContainerImpl.from(srckey), DataContainerImpl.from(dstkey)
        , DataContainerImpl.from(member)) ? 1L : 0L);
    return response;
  }

  @Override
  public Response<String> spop(final String key) {
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(
        DataContainerImpl.toBytes(
            mockStorage.spop(DataContainerImpl.from(key))
        )
    );
    return response;
  }

  @Override
  public Response<byte[]> spop(final byte[] key) {
    Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
    response.set(
        DataContainerImpl.toBytes(
            mockStorage.spop(DataContainerImpl.from(key))
        )
    );
    return response;
  }

  @Override
  public Response<String> srandmember(final String key) {
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    final DataContainer result = mockStorage.srandmember(DataContainerImpl.from(key));
    response.set(result == null ? null : result.getBytes());
    return response;
  }

  @Override
  public Response<byte[]> srandmember(final byte[] key) {
    Response<byte[]> response = new Response<byte[]>(BuilderFactory.BYTE_ARRAY);
    final DataContainer result = mockStorage.srandmember(DataContainerImpl.from(key));
    response.set(result == null ? null : result.getBytes());
    return response;
  }

  @Override
  public Response<Set<String>> smembers(final String key) {
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
    final Set<DataContainer> members = mockStorage.smembers(DataContainerImpl.from(key));
    response.set(DataContainerImpl.toBytes(members));

    return response;
  }

  @Override
  public Response<Set<byte[]>> smembers(final byte[] key) {
    Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
    final Set<DataContainer> members = mockStorage.smembers(DataContainerImpl.from(key));
    response.set(DataContainerImpl.toBytes(members));

    return response;
  }

  @Override
  public Response<Set<String>> sunion(final String... keys) {
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
    response.set(DataContainerImpl.toBytes(mockStorage.sunion(DataContainerImpl.from(keys))));
    return response;
  }

  @Override
  public Response<Set<byte[]>> sunion(final byte[]... keys) {
    Response<Set<byte[]>> response = new Response<Set<byte[]>>(BuilderFactory.BYTE_ARRAY_ZSET);
    response.set(DataContainerImpl.toBytes(mockStorage.sunion(DataContainerImpl.from(keys))));
    return response;
  }

  @Override
  public Response<Long> sunionstore(final String dstkey, final String... keys) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.sunionstore(DataContainerImpl.from(dstkey), DataContainerImpl
        .from(keys)));
    return response;
  }

  @Override
  public Response<Long> sunionstore(final byte[] dstkey, final byte[]... keys) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set((long) mockStorage.sunionstore(DataContainerImpl.from(dstkey), DataContainerImpl
        .from(keys)));
    return response;
  }

  @Override public Response<Long> zadd(String key, double score, String member) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(
        mockStorage.zadd(DataContainerImpl.from(key), score, DataContainerImpl.from(member)));
    return response;
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(String key, String min, String max) {
    Response<Set<Tuple>> response = new Response<Set<Tuple>>(BuilderFactory.TUPLE_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrangeByScoreWithScores(DataContainerImpl.from(key), min, max)));
    return response;
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(String key, double min, double max) {
    Response<Set<Tuple>> response = new Response<Set<Tuple>>(BuilderFactory.TUPLE_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrangeByScoreWithScores(DataContainerImpl.from(key), min, max)));
    return response;
  }

  @Override
  public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max) {
    Response<Set<Tuple>> response = new Response<Set<Tuple>>(BuilderFactory.TUPLE_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrangeByScoreWithScores(DataContainerImpl.from(key), min, max)));
    return response;
  }

  @Override public Response<Set<String>> zrangeByScore(String key, String min, String max) {
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrangeByScore(DataContainerImpl.from(key), min, max)));
    return response;
  }

  @Override public Response<Set<String>> zrangeByScore(String key, double min, double max) {
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrangeByScore(DataContainerImpl.from(key), min, max)));
    return response;
  }

  @Override public Response<Set<String>> zrange(String key, long start, long end) {
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrange(DataContainerImpl.from(key), start, end)));
    return response;
  }

  @Override public Response<String> ltrim(String key, long start, long end) {
    mockStorage.ltrim(DataContainerImpl.from(key), start, end);
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override public Response<Long> zrank(String key, String member) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.zrank(DataContainerImpl.from(key), member));
    return response;
  }

  @Override public Response<Long> zremrangeByRank(String key, long start, long end) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.zremrangeByRank(DataContainerImpl.from(key), start, end));
    return response;
  }

  @Override public Response<Long> zcard(String key) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.zcard(DataContainerImpl.from(key)));
    return response;
  }

  @Override public Response<Double> zscore(String key, String member) {
    Response<Double> response = new Response<Double>(BuilderFactory.DOUBLE);
    DataContainer result = mockStorage.zscore(DataContainerImpl.from(key), member);
    response.set(result == null ? null : result.getBytes());
    return response;
  }

  @Override public Response<Set<Tuple>> zrangeWithScores(String key, long start, long end) {
    Response<Set<Tuple>> response = new Response<Set<Tuple>>(BuilderFactory.TUPLE_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrangeWithScores(DataContainerImpl.from(key), start, end)));
    return response;
  }

  @Override public Response<Set<Tuple>> zrevrangeWithScores(String key, long start, long end) {
    Response<Set<Tuple>> response = new Response<Set<Tuple>>(BuilderFactory.TUPLE_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrevrangeWithScores(DataContainerImpl.from(key), start, end)));
    return response;
  }

  @Override public Response<Set<String>> zrevrangeByScore(String key, String max, String min) {
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_SET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrevrangeByScore(DataContainerImpl.from(key), max, min)));
    return response;
  }

  @Override public Response<Set<String>> zrevrangeByScore(String key, double max, double min) {
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrevrangeByScore(DataContainerImpl.from(key), max, min)));
    return response;
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min) {
    Response<Set<Tuple>> response = new Response<Set<Tuple>>(BuilderFactory.TUPLE_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrevrangeByScoreWithScores(DataContainerImpl.from(key), max, min)));
    return response;
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min) {
    Response<Set<Tuple>> response = new Response<Set<Tuple>>(BuilderFactory.TUPLE_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrevrangeByScoreWithScores(DataContainerImpl.from(key), max, min)));
    return response;
  }

  @Override
  public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min, int
      offset, int count) {
    Response<Set<Tuple>> response = new Response<Set<Tuple>>(BuilderFactory.TUPLE_ZSET);
    response.set(DataContainerImpl.toBytes(mockStorage.zrevrangeByScoreWithScores(
        DataContainerImpl.from(key), max, min, offset, count)));
    return response;
  }

  @Override public Response<Set<String>> zrevrange(String key, long start, long end) {
    Response<Set<String>> response = new Response<Set<String>>(BuilderFactory.STRING_ZSET);
    response.set(DataContainerImpl.toBytes(
        mockStorage.zrevrange(DataContainerImpl.from(key), start, end)));
    return response;
  }

  @Override public Response<Long> zremrangeByScore(String key, String start, String end) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.zremrangeByScore(DataContainerImpl.from(key), start, end));
    return response;
  }

  @Override public Response<Long> zremrangeByScore(String key, double start, double end) {
    Response<Long> response = new Response<Long>(BuilderFactory.LONG);
    response.set(mockStorage.zremrangeByScore(DataContainerImpl.from(key), start, end));
    return response;
  }

  @Override public Response<String> multi() {
    MockStorage tmpMockStorage = mockStorage.cloneStorage();
    oldMockStorage = mockStorage;
    mockStorage = tmpMockStorage;
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }

  @Override public Response<List<Object>> exec() {
    oldMockStorage = null;
    Response<List<Object>> response = new Response<List<Object>>(OBJECT_LIST);
    response.set(null);
    return response;
  }

  @Override public Response<String> discard() {
    mockStorage = oldMockStorage;
    oldMockStorage = null;
    Response<String> response = new Response<String>(BuilderFactory.STRING);
    response.set(OK_RESPONSE);
    return response;
  }
}
