package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBuilder.aTransfusionReactionType;
import static org.jembi.bsis.helpers.matchers.TransfusionReactionTypeMatcher.hasSameStateAsTransfusionReactionType;

import java.util.List;

import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TransfusionReactionTypeRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository;

  @Test
  public void testGetAllTransfusionReactionTypesIncludedDeleted_verifyAllTransfusionReactionTypesReturned() {
    TransfusionReactionType reactionType1 = aTransfusionReactionType().withName("reaction 1").buildAndPersist(entityManager); // match
    TransfusionReactionType reactionType2 = aTransfusionReactionType().withName("reaction 2").buildAndPersist(entityManager); // match
    TransfusionReactionType reactionType3 = aTransfusionReactionType().withName("reaction 3").buildAndPersist(entityManager); // match  
    TransfusionReactionType reactionType4 = aTransfusionReactionType().withName("reaction 4").thatIsDeleted().buildAndPersist(entityManager); // match
    TransfusionReactionType reactionType5 = aTransfusionReactionType().withName("reaction 5").buildAndPersist(entityManager); // match

    List<TransfusionReactionType> transfusionReactionTypes =
        transfusionReactionTypeRepository.getAllTransfusionReactionTypes(true);

    // Verify correct amount of reaction are returned
    Assert.assertEquals("Verify all transfusionReactionTypes including deleted returned", 5,
        transfusionReactionTypes.size());

    // Verify all reaction were returned
    Assert.assertTrue("Verify reaction 1 present", transfusionReactionTypes.contains(reactionType1));
    Assert.assertTrue("Verify reaction 2 present", transfusionReactionTypes.contains(reactionType2));
    Assert.assertTrue("Verify reaction 3 present", transfusionReactionTypes.contains(reactionType3));
    Assert.assertTrue("Verify reaction 4 present", transfusionReactionTypes.contains(reactionType4));
    Assert.assertTrue("Verify reaction 5 present", transfusionReactionTypes.contains(reactionType5));
  }

  @Test
  public void testGetAllTransfusionReactionTypesNotIncludedDeleted_verifyAllTransfusionReactionTypesExceptDeletedReturned() {
    TransfusionReactionType reactionType1 = aTransfusionReactionType().withName("reaction 1").buildAndPersist(entityManager); // match
    TransfusionReactionType reactionType2 = aTransfusionReactionType().withName("reaction 2").buildAndPersist(entityManager); // match
    TransfusionReactionType reactionType3 = aTransfusionReactionType().withName("reaction 3").buildAndPersist(entityManager); // match  
    // reaction 4 is deleted and should not be returned
    TransfusionReactionType reactionType4 = aTransfusionReactionType().withName("reaction 4").thatIsDeleted().buildAndPersist(entityManager); // match
    TransfusionReactionType reactionType5 = aTransfusionReactionType().withName("reaction 5").buildAndPersist(entityManager); // match

    List<TransfusionReactionType> transfusionReactionTypes =
        transfusionReactionTypeRepository.getAllTransfusionReactionTypes(false);

    // Verify correct amount of reaction are returned
    Assert.assertEquals("Verify all transfusionReactionTypes except deleted returned", 4, transfusionReactionTypes.size());

    // Verify all reaction were returned
    Assert.assertTrue("Verify reaction 1 present", transfusionReactionTypes.contains(reactionType1));
    Assert.assertTrue("Verify reaction 2 present", transfusionReactionTypes.contains(reactionType2));
    Assert.assertTrue("Verify reaction 3 present", transfusionReactionTypes.contains(reactionType3));
    Assert.assertFalse("Verify reaction 4 absent", transfusionReactionTypes.contains(reactionType4));
    Assert.assertTrue("Verify reaction 5 present", transfusionReactionTypes.contains(reactionType5));
  }

  @Test
  public void testGetById_verifyCorrectEntityReturned() {
    TransfusionReactionType expectedReactionType = aTransfusionReactionType().withName("reaction 1").buildAndPersist(entityManager); // match
    aTransfusionReactionType().withName("reaction 2").buildAndPersist(entityManager); // no match

    TransfusionReactionType reactionType = transfusionReactionTypeRepository.findById(expectedReactionType.getId());

    assertThat(reactionType, hasSameStateAsTransfusionReactionType(expectedReactionType));
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testGetById_verifyExeptionThrown() {
    transfusionReactionTypeRepository.findById(1L);
  }
}
