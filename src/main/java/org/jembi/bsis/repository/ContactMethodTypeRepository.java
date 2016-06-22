package org.jembi.bsis.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.address.ContactMethodType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ContactMethodTypeRepository {

  @PersistenceContext
  EntityManager em;

  public List<ContactMethodType> getAllContactMethodTypes() {
    TypedQuery<ContactMethodType> query;
    query = em.createQuery("SELECT c from ContactMethodType c where c.isDeleted=:isDeleted", ContactMethodType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public void saveContactMethod(ContactMethodType contactMethodType) {
    em.persist(contactMethodType);
    em.flush();
  }
}
