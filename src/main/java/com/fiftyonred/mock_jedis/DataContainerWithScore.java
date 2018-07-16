package com.fiftyonred.mock_jedis;

public class DataContainerWithScore implements DataContainer<DataContainerWithScore> {
  private final DataContainer delegate;
  private double score;

  DataContainerWithScore(DataContainer delegate, double score) {
    this.delegate = delegate;
    this.score = score;
  }

  @Override public byte[] getBytes() {
    return delegate.getBytes();
  }

  @Override public String getString() {
    return delegate.getString();
  }

  @Override public DataContainerImpl.Source getSource() {
    return delegate.getSource();
  }

  @Override public DataContainer append(DataContainerImpl container) {
    return delegate.append(container);
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }

  @Override public boolean equals(Object obj) {
    if (obj instanceof DataContainerWithScore) {
      return delegate.equals(((DataContainerWithScore) obj).delegate);
    } else {
      return delegate.equals(obj);
    }
  }

  @Override public int hashCode() {
    return delegate.hashCode();
  }

  @Override public int compareTo(DataContainerWithScore o) {
    // Sort by score first
    int result = Double.compare(score, o.score);
    if (result != 0) {
      return result;
    } else {
      // If scores are the same, compare string representation of data
      return getString().compareTo(o.getString());
    }
  }
}
