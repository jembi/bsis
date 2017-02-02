package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.model.counselling.CounsellingStatus;

public class PostDonationCounsellingBackingFormBuilder extends AbstractBuilder<PostDonationCounsellingBackingForm> {

  private Long id;
  private Date counsellingDate;
  private CounsellingStatus counsellingStatus;
  private boolean flaggedForCounselling;
  private String notes;
  
  public PostDonationCounsellingBackingFormBuilder withId(Long id) {
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

  @Override
  public PostDonationCounsellingBackingForm build() {
    PostDonationCounsellingBackingForm form = new PostDonationCounsellingBackingForm();
    form.setId(id);
    form.setCounsellingDate(counsellingDate);
    form.setCounsellingStatus(counsellingStatus.getId());
    form.setFlaggedForCounselling(flaggedForCounselling);
    form.setNotes(notes);
    return form;
  }

  public static PostDonationCounsellingBackingFormBuilder aPostDonationCounsellingBackingForm() {
    return new PostDonationCounsellingBackingFormBuilder();
  }
}