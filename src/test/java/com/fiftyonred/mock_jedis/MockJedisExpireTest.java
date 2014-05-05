package com.fiftyonred.mock_jedis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

public class MockJedisExpireTest {
    private Jedis j = null;

    @Before
    public void setUp() {
        j = new MockJedis("test");
    }

    @Test
    public void testPsetexAndPttl() throws InterruptedException {
        int delay = 200;
        
        j.psetex("test", delay, "value");
        assertEquals("value", j.get("test"));

        assertTrue(j.pttl("test") > 0);
        
        Thread.sleep(delay + 1);
        
        assertTrue(j.pttl("test") == -1);
    }
    
    @Test
    public void testSetexAndTtl() throws InterruptedException {
        int delay = 1;
        
        j.setex("test", delay, "value");
        assertEquals("value", j.get("test"));

        assertTrue(j.ttl("test") > 0);
        
        Thread.sleep(delay * 1000 + 1);

        assertEquals(-1L, j.ttl("test").longValue());
    }

    @Test
    public void testExpire() throws InterruptedException {
        int delay = 1;

        j.set("test", "123");
        j.expire("test", delay);

        assertTrue(j.ttl("test") > 0);

        Thread.sleep(delay * 1000 + 1);

        assertNull(j.get("test"));
        assertEquals(-1L, j.ttl("test").longValue());
    }

    @Test
    public void testPExpire() throws InterruptedException {
        int delay = 200;

        j.set("test", "123");
        j.pexpire("test", delay);

        assertTrue(j.ttl("test") > 0);

        Thread.sleep(delay + 1);

        assertNull(j.get("test"));
        assertEquals(-1L, j.ttl("test").longValue());
    }

    @Test
    public void testExpireAt() throws InterruptedException {
        int delay = 1;

        j.set("test", "123");
        long startTimeInSec = System.currentTimeMillis() / 1000;
        j.expireAt("test", startTimeInSec + delay);

        assertTrue(j.ttl("test") > 0);

        Thread.sleep(delay * 1000 + 1);

        assertNull(j.get("test"));
        assertTrue(j.ttl("test") == -1);
    }

    @Test
    public void testPexpireAt() throws InterruptedException {
        int delay = 200;

        j.set("test", "123");
        long startTimeInMillisec = System.currentTimeMillis();
        j.pexpireAt("test", startTimeInMillisec + delay);

        assertTrue(j.ttl("test") > 0);

        Thread.sleep(delay + 1);

        assertNull(j.get("test"));
        assertTrue(j.ttl("test") == -1);
    }
    
    @Test
    public void testRenew() throws InterruptedException {
        int delay = 200;

        j.psetex("test", delay, "value");

        assertEquals("value", j.get("test"));
        assertTrue(j.pttl("test") > 0);

        Thread.sleep(delay / 2 + 1);

        j.psetex("test", delay, "value");

        assertEquals("value", j.get("test"));
        assertTrue(j.pttl("test") > 0);

        Thread.sleep(delay + 1);

        assertNull(j.get("test"));
        assertTrue(j.pttl("test") == -1);
        
    }
}
