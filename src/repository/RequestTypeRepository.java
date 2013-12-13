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

  public RequestType getRequestTypeById(Integer requestTypeId) {
    TypedQuery<RequestType> query;
    query = em.createQuery("SELECT r from RequestType r " +
            "where r.id=:id AND r.isDeleted=:isDeleted", RequestType.class);
    query.setParameter("isDeleted", false);
    query.setParameter("id", requestTypeId);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllRequestTypes(List<RequestType> allRequestTypes) {
    for (RequestType rt: allRequestTypes) {
        RequestType existingRequestType = getRequestTypeById(rt.getId());
        if (existingRequestType != null) {
          existingRequestType.setRequestType(rt.getRequestType());
          existingRequestType.setBulkTransfer(rt.getBulkTransfer());
          em.merge(existingRequestType);
        }
        else {
          rt.setDescription("");
          em.persist(rt);
        }
    }
    em.flush();
  }

  public RequestType getRequestTypeByName(String requestTypeName) {
    TypedQuery<RequestType> query;
    query = em.createQuery("SELECT r from RequestType r " +
            "where r.requestType=:requestTypeName AND r.isDeleted=:isDeleted", RequestType.class);
    query.setParameter("isDeleted", false);
    query.setParameter("requestTypeName", requestTypeName);
    RequestType requestType = null;
    try {
      requestType = query.getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
    } catch (NonUniqueResultException ex) {
      ex.printStackTrace();
    }
    return requestType;
  }
}
