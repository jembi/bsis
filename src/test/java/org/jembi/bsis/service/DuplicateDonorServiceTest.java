package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.dto.DuplicateDonorDTO;
import org.jembi.bsis.helpers.builders.BloodTestingRuleResultBuilder;
import org.jembi.bsis.helpers.builders.DeferralReasonBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.DonorBuilder;
import org.jembi.bsis.helpers.builders.DonorDeferralBuilder;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.donor.DuplicateDonorBackup;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DeferralReasonType;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.donordeferral.DurationType;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.SequenceNumberRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import scala.actors.threadpool.Arrays;

public class DuplicateDonorServiceTest extends UnitTestSuite {

  private static final UUID DONOR_DEFERRAL_ID = UUID.randomUUID();
  private static final UUID DONATION_ID_1 = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a1");
  private static final UUID DONATION_ID_2 = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a2");
  private static final UUID DONATION_ID_3 = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a3");
  private static final UUID DONATION_ID_4 = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a4");

  @InjectMocks
  private DuplicateDonorService service;

  @Mock
  private DonorRepository donorRepository;

  @Mock
  private SequenceNumberRepository sequenceNumberRepository;

  @Mock
  private BloodTestsService bloodTestsService;

  @Mock
  private DonorService donorService;

  @Test
  public void testFindDuplicateDonors() {
    when(donorRepository.getDuplicateDonors()).thenReturn(new ArrayList<DuplicateDonorDTO>());
    List<DuplicateDonorDTO> duplicateDonors = service.findDuplicateDonors();
    Assert.assertTrue("No matching donors", duplicateDonors.isEmpty());
  }
  
  @Test
  public void testFindDuplicateDonorsForDonor() throws Exception {
    Donor donor = DonorBuilder.aDonor().withFirstName("Sue").withLastName("Smith").withBirthDate("1999-10-10")
        .withGender(Gender.female).withDonorStatus(DonorStatus.NORMAL).thatIsNotDeleted().build();
    when(donorRepository.getDuplicateDonors()).thenReturn(new ArrayList<DuplicateDonorDTO>());
    List<Donor> duplicateDonors = service.findDuplicateDonors(donor);
    Assert.assertTrue("No matching donors", duplicateDonors.isEmpty());
  }

  @Test
  public void testMergeDuplicateDonorsNull() {
    List<DuplicateDonorBackup> backupLogs = service.mergeDonors(new Donor(), (List<Donor>) null);
    Assert.assertNotNull("backupLogs returned", backupLogs);
    Assert.assertEquals("No backups necessary", 0, backupLogs.size());
  }

  @Test
  public void testMergeDuplicateDonorsEmptyList() {
    List<DuplicateDonorBackup> backupLogs = service.mergeDonors(new Donor(), new ArrayList<Donor>());
    Assert.assertNotNull("backupLogs returned", backupLogs);
    Assert.assertEquals("No backups necessary", 0, backupLogs.size());
  }

  @Test
  public void testMergeDuplicateDonors() throws Exception {
    // create donors
    List<Donor> donors = new ArrayList<Donor>();
    Donor david1 = DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").build();
    Donor david2 = DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").build();
    donors.add(david1);
    donors.add(david2);

    // create donations (david1)
    Donation donation1 = aDonation().withId(DONATION_ID_1).withDonor(david1).withDonationDate(new Date()).build();
    Donation donation2 = aDonation().withId(DONATION_ID_2).withDonor(david1).withDonationDate(new Date()).build();
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(donation1);
    donations.add(donation2);
    david1.setDonations(donations);

    // create deferrals (david2)
    DeferralReason deferralReason1 = DeferralReasonBuilder.aDeferralReason()
        .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
    DonorDeferral deferral1 = DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID).withDeferredDonor(david2)
        .withDeferralReason(deferralReason1).build();
    List<DonorDeferral> deferrals = new ArrayList<DonorDeferral>();
    deferrals.add(deferral1);
    david2.setDeferrals(deferrals);

    // create new Donor
    Donor david3 = DonorBuilder.aDonor().withDonorNumber("3").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").build();

    // run test
    List<DuplicateDonorBackup> backupLogs = service.mergeDonors(david3, donors);

