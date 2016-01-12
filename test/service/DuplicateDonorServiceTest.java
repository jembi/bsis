package service;

import static org.mockito.Mockito.when;
import helpers.builders.BloodTestingRuleResultBuilder;
import helpers.builders.DeferralReasonBuilder;
import helpers.builders.DonationBuilder;
import helpers.builders.DonorBuilder;
import helpers.builders.DonorDeferralBuilder;
import helpers.builders.PackTypeBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.donor.DuplicateDonorBackup;
import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DonorDeferral;
import model.donordeferral.DurationType;
import model.packtype.PackType;
import model.util.Gender;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import repository.DonorRepository;
import repository.SequenceNumberRepository;
import repository.bloodtesting.BloodTypingStatus;
import scala.actors.threadpool.Arrays;
import viewmodel.BloodTestingRuleResult;

@RunWith(MockitoJUnitRunner.class)
public class DuplicateDonorServiceTest {
	
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
	public void testFindDuplicateDonorsNull() throws Exception {
		Map<String, List<Donor>> duplicateDonors = service.findDuplicateDonors(null);
		Assert.assertTrue("No matching donors", duplicateDonors.isEmpty());
	}

	@Test
	public void testFindDuplicateDonorsEmpty() throws Exception {
		Map<String, List<Donor>> duplicateDonors = service.findDuplicateDonors(new ArrayList<Donor>());
		Assert.assertTrue("No matching donors", duplicateDonors.isEmpty());
	}

	@Test
	public void testFindDuplicateDonorsBasic() throws Exception {
		List<Donor> donors = new ArrayList<Donor>();
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
		List<Donor> donors = new ArrayList<Donor>();
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
		List<Donor> donors = new ArrayList<Donor>();
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
		List<Donor> donors = new ArrayList<Donor>();
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
		List<DuplicateDonorBackup> backupLogs = service.mergeDonors(new Donor(), (List<Donor>) null);
		Assert.assertNotNull("backupLogs returned", backupLogs);
		Assert.assertEquals("No backups necessary", 0, backupLogs.size());
	}

	@Test
	public void testMergeDuplicateDonorsEmptyList() throws Exception {
		List<DuplicateDonorBackup> backupLogs = service.mergeDonors(new Donor(), new ArrayList<Donor>());
		Assert.assertNotNull("backupLogs returned", backupLogs);
		Assert.assertEquals("No backups necessary", 0, backupLogs.size());
	}
	
