/**
 * 
 */
package controller;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityExistsException;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import repository.CollectedSampleRepository;
import repository.ProductRepository;
import repository.ProductTypeRepository;
import model.collectedsample.CollectedSample;
import model.product.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration
public class ProductControllerTest {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CollectedSampleRepository collectedSampleRepository;
	
	@Autowired
	private ProductTypeRepository productTypeRepository;
	
	// Test case for Save product
	@Test
	public void addProductTest() {
		
		 Product savedProduct = null;
		try {
      Product product = new Product();
      product.setIsDeleted(false);
      
      CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(1L);
      collectedSample.setUnitWeight(BigDecimal.valueOf(113L));
      product.setCollectedSample(collectedSample);
      product.setProductType(productTypeRepository.getProductTypeById(1));
      product.setCreatedOn(new Date());
      
      Calendar c=new GregorianCalendar();
      System.out.println("after :"+ productTypeRepository.getProductTypeById(1).getExpiryIntervalMinutes());
      c.add(Calendar.MINUTE, productTypeRepository.getProductTypeById(1).getExpiryIntervalMinutes());
      Date expiredate=c.getTime();
      product.setExpiresOn(expiredate);
      product.setNotes("test");
      
      savedProduct = productRepository.addProduct(product);
      } catch (Exception ex) {
      ex.printStackTrace();
    }
	}
	
	// Test case for update product
	@Test
	public void updateProductTest() {
		try {
      Product product = new Product();
      product.setIsDeleted(false);
      product.setId(1L);
      
      //update unit weight in collectionsample table
      CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(1L);
      collectedSample.setUnitWeight(BigDecimal.valueOf(113L));
      product.setCollectedSample(collectedSample);
      
      product.setProductType(productTypeRepository.getProductTypeById(2));
      product.setCreatedOn(new Date());
      
      Calendar c=new GregorianCalendar();
      c.add(Calendar.MINUTE, productTypeRepository.getProductTypeById(1).getExpiryIntervalMinutes());
      Date expiredate=c.getTime();
      product.setExpiresOn(expiredate);
      product.setNotes("test");
      
      Product existingProduct = productRepository.updateProduct(product);
      
      } catch (Exception ex) {
      ex.printStackTrace();
    }
	}

}
