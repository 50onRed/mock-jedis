package com.fiftyonred.mock_jedis;

import com.fiftyonred.utils.WildcardMatcher;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.*;

import static com.fiftyonred.mock_jedis.DataContainer.CHARSET;

public class MockStorage {

	private static final int NUM_DBS = 16;
	public static final long MILLISECONDS_IN_SECOND = 1000L;

	private final WildcardMatcher wildcardMatcher = new WildcardMatcher();
	private final List<Map<DataContainer, KeyInformation>> allKeys;
	private final List<Map<DataContainer, DataContainer>> allStorage;
	private final List<Map<DataContainer, Map<DataContainer, DataContainer>>> allHashStorage;
	private final List<Map<DataContainer, List<DataContainer>>> allListStorage;
	private final List<Map<DataContainer, Set<DataContainer>>> allSetStorage;

	private int currentDB;
	private Map<DataContainer, KeyInformation> keys;
	private Map<DataContainer, DataContainer> storage;
	private Map<DataContainer, Map<DataContainer, DataContainer>> hashStorage;
	private Map<DataContainer, List<DataContainer>> listStorage;
	private Map<DataContainer, Set<DataContainer>> setStorage;

	public MockStorage() {
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

	public synchronized void rename(final DataContainer oldkey, final DataContainer newkey) {
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
			case STRING:
			default:
				storage.put(newkey, storage.get(oldkey));
				storage.remove(oldkey);
		}
		keys.put(newkey, info);
		keys.remove(oldkey);
	}

	public synchronized boolean renamenx(final DataContainer oldkey, final DataContainer newkey) {
		if (oldkey.equals(newkey)) {
			throw new JedisDataException("ERR source and destination objects are the same");
		}
		final KeyInformation newInfo = keys.get(newkey);
		if (newInfo == null) {
			rename(oldkey, newkey);
			return true;
		}
		return false;
	}

	public synchronized void set(final DataContainer key, final DataContainer value) {
		createOrUpdateKey(key, KeyType.STRING, true);
		storage.put(key, value);
	}

	public synchronized boolean setnx(final DataContainer key, final DataContainer value) {
		final DataContainer result = getContainerFromStorage(key, false);
		if (result == null) {
			set(key, value);
			return true;
		}
		return false;
	}

	public DataContainer get(final DataContainer key) {
		return getContainerFromStorage(key, false);
	}

	public synchronized DataContainer getSet(final DataContainer key, final DataContainer value) {
		final DataContainer result = get(key);
		set(key, value);
		return result;
	}

	public boolean exists(final DataContainer key) {
		return keys.containsKey(key);
	}

	public KeyType type(final DataContainer key) {
		final KeyInformation info = keys.get(key);
		return info == null ? null : info.getType();
	}

