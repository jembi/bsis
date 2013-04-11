package repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeReasonCategory;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ProductStatusChangeReasonRepository {

  @PersistenceContext
  private EntityManager em;

  public List<ProductStatusChangeReason> getAllProductStatusChangeReasons() {
    TypedQuery<ProductStatusChangeReason> query;
    query = em.createQuery("SELECT p from ProductStatusChangeReason p where p.isDeleted=:isDeleted",
        ProductStatusChangeReason.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public List<ProductStatusChangeReason> getProductStatusChangeReasons(
      ProductStatusChangeReasonCategory category) {
    TypedQuery<ProductStatusChangeReason> query;
    query = em.createQuery("SELECT p from ProductStatusChangeReason p where " +
    		"p.category=:category AND p.isDeleted=:isDeleted",
        ProductStatusChangeReason.class);
    query.setParameter("isDeleted", false);
    query.setParameter("category", category);
    return query.getResultList();
  }
  
  public ProductStatusChangeReason getProductStatusChangeReasonById(Integer id) {
    TypedQuery<ProductStatusChangeReason> query;
    query = em.createQuery("SELECT p from ProductStatusChangeReason p " +
            "where p.id=:id AND p.isDeleted=:isDeleted", ProductStatusChangeReason.class);
    query.setParameter("isDeleted", false);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public Map<ProductStatusChangeReasonCategory, ProductStatusChangeReason> getAllProductStatusChangeReasonsAsMap() {
    TypedQuery<ProductStatusChangeReason> query;
    query = em.createQuery("SELECT p from ProductStatusChangeReason p where p.isDeleted=:isDeleted",
        ProductStatusChangeReason.class);
    query.setParameter("isDeleted", false);
    Map<ProductStatusChangeReasonCategory, ProductStatusChangeReason> statusChangeReasonMap =
        new HashMap<ProductStatusChangeReasonCategory, ProductStatusChangeReason>();
    for (ProductStatusChangeReason statusChangeReason : query.getResultList()) {
      statusChangeReasonMap.put(statusChangeReason.getCategory(), statusChangeReason);
    }
    return statusChangeReasonMap;
  }
}
