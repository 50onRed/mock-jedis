package com.fiftyonred.mock_jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class MockJedisClusterTest {
	@Test
	public void testCluster() {
		MockJedisCluster cluster = new MockJedisCluster(new HashSet<HostAndPort>(), 0);
		cluster.set("test", "test");
		assertEquals("test", cluster.get("test"));
	}
}
