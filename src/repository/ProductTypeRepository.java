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
    query = em.createQuery("SELECT p from ProductType p where p.isDeleted=:isDeleted", ProductType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }
  
  public boolean isProductTypeValid(String checkProductType) {
    String queryString = "SELECT p from ProductType p where p.isDeleted=:isDeleted";
    TypedQuery<ProductType> query = em.createQuery(queryString, ProductType.class);
    query.setParameter("isDeleted", false);
    for (ProductType productType : query.getResultList()) {
      if (productType.getProductType().equals(checkProductType))
        return true;
    }
    return false;
  }

  public ProductType fromString(String productType) {
    TypedQuery<ProductType> query;
    query = em.createQuery("SELECT p from ProductType p " +
    		    "where p.productType=:productType AND p.isDeleted=:isDeleted", ProductType.class);
    query.setParameter("isDeleted", false);
    query.setParameter("productType", productType);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveAllProductTypes(List<ProductType> allProductTypes) {
    for (ProductType pt: allProductTypes) {
        ProductType existingProductType = fromString(pt.getProductType());
        if (existingProductType != null) {
          existingProductType.setProductTypeName(pt.getProductTypeName());
          em.merge(existingProductType);
        }
        else {
          pt.setShelfLife(0);
          pt.setShelfLifeUnits("");
          pt.setDescription("");
          em.persist(pt);
        }
    }
    em.flush();
  }
}
