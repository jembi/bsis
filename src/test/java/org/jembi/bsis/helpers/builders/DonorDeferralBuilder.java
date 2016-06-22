package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.DonorDeferralPersister;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.location.Location;

public class DonorDeferralBuilder extends AbstractEntityBuilder<DonorDeferral> {

  private Long id;
  private Donor deferredDonor = DonorBuilder.aDonor().build();
  private DeferralReason deferralReason = DeferralReasonBuilder.aDeferralReason().build();
  private Date deferredUntil;
  private Boolean voided;
  private String deferralReasonText;
  private Location venue = LocationBuilder.aLocation().build();
  private Date deferralDate = new Date();

  public DonorDeferralBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public DonorDeferralBuilder withDeferredDonor(Donor deferredDonor) {
    this.deferredDonor = deferredDonor;
    return this;
  }

  public DonorDeferralBuilder withDeferralReason(DeferralReason deferralReason) {
    this.deferralReason = deferralReason;
    return this;
  }

  public DonorDeferralBuilder withDeferralReasonText(String deferralReasonText) {
    this.deferralReasonText = deferralReasonText;
    return this;
  }

  public DonorDeferralBuilder withDeferredUntil(Date deferredUntil) {
    this.deferredUntil = deferredUntil;
    return this;
  }

  public DonorDeferralBuilder thatIsVoided() {
    voided = true;
    return this;
  }

  public DonorDeferralBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  public DonorDeferralBuilder withDeferralDate(Date deferralDate) {
    this.deferralDate = deferralDate;
    return this;
  }

  @Override
  public DonorDeferral build() {
    DonorDeferral donorDeferral = new DonorDeferral();
    donorDeferral.setId(id);
    donorDeferral.setDeferredDonor(deferredDonor);
    donorDeferral.setDeferralReason(deferralReason);
    donorDeferral.setDeferredUntil(deferredUntil);
    donorDeferral.setVenue(venue);
    if (voided != null) {
      donorDeferral.setIsVoided(voided);
    }
    donorDeferral.setDeferralReasonText(deferralReasonText);
    donorDeferral.setDeferralDate(deferralDate);
    return donorDeferral;
  }

  @Override
  public AbstractEntityPersister<DonorDeferral> getPersister() {
    return new DonorDeferralPersister();
  }

  public static DonorDeferralBuilder aDonorDeferral() {
    return new DonorDeferralBuilder();
  }

}
