package backingform;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import model.location.Location;
import model.user.User;
import utils.CustomDateFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DeferralBackingForm {

  @Valid
  @JsonIgnore
  private DonorDeferral deferral;
  private Long venueId;

  public DeferralBackingForm() {
    deferral = new DonorDeferral();
  }

  public DeferralBackingForm(DonorDeferral deferral) {
    this.deferral = deferral;
  }

  public DonorDeferral getDonorDeferral() {
    return deferral;
  }

  public boolean equals(Object obj) {
    return deferral.equals(obj);
  }

  public Long getId() {
    return deferral.getId();
  }

  public void setId(Long id) {
    deferral.setId(id);
  }

  @JsonIgnore
  public Date getLastUpdated() {
    return deferral.getLastUpdated();
  }

  @JsonIgnore
  public Date getCreatedDate() {
    return deferral.getCreatedDate();
  }

  @JsonIgnore
  public User getCreatedBy() {
    return deferral.getCreatedBy();
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return deferral.getLastUpdatedBy();
  }

  @JsonIgnore
  public User getVoidedBy() {
    return deferral.getVoidedBy();
  }

  @JsonIgnore
  public Date getVoidedDate() {
    return deferral.getVoidedDate();
  }

  public String getDeferredDonor() {
    Donor deferredDonor = deferral.getDeferredDonor();
    if (deferredDonor == null || deferredDonor.getId() == null) {
      return null;
    }

    return deferredDonor.getId().toString();
  }

  public void setDeferredDonor(String deferredDonorId) {
    if (deferredDonorId == null) {
      deferral.setDeferredDonor(null);
    } else {
      Donor d = new Donor();
      try {
        d.setId(Long.parseLong(deferredDonorId));
        deferral.setDeferredDonor(d);
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
        deferral.setDeferredDonor(null);
      }
    }
  }

  public String getDeferralReason() {
    DeferralReason deferralReason = deferral.getDeferralReason();
    if (deferralReason == null || deferralReason.getId() == null) {
      return null;
    }

    return deferralReason.getId().toString();
  }

  public void setDeferralReason(DeferralReason deferralReason) {
    if (deferralReason == null || deferralReason.getId() == null) {
      deferral.setDeferralReason(null);
    } else {
      DeferralReason dr = new DeferralReason();
      try {
        dr.setId(deferralReason.getId());
        deferral.setDeferralReason(dr);
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
        deferral.setDeferralReason(null);
      }
    }
  }

  public String getDeferralReasonText() {
    return deferral.getDeferralReasonText();
  }

  public void setDeferralReasonText(String deferralReasonText) {
    deferral.setDeferralReasonText(deferralReasonText);
  }

  public String getDeferredUntil() {
    if (deferral.getDeferredUntil() == null) {
      return "";
    }
    return CustomDateFormatter.getDateString(deferral.getDeferredUntil());
  }

  public void setDeferredUntil(String deferredUntil) {
    if (deferredUntil != null) {
      try {
        deferral.setDeferredUntil(CustomDateFormatter.getDateFromString(deferredUntil));
      } catch (ParseException ex) {
        ex.printStackTrace();
        deferral.setDeferredUntil(null);
      }
    }
  }

  @JsonIgnore
  public void setPermissions(Map<String, Boolean> permissions) {
    // Ignore
  }

  public void setDonorNumber(String donorNumber) {
    setDeferredDonor(donorNumber);
  }

  public void setVenueId(Long venueId) {
    this.venueId = venueId;
    Location venue = new Location();
    venue.setId(venueId);
    deferral.setVenue(venue);
  }

  public Long getVenueId() {
    return venueId;
  }
}
