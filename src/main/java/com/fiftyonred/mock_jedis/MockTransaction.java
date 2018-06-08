package com.fiftyonred.mock_jedis;

import java.util.List;

import redis.clients.jedis.Transaction;

public class MockTransaction extends Transaction {
  private final MockPipeline mockPipeline;

  MockTransaction(MockPipeline mockPipeline) {
    this.mockPipeline = mockPipeline;
  }

  @Override public List<Object> exec() {
    return mockPipeline.exec().get();
  }

  @Override public String discard() {
    return mockPipeline.discard().get();
  }
}
