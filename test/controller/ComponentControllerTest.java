/**
 * 
 */
package controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityExistsException;

import model.component.Component;
import model.component.ProductStatus;
import model.donation.Donation;
import model.producttype.ProductType;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import repository.DonationRepository;
import repository.ComponentRepository;
import repository.ProductTypeRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/bsis-servlet.xml")
@WebAppConfiguration
public class ComponentControllerTest {
	
	@Autowired
	private ComponentRepository componentRepository;
	
	@Autowired
	private DonationRepository donationRepository;
	
	@Autowired
	private ProductTypeRepository productTypeRepository;
	
	// Test case for record new productComponents
	@Ignore @Test
	public void recordNewProductComponentsTest() {
		
		 Component savedProduct = null;
		
				ProductType productType2 = componentRepository.findProductTypeBySelectedProductType(1);
	      String donationIdentificationNumber = "D0001";
	      String status = "QUARANTINED";
	      long productId = 1L;
	      
	      if(donationIdentificationNumber.contains("-")){
	      	donationIdentificationNumber = donationIdentificationNumber.split("-")[0];
	      }
	      String sortName = productType2.getProductTypeNameShort();
	      int noOfUnits = 3;
	      long donationId = 1;
	      
	      String createdPackNumber = donationIdentificationNumber +"-"+sortName;
	      
	      // Add New product
	      if(!status.equalsIgnoreCase("PROCESSED")){
	      if(noOfUnits > 0 ){
	      	
	      	for(int i=1; i <= noOfUnits ; i++){
	      		try{
		        	Component product = new Component();
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
			        componentRepository.addComponent(product);
	
			        // Once product save successfully update selected product status with processed
			        componentRepository.setProductStatusToProcessed(productId);
			        
			      } catch (EntityExistsException ex) {
			        ex.printStackTrace();
			      } catch (Exception ex) {
			        ex.printStackTrace();
			      }
	      	}
	      }
	      else{
	      	
	      	try{
		        	Component product = new Component();
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
			        componentRepository.addComponent(product);
			        componentRepository.setProductStatusToProcessed(productId);
			        
			      } catch (EntityExistsException ex) {
			        ex.printStackTrace();
			      } catch (Exception ex) {
			        ex.printStackTrace();
			      }
		    	}
	      }
		}
	
}


