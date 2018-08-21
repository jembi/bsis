package org.jembi.bsis.repository;

import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.junit.Assert.assertEquals;

public class PackTypeRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private PackTypeRepository packTypeRepository;

  @Test
  public void testGetAllPackTypesProducingTestSamples() {
    PackType producingTestSamples = aPackType().withTestSampleProduced(true).buildAndPersist(entityManager);
    PackType anotherProducingTestSamples = aPackType().withTestSampleProduced(true).buildAndPersist(entityManager);
    PackType doesNotProduceTestSamples = aPackType().withTestSampleProduced(false).buildAndPersist(entityManager);

    List<PackType> actual = packTypeRepository.getAllPackTypesProducingTestSamples();

    assertThat(actual, hasItems(producingTestSamples, anotherProducingTestSamples));
    assertThat(actual, not(hasItem(doesNotProduceTestSamples)));
  }

  @Test
  public void testSavePackType_shouldPersistCorrectly() {
    // Set up data
    PackType packType = aPackType()
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
  public void testSavePackTypeThatCountsAsDonationWithNoComponentType_shouldThrow() {
    // Set up data
    PackType packType = aPackType()
        .withMaxWeight(999)
        .withMinWeight(222)
        .withLowVolumeWeight(555)
        .withComponentType(null).build();

    // Run test
    packTypeRepository.savePackType(packType);
  }

}
