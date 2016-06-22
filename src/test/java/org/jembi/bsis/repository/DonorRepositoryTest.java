package org.jembi.bsis.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.apache.commons.lang.time.DateUtils;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.backingform.DonorBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.dto.DuplicateDonorDTO;
import org.jembi.bsis.model.address.Address;
import org.jembi.bsis.model.address.AddressType;
import org.jembi.bsis.model.address.Contact;
import org.jembi.bsis.model.donation.DonationConstants;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.donor.DuplicateDonorBackup;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.jembi.bsis.viewmodel.DonorSummaryViewModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;

public class DonorRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  DonorRepository donorRepository;
  private User user;
  private Long userDbId = 1l;
  private DonorBackingForm donorBackingForm;
  Date donorBirthdate = null;
  ApplicationContext applicationContext = null;
  UserDetailsService userDetailsService;
  private Donor donor;

  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/DonorRepositoryDataset.xml");
    IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
    Date today = new Date();
    Map<String, Object> replacements = new HashMap<String, Object>();
    replacements.put("DateDonorNotDue", DateUtils.addDays(today,
        -(DonationConstants.BLOCK_BETWEEN_DONATIONS - 1)));
    replacements.put("DateDonorDue", DateUtils.addDays(today,
        -(DonationConstants.BLOCK_BETWEEN_DONATIONS + 1)));

    replacements.put("DateDeferredOn", DateUtils.addDays(today, -(2)));
    replacements.put("DateDeferredUnit", DateUtils.addDays(today, (2)));

    ReplacementDataSet rDataSet = new ReplacementDataSet(dataSet);
    for (String key : replacements.keySet()) {
      rDataSet.addReplacementObject("${" + key + "}",
          replacements.get(key));
    }
    return rDataSet;
  }

  @Before
  public void createDonorForm() {
    donor = new Donor();
    donorBackingForm = new DonorBackingForm(donor);
  }

  @Test
  /**
   * Should fail when donor is missing required fields. saveDonor(Donor)
   */
  public void saveDonor_shouldPersist() throws ParseException {
    donorBackingForm = new DonorBackingForm();
    setBackingFormValue(donorBackingForm);
    // New DonorNumber(000005) which is assigned to Donor Persist Object
    donorBackingForm.setDonorNumber("000005");
    donorRepository.saveDonor(donorBackingForm.getDonor());
    assertTrue(
        "Donor's Id should not zero. Once Donor object should persist,new Id is generated and assigned to Donor.",
        donorBackingForm.getDonor().getId() == 0 ? false : true);
  }

  @Test
  /**
   * Should return donor with given id findDonorById(Long)
   */
  public void findDonorById_shouldReturnDonor() {
    // 1 is Donor's ID.
    Donor findDonor = donorRepository.findDonorById(1l);
    assertNotNull(
        "Donor Object should not null.Donor Id is exist into Donor's table.",
        findDonor);
    assertTrue("Donor's id value should be 1.",
        findDonor.getId() == 1 ? true : false);
  }


  /**
   * Expects NoResultException to be thrown  when Donor id does not exist findDonorById(Long)
   */
  @Test(expected = NoResultException.class)
  public void findDonorById_shouldExpectNoResultException() {
    // 18 ID is not exist into Donor table.
    donorRepository.findDonorById(18l);
  }


  /**
   * Expects NoResultException to be thrown when Donor has been deleted findDonorById(Long)
   */
  @Test(expected = NoResultException.class)
  public void findDonorById_shouldExpectNoResultExceptionWhenDonorIsDeleted() {
    // 2 is Deleted Donor's ID.
    donorRepository.findDonorById(2l);
  }

  @Test
  /**
   * Should return empty list (instead of a null object) when no match is
   * found.
   * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String,
   * Object>, Boolean)
   *
   */
  public void findAnyDonor_listSizeShouldZero() {
    String donorFirstName = "xxx";
    String donorLastName = "";

    assertEquals("List size should be zero, no matching search results.",
        0, donorRepository.findAnyDonor(donorFirstName, donorLastName, true).size());
  }

  @Test
  /**
   * Should fetch all donors that partially match first name
   * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String,
   * Object>, Boolean)
   */
  public void findAnyDonor_listSizeShouldNotZeroPartialFirstNameMatch() {
    String donorFirstName = "fir";
    String donorLastName = "";

    List<Donor> listDonors = donorRepository.findAnyDonor(donorFirstName, donorLastName, true);

    assertNotSame(
        "List size should not zero.Matching records is found base on firstname.",
        0, listDonors.size());
    boolean isValid = false;
    for (Donor donor : listDonors) {
      if (donor.getFirstName().startsWith("fir")) {
        isValid = true;
      } else {
        isValid = false;
        break;
      }
    }
    assertTrue("Donor's First Name should be start with 'fir'.", isValid);

  }

  @Test
  /**
   * Should fetch all donors that fully match first name
   * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String,
   * Object>, Boolean)
   */
  public void findAnyDonor_listSizeShouldNotZeroFullyFirstNameMatch() {
    String donorFirstName = "firstName";
    String donorLastName = "";

    List<Donor> listDonors = donorRepository.findAnyDonor(donorFirstName, donorLastName, false);

    assertNotSame(
        "List size should not zero.Matching records is found base on firstname.",
        0, listDonors.size());
    boolean isValid = false;
    for (Donor donor : listDonors) {
      if (donor.getFirstName().equals(donorFirstName)) {
        isValid = true;
      } else {
        isValid = false;
        break;
      }
    }
    assertTrue("Found Donor's with ," + donorFirstName, isValid);

  }

  @Test
  /**
   * Should fetch all donors that partially match last name
   * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String,
   * Object>, Boolean)
   */
  public void findAnyDonor_listSizeShouldNotZeroPartialLastNameMatch() {
    String donorFirstName = "";
    String donorLastName = "las";

    List<Donor> listDonors = donorRepository.findAnyDonor(donorFirstName, donorLastName, true);

    assertNotSame(
        "List size should not zero.Matching records is found base on lastname.",
        0, listDonors.size());
    boolean isValid = false;
    for (Donor donor : listDonors) {
      if (donor.getLastName().startsWith("las")) {
        isValid = true;
      } else {
        isValid = false;
      }
    }
    assertTrue("Donor's Last Name should be start with 'las'.", isValid);
  }

  @Test
  /**
   * Should not return donors who have been deleted.
   * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String,
   * Object>, Boolean)
   */
  public void findAnyDonor_deleteObjectShouldNotPartOfList() {
    String donorFirstName = "fir";
    String donorLastName = "";

    List<Donor> listDonor = donorRepository.findAnyDonor(donorFirstName, donorLastName, true);

    for (Donor donor : listDonor) {
      // 2 is deleted donor id
      assertFalse(
          "Donor's id 2 is deleted from database. so Deleted Donor should not included in the list.",
          donor.getId() == 2 ? true : false);
    }
  }

  @Test
  /**
   * Should create new Donor from existing Donor object addDonor(Donor)
   */
  public void addDonor_shouldPersist() {
    // 1 is Donor's Id.
    Donor createNewDonorFromExistDonor = donorRepository.findDonorById(1l);
    Donor newDonor = this.copyDonor(createNewDonorFromExistDonor);
    // New DonorNumber(000006) which is assigned to Donor Persist Object.
    newDonor.setDonorNumber("000006");
    donorRepository.addDonor(newDonor);
    assertTrue(
        "Donor's Id should not zero. Once Donor should persist,new Id is generated and assigned to Donor.",
        newDonor.getId() == 0 ? false : true);
  }

  @Test
  /**
   * Should update existing Donor with Donor object updateDonor(Donor)
   */
  public void updateDonor_shouldReturnNotNull() {
    // 1 is Donor Id.
    Donor editDonor = donorRepository.findDonorById(1l);
    donorBackingForm = new DonorBackingForm(editDonor);
    setBackingUpdateFormValue(donorBackingForm);
    assertNotNull("Donor Object should update.",
        donorRepository.updateDonorDetails(donorBackingForm.getDonor()));

  }


  /**
   * Expects NoResultException to be thrown  when Donor Number does not match an existing Donor
   * updateDonor(Donor)
   */
  @Test(expected = NoResultException.class)
  public void updateDonor_shouldExpectNoResultException() {
    Donor editDonor = new Donor();
    editDonor.setId(-1l);
    donorBackingForm = new DonorBackingForm(editDonor);
    setBackingUpdateFormValue(donorBackingForm);
    donorRepository.updateDonorDetails(donorBackingForm.getDonor());
  }

  @Test
  /**
   * Should create new Donors from existing Donor objects
   * addAllDonors(List<Donor>)
   */
  public void addAllDonors_listDonorsShouldPersist() throws ParseException {
    Donor donor = new Donor();
    DonorBackingForm donorBackingForm = new DonorBackingForm(donor);
    donorBackingForm.setDonorNumber("000007");
    setBackingFormValue(donorBackingForm);
    List<Donor> listAllDonor = new ArrayList<Donor>();
    listAllDonor.add(donorBackingForm.getDonor());
    donor = new Donor();
    donorBackingForm = new DonorBackingForm(donor);
    donorBirthdate = CustomDateFormatter.getDateFromString("1991-06-11");
    donorBackingForm.setBirthDate(donorBirthdate);
    donorBackingForm.setDonorNumber("000008");
    setBackingFormValue(donorBackingForm);
    listAllDonor.add(donorBackingForm.getDonor());
    donor = new Donor();
    donorBackingForm = new DonorBackingForm(donor);
    donorBirthdate = CustomDateFormatter.getDateFromString("1991-06-12");
    donorBackingForm.setBirthDate(donorBirthdate);
    donorBackingForm.setDonorNumber("000009");
    setBackingFormValue(donorBackingForm);
    listAllDonor.add(donorBackingForm.getDonor());
    for (Donor donorObject : listAllDonor) {
      donorRepository.addDonor(donorObject);
    }
    assertTrue("List All Donor should persist.", true);

  }

  @Test
  /**
   * Should return donor with given Donor Number
   * findDonorByDonorNumber(String,boolean)
   */
  public void findDonorByDonorNumber_donorObjectShouldNotNullDonorDeleteFalse() {
    // 000001 DonorNumber
    Donor findDonor = donorRepository.findDonorByDonorNumber("000001",
        false);
    assertNotNull(
        "Donor object should not null.Because DonorNumber 000001 is exist into donor table",
        findDonor);
    assertTrue("Donor's Donor Number should be '000001'.", findDonor
        .getDonorNumber().equals("000001"));
  }

  /**
   * Expects null when donor not exists findDonorByDonorNumber(String,boolean)
   */
  @Test
  public void findDonorByDonorNumber_donorObjectShouldNullDonorDeleteFalse() {
    // 000009 DonorNumber is not exist.
    assertNull("Donor with donor number 000014 should return null",
        donorRepository.findDonorByDonorNumber("000014", false));
  }


  /**
   * Expects null  when donor with given donor number doesn't exist findDonorByDonorNumber(String,boolean)
   */
  @Test
  public void findDonorByDonorNumber_donorObjectShouldNullDonorDeleteTrue() {
    //000001 donor number
    assertNull("Donor with donor number 000001 should return null",
        donorRepository.findDonorByDonorNumber("000001", true));
  }

  @Test
  /**
   * Should return deleted donor because IsDelete true
   * findDonorByDonorNumber(String,boolean)
   */
  public void findDonorByDonorNumber_donorObjectShouldNotNullDonorDeleteTrue() {
    // 000002 Deleted DonorNumber
    Donor deletedDonor = donorRepository.findDonorByDonorNumber("000002",
        true);
    assertNotNull(
        "Donor's object should not null. Because matching donor is deleted and method argument is isDeleted=true",
        deletedDonor);
    assertTrue("Donor's donor number should be '000002'.", deletedDonor
        .getDonorNumber().equals("000002"));
  }

  @Test
  /**
   * Should return all non-deleted deferral reasons in the database
   * getDeferralReasons()
   */
  public void getDeferralReasons_shouldReturnNonDeletedRecord() {
    List<DeferralReason> list = donorRepository.getDeferralReasons();
    if (list != null && list.size() > 0) {
      for (DeferralReason deferralReason : list) {
        assertFalse(
            "Soft Deleted DeferralReason object should not include in list.",
            deferralReason.getIsDeleted());
      }
    }
  }

  @Test
  /**
   * Should not return deferral reasons that have been deleted
   * getDeferralReasons()"
   */
  public void getDeferralReasons_shouldNotReturnDeferalReason() {
    List<DeferralReason> list = donorRepository.getDeferralReasons();
    if (list != null && list.size() > 0) {
      for (DeferralReason deferralReason : list) {
        // 7 ID is deleted from DeferralReason.
        assertNotSame(
            "Soft Deleted DeferralReason object should not include in list.",
            7, deferralReason.getId());
      }
    }
  }

  @Test
  /**
   * DonorDeferral list size should be zero getDonorDeferrals(Long)
   */
  public void getDonorDeferrals_listSizeShouldZero() {
    // 2 is Donor ID
    assertEquals(
        "Deferral Donor is not found base on donor id so,List size should be zero.",
        0, donorRepository.getDonorDeferrals(2l).size());

  }

  @Test
  /**
   * DonorDeferral list size should not zero getDonorDeferrals(Long)
   */
  public void getDonorDeferrals_listSizeShouldNotZero() {
    // 1 is Donor ID
    List<DonorDeferral> listDonorDeferral = donorRepository
        .getDonorDeferrals(1l);
    assertNotSame(
        "Deferral Donor is found base on donor id so,List size should not zero.",
        0, listDonorDeferral.size());
    for (DonorDeferral donorDeferral : listDonorDeferral) {
      assertTrue("DonorDeferral's Donor Id should be 1.", donorDeferral
          .getDeferredDonor().getId() == 1 ? true : false);
    }

  }

  @Test
  /**
   * Should return last donor derferral date getLastDonorDeferralDate(long)
   *
   */
  public void getLastDonorDeferralDate_shouldReturnlastDeferredUntil() {
    // 4 is Donor ID
    Date currentDate = DateUtils.addDays(new Date(), (2));
    Date date = donorRepository.getLastDonorDeferralDate(6l);
    assertNotNull("should return last donor derferral date", date);
    String str = dateFormat.format(date);
    String str_currentDate = dateFormat.format(currentDate);
    assertTrue("Latest deferal date should be '" + str_currentDate + "'.",
        str.equals(str_currentDate));
  }

  @Test
  /**
   * Should return only donor deferral when donor has one deferral DonorDeferral getLastDonorDeferral(Long donorId)
   */

  public void getLastDonorDeferral_shouldReturnOnlyDonorDeferral() {
    Date latestDeferralDate = DateUtils.addDays(new Date(), (2));
    DonorDeferral lastDonorDeferral = donorRepository.getLastDonorDeferral(1l);
    String str = dateFormat.format(lastDonorDeferral.getDeferredUntil());
    String str_latestDeferralDate = dateFormat.format(latestDeferralDate);
    assertTrue("Should equal latest deferral Date: " + str_latestDeferralDate, str.equals(str_latestDeferralDate));
  }

  @Test
  /**
   * Should return last donor deferral DonorDeferral getLastDonorDeferral(Long donorId)
   */

  public void getLastDonorDeferral_shouldReturnLastDonorDeferral() {
    Date latestDeferralDate = DateUtils.addDays(new Date(), (2));
    DonorDeferral lastDonorDeferral = donorRepository.getLastDonorDeferral(6l);
    String str = dateFormat.format(lastDonorDeferral.getDeferredUntil());
    String str_latestDeferralDate = dateFormat.format(latestDeferralDate);
    assertTrue("Should equal latest deferral Date: " + str_latestDeferralDate, str.equals(str_latestDeferralDate));
  }

  @Test
  /**
   * Should return null when donor has no deferral DonorDeferral getLastDonorDeferral(Long donorId)
   */

  public void getLastDonorDeferral_shouldReturnNull() {
    DonorDeferral lastDonorDeferral = donorRepository.getLastDonorDeferral(3l);
    assertNull("should return null when donor has no deferral", lastDonorDeferral);
  }

  @Test
  /**
   * Should return Non EMpty Language List
   */
  public void getAllLanguages_shuouldReturnNonEmptyList() {
    assertTrue("Languages List  should not be empty", !donorRepository.getAllLanguages().isEmpty());
  }

  @Test
  /**
   * Should return Non EMpty ID Types List
   */
  public void getAllIdTypes_shuouldReturnNonEmptyList() {
    assertTrue("IDTypes List  should not be empty", !donorRepository.getAllIdTypes().isEmpty());
  }

  /**
   * Called when insert new record into table.
   * 
   * @throws ParseException
   */
  public void setBackingFormValue(DonorBackingForm donorBackingForm) throws ParseException {
    Location l = new Location();
    l.setId(Long.parseLong("1"));
    AddressType a = new AddressType();
    a.setId(Long.parseLong("1"));
    donorBirthdate = CustomDateFormatter.getDateFromString("1991-06-11");
    donorBackingForm.setAddress(new Address());
    donorBackingForm.setContact(new Contact());
    donorBackingForm.setHomeAddressLine1("myaddress");
    donorBackingForm.setFirstName("firstname");
    donorBackingForm.setMiddleName("middlename");
    donorBackingForm.setLastName("lastname");
    donorBackingForm.setIsDeleted(false);
    donorBackingForm.setGender(Gender.male);
    donorBackingForm.setCallingName("CallingName");
    donorBackingForm.setHomeAddressCity("City");
    donorBackingForm.setHomeAddressCountry("country");
    donorBackingForm.setHomeAddressLine1("homeAddressLine1");
    user = new User();
    user.setId(userDbId);
    donorBackingForm.setHomeAddressDistrict("District");
    donorBackingForm.setVenue(new LocationBackingForm(l));
    donorBackingForm.setIdNumber("1111");
    donorBackingForm.setNotes("Notes");
    donorBackingForm.setMobileNumber("9999999999");
    donorBackingForm.setWorkNumber("8888888888");
    donorBackingForm.setPreferredAddressType(a);
    donorBackingForm.setHomeAddressProvince("Province");
    donorBackingForm.setHomeAddressState("State");
    donorBackingForm.setHomeAddressZipcode("361001");
    donorBackingForm.setBirthDate(donorBirthdate);
    donorBackingForm.setBirthDateEstimated(true);

  }

  /**
   * Called when update existing record.
   */
  public void setBackingUpdateFormValue(DonorBackingForm donorBackingForm) {

    if (donorBackingForm.getAddress() != null) {
      donorBackingForm.setAddress(donorBackingForm.getAddress());
    } else {
      donorBackingForm.setAddress(new Address());
    }

    if (donorBackingForm.getContact() != null) {
      donorBackingForm.setContact(donorBackingForm.getContact());
    } else {
      donorBackingForm.setContact(new Contact());
    }

    Location l = new Location();
    l.setId(Long.parseLong("2"));
    AddressType a = new AddressType();
    a.setId(Long.parseLong("2"));
    donorBackingForm.setHomeAddressLine1("address_update");
    donorBackingForm.setFirstName("firstName_update");
    donorBackingForm.setMiddleName("middlename_update");
    donorBackingForm.setLastName("lastname_update");
    donorBackingForm.setIsDeleted(false);
    donorBackingForm.setGender(Gender.female);
    donorBackingForm.setCallingName("CallingName_update");
    donorBackingForm.setHomeAddressCity("City_update");
    donorBackingForm.setHomeAddressCountry("country_update");
    donorBackingForm.setHomeAddressDistrict("District_update");
    donorBackingForm.setVenue(new LocationBackingForm(l));
    donorBackingForm.setIdNumber("1212");
    donorBackingForm.setNotes("Notes_update");
    donorBackingForm.setMobileNumber("9878787878");
    donorBackingForm.setWorkNumber("874525452");
    donorBackingForm.setPreferredAddressType(a);
    donorBackingForm.setHomeAddressProvince("Province_update");
    donorBackingForm.setHomeAddressState("State_update");
    donorBackingForm.setHomeAddressZipcode("361001");
  }

  public Donor copyDonor(Donor donor) {
    Donor copyDonor = new Donor();
    Address address = new Address();
    Contact contact = new Contact();

    address.setHomeAddressLine1(donor.getAddress().getHomeAddressLine1());
    address.setHomeAddressCity(donor.getAddress().getHomeAddressCity());
    address.setHomeAddressCountry(donor.getAddress().getHomeAddressCountry());
    address.setHomeAddressDistrict(donor.getAddress().getHomeAddressDistrict());
    address.setHomeAddressState(donor.getAddress().getHomeAddressState());
    address.setHomeAddressProvince(donor.getAddress().getHomeAddressProvince());
    address.setHomeAddressZipcode(donor.getAddress().getHomeAddressZipcode());

    contact.setMobileNumber(donor.getContact().getMobileNumber());
    contact.setHomeNumber(donor.getContact().getHomeNumber());

    copyDonor.setAddress(address);
    copyDonor.setContact(contact);

    copyDonor.setAge(donor.getAge());
    copyDonor.setBirthDate(donor.getBirthDate());
    copyDonor.setBirthDateEstimated(donor.getBirthDateEstimated());
    copyDonor.setBirthDateInferred(donor.getBirthDateInferred());
    copyDonor.setBloodAbo(donor.getBloodAbo());
    copyDonor.setCallingName(donor.getCallingName());
    copyDonor.setDateOfLastDonation(donor.getDateOfLastDonation());
    copyDonor.setDonorHash(donor.getDonorHash());
    copyDonor.setVenue(donor.getVenue());
    copyDonor.setDonorStatus(donor.getDonorStatus());
    copyDonor.setFirstName(donor.getFirstName());
    copyDonor.setGender(donor.getGender());
    copyDonor.setLastName(donor.getLastName());
    copyDonor.setMiddleName(donor.getMiddleName());
    copyDonor.setIdNumber(donor.getIdNumber());
    copyDonor.setNotes(donor.getNotes());
    return copyDonor;
  }

  @Test
  /**
   * Test passes on retrieving address of donor
   */
  public void getDonorAddress_ShouldReturnNotNull() {
    assertNotNull("Expected : Address Type  but Found : NULL",
        donorRepository.findDonorById(1l).getAddress());
  }

  @Test
  /**
   * Test passes on retrieving Contact of donor
   */
  public void getDonorContact_ShouldReturnNotNull() {
    assertNotNull("Expected : Contact Type but Found : NULL",
        donorRepository.findDonorById(1l).getContact());
  }

