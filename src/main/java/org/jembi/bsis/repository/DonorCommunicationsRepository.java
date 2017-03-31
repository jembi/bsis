package org.jembi.bsis.repository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonorCommunicationsRepository {

  /**
   * The Constant LOGGER.
   */
  @SuppressWarnings("unused")
  private static final Logger LOGGER = Logger.getLogger(DonorCommunicationsRepository.class);

  @PersistenceContext
  private EntityManager em;

  public List<Donor> findDonors(
      List<Location> venue, String clinicDate,
      String lastDonationFromDate, String lastDonationToDate,
      List<BloodGroup> bloodGroups, boolean anyBloodGroup, boolean noBloodGroup,
      Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor) throws ParseException {

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Donor> cq = cb.createQuery(Donor.class);
    Subquery<UUID> donorDeferral = cq.subquery(UUID.class);
    Root<Donor> root = cq.from(Donor.class);
    Root<DonorDeferral> rootdonorDeferral = donorDeferral.from(DonorDeferral.class);

    List<Predicate> venuePredicates = new ArrayList<Predicate>();
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
        donorDeferral.select(rootdonorDeferral.get("id").as(UUID.class)).where(cb.equal(rootdonorDeferral.get("deferredDonor"), root),
            cb.greaterThanOrEqualTo(rootdonorDeferral.get("deferredUntil").as(Date.class), CustomDateFormatter.parse(clinicDateToCheckdeferredDonor)));
        venuePredicates.add(cb.not(cb.exists(donorDeferral)));
      }

    }
    // If Clinic Date is not specified, Donors should not be currently deferred
    else {
      donorDeferral.select(rootdonorDeferral.get("id").as(UUID.class)).where(
          cb.equal(rootdonorDeferral.get("deferredDonor"), root),
          cb.greaterThanOrEqualTo(rootdonorDeferral.get("deferredUntil").as(Date.class), new Date()));
      venuePredicates.add(cb.not(cb.exists(donorDeferral)));
    }


    List<Predicate> bgPredicates = new ArrayList<Predicate>();

    // If anyBloodGroup is true then use all blood groups otherwise use the selected blood groups
    Collection<BloodGroup> selectedBloodGroups = anyBloodGroup ? BloodGroup.getAllBloodGroups() : bloodGroups;
    if (selectedBloodGroups != null) {
      for (BloodGroup bg : selectedBloodGroups) {
        Expression<Boolean> aboExp = cb.equal(root.<String>get("bloodAbo"), bg.getBloodAbo().toString());
        Expression<Boolean> rhExp = cb.equal(root.<String>get("bloodRh"), bg.getBloodRh().toString());
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

    venuePredicates.add(cb.or(bgPredicates.toArray(new Predicate[0])));

    venuePredicates.add(cb.equal(root.<String>get("isDeleted"), false));

    venuePredicates.add(cb.not(root.get("donorStatus").in(Arrays.asList(DonorStatus.MERGED))));

    cq.where(venuePredicates.toArray(new Predicate[0]));

    int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
    int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

    if (pagingParams.containsKey("sortColumn") && pagingParams.containsKey("sortDirection")) {
      List<Order> order = new ArrayList<Order>();
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

    List<Donor> donorResults = query.getResultList();

    return donorResults;
  }

}
