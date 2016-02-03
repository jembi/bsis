package repository;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import model.donor.DonorStatus;
import model.location.Location;
import viewmodel.MobileClinicLookUpDonorViewModel;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MobileClinicRepository {

/**
 * The Constant LOGGER.
*/
private static final Logger LOGGER = Logger.getLogger(MobileClinicRepository.class);
    
  @PersistenceContext
  private EntityManager em;
  
  public List<MobileClinicLookUpDonorViewModel> lookUp(Location venue) throws NoResultException {
      return em.createQuery(
              "SELECT NEW viewmodel.MobileClinicLookUpDonorViewModel(d.id, d.donorNumber, d.firstName, d.lastName, d.gender, d.birthDate, d.bloodAbo, d.bloodRh) " +
              "FROM Donor d " +
              "WHERE d.venue = :venue " +
              "AND d.isDeleted = :isDeleted " +
              "AND d.donorStatus NOT IN :excludedStatuses ",
              MobileClinicLookUpDonorViewModel.class)
              .setParameter("venue", venue)
              .setParameter("isDeleted", false)
              .setParameter("excludedStatuses", Arrays.asList(DonorStatus.MERGED))
              .getResultList();
  }
  
}

