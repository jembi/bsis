package repository;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.producttype.ProductType;
import model.producttype.ProductTypeTimeUnits;

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
  
  public List<ProductType> getAllProductTypesIncludeDeleted() {
    TypedQuery<ProductType> query;
    query = em.createQuery("SELECT p from ProductType p", ProductType.class);
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

  public ProductType getProductTypeById(Integer id) {
    TypedQuery<ProductType> query;
    query = em.createQuery("SELECT pt from ProductType pt " +
            "where pt.id=:id", ProductType.class);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void saveNewProductType(Map<String, Object> newProductTypeAsMap) {
    ProductType productType = new ProductType();
    productType.setProductType((String) newProductTypeAsMap.get("productType"));
    productType.setProductTypeNameShort((String) newProductTypeAsMap.get("productTypeNameShort"));
    try {
      Integer expiresAfter = Integer.parseInt((String) newProductTypeAsMap.get("expiresAfter"));
      productType.setExpiresAfter(expiresAfter);
    } catch (NumberFormatException ex) {
      ex.printStackTrace();
    }
    productType.setCanPool(false);
    productType.setCanSubdivide(true);
    productType.setDescription("");
    Boolean hasBloodGroup = Boolean.valueOf((String) newProductTypeAsMap.get("hasBloodGroup"));
    productType.setHasBloodGroup(hasBloodGroup);
    ProductTypeTimeUnits expiresAfterUnits;
    expiresAfterUnits = ProductTypeTimeUnits.valueOf((String) newProductTypeAsMap.get("expiresAfterUnits"));
    productType.setExpiresAfterUnits(expiresAfterUnits);
    productType.setIsDeleted(false);
    em.persist(productType);
  }

  public void deactivateProductType(Integer productTypeId) {
    ProductType productType = getProductTypeById(productTypeId);
    productType.setIsDeleted(true);
    em.merge(productType);
  }

  public void activateProductType(Integer productTypeId) {
    ProductType productType = getProductTypeById(productTypeId);
    productType.setIsDeleted(false);
    em.merge(productType);
  }
}
