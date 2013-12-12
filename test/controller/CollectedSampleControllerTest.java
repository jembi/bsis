package controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;


import java.math.BigDecimal;
import java.util.Date;
import backingform.CollectedSampleBackingForm;
import repository.DonorRepository;
import repository.CollectedSampleRepository;
import model.collectedsample.CollectedSample;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.util.Gender;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration
public class CollectedSampleControllerTest {
	
	  @Autowired
    DonorRepository donorRepository;
    
    @Autowired
    private CollectedSampleRepository collectedSampleRepository;
  	
	@Test
	 public void testAddDateOfFirstDonation() {
	  try{
	  	
		  	Donor donor = new Donor();

		  	Long donorId = 1L;
		  	donor = donorRepository.findDonorById(donorId);
		  	
		  	CollectedSample savedCollection = null;
	      try {
	      	CollectedSample collectedSample = new CollectedSample();
	        collectedSample.setDonor(donor);
	        collectedSample.setCollectedOn(new Date());
	        collectedSample.setBloodPressureDiastolic(100);
	        collectedSample.setBloodPressureSystolic(90);
	        collectedSample.setHaemoglobinCount(BigDecimal.valueOf(25));
	        collectedSample.setDonorPulse(75);
	        collectedSample.setDonorWeight(BigDecimal.valueOf(70));
	        collectedSample.setCollectionNumber("C0000001");
	        
	        if(collectedSample.getDonor().getDateOfFirstDonation() == null){
	        	collectedSample.getDonor().setDateOfFirstDonation(collectedSample.getCollectedOn());
	        }
	        
	        collectedSample.setIsDeleted(false);
	        savedCollection = collectedSampleRepository.addCollectedSample(collectedSample);
	        
	      } catch (EntityExistsException ex) {
	        ex.printStackTrace();
	      } catch (Exception ex) {
	        ex.printStackTrace();
	      }
	    	  	
	  }catch(Exception e){
	   e.printStackTrace();
	  }
	 }
	

}