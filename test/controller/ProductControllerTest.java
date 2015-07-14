/**
 * 
 */
package controller;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityExistsException;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.junit.Test;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import repository.DonationRepository;
import repository.ProductRepository;
import repository.ProductTypeRepository;
import model.donation.Donation;
import model.product.Product;
import model.product.ProductStatus;
import model.producttype.ProductType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration
public class ProductControllerTest {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private DonationRepository donationRepository;
	
	@Autowired
	private ProductTypeRepository productTypeRepository;
	
	// Test case for record new productComponents
	@Ignore @Test
	public void recordNewProductComponentsTest() {
		
		 Product savedProduct = null;
		
				ProductType productType2 = productRepository.findProductTypeBySelectedProductType(1);
	      String collectionNumber = "D0001";
	      String status = "QUARANTINED";
	      long productId = 1L;
	      
	      if(collectionNumber.contains("-")){
	      	collectionNumber = collectionNumber.split("-")[0];
	      }
	      String sortName = productType2.getProductTypeNameShort();
	      int noOfUnits = 3;
	      long donationId = 1;
	      
	      String createdPackNumber = collectionNumber +"-"+sortName;
	      
	      // Add New product
	      if(!status.equalsIgnoreCase("PROCESSED")){
	      if(noOfUnits > 0 ){
	      	
	      	for(int i=1; i <= noOfUnits ; i++){
	      		try{
		        	Product product = new Product();
		          product.setIsDeleted(false);
		          product.setComponentIdentificationNumber(createdPackNumber+"-"+i);
		          Calendar c=new GregorianCalendar();
		          System.out.println("after :"+ productTypeRepository.getProductTypeById(1).getExpiryIntervalMinutes());
		          c.add(Calendar.MINUTE, productTypeRepository.getProductTypeById(1).getExpiryIntervalMinutes());
		          Date expiredate=c.getTime();
		          
		          
		          product.setCreatedOn(new Date());
		          product.setExpiresOn(expiredate);
		          ProductType productType = new ProductType();
		          productType.setProductTypeName(productType2.getProductTypeName());
		          productType.setId(productType2.getId());
		          product.setProductType(productType);
		          Donation donation = new Donation();
		          donation.setId(donationId);
		          product.setDonation(donation);
		          product.setStatus(ProductStatus.QUARANTINED);
			        productRepository.addProduct(product);
	
			        // Once product save successfully update selected product status with processed
			        productRepository.setProductStatusToProcessed(productId);
			        
			      } catch (EntityExistsException ex) {
			        ex.printStackTrace();
			      } catch (Exception ex) {
			        ex.printStackTrace();
			      }
	      	}
	      }
	      else{
	      	
	      	try{
		        	Product product = new Product();
		          product.setIsDeleted(false);
		          product.setComponentIdentificationNumber(createdPackNumber);

		          Calendar c=new GregorianCalendar();
		          System.out.println("after :"+ productTypeRepository.getProductTypeById(1).getExpiryIntervalMinutes());
		          c.add(Calendar.MINUTE, productTypeRepository.getProductTypeById(1).getExpiryIntervalMinutes());
		          Date expiredate=c.getTime();
		          
		          product.setCreatedOn(new Date());
		          product.setExpiresOn(expiredate);
		          ProductType productType = new ProductType();
		          productType.setProductTypeName(productType2.getProductTypeName());
		          productType.setId(productType2.getId());
		          product.setProductType(productType);
		          Donation donation = new Donation();
		          donation.setId(donationId);
		          product.setDonation(donation);
		          product.setStatus(ProductStatus.QUARANTINED);
			        productRepository.addProduct(product);
			        productRepository.setProductStatusToProcessed(productId);
			        
			      } catch (EntityExistsException ex) {
			        ex.printStackTrace();
			      } catch (Exception ex) {
			        ex.printStackTrace();
			      }
		    	}
	      }
		}
	
}


