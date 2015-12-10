package service;

import helpers.builders.DeferralReasonBuilder;
import helpers.builders.DonationBuilder;
import helpers.builders.DonorBuilder;
import helpers.builders.DonorDeferralBuilder;
import model.donation.Donation;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.donor.DuplicateDonorBackup;
import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DonorDeferral;
import model.donordeferral.DurationType;
import model.util.Gender;
import org.junit.Assert;
import org.junit.Test;
import scala.actors.threadpool.Arrays;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DuplicateDonorServiceTest {

  private DuplicateDonorService service = new DuplicateDonorService();

  @Test
  public void testFindDuplicateDonorsNull() throws Exception {
    Map<String, List<Donor>> duplicateDonors = service.findDuplicateDonors(null);
    Assert.assertTrue("No matching donors", duplicateDonors.isEmpty());
  }

  @Test
  public void testFindDuplicateDonorsEmpty() throws Exception {
    Map<String, List<Donor>> duplicateDonors = service.findDuplicateDonors(new ArrayList<>());
    Assert.assertTrue("No matching donors", duplicateDonors.isEmpty());
  }

  @Test
  public void testFindDuplicateDonorsBasic() throws Exception {
    List<Donor> donors = new ArrayList<>();
    donors.add(DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("3").withFirstName("Jo").withLastName("Smith")
            .withGender(Gender.female).withBirthDate("1978-10-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("4").withFirstName("Nancy").withLastName("Drew")
            .withGender(Gender.female).withBirthDate("1964-11-20").build());
    Map<String, List<Donor>> duplicateDonorsMap = service.findDuplicateDonors(donors);
    Assert.assertEquals("One set of matching donors", 1, duplicateDonorsMap.size());
    List<Donor> duplicateDonors = duplicateDonorsMap.get("1");
    Assert.assertEquals("Two matching donors", 2, duplicateDonors.size());
    Assert.assertEquals("David is matching Donor", "David", duplicateDonors.get(0).getFirstName());
    Assert.assertEquals("David is matching Donor", "David", duplicateDonors.get(1).getFirstName());
  }

  @Test
  public void testFindDuplicateDonorsDouble() throws Exception {
    List<Donor> donors = new ArrayList<>();
    donors.add(DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("3").withFirstName("Jo").withLastName("Smith")
            .withGender(Gender.female).withBirthDate("1978-10-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("4").withFirstName("Sue").withLastName("Simpson")
            .withGender(Gender.female).withBirthDate("1982-02-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("5").withFirstName("Nancy").withLastName("Drew")
            .withGender(Gender.female).withBirthDate("1964-11-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("6").withFirstName("Sue").withLastName("Simpson")
            .withGender(Gender.female).withBirthDate("1982-02-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("7").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build());
    Map<String, List<Donor>> duplicateDonorsMap = service.findDuplicateDonors(donors);
    Assert.assertEquals("Two sets of matching donors", 2, duplicateDonorsMap.size());
    List<Donor> duplicateDonors1 = duplicateDonorsMap.get("1");
    Assert.assertEquals("Three matching donors", 3, duplicateDonors1.size());
    Assert.assertEquals("David is matching Donor", "David", duplicateDonors1.get(0).getFirstName());
    Assert.assertEquals("David is matching Donor", "David", duplicateDonors1.get(1).getFirstName());
    Assert.assertEquals("David is matching Donor", "David", duplicateDonors1.get(2).getFirstName());
    List<Donor> duplicateDonors2 = duplicateDonorsMap.get("4");
    Assert.assertEquals("Two matching donors", 2, duplicateDonors2.size());
    Assert.assertEquals("Sue is matching Donor", "Sue", duplicateDonors2.get(0).getFirstName());
    Assert.assertEquals("Sue is matching Donor", "Sue", duplicateDonors2.get(1).getFirstName());
  }

  @Test
  public void testFindDuplicateDonorsMatchingOneBasic() throws Exception {
    List<Donor> donors = new ArrayList<>();
    Donor donor = DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build();
    donors.add(donor);
    donors.add(DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("3").withFirstName("Jo").withLastName("Smith")
            .withGender(Gender.female).withBirthDate("1978-10-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("4").withFirstName("Nancy").withLastName("Drew")
            .withGender(Gender.female).withBirthDate("1964-11-20").build());
    List<Donor> duplicateDonors = service.findDuplicateDonors(donor, donors);
    Assert.assertEquals("Two matching donors", 2, duplicateDonors.size());
    Assert.assertEquals("David is matching Donor", "David", duplicateDonors.get(0).getFirstName());
    Assert.assertEquals("David is matching Donor", "David", duplicateDonors.get(1).getFirstName());
  }

  @Test
  public void testFindDuplicateMatchingOneDonorsDouble() throws Exception {
    List<Donor> donors = new ArrayList<>();
    donors.add(DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("3").withFirstName("Jo").withLastName("Smith")
            .withGender(Gender.female).withBirthDate("1978-10-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("4").withFirstName("Sue").withLastName("Simpson")
            .withGender(Gender.female).withBirthDate("1982-02-20").build());
    donors.add(DonorBuilder.aDonor().withDonorNumber("5").withFirstName("Nancy").withLastName("Drew")
            .withGender(Gender.female).withBirthDate("1964-11-20").build());
    Donor donor = DonorBuilder.aDonor().withDonorNumber("6").withFirstName("Sue").withLastName("Simpson")
            .withGender(Gender.female).withBirthDate("1982-02-20").build();
    donors.add(donor);
    donors.add(DonorBuilder.aDonor().withDonorNumber("7").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build());
    List<Donor> duplicateDonors = service.findDuplicateDonors(donor, donors);
    Assert.assertEquals("Two matching donors", 2, duplicateDonors.size());
    Assert.assertEquals("Sue is matching Donor", "Sue", duplicateDonors.get(0).getFirstName());
    Assert.assertEquals("Sue is matching Donor", "Sue", duplicateDonors.get(1).getFirstName());
  }

  @Test
  public void testMergeDuplicateDonorsNull() throws Exception {
    List<DuplicateDonorBackup> backupLogs = service.mergeDonors(new Donor(), null);
    Assert.assertNotNull("backupLogs returned", backupLogs);
    Assert.assertEquals("No backups necessary", 0, backupLogs.size());
  }

  @Test
  public void testMergeDuplicateDonorsEmptyList() throws Exception {
    List<DuplicateDonorBackup> backupLogs = service.mergeDonors(new Donor(), new ArrayList<>());
    Assert.assertNotNull("backupLogs returned", backupLogs);
    Assert.assertEquals("No backups necessary", 0, backupLogs.size());
  }

  @Test
  public void testMergeDuplicateDonors() throws Exception {
    // create donors
    List<Donor> donors = new ArrayList<>();
    Donor david1 = DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build();
    Donor david2 = DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build();
    donors.add(david1);
    donors.add(david2);

    // create donations (david1)
    Donation donation1 = DonationBuilder.aDonation().withId(1L).withDonor(david1).withDonationDate(new Date()).build();
    Donation donation2 = DonationBuilder.aDonation().withId(2L).withDonor(david1).withDonationDate(new Date()).build();
    List<Donation> donations = new ArrayList<>();
    donations.add(donation1);
    donations.add(donation2);
    david1.setDonations(donations);

    // create deferrals (david2)
    DeferralReason deferralReason1 = DeferralReasonBuilder.aDeferralReason()
            .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
    DonorDeferral deferral1 = DonorDeferralBuilder.aDonorDeferral().withId(1L).withDeferredDonor(david2)
            .withDeferralReason(deferralReason1).build();
    List<DonorDeferral> deferrals = new ArrayList<>();
    deferrals.add(deferral1);
    david2.setDeferrals(deferrals);

    // create new Donor
    Donor david3 = DonorBuilder.aDonor().withDonorNumber("3").withFirstName("David").withLastName("Smith")
            .withGender(Gender.male).withBirthDate("1977-10-20").build();

    List<DuplicateDonorBackup> backupLogs = service.mergeDonors(david3, donors);
    Assert.assertNotNull("backupLogs returned", backupLogs);
    Assert.assertEquals("Donation and Deferral backups necessary", 3, backupLogs.size());
    Assert.assertTrue("1st DuplicateDonorBackup", backupLogs.contains(new DuplicateDonorBackup("3", "1", 1L, null)));
    Assert.assertTrue("2nd DuplicateDonorBackup", backupLogs.contains(new DuplicateDonorBackup("3", "1", 2L, null)));
    Assert.assertTrue("3rd DuplicateDonorBackup", backupLogs.contains(new DuplicateDonorBackup("3", "2", null, 1L)));
    Assert.assertNull("Donations were moved", david1.getDonations());
    Assert.assertNull("Deferrals were moved", david2.getDeferrals());
    Assert.assertEquals("Donations were moved", 2, david3.getDonations().size());
    Assert.assertEquals("Deferrals were moved", 1, david3.getDeferrals().size());
    Assert.assertEquals("Donor status was changed", DonorStatus.MERGED, david1.getDonorStatus());
    Assert.assertEquals("Donor status was changed", DonorStatus.MERGED, david2.getDonorStatus());
  }

  @Test
  public void testMatchNullDonor() throws Exception {
    boolean result = service.match(null, null);
    Assert.assertTrue("Null Donors match", result);
  }

  @Test
  public void testMatchOneNullDonor() throws Exception {
    boolean result = service.match(null, new Donor());
    Assert.assertFalse("One null Donors does not match", result);
  }

  @Test
  public void testMatchingDonor() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertTrue("Donor does match", result);
  }

  @Test
  public void testMatchingDonorDifferentBirthTime() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate(sdf.parse("1977-10-20 09:11")).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate(sdf.parse("1977-10-20 21:11")).build();
    boolean result = service.match(d1, d2);
    Assert.assertTrue("Donor does match", result);
  }

  @Test
  public void testNonMatchingDonorFirstName() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("Davey").withLastName("Smith").withGender(Gender.male)
            .withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testMatchingDonorFirstNameNull() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withLastName("Smith").withGender(Gender.male).withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withLastName("Smith").withGender(Gender.male).withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertTrue("Donor does match", result);
  }

  @Test
  public void testNonMatchingDonorFirstNameNull1() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withLastName("Smith").withGender(Gender.male).withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("Davey").withLastName("Smith").withGender(Gender.male)
            .withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testNonMatchingDonorFirstNameNull2() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("Davey").withLastName("Smith").withGender(Gender.male)
            .withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withLastName("Smith").withGender(Gender.male).withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testMatchingDonorLastName() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withGender(Gender.male).withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("David").withGender(Gender.male).withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertTrue("Donor does match", result);
  }

  @Test
  public void testNonMatchingDonorLastName() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("David").withLastName("Simpson").withGender(Gender.male)
            .withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testNonMatchingDonorLastNameNull1() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withGender(Gender.male).withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("David").withLastName("Simpson").withGender(Gender.male)
            .withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testNonMatchingDonorLastNameNull2() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withLastName("Simpson").withGender(Gender.male)
            .withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("David").withGender(Gender.male).withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testNonMatchingDonorGender() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("Jo").withLastName("Smith").withGender(Gender.male)
            .withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("Jo").withLastName("Smith").withGender(Gender.female)
            .withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testMatchingDonorGenderNull() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("Jo").withLastName("Smith").withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("Jo").withLastName("Smith").withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertTrue("Donor does match", result);
  }

  @Test
  public void testNonMatchingDonorGenderNull1() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("Jo").withLastName("Smith").withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("Jo").withLastName("Smith").withGender(Gender.female)
            .withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testNonMatchingDonorGenderNull2() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("Jo").withLastName("Smith").withGender(Gender.female)
            .withBirthDate(new Date()).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("Jo").withLastName("Smith").withBirthDate(new Date()).build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testNonMatchingDonorBirthDate() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate("1977-10-20").build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate("1978-10-20").build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testMatchingDonorBirthDateNull() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male).build();
    boolean result = service.match(d1, d2);
    Assert.assertTrue("Donor does match", result);
  }

  @Test
  public void testNonMatchingDonorBirthDateNull1() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate("1978-10-20").build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testNonMatchingDonorBirthDateNull2() throws Exception {
    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate("1978-10-20").build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male).build();
    boolean result = service.match(d1, d2);
    Assert.assertFalse("Donor does not match", result);
  }

  @Test
  public void testGetDonorDeferrals() throws Exception {
    DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().withDefaultDuration(1)
            .withDurationType(DurationType.PERMANENT).withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();

    Date dd1CreatedDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-01-01");
    DonorDeferral dd1 = DonorDeferralBuilder.aDonorDeferral().withId(1L).withDeferralReason(deferralReason)
            .withDeferredUntil(new Date()).withCreatedDate(dd1CreatedDate).build();

    Date dd2CreatedDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-01-01");
    DonorDeferral dd2 = DonorDeferralBuilder.aDonorDeferral().withId(2L).withDeferralReason(deferralReason)
            .withDeferredUntil(new Date()).withCreatedDate(dd2CreatedDate).build();

    Donor d1 = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate("1978-10-20").withDeferrals(Arrays.asList(new DonorDeferral[]{dd1})).build();
    Donor d2 = DonorBuilder.aDonor().withFirstName("John").withLastName("Smith").withGender(Gender.male)
            .withBirthDate("1958-10-20").withDeferrals(Arrays.asList(new DonorDeferral[]{dd2})).build();

    List<DonorDeferral> deferrals = service.combineDeferralsAndSortByDate(Arrays.asList(new Donor[]{d1, d2}));

    Assert.assertNotNull("List was returned", deferrals);
    Assert.assertEquals("Correct number of Deferrals in the list", 2, deferrals.size());
    Assert.assertEquals("Deferrals in the correct order", Long.valueOf(2), deferrals.get(0).getId());
    Assert.assertEquals("Deferrals in the correct order", Long.valueOf(1), deferrals.get(1).getId());
  }

  @Test
  public void testGetDonorDonations() throws Exception {
    Donation d1 = DonationBuilder.aDonation().withId(1L)
            .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-07-01")).build();
    Donation d2 = DonationBuilder.aDonation().withId(2L)
            .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-01")).build();
    Donation d3 = DonationBuilder.aDonation().withId(3L)
            .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-09-01")).build();
    Donor david = DonorBuilder.aDonor().withFirstName("David").withLastName("Smith").withGender(Gender.male)
            .withBirthDate("1978-10-20").withDonations(Arrays.asList(new Donation[]{d1, d2, d3})).build();

    Donation d4 = DonationBuilder.aDonation().withId(4L)
            .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-05-01")).build();
    Donor john = DonorBuilder.aDonor().withFirstName("John").withLastName("Smith").withGender(Gender.male)
            .withBirthDate("1958-10-20").withDonations(Arrays.asList(new Donation[]{d4})).build();

    List<Donation> donations = service.combineDonations(Arrays.asList(new Donor[]{david, john}));
    donations = service.sortDonationsByDate(donations);

    Assert.assertNotNull("List was returned", donations);
    Assert.assertEquals("Correct number of Deferrals in the list", 4, donations.size());
    Assert.assertEquals("Donations in the correct order", Long.valueOf(2), donations.get(0).getId());
    Assert.assertEquals("Donations in the correct order", Long.valueOf(4), donations.get(1).getId());
    Assert.assertEquals("Donations in the correct order", Long.valueOf(1), donations.get(2).getId());
    Assert.assertEquals("Donations in the correct order", Long.valueOf(3), donations.get(3).getId());
  }
}