    @Test
    public void testMergeDuplicateDonors() throws Exception {
        // create donors
        String donorNumber1 = "1";
        String donorNumber2 = "2";
        List<String> donorNumbers = Arrays.asList(new String[] {donorNumber1, donorNumber2});
        
        List<Donor> donors = new ArrayList<Donor>();
        Donor david1 = DonorBuilder.aDonor().withDonorNumber(donorNumber1).withFirstName("David").withLastName("Smith")
                .withGender(Gender.male).withBirthDate("1977-10-20").build();
        Donor david2 = DonorBuilder.aDonor().withDonorNumber(donorNumber2).withFirstName("David").withLastName("Smith")
                .withGender(Gender.male).withBirthDate("1977-10-20").build();
        donors.add(david1);
        donors.add(david2);
        
        // create donations (david1)
        Donation donation1 = DonationBuilder.aDonation().withId(1l).withDonor(david1).withDonationDate(new Date()).build();
        Donation donation2 = DonationBuilder.aDonation().withId(2l).withDonor(david1).withDonationDate(new Date()).build();
        List<Donation> donations = new ArrayList<Donation>();
        donations.add(donation1);
        donations.add(donation2);
        david1.setDonations(donations);
        
        // create deferrals (david2)
        DeferralReason deferralReason1 = DeferralReasonBuilder.aDeferralReason()
                .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
        DonorDeferral deferral1 = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferredDonor(david2)
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
        Assert.assertTrue("1st DuplicateDonorBackup", backupLogs.contains(new DuplicateDonorBackup("3", "1", 1l, null)));
        Assert.assertTrue("2nd DuplicateDonorBackup", backupLogs.contains(new DuplicateDonorBackup("3", "1", 2l, null)));
        Assert.assertTrue("3rd DuplicateDonorBackup", backupLogs.contains(new DuplicateDonorBackup("3", "2", null, 1l)));
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
	    List<String> donorNumbers = Arrays.asList(new String[] {donorNumber1, donorNumber2});
	    
		List<Donor> donors = new ArrayList<Donor>();
		Donor david1 = DonorBuilder.aDonor().withDonorNumber(donorNumber1).withFirstName("David").withLastName("Smith")
		        .withGender(Gender.male).withBirthDate("1977-10-20").build();
		Donor david2 = DonorBuilder.aDonor().withDonorNumber(donorNumber2).withFirstName("David").withLastName("Smith")
		        .withGender(Gender.male).withBirthDate("1977-10-20").build();
		donors.add(david1);
		donors.add(david2);
		
		// create donations (david1)
		PackType packType = PackTypeBuilder.aPackType().withPeriodBetweenDonations(90).build();
		Donation donation1 = DonationBuilder.aDonation().withId(1l).withDonor(david1).withDonationDate(new Date()).withPackType(packType).build();
		Donation donation2 = DonationBuilder.aDonation().withId(2l).withDonor(david1).withDonationDate(new Date()).withPackType(packType).build();
		List<Donation> donations = new ArrayList<Donation>();
		donations.add(donation1);
		donations.add(donation2);
		david1.setDonations(donations);
		
		// create deferrals (david2)
		DeferralReason deferralReason1 = DeferralReasonBuilder.aDeferralReason()
		        .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();
		DonorDeferral deferral1 = DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferredDonor(david2)
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
            .withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
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
  public void testGetAllDeferralsToMerge() throws Exception {
    // create test data
    String donorNumber1 = "1000001";
    String donorNumber2 = "1000002";
    List<String> donorNumbers = Arrays.asList(new String[] {donorNumber1, donorNumber2});

    DeferralReason deferralReason =
        DeferralReasonBuilder.aDeferralReason().withDefaultDuration(1)
            .withDurationType(DurationType.PERMANENT)
            .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE).build();

    Date dd1CreatedDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-01-01");
    DonorDeferral dd1 =
        DonorDeferralBuilder.aDonorDeferral().withId(1l).withDeferralReason(deferralReason)
            .withDeferredUntil(new Date()).withCreatedDate(dd1CreatedDate).build();

    Date dd2CreatedDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-01-01");
    DonorDeferral dd2 =
        DonorDeferralBuilder.aDonorDeferral().withId(2l).withDeferralReason(deferralReason)
            .withDeferredUntil(new Date()).withCreatedDate(dd2CreatedDate).build();

    Donor d1 =
        DonorBuilder.aDonor().withDonorNumber(donorNumber1).withFirstName("David")
            .withLastName("Smith").withGender(Gender.male).withBirthDate("1978-10-20")
            .withDeferrals(Arrays.asList(new DonorDeferral[] {dd1})).build();
    Donor d2 =
        DonorBuilder.aDonor().withDonorNumber(donorNumber2).withFirstName("John")
            .withLastName("Smith").withGender(Gender.male).withBirthDate("1958-10-20")
            .withDeferrals(Arrays.asList(new DonorDeferral[] {dd2})).build();

    List<Donor> donors = Arrays.asList(new Donor[] {d1, d2});

    // setup mocks
    when(donorRepository.findDonorsByNumbers(donorNumbers)).thenReturn(donors);

    // run test
    List<DonorDeferral> deferrals = service.getAllDeferralsToMerge(null, donorNumbers);

    // check results
    Assert.assertNotNull("List was returned", deferrals);
    Assert.assertEquals("Correct number of Deferrals in the list", 2, deferrals.size());
    Assert.assertEquals("Deferrals in the correct order", Long.valueOf(2), deferrals.get(0).getId());
    Assert.assertEquals("Deferrals in the correct order", Long.valueOf(1), deferrals.get(1).getId());
  }
	
	@Test
	public void testGetAllDonationsToMerge() throws Exception {
	    // create test data
	    String donorNumber1 = "1000001";
	    String donorNumber2 = "1000002";
	    List<String> donorNumbers = Arrays.asList(new String[] {donorNumber1, donorNumber2});
	    
	    PackType packType = PackTypeBuilder.aPackType().withPeriodBetweenDonations(90).build();
		Donation d1 = DonationBuilder.aDonation().withId(1l).withPackType(packType)
		        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-07-01")).build();
		Donation d2 = DonationBuilder.aDonation().withId(2l).withPackType(packType)
		        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-01")).build();
		Donation d3 = DonationBuilder.aDonation().withId(3l).withPackType(packType)
		        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-09-01")).build();
        Donation d4 = DonationBuilder.aDonation().withId(4l).withPackType(packType)
            .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2015-05-01")).build();

		Donor david = DonorBuilder.aDonor().withDonorNumber(donorNumber1).withFirstName("David").withLastName("Smith").withGender(Gender.male)
		        .withBirthDate("1978-10-20").withDonations(Arrays.asList(new Donation[] { d1, d2, d3 })).build();
		
		Donor john = DonorBuilder.aDonor().withDonorNumber(donorNumber2).withFirstName("John").withLastName("Smith").withGender(Gender.male)
		        .withBirthDate("1958-10-20").withDonations(Arrays.asList(new Donation[] { d4 })).build();
		
		List<Donor> donors = Arrays.asList(new Donor[] { david, john });
		
		BloodTestingRuleResult btrr = BloodTestingRuleResultBuilder.aBloodTestingRuleResult()
		    .withBloodAbo("A").withBloodRh("+")
		    .withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
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
      Assert.assertEquals("Donations in the correct order", Long.valueOf(2), donations.get(0).getId());
      Assert.assertEquals("Donations in the correct order", Long.valueOf(4), donations.get(1).getId());
      Assert.assertEquals("Donations in the correct order", Long.valueOf(1), donations.get(2).getId());
      Assert.assertEquals("Donations in the correct order", Long.valueOf(3), donations.get(3).getId());
	}
}
