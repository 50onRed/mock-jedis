# Mock Jedis

Mock Jedis is a library for mocking out [Jedis](https://github.com/xetorthio/jedis) clients.
It's useful for testing your code without actually having a live redis server up.
Currently, mock-jedis supports pipelining and all the basic Jedis commands, but if you find missing 
functionality you're welcome to submit a pull request.

## Compile
with gradle (preferred method):
```shell
gradle build
```

## Adding mock-jedis to your project

Add it as a dependency to your project.

Here's a sample gradle script that will pull mock-jedis 0.3.1 from maven-central
```gradle
buildscript {
    repositories {
        mavenCentral()
    }
}

apply plugin: 'java'

dependencies {
  testCompile 'com.fiftyonred:mock-jedis:0.3.1'
}
```

Sample maven dependency definition:
```xml
<dependency>
    <groupId>com.fiftyonred</groupId>
    <artifactId>mock-jedis</artifactId>
    <version>0.3.1</version>
    <type>jar</type>
    <scope>test</scope>
</dependency>
```

## Using mock-jedis
```java
Jedis j = new MockJedis("test");
j.set("test", "123");
assertEquals("123", j.get("test"));
```

## Supported Commands
Currently the following commands are supported by mock-jedis
 - KEYS: DEL DUMP EXISTS EXPIRE EXPIREAT KEYS PERSIST PEXPIRE PEXPIREAT PTTL RANDOMKEY RENAME RENAMENX RESTORE TTL TYPE
 - STRINGS: APPEND DECR DECRBY GET GETSET INCR INCRBY INCRBYFLOAT MGET MSET MSETNX PSETEX SET SETEX SETNX STRLEN
 - HASHES: HDEL HEXISTS HGET HGETALL HINCRBY HINCRBYFLOAT HKEYS HLEN HMGET HMSET HSET HSETNX HVALS
 - LISTS: LLEN LPOP LPUSH
 - SETS: SADD SCARD SDIFF SDIFFSTORE SINTER SINTERSTORE SISMEMBER SMEMBERS SMOVE SPOP SRANDMEMBER SREM SUNION SUNIONSTORE
 - CONNECTIONS: ECHO PING SELECT QUIT
 - SERVER: DBSIZE FLUSHALL FLUSHDB
 - PIPELINES

## Unsupported Things
 - All commands not listed above
