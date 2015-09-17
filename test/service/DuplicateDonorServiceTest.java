package service;

import helpers.builders.DonorBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.donor.Donor;
import model.util.Gender;

import org.junit.Assert;
import org.junit.Test;

public class DuplicateDonorServiceTest {
	
	private DuplicateDonorService service = new DuplicateDonorService();
	
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
		donors.add(DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith").withGender(Gender.male)
		        .withBirthDate("1977-10-20").build());
		donors.add(DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith").withGender(Gender.male)
		        .withBirthDate("1977-10-20").build());
		donors.add(DonorBuilder.aDonor().withDonorNumber("3").withFirstName("Jo").withLastName("Smith").withGender(Gender.female)
		        .withBirthDate("1978-10-20").build());
		donors.add(DonorBuilder.aDonor().withDonorNumber("4").withFirstName("Nancy").withLastName("Drew").withGender(Gender.female)
	        .withBirthDate("1964-11-20").build());
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
		donors.add(DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith").withGender(Gender.male)
		        .withBirthDate("1977-10-20").build());
		donors.add(DonorBuilder.aDonor().withDonorNumber("2").withFirstName("David").withLastName("Smith").withGender(Gender.male)
		        .withBirthDate("1977-10-20").build());
		donors.add(DonorBuilder.aDonor().withDonorNumber("3").withFirstName("Jo").withLastName("Smith").withGender(Gender.female)
		        .withBirthDate("1978-10-20").build());
		donors.add(DonorBuilder.aDonor().withDonorNumber("4").withFirstName("Sue").withLastName("Simpson").withGender(Gender.female)
	        .withBirthDate("1982-02-20").build());
		donors.add(DonorBuilder.aDonor().withDonorNumber("5").withFirstName("Nancy").withLastName("Drew").withGender(Gender.female)
	        .withBirthDate("1964-11-20").build());
		donors.add(DonorBuilder.aDonor().withDonorNumber("6").withFirstName("Sue").withLastName("Simpson").withGender(Gender.female)
	        .withBirthDate("1982-02-20").build());
		donors.add(DonorBuilder.aDonor().withDonorNumber("7").withFirstName("David").withLastName("Smith").withGender(Gender.male)
	        .withBirthDate("1977-10-20").build());
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
}
