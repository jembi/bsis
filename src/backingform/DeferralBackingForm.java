package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.validation.Valid;
import model.donor.Donor;
import model.donordeferral.DonorDeferral;
import model.donordeferral.DeferralReason;
import model.location.Location;
import model.user.User;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import utils.CustomDateFormatter;

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
    
    /*
    @JsonIgnore
    public Date getDeferredOn() {
        return deferral.getDeferredOn();
    }
    */
    
    public String getDeferredOn() {
    	if (deferral.getDeferredOn() == null) {
            return "";
        }
        return CustomDateFormatter.getDateString(deferral.getDeferredOn());
    }
    
    public void setDeferredOn(String deferredOn) {
    	if (deferredOn != null){
    		try {
                deferral.setDeferredOn(CustomDateFormatter.getDateFromString(deferredOn));
            } catch (ParseException ex) {
                ex.printStackTrace();
                deferral.setDeferredOn(null);
            }
    	}
    }
    
    @JsonIgnore
    public User getDeferredBy() {
        return deferral.getDeferredBy();
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
        	 Donor d = new  Donor();
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
        	 DeferralReason dr = new  DeferralReason();
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
    	if (deferredUntil != null){
    		try {
                deferral.setDeferredUntil(CustomDateFormatter.getDateFromString(deferredUntil));
            } catch (ParseException ex) {
                ex.printStackTrace();
                deferral.setDeferredUntil(null);
            }
    	}
    }

}
