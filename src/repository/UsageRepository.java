package repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.usage.ComponentUsage;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UsageRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveUsage(ComponentUsage componentUsage) {
    em.persist(componentUsage);
    em.flush();
  }

  public ComponentUsage findComponentUsage(String productNumber) throws NoResultException, NonUniqueResultException{
      String queryString = "SELECT p FROM ComponentUsage p WHERE p.productNumber = :productNumber and p.isDeleted= :isDeleted";
      TypedQuery<ComponentUsage> query = em.createQuery(queryString,
              ComponentUsage.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      ComponentUsage componentUsage = query.setParameter("productNumber",
              productNumber).getSingleResult();
      return componentUsage;
  }

  public void deleteAllUsages() {
    Query query = em.createQuery("DELETE FROM ComponentUsage u");
    query.executeUpdate();
  }

  public List<ComponentUsage> findAnyUsageMatching(String productNumber,
      String dateUsedFrom, String dateUsedTo, List<String> useIndications) throws ParseException {

    TypedQuery<ComponentUsage> query = em.createQuery(
        "SELECT u FROM ComponentUsage u WHERE "
            + "(u.productNumber = :productNumber OR "
            + "u.useIndication IN (:useIndications)) AND "
            + "(u.dateUsed BETWEEN :dateUsedFrom AND " + ":dateUsedTo) AND "
            + "(u.isDeleted= :isDeleted)", ComponentUsage.class);

    query.setParameter("isDeleted", Boolean.FALSE);

    query.setParameter("productNumber", productNumber == null ? ""
        : productNumber);
    query.setParameter("useIndications", useIndications);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      Date from = (dateUsedFrom == null || dateUsedFrom.equals("")) ? dateFormat
                  .parse("31/12/1970") : dateFormat.parse(dateUsedFrom);
  
      query.setParameter("dateUsedFrom", from);
   
      Date to = (dateUsedTo == null || dateUsedTo.equals("")) ? dateFormat
                  .parse(dateFormat.format(new Date())) : dateFormat.parse(dateUsedTo);
      query.setParameter("dateUsedTo", to);
   

    List<ComponentUsage> resultList = query.getResultList();
    return resultList;
  }

  public ComponentUsage findUsageByProductNumber(String productNumber) throws NoResultException, NonUniqueResultException{
    TypedQuery<ComponentUsage> query = em
        .createQuery(
            "SELECT u FROM ComponentUsage u WHERE u.productNumber = :productNumber and u.isDeleted= :isDeleted",
            ComponentUsage.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productNumber", productNumber);
    ComponentUsage usage = query.getSingleResult();
    return usage;
  }

  public void deleteUsage(String productNumber) {
    ComponentUsage existingUsage = findUsageByProductNumber(productNumber);
    existingUsage.setIsDeleted(Boolean.TRUE);
    em.merge(existingUsage);
    em.flush();
  }

  public ComponentUsage findUsageById(Long usageId) throws IllegalArgumentException{
    return em.find(ComponentUsage.class, usageId);
  }

  public ComponentUsage addUsage(ComponentUsage usage) {
    em.persist(usage);
    em.flush();
    em.refresh(usage);
    return usage;
  }
}
