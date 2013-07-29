package com.fiftyonred.mock_jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.util.SafeEncoder;

public class MockPipeline extends Pipeline {

	private Map<String, String> storage = null;
	private Map<String, Map<String, String>> hashStorage = null;
	
	public MockPipeline() {
		storage = new HashMap<String, String>();
		hashStorage = new HashMap<String, Map<String, String>>();
	}
	
	public void clear() {
		storage.clear();
	}
	
	@Override
	public Response<String> set(String key, String value) {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		storage.put(key, value);
		response.set("OK".getBytes());
		return response;
	}
	
	@Override
	public Response<String> setex(String key, int seconds, String value) {
		return set(key, value);
	}
	
	@Override
	public Response<String> get(String key) {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		String val = storage.get(key);
		response.set(val != null ? val.getBytes() : null);
		return response;
	}
	
	@Override
	public Response<Long> expire(String key, int seconds) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		response.set(seconds);
		return response;
	}
	
	@Override
	public Response<List<String>> mget(String... keys) {
		Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		
		List<byte[]> result = new ArrayList<byte[]>();
		for (String key: keys) {
			if (storage.containsKey(key)) {
				result.add(storage.get(key).getBytes());
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
	public Response<Long> decrBy(String key, long integer) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		String val = storage.get(key);
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
	public Response<Long> incrBy(String key, long integer) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		String val = storage.get(key);
		Long result = val == null ? integer : Long.valueOf(val) + integer;
		storage.put(key, result.toString());
		response.set(result);
		return response;
	}
	
	@Override
	public Response<Long> del(String... keys) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		Long result = 0L;
		for (String key: keys) {
			String i = storage.remove(key);
			if (i != null) {
				++result;
			}
		}
		response.set(result);
		return response;
	}
	
	@Override
	public Response<String> hget(String key, String field) {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		if (hashStorage.containsKey(key)) {
			response.set(hashStorage.get(key).get(field));
		}
		return response;
	}

	@Override
	public Response<Map<String, String>> hgetAll(String key) {
		Response<Map<String, String>> response = new Response<Map<String, String>>(BuilderFactory.STRING_MAP);
		Map<String, String> result = hashStorage.get(key);

		if (hashStorage.containsKey(key)) {
			List<byte[]> encodedResult = new ArrayList<byte[]>();
			for (String k : result.keySet()) {
				encodedResult.add(SafeEncoder.encode(k));
				encodedResult.add(SafeEncoder.encode(result.get(k)));
			}
			response.set(encodedResult);
		} else {
			response.set(new ArrayList<byte[]>());
		}
		return response;
	}

	@Override
	public Response<Long> hset(String key, String field, String value) {
		Response<Long> response = new Response<Long>(BuilderFactory.LONG);
		Map<String, String> m;
		if (!hashStorage.containsKey(key)) {
			m = new HashMap<String, String>();
		} else {
			m = hashStorage.get(key);
		}
		response.set(m.containsKey(field) ? 0L : 1L);
		m.put(field, value);
		if (!hashStorage.containsKey(key)) {
			hashStorage.put(key, m);
		}
		response.set("OK");
		return response;
	}
	
	@Override
	public Response<List<String>> hmget(String key, String... fields) {
		Response<List<String>> response = new Response<List<String>>(BuilderFactory.STRING_LIST);
		List<byte[]> result = new ArrayList<byte[]>();
		if (!hashStorage.containsKey(key)) {
			for (String field: fields) {
				result.add(null);
			}
			response.set(result);
			return response;
		}
		for (String field: fields) {
			String v = hashStorage.get(key).get(field);
			result.add(v != null ? v.getBytes() : null);
		}
		response.set(result);
		return response;
	}
	
	@Override
	public Response<String> hmset(String key, Map<String, String> hash) {
		Response<String> response = new Response<String>(BuilderFactory.STRING);
		Map<String, String> m;
		if (!hashStorage.containsKey(key)) {
			m = new HashMap<String, String>();
		} else {
			m = hashStorage.get(key);
		}
		for (Map.Entry<String, String> e: hash.entrySet()) {
			m.put(e.getKey(), e.getValue());
		}
		if (!hashStorage.containsKey(key)) {
			hashStorage.put(key, m);
		}
		response.set("OK".getBytes());
		return response;
	}
	
	@Override
	public void sync() {
		// do nothing
	}	
}
