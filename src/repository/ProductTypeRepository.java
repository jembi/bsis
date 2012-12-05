package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.producttype.ProductType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ProductTypeRepository {

  @PersistenceContext
  private EntityManager em;

  public List<ProductType> getAllProductTypes() {
    TypedQuery<ProductType> query;
    query = em.createQuery("SELECT p from ProductType p", ProductType.class);
    return query.getResultList();
  }
  
  public boolean isProductTypeValid(String checkProductType) {
    String queryString = "SELECT p from ProductType p";
    TypedQuery<ProductType> query = em.createQuery(queryString, ProductType.class);
    for (ProductType productType : query.getResultList()) {
      if (productType.getProductType().equals(checkProductType))
        return true;
    }
    return false;
  }

  public ProductType fromString(String productType) {
    TypedQuery<ProductType> query;
    query = em.createQuery("SELECT p from ProductType p " +
    		    "where b.productType=:productType", ProductType.class);
    query.setParameter("productType", productType);
    return query.getSingleResult();
  }
}