    // verify test
    Assert.assertNotNull("backupLogs returned", backupLogs);
    Assert.assertEquals("Donation and Deferral backups necessary", 3, backupLogs.size());
    Assert.assertTrue("1st DuplicateDonorBackup", backupLogs.contains(new DuplicateDonorBackup("3", "1", DONATION_ID_1, null)));
    Assert.assertTrue("2nd DuplicateDonorBackup", backupLogs.contains(new DuplicateDonorBackup("3", "1", DONATION_ID_2, null)));
    Assert.assertTrue("3rd DuplicateDonorBackup",
        backupLogs.contains(new DuplicateDonorBackup("3", "2", null, DONOR_DEFERRAL_ID)));
    Assert.assertNull("Donations were moved", david1.getDonations());
    Assert.assertNull("Deferrals were moved", david2.getDeferrals());
    Assert.assertEquals("Donations were moved", 2, david3.getDonations().size());
    Assert.assertEquals("Deferrals were moved", 1, david3.getDeferrals().size());
    Assert.assertEquals("Donor status was changed", DonorStatus.MERGED, david1.getDonorStatus());
    Assert.assertEquals("Donor status was changed", DonorStatus.MERGED, david2.getDonorStatus());
  }

  @Test
  public void testMergeAndSaveDonors() throws Exception {
    // create donors
    String donorNumber1 = "1";
    String donorNumber2 = "2";
    List<String> donorNumbers = Arrays.asList(new String[]{donorNumber1, donorNumber2});

    List<Donor> donors = new ArrayList<Donor>();
    Donor david1 = DonorBuilder.aDonor().withDonorNumber(donorNumber1).withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").build();
    Donor david2 = DonorBuilder.aDonor().withDonorNumber(donorNumber2).withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").build();
    donors.add(david1);
    donors.add(david2);

    // create donations (david1)
    PackType packType = PackTypeBuilder.aPackType().withPeriodBetweenDonations(90).build();
    Donation donation1 = DonationBuilder.aDonation().withId(DONATION_ID_1).withDonor(david1).withDonationDate(new Date()).withPackType(packType).build();
    Donation donation2 = DonationBuilder.aDonation().withId(DONATION_ID_2).withDonor(david1).withDonationDate(new Date()).withPackType(packType).build();
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(donation1);
    donations.add(donation2);
    david1.setDonations(donations);

    // create deferrals (david2)
    DeferralReason deferralReason1 = DeferralReasonBuilder.aDeferralReason()
        .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
    DonorDeferral deferral1 = DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID).withDeferredDonor(david2)
        .withDeferralReason(deferralReason1).build();
    List<DonorDeferral> deferrals = new ArrayList<DonorDeferral>();
    deferrals.add(deferral1);
    david2.setDeferrals(deferrals);

    // create new Donor
    Donor david3 = DonorBuilder.aDonor().withDonorNumber("3").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").build();

    // create outcomes
    BloodTestingRuleResult btrr = BloodTestingRuleResultBuilder.aBloodTestingRuleResult()
        .withBloodAbo("A").withBloodRh("+")
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .build();

    // set up mocks
    when(sequenceNumberRepository.getNextDonorNumber()).thenReturn("1234");
    when(donorRepository.findDonorsByNumbers(donorNumbers)).thenReturn(donors);
    when(donorRepository.addMergedDonor(Matchers.any(Donor.class), Matchers.anyList(), Matchers.anyList())).thenReturn(david3);
    when(bloodTestsService.executeTests(donation1)).thenReturn(btrr);
    when(bloodTestsService.executeTests(donation2)).thenReturn(btrr);
    when(donorRepository.updateDonor(david3)).thenReturn(david3);

    // run test
    david3 = service.mergeAndSaveDonors(david3, donorNumbers);

    // verify test
    Assert.assertNull("Donations were moved", david1.getDonations());
    Assert.assertNull("Deferrals were moved", david2.getDeferrals());
    Assert.assertEquals("Donations were moved", 2, david3.getDonations().size());
    Assert.assertEquals("Deferrals were moved", 1, david3.getDeferrals().size());
    Assert.assertEquals("Donor status was changed", DonorStatus.MERGED, david1.getDonorStatus());
    Assert.assertEquals("Donor status was changed", DonorStatus.MERGED, david2.getDonorStatus());
  }

  @Test
  public void testGetAllDeferralsToMerge() throws Exception {
    // create test data
    String donorNumber1 = "1000001";
    String donorNumber2 = "1000002";
    List<String> donorNumbers = Arrays.asList(new String[]{donorNumber1, donorNumber2});

    DeferralReason deferralReason =
        DeferralReasonBuilder.aDeferralReason().withDefaultDuration(1)
            .withDurationType(DurationType.PERMANENT)
            .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();

    Date dd1DeferralDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-01-01");
    DonorDeferral dd1 = DonorDeferralBuilder.aDonorDeferral().withId(DONOR_DEFERRAL_ID)
        .withDeferralReason(deferralReason)
        .withDeferredUntil(new Date()).withDeferralDate(dd1DeferralDate).build();

    UUID donorDeferralId2 = UUID.randomUUID();
    Date dd2DeferralDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-01-01");
    DonorDeferral dd2 = DonorDeferralBuilder.aDonorDeferral().withId(donorDeferralId2)
        .withDeferralReason(deferralReason)
        .withDeferredUntil(new Date()).withDeferralDate(dd2DeferralDate).build();

    Donor d1 =
        DonorBuilder.aDonor().withDonorNumber(donorNumber1).withFirstName("David")
            .withLastName("Smith").withGender(Gender.male).withBirthDate("1978-10-20")
            .withDeferrals(Arrays.asList(new DonorDeferral[]{dd1})).build();
    Donor d2 =
        DonorBuilder.aDonor().withDonorNumber(donorNumber2).withFirstName("John")
            .withLastName("Smith").withGender(Gender.male).withBirthDate("1958-10-20")
            .withDeferrals(Arrays.asList(new DonorDeferral[]{dd2})).build();

    List<Donor> donors = Arrays.asList(new Donor[]{d1, d2});

    // setup mocks
    when(donorRepository.findDonorsByNumbers(donorNumbers)).thenReturn(donors);

    // run test
    List<DonorDeferral> deferrals = service.getAllDeferralsToMerge(null, donorNumbers);

    // check results
    Assert.assertNotNull("List was returned", deferrals);
    Assert.assertEquals("Correct number of Deferrals in the list", 2, deferrals.size());
    Assert.assertEquals("Deferrals in the correct order", donorDeferralId2, deferrals.get(0).getId());
    Assert.assertEquals("Deferrals in the correct order", DONOR_DEFERRAL_ID, deferrals.get(1).getId());
  }

  @Test
  public void testGetAllDonationsToMerge() throws Exception {
    // create test data
    String donorNumber1 = "1000001";
    String donorNumber2 = "1000002";
    List<String> donorNumbers = Arrays.asList(new String[] {donorNumber1, donorNumber2});

    PackType packType = PackTypeBuilder.aPackType().withPeriodBetweenDonations(90).build();
    Donation d1 = DonationBuilder.aDonation().withId(DONATION_ID_1).withPackType(packType)
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-07-01")).build();
    Donation d2 = DonationBuilder.aDonation().withId(DONATION_ID_2).withPackType(packType)
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-01")).build();
    Donation d3 = DonationBuilder.aDonation().withId(DONATION_ID_3).withPackType(packType)
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-09-01")).build();
    Donation d4 = DonationBuilder.aDonation().withId(DONATION_ID_4).withPackType(packType)
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-05-01")).build();

    Donor david = DonorBuilder.aDonor().withDonorNumber(donorNumber1).withFirstName("David").withLastName("Smith").withGender(Gender.male)
        .withBirthDate("1978-10-20").withDonations(Arrays.asList(new Donation[]{d1, d2, d3})).build();

    Donor john = DonorBuilder.aDonor().withDonorNumber(donorNumber2).withFirstName("John").withLastName("Smith").withGender(Gender.male)
        .withBirthDate("1958-10-20").withDonations(Arrays.asList(new Donation[]{d4})).build();

    List<Donor> donors = Arrays.asList(new Donor[]{david, john});

    BloodTestingRuleResult btrr = BloodTestingRuleResultBuilder.aBloodTestingRuleResult()
        .withBloodAbo("A").withBloodRh("+")
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .build();

    // setup mocks
    when(donorRepository.findDonorsByNumbers(donorNumbers)).thenReturn(donors);
    when(bloodTestsService.executeTests(d1)).thenReturn(btrr);
    when(bloodTestsService.executeTests(d2)).thenReturn(btrr);
    when(bloodTestsService.executeTests(d3)).thenReturn(btrr);
    when(bloodTestsService.executeTests(d4)).thenReturn(btrr);

    List<Donation> donations = service.getAllDonationsToMerge(new Donor(), donorNumbers);

    Assert.assertNotNull("List was returned", donations);
    Assert.assertEquals("Correct number of Deferrals in the list", 4, donations.size());
    Assert.assertEquals("Donations in the correct order", DONATION_ID_2, donations.get(0).getId());
    Assert.assertEquals("Donations in the correct order", DONATION_ID_4, donations.get(1).getId());
    Assert.assertEquals("Donations in the correct order", DONATION_ID_1, donations.get(2).getId());
    Assert.assertEquals("Donations in the correct order", DONATION_ID_3, donations.get(3).getId());
  }
  
  @Test
  public void testGetAllDonationsToMerge_shouldRunExecuteTestsOnlyForDonationsWithTestSamplesButReturnAll()
      throws Exception {
    // create test data
    String donorNumber1 = "1000001";
    String donorNumber2 = "1000002";
    List<String> donorNumbers = Arrays.asList(new String[]{donorNumber1, donorNumber2});

    PackType packType = PackTypeBuilder.aPackType().withPeriodBetweenDonations(90).build();
    PackType packTypeNoTestSamples = PackTypeBuilder.aPackType().withTestSampleProduced(false).withPeriodBetweenDonations(90).build();
    Donation d1 = DonationBuilder.aDonation().withId(DONATION_ID_1).withPackType(packType)
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-07-01")).build();
    Donation d2NoTestSamples = DonationBuilder.aDonation().withId(DONATION_ID_2).withPackType(packTypeNoTestSamples)
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-01")).build();
    Donation d3NoTestSamples = DonationBuilder.aDonation().withId(DONATION_ID_3).withPackType(packTypeNoTestSamples)
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-09-01")).build();
    Donation d4 = DonationBuilder.aDonation().withId(DONATION_ID_4).withPackType(packType)
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-05-01")).build();

    Donor david = DonorBuilder.aDonor().withDonorNumber(donorNumber1).withFirstName("David").withLastName("Smith").withGender(Gender.male)
        .withBirthDate("1978-10-20").withDonations(Arrays.asList(new Donation[]{d1, d2NoTestSamples, d3NoTestSamples})).build();

    Donor john = DonorBuilder.aDonor().withDonorNumber(donorNumber2).withFirstName("John").withLastName("Smith").withGender(Gender.male)
        .withBirthDate("1958-10-20").withDonations(Arrays.asList(new Donation[]{d4})).build();

    List<Donor> donors = Arrays.asList(new Donor[]{david, john});

    // setup mocks
    when(donorRepository.findDonorsByNumbers(donorNumbers)).thenReturn(donors);

    // run test
    List<Donation> donations = service.getAllDonationsToMerge(new Donor(), donorNumbers);

    // verify
    verify(bloodTestsService, Mockito.times(1)).executeTests(d1);
    verify(bloodTestsService, Mockito.times(0)).executeTests(d2NoTestSamples);
    verify(bloodTestsService, Mockito.times(0)).executeTests(d3NoTestSamples);
    verify(bloodTestsService, Mockito.times(1)).executeTests(d4);

    Assert.assertNotNull("List was returned", donations);
    Assert.assertEquals("Correct number of Donations in the list", 4, donations.size());
    Assert.assertEquals("Donations in the correct order", DONATION_ID_2, donations.get(0).getId());
    Assert.assertEquals("Donations in the correct order", DONATION_ID_4, donations.get(1).getId());
    Assert.assertEquals("Donations in the correct order", DONATION_ID_1, donations.get(2).getId());
    Assert.assertEquals("Donations in the correct order", DONATION_ID_3, donations.get(3).getId());
  }
}
