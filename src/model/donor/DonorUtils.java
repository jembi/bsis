package model.donor;

import java.util.Date;

import org.joda.time.DateMidnight;
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
    Integer age = donor.getAge();
    Date ageUpdatedOn = donor.getAgeUpdatedOn();
    if (age == null || ageUpdatedOn == null)
      return null;
    DateTime t1 = new DateTime(ageUpdatedOn);
    t1 = t1.dayOfYear().setCopy(1);
    t1 = t1.monthOfYear().setCopy(1);
    DateTime t2 = new DateTime(new Date());
    return Years.yearsBetween(t1.toDateMidnight(), t2.toDateMidnight()).getYears();
  }
}
