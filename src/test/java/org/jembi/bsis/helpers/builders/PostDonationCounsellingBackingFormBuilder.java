package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.model.counselling.CounsellingStatus;

public class PostDonationCounsellingBackingFormBuilder extends AbstractBuilder<PostDonationCounsellingBackingForm> {

  private UUID id;
  private Date counsellingDate;
  private CounsellingStatus counsellingStatus;
  private boolean flaggedForCounselling;
  private Boolean referred;
  private String notes;
  private LocationBackingForm referralSite;
  
  public PostDonationCounsellingBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public PostDonationCounsellingBackingFormBuilder withCounsellingDate(Date counsellingDate) {
    this.counsellingDate = counsellingDate;
    return this;
  }

  public PostDonationCounsellingBackingFormBuilder withCounsellingStatus(CounsellingStatus counsellingStatus) {
    this.counsellingStatus = counsellingStatus;
    return this;
  }

  public PostDonationCounsellingBackingFormBuilder thatIsReferred() {
    this.referred = Boolean.TRUE;
    return this;
  }

  public PostDonationCounsellingBackingFormBuilder thatIsNotReferred() {
    this.referred = Boolean.FALSE;
    return this;
  }

  public PostDonationCounsellingBackingFormBuilder thatIsFlaggedForCounselling() {
    this.flaggedForCounselling = true;
    return this;
  }

  public PostDonationCounsellingBackingFormBuilder thatIsNotFlaggedForCounselling() {
    this.flaggedForCounselling = false;
    return this;
  }

  public PostDonationCounsellingBackingFormBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }
  
  public PostDonationCounsellingBackingFormBuilder withReferralSite(LocationBackingForm referralSite) {
    this.referralSite = referralSite;
    return this;
  }
  
  @Override
  public PostDonationCounsellingBackingForm build() {
    PostDonationCounsellingBackingForm form = new PostDonationCounsellingBackingForm();
    form.setId(id);
    form.setCounsellingDate(counsellingDate);
    form.setCounsellingStatus(counsellingStatus);
    form.setFlaggedForCounselling(flaggedForCounselling);
    form.setReferred(referred);
    form.setNotes(notes);
    form.setReferralSite(referralSite);
    return form;
  }

  public static PostDonationCounsellingBackingFormBuilder aPostDonationCounsellingBackingForm() {
    return new PostDonationCounsellingBackingFormBuilder();
  }
}