package service;

import javax.persistence.NoResultException;

import model.donordeferral.DonorDeferral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import repository.DonorDeferralRepository;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class DeferralConstraintChecker {
	
	@Autowired
	private DonorDeferralRepository donorDeferralRepository;
	
	public boolean canDeleteDonorDeferral(long donorDeferralId) throws NoResultException {
		DonorDeferral donorDeferral = donorDeferralRepository.findDonorDeferralById(donorDeferralId);
		
		if (donorDeferral.getDeferralReason().getType().isAutomatedDeferral()) {
			return false;
		}
		
		return true;
	}
	
}
