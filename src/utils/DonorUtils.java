package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


import model.donor.Donor;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.stereotype.Component;


@Component
public class DonorUtils {
  
  public static Integer computeDonorAge(Donor donor) {
    Date birthDate = donor.getBirthDate();
    Date birthDateInferred = donor.getBirthDateInferred();

    if (birthDate == null && birthDateInferred == null)
      return null;

    Date birthDateToUse = birthDate;
    if (birthDateToUse == null)
      birthDateToUse = birthDateInferred;

    DateTime t1 = new DateTime(birthDateToUse);
    DateTime t2 = new DateTime(new Date());
    return Years.yearsBetween(t1.toDateMidnight(), t2.toDateMidnight()).getYears();
  }

  public static String computeDonorHash(Donor donor) {
    String firstName = donor.getFirstName();
    if (firstName == null)
      firstName = "";
    String middleName = donor.getMiddleName();
    if (middleName == null)
      middleName = "";
    String lastName = donor.getLastName();
    if (lastName == null)
      lastName = "";
    Date birthDate = donor.getBirthDate();
    Date birthDateInferred = donor.getBirthDateInferred();

    String birthDatePart = "";
    if (birthDate == null && birthDateInferred == null)
      birthDatePart = "";

    Date birthDateToUse = birthDate;
    if (birthDateToUse == null)
      birthDateToUse = birthDateInferred;

    if (birthDateToUse != null)
      birthDatePart = CustomDateFormatter.getDateString(birthDateToUse);

    String md5Checksum = "";
    String hashComponents = firstName + middleName + lastName + birthDatePart;
    // code taken from here: http://stackoverflow.com/a/10530959/161628
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(hashComponents.getBytes());
      md5Checksum = String.format("%032x", new BigInteger(1, md5.digest()));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return md5Checksum;
  }

}
