package service;

import model.donation.Donation;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.donor.DuplicateDonorBackup;
import model.donordeferral.DonorDeferral;
import model.packtype.PackType;
import model.util.Gender;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.DonorRepository;
import repository.SequenceNumberRepository;
import viewmodel.BloodTestingRuleResult;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service that provides functionality in order to identify and merge duplicate Donors
 */
@Transactional
@Service
public class DuplicateDonorService {

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private SequenceNumberRepository sequenceNumberRepository;

  @Autowired
  private BloodTestsService bloodTestsService;

  @Autowired
  private DonorService donorService;

  /**
   * Completes the merge of the list of donors and also persists the new donor, the updates to the
   * existing donors and the backup logs necessary for restoring merged donors.
   *
   * @param newDonor     Donor with the details selected by the user merging
   * @param donorNumbers List of donorNumbers for the Donors that are being merged into the
   *                     newDonor
   * @return Donor new donor
   */
  public Donor mergeAndSaveDonors(Donor newDonor, List<String> donorNumbers) {
    // set donor number for the new donor
    newDonor.setDonorNumber(sequenceNumberRepository.getNextDonorNumber());
    // get the list of donors to merge
    List<Donor> donorsToMerge = donorRepository.findDonorsByNumbers(donorNumbers);
    // do the merge
    List<DuplicateDonorBackup> backupLogs = mergeDonors(newDonor, donorsToMerge);
    // save the new donor, the merged donors and the backup logs
    Donor savedDonor = donorRepository.addMergedDonor(newDonor, donorsToMerge, backupLogs);
    // run the necessary blood tests
    executeTestsAndUpdate(newDonor, newDonor.getDonations());
    // persist the updates after the tests
    savedDonor = donorRepository.updateDonor(savedDonor);

    return savedDonor;
  }

  /**
   * Complete the merge of the list of donors by changing the status and moving Donations and
   * Deferrals to the specified new donor.
   *
   * @param newDonor Donor with the details selected by the user merging
   * @param donors   List of Donors that are being merged into the newDonor
   * @return List of DuplicateDonorBackup records that can be used to rollback a merge
   */
  protected List<DuplicateDonorBackup> mergeDonors(Donor newDonor, List<Donor> donors) {
    String newDonorNumber = newDonor.getDonorNumber();
    // combine Donations and Deferrals and create a backup log
    List<Donation> combinedDonations = new ArrayList<>();
    List<DonorDeferral> combinedDeferrals = new ArrayList<>();
    List<DuplicateDonorBackup> backupLog = new ArrayList<>();
    if (donors != null) {
      for (Donor donor : donors) {
        donor.setDonorStatus(DonorStatus.MERGED);
        String donorNumber = donor.getDonorNumber();
        List<Donation> donorDonations = donor.getDonations();
        if (donorDonations != null) {
          for (Donation donation : donorDonations) {
            if (donation != null) {
              combinedDonations.add(donation);
              donation.setDonor(newDonor);
              backupLog.add(new DuplicateDonorBackup(newDonorNumber, donorNumber, donation.getId(), null));
            }

          }
        }
        List<DonorDeferral> deferrals = donor.getDeferrals();
        if (deferrals != null) {
          for (DonorDeferral deferral : deferrals) {
            if (deferral != null) {
              combinedDeferrals.add(deferral);
              deferral.setDeferredDonor(newDonor);
              backupLog.add(new DuplicateDonorBackup(newDonorNumber, donorNumber, null, deferral.getId()));
            }
          }
        }
        donor.setDeferrals(null);
        donor.setDonations(null);
      }
    }
    combinedDonations = sortDonationsByDate(combinedDonations);
    newDonor.setDonations(combinedDonations);
    newDonor.setDeferrals(combinedDeferrals);
    return backupLog;
  }

