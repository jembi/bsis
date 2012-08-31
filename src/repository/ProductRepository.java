package repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Collection;
import model.Product;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Repository
@Transactional
public class ProductRepository {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private CollectionRepository collectionRepository;

	public void saveProduct(Product product) {
		Collection collection = collectionRepository.findCollection(product
				.getCollectionNumber());
		product.setAbo(collection.getAbo());
		product.setRhd(collection.getRhd());
		em.persist(product);
		em.flush();
	}

	public Product updateProduct(Product product, String existingProductNumber) {
		Product existingProduct = findProduct(existingProductNumber);
		Collection collection = collectionRepository.findCollection(product
				.getCollectionNumber());
		product.setAbo(collection.getAbo());
		product.setRhd(collection.getRhd());
		existingProduct.copy(product);
		em.merge(existingProduct);
		em.flush();
		return existingProduct;
	}

	public Product findProduct(String productNumber) {
		Product product = null;
		if (productNumber != null && productNumber.length() > 0) {
			String queryString = "SELECT p FROM Product p WHERE p.productNumber = :productNumber and p.isDeleted= :isDeleted";
			TypedQuery<Product> query = em.createQuery(queryString,
					Product.class);
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

	public void delete(String existingProductNumber) {
		Product existingProduct = findProduct(existingProductNumber);
		existingProduct.setIsDeleted(Boolean.TRUE);
		em.merge(existingProduct);
		em.flush();
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

	public List<Product> getAllUnissuedThirtyFiveDayProducts(
			String productType, String abo, String rhd) {
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
}
