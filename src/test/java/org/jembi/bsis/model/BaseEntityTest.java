package org.jembi.bsis.model;

import org.jembi.bsis.model.BaseEntity;
import org.junit.Assert;
import org.junit.Test;


public class BaseEntityTest {

  class TestEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
  }

  class AnotherTestEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
  }

  @Test
  public void testDifferentEntitiesSameBaseClass() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(123l);
    AnotherTestEntity testEntity2 = new AnotherTestEntity();
    testEntity2.setId(123l);
    Assert.assertFalse("Different objects don't match", testEntity1.equals(testEntity2));
  }

  @Test
  public void testToStringNullId() {
    TestEntity testEntity1 = new TestEntity();
    String str = testEntity1.toString();
    Assert.assertTrue("Object short class name in toString",
        str.contains("BaseEntityTest.TestEntity"));
    Assert.assertTrue("Null id referenced in toString", str.contains("id=<null>"));
  }

  @Test
  public void testToStringNotNullId() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(123l);
    String str = testEntity1.toString();
    Assert.assertTrue("Id referenced in toString", str.contains("id=123"));
  }

  @Test
  public void testEqualsReflexive() {
    TestEntity testEntity1 = new TestEntity();

    boolean equals = testEntity1.equals(testEntity1);

    Assert.assertTrue("Objects are equal", equals);
  }

  @Test
  public void testEqualsSymmetric() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(1l);
    TestEntity testEntity2 = new TestEntity();
    testEntity2.setId(1l);
    Assert.assertTrue("Equals is symetric",
        testEntity1.equals(testEntity2) && testEntity2.equals(testEntity1));
  }

  @Test
  public void testEqualsConsistant() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(1l);
    int hashCode = testEntity1.hashCode();
    for (int i = 0; i < 25; i++) {
      Assert.assertEquals("Equals is consistant", hashCode, testEntity1.hashCode());
    }
  }

  @Test
  public void testEqualsTransitive() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(1l);
    TestEntity testEntity2 = new TestEntity();
    testEntity2.setId(1l);
    TestEntity testEntity3 = new TestEntity();
    testEntity3.setId(1l);
    Assert.assertTrue("Equals", testEntity1.equals(testEntity2));
    Assert.assertTrue("Equals", testEntity1.equals(testEntity3));
    Assert.assertTrue("Equals is transitive", testEntity2.equals(testEntity3));
  }

  @Test
  public void testEqualsNewObjects() {
    TestEntity testEntity1 = new TestEntity();
    TestEntity testEntity2 = new TestEntity();

    boolean equals = testEntity1.equals(testEntity2);

    Assert.assertFalse("Objects are not equal", equals);
  }

  @Test
  public void testEqualsDifferentObjects() {
    TestEntity testEntity1 = new TestEntity();
    String testEntity2 = new String("testEntity1");

    boolean equals = testEntity1.equals(testEntity2);

    Assert.assertFalse("Objects are not equal", equals);
  }

  @Test
  public void testEqualsSameId() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(1l);
    TestEntity testEntity2 = new TestEntity();
    testEntity2.setId(1l);

    boolean equals = testEntity1.equals(testEntity2);

    Assert.assertTrue("Objects are equal", equals);
  }

  @Test
  public void testEqualsDifferentId() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(1l);
    TestEntity testEntity2 = new TestEntity();
    testEntity2.setId(2l);

    boolean equals = testEntity1.equals(testEntity2);

    Assert.assertFalse("Objects are not equal", equals);
  }

  @Test
  public void testEqualsWithNull() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(1l);

    boolean equals = testEntity1.equals(null);

    Assert.assertFalse("Objects are not equal", equals);
  }

  @Test
  public void testEqualsWithNullId() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(1l);
    TestEntity testEntity2 = new TestEntity();

    boolean equals = testEntity1.equals(testEntity2);

    Assert.assertFalse("Objects are not equal", equals);
  }

  @Test
  public void testHashCodeSameReference() {
    TestEntity testEntity1 = new TestEntity();
    TestEntity testEntity2 = testEntity1;

    int hashCode1 = testEntity1.hashCode();
    int hashCode2 = testEntity2.hashCode();

    Assert.assertEquals("hash function is equals", hashCode1, hashCode2);
  }

  @Test
  public void testHashCodeNewObjectsEquals() {
    TestEntity testEntity1 = new TestEntity();
    testEntity1.setId(1l);
    TestEntity testEntity2 = new TestEntity();
    testEntity2.setId(1l);

    int hashCode1 = testEntity1.hashCode();
    int hashCode2 = testEntity2.hashCode();

    Assert.assertEquals("hash function is equals", hashCode1, hashCode2);
  }
}