  /**
   * Retrieves a list of the Donations made by the specified Donors who are going to be merged
   * into a single Donor. The BloodTestingRuleEngine is executed for each Donation (in
   * chronological order) and the Donation updated with the results. Note: none of these changes
   * will be persisted.
   *
   * @param newDonor Donor new merged donor
   * @param donors   List of Donors that are being merged into the newDonor
   * @return List of Donations, in chronological order
   */
  public List<Donation> getAllDonationsToMerge(Donor newDonor, List<String> donorNumbers) {
    List<Donation> combinedDonations = combineDonations(donorRepository.findDonorsByNumbers(donorNumbers));
    combinedDonations = sortDonationsByDate(combinedDonations);
    executeTestsAndUpdate(newDonor, combinedDonations);
    return combinedDonations;
  }

  private List<Donation> combineDonations(List<Donor> donors) {
    List<Donation> combinedDonations = new ArrayList<>();
    if (donors != null) {
      for (Donor donor : donors) {
        List<Donation> donorDonations = donor.getDonations();
        if (donorDonations != null) {
          for (Donation donation : donorDonations) {
            if (donation != null) {
              combinedDonations.add(donation);
            }
          }
        }
      }
    }
    return combinedDonations;
  }

  private List<Donation> sortDonationsByDate(List<Donation> combinedDonations) {
    // sort donations in chronological order
    Collections.sort(combinedDonations, new Comparator<Donation>() {

      @Override
      public int compare(Donation d1, Donation d2) {
        return d1.getDonationDate().compareTo(d2.getDonationDate());
      }
    });
    return combinedDonations;
  }

  private void executeTestsAndUpdate(Donor newDonor, List<Donation> combinedDonations) {
    for (Donation donation : combinedDonations) {
      // analyse the Blood Tests
      BloodTestingRuleResult ruleResult = bloodTestsService.executeTests(donation);
      // FIXME: note: donation was updated after running the tests (not persisted) - see line 337 of BloodTestingRuleEngine
      // process test results
      bloodTestsService.updateDonationWithTestResults(donation, ruleResult);
      // sets the Donor's donation related attributes
      donorService.setDonorDateOfFirstDonation(newDonor, donation);
      donorService.setDonorDateOfLastDonation(newDonor, donation);
      setDonorDueToDonate(newDonor, donation);
    }
  }

  /**
   * Sets the Donor's next due date based on the specified donation.
   * <p>
   * NOTE: If the specified Donation is not the latest Donation, then an invalid "due to donate"
   * date will be set.
   *
   * @param donor    Donor to update
   * @param donation Donation latest
   * @see DonorService.setDonorDueToDonate(Donor)
   */
  private void setDonorDueToDonate(Donor donor, Donation donation) {
    PackType packType = donation.getPackType();
    int periodBetweenDays = packType.getPeriodBetweenDonations();
    Calendar dueToDonateDate = Calendar.getInstance();
    dueToDonateDate.setTime(donation.getDonationDate());
    dueToDonateDate.add(Calendar.DAY_OF_YEAR, periodBetweenDays);
    if (donor.getDueToDonate() == null || dueToDonateDate.getTime().after(donor.getDueToDonate())) {
      donor.setDueToDonate(dueToDonateDate.getTime());
    }
  }

  /**
   * Retrieves a list of the Deferrals for the specified Donors.
   *
   * @param newDonor Donor new merged donor
   * @param donors   List of Donors that are being merged into the newDonor
   * @return List of DonorDeferrals, in chronological order
   */
  public List<DonorDeferral> getAllDeferralsToMerge(Donor newDonor, List<String> donorNumbers) {
    List<Donor> donors = donorRepository.findDonorsByNumbers(donorNumbers);
    return combineDeferralsAndSortByDate(donors);
  }

