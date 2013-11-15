package repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import model.producttype.ProductTypeTimeUnits;

import org.apache.commons.lang3.StringUtils;
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
    productType.setProductType((String) newProductTypeAsMap.get("productTypeName"));
    productType.setProductTypeNameShort((String) newProductTypeAsMap.get("productTypeNameShort"));
    try {
      Integer expiresAfter = Integer.parseInt((String) newProductTypeAsMap.get("expiresAfter"));
      productType.setExpiresAfter(expiresAfter);
    } catch (NumberFormatException ex) {
      productType.setExpiresAfter(0);
      ex.printStackTrace();
    }
    productType.setDescription("");
    Boolean hasBloodGroup = Boolean.valueOf((String) newProductTypeAsMap.get("hasBloodGroup"));
    productType.setHasBloodGroup(hasBloodGroup);
    ProductTypeTimeUnits expiresAfterUnits;
    expiresAfterUnits = ProductTypeTimeUnits.valueOf((String) newProductTypeAsMap.get("expiresAfterUnits"));
    productType.setExpiresAfterUnits(expiresAfterUnits);
    productType.setIsDeleted(false);
    if (newProductTypeAsMap.get("createPediProductType").equals("true")) {
      ProductType pediProductType = new ProductType();
      pediProductType.setProductType(productType.getProductType() + " Pedi");
      pediProductType.setProductTypeNameShort(productType.getProductTypeNameShort() + " Pedi");
      pediProductType.setExpiresAfter(productType.getExpiresAfter());
      pediProductType.setExpiresAfterUnits(productType.getExpiresAfterUnits());
      pediProductType.setDescription("");
      pediProductType.setHasBloodGroup(productType.getHasBloodGroup());
      productType.setPediProductType(pediProductType);
      pediProductType.setIsDeleted(false);
      em.persist(pediProductType);
    }
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

  public List<ProductTypeCombination> getAllProductTypeCombinations() {

    String queryStr = "SELECT pt from ProductTypeCombination pt WHERE " +
                      "pt.isDeleted=:isDeleted";
    TypedQuery<ProductTypeCombination> query = em.createQuery(queryStr, ProductTypeCombination.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public List<ProductTypeCombination> getAllProductTypeCombinationsIncludeDeleted() {

    String queryStr = "SELECT pt from ProductTypeCombination pt";
    TypedQuery<ProductTypeCombination> query = em.createQuery(queryStr, ProductTypeCombination.class);
    return query.getResultList();
  }

  public ProductTypeCombination getProductTypeCombinationById(Integer id) {
    TypedQuery<ProductTypeCombination> query;
    query = em.createQuery("SELECT pt from ProductTypeCombination pt " +
            "where pt.id=:id", ProductTypeCombination.class);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void updateProductType(Map<String, Object> newProductTypeAsMap) {
    String productTypeId = (String) newProductTypeAsMap.get("id");
    ProductType productType = getProductTypeById(Integer.parseInt(productTypeId));
    productType.setProductType((String) newProductTypeAsMap.get("productTypeName"));
    productType.setProductTypeNameShort((String) newProductTypeAsMap.get("productTypeNameShort"));
    try {
      Integer expiresAfter = Integer.parseInt((String) newProductTypeAsMap.get("expiresAfter"));
      productType.setExpiresAfter(expiresAfter);
    } catch (NumberFormatException ex) {
      productType.setExpiresAfter(0);
      ex.printStackTrace();
    }
    ProductTypeTimeUnits expiresAfterUnits;
    expiresAfterUnits = ProductTypeTimeUnits.valueOf((String) newProductTypeAsMap.get("expiresAfterUnits"));
    productType.setExpiresAfterUnits(expiresAfterUnits);
    em.merge(productType);
  }

  public void deactivateProductTypeCombination(Integer productTypeCombinationId) {
    ProductTypeCombination productTypeCombination = getProductTypeCombinationById(productTypeCombinationId);
    productTypeCombination.setIsDeleted(true);
    em.merge(productTypeCombination);
  }

  public void activateProductTypeCombination(Integer productTypeCombinationId) {
    ProductTypeCombination productTypeCombination = getProductTypeCombinationById(productTypeCombinationId);
    productTypeCombination.setIsDeleted(false);
    em.merge(productTypeCombination);
  }

  public void saveNewProductTypeCombination(
      Map<String, Object> newProductTypeCombinationAsMap) {
    ProductTypeCombination productTypeCombination = new ProductTypeCombination();
    String combinationName = (String) newProductTypeCombinationAsMap.get("combinationName");
    String productTypeIds = (String) newProductTypeCombinationAsMap.get("productTypeIds");
    Set<ProductType> productTypes = new HashSet<ProductType>();
    List<String> combinationNameList = new ArrayList<String>();
    for (String productTypeId : productTypeIds.split(",")) {
      if (StringUtils.isBlank(productTypeId))
        continue;
      ProductType productType = getProductTypeById(Integer.parseInt(productTypeId));
      productTypes.add(productType);
      combinationNameList.add(productType.getProductTypeNameShort());
    }

    if (StringUtils.isBlank(combinationName)) {
      combinationName = StringUtils.join(combinationNameList, ",");
    }

    productTypeCombination.setCombinationName(combinationName);
    productTypeCombination.setProductTypes(productTypes);

    productTypeCombination.setIsDeleted(false);
    em.persist(productTypeCombination);
  }

  public ProductType getProductTypeByName(String productTypeName) {
    TypedQuery<ProductType> query;
    query = em.createQuery("SELECT pt from ProductType pt " +
            "where pt.productType=:productTypeName", ProductType.class);
    query.setParameter("productTypeName", productTypeName);
    ProductType productType = null;
    try {
      productType = query.getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
    } catch (NonUniqueResultException ex) {
      ex.printStackTrace();
    }
    return productType;
  }
  
  public List<ProductType> getAllParentProductTypes() {
    TypedQuery<ProductType> query;
    query = em.createQuery("SELECT p from ProductType p where p.isDeleted=:isDeleted AND pediProductType_id != null", ProductType.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }
  
  public List<ProductType> getProductTypeByIdList(Integer id) {
    TypedQuery<ProductType> query;
//    query = em.createQuery("SELECT pt from ProductType pt " +
//            "where pt.id=:id", ProductType.class);

  query = em.createQuery("SELECT pt from ProductType pt where pt.id IN (SELECT pt1.pediProductType from ProductType pt1 WHERE pt1.id=:id) ", ProductType.class);

    //SELECT * FROM productType WHERE id IN (SELECT pediProductType_id FROM productType WHERE id=2);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getResultList();
  }
}
