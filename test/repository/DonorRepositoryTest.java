package repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import model.user.User;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import repository.DonorRepository;
import repository.SequenceNumberRepository;
import security.LoginUserService;
import security.V2VUserDetails;
import backingform.DonorBackingForm;
import backingform.validator.DonorBackingFormValidator;
import controller.UtilController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration
public class DonorRepositoryTest {
	@SuppressWarnings("unused")
	private MockHttpServletRequest request;
	private Validator validator;
	@Autowired
	private UtilController utilController;
	@Autowired
	DonorRepository donorRepository;
	@Autowired
	private SequenceNumberRepository sequenceNumberRepository;
	private User user;
	private Donor donor;
	private long dbid, deletedbid, updatedbid;;;
	private String donorNumber;
	private DonorBackingForm donorBackingForm;
	private DonorBackingFormValidator donorBackingFormValidator;

	private BindException errors;
	ApplicationContext applicationContext = null;
	UserDetailsService userDetailsService;

	@Before
	public void init() {
		applicationContext = new ClassPathXmlApplicationContext(
				"file:**/security-v2v-servlet.xml");
		dbid = 1;// For Find Record set db id here.
		deletedbid = 2; // Delete datbase record set db id here.
		updatedbid = 121;// For update record set db id here.
		donorNumber = "000001";
		validator = new DonorBackingFormValidator();
		donorBackingFormValidator = new DonorBackingFormValidator(validator,
				utilController);
		donor = new Donor();
		donorBackingForm = new DonorBackingForm(donor);
		user = new User();
		user.setId(2);

		userDetailsService = applicationContext.getBean(LoginUserService.class);

	}

	@Test
	public void testAddDonor() {
		String DateToString = "10/06/1989";
		donorBackingForm.setBirthDate(DateToString);
		// Donorrandomnumber generation
		donorBackingForm.setDonorNumber("");
		setBackingFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		donorBackingFormValidator.validate(obj, errors);
		List<ObjectError> list = errors.getAllErrors();
		for (ObjectError oe : list) {
			System.out.println(oe.getDefaultMessage());
		}
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.addDonor(donor);
		boolean isIdZero = donor.getId() == 0 ? false : true;
		// dbid = donor.getId();
		assertTrue(isIdZero);
		System.out.println("success");
	}

	@Test
	public void testGenerateUniqueDonorNumber() {
		String generateRandomUniqueNo = DonorRepository
				.generateUniqueDonorNumber();
		assertFalse(StringUtils.isEmpty(generateRandomUniqueNo));
	}

	@Test
	public void testfindDonorByDonorNumber() {
		Donor donor = donorRepository.findDonorByDonorNumber("000001", false);
		System.out.println("Donor ::::" + donor);
		assertNotNull(donor);
	}

	@Test
	public void testSaveDonor() {
		String DateToString = "10/06/1991";
		donorBackingForm.setBirthDate(DateToString);
		donorBackingForm.setDonorNumber("");
		setBackingFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		donorBackingFormValidator.validate(obj, errors);
		List<ObjectError> list = errors.getAllErrors();
		for (ObjectError oe : list) {
			System.out.println(oe.getDefaultMessage());
		}
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.saveDonor(donor);
		/*
		 * Issue doonorHash value is set to null. donor status value is set to
		 * null.
		 */

		boolean isIdZero = donor.getId() == 0 ? false : true;
		// dbid = donor.getId();
		assertTrue(isIdZero);
		System.out.println("success");
	}

