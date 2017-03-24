package org.jembi.bsis.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.UUID;

import org.junit.Test;

public class BaseUUIDEntityTests {
  
  private static final UUID UUID1 = UUID.randomUUID();
  private static final UUID UUID2 = UUID.randomUUID();

  class TestEntity extends BaseUUIDEntity {
    private static final long serialVersionUID = 1L;
  }

  class AnotherTestEntity extends BaseUUIDEntity {
    private static final long serialVersionUID = 1L;
  }

  @Test
  public void testDifferentEntitiesSameBaseClass() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(UUID1);
    AnotherTestEntity testEntity2 = new AnotherTestEntity();
    testEntity2.setId(UUID1);
    assertThat("Different objects don't match", testEntity1.equals(testEntity2), is(false));
  }

  @Test
  public void testToStringNullId() {
    TestEntity testEntity1 = new TestEntity();
    String str = testEntity1.toString();
    assertThat("Object short class name in toString", str.contains("BaseUUIDEntityTests.TestEntity"), is(true));
    assertThat("Null id referenced in toString", str.contains("id=<null>"), is(true));
  }

  @Test
  public void testToStringNotNullId() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(UUID1);
    String str = testEntity1.toString();
    assertThat("Id referenced in toString", str.contains("id=" + UUID1.toString()), is(true));
  }

  @Test
  public void testEqualsReflexive() {
    TestEntity testEntity1 = new TestEntity();
    assertThat("Objects are equal", testEntity1.equals(testEntity1), is(true));
  }

  @Test
  public void testEqualsSymmetric() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(UUID1);
    TestEntity testEntity2 = new TestEntity();
    testEntity2.setId(UUID1);
    assertThat("Equals is symetric", testEntity1.equals(testEntity2) && testEntity2.equals(testEntity1), is(true));
  }

  @Test
  public void testEqualsTransitive() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(UUID1);
    TestEntity testEntity2 = new TestEntity();
    testEntity2.setId(UUID1);
    TestEntity testEntity3 = new TestEntity();
    testEntity3.setId(UUID1);
    assertThat("Equals", testEntity1.equals(testEntity2), is(true));
    assertThat("Equals", testEntity1.equals(testEntity3), is(true));
    assertThat("Equals is transitive", testEntity2.equals(testEntity3), is(true));
  }

  @Test
  public void testEqualsNewObjects() {
    TestEntity testEntity1 = new TestEntity();
    TestEntity testEntity2 = new TestEntity();
    assertThat("Objects are not equal", testEntity1.equals(testEntity2), is(false));
  }

  @Test
  public void testEqualsDifferentObjects() {
    TestEntity testEntity1 = new TestEntity();
    String testEntity2 = new String("testEntity1");
    assertThat("Objects are not equal", testEntity1.equals(testEntity2), is(false));
  }

  @Test
  public void testEqualsSameId() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(UUID1);
    TestEntity testEntity2 = new TestEntity();
    testEntity2.setId(UUID1);
    assertThat("Objects are equal", testEntity1.equals(testEntity2), is(true));
  }

  @Test
  public void testEqualsDifferentId() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(UUID1);
    TestEntity testEntity2 = new TestEntity();
    testEntity2.setId(UUID2);
    assertThat("Objects are not equal", testEntity1.equals(testEntity2), is(false));
  }

  @Test
  public void testEqualsWithNull() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(UUID1);
    assertThat("Objects are not equal", testEntity1.equals(null), is(false));
  }

  @Test
  public void testEqualsWithNullId() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(UUID1);
    TestEntity testEntity2 = new TestEntity();
    assertThat("Objects are not equal", testEntity1.equals(testEntity2), is(false));
  }

  @Test
  public void testHashCodeSameReference() {
    TestEntity testEntity1 = new TestEntity();
    TestEntity testEntity2 = testEntity1;
    assertThat("hash function is equals", testEntity1.hashCode(), is(testEntity2.hashCode()));
  }

  @Test
  public void testHashCodeNewObjectsEquals() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(UUID1);
    TestEntity testEntity2 = new TestEntity();
    testEntity2.setId(UUID1);
    assertThat("hash function is equals", testEntity1.hashCode(), is(testEntity2.hashCode()));
  }
}
