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

import model.usage.ProductUsage;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Repository
@Transactional
public class UsageRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveUsage(ProductUsage productUsage) {
    em.persist(productUsage);
    em.flush();
  }

  public ProductUsage findProductUsage(String productNumber) throws NoResultException, NonUniqueResultException{
      String queryString = "SELECT p FROM ProductUsage p WHERE p.productNumber = :productNumber and p.isDeleted= :isDeleted";
      TypedQuery<ProductUsage> query = em.createQuery(queryString,
              ProductUsage.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      ProductUsage productUsage = query.setParameter("productNumber",
              productNumber).getSingleResult();
      return productUsage;
  }

  public void deleteAllUsages() {
    Query query = em.createQuery("DELETE FROM ProductUsage u");
    query.executeUpdate();
  }

  public List<ProductUsage> findAnyUsageMatching(String productNumber,
      String dateUsedFrom, String dateUsedTo, List<String> useIndications) {

    TypedQuery<ProductUsage> query = em.createQuery(
        "SELECT u FROM ProductUsage u WHERE "
            + "(u.productNumber = :productNumber OR "
            + "u.useIndication IN (:useIndications)) AND "
            + "(u.dateUsed BETWEEN :dateUsedFrom AND " + ":dateUsedTo) AND "
            + "(u.isDeleted= :isDeleted)", ProductUsage.class);

    query.setParameter("isDeleted", Boolean.FALSE);

    query.setParameter("productNumber", productNumber == null ? ""
        : productNumber);
    query.setParameter("useIndications", useIndications);

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      Date from = null;
      try {
          from = (dateUsedFrom == null || dateUsedFrom.equals("")) ? dateFormat
                  .parse("31/12/1970") : dateFormat.parse(dateUsedFrom);
      } catch (ParseException ex) {
          ex.printStackTrace();
      }
      query.setParameter("dateUsedFrom", from);
   
      Date to = null;
      try {
          to = (dateUsedTo == null || dateUsedTo.equals("")) ? dateFormat
                  .parse(dateFormat.format(new Date())) : dateFormat.parse(dateUsedTo);
      } catch (ParseException ex) {
          ex.printStackTrace();
      }
      query.setParameter("dateUsedTo", to);
   

    List<ProductUsage> resultList = query.getResultList();
    return resultList;
  }

  public ProductUsage findUsageByProductNumber(String productNumber) throws NoResultException, NonUniqueResultException{
    TypedQuery<ProductUsage> query = em
        .createQuery(
            "SELECT u FROM ProductUsage u WHERE u.productNumber = :productNumber and u.isDeleted= :isDeleted",
            ProductUsage.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productNumber", productNumber);
    ProductUsage usage = query.getSingleResult();
    return usage;
  }

  public void deleteUsage(String productNumber) {
    ProductUsage existingUsage = findUsageByProductNumber(productNumber);
    existingUsage.setIsDeleted(Boolean.TRUE);
    em.merge(existingUsage);
    em.flush();
  }

  public ProductUsage findUsageById(Long usageId) throws IllegalArgumentException{
    return em.find(ProductUsage.class, usageId);
  }

  public ProductUsage addUsage(ProductUsage usage) {
    em.persist(usage);
    em.flush();
    em.refresh(usage);
    return usage;
  }
}