  private List<DonorDeferral> combineDeferralsAndSortByDate(List<Donor> donors) {
    List<DonorDeferral> combinedDeferrals = new ArrayList<>();
    if (donors != null) {
      for (Donor donor : donors) {
        List<DonorDeferral> donorDeferrals = donor.getDeferrals();
        if (donorDeferrals != null) {
          for (DonorDeferral deferral : donorDeferrals) {
            if (deferral != null) {
              combinedDeferrals.add(deferral);
            }
          }
        }
      }
    }

    // sort donations in chronological order
    Collections.sort(combinedDeferrals, new Comparator<DonorDeferral>() {

      @Override
      public int compare(DonorDeferral d1, DonorDeferral d2) {
        return d1.getCreatedDate().compareTo(d2.getCreatedDate());
      }
    });
    return combinedDeferrals;
  }

  /**
   * Identifies the duplicate donors matching on first name, last name, gender and date of birth.
   *
   * @param donors List<Donor> list of donors to check
   * @return Map<List<Donor>> map of duplicate donors found, will not be null or contain nulls
   */
  public Map<String, List<Donor>> findDuplicateDonors(List<Donor> donors) {
    Map<String, List<Donor>> duplicateDonors = new HashMap<>();
    if (donors != null) {
      List<Donor> potentialDuplicates = new ArrayList<>(donors);
      for (int i = 0; i < potentialDuplicates.size(); i++) {
        Donor d1 = potentialDuplicates.get(i);
        List<Donor> duplicates = new ArrayList<>();
        // find the duplicates starting from the next element in the list of Donors
        for (int j = i + 1; j < potentialDuplicates.size(); j++) {
          Donor d2 = potentialDuplicates.get(j);
          if (match(d1, d2)) {
            duplicates.add(d2);
          }
        }
        if (!duplicates.isEmpty()) {
          // remove the found duplicates from the potential list
          for (Donor d3 : duplicates) {
            potentialDuplicates.remove(d3);
          }
          // add the new duplicates to the duplicate map
          duplicates.add(d1);
          duplicateDonors.put(String.valueOf(d1.getDonorNumber()), duplicates);
        }
      }
    }
    return duplicateDonors;
  }

  /**
   * Identifies the donors that are probably a duplicate of the specified donor
   *
   * @param donor  Donor on which to match
   * @param donors List<Donor> list of donors to check
   * @return List<Donor> list of suspected duplicate of the specified donor
   */
  public List<Donor> findDuplicateDonors(Donor donor, List<Donor> donors) {
    List<Donor> duplicates = new ArrayList<>();
    for (int i = 0; i < donors.size(); i++) {
      Donor donor2 = donors.get(i);
      if (match(donor, donor2)) {
        duplicates.add(donor2);
      }
    }
    return duplicates;
  }

  /**
   * Determines if two donors are a match. If both Donors are null, then they are matched.
   *
   * @param donor1 Donor first donor to match
   * @param donor2 Donor second donor to match
   * @return boolean true if the donors are considered duplicates
   */
  public boolean match(Donor donor1, Donor donor2) {
    if (donor1 == null && donor2 == null) {
      return true;
    }
    if (donor1 == null || donor2 == null) {
      return false;
    }
    if (!StringUtils.equalsIgnoreCase(donor1.getFirstName(), donor2.getFirstName())) {
      return false;
    }
    if (!StringUtils.equalsIgnoreCase(donor1.getLastName(), donor2.getLastName())) {
      return false;
    }
    if (!equals(donor1.getGender(), donor2.getGender())) {
      return false;
    }
    if (!equals(donor1.getBirthDate(), donor2.getBirthDate())) {
      return false;
    }
    return true;
  }

  private boolean equals(Gender gender1, Gender gender2) {
    if ((gender1 == null && gender2 == null) || (gender1 != null && gender1.equals(gender2))) {
      return true;
    }
    return false;
  }

  private boolean equals(Date dob1, Date dob2) {
    if (dob1 == null && dob2 == null) {
      return true;
    }
    if (dob1 == null || dob2 == null) {
      return false;
    }
    SimpleDateFormat dobFormatter = new SimpleDateFormat("yyyy-MM-dd");
    String dob1String = dobFormatter.format(dob1);
    String dob2String = dobFormatter.format(dob2);
    return StringUtils.equals(dob1String, dob2String);
  }
}
