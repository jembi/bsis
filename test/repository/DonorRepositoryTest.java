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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import security.LoginUserService;
import security.V2VUserDetails;
import util.HSQLServerUtil;
import backingform.DonorBackingForm;
import controller.UtilController;

@ContextConfiguration(locations = { "classpath*:ApplicationContextTest.xml" })
public class DonorRepositoryTest extends
		AbstractTransactionalTestNGSpringContextTests {
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
	private long donorDbId, donorDeletedDbId;
	private int userDbId;
	private String deletedDonorNmber, donorNumber;
	private DonorBackingForm donorBackingForm;
	String donorBirthdate = null;
	ApplicationContext applicationContext = null;
	UserDetailsService userDetailsService;

	@Test(dependsOnMethods = { "setupUser", "setUpContactMethodType",
			"setUpLocation" }, description = "Donor Object should persist. method=addDonor(Donor)")
	@Rollback(false)
	public void addDonor_shouldPersist() {

		try {
			DonorBackingForm donorBackingForm = new DonorBackingForm();
			setBackingFormValue(donorBackingForm);
			donorBackingForm
					.setDonorNumber(utilController.getNextDonorNumber());
			donorRepository.addDonor(donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.", donorBackingForm
					.getDonor().getId() == 0 ? false : true);
			donorDbId = donorBackingForm.getDonor().getId();
			donorNumber = donorBackingForm.getDonor().getDonorNumber();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = { "addDonor_shouldPersist" }, description = "Donor object should not null when Donor Number match with an existing Donor. method=updateDonor(Donor)")
	@Rollback(false)
	public void updateDonor_shouldReturnNotNull() {
		Donor editDonor = donorRepository.findDonorById(donorDbId);
		donorBackingForm = new DonorBackingForm(editDonor);
		setBackingUpdateFormValue(donorBackingForm);
		assertNotNull("Donor Object should update.",
				donorRepository.updateDonor(donorBackingForm.getDonor()));
	}

	@Test(dependsOnMethods = { "deleteDonor_shouldPersist" }, description = "should return deleted donor because IsDelete true. method=findDonorByDonorNumber(String,boolean)")
	public void findDonorByDonorNumber_deleteDonorObjectShouldNotNullDonorDeleteTrue() {
		try {
			assertNotNull("Donor object should not null.",
					donorRepository.findDonorByDonorNumber(deletedDonorNmber,
							true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(dependsOnMethods = { "addDonor_shouldPersist" }, description = "should return none deleted donor because IsDelete false. method=findDonorByDonorNumber(String,boolean)")
	public void findDonorByDonorNumber_donorObjectShouldNotNullDonorDeleteFalse() {
		try {
			assertNotNull("Donor object should not null.",
					donorRepository.findDonorByDonorNumber(donorNumber,
							false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = { "addDonor_shouldPersist",
			"updateDonor_shouldReturnNotNull" }, description = "should return none deleted listall donor. method=getAllDonors()")
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

	@Test(dependsOnMethods = { "setupUser", "setUpContactMethodType",
			"setUpLocation" }, description = "Donor Object should persist. method=addDonor(Donor)")
	@Rollback(false)
	public void deleteDonor_shouldPersist() {

		try {
			DonorBackingForm donorBackingForm = new DonorBackingForm();
			setBackingFormValue(donorBackingForm);
			donorBackingForm
					.setDonorNumber(utilController.getNextDonorNumber());
			donorBackingForm.setIsDeleted(true);
			donorRepository.addDonor(donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.", donorBackingForm
					.getDonor().getId() == 0 ? false : true);
			deletedDonorNmber = donorBackingForm.getDonor().getDonorNumber();
			donorDeletedDbId = donorBackingForm.getDonor().getId();
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
	 * This method is useful to configure instance variable value which is used
	 * by different test case .
	 * 
	 * @throws AclFormatException
	 * @throws IOException
	 */
	@BeforeClass
	public void init() {
		try {
			HSQLServerUtil.getInstance().start("DBNAME");

		} catch (Exception ee) {

		}

	}

	/**
	 * Create New Location
	 */
	@Test
	@Rollback(false)
	public void setUpLocation() {
		try {
			model.location.Location location = new Location();
			location.setName("location");
			locationRepository.saveLocation(location);
			System.out.println("Location Id is:::" + location.getId());

			location = new Location();
			location.setName("location");
			locationRepository.saveLocation(location);
			System.out.println("Location Id is:::" + location.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create New User
	 */
	@Test
	@Rollback(false)
	public void setupUser() {
		try {
			user = new User();
			user.setEmailId("email@email.com");
			user.setFirstName("admin");
			user.setIsAdmin(true);
			user.setIsActive(true);
			user.setIsDeleted(false);
			user.setIsStaff(true);
			user.setUsername("admin");
			user.setPassword("admin");
			userRepository.addUser(user);
			userDbId = user.getId();
			user = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create various Contact Method Type
	 */
	@Test
	@Rollback(false)
	public void setUpContactMethodType() {
		String[] str = { "None", "Phone", "SMS", "Email", "Mail",
				"Do not contact" };
		ContactMethodType contactMethodType = null;
		for (String contactString : str) {
			contactMethodType = new ContactMethodType();
			contactMethodType.setContactMethodType(contactString);
			contactMethodType.setIsDeleted(false);
			contactMethodTypeRepository.saveContactMethod(contactMethodType);
			contactMethodType = null;
		}

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
