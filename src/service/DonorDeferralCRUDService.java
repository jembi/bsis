package service;

import java.util.Date;

import javax.persistence.NoResultException;

import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DonorDeferral;
import model.donordeferral.DurationType;

import org.apache.commons.lang3.StringUtils;
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
    
    @Autowired
    DeferralConstraintChecker deferralConstraintChecker;
    
    //@Autowired
    //private UtilController utilController;
    
    public DonorDeferral createDeferralForDonorWithDeferralReasonType(Donor donor, DeferralReasonType deferralReasonType) {
        LOGGER.info("Creating deferral for donor: " + donor);
        
        // Look up deferral reason
        DeferralReason deferralReason = deferralReasonRepository.findDeferralReasonByType(deferralReasonType);
        
        DonorDeferral donorDeferral = new DonorDeferral();
        donorDeferral.setDeferredDonor(donor);
        donorDeferral.setDeferralReason(deferralReason);
        donorDeferral.setIsVoided(Boolean.FALSE);
        
        if (deferralReason.getDurationType() == DurationType.PERMANENT) {
            donorDeferral.setDeferredUntil(PERMANENT_DEFERRAL_DATE);
        } else {
            Date now = dateGeneratorService.generateDate();
            Date deferredUntilDate = new DateTime(now).plusDays(deferralReason.getDefaultDuration()).toDate();
            donorDeferral.setDeferredUntil(deferredUntilDate);
        }
        
        donorDeferralRepository.save(donorDeferral);
        return donorDeferral;
    }
    
	public DonorDeferral findDeferralById(Long donorDeferralId) throws IllegalStateException, NoResultException {
		DonorDeferral donorDeferral = donorDeferralRepository.findDonorDeferralById(donorDeferralId);
		return donorDeferral;
	}
	
	public void deleteDeferral(Long donorDeferralId) throws IllegalStateException, NoResultException {
		if (!deferralConstraintChecker.canDeleteDonorDeferral(donorDeferralId)) {
			throw new IllegalStateException("Cannot delete deferral with constraints");
		}
		DonorDeferral donorDeferral = findDeferralById(donorDeferralId);
		if (donorDeferral == null) {
			throw new IllegalStateException("DonorDeferral with id " + donorDeferralId
			        + " does not exist (or has already been deleted).");
		}
		donorDeferral.setIsVoided(Boolean.TRUE);
		donorDeferral.setVoidedDate(new Date());
		//donorDeferral.setVoidedBy(utilController.getCurrentUser());
		donorDeferralRepository.save(donorDeferral);
	}
	
	public DonorDeferral updateDeferral(DonorDeferral deferral) {
		donorDeferralRepository.save(deferral);
		return deferral;
	}
	
	public DonorDeferral endDeferral(Long donorDeferralId, String comment) {
		if (!deferralConstraintChecker.canDeleteDonorDeferral(donorDeferralId)) {
			throw new IllegalStateException("Cannot end deferral with constraints");
		}
		DonorDeferral deferral = findDeferralById(donorDeferralId);
		if (deferral == null) {
			throw new IllegalStateException("DonorDeferral with id " + donorDeferralId
			        + " does not exist (or has already been deleted).");
		}
		appendComment(deferral, comment);
		deferral.setDeferredUntil(new Date());
		donorDeferralRepository.save(deferral);
		return deferral;
	}
	
	protected void appendComment(DonorDeferral deferral, String comment) {
		if (StringUtils.isEmpty(deferral.getDeferralReasonText())) {
			deferral.setDeferralReasonText(comment);
		} else {
			StringBuilder newComment = new StringBuilder(deferral.getDeferralReasonText().trim());
			if (!deferral.getDeferralReasonText().trim().endsWith(".")) {
				newComment.append(".");
			}
			newComment.append(" ");
			newComment.append(comment);
			deferral.setDeferralReasonText(newComment.toString());
		}
	}
}
