package repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import model.donor.Donor;
import model.donordeferral.DonorDeferral;
import model.location.Location;
import model.util.BloodGroup;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import controller.UtilController;

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
  			
  public List<Object> findDonorFromDonorCommunication(
			List<Location> donorPanel, String clinicDate,
			String lastDonationFromDate, String lastDonationToDate,
			List<BloodGroup> bloodGroups, String anyBloodGroup,
			Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Donor> cq = cb.createQuery(Donor.class);
		Subquery<Long> donorDefferel =   cq.subquery(Long.class);
		Root<Donor> root = cq.from(Donor.class);
		Root<DonorDeferral> rootdonorDefferel = donorDefferel.from(DonorDeferral.class);		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		List<Predicate> panelPredicates = new ArrayList<Predicate>();
		if(donorPanel != null  && !donorPanel.isEmpty()) {
		panelPredicates.add(root.get("donorPanel").in(donorPanel));
		}
		if(!StringUtils.isBlank(clinicDate)) {
			try {
				panelPredicates.add(cb.lessThanOrEqualTo(root.get("dateOfLastDonation").as(Date.class), dateFormat.parse(clinicDate)));				
				if(!StringUtils.isBlank(clinicDateToCheckdeferredDonor)) {					
					donorDefferel.select(rootdonorDefferel.get("id").as(Long.class)).where(cb.equal(rootdonorDefferel.get("deferredDonor"), root), 
					cb.lessThanOrEqualTo(rootdonorDefferel.get("deferredOn").as(Date.class), dateFormat.parse(clinicDateToCheckdeferredDonor)),
					cb.greaterThanOrEqualTo(rootdonorDefferel.get("deferredUntil").as(Date.class), dateFormat.parse(clinicDateToCheckdeferredDonor) ));
					panelPredicates.add(cb.not(cb.exists(donorDefferel)));
				}
			} catch (ParseException e) {
				LOGGER.debug("DonorCommunicationsRepository:findDonorFromDonorCommunication: clinicDate:ParseException"+e);
			}
		}
		if(!StringUtils.isBlank(lastDonationFromDate)) {
			try {
				panelPredicates.add(cb.greaterThanOrEqualTo(root.get("dateOfLastDonation").as(Date.class), dateFormat.parse(lastDonationFromDate)));
				donorDefferel.select(rootdonorDefferel.get("id").as(Long.class)).where(cb.equal(rootdonorDefferel.get("deferredDonor"), root), 
				cb.greaterThanOrEqualTo(rootdonorDefferel.get("deferredUntil").as(Date.class), dateFormat.parse(lastDonationFromDate) ));
				panelPredicates.add(cb.not(cb.exists(donorDefferel)));
			} catch (ParseException e) {
				LOGGER.debug("DonorCommunicationsRepository:findDonorFromDonorCommunication:lastDonationFromDate:ParseException"+e);
			}			
		}
		if(!StringUtils.isBlank(lastDonationToDate)) {
			try {
				panelPredicates.add(cb.lessThanOrEqualTo(root.get("dateOfLastDonation").as(Date.class), dateFormat.parse(lastDonationToDate)));
				donorDefferel.select(rootdonorDefferel.get("id").as(Long.class)).where(cb.equal(rootdonorDefferel.get("deferredDonor"), root), 
				cb.lessThanOrEqualTo(rootdonorDefferel.get("deferredUntil").as(Date.class), dateFormat.parse(lastDonationToDate) ));
				panelPredicates.add(cb.not(cb.exists(donorDefferel)));
			} catch (ParseException e) {
				LOGGER.debug("DonorCommunicationsRepository:findDonorFromDonorCommunication:lastDonationToDate:ParseException"+e);
			}			
		}		
	   if(bloodGroups != null && !bloodGroups.isEmpty() &&  !anyBloodGroup.equals("true")){
	      List<Predicate> bgPredicates = new ArrayList<Predicate>();
	      for (BloodGroup bg : bloodGroups) {
	        Expression<Boolean> aboExp = cb.equal(root.<String>get("bloodAbo"), bg.getBloodAbo().toString());
	        Expression<Boolean> rhExp = cb.equal(root.<String>get("bloodRh"), bg.getBloodRh().toString());
	        bgPredicates.add(cb.and(aboExp, rhExp));
	      }
	      panelPredicates.add(cb.or(bgPredicates.toArray(new Predicate[0])));
	   }
	    panelPredicates.add(cb.equal(root.<String> get("isDeleted"), false));
		cq.where(panelPredicates.toArray(new Predicate[0]));

		int start = ((pagingParams.get("start") != null) ? Integer.parseInt(pagingParams.get("start").toString()) : 0);
		int length = ((pagingParams.get("length") != null) ? Integer.parseInt(pagingParams.get("length").toString()) : Integer.MAX_VALUE);

		if (pagingParams.containsKey("sortColumn") && pagingParams.containsKey("sortDirection")) {
			List<Order> order = new ArrayList<Order>();
			if (pagingParams.get("sortDirection").equals("asc")) {
				order.add(cb.asc(root.<String> get((String) pagingParams.get("sortColumn"))));
			} else {
				order.add(cb.desc(root.<String> get((String) pagingParams.get("sortColumn"))));
			}
			cq.orderBy(order);
		}

		TypedQuery<Donor> query = em.createQuery(cq);
		query.setFirstResult(start);
		query.setMaxResults(length);

		CriteriaQuery<Long> countCriteriaQuery = cb.createQuery(Long.class);
		Root<Donor> countRoot = countCriteriaQuery.from(Donor.class);
		countCriteriaQuery.where(panelPredicates.toArray(new Predicate[0]));
		countCriteriaQuery.select(cb.countDistinct(countRoot));
		
		TypedQuery<Long> countQuery = em.createQuery(countCriteriaQuery);
		Long totalResults = countQuery.getSingleResult().longValue();
		return Arrays.asList(query.getResultList(), totalResults);
	}

}
