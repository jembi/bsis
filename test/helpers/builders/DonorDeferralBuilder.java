package helpers.builders;

import java.util.Date;

import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;

public class DonorDeferralBuilder extends AbstractEntityBuilder<DonorDeferral> {
    
	private Long id;
    private Donor deferredDonor;
    private DeferralReason deferralReason;
    private Date deferredUntil;
    private Date createdDate;
    private Boolean voided;
    
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
    
    public DonorDeferralBuilder withDeferredUntil(Date deferredUntil) {
        this.deferredUntil = deferredUntil;
        return this;
    }
    
    public DonorDeferralBuilder thatIsNotVoided() {
        voided = false;
        return this;
    }
    
    public DonorDeferralBuilder withCreatedDate(Date createdDate) {
    	this.createdDate = createdDate;
    	return this;
    }

    @Override
    public DonorDeferral build() {
        DonorDeferral donorDeferral = new DonorDeferral();
        donorDeferral.setId(id);
        donorDeferral.setDeferredDonor(deferredDonor);
        donorDeferral.setDeferralReason(deferralReason);
        donorDeferral.setDeferredUntil(deferredUntil);
        donorDeferral.setIsVoided(voided);
        donorDeferral.setCreatedDate(createdDate);
        return donorDeferral;
    }
    
    public static DonorDeferralBuilder aDonorDeferral() {
        return new DonorDeferralBuilder();
    }

}
