package repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Product;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Repository
@Transactional
public class ProductRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveProduct(Product product) {
    em.persist(product);
    em.flush();
  }

  public Product findProduct(String productNumber) {
    Product product = null;
    if (productNumber != null && productNumber.length() > 0) {
      String queryString = "SELECT p FROM Product p WHERE p.productNumber = :productNumber and p.isDeleted= :isDeleted";
      TypedQuery<Product> query = em.createQuery(queryString, Product.class);
      query.setParameter("isDeleted", Boolean.FALSE);
      List<Product> products = query.setParameter("productNumber",
          productNumber).getResultList();
      if (products != null && products.size() > 0) {
        product = products.get(0);
      }
    }
    return product;
  }

  public List<Product> getAllUnissuedProducts() {
    String queryString = "SELECT p FROM Product p where p.isDeleted = :isDeleted and p.isIssued= :isIssued";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    return query.getResultList();
  }

  public List<Product> getAllUnissuedThirtyFiveDayProducts() {
    String queryString = "SELECT p FROM Product p where p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.dateCollected > :minDate";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    query.setParameter("minDate", new DateTime(new Date()).minusDays(35)
        .toDate());
    return query.getResultList();
  }

  public List<Product> getAllProducts() {
    String queryString = "SELECT p FROM Product p where p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.getResultList();
  }

  public boolean isProductCreated(String collectionNumber) {
    String queryString = "SELECT p FROM Product p WHERE p.collectionNumber = :collectionNumber and p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Product> products = query.setParameter("collectionNumber",
        collectionNumber).getResultList();
    if (products != null && products.size() > 0) {
      return true;
    }
    return false;
  }

  public void deleteAllProducts() {
    Query query = em.createQuery("DELETE FROM Product p");
    query.executeUpdate();
  }

  public List<Product> getAllProducts(String productType) {
    String queryString = "SELECT p FROM Product p where p.type = :productType and p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productType", productType);
    return query.getResultList();
  }

  public List<Product> getProducts(Date fromDate, Date toDate) {
    TypedQuery<Product> query = em
        .createQuery(
            "SELECT p FROM Product p WHERE  p.dateCollected >= :fromDate and p.dateCollected<= :toDate and p.isDeleted = :isDeleted",
            Product.class);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Product> products = query.getResultList();
    if (CollectionUtils.isEmpty(products)) {
      return new ArrayList<Product>();
    }
    return products;
  }

  public List<Product> getAllUnissuedProducts(String productType, String abo,
      String rhd) {
    String queryString = "SELECT p FROM Product p where p.type = :productType and p.abo= :abo and p.rhd= :rhd and p.isDeleted = :isDeleted and p.isIssued= :isIssued";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    query.setParameter("productType", productType);
    query.setParameter("abo", abo);
    query.setParameter("rhd", rhd);
    return query.getResultList();
  }

  public List<Product> getAllUnissuedThirtyFiveDayProducts(String productType,
      String abo, String rhd) {
    String queryString = "SELECT p FROM Product p where p.type = :productType and p.abo= :abo and p.rhd= :rhd and p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.dateCollected > :minDate";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("isIssued", Boolean.FALSE);
    query.setParameter("productType", productType);
    query.setParameter("abo", abo);
    query.setParameter("rhd", rhd);
    query.setParameter("minDate", new DateTime(new Date()).minusDays(35)
        .toDate());

    return query.getResultList();
  }

  public Product findProduct(Long productId) {
    return em.find(Product.class, productId);
  }

  public void updateProductBloodGroup(String collectionNumber, String abo,
      String rhd) {
    String queryString = "SELECT p FROM Product p WHERE p.collectionNumber = :collectionNumber and p.isDeleted = :isDeleted";
    TypedQuery<Product> query = em.createQuery(queryString, Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    List<Product> products = query.setParameter("collectionNumber",
        collectionNumber).getResultList();
    for (Product product : products) {
      if (StringUtils.hasText(abo)) {
        product.setAbo(abo);
      }
      if (StringUtils.hasText(rhd)) {
        product.setRhd(rhd);
      }
      em.merge(product);
    }
    em.flush();
  }

  public List<Product> findAnyProductMatching(String productNumber,
      String collectionNumber, List<String> types, List<String> availability) {

    TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE "
        + "(p.productNumber = :productNumber OR "
        + "p.collectionNumber = :collectionNumber "
        + "OR p.type IN (:types)) AND (p.isIssued IN (:isIssued)) AND "
        + "(p.isDeleted= :isDeleted)",
        Product.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    String productNo = ((productNumber == null) ? "" : productNumber);
    query.setParameter("productNumber", productNo);
    String collectionNo = ((collectionNumber == null) ? "" : collectionNumber);
    query.setParameter("collectionNumber", collectionNo);
    query.setParameter("types", types);
    query.setParameter("isIssued", getIssuedListFromAvailability(availability));

    List<Product> resultList = query.getResultList();
    return resultList;
  }

  private List<Boolean> getIssuedListFromAvailability(List<String> availability) {
    List<Boolean> issued = new ArrayList<Boolean>();
    if (availability == null)
      return issued;
    for (String available : availability) {
      if (available.equals("available"))
        issued.add(false);
      if (available.equals("notAvailable")) {
        issued.add(true);
      }
    }
    return issued;
  }

  public Product findProductByProductNumber(String productNumber) {
    TypedQuery<Product> query = em
        .createQuery(
            "SELECT p FROM Product p WHERE p.productNumber = :productNumber and p.isDeleted= :isDeleted",
            Product.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productNumber", productNumber);
    List<Product> products = query.getResultList();
    if (CollectionUtils.isEmpty(products)) {
      return null;
    }
    return products.get(0);
  }

  public Product updateOrAddProduct(Product product) {
    Product existingProduct = findProductByProductNumber(product
        .getProductNumber());
    if (existingProduct == null) {
      product.setIssued(Boolean.FALSE);
      product.setIsDeleted(false);
      saveProduct(product);
      return product;
    }
    existingProduct.setCollectionNumber(product.getCollectionNumber());
    existingProduct.setType(product.getType());
    product.setIssued(Boolean.FALSE);
    existingProduct.setIsDeleted(false);
    em.merge(existingProduct);
    em.flush();
    return existingProduct;
  }

  public void deleteProduct(String productNumber) {
    Product existingProduct = findProductByProductNumber(productNumber);
    existingProduct.setIsDeleted(Boolean.TRUE);
    em.merge(existingProduct);
    em.flush();
  }
}
