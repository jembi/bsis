package org.jembi.bsis.repository;

import static org.junit.Assert.assertEquals;

import javax.validation.ConstraintViolationException;

import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PackTypeRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private PackTypeRepository packTypeRepository;

  @Test
  public void testSavePackType_shouldPersistCorrectly() throws Exception {
    // Set up data
    PackType packType = PackTypeBuilder.aPackType()
        .withMaxWeight(999)
        .withMinWeight(222)
        .withLowVolumeWeight(555)
        .withComponentType(ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager)).build();

    // Run test
    packTypeRepository.savePackType(packType);

    // Verify
    assertEquals("maxWeight is correct", new Integer(999), packType.getMaxWeight());
    assertEquals("minWeight is correct", new Integer(222), packType.getMinWeight());
    assertEquals("lowVolumeWeight is correct", new Integer(555), packType.getLowVolumeWeight());
  }

  @Test(expected = ConstraintViolationException.class)
  public void testSavePackTypeThatCountsAsDonationWithNoComponentType_shouldThrow() throws Exception {
    // Set up data
    PackType packType = PackTypeBuilder.aPackType()
        .withMaxWeight(999)
        .withMinWeight(222)
        .withLowVolumeWeight(555)
        .withComponentType(null).build();

    // Run test
    packTypeRepository.savePackType(packType);
  }

}
