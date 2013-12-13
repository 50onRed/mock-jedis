package com.fiftyonred.mock_jedis;

public class KeyInformation {
	private KeyType type;
	/**
	 * value in milliseconds when this key expires
	 * -1 if this key never expires
	 */
	private long expiration;

	public KeyInformation(final KeyType type) {
		this.type = type;
		this.expiration = -1L;
	}

	public KeyType getType() {
		return type;
	}

	public void setType(final KeyType type) {
		this.type = type;
	}

	public void setExpiration(final long expiration) {
		this.expiration = expiration;
	}

	public boolean isTTLSetAndKeyExpired() {
		return expiration != -1L && isExpired();
	}

	private boolean isExpired() {
		return expiration < System.currentTimeMillis();
	}

	public long getTTL() {
		if (expiration == -1L) {
			return -1L;
		}
		if (isExpired()) {
			return -1L;
		}
		return expiration - System.currentTimeMillis();
	}
}
