package org.jembi.bsis.service.export;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.output.NullOutputStream;
import org.jembi.bsis.dto.BloodTestResultExportDTO;
import org.jembi.bsis.dto.ComponentExportDTO;
import org.jembi.bsis.dto.DeferralExportDTO;
import org.jembi.bsis.dto.DonationExportDTO;
import org.jembi.bsis.dto.DonorExportDTO;
import org.jembi.bsis.dto.PostDonationCounsellingExportDTO;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.HaemoglobinLevel;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.BloodTestResultRepository;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.service.DateGeneratorService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DataExportServiceTests extends UnitTestSuite {
  
  /**
   * The expected MD5 sum of the output zip. The contents of the zip was validated manually to
   * ensure that it is correct. If changes are made to the test then the output should be validated
   * again and this value should be updated to be the MD5 sum of the updated zip.
   */
  private static final String EXPECTED_DIGEST = "7ec990d981eaab1bacdf9050ab0dd33f";
  
  @InjectMocks
  private DataExportService dataExportService;
  @Mock
  private DateGeneratorService dateGeneratorService;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  @Mock
  private DonorDeferralRepository deferralRepository;
  @Mock
  private BloodTestResultRepository bloodTestResultRepository;
  @Mock
  private ComponentRepository componentRepository;
  
  @BeforeClass
  public static void setTimeZone() {
    TimeZone.setDefault(TimeZone.getTimeZone("Africa/Johannesburg"));
  }
  
  @AfterClass
  public static void unsetTimeZone() {
    TimeZone.setDefault(null);
  }
  
  @Test
  public void testExportData_shouldGenerateCorrectOutput() throws NoSuchAlgorithmException, IOException {
    // Set up fixture
    MessageDigest digest = MessageDigest.getInstance("MD5");
    
    DonorExportDTO donor = new DonorExportDTO();
    donor.setDonorNumber("0000337");
    donor.setCreatedDate(new Date(1472900253711L));
    donor.setCreatedBy("created_by_user");;
    donor.setLastUpdated(new Date(1473505090988L));
    donor.setLastUpdatedBy("updated_by_user");
    donor.setTitle("Mr");
    donor.setFirstName("John");
    donor.setMiddleName("B");
    donor.setLastName("Smith");
    donor.setCallingName("Johnny");
    donor.setGender(Gender.male);
    donor.setBirthDate(new Date(400768342090L));
    donor.setPreferredLanguage("English");
    donor.setVenue("Earth");
    donor.setBloodABO("AB");
    donor.setBloodRh("-");
    donor.setNotes("Great guy");
    donor.setIdType("National");
    donor.setIdNumber("8000000000001");
    donor.setDateOfFirstDonation(new Date(1472900253711L));
    donor.setDateOfLastDonation(new Date(1473505090988L));
    donor.setDueToDonate(new Date(1478084315818L));
    donor.setContactMethodType("Mobile");
    donor.setMobileNumber("1234567890");
    donor.setHomeNumber("2345678901");
    donor.setWorkNumber("3456789012");
    donor.setEmail("john.b.smith@example.com");
    donor.setPreferredAddressType("Home");
    donor.setHomeAddressLine1("Unit 3");
    donor.setHomeAddressLine2("1 Main Road");
    donor.setHomeAddressCity("Osgiliath");
    donor.setHomeAddressProvince("Gondor");
    donor.setHomeAddressDistrict("Anduin");
    donor.setHomeAddressState("South-kingdom");
    donor.setHomeAddressCountry("Middle Earth");
    donor.setHomeAddressZipcode("1234");
    donor.setWorkAddressLine1("Unit 3");
    donor.setWorkAddressLine2("1 Main Road");
    donor.setWorkAddressCity("Osgiliath");
    donor.setWorkAddressProvince("Gondor");
    donor.setWorkAddressDistrict("Anduin");
    donor.setWorkAddressState("South-kingdom");
    donor.setWorkAddressCountry("Middle Earth");
    donor.setWorkAddressZipcode("1234");
    donor.setPostalAddressLine1("Unit 3");
    donor.setPostalAddressLine2("1 Main Road");
    donor.setPostalAddressCity("Osgiliath");
    donor.setPostalAddressProvince("Gondor");
    donor.setPostalAddressDistrict("Anduin");
    donor.setPostalAddressState("South-kingdom");
    donor.setPostalAddressCountry("Middle Earth");
    donor.setPostalAddressZipcode("1234");
    
    DonationExportDTO donation = new DonationExportDTO();
    donation.setDonorNumber("000037");
    donation.setDonationIdentificationNumber("0000001");
    donation.setCreatedDate(new Date(1472900253711L));
    donation.setCreatedBy("created_by_user");
    donation.setLastUpdated(new Date(1472900253711L));
    donation.setLastUpdatedBy("updated_by_user");
    donation.setPackType("Single");
    donation.setDonationDate(new Date(1472900253711L));
    donation.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    donation.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    donation.setTtiStatus(TTIStatus.SAFE);
    donation.setBleedStartTime(new Date(1472900253711L));
    donation.setBleedEndTime(new Date(1472900914728L));
    donation.setDonorWeight(new BigDecimal(90));
    donation.setBloodPressureSystolic(120);
    donation.setBloodPressureDiastolic(80);
    donation.setDonorPulse(60);
    donation.setHaemoglobinCount(new BigDecimal(12));
    donation.setHaemoglobinLevel(HaemoglobinLevel.PASS);
    donation.setAdverseEventType("Fainting");
    donation.setAdverseEventComment("Passed out");
    donation.setBloodAbo("AB");
    donation.setBloodRh("-");
    donation.setReleased(true);
    donation.setIneligbleDonor(false);
    donation.setNotes("Minor adverse event");
    
    PostDonationCounsellingExportDTO postDonationCounselling = new PostDonationCounsellingExportDTO();
    postDonationCounselling.setDonationIdentificationNumber("0000001");
    postDonationCounselling.setCreatedDate(new Date(1472901294359L));
    postDonationCounselling.setCreatedBy("created_by_user");
    postDonationCounselling.setLastUpdated(new Date(1472901294360L));
    postDonationCounselling.setLastUpdatedBy("updated_by_user");
    postDonationCounselling.setCounsellingDate(new Date(1473506122982L));
    
    DeferralExportDTO deferral = new DeferralExportDTO();
    deferral.setDonorNumber("000037");
    deferral.setCreatedDate(new Date(1472901294359L));
    deferral.setCreatedBy("created_by_user");
    deferral.setLastUpdated(new Date(1472901294360L));
    deferral.setLastUpdatedBy("updated_by_user");
    deferral.setDeferralReasonText("Low weight");
    deferral.setDeferralReason("Test Outcomes");
    deferral.setDeferralDate(new Date(1473506122982L));
    deferral.setDeferredUntil(new Date(1478085703109L));
    
    BloodTestResultExportDTO bloodTestResult = new BloodTestResultExportDTO();
    bloodTestResult.setDonationIdentificationNumber("0000001");
    bloodTestResult.setCreatedDate(new Date(1472900253711L));
    bloodTestResult.setCreatedBy("created_by_user");
    bloodTestResult.setLastUpdated(new Date(1472900253711L));
    bloodTestResult.setLastUpdatedBy("updated_by_user");;
    bloodTestResult.setTestName("HIV");
    bloodTestResult.setResult("NEG");
    
    ComponentExportDTO component = new ComponentExportDTO();
    component.setDonationIdentificationNumber("0000001");
    component.setComponentCode("0011");
    component.setCreatedDate(new Date(1472900253711L));
    component.setCreatedBy("created_by_user");
    component.setLastUpdated(new Date(1472900253711L));
    component.setLastUpdatedBy("updated_by_user");
    component.setParentComponentCode("0012");
    component.setCreatedOn(new Date(1472900253711L));
    component.setStatus(ComponentStatus.DISCARDED);
    component.setLocation("Osgiliath");
    component.setIssuedOn(new Date(1473507104562L));
    component.setInventoryStatus(InventoryStatus.REMOVED);
    component.setDiscardedOn(new Date(1473679924238L));
    component.setDiscardReason("Split");
    component.setExpiresOn(new Date(1478086336850L));
    component.setNotes("Component was dropped");
    
    // Set up expectations
    when(dateGeneratorService.generateDate()).thenReturn(new Date(1473761369898L));
    when(donorRepository.findDonorsForExport()).thenReturn(Arrays.asList(donor));
    when(donationRepository.findDonationsForExport()).thenReturn(Arrays.asList(donation));
    when(postDonationCounsellingRepository.findPostDonationCounsellingsForExport()).thenReturn(Arrays.asList(postDonationCounselling));
    when(deferralRepository.findDeferralsForExport()).thenReturn(Arrays.asList(deferral));
    when(bloodTestResultRepository.findBloodTestResultsForExport()).thenReturn(Arrays.asList(bloodTestResult));
    when(componentRepository.findComponentsForExport()).thenReturn(new LinkedHashSet<>(Arrays.asList(component)));
    
    // Exercise SUT
    NullOutputStream outputStream = new NullOutputStream();
    DigestOutputStream digestOutputStream = new DigestOutputStream(outputStream, digest);
    dataExportService.exportData(digestOutputStream);
    
    // Verify
    String outputDigest = Hex.encodeHexString(digest.digest());
    assertThat(outputDigest, is(EXPECTED_DIGEST));
  }

}
