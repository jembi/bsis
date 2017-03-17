package org.jembi.bsis.repository;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UuidRepository {

  @PersistenceContext
  private EntityManager em;

  public UUID generateBinaryUUID() {
    SessionImplementor sessionImplementor = (SessionImplementor) em.getDelegate();
    Dialect dialect = sessionImplementor.getFactory().getDialect();
    // HSQL does not support the sql function. The below is a work-around so that
    // the JUnit suite, which uses HSQL, will work.
    if (dialect instanceof HSQLDialect) {
      return UUID.randomUUID();
    } else {
      return (UUID) em.createQuery("SELECT GENERATEBINARYUUID()").getSingleResult();
    }
  }

}
