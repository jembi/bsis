package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.TypedQuery;

import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.springframework.stereotype.Repository;

@Repository
public class ReturnFormRepository extends AbstractRepository<ReturnForm> {

  public ReturnForm findById(UUID id) {
    TypedQuery<ReturnForm> query = entityManager.createNamedQuery(ReturnFormNamedQueryConstants.NAME_FIND_BY_ID, ReturnForm.class);
    query.setParameter("id", id);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public List<ReturnForm> findReturnForms(Date returnDateFrom, Date returnDateTo, UUID returnedFromId,
      UUID returnedToId, ReturnStatus status) {
    String queryString = "SELECT r FROM ReturnForm r WHERE r.isDeleted = :isDeleted ";

    if (returnDateFrom != null) {
      queryString = queryString + "AND r.returnDate >= :returnDateFrom ";
    }
    if (returnDateTo != null) {
      queryString = queryString + "AND r.returnDate <= :returnDateTo ";
    }
    if (returnedFromId != null) {
      queryString = queryString + "AND r.returnedFrom.id = :returnedFromId ";
    }
    if (returnedToId != null) {
      queryString = queryString + "AND r.returnedTo.id = :returnedToId ";
    }
    if (status != null) {
      queryString = queryString + "AND r.status = :status ";
    }

    queryString = queryString + "ORDER BY r.returnDate DESC";

    TypedQuery<ReturnForm> query = entityManager.createQuery(queryString, ReturnForm.class);
    query.setParameter("isDeleted", false);

    if (returnDateFrom != null) {
      query.setParameter("returnDateFrom", returnDateFrom);
    }
    if (returnDateTo != null) {
      query.setParameter("returnDateTo", returnDateTo);
    }
    if (returnedFromId != null) {
      query.setParameter("returnedFromId", returnedFromId);
    }
    if (returnedToId != null) {
      query.setParameter("returnedToId", returnedToId);
    }
    if (status != null) {
      query.setParameter("status", status);
    }

    return query.getResultList();
  }
}