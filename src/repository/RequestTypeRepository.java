package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.requesttype.RequestType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RequestTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public List<RequestType> getAllRequestTypes() {
    TypedQuery<RequestType> query;
    query = em.createQuery("SELECT r from RequestType r where r.isDeleted=:isDeleted", RequestType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public boolean isRequestTypeValid(String checkRequestType) {
    String queryString = "SELECT r from RequestType r where r.isDeleted=:isDeleted";
    TypedQuery<RequestType> query = em.createQuery(queryString, RequestType.class);
    query.setParameter("isDeleted", false);
    for (RequestType requestType : query.getResultList()) {
      if (requestType.getRequestType().equals(checkRequestType))
        return true;
    }
    return false;
  }

  public RequestType getRequestTypeById(Long requestTypeId) throws NoResultException {
    TypedQuery<RequestType> query;
    query = em.createQuery("SELECT r from RequestType r " +
        "where r.id=:id AND r.isDeleted=:isDeleted", RequestType.class);
    query.setParameter("isDeleted", false);
    query.setParameter("id", requestTypeId);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveRequestType(RequestType requestType) {
    em.persist(requestType);
  }

  public RequestType updateRequestType(RequestType requestType)
      throws IllegalArgumentException {
    return em.merge(requestType);
  }

  public RequestType getRequestTypeByName(String requestTypeName) throws NoResultException, NonUniqueResultException {
    TypedQuery<RequestType> query;
    query = em.createQuery("SELECT r from RequestType r " +
        "where r.requestType=:requestTypeName AND r.isDeleted=:isDeleted", RequestType.class);
    query.setParameter("isDeleted", false);
    query.setParameter("requestTypeName", requestTypeName);
    RequestType requestType = null;
    requestType = query.getSingleResult();
    return requestType;
  }
}
