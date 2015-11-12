package service;

import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DonorDeferral;
import model.donordeferral.DurationType;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
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
    @Autowired
    private DateGeneratorService dateGeneratorService;
    
    public DonorDeferral createDeferralForDonorWithDeferralReasonType(Donor donor, DeferralReasonType deferralReasonType)
            throws NoResultException, NonUniqueResultException {

        LOGGER.info("Creating deferral for donor: " + donor);
        
        // Look up deferral reason
        DeferralReason deferralReason = deferralReasonRepository.findDeferralReasonByType(deferralReasonType);
        
        boolean permanentDeferral = deferralReason.getDurationType() == DurationType.PERMANENT;
        
        if (permanentDeferral) {

          List<DonorDeferral> donorDeferrals = donorDeferralRepository.findDonorDeferralsForDonorByDeferralReason(
              donor, deferralReason);

          if (donorDeferrals.size() > 0) {
            // The donor already has a permanent deferral of this type so return it
            return donorDeferrals.get(0);
          }
        }
        
        DonorDeferral donorDeferral = new DonorDeferral();
        donorDeferral.setDeferredDonor(donor);
        donorDeferral.setDeferralReason(deferralReason);
        donorDeferral.setIsVoided(Boolean.FALSE);
        
        if (permanentDeferral) {
            donorDeferral.setDeferredUntil(PERMANENT_DEFERRAL_DATE);
        } else {
            Date now = dateGeneratorService.generateDate();
            Date deferredUntilDate = new DateTime(now).plusDays(deferralReason.getDefaultDuration()).toDate();
            donorDeferral.setDeferredUntil(deferredUntilDate);
        }
        
        donorDeferralRepository.save(donorDeferral);
        return donorDeferral;
    }

}
