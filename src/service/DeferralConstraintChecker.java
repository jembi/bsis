package service;

import model.donordeferral.DonorDeferral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import repository.DonorDeferralRepository;

import javax.persistence.NoResultException;
import java.util.Date;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class DeferralConstraintChecker {

  @Autowired
  private DonorDeferralRepository donorDeferralRepository;

  public boolean canEditDonorDeferral(long donorDeferralId) throws NoResultException {
    DonorDeferral donorDeferral = donorDeferralRepository.findDonorDeferralById(donorDeferralId);

    return !donorDeferral.getDeferralReason().getType().isAutomatedDeferral();

  }

  public boolean canDeleteDonorDeferral(long donorDeferralId) throws NoResultException {
    DonorDeferral donorDeferral = donorDeferralRepository.findDonorDeferralById(donorDeferralId);

    return !donorDeferral.getDeferralReason().getType().isAutomatedDeferral();

  }

  public boolean canEndDonorDeferral(long donorDeferralId) throws NoResultException {
    DonorDeferral donorDeferral = donorDeferralRepository.findDonorDeferralById(donorDeferralId);

    if (donorDeferral.getDeferralReason().getType().isAutomatedDeferral()) {
      // not possible to end an automatic deferral
      return false;
    }

    Date today = new Date();
    return !today.after(donorDeferral.getDeferredUntil());

  }

}
