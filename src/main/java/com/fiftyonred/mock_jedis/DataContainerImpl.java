package com.fiftyonred.mock_jedis;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Protocol;

/**
 * Container for redis data (key/value bytes).
 *
 * @author konstantin.shchepanovskyi@playtech.com
 */
@SuppressWarnings("WeakerAccess")
public class DataContainerImpl implements DataContainer<DataContainerImpl> {

  // Redis protocol charset (Jedis uses UTF-8 as a constant)
  public static final Charset CHARSET = Charset.forName(Protocol.CHARSET);

  private final byte[] bytes;
  private final String string;
  private final Source source;
  private final int hash;

  protected DataContainerImpl(byte[] bytes, String string, Source source) {
    this.bytes = bytes;
    this.string = string;
    this.source = source;
    this.hash = calculateHash(bytes, string, source);
  }

  public static DataContainerImpl from(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    byte[] copy = Arrays.copyOf(bytes, bytes.length);
    String str = new String(copy, Charset.defaultCharset());
    return new DataContainerImpl(copy, str, Source.BYTES);
  }

  public static DataContainerImpl from(final String str) {
    if (str == null) {
      return null;
    }
    byte[] bytes = str.getBytes(CHARSET);
    return new DataContainerImpl(bytes, str, Source.STRING);
  }

  public static DataContainerImpl[] from(final String[] strings) {
    if (strings == null) {
      return null;
    }
    final DataContainerImpl[] result = new DataContainerImpl[strings.length];
    for (int i = 0; i < strings.length; i++) {
      result[i] = from(strings[i]);
    }
    return result;
  }

  public static DataContainerImpl[] from(byte[][] byteArrays) {
    if (byteArrays == null) {
      return null;
    }
    final DataContainerImpl[] result = new DataContainerImpl[byteArrays.length];
    for (int i = 0; i < byteArrays.length; i++) {
      result[i] = from(byteArrays[i]);
    }
    return result;
  }

  public static Map<DataContainer, DataContainer> fromByteMap(final Map<byte[], byte[]> byteMap) {
    if (byteMap == null) {
      return null;
    }
    final Map<DataContainer, DataContainer> result = new HashMap<DataContainer, DataContainer>(byteMap.size());
    for (final Map.Entry<byte[], byte[]> entry : byteMap.entrySet()) {
      result.put(from(entry.getKey()), from(entry.getValue()));
    }
    return result;
  }

  public static Map<DataContainer, DataContainer> fromStringMap(final Map<String, String> byteMap) {
    if (byteMap == null) {
      return null;
    }
    final Map<DataContainer, DataContainer> result = new HashMap<DataContainer, DataContainer>(byteMap.size());
    for (final Map.Entry<String, String> entry : byteMap.entrySet()) {
      result.put(from(entry.getKey()), from(entry.getValue()));
    }
    return result;
  }

  public static Map<DataContainer, DataContainer> fromStringToDoubleMap(
      final Map<String, Double> stringToDoubleMap) {
    if (stringToDoubleMap == null) {
      return null;
    }
    final Map<DataContainer, DataContainer> result = new HashMap<DataContainer, DataContainer>(stringToDoubleMap.size());
    for (final Map.Entry<String, Double> entry : stringToDoubleMap.entrySet()) {
      result.put(from(entry.getKey()), from(Double.toString(entry.getValue())));
    }
    return result;
  }

  public static byte[] toBytes(DataContainer container) {
    if (container == null) {
      return null;
    }
    return container.getBytes();
  }

  public static List<byte[]> toBytes(Collection<? extends DataContainer> containers) {
    if (containers == null) {
      return null;
    }
    List<byte[]> result = new ArrayList<byte[]>(containers.size());
    for (DataContainer container : containers) {
      result.add(toBytes(container));
      if (container instanceof DataContainerWithScore) {
        // TODO: This is not great, ideally should be encapsulated in DataContainerWithScore
        double score = ((DataContainerWithScore) container).getScore();
        result.add(Double.toString(score).getBytes(CHARSET));
      }
    }
    return result;
  }

  /**
   * Return data from this container. If container is created from String, then it returns sequence of bytes of that
   * string encoded in UTF-8.
   */
  @Override public byte[] getBytes() {
    return bytes;
  }

  @Override public String getString() {
    return string;
  }

  @Override public Source getSource() {
    return source;
  }

  @Override public DataContainer append(DataContainerImpl container) {
    switch (source) {
      case BYTES:
        byte[] a = bytes;
        byte[] b = container.bytes;
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return DataContainerImpl.from(c);
      case STRING:
        return DataContainerImpl.from(string + container.getString());
      default:
        throw new IllegalStateException("unimplemented");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DataContainerImpl)) return false;

    DataContainerImpl that = (DataContainerImpl) o;

    if (source != that.source) {
      return Arrays.equals(this.bytes, that.bytes);
    }
    switch (source) {

      case BYTES:
        return Arrays.equals(this.bytes, that.bytes);
      case STRING:
        return this.string.equals(that.string);
      default:
        throw new IllegalStateException("unimplemented");
    }
  }

  @Override
  public int hashCode() {
    return hash;
  }

  private int calculateHash(byte[] bytes, String string, Source source) {
    switch (source) {
      case BYTES:
        return Arrays.hashCode(bytes);
      case STRING:
        return string.hashCode();
      default:
        return 0;
    }
  }

  @Override
  public String toString() {
    return string;
  }

  @Override
  public int compareTo(DataContainerImpl o) {
    // compare string representation of data (in the same way as redis does)
    return string.compareTo(o.getString());
  }

  public enum Source {
    BYTES,
    STRING
  }
}
