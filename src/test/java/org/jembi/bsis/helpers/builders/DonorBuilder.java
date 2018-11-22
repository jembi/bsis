package org.jembi.bsis.helpers.builders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.DonorPersister;
import org.jembi.bsis.model.address.Address;
import org.jembi.bsis.model.address.AddressType;
import org.jembi.bsis.model.address.Contact;
import org.jembi.bsis.model.address.ContactMethodType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.idtype.IdType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.preferredlanguage.PreferredLanguage;
import org.jembi.bsis.model.util.Gender;

public class DonorBuilder extends AbstractEntityBuilder<Donor> {

  private UUID id;
  private String donorNumber;
  private String title;
  private String firstName;
  private String lastName;
  private String middleName;
  private String callingName;
  private Gender gender;
  private Date birthDate;
  private String notes;
  private Boolean deleted = false;
  private Date dateOfFirstDonation;
  private Date dateOfLastDonation;
  private Date dueToDonate;
  private DonorStatus donorStatus = DonorStatus.NORMAL;
  private Location venue;
  private List<DonorDeferral> deferrals;
  private List<Donation> donations;
  private String bloodAbo;
  private String bloodRh;
  private PreferredLanguage preferredLanguage;
  private IdType idType;
  private String idNumber;
  private Contact contact;
  private ContactMethodType contactMethodType;
  private Address address;
  private AddressType addressType;
  private Date createdDate;

  public DonorBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public DonorBuilder withDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
    return this;
  }

  public DonorBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public DonorBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  public DonorBuilder thatIsNotDeleted() {
    deleted = false;
    return this;
  }

  public DonorBuilder withDateOfFirstDonation(Date dateOfFirstDonation) {
    this.dateOfFirstDonation = dateOfFirstDonation;
    return this;
  }

  public DonorBuilder withDateOfLastDonation(Date dateOfLastDonation) {
    this.dateOfLastDonation = dateOfLastDonation;
    return this;
  }
  
  public DonorBuilder withDueToDonate(Date dueToDonate) {
    this.dueToDonate = dueToDonate;
    return this;
  }

  public DonorBuilder withDonorStatus(DonorStatus donorStatus) {
    this.donorStatus = donorStatus;
    return this;
  }

  public DonorBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }
  
  public DonorBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public DonorBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public DonorBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }
  
  public DonorBuilder withMiddleName(String middleName) {
    this.middleName = middleName;
    return this;
  }
  
  public DonorBuilder withCallingName(String callingName) {
    this.callingName = callingName;
    return this;
  }

  public DonorBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public DonorBuilder withBirthDate(Date birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public DonorBuilder withBirthDate(String dateOfBirth) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    this.birthDate = sdf.parse(dateOfBirth);
    return this;
  }

  public DonorBuilder withDeferrals(List<DonorDeferral> deferrals) {
    this.deferrals = deferrals;
    return this;
  }

  public DonorBuilder withDonations(List<Donation> donations) {
    this.donations = donations;
    return this;
  }

  public DonorBuilder withDonation(Donation donation) {
    if (donations == null) {
      donations = new ArrayList<>();
    }
    donations.add(donation);
    return this;
  }

  public DonorBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public DonorBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }
  
  public DonorBuilder withPreferredLanguage(PreferredLanguage preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
    return this;
  }
  
  public DonorBuilder withIdType(IdType idType) {
    this.idType = idType;
    return this;
  }
  
  public DonorBuilder withIdNumber(String idNumber) {
    this.idNumber = idNumber;
    return this;
  }
  
  public DonorBuilder withContact(Contact contact) {
    this.contact = contact;
    return this;
  }
  
  public DonorBuilder withContactMethodType(ContactMethodType contactMethodType) {
    this.contactMethodType = contactMethodType;
    return this;
  }
  
  public DonorBuilder withAddress(Address address) {
    this.address = address;
    return this;
  }
  
  public DonorBuilder withAddressType(AddressType addressType) {
    this.addressType = addressType;
    return this;
  }
  
  public DonorBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  @Override
  public Donor build() {
    Donor donor = new Donor();
    donor.setId(id);
    donor.setDonorNumber(donorNumber);
    donor.setTitle(title);
    donor.setFirstName(firstName);
    donor.setLastName(lastName);
    donor.setCallingName(callingName);
    donor.setMiddleName(middleName);
    donor.setGender(gender);
    donor.setBirthDate(birthDate);
    donor.setNotes(notes);
    donor.setIsDeleted(deleted);
    donor.setDateOfFirstDonation(dateOfFirstDonation);
    donor.setDateOfLastDonation(dateOfLastDonation);
    donor.setDueToDonate(dueToDonate);
    donor.setDonorStatus(donorStatus);
    donor.setVenue(venue);
    donor.setDeferrals(deferrals);
    donor.setDonations(donations);
    donor.setBloodAbo(bloodAbo);
    donor.setBloodRh(bloodRh);
    donor.setPreferredLanguage(preferredLanguage);
    donor.setIdType(idType);
    donor.setIdNumber(idNumber);
    donor.setAddress(address);
    donor.setAddressType(addressType);
    donor.setContact(contact);
    donor.setContactMethodType(contactMethodType);
    donor.setCreatedDate(createdDate);
    return donor;
  }

  @Override
  public AbstractEntityPersister<Donor> getPersister() {
    return new DonorPersister();
  }

  public static DonorBuilder aDonor() {
    return new DonorBuilder();
  }
}
