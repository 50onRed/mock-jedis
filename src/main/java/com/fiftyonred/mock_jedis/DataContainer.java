package com.fiftyonred.mock_jedis;

import redis.clients.jedis.Protocol;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Container for redis data (key/value bytes).
 *
 * @author konstantin.shchepanovskyi@playtech.com
 */
public class DataContainer implements Comparable<DataContainer> {

	// Redis protocol charset (Jedis uses UTF-8 as a constant)
	public static final Charset CHARSET = Charset.forName(Protocol.CHARSET);

	private final byte[] bytes;
	private final String string;
	private final Source source;
	private final int hash;

	private DataContainer(byte[] bytes, String string, Source source) {
		this.bytes = bytes;
		this.string = string;
		this.source = source;
		this.hash = calculateHash(bytes, string, source);
	}

	public static DataContainer from(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		byte[] copy = Arrays.copyOf(bytes, bytes.length);
		String str = new String(copy, Charset.defaultCharset());
		return new DataContainer(copy, str, Source.BYTES);
	}

	public static DataContainer from(final String str) {
		if (str == null) {
			return null;
		}
		byte[] bytes = str.getBytes(CHARSET);
		return new DataContainer(bytes, str, Source.STRING);
	}

	public static DataContainer[] from(final String[] strings) {
		if (strings == null) {
			return null;
		}
		final DataContainer[] result = new DataContainer[strings.length];
		for (int i = 0; i < strings.length; i++) {
			result[i] = from(strings[i]);
		}
		return result;
	}

	public static DataContainer[] from(byte[][] byteArrays) {
		if (byteArrays == null) {
			return null;
		}
		final DataContainer[] result = new DataContainer[byteArrays.length];
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

	public static byte[] toBytes(DataContainer container) {
		if (container == null) {
			return null;
		}
		return container.getBytes();
	}

	public static List<byte[]> toBytes(Collection<DataContainer> containers) {
		if (containers == null) {
			return null;
		}
		List<byte[]> result = new ArrayList<byte[]>(containers.size());
		for (DataContainer container : containers) {
			result.add(toBytes(container));
		}
		return result;
	}

	/**
	 * Return data from this container. If container is created from String, then it returns sequence of bytes of that
	 * string encoded in UTF-8.
	 */
	public byte[] getBytes() {
		return bytes;
	}

	public String getString() {
		return string;
	}

	public Source getSource() {
		return source;
	}

	public DataContainer append(DataContainer container) {
		switch (source) {
			case BYTES:
				byte[] a = bytes;
				byte[] b = container.bytes;
				byte[] c = new byte[a.length + b.length];
				System.arraycopy(a, 0, c, 0, a.length);
				System.arraycopy(b, 0, c, a.length, b.length);
				return DataContainer.from(c);
			case STRING:
				return DataContainer.from(string + container.getString());
			default:
				throw new IllegalStateException("unimplemented");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DataContainer)) return false;

		DataContainer that = (DataContainer) o;

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
	public int compareTo(DataContainer o) {
		// compare string representation of data (in the same way as redis does)
		return string.compareTo(o.getString());
	}

	public enum Source {
		BYTES,
		STRING
	}
}
