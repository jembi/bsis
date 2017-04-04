package org.jembi.bsis.model.donor;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.jembi.bsis.model.BaseModificationTrackerEntity;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.model.address.Address;
import org.jembi.bsis.model.address.AddressType;
import org.jembi.bsis.model.address.Contact;
import org.jembi.bsis.model.address.ContactMethodType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.idtype.IdType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.preferredlanguage.PreferredLanguage;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.DonorNamedQueryConstants;
import org.jembi.bsis.utils.DonorUtils;

@NamedQueries({
  @NamedQuery(name = DonorNamedQueryConstants.NAME_GET_ALL_DUPLICATE_DONORS,
      query = DonorNamedQueryConstants.QUERY_GET_ALL_DUPLICATE_DONORS),
  @NamedQuery(name = DonorNamedQueryConstants.NAME_GET_DUPLICATE_DONORS,
      query = DonorNamedQueryConstants.QUERY_GET_DUPLICATE_DONORS),
  @NamedQuery(name = DonorNamedQueryConstants.NAME_MOBILE_CLINIC_LOOKUP,
      query = DonorNamedQueryConstants.QUERY_MOBILE_CLINIC_LOOKUP),
  @NamedQuery(name = DonorNamedQueryConstants.NAME_FIND_DONOR_BY_DONOR_NUMBER,
      query = DonorNamedQueryConstants.QUERY_FIND_DONOR_BY_DONOR_NUMBER),
  @NamedQuery(name = DonorNamedQueryConstants.NAME_FIND_DONOR_BY_DONATION_IDENTIFICATION_NUMBER,
      query = DonorNamedQueryConstants.QUERY_FIND_DONOR_BY_DONATION_IDENTIFICATION_NUMBER),
  @NamedQuery(name = DonorNamedQueryConstants.NAME_COUNT_DONOR_WITH_ID,
      query = DonorNamedQueryConstants.QUERY_COUNT_DONOR_WITH_ID),
  @NamedQuery(name = DonorNamedQueryConstants.NAME_FIND_DONORS_FOR_EXPORT,
      query = DonorNamedQueryConstants.QUERY_FIND_DONORS_FOR_EXPORT),
  @NamedQuery(name = DonorNamedQueryConstants.NAME_FIND_MOBILE_CLINIC_DONORS_BY_VENUES,
      query = DonorNamedQueryConstants.QUERY_FIND_MOBILE_CLINIC_DONORS_BY_VENUES)
})
@Entity
@Audited
public class Donor extends BaseModificationTrackerUUIDEntity {

  private static final long serialVersionUID = 1L;

  /**
   * Donor Number column should be indexed. It has high selectivity. Search by donor number is very
   * common usecase. VARCHAR(15) should be sufficient. Smaller index keys are better. In most cases,
   * this field will be auto-generated.
   */
  @Column(length = 20, unique = true)
  @Index(name = "donor_donorNumber_index")
  @Length(max = 20)
  private String donorNumber;

  /**
   * Just store the string for the Title. Low selectivity column. No need to index it.
   */
  @Column(length = 15)
  private String title;

  /**
   * Find donor by first few characters of first name can be made faster with this index.
   */
  @Column(length = 20)
  @Index(name = "donor_firstName_index")
  @Length(max = 20)
  private String firstName;

  @Length(max = 20)
  @Column(length = 20)
  private String middleName;

  /**
   * Find donor by first few characters of last name can be made faster with this index.
   */
  @Length(max = 20)
  @Index(name = "donor_lastName_index")
  @Column(length = 20)
  private String lastName;

  /**
   * Some people prefer to be called by a different name. If required this field can be used.
   */
  @Length(max = 20)
  @Column(length = 20)
  private String callingName;

  /**
   * Just store the string for the gender. Low selectivity column. No need to index it.
   */
  @Enumerated(EnumType.STRING)
  @Column(length = 15)
  private Gender gender;

  /**
   * TODO: Not sure if an index will help here.
   */
  @Column(length = 10)
  private String bloodAbo;

  @Column(length = 10)
  private String bloodRh;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private DonorStatus donorStatus = DonorStatus.NORMAL;

  /**
   * Do not see a need to search by birthdate so need not add an index here.
   */
  @Temporal(TemporalType.DATE)
  private Date birthDate;

  @Temporal(TemporalType.DATE)
  private Date birthDateInferred;

  private Boolean birthDateEstimated;

  /**
   * TODO: Not sure if age is used anymore
   */
  @Column(columnDefinition = "TINYINT")
  private Integer age;

  @Column(length = 50)
  private String donorHash;

  /**
   * Which venue the donor is registered to
   */
  @ManyToOne
  private Location venue;

  @Lob
  private String notes;

  @Temporal(TemporalType.TIMESTAMP)
  private Date dateOfLastDonation;

  /**
   * Never delete the rows. Just mark them as deleted.
   */
  private Boolean isDeleted = Boolean.FALSE;

  @OneToMany(mappedBy = "donor")
  @Where(clause = "isDeleted = 0")
  private List<Donation> donations;

  /**
   * If a donor has been deferred then we can disallow him to donate the next time.
   */
  @OneToMany(mappedBy = "deferredDonor")
  @Where(clause = "isVoided = 0")
  private List<DonorDeferral> deferrals;