	public synchronized boolean move(final DataContainer key, final int dbIndex) {
		if (dbIndex < 0 || dbIndex >= NUM_DBS) {
			throw new JedisDataException("ERR index out of range");
		}
		final KeyInformation info = keys.get(key);
		if (info == null) {
			return false;
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
				return true;
			} else {
				return false;
			}
		}
	}

	public synchronized DataContainer randomKey() {
		return keys.isEmpty() ? null : getRandomElementFromSet(keys.keySet());
	}

	public synchronized void select(final int dbIndex) {
		if (dbIndex < 0 || dbIndex >= NUM_DBS) {
			throw new JedisDataException("ERR invalid DB index");
		}
		currentDB = dbIndex;
		keys = allKeys.get(dbIndex);
		storage = allStorage.get(dbIndex);
		hashStorage = allHashStorage.get(dbIndex);
		listStorage = allListStorage.get(dbIndex);
		setStorage = allSetStorage.get(dbIndex);
	}

	public synchronized boolean pexpireAt(final DataContainer key, final long millisecondsTimestamp) {
		final KeyInformation info = keys.get(key);
		if (info == null || info.isTTLSetAndKeyExpired()) {
			return false;
		}
		info.setExpiration(millisecondsTimestamp);
		return true;
	}

	public synchronized void psetex(final DataContainer key, final int milliseconds, final DataContainer value) {
		psetex(key, (long)milliseconds, value);
	}

	public synchronized void psetex(final DataContainer key, final long milliseconds, final DataContainer value) {
		set(key, value);
		pexpireAt(key, System.currentTimeMillis() + milliseconds);
	}

	public long ttl(final DataContainer key) {
		Long pttlInResponse = pttl(key);
		if (pttlInResponse == -1L) {
			return pttlInResponse;
		}
		if (pttlInResponse > 0L && pttlInResponse < MILLISECONDS_IN_SECOND) {
			pttlInResponse = MILLISECONDS_IN_SECOND;
		}
		return pttlInResponse / MILLISECONDS_IN_SECOND;
	}

	public synchronized long append(final DataContainer key, final DataContainer value) {
		final DataContainer container = getContainerFromStorage(key, true);
		final DataContainer newVal = container.append(value);
		set(key, newVal);
		return newVal.getString().length();
	}

	public synchronized long pttl(final DataContainer key) {
		final KeyInformation info = keys.get(key);
		return info == null ? -1L : info.getTTL();
	}

	public synchronized boolean persist(final DataContainer key) {
		final KeyInformation info = keys.get(key);
		if (info.getTTL() == -1) {
			return false;
		}
		info.setExpiration(-1L);
		return true;
	}

	public synchronized List<DataContainer> mget(final DataContainer... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'mget' command");
		}

		final List<DataContainer> result = new ArrayList<DataContainer>(keys.length);
		for (final DataContainer key : keys) {
			result.add(getContainerFromStorage(key, false));
		}
		return result;
	}

	public synchronized void mset(final DataContainer... keysvalues) {
		final int l = keysvalues.length;
		if (l <= 0 || l % 2 != 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'mset' command");
		}

		for (int i = 0; i < l; i += 2) {
			set(keysvalues[i], keysvalues[i + 1]);
		}
	}

	public synchronized boolean msetnx(final DataContainer... keysvalues) {
		final int l = keysvalues.length;
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

	public synchronized long incrBy(final DataContainer key, final long integer) {
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
		return result;
	}

	public synchronized DataContainer incrByFloat(final DataContainer key, final double increment) {
		DataContainer val = getContainerFromStorage(key, true);
		final double result;
		try {
			result = val == null || val.getString().isEmpty() ? increment : Double.parseDouble(val.getString()) + increment;
		} catch (final NumberFormatException ignored) {
			throw new JedisDataException("ERR value is not a valid float");
		}
		val = DataContainer.from(Double.toString(result));
		storage.put(key, val);
		return val;
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

	protected static List<String> convertToStrings(final Collection<byte[]> collection) {
		final List<String> result = new ArrayList<String>(collection.size());
		for (final byte[] entry : collection) {
			result.add(new String(entry, CHARSET));
		}
		return result;
	}

	public synchronized int sort(final DataContainer key, final SortingParams sortingParameters, final DataContainer dstkey) {
		final List<DataContainer> sorted = sort(key, sortingParameters);

		del(dstkey);
		keys.put(dstkey, new KeyInformation(KeyType.LIST));
		listStorage.put(dstkey, sorted);

		return sorted.size();
	}

	public int strlen(final DataContainer key) {
		final DataContainer val = getContainerFromStorage(key, false);
		return val == null ? 0 : val.getString().length();
	}

	public long del(final DataContainer... keys) {
		long result = 0L;
		for (final DataContainer key : keys) {
			if (del(key)) {
				++result;
			}
		}

		return result;
	}

	public synchronized boolean del(final DataContainer key) {
		final KeyInformation info = keys.remove(key);
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

	public DataContainer hget(final DataContainer key, final DataContainer field) {
		final Map<DataContainer, DataContainer> result = getHashFromStorage(key, false);
		if (result == null) {
			return null;
		}
		return result.get(field);
	}

	public Map<DataContainer, DataContainer> hgetAll(final DataContainer key) {
		return getHashFromStorage(key, false);
	}

	public Set<DataContainer> hkeys(final DataContainer key) {
		final Map<DataContainer, DataContainer> result = getHashFromStorage(key, false);
		return result == null ? null : result.keySet();
	}

	public Collection<DataContainer> hvals(final DataContainer key) {
		final Map<DataContainer, DataContainer> result = getHashFromStorage(key, false);
		return result == null ? null : result.values();
	}

	public synchronized boolean hset(final DataContainer key, final DataContainer field, final DataContainer value) {
		final Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);
		final DataContainer previousValue = m.put(field, value);
		return previousValue == null;
	}

	public synchronized boolean hsetnx(final DataContainer key, final DataContainer field, final DataContainer value) {
		final Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);
		if (m.containsKey(field)) {
			return false;
		}
		m.put(field, value);
		return true;
	}

	public List<DataContainer> hmget(final DataContainer key, final DataContainer... fields) {
		final Map<DataContainer, DataContainer> hash = getHashFromStorage(key, false);
		if (hash == null) {
			return null;
		}

		final List<DataContainer> result = new ArrayList<DataContainer>();
		for (final DataContainer field : fields) {
			result.add(hash.get(field));
		}
		return result;
	}

	public synchronized void hmset(final DataContainer key, final Map<DataContainer, DataContainer> hash) {
		final Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);
		for (final Map.Entry<DataContainer, DataContainer> e : hash.entrySet()) {
			m.put(e.getKey(), e.getValue());
		}
	}

	public synchronized long hincrBy(final DataContainer key, final DataContainer field, final long value) {
		final Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);

		DataContainer val = m.get(field);
		if (val == null) {
			val = DataContainer.from(Long.valueOf(0L).toString());
		}
		final long result;
		try {
			result = Long.valueOf(val.getString()) + value;
		} catch (final NumberFormatException ignored) {
			throw new JedisDataException("ERR value is not an integer or out of range");
		}
		m.put(field, DataContainer.from(Long.toString(result)));
		return result;
	}

	public synchronized DataContainer hincrByFloat(final DataContainer key, final DataContainer field, final double increment) {
		final Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);

		DataContainer val = m.get(field);
		if (val == null) {
			val = DataContainer.from(Double.toString(0.0D));
		}
		final double result;
		try {
			result = Double.parseDouble(val.toString()) + increment;
		} catch (final NumberFormatException ignored) {
			throw new JedisDataException("ERR value is not a valid float");
		}
		final DataContainer resultContainer = DataContainer.from(Double.toString(result));
		m.put(field, resultContainer);
		return resultContainer;
	}

	public synchronized long hdel(final DataContainer key, final DataContainer... fields) {
		final Map<DataContainer, DataContainer> m = getHashFromStorage(key, true);
		long result = 0L;
		for (final DataContainer currentField : fields) {
			if (m.remove(currentField) != null) {
				++result;
			}
		}
		return result;
	}

	public boolean hexists(final DataContainer key, final DataContainer field) {
		final Map<DataContainer, DataContainer> hash = getHashFromStorage(key, false);
		return hash != null && hash.containsKey(field);
	}

	public int hlen(final DataContainer key) {
		final Map<DataContainer, DataContainer> hash = getHashFromStorage(key, false);
		return hash == null ? 0 : hash.size();
	}

	public synchronized int lpush(final DataContainer key, final DataContainer... string) {
		List<DataContainer> list = getListFromStorage(key, true);
		if (list == null) {
			list = new ArrayList<DataContainer>();
			listStorage.put(key, list);
		}
		Collections.addAll(list, string);
		return list.size();
	}

	public synchronized DataContainer lpop(final DataContainer key) {
		final List<DataContainer> list = getListFromStorage(key, true);
		return list == null || list.isEmpty() ? null : list.remove(list.size() - 1);
	}

	public synchronized int rpush(final DataContainer key, final DataContainer... string) {
		List<DataContainer> list = getListFromStorage(key, true);
		if (list == null) {
			list = new ArrayList<DataContainer>();
			listStorage.put(key, list);
		}
		for (DataContainer d: string) list.add(0, d);
		return list.size();
	}

	public synchronized DataContainer rpop(final DataContainer key) {
		final List<DataContainer> list = getListFromStorage(key, true);
		return list == null || list.isEmpty() ? null : list.remove(0);
	}

	public synchronized int llen(final DataContainer key) {
		final List<DataContainer> list = getListFromStorage(key, false);
		return list == null ? 0 : list.size();
	}

	public synchronized List<DataContainer> lrange(final DataContainer key, long start, long end) {
		final List<DataContainer> full = getListFromStorage(key, false);
		
		if (full == null) {
			return Collections.emptyList();
		}

		final List<DataContainer> result = new ArrayList<DataContainer>();

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

	public Set<DataContainer> keys(final DataContainer pattern) {
		final Set<DataContainer> result = new HashSet<DataContainer>();
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
				final DataContainer container = DataContainer.from("");
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

	public synchronized long sadd(final DataContainer key, final DataContainer... members) {
		final Set<DataContainer> set = getSetFromStorage(key, true);

		long added = 0L;
		for (final DataContainer s : members) {
			if (set.add(s)) {
				added++;
			}
		}

		return added;
	}

	public synchronized long srem(final DataContainer key, final DataContainer... member) {
		final Set<DataContainer> set = getSetFromStorage(key, true);
		long removed = 0L;
		for (final DataContainer s : member) {
			if (set.remove(s)) {
				++removed;
			}
		}
		return removed;
	}

	public synchronized int scard(final DataContainer key) {
		final Set<DataContainer> set = getSetFromStorage(key, true);
		return set.size();
	}

	public synchronized Set<DataContainer> sdiff(final DataContainer... keys) {
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

	public synchronized int sdiffstore(final DataContainer dstKey, final DataContainer... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sdiff' command");
		}
		final Set<DataContainer> diff = sdiff(keys);
		final Set<DataContainer> dst = getSetFromStorage(dstKey, true);
		if (!dst.isEmpty()) {
			dst.clear();
		}
		dst.addAll(diff);
		return diff.size();
	}

	public synchronized Set<DataContainer> sinter(final DataContainer... keys) {
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

	public synchronized int sinterstore(final DataContainer dstKey, final DataContainer... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sinterstore' command");
		}
		final Set<DataContainer> inter = sinter(keys);
		final Set<DataContainer> dst = getSetFromStorage(dstKey, true);
		if (!dst.isEmpty()) {
			dst.clear();
		}
		dst.addAll(inter);
		return inter.size();
	}

	public boolean sismember(final DataContainer key, final DataContainer member) {
		final Set<DataContainer> set = getSetFromStorage(key, false);
		return set != null && set.contains(member);
	}

	public synchronized boolean smove(final DataContainer srckey, final DataContainer dstkey, final DataContainer member) {
		final Set<DataContainer> src = getSetFromStorage(srckey, false);
		final Set<DataContainer> dst = getSetFromStorage(dstkey, true);
		if (!src.remove(member)) {
			return false;
		}
		dst.add(member);
		return true;
	}

	public synchronized DataContainer spop(final DataContainer key) {
		final DataContainer member = srandmember(key);
		if (member != null) {
			final Set<DataContainer> src = getSetFromStorage(key, false);
			src.remove(member);
		}
		return member;
	}

	public synchronized DataContainer srandmember(final DataContainer key) {
		final Set<DataContainer> src = getSetFromStorage(key, false);
		return src == null ? null : getRandomElementFromSet(src);
	}

	public synchronized Set<DataContainer> smembers(final DataContainer key) {
		return getSetFromStorage(key, true);
	}

	public synchronized Set<DataContainer> sunion(final DataContainer... keys) {
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

	public synchronized int sunionstore(final DataContainer dstkey, final DataContainer... keys) {
		if (keys.length <= 0) {
			throw new JedisDataException("ERR wrong number of arguments for 'sunionstore' command");
		}
		final Set<DataContainer> inter = sunion(keys);
		final Set<DataContainer> dst = getSetFromStorage(dstkey, true);
		if (!dst.isEmpty()) {
			dst.clear();
		}
		dst.addAll(inter);
		return inter.size();
	}
}

