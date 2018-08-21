package org.jembi.bsis.repository;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.packtype.PackType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PackTypeRepository {

  public static final String NAME_FIND_PRODUCING_TEST_SAMPLES = "PackType.findProducingTestSamples";
  public static final String QUERY_FIND_PRODUCING_TEST_SAMPLES = "SELECT pt FROM PackType pt " +
      "WHERE pt.testSampleProduced = :testSampleProduced";

  @PersistenceContext
  private EntityManager entityManager;

  public List<PackType> getAllPackTypes() {
    TypedQuery<PackType> query;
    query = entityManager.createQuery("SELECT b from PackType b", PackType.class);
    return query.getResultList();
  }

  public List<PackType> getAllEnabledPackTypes() {
    TypedQuery<PackType> query;
    query = entityManager.createQuery("SELECT b from PackType b where b.isDeleted=:isDeleted", PackType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public List<PackType> getAllPackTypesProducingTestSamples() {
    return entityManager.createNamedQuery(NAME_FIND_PRODUCING_TEST_SAMPLES, PackType.class)
        .setParameter("testSampleProduced", true)
        .getResultList();
  }

  public PackType findPackTypeByName(String packType) {
    String queryString = "SELECT b FROM PackType b WHERE b.packType = :packTypeName";
    TypedQuery<PackType> query = entityManager.createQuery(queryString, PackType.class);
    query.setParameter("packTypeName", packType);
    PackType result = null;
    try {
      result = query.getSingleResult();
    } catch (NoResultException ex) {
    }
    return result;
  }

  public PackType getPackTypeById(UUID packTypeId) {
    TypedQuery<PackType> query;
    query = entityManager.createQuery("SELECT b from PackType b " +
        "where b.id=:id", PackType.class);

    query.setParameter("id", packTypeId);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllPackTypes(List<PackType> allPackTypes) {
    for (PackType bt : allPackTypes) {
      PackType existingPackType = getPackTypeById(bt.getId());
      if (existingPackType != null) {
        existingPackType.setPackType(bt.getPackType());
        entityManager.merge(existingPackType);
      } else {
        entityManager.persist(bt);
      }
    }
    entityManager.flush();
  }

  public PackType savePackType(PackType packType) {
    entityManager.persist(packType);
    entityManager.flush();
    return packType;
  }

  public PackType updatePackType(PackType packType) {
    PackType existingPackType = getPackTypeById(packType.getId());
    if (existingPackType == null) {
      return null;
    }
    existingPackType.copy(packType);
    entityManager.merge(existingPackType);
    entityManager.flush();
    return existingPackType;
  }
}
