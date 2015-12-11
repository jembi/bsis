package backingform;

import model.donor.Donor;

import java.util.List;

/**
 * BackingForm used to receive data via the API that will result in the specified duplicate donors
 * being merged and a new donor being created as specified.
 */
public class DuplicateDonorsBackingForm extends DonorBackingForm {

  private List<String> duplicateDonorNumbers;

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