	@Test
	public void testUpdateDonor() {

		this.authentication();
		Donor editDonor = donorRepository.findDonorById(updatedbid);
		donorBackingForm = new DonorBackingForm(editDonor);
		setBackingUpdateFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		donorBackingFormValidator.validate(obj, errors);
		List<ObjectError> list = errors.getAllErrors();
		for (ObjectError oe : list) {
			System.out.println(oe.getDefaultMessage());
		}
		assertFalse(errors.hasErrors());
		System.out.println("editDonor__________" + editDonor.getLastUpdated());
		Donor updateDonor = donorBackingForm.getDonor();
		assertNotNull(donorRepository.updateDonor(updateDonor));
	}

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

	}

	@Test
	public void testgetAllDonors() {
		List<Donor> lists = donorRepository.getAllDonors();
		for (Donor donor : lists) {
			assertFalse(donor.getIsDeleted());
		}
	}

	public void setBackingFormValue(DonorBackingForm donorBackingForm) {
		Date date = new Date();
		donorBackingForm.setAddress("address");
		donorBackingForm.setFirstName("nikita");
		donorBackingForm.setMiddleName("middlename");
		donorBackingForm.setLastName("shah");
		donorBackingForm.setIsDeleted(false);
		donorBackingForm.setGender("male");
		donorBackingForm.setCallingName("CallingName");
		donorBackingForm.setCity("City");
		donorBackingForm.setCountry("country");
		donorBackingForm.setCreatedBy(user);
		donorBackingForm.setCreatedDate(date);
		donorBackingForm.setLastUpdated(date);
		donorBackingForm.setLastUpdatedBy(user);
		donorBackingForm.setDistrict("District");
		donorBackingForm.setDonorPanel("3");
		donorBackingForm.setNationalID("1111");
		donorBackingForm.setNotes("Notes");
		donorBackingForm.setOtherPhoneNumber("9999999999");
		donorBackingForm.setPhoneNumber("8888888888");
		donorBackingForm.setPreferredContactMethod("1");
		donorBackingForm.setProvince("Province");
		donorBackingForm.setState("State");
		donorBackingForm.setZipcode("361001");
	}

	 @Test
	public void testFindDonorByIdLong() {
		Donor donor = donorRepository.findDonorById(dbid);
		assertNotNull("Find Donor By Id method argument is long", donor);
	}

	 @Test
	public void testFindDonorByIdStringWithSpace() {
		Donor donor = donorRepository.findDonorById("18");
		assertNotNull("Find Donor By Id method argument is black space", donor);
	}

	 @Test
	public void testFindDonorByIdStringWithoutSpace() {
		Donor donor = donorRepository.findDonorById(String.valueOf(dbid));
		assertNotNull("Find Donor By Id method argument is String number",
				donor);
	}

	 @Test
	public void testFindDonorByNumber() {
		Donor donor = donorRepository.findDonorByNumber(donorNumber);
		assertNotNull("Find Donor by DonorNumber", donor);
	}

	 @Test
	public void testDeleteDonorById() {
		donorRepository.deleteDonor(deletedbid);
		assertTrue(true);
	}

	@Test
	public void testdeferDonor() {
		boolean isScucess = false;
		try {
			this.authentication();
			donorRepository.deferDonor("168", "28/02/2014", "1", "text");
			isScucess = true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			isScucess = false;
		} finally {
			assertTrue(isScucess);
		}
	}

	@Test
	public void testaddAllDonors() {
		Donor donor = new Donor();
		DonorBackingForm donorBackingForm = new DonorBackingForm(donor);
		String DateToString = "10/06/1991";
		donorBackingForm.setBirthDate(DateToString);

		donorBackingForm.setDonorNumber(sequenceNumberRepository
				.getNextDonorNumber());
		setBackingFormValue(donorBackingForm);
		List<Donor> listAllDonor = new ArrayList<Donor>();
		listAllDonor.add(donorBackingForm.getDonor());
		donor = new Donor();
		donorBackingForm = new DonorBackingForm(donor);
		DateToString = "11/06/1991";
		donorBackingForm.setBirthDate(DateToString);
		donorBackingForm.setDonorNumber(sequenceNumberRepository
				.getNextDonorNumber());
		setBackingFormValue(donorBackingForm);
		listAllDonor.add(donorBackingForm.getDonor());
		donor = new Donor();
		donorBackingForm = new DonorBackingForm(donor);
		DateToString = "12/06/1991";
		donorBackingForm.setBirthDate(DateToString);
		donorBackingForm.setDonorNumber(sequenceNumberRepository
				.getNextDonorNumber());
		setBackingFormValue(donorBackingForm);
		listAllDonor.add(donorBackingForm.getDonor());
		donorRepository.addAllDonors(listAllDonor);
	}
	
	@Test
	public void testGetDeferralReasons(){
		List<DeferralReason> list = donorRepository.getDeferralReasons();
		if(list!=null && list.size()>0){
			for(DeferralReason deferralReason:list){
				if(deferralReason.getIsDeleted()==true){
					assertTrue(false);
				}
			}
		}else{
			assertTrue(false);
		}
	}
	
	@Test
	public void testGetDonorDeferrals()
	{
		List<DonorDeferral> list = donorRepository.getDonorDeferrals(1l);
		if(list!=null && list.size()>0){
				for(DonorDeferral donorDeferral:list){
					System.out.println(donorDeferral.getId());
				}
		}
		
	}
	
	@Test
	public void testisCurrentlyDeferredPassList(){
		List<DonorDeferral> list = donorRepository.getDonorDeferrals(167l);
		System.out.println("Is defer donor found with list as a argument:::"+donorRepository.isCurrentlyDeferred(list));
	}
	
	@Test
	public void testisCurrentlyDeferredPassDonor(){
		Donor donor = donorRepository.findDonorById(167l);
		
		System.out.println("Is defer donor found with Donor argument as argument:::"+donorRepository.isCurrentlyDeferred(donor));
	}
	

	public void authentication() {
		V2VUserDetails userDetails = (V2VUserDetails) userDetailsService
				.loadUserByUsername("admin");
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}

}
