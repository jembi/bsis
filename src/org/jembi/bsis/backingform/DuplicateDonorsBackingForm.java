package org.jembi.bsis.backingform;

import java.util.List;

import org.jembi.bsis.model.donor.Donor;

/**
 * BackingForm used to receive data via the API that will result in the specified duplicate donors
 * being merged and a new donor being created as specified.
 */
public class DuplicateDonorsBackingForm extends DonorBackingForm {

  List<String> duplicateDonorNumbers;

  public DuplicateDonorsBackingForm() {
    super();
  }

  public DuplicateDonorsBackingForm(Donor donor) {
    super(donor);
  }

  public List<String> getDuplicateDonorNumbers() {
    return duplicateDonorNumbers;
  }

  public void setDuplicateDonorNumbers(List<String> duplicateDonorNumbers) {
    this.duplicateDonorNumbers = duplicateDonorNumbers;
  }
}
