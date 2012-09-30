package repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Request;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Repository
@Transactional
public class RequestRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveRequest(Request request) {
    em.persist(request);
    em.flush();
  }

  public Request updateRequest(Request request, String existingRequestNumber) {
    Request existingRequest = findRequest(existingRequestNumber);
    existingRequest.copy(request);
    em.merge(existingRequest);
    em.flush();
    return existingRequest;
  }

  public Request findRequest(String requestNumber) {
    Request request = null;
    if (requestNumber != null && requestNumber.length() > 0) {
      String queryString = "SELECT p FROM Request p WHERE p.requestNumber = :requestNumber and p.isDeleted= :isDeleted";
      TypedQuery<Request> query = em.createQuery(queryString, Request.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      List<Request> requests = query.setParameter("requestNumber",
          requestNumber).getResultList();
      if (requests != null && requests.size() > 0) {
        request = requests.get(0);
      }
    }
    return request;
  }

  public Request findRequest(Long requestId) {
    Request request = null;
    if (requestId != null) {
      String queryString = "SELECT p FROM Request p WHERE p.requestId = :requestId and p.isDeleted= :isDeleted";
      TypedQuery<Request> query = em.createQuery(queryString, Request.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      List<Request> requests = query.setParameter("requestId", requestId)
          .getResultList();
      if (requests != null && requests.size() > 0) {
        request = requests.get(0);
      }
    }
    return request;
  }

  public ArrayList<Request> getAllRequests() {
    String queryString = "SELECT p FROM Request p where p.isDeleted = :isDeleted order by p.dateRequested";
    TypedQuery<Request> query = em.createQuery(queryString, Request.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return new ArrayList<Request>(query.getResultList());
  }

  public void deleteAllRequests() {
    Query query = em.createQuery("DELETE FROM Request p");
    query.executeUpdate();
  }

  public List<Request> getRequests(Date fromDateRequested, Date toDateRequested) {
    TypedQuery<Request> query = em
        .createQuery(
            "SELECT p FROM Request p WHERE  p.dateRequested >= :fromDate and p.dateRequested<= :toDate and p.isDeleted = :isDeleted",
            Request.class);
    query.setParameter("fromDate", fromDateRequested);
    query.setParameter("toDate", toDateRequested);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Request> requests = query.getResultList();
    if (CollectionUtils.isEmpty(requests)) {
      return new ArrayList<Request>();
    }
    return requests;
  }

  public ArrayList<Request> getAllUnfulfilledRequests() {
    String queryString = "SELECT p FROM Request p where p.status = 'pending' or p.status='partiallyFulfilled' and p.isDeleted = :isDeleted order by p.dateRequested";
    TypedQuery<Request> query = em.createQuery(queryString, Request.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return new ArrayList<Request>(query.getResultList());
  }

  public List<Request> findAnyRequestMatching(String requestNumber,
      String dateRequestedFrom, String dateRequestedTo,
      String dateRequiredFrom, String dateRequiredTo, List<String> sites,
      List<String> productTypes, List<String> statuses) {

    TypedQuery<Request> query = em
        .createQuery(
            "SELECT r FROM Request r, Location L WHERE "
                + "(L.locationId=r.siteId AND L.isCollectionSite=TRUE) AND "
                + "(r.requestNumber = :requestNumber OR L.name IN (:sites) OR "
                + "r.productType IN (:productTypes)) AND (r.status IN (:statuses)) AND "
                + "((r.dateRequested BETWEEN :dateRequestedFrom AND "
                + ":dateRequestedTo) AND (r.dateRequired BETWEEN "
                + ":dateRequiredFrom AND " + ":dateRequiredTo)) AND "
                + "(r.isDeleted= :isDeleted)", Request.class);

    query.setParameter("isDeleted", Boolean.FALSE);

    query.setParameter("requestNumber", requestNumber == null ? ""
        : requestNumber);
    query.setParameter("sites", sites);
    query.setParameter("productTypes", productTypes);
    query.setParameter("statuses", statuses);

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    try {
      Date from = (dateRequestedFrom == null || dateRequestedFrom.equals("")) ? dateFormat
          .parse("12/31/1970") : dateFormat.parse(dateRequestedFrom);
      query.setParameter("dateRequestedFrom", from);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      Date to = (dateRequestedTo == null || dateRequestedTo.equals("")) ? dateFormat
          .parse(dateFormat.format(new Date())) : dateFormat
          .parse(dateRequestedTo);
      query.setParameter("dateRequestedTo", to);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    try {
      Date from = (dateRequiredFrom == null || dateRequiredFrom.equals("")) ? dateFormat
          .parse("12/31/1970") : dateFormat.parse(dateRequiredFrom);
      query.setParameter("dateRequiredFrom", from);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    try {
      Date to = (dateRequiredTo == null || dateRequiredTo.equals("")) ? dateFormat
          .parse(dateFormat.format(new Date())) : dateFormat
          .parse(dateRequiredTo);
      query.setParameter("dateRequiredTo", to);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    List<Request> resultList = query.getResultList();
    return resultList;
  }

  public Request findRequestByRequestNumber(String requestNumber) {
    TypedQuery<Request> query = em
        .createQuery(
            "SELECT r FROM Request r WHERE r.requestNumber = :requestNumber and r.isDeleted= :isDeleted",
            Request.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("requestNumber", requestNumber);
    List<Request> requests = query.getResultList();
    if (CollectionUtils.isEmpty(requests)) {
      return null;
    }
    return requests.get(0);
  }

  public Request updateOrAddRequest(Request request) {
    Request existingRequest = findRequestByRequestNumber(request
        .getRequestNumber());
    if (existingRequest == null) {
      request.setIsDeleted(false);
      saveRequest(request);
      return request;
    }
    existingRequest.copy(request);
    existingRequest.setIsDeleted(false);
    em.merge(existingRequest);
    em.flush();
    return existingRequest;
  }

  public List<Request> findRequestsNotFulfilled() {
    TypedQuery<Request> query = em.createQuery(
        "SELECT r FROM Request r, Location L WHERE "
            + "(L.locationId=r.siteId AND L.isCollectionSite=TRUE) AND"
            + "(r.status NOT IN (:statuses)) AND "
            + "(r.isDeleted= :isDeleted)", Request.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("statuses", Arrays.asList("fulfilled"));
    List<Request> resultList = query.getResultList();
    return resultList;
  }

  public void deleteRequest(String requestNumber) {
    Request existingRequest = findRequestByRequestNumber(requestNumber);
    existingRequest.setIsDeleted(Boolean.TRUE);
    em.merge(existingRequest);
    em.flush();
  }
}
