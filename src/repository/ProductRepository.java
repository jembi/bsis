package repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.collectedsample.CollectedSample;
import model.product.Product;
import model.producttype.ProductType;
import model.util.BloodAbo;
import model.util.BloodRhd;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

  private Date getDateExpiresFromOrDefault(String dateExpiresFrom) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date from = null;
    try {
      from = (dateExpiresFrom == null || dateExpiresFrom.equals("")) ? dateFormat
          .parse("12/31/1970") : dateFormat.parse(dateExpiresFrom);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return from;      
  }

  private Date getDateExpiresToOrDefault(String dateExpiresTo) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date to = null;
    try {
      to = (dateExpiresTo == null || dateExpiresTo.equals("")) ? new Date() :
              dateFormat.parse(dateExpiresTo);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(to);
    cal.add(Calendar.DATE, 365);
    to = cal.getTime();
    return to;      
  }

  public List<Product> findProductByProductNumber(
      String productNumber, String dateExpiresFrom,
      String dateExpiresTo) {

    TypedQuery<Product> query = em
        .createQuery(
            "SELECT p FROM Product p WHERE " +
            "p.productNumber = :productNumber and " +
            "((p.expiresOn is NULL) or " +
            " (p.expiresOn >= :fromDate and p.expiresOn <= :toDate)) and " +
            "c.isDeleted= :isDeleted",
            Product.class);

    Date from = getDateExpiresFromOrDefault(dateExpiresFrom);
    Date to = getDateExpiresToOrDefault(dateExpiresTo);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productNumber", productNumber);
    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);

    return query.getResultList();
  }

  public List<Product> findProductByCollectionNumber(
      String collectionNumber, String dateExpiresFrom,
      String dateExpiresTo) {

    TypedQuery<Product> query = em
        .createQuery(
            "SELECT p FROM Product p WHERE " +
            "p.collectionNumber = :collectionNumber and " +
            "((p.expiresOn is NULL) or " +
            " (p.expiresOn >= :fromDate and p.expiresOn <= :toDate)) and " +
            "c.isDeleted= :isDeleted",
            Product.class);

    Date from = getDateExpiresFromOrDefault(dateExpiresFrom);
    Date to = getDateExpiresToOrDefault(dateExpiresTo);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("collectionNumber", collectionNumber);
    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);

    return query.getResultList();
  }

  public List<Product> findProductByProductTypes(
      List<String> productTypes, String dateExpiresFrom, String dateExpiresTo) {
    TypedQuery<Product> query = em
        .createQuery(
            "SELECT p FROM Product p WHERE " +
            "p.productType.productType IN (:productTypes) and " +
            "((p.expiresOn is NULL) or " +
            " (p.expiresOn >= :fromDate and p.expiresOn <= :toDate)) and " +
            "p.isDeleted= :isDeleted",
            Product.class);

    Date from = getDateExpiresFromOrDefault(dateExpiresFrom);
    Date to = getDateExpiresToOrDefault(dateExpiresTo);

    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("productTypes", productTypes);
    query.setParameter("fromDate", from);
    query.setParameter("toDate", to);

    return query.getResultList();
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

  public Product findProductById(Long productId) {
    return em.find(Product.class, productId);
  }

  public List<Product> findAnyProductMatching(String productNumber,
      String collectionNumber, List<ProductType> types, List<String> availability) {

    return findAnyProductMatching(productNumber, collectionNumber,
        Arrays.asList(BloodAbo.A, BloodAbo.B, BloodAbo.O, BloodAbo.AB),
        Arrays.asList(BloodRhd.POSITIVE, BloodRhd.NEGATIVE), types, availability);
  }

  public List<Product> findAnyProductMatching(String productNumber,
      String collectionNumber, List<BloodAbo> bloodAbo, List<BloodRhd> bloodRhd,
      List<ProductType> types, List<String> availability) {

    TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE "
        + "(p.productNumber = :productNumber OR "
        + "p.collectionNumber = :collectionNumber "
        + "OR p.type IN (:types)) AND (p.isIssued IN (:isIssued)) AND "
        + "p.abo IN (:bloodAbo) AND p.rhd IN (:bloodRhd) AND "
        + "(p.isDeleted= :isDeleted)", Product.class);

    query.setParameter("isDeleted", Boolean.FALSE);
    String productNo = ((productNumber == null) ? "" : productNumber);
    query.setParameter("productNumber", productNo);
    String collectionNo = ((collectionNumber == null) ? "" : collectionNumber);
    query.setParameter("collectionNumber", collectionNo);
    query.setParameter("bloodAbo", bloodAbo);
    query.setParameter("bloodRhd", bloodRhd);
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

  public Product updateProduct(Product product) {
    Product existingProduct = findProductById(product.getId());
    if (existingProduct == null) {
      return null;
    }
    existingProduct.copy(product);
    em.merge(existingProduct);
    em.flush();
    return existingProduct;
  }

  public Product updateOrAddProduct(Product product) {
    Product existingProduct = findProductByProductNumber(product
        .getProductNumber());
    if (existingProduct == null) {
      product.setIsDeleted(false);
      saveProduct(product);
      return product;
    }
    existingProduct.copy(product);
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

  public void issueProduct(String productNumber) {
    Product existingProduct = findProductByProductNumber(productNumber);
//    existingProduct.setIssued(Boolean.TRUE);
    em.merge(existingProduct);
    em.flush();
  }

  public void addProduct(Product product) {
    em.persist(product);
    em.flush();
  }

  public void deleteProduct(Long productId) {
    Product existingProduct = findProductById(productId);
    existingProduct.setIsDeleted(Boolean.TRUE);
    em.merge(existingProduct);
    em.flush();
  }
}
