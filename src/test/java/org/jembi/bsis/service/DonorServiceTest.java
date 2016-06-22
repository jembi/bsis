package org.jembi.bsis.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.DonorBuilder;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.service.DonorService;
import org.junit.Assert;
import org.junit.Test;

public class DonorServiceTest {

  @Test
  public void testSetDateOfFirstDonation() throws Exception {

    Donor david1 = DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").build();

    Date dateOfFirstDonation = new SimpleDateFormat("yyyy-MM-dd").parse("2015-09-01");
    Donation donation1 = DonationBuilder.aDonation().withDonor(david1).withDonationDate(dateOfFirstDonation).build();

    DonorService donorService = new DonorService();
    donorService.setDonorDateOfFirstDonation(david1, donation1);

    Assert.assertEquals("Date of First Donation set", dateOfFirstDonation, david1.getDateOfFirstDonation());
  }

  @Test
  public void testSetDonorDateOfLastDonation() throws Exception {

    Donor david1 = DonorBuilder.aDonor().withDonorNumber("1").withFirstName("David").withLastName("Smith")
        .withGender(Gender.male).withBirthDate("1977-10-20").build();

    Date dateOfFirstDonation = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-01");
    Date dateOfLastDonation = new SimpleDateFormat("yyyy-MM-dd").parse("2015-10-01");
    PackType packType = new PackType();
    int period = 23;
    packType.setPeriodBetweenDonations(period);
    Donation donation1 = DonationBuilder.aDonation().withDonor(david1).withDonationDate(dateOfFirstDonation)
        .withPackType(packType).build();
    Donation donation2 = DonationBuilder.aDonation().withDonor(david1).withDonationDate(dateOfLastDonation)
        .withPackType(packType).build();

    DonorService donorService = new DonorService();
    donorService.setDonorDateOfLastDonation(david1, donation2);
    donorService.setDonorDateOfLastDonation(david1, donation1);

    Assert.assertEquals("Date of last donation set", dateOfLastDonation, david1.getDateOfLastDonation());
  }
}
