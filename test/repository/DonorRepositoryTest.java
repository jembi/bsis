package repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import model.address.ContactMethodType;
import model.donor.Donor;
import model.location.Location;
import model.user.User;

import org.hsqldb.server.ServerAcl.AclFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import security.LoginUserService;
import security.V2VUserDetails;
import util.Verifies;
import backingform.DonorBackingForm;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import controller.UtilController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/ApplicationContextTest.xml")
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@DatabaseSetup("classpath:/DonorDataset.xml")
public class DonorRepositoryTest {
	@Autowired
	private UtilController utilController;
	@Autowired
	DonorRepository donorRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private SequenceNumberRepository sequenceNumberRepository;
	private User user;
	@Autowired(required = false)
	private RoleRepository roleRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private ContactMethodTypeRepository contactMethodTypeRepository;
	private int userDbId=1;
	private DonorBackingForm donorBackingForm;
	String donorBirthdate = null;
	ApplicationContext applicationContext = null;
	UserDetailsService userDetailsService;

	@Test
	@Verifies(value="should return  donor because IsDeleted false.",method="findDonorByDonorNumber(String,boolean)")
	
	public void findDonorByDonorNumber_donorObjectShouldNotNullDonorDeleteFalse() {
		try {
			assertNotNull("Donor object should not null.",
					donorRepository.findDonorByDonorNumber("000001",
							false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Verifies(value = "Donor Object should persist.", method = "addDonor(Donor)")
	public void addDonor_shouldPersist() {

		try {
			DonorBackingForm donorBackingForm = new DonorBackingForm();
			setBackingFormValue(donorBackingForm);
			donorBackingForm
					.setDonorNumber("000004");
			donorRepository.addDonor(donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.", donorBackingForm
					.getDonor().getId() == 0 ? false : true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
		@Verifies(value = "Donor Object should persist.", method = "saveDonor(Donor)")
		public void saveDonor_shouldPersist() {
			donorBackingForm = new DonorBackingForm();
			setBackingFormValue(donorBackingForm);
			donorBackingForm.setDonorNumber("000005");
			donorRepository.saveDonor(donorBackingForm.getDonor());
			assertTrue(donorBackingForm.getDonor().getId() == 0 ? false : true);
		}
	
	@Test
	@Verifies(value="Donor object should not null when Donor Number match with an existing Donor",method="updateDonor(Donor)")
	public void updateDonor_shouldReturnNotNull() {
		Donor editDonor = donorRepository.findDonorById(1l);
		donorBackingForm = new DonorBackingForm(editDonor);
		setBackingUpdateFormValue(donorBackingForm);
		assertNotNull("Donor Object should update.",
				donorRepository.updateDonor(donorBackingForm.getDonor()));
	}

	@Test
	@Verifies(value="should return deleted donor because IsDelete true.",method="findDonorByDonorNumber(String,boolean)")
	public void findDonorByDonorNumber_deleteDonorObjectShouldNotNullDonorDeleteTrue() {
		try {
			assertNotNull("Donor object should not null.",
					donorRepository.findDonorByDonorNumber("000002",
							true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Verifies(value="should delete donor from database.",method="deleteDonor(long)")
	public void deleteDonor_shouldSoftDeleteDonorFromDatabase() {
		boolean isDelete = true;
		try{
		donorRepository.deleteDonor(2l);
		}catch(Exception e){
			isDelete = false;
		}finally{
			assertFalse("SoftDelete operation is completed successfully.",isDelete);
		}
	}
	
	
	@Test
	@Verifies(value="should return none deleted donor list.",method="getAllDonors()")
	public void getAllDonors_shouldReturnNoneDeleteDonor() {
		try {
			List<Donor> listDonor = donorRepository.getAllDonors();
			for (Donor donor : listDonor) {
				assertFalse(donor.getIsDeleted());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	

	/**
	 * Called when insert new record into table.
	 * 
	 * @param donorBackingForm
	 */
	public void setBackingFormValue(DonorBackingForm donorBackingForm) {
		Date date = new Date();
		donorBackingForm.setAddress("myaddress");
		donorBackingForm.setFirstName("firstname");
		donorBackingForm.setMiddleName("middlename");
		donorBackingForm.setLastName("lastname");
		donorBackingForm.setIsDeleted(false);
		donorBackingForm.setGender("male");
		donorBackingForm.setCallingName("CallingName");
		donorBackingForm.setCity("City");
		donorBackingForm.setCountry("country");
		user = new User();
		user.setId(userDbId);
		donorBackingForm.setCreatedBy(user);
		donorBackingForm.setCreatedDate(date);
		donorBackingForm.setLastUpdated(date);
		donorBackingForm.setLastUpdatedBy(user);
		donorBackingForm.setDistrict("District");
		donorBackingForm.setDonorPanel("1");
		donorBackingForm.setNationalID("1111");
		donorBackingForm.setNotes("Notes");
		donorBackingForm.setOtherPhoneNumber("9999999999");
		donorBackingForm.setPhoneNumber("8888888888");
		donorBackingForm.setPreferredContactMethod("1");
		donorBackingForm.setProvince("Province");
		donorBackingForm.setState("State");
		donorBackingForm.setZipcode("361001");
		donorBackingForm.setBirthDate(donorBirthdate);
		donorBackingForm.setBirthDateEstimated(true);
	}
	
	

	/**
	 * Called when update existing record.
	 * 
	 * @param donorBackingForm
	 */
	public void setBackingUpdateFormValue(DonorBackingForm donorBackingForm) {
		donorBackingForm.setAddress("address_update");
		donorBackingForm.setFirstName("firstName_update");
		donorBackingForm.setMiddleName("middlename_update");
		donorBackingForm.setLastName("lastname_update");
		donorBackingForm.setIsDeleted(false);
		donorBackingForm.setGender("female");
		donorBackingForm.setCallingName("CallingName_update");
		donorBackingForm.setCity("City_update");
		donorBackingForm.setCountry("country_update");
		donorBackingForm.setDistrict("District_update");
		donorBackingForm.setDonorPanel("2");
		donorBackingForm.setNationalID("1212");
		donorBackingForm.setNotes("Notes_update");
		donorBackingForm.setOtherPhoneNumber("9878787878");
		donorBackingForm.setPhoneNumber("874525452");
		donorBackingForm.setPreferredContactMethod("2");
		donorBackingForm.setProvince("Province_update");
		donorBackingForm.setState("State_update");
		donorBackingForm.setZipcode("361001");
		user = new User();
		user.setId(userDbId);
		donorBackingForm.setCreatedBy(user);
	}

	
	

	/**
	 * UserPassword,V2vUserDetails(Principal) and authority detail store into
	 * SecurityContextHolder.
	 */
	public void userAuthentication() {
		applicationContext = new ClassPathXmlApplicationContext(
				"file:**/security-v2v-servlet.xml");
		userDetailsService = applicationContext.getBean(LoginUserService.class);
		V2VUserDetails userDetails = (V2VUserDetails) userDetailsService
				.loadUserByUsername("admin");
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}

}
