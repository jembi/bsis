package model.donor;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Years;

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
}
