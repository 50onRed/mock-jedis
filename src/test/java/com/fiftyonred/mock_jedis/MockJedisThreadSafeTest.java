package com.fiftyonred.mock_jedis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertEquals;

public class MockJedisThreadSafeTest {
	private Jedis j = null;

	@Before
	public void setUp() {
		j = new MockJedis("test");
	}

	@Test
	public void testThreadSafety() throws InterruptedException {
		final Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 1000; ++i) {
					j.incr("test");
				}
			}
		});
		final Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 1000; ++i) {
					j.decr("test");
				}
			}
		});
		t1.start();
		t2.start();
		t1.join();
		t2.join();

		assertEquals("0", j.get("test"));
	}
}
