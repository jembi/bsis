package repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
      if (productType.getProductTypeName().equals(checkProductType))
        return true;
    }
    return false;
  }

  public ProductType getProductTypeById(Integer id) throws NoResultException, NonUniqueResultException{
    TypedQuery<ProductType> query;
    query = em.createQuery("SELECT pt from ProductType pt " +
            "where pt.id=:id", ProductType.class);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getSingleResult();
  }

  public void deactivateProductType(Integer productTypeId){
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

  public ProductTypeCombination getProductTypeCombinationById(Integer id)
          throws NoResultException, NonUniqueResultException{
    TypedQuery<ProductTypeCombination> query;
    query = em.createQuery("SELECT pt from ProductTypeCombination pt " +
            "where pt.id=:id", ProductTypeCombination.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }

  public void saveComponentTypeCombination(
      ProductTypeCombination productTypeCombination) {
      
    String combinationName = productTypeCombination.getCombinationName();
    List<ProductType> productTypes = new ArrayList<ProductType>();
    List<String> combinationNameList = new ArrayList<String>();
  
    for (ProductType productType : productTypeCombination.getProductTypes()) {
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
  
  public ProductType getProductTypeByName(String productTypeName) throws NoResultException, NonUniqueResultException{
    TypedQuery<ProductType> query;
    query = em.createQuery("SELECT pt from ProductType pt " +
            "where pt.productType=:productTypeName", ProductType.class);
    query.setParameter("productTypeName", productTypeName);
    ProductType productType = null;
    productType = query.getSingleResult();
    return productType;
  }
  
  public List<ProductType> getAllParentProductTypes() {
    TypedQuery<ProductType> query;
    List<ProductType> productTypes = new ArrayList<ProductType>();
    query = em.createQuery("SELECT p from ProductType p where p.isDeleted=:isDeleted AND pediProductType_id != null AND p.id!= 1", ProductType.class);
    query.setParameter("isDeleted", false);
    productTypes = query.getResultList(); 
    productTypes.add(getProductTypeByIdList(1).get(0));
    return productTypes;
  }
  
  public List<ProductType> getProductTypeByIdList(Integer id) {
    TypedQuery<ProductType> query;
    query = em.createQuery("SELECT pt from ProductType pt where pt.id IN (SELECT pt1.pediProductType from ProductType pt1 WHERE pt1.id=:id) ", ProductType.class);
    query.setParameter("id", id);
    if (query.getResultList().size() == 0)
      return null;
    return query.getResultList();
  }
  
  public ProductType saveComponentType(ProductType componentType) {
	em.persist(componentType);
	return componentType;
  }
  
  public ProductType updateComponentType(ProductType productType){
    ProductType existingProductType = getProductTypeById(productType.getId());
    existingProductType.copy(productType);
    return em.merge(existingProductType);
  }
  
  
  public ProductTypeCombination updateComponentTypeCombination(
          ProductTypeCombination componentTypeCombination)throws IllegalArgumentException{
	 ProductTypeCombination existingProductTypeCombination = getProductTypeCombinationById(componentTypeCombination.getId());
	 existingProductTypeCombination.copy(componentTypeCombination);
     return em.merge(existingProductTypeCombination);
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
          
}