//    @Test
//    /**
//     * Passes if Non Empty Contact List returned
//     */
//    public void getDonorIdNumbers_ShouldReturnNotEmptyList() {
//        assertTrue("Expected : ID Number Set but Found : Empty Set",
//                donorRepository.findDonorById(1l).getIdNumbers().size() > 0);
//    }

  @Test
  /**
   *Test Passes if non Empty Language List returned
   */
  public void getAllLanguages_ShouldReturnNonEmptyList() {
    assertTrue("Expected : Languages Set  but FOund : Empty Set",
        donorRepository.getAllLanguages().size() > 0);
  }

  @Test
  /**
   *Test Passes if non Empty Language List returned
   */
  public void getAllIdTypes_ShouldReturnNonEmptyList() {
    assertTrue("Expected : ID Type Set  but FOund : Empty Set",
        donorRepository.getAllIdTypes().size() > 0);
  }

  @Test
  public void testFindDonorsByNumbersEmptyList() throws Exception {
    List<Donor> donors = donorRepository.findDonorsByNumbers(new ArrayList<String>());
    Assert.assertNotNull("Not null list was returned", donors);
    Assert.assertEquals("No Donors were returned", 0, donors.size());
  }

  @Test
  public void testFindDonorsByNumbersNullList() throws Exception {
    List<Donor> donors = donorRepository.findDonorsByNumbers(null);
    Assert.assertNotNull("Not null list was returned", donors);
    Assert.assertEquals("No Donors were returned", 0, donors.size());
  }

  @Test
  public void testFindDonorsByNumbersDoesntExist() throws Exception {
    List<String> donorNumbers = new ArrayList<String>();
    donorNumbers.add("xxxxxx");
    List<Donor> donors = donorRepository.findDonorsByNumbers(donorNumbers);
    Assert.assertNotNull("Not null list was returned", donors);
    Assert.assertEquals("No Donors were returned", 0, donors.size());
  }

  @Test
  public void testFindDonorsByNumbers() throws Exception {
    List<String> donorNumbers = new ArrayList<String>();
    donorNumbers.add("000001");
    donorNumbers.add("000003");
    List<Donor> donors = donorRepository.findDonorsByNumbers(donorNumbers);
    Assert.assertNotNull("Not null list was returned", donors);
    Assert.assertEquals("Two Donors were returned", 2, donors.size());
    Assert.assertEquals("donorNumber matches", "000001", donors.get(0).getDonorNumber());
    Assert.assertEquals("donorNumber matches", "000003", donors.get(1).getDonorNumber());
  }

  @Test
  public void addMergedDonor() throws Exception {
    List<DuplicateDonorBackup> backupLogs = new ArrayList<DuplicateDonorBackup>();
    backupLogs.add(new DuplicateDonorBackup("1234567", "000001", 1l, null));
    backupLogs.add(new DuplicateDonorBackup("1234567", "000001", null, 1l));
    Donor oldDonor = donorRepository.findDonorByDonorNumber("000001", false);
    List<Donor> donorsToMerge = new ArrayList<Donor>();
    donorsToMerge.add(oldDonor);
    Donor newDonor = new Donor();
    newDonor.setDonorNumber("1234567");
    newDonor.setFirstName("Merged");
    newDonor.setLastName("Donor");
    newDonor.setDonations(oldDonor.getDonations());
    oldDonor.setDonations(null);
    newDonor.setDeferrals(oldDonor.getDeferrals());
    oldDonor.setDeferrals(null);
    Donor savedDonor = donorRepository.addMergedDonor(newDonor, donorsToMerge, backupLogs);
    Assert.assertNotNull("Id has been set", savedDonor.getId());
    Assert.assertNotNull("Donations have been moved", savedDonor.getDonations());
    Assert.assertEquals("Donations have been moved", 1, savedDonor.getDonations().size());
    Assert.assertNotNull("Deferrals have been moved", savedDonor.getDeferrals());
    Assert.assertEquals("Deferrals have been moved", 3, savedDonor.getDeferrals().size());
    oldDonor = donorRepository.findDonorByDonorNumber("000001", false); // will return all statues
    Assert.assertNull("Donations have been moved", oldDonor.getDonations());
    Assert.assertNull("Deferrals have been moved", oldDonor.getDeferrals());
  }

  @Test
  public void testFindDonorByNumberExcludeMerged() throws Exception {
    Donor donor = donorRepository.findDonorByDonorNumber("000001", false);
    donor.setDonorStatus(DonorStatus.MERGED);
    donorRepository.saveDonor(donor);
    donor = donorRepository.findDonorByDonorNumber("000001", false, new DonorStatus[]{DonorStatus.MERGED});
    Assert.assertNull("Donor was not found", donor);
  }

  @Test
  public void testFindDonorSummaryByDonorNumber() throws Exception {
    DonorSummaryViewModel donorSummary = donorRepository.findDonorSummaryByDonorNumber("000001");
    Assert.assertNotNull("Donor was found", donorSummary);
    Assert.assertEquals("Donor was found", "firstName", donorSummary.getFirstName());

  }

  @Test(expected = NoResultException.class)
  public void testFindDonorSummaryByDonorNumberMerged() throws Exception {
    // Set up
    Donor donor = donorRepository.findDonorByDonorNumber("000001", false);
    donor.setDonorStatus(DonorStatus.MERGED);
    donorRepository.saveDonor(donor);
    // Test
    donorRepository.findDonorSummaryByDonorNumber("000001");
  }

  @Test
  public void testGetAllDuplicateDonors() throws Exception {
    List<DuplicateDonorDTO> duplicateDonors = donorRepository.getDuplicateDonors();
    Assert.assertEquals("There are 5 duplicates", duplicateDonors.get(0).getCount(), 5);
  }

  @Test
  public void testGetDuplicateDonors() throws Exception {
    String firstName = "firstName";
    String lastName = "lastName";
    Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse("1991-06-10");
    Gender gender = Gender.male;
    List<Donor> duplicateDonors = donorRepository.getDuplicateDonors(firstName, lastName, birthDate, gender);
    Assert.assertEquals("There are 5 duplicates", duplicateDonors.size(), 5);
    for (Donor donor : duplicateDonors) {
      Assert.assertEquals("First name correct", firstName, donor.getFirstName());
      Assert.assertEquals("Last name correct", lastName, donor.getLastName());
      Assert.assertEquals("birth date correct", birthDate, donor.getBirthDate());
      Assert.assertEquals("gender correct", gender, donor.getGender());
    }
  }

}
