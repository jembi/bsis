package model.donor;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Years;

public class DonorUtils {

  public static Integer computeDonorAge(Donor donor) {
    Date birthDate = donor.getBirthDate();
    if (birthDate != null) {
      DateTime t1 = new DateTime(birthDate);
      DateTime t2 = new DateTime(new Date());
      return Years.yearsBetween(t1.toDateMidnight(), t2.toDateMidnight()).getYears();
    }
    return null;
  }
}
