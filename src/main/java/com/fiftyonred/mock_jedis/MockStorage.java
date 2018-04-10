package com.fiftyonred.mock_jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fiftyonred.utils.WildcardMatcher;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.exceptions.JedisDataException;

import static com.fiftyonred.mock_jedis.DataContainerImpl.CHARSET;

@SuppressWarnings("WeakerAccess")
public class MockStorage {

  private static final int NUM_DBS = 16;
  public static final long MILLISECONDS_IN_SECOND = 1000L;

  private final WildcardMatcher wildcardMatcher = new WildcardMatcher();
  private final List<Map<DataContainer, KeyInformation>> allKeys;
  private final List<Map<DataContainer, DataContainer>> allStorage;
  private final List<Map<DataContainer, Map<DataContainer, DataContainer>>> allHashStorage;
  private final List<Map<DataContainer, List<DataContainer>>> allListStorage;
  private final List<Map<DataContainer, Set<DataContainer>>> allSetStorage;
  private final List<Map<DataContainer, NavigableSet<DataContainerWithScore>>> allSortedSetStorage;

  private int currentDB;
  private Map<DataContainer, KeyInformation> keys;
  private Map<DataContainer, DataContainer> storage;
  private Map<DataContainer, Map<DataContainer, DataContainer>> hashStorage;
  private Map<DataContainer, List<DataContainer>> listStorage;
  private Map<DataContainer, Set<DataContainer>> setStorage;
  private Map<DataContainer, NavigableSet<DataContainerWithScore>> sortedSetStorage;

  public MockStorage() {
    allKeys = new ArrayList<Map<DataContainer, KeyInformation>>(NUM_DBS);
    allStorage = new ArrayList<Map<DataContainer, DataContainer>>(NUM_DBS);
    allHashStorage = new ArrayList<Map<DataContainer, Map<DataContainer, DataContainer>>>(NUM_DBS);
    allListStorage = new ArrayList<Map<DataContainer, List<DataContainer>>>(NUM_DBS);
    allSetStorage = new ArrayList<Map<DataContainer, Set<DataContainer>>>(NUM_DBS);
    allSortedSetStorage = new ArrayList<Map<DataContainer, NavigableSet<DataContainerWithScore>>>
        (NUM_DBS);
    for (int i = 0; i < NUM_DBS; ++i) {
      allKeys.add(new HashMap<DataContainer, KeyInformation>());
      allStorage.add(new HashMap<DataContainer, DataContainer>());
      allHashStorage.add(new HashMap<DataContainer, Map<DataContainer, DataContainer>>());
      allListStorage.add(new HashMap<DataContainer, List<DataContainer>>());
      allSetStorage.add(new HashMap<DataContainer, Set<DataContainer>>());
      allSortedSetStorage.add(new HashMap<DataContainer, NavigableSet<DataContainerWithScore>>());
    }
    select(0);
  }

  public int getCurrentDB() {
    return currentDB;
  }

  private static <T> T getRandomElementFromSet(final Set<T> set) {
    return (T) set.toArray()[(int) (Math.random() * set.size())];
  }

  public int dbSize() {
    return keys.size();
  }

  public synchronized void flushAll() {
    for (int dbNum = 0; dbNum < NUM_DBS; ++dbNum) {
      allKeys.get(dbNum).clear();
      allStorage.get(dbNum).clear();
      allHashStorage.get(dbNum).clear();
      allListStorage.get(dbNum).clear();
    }
  }

  public synchronized void flushDB() {
    keys.clear();
    storage.clear();
    hashStorage.clear();
    listStorage.clear();
  }

