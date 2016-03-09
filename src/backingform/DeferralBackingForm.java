package backingform;

import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import model.location.Location;
import model.user.User;
import utils.DateTimeSerialiser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DeferralBackingForm {

  @Valid
  @JsonIgnore
  private DonorDeferral deferral;

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

  public Long getDeferredDonor() {
    Donor deferredDonor = deferral.getDeferredDonor();
    if (deferredDonor == null || deferredDonor.getId() == null) {
      return null;
    }

    return deferredDonor.getId();
  }

  public void setDeferredDonor(Long deferredDonorId) {
    if (deferredDonorId == null) {
      deferral.setDeferredDonor(null);
    } else {
      Donor d = new Donor();
      d.setId(deferredDonorId);
      deferral.setDeferredDonor(d);
    }
  }

  // FIXME: getter does not match setter
  public String getDeferralReason() {
    DeferralReason deferralReason = deferral.getDeferralReason();
    if (deferralReason == null || deferralReason.getId() == null) {
      return null;
    }

    return deferralReason.getId().toString();
  }

  // FIXME: getter does not match setter. 
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

  public Date getDeferredUntil() {
     return deferral.getDeferredUntil();
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setDeferredUntil(Date deferredUntil) {
    deferral.setDeferredUntil(deferredUntil);
  }

  @JsonIgnore
  public void setPermissions(Map<String, Boolean> permissions) {
    // Ignore
  }

  public void setDonorNumber(String donorNumber) {
    if (donorNumber == null) {
      deferral.setDeferredDonor(null);
    } else {
      Donor d = new Donor();
      d.setDonorNumber(donorNumber);
    }
  }

  public void setVenue(Location venue) {
    if (venue == null || venue.getId() == null) {
      deferral.setVenue(null);
    } else {
      Location location = new Location();
      location.setId(venue.getId());
      deferral.setVenue(location);       
    }
  }

  public Location getVenue() {
    return deferral.getVenue();
  }

}
