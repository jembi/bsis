package repository;



import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import model.address.ContactMethodType;
import model.donor.Donor;
import model.location.Location;
import model.user.User;

import org.hsqldb.server.ServerAcl.AclFormatException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import util.HSQLServerUtil;
import util.Verifies;
import backingform.DonorBackingForm;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

import controller.UtilController;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
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
	private Donor donor;
	@Autowired
	private	RoleRepository roleRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private ContactMethodTypeRepository contactMethodTypeRepository;
	private int dbid;
	private DonorBackingForm donorBackingForm;
	String donorBirthdate = null;
	ApplicationContext applicationContext = null;
	UserDetailsService userDetailsService;

	/**
	 * This method is useful to configure instance variable value which is used by different test case .
	 * @throws AclFormatException 
	 * @throws IOException 
	 */
	@Before
	public void init() throws IOException, AclFormatException {
		HSQLServerUtil.getInstance().start("DBNAME"); 
		setupUser();
		setUpLocation();
		setUpContactMethodType();
		donorBirthdate = "10/06/1989";
		donor = new Donor();
		donorBackingForm = new DonorBackingForm(donor);
	}
	
	public void setUpLocation(){
		try{
			model.location.Location location = new Location();
			location.setName("location");
			locationRepository.saveLocation(location);
			System.out.println("Location Id is:::"+location.getId());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void setupUser(){
		try{
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
		dbid = user.getId();
		user = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setUpContactMethodType(){
		String[] str={"None","Phone","SMS","Email","Mail","Do not contact"};
		ContactMethodType contactMethodType = null;
		for(String contactString:str){
		contactMethodType = new ContactMethodType();
		contactMethodType.setContactMethodType(contactString);
		contactMethodType.setIsDeleted(false);
		contactMethodTypeRepository.saveContactMethod(contactMethodType);
		contactMethodType = null;
		}
		
	}
	
	@Test
	@Verifies(value = "Donor Object should persist.", method = "addDonor(Donor)")
	public void addDonor_shouldPersist() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber(utilController.getNextDonorNumber());
		donorBackingForm.setFirstName("adddonor");
		donorRepository.addDonor( donorBackingForm.getDonor());
		System.out.println("Donor ID IS::::"+donorBackingForm.getDonor().getId());
		System.out.println("Donor Number:::::"+donorBackingForm.getDonor().getDonorNumber());
		assertTrue("Donor Object should persist.",donorBackingForm.getDonor().getId() == 0 ? false : true);
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
		user.setId(dbid);
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

		
}
