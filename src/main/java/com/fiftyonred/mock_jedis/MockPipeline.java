package com.fiftyonred.mock_jedis;

import com.fiftyonred.utils.WildcardMatcher;
import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.util.SafeEncoder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MockPipeline extends Pipeline {
	private final WildcardMatcher wildcardMatcher = new WildcardMatcher();

    private final Map<String, Long> ttls = new ConcurrentHashMap<String, Long>();

    private final Map<String, String> storage;
    private final Map<String, Map<String, String>> hashStorage;
    private final Map<String, List<String>> listStorage;
    
	public MockPipeline() {
		storage = new HashMap<String, String>();
		hashStorage = new HashMap<String, Map<String, String>>();
		listStorage = new HashMap<String, List<String>>();
	}

	public void clear() {
		storage.clear();
	}

	@Override
	public synchronized Response<String> set(String key, String value) {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		storage.put(key, value);
		response.set("OK".getBytes());
		return response;
	}

	@Override
	public synchronized Response<String> get(String key) {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		String val = getFromStorage(storage, key);
		response.set(val != null ? val.getBytes() : null);
		return response;
	}

    @Override
    public synchronized Response<String> setex(String key, int seconds, String value) {
        return psetex(key, seconds * 1000, value);
    }

    @Override
    public synchronized Response<String> psetex(String key, int milliseconds, String value) {
        Response<String> response = set(key, value);
        pexpire(key, milliseconds);
        return response;
    }

    @Override
    public synchronized Response<Long> expire(String key, int seconds) {
        return expireAt(key, System.currentTimeMillis() / 1000 + seconds);
    }

    @Override
    public synchronized Response<Long> expireAt(String key, long seconds) {
        return pexpireAt(key, seconds * 1000);
    }

    @Override
    public synchronized Response<Long> pexpire(String key, int milliseconds) {
        return pexpireAt(key, System.currentTimeMillis() + milliseconds);
    }

    @Override
    public synchronized Response<Long> pexpireAt(String key, long millisecondsTimestamp) {
        Response<Long> response = new Response<Long>(BuilderFactory.LONG);
        if(!(storageContainsKey(storage, key) || storageContainsKey(hashStorage, key) || storageContainsKey(listStorage, key))) {
            response.set(0L);
        } else {
            ttls.put(key, millisecondsTimestamp);
            response.set(1L);
        }

        return response;
    }

    @Override
    public synchronized Response<Long> ttl(String key) {
        Long pttlInResponse = pttl(key).get();
        
        Response<Long> response = new Response<Long>(BuilderFactory.LONG);
        if(pttlInResponse != -1) {
            if(pttlInResponse > 0 && pttlInResponse < 1000L) {
                pttlInResponse = 1000L;
            }
            response.set(pttlInResponse / 1000L);
        } else {
            response.set(pttlInResponse);
        }
        return response;
    }

    @Override
    public synchronized Response<Long> pttl(String key) {
        checkExpiration(key);
        Response<Long> response = new Response<Long>(BuilderFactory.LONG);
        Long ttl = ttls.get(key);
        if(ttl != null) {
            response.set(ttl - System.currentTimeMillis());
        } else {
            response.set(-1L);
        }
        return response;
    }

	@Override
	public synchronized Response<List<String>> mget(String... keys) {
		Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);

		List<byte[]> result = new ArrayList<byte[]>();
		for (String key : keys) {
			if (storageContainsKey(storage, key)) {
				result.add(getFromStorage(storage, key).getBytes());
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
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		String val = getFromStorage(storage, key);
		Long result = val == null ? 0L - integer : Long.valueOf(val) - integer;
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
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		String val = getFromStorage(storage, key);
		Long result = val == null ? integer : Long.valueOf(val) + integer;
		storage.put(key, result.toString());
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<Long> del(String... keys) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		Long result = 0L;
		for (String key : keys) {
            clearExpiration(key);
			String i = storage.remove(key);
			if (i != null) {
				++result;
			}
		}
		response.set(result);
		return response;
	}

	private Map<String, String> getOrCreateHash(final String key) {
		Map<String, String> result = getFromStorage(hashStorage, key);
		if (result == null) {
			result = new HashMap<String, String>();
			hashStorage.put(key, result);
		}
		return result;
	}
	
	@Override
	public synchronized Response<String> hget(final String key, final String field) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		if (storageContainsKey(hashStorage, key)) {
			final Map<String, String> result = getFromStorage(hashStorage, key);
			response.set(result.containsKey(field) ? result.get(field).getBytes() : null);
		}
		return response;
	}

	@Override
	public synchronized Response<Map<String, String>> hgetAll(final String key) {
		final Response<Map<String, String>> response = new Response<Map<String, String>>(BuilderFactory.STRING_MAP);
		final Map<String, String> result = getFromStorage(hashStorage, key);

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
		final Map<String, String> result = getFromStorage(hashStorage, key);

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
		final Map<String, String> result = getFromStorage(hashStorage, key);

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
		final Map<String, String> m = getOrCreateHash(key);
		response.set(m.containsKey(field) ? 0L : 1L);
		m.put(field, value);

		return response;
	}

	@Override
	public synchronized Response<Long> hsetnx(final String key, final String field, final String value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<String, String> m = getOrCreateHash(key);
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
		if (!storageContainsKey(hashStorage, key)) {
			for (String field : fields) {
				result.add(null);
			}
			response.set(result);
			return response;
		}

		for (String field : fields) {
			final String v = getFromStorage(hashStorage, key).get(field);
			result.add(v != null ? v.getBytes() : null);
		}
		response.set(result);
		return response;
	}

	@Override
	public synchronized Response<String> hmset(final String key, Map<String, String> hash) {
		final Response<String> response = new Response<String>(BuilderFactory.STRING);
		final Map<String, String> m = getOrCreateHash(key);
		for (Map.Entry<String, String> e : hash.entrySet()) {
			m.put(e.getKey(), e.getValue());
		}
		response.set("OK".getBytes());
		return response;
	}

	@Override
	public synchronized Response<Long> hincrBy(final String key, final String field, final long value) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		final Map<String, String> m = getOrCreateHash(key);

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
		final Map<String, String> m = getOrCreateHash(key);

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
		final Map<String, String> m = getOrCreateHash(key);
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
		if (storageContainsKey(hashStorage, key)) {
			response.set(getFromStorage(hashStorage, key).containsKey(field) ? 1L : 0L);
		}

		return response;
	}

	@Override
	public synchronized Response<Long> hlen(final String key) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		if (storageContainsKey(hashStorage, key)) {
			response.set((long) getFromStorage(hashStorage, key).size());
		} else {
			response.set(0L);
		}

		return response;
	}

	@Override
	public synchronized Response<Long> lpush(String key, String... string) {
		final Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		List<String> list = getFromStorage(listStorage, key);
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
		final List<String> list = getFromStorage(listStorage, key);
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
		final List<String> list = getFromStorage(listStorage, key);
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
		filterKeys(pattern, storageKeySet(storage), result);
		filterKeys(pattern, storageKeySet(hashStorage), result);

		response.set(result);
		return response;
	}

	public void filterKeys(final String pattern, final Collection<String> collection, final List<byte[]> result) {
		for (String key : collection) {
			if (wildcardMatcher.match(key, pattern))
				result.add(key.getBytes());
		}
	}

    protected <T> T getFromStorage(Map<String, T> storage, String key) {
        checkExpiration(key);
        return storage.get(key);
    }

    protected <T> boolean storageContainsKey(Map<String, T> storage, String key) {
        checkExpiration(key);
        return storage.containsKey(key);
    }

    protected Set<String> storageKeySet(Map<String, ?> storage) {
        for (String key : storage.keySet()) {
            checkExpiration(key);
        }
        return storage.keySet();
    }
    
    protected synchronized void checkExpiration(String key) {
        Long ttl = ttls.get(key);
        if(ttl == null) {
            return;
        }
        
        if(ttl < System.currentTimeMillis()) {
            storage.remove(key);
            hashStorage.remove(key);
            listStorage.remove(key);
            ttls.remove(key);
        }
    }
    
    protected synchronized void clearExpiration(String key) {
        ttls.remove(key);
    }
}
