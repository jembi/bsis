package repository;

import controller.UtilController;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.donordeferral.DonorDeferral;
import model.location.Location;
import model.util.BloodGroup;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import utils.CustomDateFormatter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.*;

@Repository
@Transactional
public class DonorCommunicationsRepository {

  /**
   * The Constant LOGGER.
   */
  private static final Logger LOGGER = Logger.getLogger(DonorCommunicationsRepository.class);

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private UtilController utilController;

  public List<Donor> findDonors(
          List<Location> venue, String clinicDate,
          String lastDonationFromDate, String lastDonationToDate,
          List<BloodGroup> bloodGroups, boolean anyBloodGroup, boolean noBloodGroup,
          Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor) throws ParseException {

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Donor> cq = cb.createQuery(Donor.class);
    Subquery<Long> donorDeferral = cq.subquery(Long.class);
    Root<Donor> root = cq.from(Donor.class);
    Root<DonorDeferral> rootdonorDeferral = donorDeferral.from(DonorDeferral.class);

    List<Predicate> venuePredicates = new ArrayList<>();
    if (venue != null && !venue.isEmpty()) {
      venuePredicates.add(root.get("venue").in(venue));
    }

    if (!StringUtils.isBlank(lastDonationFromDate)) {
      venuePredicates.add(cb.greaterThanOrEqualTo(root.get("dateOfLastDonation").as(Date.class), CustomDateFormatter.parse(lastDonationFromDate)));

    }
    if (!StringUtils.isBlank(lastDonationToDate)) {
      venuePredicates.add(cb.lessThanOrEqualTo(root.get("dateOfLastDonation").as(Date.class), CustomDateFormatter.parse(lastDonationToDate)));

    }

    // If Clinic Date is specified, Donors should not be deferred on that date
    if (!StringUtils.isBlank(clinicDate)) {
      venuePredicates.add(cb.lessThanOrEqualTo(root.get("dueToDonate").as(Date.class), CustomDateFormatter.parse(clinicDate)));
      if (!StringUtils.isBlank(clinicDateToCheckdeferredDonor)) {
        donorDeferral.select(rootdonorDeferral.get("id").as(Long.class)).where(cb.equal(rootdonorDeferral.get("deferredDonor"), root),
                cb.greaterThanOrEqualTo(rootdonorDeferral.get("deferredUntil").as(Date.class), CustomDateFormatter.parse(clinicDateToCheckdeferredDonor)));
        venuePredicates.add(cb.not(cb.exists(donorDeferral)));
      }

    }
    // If Clinic Date is not specified, Donors should not be currently deferred
    else {
      donorDeferral.select(rootdonorDeferral.get("id").as(Long.class)).where(cb.equal(rootdonorDeferral.get("deferredDonor"), root),
              cb.greaterThanOrEqualTo(rootdonorDeferral.get("deferredUntil").as(Date.class), new Date()));
      venuePredicates.add(cb.not(cb.exists(donorDeferral)));
    }


    List<Predicate> bgPredicates = new ArrayList<>();

    // If anyBloodGroup is true then use all blood groups otherwise use the selected blood groups
    Collection<BloodGroup> selectedBloodGroups = anyBloodGroup ? BloodGroup.getAllBloodGroups() : bloodGroups;
    if (selectedBloodGroups != null) {
      for (BloodGroup bg : selectedBloodGroups) {
        Expression<Boolean> aboExp = cb.equal(root.<String>get("bloodAbo"), bg.getBloodAbo());
        Expression<Boolean> rhExp = cb.equal(root.<String>get("bloodRh"), bg.getBloodRh());
        bgPredicates.add(cb.and(aboExp, rhExp));
      }
    }

    // If noBloodGroup is true then include donors with no blood group
    if (noBloodGroup) {
      Expression<String> bloodAbo = root.<String>get("bloodAbo");
      Expression<String> bloodRh = root.<String>get("bloodRh");
      Expression<Boolean> aboExpression = cb.or(cb.isNull(bloodAbo), cb.equal(bloodAbo, ""));
      Expression<Boolean> rhExpression = cb.or(cb.isNull(bloodRh), cb.equal(bloodRh, ""));
      bgPredicates.add(cb.and(aboExpression, rhExpression));
    } else if (selectedBloodGroups == null || selectedBloodGroups.isEmpty()) {
      // No blood groups were selected so return early
      return Collections.emptyList();
    }

    venuePredicates.add(cb.or(bgPredicates.toArray(new Predicate[bgPredicates.size()])));

    venuePredicates.add(cb.equal(root.<String>get("isDeleted"), false));

    venuePredicates.add(cb.not(root.get("donorStatus").in(Collections.singletonList(DonorStatus.MERGED))));

    cq.where(venuePredicates.toArray(new Predicate[venuePredicates.size()]));

    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

    if (pagingParams.containsKey("sortColumn") && pagingParams.containsKey("sortDirection")) {
      List<Order> order = new ArrayList<>();
      if (pagingParams.get("sortDirection").equals("asc")) {
        order.add(cb.asc(root.<String>get((String) pagingParams.get("sortColumn"))));
      } else {
        order.add(cb.desc(root.<String>get((String) pagingParams.get("sortColumn"))));
      }
      cq.orderBy(order);
    }

    TypedQuery<Donor> query = em.createQuery(cq);
    query.setFirstResult(start);
    query.setMaxResults(length);

    CriteriaQuery<Long> countCriteriaQuery = cb.createQuery(Long.class);
    Root<Donor> countRoot = countCriteriaQuery.from(Donor.class);
    countCriteriaQuery.where(venuePredicates.toArray(new Predicate[venuePredicates.size()]));
    countCriteriaQuery.select(cb.countDistinct(countRoot));

    TypedQuery<Long> countQuery = em.createQuery(countCriteriaQuery);

    List<Donor> donorResults = query.getResultList();

    return donorResults;
  }

}
