package repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import model.donor.Donor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration
public class DonorRepositoryTest {
	@Autowired
	DonorRepository donorRepository;
	private long dbid;
	private String donorNumber;
	
	@Before
	public void init(){
		dbid = 2;
		donorNumber="000010";
	}

	@Test
	public void testFindDonorByIdLong() {
		Donor donor = donorRepository.findDonorById(dbid);
		assertNotNull("Find Donor By Id method argument is long",donor);
	}

	@Test
	public void testFindDonorByIdStringWithblackstring() {
		Donor donor = donorRepository.findDonorById(" ");
		assertNotNull("Find Donor By Id method argument is black space",donor);
	}
	
	@Test
	public void testFindDonorByIdStringWithoutblackstring() {
		Donor donor = donorRepository.findDonorById(String.valueOf(dbid));
		
		assertNotNull("Find Donor By Id method argument is String number",donor);
	}
	
	@Test
	public void testFindDonorByNumber() {
		 Donor donor= donorRepository.findDonorByNumber(donorNumber);
		 assertNotNull("Find Donor by DonorNumber",donor);
	}
	
	@Test
	public void testDeleteDonorById() {
		 donorRepository.deleteDonor(dbid);
		 Donor donor = donorRepository.findDonorById(dbid);
		assertNull("Delete Donor object return null",donor);
	}
	
}