  @Audited(targetAuditMode = NOT_AUDITED)
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Address address;

  @Audited(targetAuditMode = NOT_AUDITED)
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Contact contact;

  @Audited(targetAuditMode = NOT_AUDITED)
  @ManyToOne(fetch = FetchType.EAGER)
  private AddressType addressType;

  @Audited(targetAuditMode = NOT_AUDITED)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = true)
  private ContactMethodType contactMethodType;

  @Audited(targetAuditMode = NOT_AUDITED)
  @ManyToOne(fetch = FetchType.EAGER)
  private IdType idType;

  private String idNumber;

  /**
   * Date of first donation
   */
  @Temporal(TemporalType.DATE)
  private Date dateOfFirstDonation;

  @Audited(targetAuditMode = NOT_AUDITED)
  @ManyToOne(fetch = FetchType.EAGER)
  private PreferredLanguage preferredLanguage;

  private Date dueToDonate;

  public Donor() {
    super();
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public Gender getGender() {
    return gender;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public void setFirstName(String firstName) {
    if (firstName != null)
      firstName = WordUtils.capitalize(firstName);
    this.firstName = firstName;
  }

  public void setMiddleName(String middleName) {
    if (middleName != null)
      middleName = WordUtils.capitalize(middleName);
    this.middleName = middleName;
  }

  public void setLastName(String lastName) {
    if (lastName != null)
      lastName = WordUtils.capitalize(lastName);
    this.lastName = lastName;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }


  public void copy(Donor donor) {
    assert (donor.getId().equals(this.getId()));
    setTitle(donor.getTitle());
    setFirstName(donor.getFirstName());
    setMiddleName(donor.getMiddleName());
    setLastName(donor.getLastName());
    setCallingName(donor.getCallingName());
    setBirthDate(donor.getBirthDate());
    setBirthDateInferred(donor.getBirthDateInferred());
    setBirthDateEstimated(donor.getBirthDateEstimated());
    setNotes(donor.getNotes());
    setGender(donor.getGender());
    setVenue(donor.getVenue());
    setPreferredLanguage(donor.getPreferredLanguage());
    setBloodAbo(donor.getBloodAbo());
    setBloodRh(donor.getBloodRh());
    this.donorHash = DonorUtils.computeDonorHash(this);
    setDateOfFirstDonation(donor.getDateOfFirstDonation());
    setIdType(donor.getIdType());
    setAddressType(donor.getAddressType());
    setContactMethodType(donor.getContactMethodType());
    setIdNumber(donor.getIdNumber());
    setContact(donor.getContact());
    setAddress(donor.getAddress());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("hashCode", Integer.toHexString(hashCode()))
        .append("id", getId())
        .build();
  }

  public List<Donation> getDonations() {
    return donations;
  }

  public void setDonations(List<Donation> donations) {
    this.donations = donations;
  }

  public String getCallingName() {
    return callingName;
  }

  public void setCallingName(String callingName) {
    this.callingName = callingName;
  }

  public List<DonorDeferral> getDeferrals() {
    return deferrals;
  }

  public void setDeferrals(List<DonorDeferral> deferrals) {
    this.deferrals = deferrals;
  }

  public Date getBirthDateInferred() {
    return birthDateInferred;
  }

  public void setBirthDateInferred(Date birthDateInferred) {
    this.birthDateInferred = birthDateInferred;
  }

  public Location getVenue() {
    return venue;
  }

  public void setVenue(Location venue) {
    this.venue = venue;
  }

  public Date getDateOfLastDonation() {
    return dateOfLastDonation;
  }

  public void setDateOfLastDonation(Date dateOfLastDonation) {
    this.dateOfLastDonation = dateOfLastDonation;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public DonorStatus getDonorStatus() {
    return donorStatus;
  }

  public void setDonorStatus(DonorStatus donorStatus) {
    this.donorStatus = donorStatus;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getDonorHash() {
    return donorHash;
  }

  public void setDonorHash(String donorHash) {
    this.donorHash = donorHash;
  }

  public Date getDateOfFirstDonation() {
    return dateOfFirstDonation;
  }

  public void setDateOfFirstDonation(Date dateOfFirstDonation) {
    this.dateOfFirstDonation = dateOfFirstDonation;
  }

  public Boolean getBirthDateEstimated() {
    return birthDateEstimated;
  }

  public void setBirthDateEstimated(Boolean birthDateEstimated) {
    this.birthDateEstimated = birthDateEstimated;
  }

  public PreferredLanguage getPreferredLanguage() {
    return preferredLanguage;
  }

  public void setPreferredLanguage(PreferredLanguage preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address addresss) {
    this.address = addresss;
  }

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public IdType getIdType() {
    return idType;
  }

  public void setIdType(IdType idType) {
    this.idType = idType;
  }

  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  public AddressType getAddressType() {
    return addressType;
  }

  public void setAddressType(AddressType addressType) {
    this.addressType = addressType;
  }

  public ContactMethodType getContactMethodType() {
    return contactMethodType;
  }

  public void setContactMethodType(ContactMethodType contactMethodType) {
    this.contactMethodType = contactMethodType;
  }

  public Date getDueToDonate() {
    return dueToDonate;
  }

  public void setDueToDonate(Date dueToDonate) {
    this.dueToDonate = dueToDonate;
  }
}
