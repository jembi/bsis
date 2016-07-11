package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class LocationRoundTripTests extends ContextDependentTestSuite {

  @Test
  public void testPersistValidLocation() {
    aLocation().buildAndPersist(entityManager);
  }
}
