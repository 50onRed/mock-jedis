package com.fiftyonred.mock_jedis;

public interface DataContainer<T> extends Comparable<T> {
  byte[] getBytes();
  String getString();
  DataContainerImpl.Source getSource();
  DataContainer append(DataContainerImpl container);
}
