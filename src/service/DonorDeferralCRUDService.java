package service;

import java.util.Date;

import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DeferralType;
import model.donordeferral.DonorDeferral;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.DeferralReasonRepository;
import repository.DonorDeferralRepository;

@Transactional
@Service
public class DonorDeferralCRUDService {
    
    private static final Logger LOGGER = Logger.getLogger(DonorDeferralCRUDService.class);
    
    public static final Date PERMANENT_DEFERRAL_DATE = new Date(4102444800000L); // 2100-01-01
    
    @Autowired
    private DonorDeferralRepository donorDeferralRepository;
    @Autowired
    private DeferralReasonRepository deferralReasonRepository;
    
    public DonorDeferral createAutomatedUnsafeDeferralForDonor(Donor donor) {
        LOGGER.info("Creating automated unsafe deferral for donor: " + donor);
        
        DonorDeferral donorDeferral = new DonorDeferral();
        donorDeferral.setDeferralType(DeferralType.PERMANENT);
        donorDeferral.setDeferredUntil(PERMANENT_DEFERRAL_DATE);
        donorDeferral.setDeferredDonor(donor);
        donorDeferral.setIsVoided(Boolean.FALSE);
        
        // Set deferral reason
        DeferralReason deferralReason = deferralReasonRepository.findDeferralReasonByType(
                DeferralReasonType.AUTOMATED_TTI_UNSAFE);
        donorDeferral.setDeferralReason(deferralReason);
        
        donorDeferralRepository.save(donorDeferral);
        return donorDeferral;
    }

}
