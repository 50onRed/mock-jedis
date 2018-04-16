package com.fiftyonred.mock_jedis;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataContainerWithScoreTest {
  @Test public void equals() {
    DataContainerImpl inner1 = DataContainerImpl.from("test");
    DataContainerImpl inner2 = DataContainerImpl.from("test");
    assertEquals(new DataContainerWithScore(inner1, 0), new DataContainerWithScore(inner2, 1));
  }
}
