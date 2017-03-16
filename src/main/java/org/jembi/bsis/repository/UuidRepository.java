package org.jembi.bsis.repository;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UuidRepository {

  @PersistenceContext
  private EntityManager em;

  public UUID generateBinaryUUID() {
    UUID uuid = null;
    try {
      uuid = (UUID) em.createQuery("SELECT GENERATEBINARYUUID()").getSingleResult();
    } catch (Exception e) {
      uuid = UUID.randomUUID();
    }
    return uuid;
  }

}