  public synchronized void rename(final DataContainerImpl oldkey, final DataContainer newkey) {
    if (oldkey.equals(newkey)) {
      throw new JedisDataException("ERR source and destination objects are the same");
    }
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
      case SET:
        setStorage.put(newkey, setStorage.get(oldkey));
        setStorage.remove(oldkey);
        break;
      case SORTED_SET:
        sortedSetStorage.put(newkey, sortedSetStorage.get(oldkey));
        sortedSetStorage.remove(oldkey);
        break;
      case STRING:
      default:
        storage.put(newkey, storage.get(oldkey));
        storage.remove(oldkey);
    }
    keys.put(newkey, info);
    keys.remove(oldkey);
  }

  public synchronized boolean renamenx(DataContainerImpl oldkey, DataContainer newkey) {
    if (oldkey.equals(newkey)) {
      throw new JedisDataException("ERR source and destination objects are the same");
    }
    KeyInformation newInfo = keys.get(newkey);
    if (newInfo == null) {
      rename(oldkey, newkey);
      return true;
    }
    return false;
  }

  public synchronized void set(DataContainer key, DataContainer value) {
    createOrUpdateKey(key, KeyType.STRING, true);
    storage.put(key, value);
  }

  public synchronized boolean setnx(DataContainer key, DataContainer value) {
    DataContainer result = getContainerFromStorage(key, false);
    if (result == null) {
      set(key, value);
      return true;
    }
    return false;
  }

  public DataContainer get(DataContainer key) {
    return getContainerFromStorage(key, false);
  }

  public synchronized DataContainer getSet(DataContainer key, DataContainer value) {
    DataContainer result = get(key);
    set(key, value);
    return result;
  }

  public boolean exists(DataContainer key) {
    return keys.containsKey(key);
  }

  public KeyType type(DataContainer key) {
    KeyInformation info = keys.get(key);
    return info == null ? null : info.getType();
  }

  public synchronized boolean move(DataContainer key, int dbIndex) {
    if (dbIndex < 0 || dbIndex >= NUM_DBS) {
      throw new JedisDataException("ERR index out of range");
    }
    KeyInformation info = keys.get(key);
    if (info == null) {
      return false;
    } else {
      KeyInformation infoNew = allKeys.get(dbIndex).get(key);
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
        return true;
      } else {
        return false;
      }
    }
  }

  public synchronized DataContainer randomKey() {
    return keys.isEmpty() ? null : getRandomElementFromSet(keys.keySet());
  }

  public synchronized void select(int dbIndex) {
    if (dbIndex < 0 || dbIndex >= NUM_DBS) {
      throw new JedisDataException("ERR invalid DB index");
    }
    currentDB = dbIndex;
    keys = allKeys.get(dbIndex);
    storage = allStorage.get(dbIndex);
    hashStorage = allHashStorage.get(dbIndex);
    listStorage = allListStorage.get(dbIndex);
    setStorage = allSetStorage.get(dbIndex);
    sortedSetStorage = allSortedSetStorage.get(dbIndex);
  }

  public synchronized boolean pexpireAt(DataContainer key, long millisecondsTimestamp) {
    KeyInformation info = keys.get(key);
    if (info == null || info.isTTLSetAndKeyExpired()) {
      return false;
    }
    info.setExpiration(millisecondsTimestamp);
    return true;
  }

  public synchronized void psetex(DataContainer key, int milliseconds, DataContainer value) {
    psetex(key, (long) milliseconds, value);
  }

  public synchronized void psetex(DataContainer key, long milliseconds, DataContainer value) {
    set(key, value);
    pexpireAt(key, System.currentTimeMillis() + milliseconds);
  }

  public long ttl(DataContainer key) {
    Long pttlInResponse = pttl(key);
    if (pttlInResponse == -1L) {
      return pttlInResponse;
    }
    if (pttlInResponse > 0L && pttlInResponse < MILLISECONDS_IN_SECOND) {
      pttlInResponse = MILLISECONDS_IN_SECOND;
    }
    return pttlInResponse / MILLISECONDS_IN_SECOND;
  }

  public synchronized long append(DataContainer key, DataContainerImpl value) {
    DataContainer container = getContainerFromStorage(key, true);
    DataContainer newVal = container.append(value);
    set(key, newVal);
    return newVal.getString().length();
  }

  public synchronized long pttl(DataContainer key) {
    KeyInformation info = keys.get(key);
    return info == null ? -1L : info.getTTL();
  }

  public synchronized boolean persist(DataContainer key) {
    KeyInformation info = keys.get(key);
    if (info.getTTL() == -1) {
      return false;
    }
    info.setExpiration(-1L);
    return true;
  }

  public synchronized List<DataContainer> mget(DataContainerImpl... keys) {
    if (keys.length <= 0) {
      throw new JedisDataException("ERR wrong number of arguments for 'mget' command");
    }

    List<DataContainer> result = new ArrayList<DataContainer>(keys.length);
    for (DataContainer key : keys) {
      result.add(getContainerFromStorage(key, false));
    }
    return result;
  }

  public synchronized void mset(DataContainerImpl... keysvalues) {
    int l = keysvalues.length;
    if (l <= 0 || l % 2 != 0) {
      throw new JedisDataException("ERR wrong number of arguments for 'mset' command");
    }

    for (int i = 0; i < l; i += 2) {
      set(keysvalues[i], keysvalues[i + 1]);
    }
  }

  public synchronized boolean msetnx(DataContainerImpl... keysvalues) {
    int l = keysvalues.length;
    if (l <= 0 || l % 2 != 0) {
      throw new JedisDataException("ERR wrong number of arguments for 'msetnx' command");
    }

    boolean result = true;
    for (int i = 0; i < l; i += 2) {
      if (!setnx(keysvalues[i], keysvalues[i + 1])) {
        result = false;
      }
    }
    return result;
  }

  public synchronized long incrBy(DataContainer key, long integer) {
    DataContainer val = getContainerFromStorage(key, true);

    long oldValue;
    try {
      oldValue = val == null || val.getString().isEmpty() ? 0L : Long.parseLong(val.getString());
    } catch (NumberFormatException ignored) {
      throw new JedisDataException("ERR value is not an integer or out of range");
    }

    // check for overflow
    if (oldValue > 0L ? integer > Long.MAX_VALUE - oldValue : integer < Long.MIN_VALUE - oldValue) {
      throw new JedisDataException("ERR value is not an integer or out of range");
    }

    long result = oldValue + integer;
    storage.put(key, DataContainerImpl.from(Long.toString(result)));
    return result;
  }

  public synchronized DataContainer incrByFloat(DataContainer key, double increment) {
    DataContainer val = getContainerFromStorage(key, true);
    double result;
    try {
      result = val == null || val.getString().isEmpty() ? increment : Double.parseDouble(val
          .getString()) + increment;
    } catch (NumberFormatException ignored) {
      throw new JedisDataException("ERR value is not a valid float");
    }
    val = DataContainerImpl.from(Double.toString(result));
    storage.put(key, val);
    return val;
  }

  private static Comparator<DataContainer> makeComparator(Collection<String> params) {
    Comparator<DataContainer> comparator;
    final int direction = params.contains(Protocol.Keyword.DESC.name().toLowerCase()) ? -1 : 1;
    if (params.contains(Protocol.Keyword.ALPHA.name().toLowerCase())) {
      comparator = new Comparator<DataContainer>() {
        @Override
        public int compare(DataContainer o1, DataContainer o2) {
          return o1.compareTo(o2) * direction;
        }
      };
    } else {
      comparator = new Comparator<DataContainer>() {
        @Override
        public int compare(DataContainer o1, DataContainer o2) {
          Long i1, i2;
          try {
            i1 = Long.parseLong(o1.getString());
            i2 = Long.parseLong(o2.getString());
          } catch (NumberFormatException e) {
            throw new JedisDataException("ERR One or more scores can't be converted into double");
          }
          return i1.compareTo(i2) * direction;
        }
      };
    }
    return comparator;
  }

  public List<DataContainer> sort(DataContainer key, SortingParams sortingParameters) {
    List<DataContainer> result = new ArrayList<DataContainer>();
    KeyInformation info = keys.get(key);
    if (info != null) {
      switch (info.getType()) {
        case LIST:
          result.addAll(listStorage.get(key));
          break;
        case SET:
          result.addAll(setStorage.get(key));
          break;
        case SORTED_SET:
          result.addAll(sortedSetStorage.get(key));
          break;
        default:
          throw new JedisDataException("WRONGTYPE Operation against a key holding the wrong kind " +
              "of value");
      }
    }

    List<String> params = convertToStrings(sortingParameters.getParams());

    Collections.sort(result, makeComparator(params));

    List<DataContainer> filteredResult = new ArrayList<DataContainer>(result.size());
    int limitpos = params.indexOf(Protocol.Keyword.LIMIT.name().toLowerCase());
    if (limitpos >= 0) {
      int start = Math.max(Integer.parseInt(params.get(limitpos + 1)), 0);
      int end = Math.min(Integer.parseInt(params.get(limitpos + 2)) + start, result.size());
      filteredResult.addAll(result.subList(start, end));
    } else {
      filteredResult.addAll(result);
    }
    return filteredResult;
  }

  protected static List<String> convertToStrings(Collection<byte[]> collection) {
    List<String> result = new ArrayList<String>(collection.size());
    for (byte[] entry : collection) {
      result.add(new String(entry, CHARSET));
    }
    return result;
  }

  public synchronized int sort(DataContainer key, SortingParams sortingParameters, DataContainer
      dstkey) {
    List<DataContainer> sorted = sort(key, sortingParameters);

    del(dstkey);
    keys.put(dstkey, new KeyInformation(KeyType.LIST));
    listStorage.put(dstkey, sorted);

    return sorted.size();
  }

  public int strlen(DataContainer key) {
    DataContainer val = getContainerFromStorage(key, false);
    return val == null ? 0 : val.getString().length();
  }

  public long del(DataContainer... keys) {
    long result = 0L;
    for (DataContainer key : keys) {
      if (del(key)) {
        ++result;
      }
    }

    return result;
  }

  public synchronized boolean del(DataContainer key) {
    KeyInformation info = keys.remove(key);
    if (info == null) {
      return false;
    }
    switch (info.getType()) {
      case HASH:
        hashStorage.remove(key);
        break;
      case LIST:
        listStorage.remove(key);
        break;
      case SET:
        setStorage.remove(key);
        break;
      case STRING:
      default:
        storage.remove(key);
    }
    return true;
  }

  public DataContainer hget(DataContainer key, DataContainer field) {
    Map<DataContainer, DataContainer> result = getHashFromStorage(key, false);
    if (result == null) {
      return null;
    }
    return result.get(field);
  }

  public Map<DataContainer, DataContainer> hgetAll(DataContainer key) {
    return getHashFromStorage(key, false);
  }

  public Set<DataContainer> hkeys(DataContainer key) {
    Map<DataContainer, DataContainer> result = getHashFromStorage(key, false);
    return result == null ? null : result.keySet();
  }

  public Collection<DataContainer> hvals(DataContainer key) {
    Map<DataContainer, DataContainer> result = getHashFromStorage(key, false);
    return result == null ? null : result.values();
  }

  public synchronized boolean hset(DataContainer key, DataContainer field, DataContainer value) {
    Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);
    DataContainer previousValue = m.put(field, value);
    return previousValue == null;
  }

  public synchronized boolean hsetnx(DataContainer key, DataContainer field, DataContainer value) {
    Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);
    if (m.containsKey(field)) {
      return false;
    }
    m.put(field, value);
    return true;
  }

  public List<DataContainer> hmget(DataContainer key, DataContainer... fields) {
    Map<DataContainer, DataContainer> hash = getHashFromStorage(key, false);
    if (hash == null) {
      return null;
    }

    List<DataContainer> result = new ArrayList<DataContainer>();
    for (DataContainer field : fields) {
      result.add(hash.get(field));
    }
    return result;
  }

  public synchronized void hmset(DataContainer key, Map<DataContainer, DataContainer> hash) {
    Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);
    for (Map.Entry<DataContainer, DataContainer> e : hash.entrySet()) {
      m.put(e.getKey(), e.getValue());
    }
  }

  public synchronized long hincrBy(DataContainer key, DataContainer field, long value) {
    Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);

    DataContainer val = m.get(field);
    if (val == null) {
      val = DataContainerImpl.from(Long.valueOf(0L).toString());
    }
    long result;
    try {
      result = Long.valueOf(val.getString()) + value;
    } catch (NumberFormatException ignored) {
      throw new JedisDataException("ERR value is not an integer or out of range");
    }
    m.put(field, DataContainerImpl.from(Long.toString(result)));
    return result;
  }

  public synchronized DataContainer hincrByFloat(DataContainer key, DataContainer field, double
      increment) {
    Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);

    DataContainer val = m.get(field);
    if (val == null) {
      val = DataContainerImpl.from(Double.toString(0.0D));
    }
    double result;
    try {
      result = Double.parseDouble(val.toString()) + increment;
    } catch (NumberFormatException ignored) {
      throw new JedisDataException("ERR value is not a valid float");
    }
    DataContainer resultContainer = DataContainerImpl.from(Double.toString(result));
    m.put(field, resultContainer);
    return resultContainer;
  }

  public synchronized long hdel(DataContainer key, DataContainer... fields) {
    Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);
    long result = 0L;
    for (DataContainer currentField : fields) {
      if (m.remove(currentField) != null) {
        ++result;
      }
    }
    return result;
  }

  public boolean hexists(DataContainer key, DataContainer field) {
    Map<DataContainer, DataContainer> hash = getHashFromStorage(key, false);
    return hash != null && hash.containsKey(field);
  }

  public int hlen(DataContainer key) {
    Map<DataContainer, DataContainer> hash = getHashFromStorage(key, false);
    return hash == null ? 0 : hash.size();
  }

  public synchronized int lpush(DataContainer key, DataContainer... string) {
    List<DataContainer> list = getListFromStorage(key, true);
    if (list == null) {
      list = new ArrayList<DataContainer>();
      listStorage.put(key, list);
    }
    Collections.addAll(list, string);
    return list.size();
  }

  public synchronized DataContainer lpop(DataContainer key) {
    List<DataContainer> list = getListFromStorage(key, true);
    return list == null || list.isEmpty() ? null : list.remove(list.size() - 1);
  }

  public synchronized int llen(DataContainer key) {
    List<DataContainer> list = getListFromStorage(key, false);
    return list == null ? 0 : list.size();
  }

  public synchronized List<DataContainer> lrange(DataContainer key, long start, long end) {
    List<DataContainer> full = getListFromStorage(key, false);
    return (List<DataContainer>) slice(full, new ArrayList<DataContainer>(), start, end);
  }

  public synchronized void ltrim(DataContainer key, long start, long end) {
    List<DataContainer> full = getListFromStorage(key, false);
    List<DataContainer> slice = (List<DataContainer>) slice(full, new ArrayList<DataContainer>(),
        start, end);
    listStorage.put(key, slice);
  }

  private <T extends DataContainer> Collection<T> slice(List<T> input, Collection<T> output,
      long start, long end) {
    if (start < 0L) {
      start = Math.max(input.size() + start, 0L);
    }
    if (end < 0L) {
      end = input.size() + end;
    }
    if (start > input.size() || start > end) {
      return output;
    }

    end = Math.min(input.size() - 1, end);

    for (int i = (int) start; i <= end; i++) {
      output.add(input.get(i));
    }
    return output;
  }

  public Set<DataContainer> keys(DataContainer pattern) {
    Set<DataContainer> result = new HashSet<DataContainer>();
    for (DataContainer key : keys.keySet()) {
      if (wildcardMatcher.match(key.getString(), pattern.getString())) {
        result.add(key);
      }
    }
    return result;
  }

  protected boolean createOrUpdateKey(DataContainer key, KeyType type, boolean resetTTL) {
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

  protected DataContainer getContainerFromStorage(DataContainer key, boolean createIfNotExist) {
    KeyInformation info = keys.get(key);
    if (info == null) {
      if (createIfNotExist) {
        createOrUpdateKey(key, KeyType.STRING, true);
        DataContainerImpl container = DataContainerImpl.from("");
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

  protected Map<DataContainer, DataContainer> getHashFromStorage(DataContainer key, boolean
      createIfNotExist) {
    KeyInformation info = keys.get(key);
    if (info == null) {
      if (createIfNotExist) {
        createOrUpdateKey(key, KeyType.HASH, false);
        Map<DataContainer, DataContainer> result = new HashMap<DataContainer, DataContainer>();
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

  protected List<DataContainer> getListFromStorage(DataContainer key, boolean createIfNotExist) {
    KeyInformation info = keys.get(key);
    if (info == null) {
      if (createIfNotExist) {
        createOrUpdateKey(key, KeyType.LIST, false);
        List<DataContainer> result = new ArrayList<DataContainer>();
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

  protected Set<DataContainer> getSetFromStorage(DataContainer key, boolean createIfNotExist) {
    KeyInformation info = keys.get(key);
    if (info == null) {
      if (createIfNotExist) {
        createOrUpdateKey(key, KeyType.SET, false);
        Set<DataContainer> result = new HashSet<DataContainer>();
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

  protected NavigableSet<DataContainerWithScore> getSortedSetFromStorage(DataContainer key,
      boolean createIfNotExist) {
    KeyInformation info = keys.get(key);
    if (info == null) {
      if (createIfNotExist) {
        createOrUpdateKey(key, KeyType.SORTED_SET, false);
        NavigableSet<DataContainerWithScore> result = new TreeSet<DataContainerWithScore>();
        sortedSetStorage.put(key, result);
        return result;
      }
      return null; // no such key exists
    }
    if (info.getType() != KeyType.SORTED_SET) {
      throw new JedisDataException("ERR Operation against a key holding the wrong kind of value");
    }
    if (info.isTTLSetAndKeyExpired()) {
      sortedSetStorage.remove(key);
      keys.remove(key);
      return null;
    }
    return sortedSetStorage.get(key);
  }

  public synchronized long sadd(DataContainer key, DataContainer... members) {
    Set<DataContainer> set = getSetFromStorage(key, true);

    long added = 0L;
    for (DataContainer s : members) {
      if (set.add(s)) {
        added++;
      }
    }

    return added;
  }

  public synchronized long zadd(DataContainer key, double score, DataContainer member) {
    SortedSet<DataContainerWithScore> set = getSortedSetFromStorage(key, true);
    return set.add(new DataContainerWithScore(member, score)) ? 1 : 0;
  }

  public synchronized long srem(DataContainer key, DataContainer... member) {
    Set<DataContainer> set = getSetFromStorage(key, true);
    long removed = 0L;
    for (DataContainer s : member) {
      if (set.remove(s)) {
        ++removed;
      }
    }
    return removed;
  }

  public synchronized int scard(DataContainer key) {
    Set<DataContainer> set = getSetFromStorage(key, true);
    return set.size();
  }

  public synchronized Set<DataContainer> sdiff(DataContainerImpl... keys) {
    int l = keys.length;
    if (l <= 0) {
      throw new JedisDataException("ERR wrong number of arguments for 'sdiff' command");
    }
    Set<DataContainer> result = new HashSet<DataContainer>(getSetFromStorage(keys[0], true));
    for (int i = 1; i < l; ++i) {
      Set<DataContainer> set = getSetFromStorage(keys[i], true);
      result.removeAll(set);
    }

    return result;
  }

  public synchronized int sdiffstore(DataContainer dstKey, DataContainerImpl... keys) {
    if (keys.length <= 0) {
      throw new JedisDataException("ERR wrong number of arguments for 'sdiff' command");
    }
    Set<DataContainer> diff = sdiff(keys);
    Set<DataContainer> dst = getSetFromStorage(dstKey, true);
    if (!dst.isEmpty()) {
      dst.clear();
    }
    dst.addAll(diff);
    return diff.size();
  }

  public synchronized Set<DataContainer> sinter(DataContainerImpl... keys) {
    int l = keys.length;
    if (l <= 0) {
      throw new JedisDataException("ERR wrong number of arguments for 'sinter' command");
    }

    Set<DataContainer> firstSet = new HashSet<DataContainer>(getSetFromStorage(keys[0], true));
    for (int i = 1; i < l; ++i) {
      Set<DataContainer> set = getSetFromStorage(keys[i], true);
      firstSet.retainAll(set);
    }

    return firstSet;
  }

  public synchronized int sinterstore(DataContainer dstKey, DataContainerImpl... keys) {
    if (keys.length <= 0) {
      throw new JedisDataException("ERR wrong number of arguments for 'sinterstore' command");
    }
    Set<DataContainer> inter = sinter(keys);
    Set<DataContainer> dst = getSetFromStorage(dstKey, true);
    if (!dst.isEmpty()) {
      dst.clear();
    }
    dst.addAll(inter);
    return inter.size();
  }

  public boolean sismember(DataContainer key, DataContainer member) {
    Set<DataContainer> set = getSetFromStorage(key, false);
    return set != null && set.contains(member);
  }

  public synchronized boolean smove(DataContainer srckey, DataContainer dstkey, DataContainer
      member) {
    Set<DataContainer> src = getSetFromStorage(srckey, false);
    Set<DataContainer> dst = getSetFromStorage(dstkey, true);
    if (!src.remove(member)) {
      return false;
    }
    dst.add(member);
    return true;
  }

  public synchronized DataContainer spop(DataContainer key) {
    DataContainer member = srandmember(key);
    if (member != null) {
      Set<DataContainer> src = getSetFromStorage(key, false);
      src.remove(member);
    }
    return member;
  }

  public synchronized DataContainer srandmember(DataContainer key) {
    Set<DataContainer> src = getSetFromStorage(key, false);
    return src == null ? null : getRandomElementFromSet(src);
  }

  public synchronized Set<DataContainer> smembers(DataContainer key) {
    return getSetFromStorage(key, true);
  }

  public synchronized Set<DataContainer> sunion(DataContainerImpl... keys) {
    int l = keys.length;
    if (l <= 0) {
      throw new JedisDataException("ERR wrong number of arguments for 'sunion' command");
    }
    Set<DataContainer> result = new HashSet<DataContainer>(getSetFromStorage(keys[0], true));
    for (int i = 1; i < l; ++i) {
      Set<DataContainer> set = getSetFromStorage(keys[i], true);
      result.addAll(set);
    }

    return result;
  }

  public synchronized int sunionstore(DataContainer dstkey, DataContainerImpl... keys) {
    if (keys.length <= 0) {
      throw new JedisDataException("ERR wrong number of arguments for 'sunionstore' command");
    }
    Set<DataContainer> inter = sunion(keys);
    Set<DataContainer> dst = getSetFromStorage(dstkey, true);
    if (!dst.isEmpty()) {
      dst.clear();
    }
    dst.addAll(inter);
    return inter.size();
  }

  public synchronized Set<DataContainerWithScore> zrangeByScoreWithScores(DataContainer key,
      String min, String max) {
    // TODO: Handle non-inclusive min/max
    double doubleMin = min.equals("-inf") ? Double.NEGATIVE_INFINITY : Double.parseDouble(min);
    double doubleMax = max.equals("+inf") ? Double.POSITIVE_INFINITY : Double.parseDouble(max);
    return zrangeByScoreWithScores(key, doubleMin, doubleMax);
  }

  public Set<DataContainerWithScore> zrangeByScoreWithScores(DataContainer key, double min,
      double max) {
    NavigableSet<DataContainerWithScore> full = getSortedSetFromStorage(key, true);
    if (full.isEmpty()) {
      return Collections.emptySet();
    }
    DataContainerWithScore first = full.first();
    DataContainerWithScore last = full.last();
    if (min > max || min > last.getScore() || max < first.getScore()) {
      return Collections.emptySet();
    }
    // find first elem with score > min
    Iterator<DataContainerWithScore> iterator = full.iterator();
    while (iterator.hasNext() && min > first.getScore()) {
      first = iterator.next();
    }
    Iterator<DataContainerWithScore> descendingIterator = full.descendingIterator();
    while (iterator.hasNext() && max < last.getScore()) {
      last = descendingIterator.next();
    }
    return new HashSet<DataContainerWithScore>(full.subSet(first, true, last, true));
  }

  public Set<DataContainer> zrangeByScore(DataContainer key, String min, String max) {
    // TODO: Handle non-inclusive min/max
    double doubleMin = min.equals("-inf") ? Double.NEGATIVE_INFINITY : Double.parseDouble(min);
    double doubleMax = max.equals("+inf") ? Double.POSITIVE_INFINITY : Double.parseDouble(max);
    return zrangeByScore(key, doubleMin, doubleMax);
  }

  public Set<DataContainer> zrangeByScore(DataContainer key, double min, double max) {
    Set<DataContainerWithScore> rangeWithScores = zrangeByScoreWithScores(key, min, max);
    Set<DataContainer> items = new HashSet<DataContainer>(rangeWithScores.size());
    for (DataContainerWithScore containerWithScore : rangeWithScores) {
      items.add(DataContainerImpl.from(containerWithScore.getString()));
    }
    return items;
  }

  public Set<DataContainer> zrange(DataContainerImpl key, long start, long end) {
    NavigableSet<DataContainerWithScore> full = getSortedSetFromStorage(key, true);
    Set<DataContainerWithScore> rangeWithScores = (Set<DataContainerWithScore>) slice(
        new ArrayList<DataContainerWithScore>(full), new HashSet<DataContainerWithScore>(),
        start, end);
    Set<DataContainer> items = new HashSet<DataContainer>(rangeWithScores.size());
    for (DataContainerWithScore containerWithScore : rangeWithScores) {
      items.add(DataContainerImpl.from(containerWithScore.getString()));
    }
    return items;
  }

  public Long zrank(DataContainerImpl key, String member) {
    NavigableSet<DataContainerWithScore> full = getSortedSetFromStorage(key, true);
    Iterator<DataContainerWithScore> iterator = full.iterator();
    long index = 0;
    while (iterator.hasNext()) {
      if (iterator.next().getString().equals(member)) {
        return index;
      }
      index++;
    }
    return null;
  }
}

