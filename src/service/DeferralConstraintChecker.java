package service;

import java.util.Date;

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

  public boolean canEditDonorDeferral(long donorDeferralId) throws NoResultException {
    DonorDeferral donorDeferral = donorDeferralRepository.findDonorDeferralById(donorDeferralId);

    if (donorDeferral.getDeferralReason().getType().isAutomatedDeferral()) {
      // not possible to delete an automatic deferral
      return false;
    }

    return true;
  }

  public boolean canDeleteDonorDeferral(long donorDeferralId) throws NoResultException {
    DonorDeferral donorDeferral = donorDeferralRepository.findDonorDeferralById(donorDeferralId);

    if (donorDeferral.getDeferralReason().getType().isAutomatedDeferral()) {
      // not possible to delete and automatic deferral
      return false;
    }

    return true;
  }

  public boolean canEndDonorDeferral(long donorDeferralId) throws NoResultException {
    DonorDeferral donorDeferral = donorDeferralRepository.findDonorDeferralById(donorDeferralId);

    if (donorDeferral.getDeferralReason().getType().isAutomatedDeferral()) {
      // not possible to end an automatic deferral
      return false;
    }

    Date today = new Date();
    if (today.after(donorDeferral.getDeferredUntil())) {
      // not possible to end a deferral that is already over
      return false;
    }

    return true;
  }

}
